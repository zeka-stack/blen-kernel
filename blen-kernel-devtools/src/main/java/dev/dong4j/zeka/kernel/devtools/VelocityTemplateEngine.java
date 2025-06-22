package dev.dong4j.zeka.kernel.devtools;

import com.baomidou.mybatisplus.generator.config.ConstVal;
import dev.dong4j.zeka.kernel.common.util.FileUtils;
import dev.dong4j.zeka.kernel.common.util.Tools;
import lombok.extern.slf4j.Slf4j;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.util.Map;

/**
 * <p>Description: Velocity 模板引擎实现文件输出 </p>
 *
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.27 18:06
 * @since 1.0.0
 */
@Slf4j
public class VelocityTemplateEngine extends com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine {
    /** Velocity engine */
    private VelocityEngine velocityEngine;

    /**
     * Writer *
     *
     * @param objectMap    object map
     * @param templatePath template path
     * @param outputFile   output file
     * @since 1.0.0
     */
    @Override
    public void writer(Map<String, Object> objectMap, String templatePath, String outputFile) {
        if (Tools.isEmpty(templatePath)) {
            return;
        }

        if (this.velocityEngine == null) {
            try {
                Field field = this.getClass().getSuperclass().getDeclaredField("velocityEngine");
                field.setAccessible(true);
                this.velocityEngine = (VelocityEngine) field.get(this);
            } catch (NoSuchFieldException | IllegalAccessException ignored) {
            }
        }

        outputFile = FileUtils.getRealFilePath(outputFile);
        Template template = this.velocityEngine.getTemplate(templatePath, ConstVal.UTF8);
        int lastIndex = outputFile.lastIndexOf(File.separator);
        this.mkdir(outputFile.substring(0, lastIndex));
        try (FileOutputStream fos = new FileOutputStream(outputFile);
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos, ConstVal.UTF8))) {
            template.merge(new VelocityContext(objectMap), writer);
            log.debug("模板: [{}], 文件: [{}]", templatePath, outputFile);
        } catch (IOException e) {
            log.error("writer file error", e);
        }
    }

    /**
     * Mkdir.
     *
     * @param filePath the file path
     * @since 1.0.0
     */
    private void mkdir(String filePath) {
        File file = new File(filePath);
        if (!file.exists() && file.mkdirs()) {
            log.info("创建 {}", filePath);
        }
    }
}
