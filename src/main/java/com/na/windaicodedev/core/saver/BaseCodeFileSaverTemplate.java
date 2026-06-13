package com.na.windaicodedev.core.saver;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.na.windaicodedev.model.enums.CodeGenTypeEnum;

import java.io.File;
import java.nio.charset.StandardCharsets;

/**
 * 抽象代码文件保存器 - 模板方法模式
 *
 * @param <T> 代码结果类型
 */
public abstract class BaseCodeFileSaverTemplate<T> {

    /**
     * 文件保存的根目录
     */
    private static final String FILE_SAVE_ROOT_DIR = System.getProperty("user.dir") + "/tmp_file/code_output";

    /**
     * 保存代码文件的模板方法标准流程
     *
     * @param result 要保存的代码
     * @return 保存的目录路径
     */
    public final File saveCode(T result) {
        // 1. 验证输入
        validateInput(result);
        // 2. 构建唯一目录
        String baseDir = buildUniqueDir();
        // 3. 保存代码文件（具体实现交给子类，可能设计多个代码文件）
        saveCodeFiles(result, baseDir);
        // 4. 返回目录对象
        return new File(baseDir);
    }


    /**
     * 构建文件的唯一路径（tmp_file/code_output/codeType_雪花 id）
     *
     * @return 唯一目录路径
     */
    protected String buildUniqueDir() {
        String codeType = getCodeType().getValue();
        String uniqueDirName = StrUtil.format("{}_{}", codeType, IdUtil.getSnowflakeNextId());
        // /tmp_file/code_output/html_12345...
        String dirPath = FILE_SAVE_ROOT_DIR + File.separator + uniqueDirName;
        FileUtil.mkdir(dirPath);
        return dirPath;
    }

    /**
     * 获取代码类型，交给子类实现
     *
     * @return 代码类型枚举
     */
    protected abstract CodeGenTypeEnum getCodeType();

    /**
     * 保存代码文件，交给子类实现
     *
     * @param result  输入结果
     * @param baseDir 保存的目录路径
     */
    protected abstract void saveCodeFiles(T result, String baseDir);

    /**
     * 验证输入参数，子类可以覆盖
     *
     * @param result 输入参数
     */
    protected void validateInput(T result) {
        if (result == null) {
            throw new IllegalArgumentException("输入结果不能为空");
        }
    }

    /**
     * 保存文件(通用)
     *
     * @param dirPath  目录路径
     * @param filename 文件名
     * @param content  文件内容
     */
    public final void saveFile(String dirPath, String filename, String content) {
        if (StrUtil.isNotBlank(content)) {
            String filePath = dirPath + File.separator + filename;
            FileUtil.writeString(content, filePath, StandardCharsets.UTF_8);
        }
    }
}
