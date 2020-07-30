package cn.zjcdjk.ezcloud.plugin;

import org.jetbrains.annotations.NotNull;
import com.intellij.openapi.components.ApplicationComponent;

/**
 * @author hansong.xhs
 * @version \$Id: CodeMaker.java, v 0.1 2017-01-19 10:18 hansong.xhs Exp $$
 */
public class EzcloudHelper implements ApplicationComponent {
    public EzcloudHelper() {
    }

    @Override
    public void initComponent() {
    }

    @Override
    public void disposeComponent() {
    }

    @Override
    @NotNull
    public String getComponentName() {
        return "EzcloudHelper";
    }
}
