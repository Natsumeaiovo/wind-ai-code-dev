package com.na.windaicodedev.model.enums;

import lombok.Getter;

@Getter
public enum UserRoleEnum {
    
    USER("用户", "user"),
    ADMIN("管理员", "admin");

    private final String text;
    private final String value;
    
    UserRoleEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }
    
    /**
     * 根据值获取枚举项
     * @param value 枚举值
     * @return 枚举项
     */
    public static UserRoleEnum getEnumByValue(String value) {
        // 如果枚举值较多，可以考虑使用 map
        for (UserRoleEnum enumItem : UserRoleEnum.values()) {
            if (enumItem.value.equals(value)) {
                return enumItem;
            }
        }
        return null;
    }
}
