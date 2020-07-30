package cn.zjcdjk.ezcloud.plugin.helper.utils;

import cn.zjcdjk.ezcloud.plugin.helper.annotation.Annotation;
import com.intellij.psi.PsiModifierList;
import com.intellij.psi.PsiModifierListOwner;
import org.jetbrains.annotations.NotNull;
import com.intellij.psi.PsiAnnotation;

public class JavaUtils {
    public static boolean isAnnotationPresent(@NotNull PsiModifierListOwner target, @NotNull Annotation annotation) {
        PsiModifierList modifierList = target.getModifierList();
        return (null != modifierList && null != modifierList.findAnnotation(annotation.getQualifiedName()));
    }
}
