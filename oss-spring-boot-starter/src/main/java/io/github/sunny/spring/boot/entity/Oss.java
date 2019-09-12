/**
 * Copyright(C) 2019 Hangzhou zhaoyunxing Technology Co., Ltd. All rights reserved.
 */
package io.github.sunny.spring.boot.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zhaoyunxing
 * @date: 2019-09-12 16:01
 * @desc:
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Oss extends Response {
    /**
     * 规则
     */
    @JSONField(name = "Policy")
    private String policy;

    /**
     * 签名
     */
    @JSONField(name = "Signature")
    private String signature;
    /**
     * 文件前缀
     */
    @JSONField(name = "FilePrefix")
    private String filePrefix;
    /**
     * 地址
     */
    @JSONField(name = "Host")
    private String host;

}
