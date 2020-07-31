package cn.zjcdjk.ezcloud.plugin.helper;

import cn.zjcdjk.ezcloud.plugin.helper.locator.CommandInfo;
import cn.zjcdjk.ezcloud.plugin.helper.locator.HandlerLocator;
import cn.zjcdjk.ezcloud.plugin.helper.locator.MethodType;
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo;
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider;
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder;
import com.intellij.openapi.editor.markup.GutterIconRenderer;
import com.intellij.openapi.util.IconLoader;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicBoolean;

public class InjectionLineMarkerProvider extends RelatedItemLineMarkerProvider {
    private static final AtomicBoolean isPackageScanDone = new AtomicBoolean(false);
    private static final String ICON_PATH = "/icon/magnifyingGlass.png";

    protected void collectNavigationMarkers(@NotNull PsiElement psiElement,
                                            Collection<? super RelatedItemLineMarkerInfo> result) {
        if (psiElement == null) return;
        if (isPackageScanDone.compareAndSet(false, true)) {
            HandlerLocator.packageScan("cn.zjcdjk", psiElement.getProject());
        }

        if (psiElement instanceof PsiMethod) {
            CommandInfo commandInfo = HandlerLocator.recordMethod((PsiMethod) psiElement);
            if (commandInfo != null) {
                if (commandInfo.getMethodType() == MethodType.Mapping) {
                    createMappingMarker(commandInfo.getCommandName(), psiElement, result);
                } else {
                    createHandlerMarker(commandInfo.getCommandName(), psiElement, result);
                }
            }
        }
    }

    private void createMappingMarker(String commandName, @NotNull PsiElement psiElement,
                                     Collection<? super RelatedItemLineMarkerInfo> result) {
        PsiMethod psiMethod = HandlerLocator.findMappingMethod(commandName, psiElement);
        if (psiMethod != null) {
            Icon icon = IconLoader.getIcon(ICON_PATH);
            NavigationGutterIconBuilder<PsiElement> builder = NavigationGutterIconBuilder.create(icon)
                    .setAlignment(GutterIconRenderer.Alignment.CENTER).setTarget(psiMethod)
                    .setTooltipTitle("Handler");
            result.add(builder.createLineMarkerInfo(psiElement));
        }
    }

    private void createHandlerMarker(String commandName, @NotNull PsiElement psiElement,
                                     Collection<? super RelatedItemLineMarkerInfo> result) {
        PsiMethod psiMethod = HandlerLocator.findHandlerMethod(commandName, psiElement);
        if (psiMethod != null) {
            Icon icon = IconLoader.getIcon(ICON_PATH);
            NavigationGutterIconBuilder<PsiElement> builder = NavigationGutterIconBuilder.create(icon)
                    .setAlignment(GutterIconRenderer.Alignment.CENTER).setTarget(psiMethod)
                    .setTooltipTitle("Mapping");
            result.add(builder.createLineMarkerInfo(psiElement));
        }
    }
}

