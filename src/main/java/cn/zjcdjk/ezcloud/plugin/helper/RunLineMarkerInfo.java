package cn.zjcdjk.ezcloud.plugin.helper;

import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.editor.markup.GutterIconRenderer;
import com.intellij.openapi.editor.markup.MarkupEditorFilter;
import com.intellij.openapi.editor.markup.MarkupEditorFilterFactory;
import com.intellij.psi.PsiElement;
import com.intellij.util.Function;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class RunLineMarkerInfo extends LineMarkerInfo<PsiElement> {
    private final DefaultActionGroup defaultActionGroup;

    RunLineMarkerInfo(PsiElement element, Icon icon, Function<PsiElement, String> tooltipProvider,
                      DefaultActionGroup actionGroup) {
        super(element, element.getTextRange(), icon, tooltipProvider, null, GutterIconRenderer.Alignment.CENTER);
        defaultActionGroup = actionGroup;
    }

    @Override
    public GutterIconRenderer createGutterRenderer() {
        return new LineMarkerGutterIconRenderer<PsiElement>(this) {
            @Override
            public AnAction getClickAction() {
                return null;
            }

            @Override
            public boolean isNavigateAction() {
                return true;
            }

            @Override
            public ActionGroup getPopupMenuActions() {
                return defaultActionGroup;
            }
        };
    }

    @NotNull
    @Override
    public MarkupEditorFilter getEditorFilter() {
        return MarkupEditorFilterFactory.createIsNotDiffFilter();
    }
}
