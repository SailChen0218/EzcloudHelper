package cn.zjcdjk.ezcloud.plugin.helper.action;

import cn.zjcdjk.ezcloud.plugin.helper.VirtualFileInfo;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class GotoHandlerAction extends AnAction {

    private VirtualFileInfo virtualFileInfo;

    public GotoHandlerAction() {
    }

    public GotoHandlerAction(VirtualFileInfo virtualFileInfo, @Nls(capitalization = Nls.Capitalization.Sentence) @Nullable String description, @Nullable Icon icon) {
        super(virtualFileInfo.getTooltip(), description, icon);
        this.virtualFileInfo = virtualFileInfo;
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getProject();
        VirtualFile virtualFile = this.virtualFileInfo.getVirtualFile();
        int lineNumber = this.virtualFileInfo.getLineNumber();

        //打开xml文件
        OpenFileDescriptor openFileDescriptor = new OpenFileDescriptor(project, virtualFile);
        Editor editor = FileEditorManager.getInstance(project).openTextEditor(openFileDescriptor, true);

        //定位到对应的sql
        CaretModel caretModel = editor.getCaretModel();
        LogicalPosition logicalPosition = caretModel.getLogicalPosition();
        logicalPosition.leanForward(true);
        LogicalPosition logical = new LogicalPosition(lineNumber, logicalPosition.column);
        caretModel.moveToLogicalPosition(logical);
        SelectionModel selectionModel = editor.getSelectionModel();
        selectionModel.selectLineAtCaret();
    }
}
