/**
 * Copyright(C) 2019 Hangzhou zhaoyunxing Technology Co., Ltd. All rights reserved.
 */
package io.github.sunny.spring.boot.service;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.auth.sts.AssumeRoleRequest;
import com.aliyuncs.auth.sts.AssumeRoleResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import io.github.sunny.spring.boot.config.OssProperties;
import io.github.sunny.spring.boot.config.OssStsProperties;
import io.github.sunny.spring.boot.entity.Oss;
import io.github.sunny.spring.boot.entity.OssSts;
import io.github.sunny.spring.boot.entity.Policy;

import java.sql.Date;

/**
 * @author zhaoyunxing
 * @date: 2019-09-12 15:02
 * @desc:
 */
public class OssService {

    private final OssProperties ossProperties;

    private final OssStsProperties ossStsProperties;

    public OssService(OssProperties ossProperties, OssStsProperties ossStsProperties) {
        this.ossProperties = ossProperties;
        this.ossStsProperties = ossStsProperties;
    }

    /**
     * 后端签名
     *
     * @return {@link Oss}
     */
    public String getSignature() {
        Oss oss = new Oss();
        try {
            String accessKeyId = ossProperties.getAccessKeyId();
            String endpoint = ossProperties.getEndpoint();
            String filePrefix = ossProperties.getFilePrefix();

            OSSClient client = new OSSClient(endpoint, new DefaultCredentialProvider(accessKeyId, ossProperties.getAccessKeySecret()), null);
            String host = getHost(ossProperties.getBucketName(), endpoint);
            long expireEndTime = System.currentTimeMillis() + ossProperties.getDurationSeconds() * 1000;

            PolicyConditions policyConds = new PolicyConditions();
            policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, ossProperties.getFileMaxSize());

            policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, filePrefix);

            String postPolicy = client.generatePostPolicy(new Date(expireEndTime), policyConds);
            byte[] binaryData = postPolicy.getBytes("utf-8");
            String encodedPolicy = BinaryUtil.toBase64String(binaryData);
            String postSignature = client.calculatePostSignature(postPolicy);

            oss.setAccessKeyId(accessKeyId);
            oss.setExpiration(String.valueOf(expireEndTime / 1000));
            oss.setHost(host);
            oss.setFilePrefix(filePrefix);
            oss.setPolicy(encodedPolicy);
            oss.setSignature(postSignature);
            oss.setMessage("获取签名成功");
        } catch (Exception ex) {
            oss.setCode("500");
            oss.setMessage(ex.getMessage());
        }
        return JSONObject.toJSONString(oss);
    }

    /**
     * 获取签名token
     *
     * @return
     */
    public String getSecurityToken() {
        OssSts osi = new OssSts();
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
            Policy policy = ossStsProperties.getPolicy();
            request.setPolicy(null == policy || null == policy.getStatement() ? null : JSONObject.toJSONString(policy));
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

        } catch (ClientException ex) {
            osi.setMessage(ex.getErrMsg());
            osi.setCode(ex.getErrCode());
            osi.setRequestId(ex.getRequestId());
        }
        return JSONObject.toJSONString(osi);
    }

    /**
     * host的格式为 bucketname.endpoint
     *
     * @param bucketName oss bucket name
     * @param endpoint   oss服务端点
     * @return String
     */
    private String getHost(String bucketName, String endpoint) {
        return "http://" + bucketName + "." + endpoint;
    }
}
