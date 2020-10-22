package xyz.erupt.auth.constant;

import lombok.Getter;

@Getter
public enum MenuTypeEnum {

    TABLE("table", "表格", "填erupt类名"),
    TREE("tree", "树", "填erupt类名"),
    FILL("fill", "充满屏幕", "路由地址"),
    ROUTER("router", "页面路由", "路由地址"),
    LINK("link", "链接", "互联网地址"),
    NEW_WINDOW("newWindow", "新页签", "互联网地址"),
    ;

    private final String code;
    private final String name;
    private final String desc;

    MenuTypeEnum(String code, String name, String desc) {
        this.code = code;
        this.name = name;
        this.desc = desc;
    }
}
