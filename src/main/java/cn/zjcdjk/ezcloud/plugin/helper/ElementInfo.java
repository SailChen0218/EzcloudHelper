package cn.zjcdjk.ezcloud.plugin.helper;

import cn.zjcdjk.ezcloud.plugin.helper.action.GotoHandlerAction;
import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.execution.lineMarker.RunLineMarkerContributor.Info;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.actionSystem.Separator;
import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.util.Function;

import javax.swing.*;
import java.util.*;

public class ElementInfo {
    private String name;
    private Map<String, VirtualFileInfo> mappingFileMap;
    private Map<String, VirtualFileInfo> handlerFileMap;

    public static ElementInfo createElementInfo(PsiElement element, ElementType elementType) {
        return createElementInfo(element, elementType, null, null);
    }

    public static ElementInfo createElementInfo(PsiElement element, ElementType elementType,
                                                String cmdHandlerName, String cmdName) {
        switch (elementType) {
            case CommandMapping:
            case QueryMapping:
                return createMappingElementInfo(element);
            case QueryHandler:
                return createQueryHandlerElementInfo(element);
            case CommandHandler:
                return createCommandHandlerElementInfo(element, cmdHandlerName, cmdName);
            default:
                throw new IllegalArgumentException("unsupported element type.");
        }
    }

    public LineMarkerInfo createQueryHandlerLineMarkerInfo(PsiMethod psiMethod, ElementInfo elementInfo) {
        Map<String, VirtualFileInfo> fileInfoMap = elementInfo.getHandlerFileMap();
        if (fileInfoMap != null) {
            Icon icon = IconLoader.getIcon("/icon/arrowright1.png");
            List<GotoHandlerAction> actions = createGotoHandlerActionList(fileInfoMap, "QueryHandler", icon);
            return createLineMarkerInfo(actions, psiMethod.getBody(), icon);
        } else {
            return null;
        }
    }

    public LineMarkerInfo createCommandHandlerLineMarkerInfo(PsiMethod psiMethod, ElementInfo elementInfo) {
        Map<String, VirtualFileInfo> fileInfoMap = elementInfo.getHandlerFileMap();
        if (fileInfoMap != null) {
            Icon icon = IconLoader.getIcon("/icon/arrowright1.png");
            List<GotoHandlerAction> actions = createGotoHandlerActionList(fileInfoMap, "CommandHandler", icon);
            return createLineMarkerInfo(actions, psiMethod.getBody(), icon);
        } else {
            return null;
        }
    }

    public LineMarkerInfo createMappingLineMarkerInfo(PsiMethod psiMethod, ElementInfo elementInfo) {
        Map<String, VirtualFileInfo> fileInfoMap = elementInfo.getMappingFileMap();
        if (fileInfoMap != null) {
            Icon icon = IconLoader.getIcon("/icon/arrowright1.png");
            List<GotoHandlerAction> actions = createGotoHandlerActionList(fileInfoMap, "mapping", icon);
            return createLineMarkerInfo(actions, psiMethod.getBody(), icon);
        } else {
            return null;
        }
    }

    private List<GotoHandlerAction> createGotoHandlerActionList(Map<String, VirtualFileInfo> fileInfoMap,
                                                                String description, Icon icon) {
        if (fileInfoMap != null) {
            Set<String> keys = fileInfoMap.keySet();
            List<GotoHandlerAction> gotoHandlerActionList = new ArrayList(keys.size());
            for (String mappingName : keys) {
                VirtualFileInfo virtualFileInfo = fileInfoMap.get(mappingName);
                GotoHandlerAction gotoHandlerAction = new GotoHandlerAction(virtualFileInfo, description, icon);
                gotoHandlerActionList.add(gotoHandlerAction);
            }
            return gotoHandlerActionList;
        } else {
            return null;
        }
    }

    private LineMarkerInfo createLineMarkerInfo(List<GotoHandlerAction> actions, PsiCodeBlock codeBlock, Icon icon) {
        if (actions == null || actions.size() == 0) {
            return null;
        }

        Info info = new Info(actions.get(0));
        DefaultActionGroup actionGroup = new DefaultActionGroup();
        for (GotoHandlerAction action : actions) {
            actionGroup.add(action);
            actionGroup.add(new Separator());
        }
        Function<PsiElement, String> tooltipProvider = (PsiElement element) -> {
            final StringBuilder tooltip = new StringBuilder();
            if (info.tooltipProvider != null) {
                String string = info.tooltipProvider.apply(element);
                if (string == null) return null;
                if (tooltip.length() != 0) {
                    tooltip.append("\n");
                }
                tooltip.append(string);
            }
            return tooltip.length() == 0 ? null : tooltip.toString();
        };
        return new RunLineMarkerInfo(codeBlock, icon, tooltipProvider, actionGroup);
    }

