package com.na.windaicodedev.ai.model;

import dev.langchain4j.model.output.structured.Description;
import lombok.Data;

/**
 * HTML 代码生成结果
 */
@Data
public class HtmlCodeResult {

    /**
     * 生成的 HTML 代码
     */
    @Description("HTML 代码")
    private String htmlCode;

    /**
     * 其他描述
     */
    @Description("其他描述")
    private String description;
}
