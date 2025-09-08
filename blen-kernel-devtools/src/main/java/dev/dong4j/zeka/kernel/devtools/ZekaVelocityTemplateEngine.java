package dev.dong4j.zeka.kernel.devtools;

import dev.dong4j.zeka.kernel.common.util.FileUtils;
import dev.dong4j.zeka.kernel.common.util.StringUtils;
import dev.dong4j.zeka.kernel.devtools.core.config.ConstVal;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;

/**
 * <p>Description: Velocity 模板引擎实现文件输出 </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.27 18:06
 * @since 1.0.0
 */
@Slf4j
public class ZekaVelocityTemplateEngine extends dev.dong4j.zeka.kernel.devtools.core.engine.VelocityTemplateEngine {

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
        if (StringUtils.isEmpty(templatePath)) {
            return;
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
