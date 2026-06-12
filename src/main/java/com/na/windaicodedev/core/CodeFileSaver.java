package com.na.windaicodedev.core;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.na.windaicodedev.ai.model.HtmlCodeResult;
import com.na.windaicodedev.ai.model.MultiFileCodeResult;
import com.na.windaicodedev.model.enums.CodeGenTypeEnum;

import java.io.File;
import java.nio.charset.StandardCharsets;

/**
 * 代码文件保存器
 */
public class CodeFileSaver {

    /**
     * 文件保存的根目录
     */
    private static final String FILE_SAVE_ROOT_DIR = System.getProperty("user.dir") + "/tmp_file/code_output";

    /**
     * 保存单 HTML 代码文件
     *
     * @param htmlCodeResult AI 生成的 HTML 代码结果
     * @return 保存后的文件路径
     */
    public static File saveHtmlCodeResult(HtmlCodeResult htmlCodeResult) {
        String dirPath = buildUniqueDir(CodeGenTypeEnum.HTML.getValue());
        saveFile(dirPath, "index.html", htmlCodeResult.getHtmlCode());
        return new File(dirPath);
    }

    /**
     * 保存多文件代码
     *
     * @param multiFileCodeResult AI 生成的多文件代码结果
     * @return 保存后的文件路径
     */
    public static File saveMultiFileCodeResult(MultiFileCodeResult multiFileCodeResult) {
        String dirPath = buildUniqueDir(CodeGenTypeEnum.MULTI_FILE.getValue());
        saveFile(dirPath, "index.html", multiFileCodeResult.getHtmlCode());
        saveFile(dirPath, "style.css", multiFileCodeResult.getCssCode());
        saveFile(dirPath, "script.js", multiFileCodeResult.getJsCode());
        return new File(dirPath);
    }


    /**
     * 构建文件的唯一路径（tmp_file/code_output/bizType_雪花 id）
     *
     * @param bizType 业务类型
     * @return 唯一目录路径
     */
    private static String buildUniqueDir(String bizType) {
        String uniqueDirName = StrUtil.format("{}_{}", bizType, IdUtil.getSnowflakeNextId());
        // /tmp_file/code_output/html_12345...
        String dirPath = FILE_SAVE_ROOT_DIR + File.separator + uniqueDirName;
        FileUtil.mkdir(dirPath);
        return dirPath;
    }

    /**
     * 保存文件(通用)
     *
     * @param dirPath  目录路径
     * @param filename 文件名
     * @param content  文件内容
     */
    private static void saveFile(String dirPath, String filename, String content) {
        String filePath = dirPath + File.separator + filename;
        FileUtil.writeString(content, filePath, StandardCharsets.UTF_8);
    }
}
