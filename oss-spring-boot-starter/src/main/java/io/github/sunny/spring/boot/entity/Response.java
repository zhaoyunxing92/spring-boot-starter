/**
 * Copyright(C) 2019 Hangzhou zhaoyunxing Technology Co., Ltd. All rights reserved.
 */
package io.github.sunny.spring.boot.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @author zhaoyunxing
 * @date: 2019-09-12 13:59
 * @desc:
 */
@Data
public class Response {

    @JSONField(name = "StatusCode")
    private String code;

    private String requestId;

    private String message;
}
