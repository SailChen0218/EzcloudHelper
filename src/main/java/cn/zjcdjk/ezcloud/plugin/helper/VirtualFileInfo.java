package cn.zjcdjk.ezcloud.plugin.helper;

import com.intellij.openapi.vfs.VirtualFile;

public class VirtualFileInfo {
    private String tooltip;
    private VirtualFile virtualFile;
    private int lineNumber;

    public String getTooltip() {
        return tooltip;
    }

    public void setTooltip(String tooltip) {
        this.tooltip = tooltip;
    }

    public VirtualFile getVirtualFile() {
        return virtualFile;
    }

    public void setVirtualFile(VirtualFile virtualFile) {
        this.virtualFile = virtualFile;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }
}
