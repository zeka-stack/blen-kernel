package dev.dong4j.zeka.kernel.spi.constants;

/**
 * SPI框架远程通信常量定义接口，提供远程服务调用相关的常量定义
 * <p>
 * 该接口专门存放与远程通信、服务发现和负载均衡相关的常量
 * 主要用于支持分布式服务架构中的服务发现和集群配置
 * <p>
 * 主要功能：
 * - 定义远程服务调用相关常量
 * - 支持服务集群和备用地址配置
 * - 提供负载均衡和容灾相关配置
 * <p>
 * 注意：使用@SuppressWarnings("java:S1214")抑制接口中定义常量的警告
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2021.02.26 17:47
 * @since 1.0.0
 */
@SuppressWarnings("java:S1214")
public interface RemotingConstants {

    /** 备用地址配置键，用于指定服务的备用地址列表 */
    String BACKUP_KEY = "backup";
}
