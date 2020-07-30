package cn.zjcdjk.ezcloud.plugin.helper.locator;

import cn.zjcdjk.ezcloud.plugin.helper.annotation.Annotation;
import cn.zjcdjk.ezcloud.plugin.helper.utils.JavaUtils;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HandlerLocator {
    private static final String COMMAND_HANDLER_CONTEXT =
            "cn.zjcdjk.ezcloud.core.command.process.CommandHandlerContext";
    private static final String EXECUTE_METHOD = "execute";

    public static final Map<String, PsiMethod> handlerHolder = new ConcurrentHashMap<>();
    public static final Map<String, PsiMethod> mappingHolder = new ConcurrentHashMap<>();

    public static void packageScan(String pkaName, Project project) {
        PsiPackage pkg = JavaPsiFacade.getInstance(project).findPackage(pkaName);
        if (pkg != null) {
            PsiClass[] psiClasses = pkg.getClasses();
            if (psiClasses != null && psiClasses.length > 0) {
                for (PsiClass psiClass : psiClasses) {
                    PsiMethod[] psiMethods = psiClass.getMethods();
                    if (psiMethods != null && psiMethods.length > 0) {
                        for (PsiMethod psiMethod : psiMethods) {
                            recordMethod(psiMethod);
                        }
                    }
                }
            }

            PsiPackage[] psiPackages = pkg.getSubPackages();
            if (psiPackages != null && psiPackages.length > 0) {
                for (PsiPackage psiPackage : psiPackages) {
                    packageScan(psiPackage.getQualifiedName(), project);
                }
            }
        }
    }

    public static CommandInfo recordMethod(PsiMethod psiMethod) {
        boolean hasAnnotation = JavaUtils.isAnnotationPresent((PsiModifierListOwner) psiMethod, Annotation.QUERY_MAPPING);
        if (hasAnnotation) {
            return recordMapping(psiMethod);
        }

        hasAnnotation = JavaUtils.isAnnotationPresent((PsiModifierListOwner) psiMethod, Annotation.COMMAND_MAPPING);
        if (hasAnnotation) {
            return recordMapping(psiMethod);
        }

        hasAnnotation = JavaUtils.isAnnotationPresent((PsiModifierListOwner) psiMethod, Annotation.QUERY_HANDLER);
        if (hasAnnotation) {
            return recordQueryHandler(psiMethod);
        }

        if (EXECUTE_METHOD.equals(psiMethod.getName())) {
            return recordCommandHandler(psiMethod);
        }

        return null;
    }

    public static CommandInfo recordMapping(PsiMethod psiMethod) {
        PsiParameterList psiParameterList = psiMethod.getParameterList();
        if (psiParameterList != null) {
            PsiParameter[] psiParameters = psiParameterList.getParameters();
            if (psiParameters != null && psiParameters.length > 0) {
                PsiType psiType = psiParameterList.getParameters()[0].getType();
                String commandName = psiType.getCanonicalText();
                mappingHolder.put(commandName, psiMethod);
                return new CommandInfo(commandName, MethodType.Mapping);
            }
        }
        return null;
    }

    public static CommandInfo recordQueryHandler(PsiMethod psiMethod) {
        PsiParameterList psiParameterList = psiMethod.getParameterList();
        if (psiParameterList != null) {
            PsiParameter[] psiParameters = psiParameterList.getParameters();
            if (psiParameters != null && psiParameters.length > 0) {
                PsiType psiType = psiParameterList.getParameters()[0].getType();
                String commandName = psiType.getCanonicalText();
                handlerHolder.put(commandName, psiMethod);
                return new CommandInfo(commandName, MethodType.Handler);
            }
        }
        return null;
    }

    public static CommandInfo recordCommandHandler(PsiMethod psiMethod) {
        PsiParameterList psiParameterList = psiMethod.getParameterList();
        if (psiParameterList != null) {
            PsiParameter[] psiParameter = psiParameterList.getParameters();
            if (psiParameter.length == 2) {
                String contextParameterName = psiParameter[0].getType().getCanonicalText();
                if (COMMAND_HANDLER_CONTEXT.equals(contextParameterName)) {
                    String cmdName = psiParameter[1].getType().getCanonicalText();
                    handlerHolder.put(cmdName, psiMethod);
                    return new CommandInfo(cmdName, MethodType.Handler);
                }
            }
        }
        return null;
    }
}
