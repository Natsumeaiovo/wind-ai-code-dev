package com.na.windaicodedev.core.saver;

import com.na.windaicodedev.ai.model.HtmlCodeResult;
import com.na.windaicodedev.ai.model.MultiFileCodeResult;
import com.na.windaicodedev.exception.BusinessException;
import com.na.windaicodedev.exception.ErrorCode;
import com.na.windaicodedev.model.enums.CodeGenTypeEnum;

import java.io.File;

/**
 * 代码文件保存执行器
 *
 * @author na
 * @date 2024/6/17 17:00
 */
public class CodeFileSaverExecutor {

    private static final HtmlCodeFileSaver htmlCodeFileSaver = new HtmlCodeFileSaver();
    private static final MultiFileCodeFileSaver multiFileCodeFileSaver = new MultiFileCodeFileSaver();

    /**
     * 执行代码保存
     * @param codeResult 代码结果对象
     * @param codeGenTypeEnum 代码类型枚举
     * @return 保存后的目录对象
     */
    public static File executeSaveCode(Object codeResult, CodeGenTypeEnum codeGenTypeEnum) {
        return switch (codeGenTypeEnum) {
            case HTML -> htmlCodeFileSaver.saveCode((HtmlCodeResult) codeResult);
            case MULTI_FILE -> multiFileCodeFileSaver.saveCode((MultiFileCodeResult) codeResult);
            default -> throw new BusinessException(ErrorCode.SYSTEM_ERROR, "不支持的代码生成类型: " + codeGenTypeEnum);
        };
    }
}
