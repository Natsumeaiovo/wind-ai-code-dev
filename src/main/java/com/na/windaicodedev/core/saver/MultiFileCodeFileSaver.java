package com.na.windaicodedev.core.saver;

import com.na.windaicodedev.ai.model.MultiFileCodeResult;
import com.na.windaicodedev.model.enums.CodeGenTypeEnum;

/**
 * 多文件代码文件保存器
 */
public class MultiFileCodeFileSaver extends BaseCodeFileSaverTemplate<MultiFileCodeResult> {

    @Override
    protected CodeGenTypeEnum getCodeType() {
        return CodeGenTypeEnum.MULTI_FILE;
    }

    @Override
    protected void saveCodeFiles(MultiFileCodeResult result, String baseDir) {
        saveFile(baseDir, "index.html", result.getHtmlCode());
        saveFile(baseDir, "style.css", result.getCssCode());
        saveFile(baseDir, "script.js", result.getJsCode());
    }
}
