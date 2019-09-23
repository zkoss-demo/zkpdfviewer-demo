package zk.demo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.image.Image;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.DefaultTreeModel;
import org.zkoss.zul.DefaultTreeNode;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelSet;
import org.zkoss.zul.TreeModel;
import org.zkoss.zul.TreeNode;
import org.zkoss.zul.Window;

public class PdfViewerDemoVM {
    private final DefaultTreeModel<TreeData> fileTreeModel;
    private final ListModelSet<String> openFileModel;
    private final Map<String, Image> signatures;

    public PdfViewerDemoVM() {
        TreeNode<TreeData> root = treeNode("root",
                treeNode("Recruit",
                        treeNode("Resume.pdf")),
                treeNode("Marketing",
                        treeNode("Catalogs.pdf")),
                treeNode("Financial",
                        treeNode("Bill.pdf"))
        );
        fileTreeModel = new DefaultTreeModel<>(root);
        openFileModel = new ListModelSet<>();
        signatures = new HashMap<>();
    }

    public TreeModel<TreeNode<TreeData>> getFileTreeModel() {
        return fileTreeModel;
    }

    public ListModel<String> getOpenFileModel() {
        return openFileModel;
    }

    public Map<String, Image> getSignatures() {
        return signatures;
    }

    private TreeNode<TreeData> treeNode(String data) {
        return new DefaultTreeNode<>(new TreeData(data, "z-icon-file-pdf-o"));
    }

    @SafeVarargs
    private final TreeNode<TreeData> treeNode(String data, TreeNode<TreeData>... children) {
        return new DefaultTreeNode<>(new TreeData(data), Arrays.asList(children));
    }

    @Command
    public void openPdfFile(@BindingParam("file") String file) {
        if (file.endsWith(".pdf")) {
            openFileModel.add(file);
            openFileModel.addToSelection(file);
        }
    }

    @Command
    public void closePdfFile(@BindingParam("file") String file) {
        openFileModel.remove(file);
    }

    @Command
    public void addSignature(@BindingParam("file") String file) {
        Map<String, String> args = new HashMap<>();
        args.put("fileName", file);
        args.put("timestamp", LocalDateTime.now().format(
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        ));
        Window window = (Window) Executions.createComponents("signature.zul", null, args);
        window.doModal();
    }

    @GlobalCommand
    @NotifyChange("signatures")
    public void saveSignature(@BindingParam("file") String file,
                              @BindingParam("timestamp") String timestamp,
                              @BindingParam("signature") Image signature) {
        signatures.put(file, signature);
    }
}