package cn.zjcdjk.ezcloud.plugin.helper;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ElementHolder {
    private static final Map<String, ElementInfo> elementInfoMap = new ConcurrentHashMap<>(32);
    public static void put(String name, ElementInfo elementInfo) {
        elementInfoMap.put(name, elementInfo);
    }

    public static ElementInfo get(String name) {
        return elementInfoMap.get(name);
    }
}
