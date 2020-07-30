package cn.zjcdjk.ezcloud.plugin.helper.locator;

public class CommandInfo {
    private String commandName;
    private MethodType methodType;

    public CommandInfo(String commandName, MethodType methodType) {
        this.commandName = commandName;
        this.methodType = methodType;
    }

    public String getCommandName() {
        return commandName;
    }

    public void setCommandName(String commandName) {
        this.commandName = commandName;
    }

    public MethodType getMethodType() {
        return methodType;
    }

    public void setMethodType(MethodType methodType) {
        this.methodType = methodType;
    }
}
