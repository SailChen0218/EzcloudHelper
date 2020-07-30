package cn.zjcdjk.ezcloud.plugin.helper.annotation;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.search.GlobalSearchScope;
import java.util.Map;
import java.util.Set;
import org.jetbrains.annotations.NotNull;

public class Annotation implements Cloneable {
    public static final Annotation QUERY_MAPPING = new Annotation("@QueryMapping", "cn.zjcdjk.ezcloud.core.annotation.QueryMapping");

    public static final Annotation COMMAND_MAPPING = new Annotation("@CommandMapping", "cn.zjcdjk.ezcloud.core.annotation.CommandMapping");

    public static final Annotation QUERY_HANDLER = new Annotation("@QueryHandler", "cn.zjcdjk.ezcloud.core.annotation.QueryHandler");

    public static final Set<Annotation> STATEMENT_SYMMETRIES = (Set<Annotation>)ImmutableSet.of(QUERY_MAPPING, COMMAND_MAPPING, QUERY_HANDLER);

    private final String label;

    private final String qualifiedName;

    private Map<String, AnnotationValue> attributePairs;

    public static class StringValue implements AnnotationValue {
        private String value;

        public StringValue(@NotNull String value) {
            this.value = value;
        }

        public String toString() {
            return "\"" + this.value + "\"";
        }
    }

    public Annotation(@NotNull String label, @NotNull String qualifiedName) {
        this.label = label;
        this.qualifiedName = qualifiedName;
        this.attributePairs = Maps.newHashMap();
    }

    private Annotation addAttribute(String key, AnnotationValue value) {
        this.attributePairs.put(key, value);
        return this;
    }

    public Annotation withAttribute(@NotNull String key, @NotNull AnnotationValue value) {
//        if (key == null)
//            $$$reportNull$$$0(2);
//        if (value == null)
//            $$$reportNull$$$0(3);
        Annotation copy = clone();
        copy.attributePairs = Maps.newHashMap(this.attributePairs);
        return copy.addAttribute(key, value);
    }

    public Annotation withValue(@NotNull AnnotationValue value) {
//        if (value == null)
//            $$$reportNull$$$0(4);
        return withAttribute("value", value);
    }

    public String toString() {
        StringBuilder builder = new StringBuilder(this.label);
        if (!Iterables.isEmpty(this.attributePairs.entrySet()))
            builder.append(setupAttributeText());
        return builder.toString();
    }

    private String setupAttributeText() {
        Optional<String> singleValue = getSingleValue();
        return singleValue.isPresent() ? (String)singleValue.get() : getComplexValue();
    }

    private String getComplexValue() {
        StringBuilder builder = new StringBuilder("(");
        for (String key : this.attributePairs.keySet()) {
            builder.append(key);
            builder.append(" = ");
            builder.append(((AnnotationValue)this.attributePairs.get(key)).toString());
            builder.append(", ");
        }
        builder.deleteCharAt(builder.length() - 1);
        builder.deleteCharAt(builder.length() - 1);
        builder.append(")");
        return builder.toString();
    }

    @NotNull
    public Optional<PsiClass> toPsiClass(@NotNull Project project) {
//        if (project == null)
//            $$$reportNull$$$0(5);
//        if (Optional.fromNullable(JavaPsiFacade.getInstance(project).findClass(getQualifiedName(), GlobalSearchScope.allScope(project))) == null)
//            $$$reportNull$$$0(6);
        return Optional.fromNullable(JavaPsiFacade.getInstance(project).findClass(getQualifiedName(), GlobalSearchScope.allScope(project)));
    }

    private Optional<String> getSingleValue() {
        try {
            String value = (String)Iterables.getOnlyElement(this.attributePairs.keySet());
            StringBuilder builder = new StringBuilder("(");
            builder.append(((AnnotationValue)this.attributePairs.get(value)).toString());
            builder.append(")");
            return Optional.of(builder.toString());
        } catch (Exception e) {
            return Optional.absent();
        }
    }

    @NotNull
    public String getLabel() {
//        if (this.label == null)
//            $$$reportNull$$$0(7);
        return this.label;
    }

    @NotNull
    public String getQualifiedName() {
//        if (this.qualifiedName == null)
//            $$$reportNull$$$0(8);
        return this.qualifiedName;
    }

    protected Annotation clone() {
        try {
            return (Annotation)super.clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException();
        }
    }

    public static interface AnnotationValue {}
}

