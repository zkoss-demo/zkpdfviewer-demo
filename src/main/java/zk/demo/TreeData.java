package zk.demo;

public class TreeData {
    private String iconSclass;
    private String label;

    public TreeData(String label) {
        this.label = label;
        this.iconSclass = "z-icon-folder-o";
    }

    public TreeData(String label, String iconSclass) {
        this.label = label;
        this.iconSclass = iconSclass;
    }

    public String getLabel() {
        return label;
    }

    public String getIconSclass() {
        return iconSclass;
    }
}