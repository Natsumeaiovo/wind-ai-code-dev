package com.na.windaicodedev.ai;

import com.na.windaicodedev.ai.model.HtmlCodeResult;
import com.na.windaicodedev.ai.model.MultiFileCodeResult;
import dev.langchain4j.service.SystemMessage;
import reactor.core.publisher.Flux;

/**
 * AI 代码生成服务接口
 * 参考文档：https://docs.langchain4j.dev/tutorials/ai-services#systemmessage
 */
public interface AiCodeGeneratorService {

    /**
     * 生成单文件 HTML 代码
     *
     * @param prompt 用户输入的提示信息
     * @return AI 的输出结果，转换为 HtmlCodeResult 对象
     */
    @SystemMessage(fromResource = "prompt/codegen-html-system-prompt.txt")
    HtmlCodeResult generateHtmlCode(String prompt);

    /**
     * 生成多文件代码
     *
     * @return AI 的输出结果，转换为 MultiFileCodeResult 对象
     */
    @SystemMessage(fromResource = "prompt/codegen-multi-file-system-prompt.txt")
    MultiFileCodeResult generateMultiFileCode(String prompt);


    /**
     * 生成单文件 HTML 代码流
     *
     * @param prompt 用户输入的提示信息
     * @return AI 的输出结果，转换为 Flux<String> 流
     */
    @SystemMessage(fromResource = "prompt/codegen-html-system-prompt.txt")
    Flux<String> generateHtmlCodeStream(String prompt);

    /**
     * 生成多文件代码流
     *
     * @param prompt 用户输入的提示信息
     * @return AI 的输出结果，转换为 Flux<String> 流
     */
    @SystemMessage(fromResource = "prompt/codegen-multi-file-system-prompt.txt")
    Flux<String> generateMultiFileCodeStream(String prompt);
}
