package zk.demo;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.zkoss.bind.BindUtils;
import org.zkoss.image.Image;
import org.zkoss.image.Images;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zkmax.zul.Signature;
import org.zkoss.zul.Button;
import org.zkoss.zul.Window;

public class SignatureComposer extends SelectorComposer<Window> {
    @Wire
    private Signature sig;
    @Wire
    private Button btnSave;
    private String fileName;
    private String timestamp;

    @Override
    public void doAfterCompose(Window comp) throws Exception {
        super.doAfterCompose(comp);
        final Map<?, ?> currentArg = Executions.getCurrent().getArg();
        fileName = (String) currentArg.get("fileName");
        timestamp = (String) currentArg.get("timestamp");
        btnSave.focus();
    }

    @Listen("onClick = #btnClear")
    public void clearSignature() {
        sig.clear();
    }

    @Listen("onClick = #btnSave")
    public void save() {
        sig.save();
    }

    @Listen("onClick = #btnCancel")
    public void cancel() {
        getSelf().detach();
    }

    @Listen("onSave = #sig")
    public void saveSignature(UploadEvent e) throws IOException {
        Map<String, Object> args = new HashMap<>();
        args.put("file", fileName);
        args.put("signature", addTimestampWatermark((Image) e.getMedia(), timestamp));

        BindUtils.postGlobalCommand(null, null, "saveSignature", args);
        Clients.showNotification("Saved.", "info", null, "middle_center", 3000);
        getSelf().detach();
    }

    private Image addTimestampWatermark(Image img, String timestamp) throws IOException {
        java.awt.Image awtImg = img.toImageIcon().getImage();
        BufferedImage bi = new BufferedImage(
            awtImg.getWidth(null), awtImg.getHeight(null), BufferedImage.TYPE_INT_RGB);
        Graphics2D g = bi.createGraphics();
        FontMetrics fm = g.getFontMetrics();
        g.drawImage(awtImg, null, null);
        g.setColor(Color.BLACK);
        g.drawString(timestamp, 0, fm.getAscent());
        g.dispose();
        return Images.encode("signature.png", bi);
    }
}