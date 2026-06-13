package com.na.windaicodedev.core;

import com.na.windaicodedev.ai.AiCodeGeneratorService;
import com.na.windaicodedev.ai.model.HtmlCodeResult;
import com.na.windaicodedev.ai.model.MultiFileCodeResult;
import com.na.windaicodedev.core.parser.CodeParserExecutor;
import com.na.windaicodedev.core.saver.CodeFileSaverExecutor;
import com.na.windaicodedev.exception.BusinessException;
import com.na.windaicodedev.exception.ErrorCode;
import com.na.windaicodedev.model.enums.CodeGenTypeEnum;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

import org.springframework.stereotype.Service;

import java.io.File;

/**
 * 【AI 代码生成门面类】，组合代码生成和保存功能
 */
@Service
@Slf4j
public class AiCodeGeneratorFacade {

    @Resource
    private AiCodeGeneratorService aiCodeGeneratorService;

    /**
     * 入口1：根据用户输入的提示信息和代码生成类型，生成代码并保存到本地
     *
     * @param userMessage     用户输入的提示信息
     * @param codeGenTypeEnum 代码生成类型
     * @return 保存后的文件路径
     */
    public File generateAndSaveCode(String userMessage, CodeGenTypeEnum codeGenTypeEnum) {
        if (codeGenTypeEnum == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "代码生成类型不能为空");
        }
        return switch (codeGenTypeEnum) {
            case HTML -> {
                HtmlCodeResult htmlCodeResult = aiCodeGeneratorService.generateHtmlCode(userMessage);
                yield CodeFileSaverExecutor.executeSaveCode(htmlCodeResult, CodeGenTypeEnum.HTML);
            }
            case MULTI_FILE -> {
                MultiFileCodeResult multiFileCodeResult = aiCodeGeneratorService.generateMultiFileCode(userMessage);
                yield CodeFileSaverExecutor.executeSaveCode(multiFileCodeResult, CodeGenTypeEnum.MULTI_FILE);
            }
            default ->
                    throw new BusinessException(ErrorCode.SYSTEM_ERROR, "不支持的生成类型，" + codeGenTypeEnum.getValue());
        };
    }

    /**
     * 入口2：根据用户输入的提示信息和代码生成类型，生成代码(流式)，并保存到本地
     *
     * @param userMessage     用户输入的提示信息
     * @param codeGenTypeEnum 代码生成类型
     * @return
     */
    public Flux<String> generateAndSaveCodeStream(String userMessage, CodeGenTypeEnum codeGenTypeEnum) {
        if (codeGenTypeEnum == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "代码生成类型不能为空");
        }
        return switch (codeGenTypeEnum) {
            case HTML -> {
                // 生成流式代码
                Flux<String> htmlCodeStream = aiCodeGeneratorService.generateHtmlCodeStream(userMessage);
                // 收集完整代码后进行解析和保存
                yield processCodeStream(htmlCodeStream, CodeGenTypeEnum.HTML);
            }
            case MULTI_FILE -> {
                Flux<String> codeStream = aiCodeGeneratorService.generateMultiFileCodeStream(userMessage);
                yield processCodeStream(codeStream, CodeGenTypeEnum.MULTI_FILE);
            }
            default ->
                    throw new BusinessException(ErrorCode.SYSTEM_ERROR, "不支持的生成类型，" + codeGenTypeEnum.getValue());
        };
    }

    /**
     * 通用代码流处理方法，收集完整代码后进行解析和保存
     *
     * @param codeStream      代码流
     * @param codeGenTypeEnum 代码生成类型
     * @return 处理后的代码流Flux
     */
    private Flux<String> processCodeStream(Flux<String> codeStream, CodeGenTypeEnum codeGenTypeEnum) {
        StringBuilder codeBuilder = new StringBuilder();
        return codeStream.doOnNext(chunk -> {
            codeBuilder.append(chunk);
        }).doOnComplete(() -> {
            // 收集完成解析并保存代码
            try {
                String completeCode = codeBuilder.toString();
                // 使用解析执行器，解析代码
                Object parsedCode = CodeParserExecutor.executeParser(completeCode, codeGenTypeEnum);
                // 使用代码保存执行器，保存代码
                File baseDir = CodeFileSaverExecutor.executeSaveCode(parsedCode, codeGenTypeEnum);
                log.info("多代码文件创建完成，目录为: {}", baseDir.getAbsolutePath());
            } catch (Exception e) {
                log.error("保存失败, {}", e.getMessage());
            }
        });
    }
}
