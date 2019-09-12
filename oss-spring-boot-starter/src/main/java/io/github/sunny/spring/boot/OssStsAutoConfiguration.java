/**
 * Copyright(C) 2019 Hangzhou zhaoyunxing Technology Co., Ltd. All rights reserved.
 */
package io.github.sunny.spring.boot;

import com.aliyun.oss.OSS;
import com.aliyuncs.OssAcsRequest;
import io.github.sunny.spring.boot.config.OssProperties;
import io.github.sunny.spring.boot.config.OssStsProperties;
import io.github.sunny.spring.boot.service.OssService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Iterator;
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
@ConditionalOnProperty(prefix = "oss", name = "enable", havingValue = "true", matchIfMissing = true)
public class OssStsAutoConfiguration {

    private static Logger log = Logger.getLogger(OssStsAutoConfiguration.class.toString());
    private final OssStsProperties ossStsProperties;
    private final OssProperties ossProperties;
    private static Validator validator;

    static {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    public OssStsAutoConfiguration(OssStsProperties ossStsProperties, OssProperties ossProperties) {
        log.info("init oss starter version:" + OssStarterVersion.getVersion());
        this.ossStsProperties = ossStsProperties;
        this.ossProperties = ossProperties;
        // 参数验证
        //todo：validators.iterator().hasNext()这个必须判断下
        Iterator<ConstraintViolation<OssStsProperties>> sts = validator.validate(ossStsProperties).iterator();
        Assert.isTrue(validator.validate(ossStsProperties).isEmpty(), sts.hasNext() ? sts.next().getMessage() : "OssStsProperties参数验证不通过");

        Iterator<ConstraintViolation<OssProperties>> oss = validator.validate(ossProperties).iterator();
        Assert.isTrue(validator.validate(ossProperties).isEmpty(), oss.hasNext() ? oss.next().getMessage() : "OssProperties参数验证不通过");

    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnClass(OSS.class)
    public OssService ossService() {
        return new OssService(ossProperties,ossStsProperties);
    }
}
