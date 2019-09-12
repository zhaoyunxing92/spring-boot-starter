/**
 * Copyright(C) 2019 Hangzhou zhaoyunxing Technology Co., Ltd. All rights reserved.
 */
package io.github.sunny.spring.boot.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author zhaoyunxing
 * @date: 2019-09-09 15:31
 * @desc:
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class OssSts extends Response {



    @JSONField(name = "AccessKeySecret")
    private String accessKeySecret;

    @JSONField(name = "SecurityToken")
    private String securityToken;

}
