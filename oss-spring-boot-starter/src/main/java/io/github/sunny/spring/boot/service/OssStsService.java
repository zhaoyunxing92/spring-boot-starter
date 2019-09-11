/**
 * Copyright(C) 2019 Hangzhou zhaoyunxing Technology Co., Ltd. All rights reserved.
 */
package io.github.sunny.spring.boot.service;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.auth.sts.AssumeRoleRequest;
import com.aliyuncs.auth.sts.AssumeRoleResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import io.github.sunny.spring.boot.config.OssStsProperties;
import io.github.sunny.spring.boot.entity.OssStsInfo;

/**
 * @author zhaoyunxing
 * @date: 2019-09-11 17:46
 * @desc:
 */
public class OssStsService {

    private final OssStsProperties ossStsProperties;

    public OssStsService(OssStsProperties ossStsProperties) {
        this.ossStsProperties = ossStsProperties;
    }

    public OssStsInfo buid() {
        OssStsInfo osi = new OssStsInfo();
        try {
            // 添加endpoint（直接使用STS endpoint，前两个参数留空，无需添加region ID）
            DefaultProfile.addEndpoint("", "", "Sts", ossStsProperties.getEndpoint());
            // 构造default profile（参数留空，无需添加region ID）
            IClientProfile profile = DefaultProfile.getProfile("", ossStsProperties.getAccessKeyId(), ossStsProperties.getAccessKeySecret());
            // 用profile构造client
            DefaultAcsClient client = new DefaultAcsClient(profile);
            final AssumeRoleRequest request = new AssumeRoleRequest();
            request.setMethod(MethodType.POST);
            request.setRoleArn(ossStsProperties.getRoleArn());
            request.setRoleSessionName(ossStsProperties.getRoleSessionName());
            // 若policy为空，则用户将获得该角色下所有权限
            request.setPolicy(JSONObject.toJSONString(ossStsProperties.getPolicy()));
            // 设置凭证有效时间
            request.setDurationSeconds(ossStsProperties.getDurationSeconds());

            final AssumeRoleResponse response = client.getAcsResponse(request);
            //        statement
            osi.setCode("200");
            osi.setMessage("获取sts成功");

            AssumeRoleResponse.Credentials credentials = response.getCredentials();

            osi.setAccessKeyId(credentials.getAccessKeyId());
            osi.setAccessKeySecret(credentials.getAccessKeySecret());
            osi.setRequestId(response.getRequestId());
            osi.setSecurityToken(credentials.getSecurityToken());
            osi.setExpiration(credentials.getExpiration());

        } catch (ClientException e) {
            osi.setMessage(e.getErrMsg());
            osi.setCode(e.getErrCode());
            osi.setRequestId(e.getRequestId());
        }
        return osi;
    }
}
