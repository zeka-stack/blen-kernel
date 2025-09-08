package dev.dong4j.zeka.kernel.notify.aop;

import dev.dong4j.zeka.kernel.common.context.Trace;
import dev.dong4j.zeka.kernel.common.util.ClassUtils;
import dev.dong4j.zeka.kernel.common.util.SnowflakeBuilder;
import dev.dong4j.zeka.kernel.common.util.StringUtils;
import dev.dong4j.zeka.kernel.notify.Message;
import dev.dong4j.zeka.kernel.notify.exception.NotifyException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

/**
 * 消息ID自动生成切面类，使用AOP拦截通知方法调用
 * 在消息通知之前自动为未设置ID的消息对象生成唯一标识
 * 支持Long类型（雪花算法）和String类型（UUID或追踪ID）的ID生成
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.05.08 13:49
 * @since 1.0.0
 */
@Aspect
@Component
public class MessageIdAspect {

    /**
     * 定义通知方法的切点，拦截所有Notify接口实现类的方法调用
     *
     * @since 1.0.0
     */
    @Pointcut("execution(* dev.dong4j.zeka.kernel.notify.Notify.*(..))")
    public void notifyPointCut() {
        // nothing to do
    }

    /**
     * 前置通知方法，在消息通知执行前自动生成消息ID
     * 检查消息对象是否已有ID，如果没有则根据类型自动生成
     * 支持Long类型（使用雪花算法）和String类型（优先使用traceId）
     *
     * @param joinPoint 连接点，包含被拦截方法的信息和参数
     * @since 1.0.0
     */
    @Before(value = "notifyPointCut()")
    @SuppressWarnings("unchecked")
    public void before(@NotNull JoinPoint joinPoint) {
        // 获取目标方法的参数信息
        Object[] obj = joinPoint.getArgs();
        for (Object argItem : obj) {
            // 判断参数是否为消息对象
            if (argItem instanceof Message) {
                // 获取消息ID的泛型类型
                Class<?> idType = ClassUtils.getSuperClassT(argItem.getClass(), 0);

                // 如果消息ID为空，则自动生成
                if (((Message<?>) argItem).getMessageId() == null) {
                    // Long类型ID使用雪花算法生成
                    if (Long.class.isAssignableFrom(idType)) {
                        Message<Long> message = (Message<Long>) argItem;
                        // todo-dong4j : (2022.03.27 20:48) [优先从 trace 中获取 traceId, 需要注意类型转换]
                        message.setMessageId(SnowflakeBuilder.builder());
                        // String类型ID优先使用traceId，其次使用UUID
                    } else if (String.class.isAssignableFrom(idType)) {
                        Message<String> paramVO = (Message<String>) argItem;
                        String traceId = Trace.context().get();
                        traceId = StringUtils.isBlank(traceId) ? StringUtils.getUid() : traceId;
                        paramVO.setMessageId(traceId);
                    } else {
                        // 抛出不支持的类型异常
                        throw new NotifyException("暂不支持 [{}] 类型的 message id", idType.getSimpleName());
                    }
                }
            }
        }
    }

}
