package dev.dong4j.zeka.kernel.notify;

import dev.dong4j.zeka.kernel.notify.entity.OperateLog;

/**
 * 通知操作日志接口，定义了通知操作日志的保存功能
 * 用于记录系统中的消息通知操作，支持审计追踪和问题排查
 * 具体的实现由上层业务系统根据实际存储需求提供
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.05.08 20:31
 * @since 1.0.0
 */
public interface NotifyOperateLog {

    /**
     * 保存操作日志信息
     * 将通知操作的相关信息持久化到存储系统中，供后续查询和分析使用
     *
     * @param object 操作日志对象，包含操作者、操作时间、操作内容等信息
     * @since 1.0.0
     */
    void saveLog(OperateLog object);
}
