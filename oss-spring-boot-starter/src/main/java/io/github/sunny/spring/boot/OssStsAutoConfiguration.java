/**
 * Copyright(C) 2019 Hangzhou zhaoyunxing Technology Co., Ltd. All rights reserved.
 */
package io.github.sunny.spring.boot;

import com.aliyun.oss.OSS;
import com.aliyuncs.OssAcsRequest;
import com.aliyuncs.sts.transform.v20150401.AssumeRoleResponseUnmarshaller;
import io.github.sunny.spring.boot.config.OssProperties;
import io.github.sunny.spring.boot.config.OssStsProperties;
import io.github.sunny.spring.boot.service.OssService;
import io.github.sunny.spring.boot.service.OssStsService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.logging.Logger;

/**
 * oss sts自动注入bean
 *
 * @author zhaoyunxing
 * @date: 2019-09-11 15:38
 */
@Configuration
@ConditionalOnClass(OssAcsRequest.class)
@EnableConfigurationProperties({OssStsProperties.class, OssProperties.class})
public class OssStsAutoConfiguration {

    private static Logger log = Logger.getLogger(OssStsAutoConfiguration.class.toString());
    private final OssStsProperties ossStsProperties;
    private final OssProperties ossProperties;

    public OssStsAutoConfiguration(OssStsProperties ossStsProperties, OssProperties ossProperties) {
        log.info("init oss starter version:" + OssStarterVersion.getVersion());
        this.ossStsProperties = ossStsProperties;
        this.ossProperties = ossProperties;
    }


    @Bean("ossStsService")
    @ConditionalOnMissingBean
    @ConditionalOnClass(AssumeRoleResponseUnmarshaller.class)
    @ConditionalOnProperty(prefix = "oss.sts", name = "enable", havingValue = "true", matchIfMissing = true)
    public OssStsService ossStsService() {
        return new OssStsService(ossStsProperties);
    }

    @Bean("ossService")
    @ConditionalOnMissingBean
    @ConditionalOnClass(OSS.class)
    @ConditionalOnProperty(prefix = "oss", name = "enable", havingValue = "true", matchIfMissing = true)
    public OssService ossService() {
        return new OssService(ossProperties);
    }
}
