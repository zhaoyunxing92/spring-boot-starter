/**
 * Copyright(C) 2019 Hangzhou zhaoyunxing Technology Co., Ltd. All rights reserved.
 */
package io.github.sunny.spring.boot.entity;

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
     * 子账号AK信息
     */
    private String accessKeyId;
    /**
     * 规则
     */
    private String policy;

    /**
     * 签名
     */
    private String signature;
    /**
     * 文件前缀
     */
    private String filePrefix;
    /**
     * 地址
     */
    private String host;
    /**
     * 过期时间
     */
    private String expire;
}
