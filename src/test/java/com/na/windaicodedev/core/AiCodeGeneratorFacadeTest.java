package com.na.windaicodedev.core;

import com.na.windaicodedev.model.enums.CodeGenTypeEnum;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AiCodeGeneratorFacadeTest {

    @Resource
    private AiCodeGeneratorFacade aiCodeGeneratorFacade;

    @Test
    void generateAndSaveCode() {
        File file = aiCodeGeneratorFacade.generateAndSaveCode("生成一个用户注册页面", CodeGenTypeEnum.MULTI_FILE);
        Assertions.assertTrue(file.exists());
    }

    @Test
    void generateAndSaveCodeStream() {
        Flux<String> codeStream = aiCodeGeneratorFacade.generateAndSaveCodeStream(
                "生成一个云盘网站", CodeGenTypeEnum.MULTI_FILE);
        // 等待所有数据收集完成
        List<String> resultList = codeStream.collectList().block();
        Assertions.assertNotNull(resultList);
        String completeContent = String.join("", resultList);
        System.out.println(completeContent);
        Assertions.assertNotNull(completeContent);
    }
}