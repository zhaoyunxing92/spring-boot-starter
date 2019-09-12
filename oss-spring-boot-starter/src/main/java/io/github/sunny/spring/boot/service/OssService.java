/**
 * Copyright(C) 2019 Hangzhou zhaoyunxing Technology Co., Ltd. All rights reserved.
 */
package io.github.sunny.spring.boot.service;

import io.github.sunny.spring.boot.entity.OssSts;

/**
 * @author zhaoyunxing
 * @date: 2019-09-12 13:43
 * @desc:
 */
public interface OssService {
    /**
     * 获取sts token
     *
     * @return {@link OssSts}
     */
    String getSecurityToken();
}
