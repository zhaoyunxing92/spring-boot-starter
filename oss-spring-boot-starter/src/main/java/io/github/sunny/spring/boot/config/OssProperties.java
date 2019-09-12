/**
 * Copyright(C) 2019 Hangzhou zhaoyunxing Technology Co., Ltd. All rights reserved.
 */
package io.github.sunny.spring.boot.config;

import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Range;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Iterator;
import java.util.Set;

/**
 * @author zhaoyunxing
 * @date: 2019-09-12 14:58
 * @desc: web值传，后端签名
 */
@Data
@ToString
@ConfigurationProperties(prefix = "oss")
public class OssProperties {
    /**
     * 是否启用sts，默认启用
     */
    private Boolean enable;
    /**
     * oss服务的所有接入地址，每个地址的功能都相同，请尽量在同区域进行调用.默认值:oss-cn-hangzhou.aliyuncs.com
     */
    @NotBlank(message = "请配置【endpoint】属性")
    private String endpoint = "oss-cn-hangzhou.aliyuncs.com";
    /**
     * 子账号AK信息
     */
    @NotBlank(message = "请配置【accessKeyId】属性")
    private String accessKeyId;
    /**
     * 子账号AK信息
     */
    @NotBlank(message = "请配置【accessKeySecret】属性")
    private String accessKeySecret;
    /**
     * bucketname
     */
    @NotBlank(message = "请配置【bucketName】属性")
    private String bucketName;
    /**
     * 文件最大限制，单位mb，默认10MB,最大1GB
     */
    @NotNull(message = "请配置【fileMaxSize】属性")
    @Range(min = 0, max = 1024, message = "文件最大可以上传必须在{min}~{max}之间")
    private Integer fileMaxSize = 10;
    /**
     * 文件前缀
     */
    @NotNull(message = "请配置【fileMaxSize】属性")
    private String filePrefix;
    /**
     * 签名的有效时间，单位s,最小10，默认10秒
     */
    @NotNull(message = "请配置【durationSeconds】属性")
    @Range(min = 10, max = 60, message = "有效期必须在{min}~{max}之间")
    private Long durationSeconds = 10L;

    /**
     * bean初始化完成后进行参数验证
     */
    @PostConstruct
    public void validator() {
        // 参数验证
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<OssProperties>> validators = validator.validate(this);
        //todo：validators.iterator().hasNext()这个必须判断下
        Iterator<ConstraintViolation<OssProperties>> iterator = validators.iterator();
        Assert.isTrue(validators.isEmpty(), iterator.hasNext() ? iterator.next().getMessage() : "OssProperties参数验证不通过");
    }
}
