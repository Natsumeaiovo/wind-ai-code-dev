package com.na.windaicodedev.core.saver;

import com.na.windaicodedev.ai.model.HtmlCodeResult;
import com.na.windaicodedev.model.enums.CodeGenTypeEnum;

/**
 * HTML 代码文件保存器
 */
public class HtmlCodeFileSaver extends BaseCodeFileSaverTemplate<HtmlCodeResult> {

    @Override
    protected CodeGenTypeEnum getCodeType() {
        return CodeGenTypeEnum.HTML;
    }

    @Override
    protected void saveCodeFiles(HtmlCodeResult result, String baseDir) {
        saveFile(baseDir, "index.html", result.getHtmlCode());
    }
}
