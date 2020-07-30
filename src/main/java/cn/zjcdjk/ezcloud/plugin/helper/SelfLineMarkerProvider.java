package cn.zjcdjk.ezcloud.plugin.helper;

import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.codeInsight.daemon.LineMarkerProviderDescriptor;
import com.intellij.psi.*;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;

public class SelfLineMarkerProvider extends LineMarkerProviderDescriptor {
    private static final String QUERY_MAPPING = "cn.zjcdjk.plugin.annotation.QueryMapping";
    private static final String COMMAND_MAPPING = "cn.zjcdjk.plugin.annotation.CommandMapping";
    private static final String QUERY_HANDLER = "cn.zjcdjk.plugin.annotation.QueryHandler";
    private static final String COMMAND_HANDLER_CONTEXT = "cn.zjcdjk.plugin.handler.CommandHandlerContext";
    private static final String EXECUTE_METHOD = "execute";

    @Nullable
    public LineMarkerInfo getLineMarkerInfo(@NotNull PsiElement psiElement) {
//        System.out.println(psiElement.getClass().getName() + "|" + psiElement.getText());
        if (psiElement instanceof PsiMethod) {
            PsiMethod psiMethod = (PsiMethod) psiElement;
            PsiAnnotation queryMappingAnnotation =
                    psiMethod.getAnnotation(QUERY_MAPPING);
            if (queryMappingAnnotation != null) {
                ElementInfo elementInfo = ElementInfo.createElementInfo(psiElement, ElementType.QueryMapping);
                ElementHolder.put(elementInfo.getName(), elementInfo);
                return elementInfo.createQueryHandlerLineMarkerInfo(psiMethod, elementInfo);
            }

            PsiAnnotation commandMappingAnnotation = psiMethod.getAnnotation(COMMAND_MAPPING);
            if (commandMappingAnnotation != null) {
                ElementInfo elementInfo = ElementInfo.createElementInfo(psiElement, ElementType.CommandMapping);
                ElementHolder.put(elementInfo.getName(), elementInfo);
                return elementInfo.createCommandHandlerLineMarkerInfo(psiMethod, elementInfo);
            }

            PsiAnnotation handlerAnnotation = psiMethod.getAnnotation(QUERY_HANDLER);
            if (handlerAnnotation != null) {
                ElementInfo elementInfo = ElementInfo.createElementInfo(psiElement, ElementType.QueryHandler);
                ElementHolder.put(elementInfo.getName(), elementInfo);
                return elementInfo.createMappingLineMarkerInfo(psiMethod, elementInfo);
            }

            if (EXECUTE_METHOD.equals(psiMethod.getName())) {
                PsiParameterList psiParameterList = psiMethod.getParameterList();
                if (!psiParameterList.isEmpty() ) {
                    PsiParameter[] psiParameter = psiParameterList.getParameters();
                    if (psiParameter.length == 2) {
                        String contextParameterName = psiParameter[0].getType().getCanonicalText();
                        if (COMMAND_HANDLER_CONTEXT.equals(contextParameterName)) {
                            String cmdHandlerName = psiElement.getContainingFile().getName();
                            cmdHandlerName = cmdHandlerName.substring(0, cmdHandlerName.length() - 5);
                            String cmdName = psiParameter[1].getType().getCanonicalText();
                            ElementInfo elementInfo = ElementInfo.createElementInfo(psiElement,
                                    ElementType.CommandHandler, cmdHandlerName, cmdName);
                            ElementHolder.put(elementInfo.getName(), elementInfo);
                            return elementInfo.createMappingLineMarkerInfo(psiMethod, elementInfo);
                        }
                    }
                }
            }
        }
        return null;
    }

    @Override
    public void collectSlowLineMarkers(@NotNull List<PsiElement> elements, @NotNull Collection<LineMarkerInfo> result) {
    }

    @Override
    public @Nullable("null means disabled") @Nls(capitalization = Nls.Capitalization.Sentence) String getName() {
        return null;
    }
}
