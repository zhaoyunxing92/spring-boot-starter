/**
 * Copyright(C) 2019 Hangzhou zhaoyunxing Technology Co., Ltd. All rights reserved.
 */
package io.github.sunny.spring.boot;

import com.aliyuncs.sts.transform.v20150401.AssumeRoleResponseUnmarshaller;
import io.github.sunny.spring.boot.config.OssStsProperties;
import io.github.sunny.spring.boot.service.impl.OssStsService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * oss sts自动注入bean
 *
 * @author zhaoyunxing
 * @date: 2019-09-11 15:38
 */
@Configuration
@ConditionalOnClass(AssumeRoleResponseUnmarshaller.class)
@EnableConfigurationProperties(OssStsProperties.class)
public class OssStsAutoConfiguration {

    private final OssStsProperties ossStsProperties;

    public OssStsAutoConfiguration(OssStsProperties ossStsProperties) {this.ossStsProperties = ossStsProperties;}

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "oss.sts", name = "enable", havingValue = "true", matchIfMissing = true)
    public OssStsService ossStsService() {
        return new OssStsService(ossStsProperties);
    }
}
