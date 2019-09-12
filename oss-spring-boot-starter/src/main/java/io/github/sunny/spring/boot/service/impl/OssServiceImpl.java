/**
 * Copyright(C) 2019 Hangzhou zhaoyunxing Technology Co., Ltd. All rights reserved.
 */
package io.github.sunny.spring.boot.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;
import io.github.sunny.spring.boot.config.OssProperties;
import io.github.sunny.spring.boot.entity.Oss;
import io.github.sunny.spring.boot.service.OssService;

import java.sql.Date;

/**
 * @author zhaoyunxing
 * @date: 2019-09-12 15:02
 * @desc:
 */
public class OssServiceImpl implements OssService {

    private OssProperties ossProperties;

    public OssServiceImpl(OssProperties ossProperties) {
        this.ossProperties = ossProperties;
    }

    @Override
    public String getSecurityToken() {
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
            oss.setExpire( String.valueOf(expireEndTime / 1000));
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
