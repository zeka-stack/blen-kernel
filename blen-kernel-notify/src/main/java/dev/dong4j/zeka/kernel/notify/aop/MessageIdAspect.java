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
 * <p>Description: aop 拦截生成 messageId </p>
 *
 * @author dong4j
 * @version 1.4.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.05.08 13:49
 * @since 1.4.0
 */
@Aspect
@Component
public class MessageIdAspect {

    /**
     * Notify point cut
     *
     * @since 1.4.0
     */
    @Pointcut("execution(* dev.dong4j.zeka.kernel.notify.Notify.*(..))")
    public void notifyPointCut() {
        // nothing to do
    }

    /**
     * Before
     *
     * @param joinPoint join point
     * @since 1.4.0
     */
    @Before(value = "notifyPointCut()")
    @SuppressWarnings("unchecked")
    public void before(@NotNull JoinPoint joinPoint) {
        // 获取目标方法的参数信息
        Object[] obj = joinPoint.getArgs();
        for (Object argItem : obj) {
            if (argItem instanceof Message) {
                Class<?> idType = ClassUtils.getSuperClassT(argItem.getClass(), 0);

                if (((Message<?>) argItem).getMessageId() == null) {
                    if (Long.class.isAssignableFrom(idType)) {
                        Message<Long> message = (Message<Long>) argItem;
                        // todo-dong4j : (2022.03.27 20:48) [优先从 trace 中获取 traceId, 需要注意类型转换]
                        message.setMessageId(SnowflakeBuilder.builder());
                    } else if (String.class.isAssignableFrom(idType)) {
                        Message<String> paramVO = (Message<String>) argItem;
                        String traceId = Trace.context().get();
                        traceId = StringUtils.isBlank(traceId) ? StringUtils.getUid() : traceId;
                        paramVO.setMessageId(traceId);
                    } else {
                        throw new NotifyException("暂不支持 [{}] 类型的 message id", idType.getSimpleName());
                    }
                }
            }
        }
    }

}
