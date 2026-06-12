package com.na.windaicodedev.ai.model;

import dev.langchain4j.model.output.structured.Description;
import lombok.Data;

@Data
public class MultiFileCodeResult {

    /**
     * 生成的 HTML 代码
     */
    @Description("HTML 代码")
    private String htmlCode;

    /**
     * 生成的 JS 代码
     */
    @Description("JS 代码")
    private String jsCode;

    /**
     * 生成的 CSS 样式代码
     */
    @Description("CSS 代码")
    private String cssCode;

    /**
     * 其他描述
     */
    @Description("其他描述")
    private String description;
}
