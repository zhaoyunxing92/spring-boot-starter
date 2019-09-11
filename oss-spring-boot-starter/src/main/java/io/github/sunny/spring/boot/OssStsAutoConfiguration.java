/**
 * Copyright(C) 2019 Hangzhou zhaoyunxing Technology Co., Ltd. All rights reserved.
 */
package io.github.sunny.spring.boot;

import com.aliyuncs.sts.transform.v20150401.AssumeRoleResponseUnmarshaller;
import io.github.sunny.spring.boot.config.OssStsProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * oss sts自动注入bean
 *
 * @author zhaoyunxing
 * @date: 2019-09-11 15:38
 */
@Configuration
@EnableConfigurationProperties(OssStsProperties.class)
@ConditionalOnClass(AssumeRoleResponseUnmarshaller.class)
@ConditionalOnProperty(prefix = "oss.sts", name = "enabled", havingValue = "true", matchIfMissing = true)
public class OssStsAutoConfiguration {
}