    private static int getLineNumber(String text, String keyWords) {
        String[] split = text.split("\n");
        int lineNumber = 0;
        for (int i = 0; i < split.length; i++) {
            String line = split[i];
            if (StringUtil.isNotEmpty(line) && line.contains(keyWords)) {
                lineNumber = i;
                break;
            }
        }
        return lineNumber;
    }

    private static ElementInfo createMappingElementInfo(PsiElement element) {
        PsiFile psiFile = element.getContainingFile();
        VirtualFile virtualFile = psiFile.getVirtualFile();

        PsiMethod psiMethod = (PsiMethod) element;
        String mappingName = psiMethod.getName();

        PsiParameterList psiParameterList = psiMethod.getParameterList();
        PsiType psiType = psiParameterList.getParameters()[0].getType();

        String commandName = psiType.getCanonicalText();
        int lineNumber = ElementInfo.getLineNumber(psiFile.getText(), mappingName);

        VirtualFileInfo virtualFileInfo = new VirtualFileInfo();
        virtualFileInfo.setTooltip(commandName);
        virtualFileInfo.setVirtualFile(virtualFile);
        virtualFileInfo.setLineNumber(lineNumber);

        ElementInfo elementInfo = ElementHolder.get(commandName);
        if (elementInfo == null) {
            elementInfo = new ElementInfo();
        }

        Map<String, VirtualFileInfo> mappingFileMap = elementInfo.getMappingFileMap();
        if (mappingFileMap == null) {
            mappingFileMap = new HashMap<>(4);
        }

        mappingFileMap.put(mappingName, virtualFileInfo);
        elementInfo.setMappingFileMap(mappingFileMap);
        elementInfo.setName(commandName);
        ElementHolder.put(commandName, elementInfo);
        return elementInfo;
    }

    private static ElementInfo createQueryHandlerElementInfo(PsiElement element) {
        PsiFile psiFile = element.getContainingFile();
        VirtualFile virtualFile = psiFile.getVirtualFile();

        PsiMethod psiMethod = (PsiMethod) element;
        String handlerName = psiMethod.getName();

        PsiParameterList psiParameterList = psiMethod.getParameterList();
        PsiType psiType = psiParameterList.getParameters()[0].getType();

        String queryName = psiType.getCanonicalText();
        int lineNumber = ElementInfo.getLineNumber(psiFile.getText(), handlerName);

        VirtualFileInfo virtualFileInfo = new VirtualFileInfo();
        virtualFileInfo.setTooltip(queryName);
        virtualFileInfo.setVirtualFile(virtualFile);
        virtualFileInfo.setLineNumber(lineNumber);

        ElementInfo elementInfo = ElementHolder.get(queryName);
        if (elementInfo == null) {
            elementInfo = new ElementInfo();
        }

        Map<String, VirtualFileInfo> handlerFileMap = elementInfo.getHandlerFileMap();
        if (handlerFileMap == null) {
            handlerFileMap = new HashMap<>(4);
        }

        handlerFileMap.put(handlerName, virtualFileInfo);
        elementInfo.setHandlerFileMap(handlerFileMap);
        elementInfo.setName(queryName);
        ElementHolder.put(queryName, elementInfo);
        return elementInfo;
    }

    private static ElementInfo createCommandHandlerElementInfo(PsiElement element,
                                                               String cmdHandlerName, String cmdName) {
        PsiFile psiFile = element.getContainingFile();
        VirtualFile virtualFile = psiFile.getVirtualFile();

        PsiMethod psiMethod = (PsiMethod) element;
        String methodName = psiMethod.getName();

        int lineNumber = ElementInfo.getLineNumber(psiFile.getText(), methodName);

        VirtualFileInfo virtualFileInfo = new VirtualFileInfo();
        virtualFileInfo.setTooltip(cmdName);
        virtualFileInfo.setVirtualFile(virtualFile);
        virtualFileInfo.setLineNumber(lineNumber);

        ElementInfo elementInfo = ElementHolder.get(cmdName);
        if (elementInfo == null) {
            elementInfo = new ElementInfo();
        }

        Map<String, VirtualFileInfo> handlerFileMap = elementInfo.getHandlerFileMap();
        if (handlerFileMap == null) {
            handlerFileMap = new HashMap<>(4);
        }

        handlerFileMap.put(cmdHandlerName, virtualFileInfo);
        elementInfo.setHandlerFileMap(handlerFileMap);
        elementInfo.setName(cmdName);
        ElementHolder.put(cmdName, elementInfo);
        return elementInfo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, VirtualFileInfo> getMappingFileMap() {
        return mappingFileMap;
    }

    public void setMappingFileMap(Map<String, VirtualFileInfo> mappingFileMap) {
        this.mappingFileMap = mappingFileMap;
    }

    public Map<String, VirtualFileInfo> getHandlerFileMap() {
        return handlerFileMap;
    }

    public void setHandlerFileMap(Map<String, VirtualFileInfo> handlerFileMap) {
        this.handlerFileMap = handlerFileMap;
    }
}

