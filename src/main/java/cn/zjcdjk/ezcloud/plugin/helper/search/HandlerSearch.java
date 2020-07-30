package cn.zjcdjk.ezcloud.plugin.helper.search;

import com.intellij.openapi.application.QueryExecutorBase;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiMethod;
import com.intellij.util.CommonProcessors;
import com.intellij.util.Processor;
import kotlin.reflect.jvm.internal.impl.load.java.structure.JavaElement;
import org.jetbrains.annotations.NotNull;

public class HandlerSearch extends QueryExecutorBase<PsiMethod, PsiElement> {
    @Override
    public void processQuery(@NotNull PsiElement queryParameters, @NotNull Processor<? super PsiMethod> consumer) {
        if (queryParameters == null) return;
        if (consumer == null) return;
        if (!(queryParameters instanceof com.intellij.psi.PsiTypeParameterListOwner))
            return;
        Processor<PsiMethod> processor = new Processor<PsiMethod>() {
            public boolean process(PsiMethod psiMethod) {
                return consumer.process(psiMethod);
            }
        };
//        CommonProcessors.FindFirstProcessor<PsiMethod> processor1 = new CommonProcessors.FindFirstProcessor();
//        JavaService.getInstance(element.getProject()).process(element, processor);
    }
}
