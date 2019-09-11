/**
 * Copyright(C) 2019 Hangzhou zhaoyunxing Technology Co., Ltd. All rights reserved.
 */
package io.github.sunny.spring.boot.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 通过{@link Statement}描述授权语义，其中可以根据业务场景包含多条语义，每条包含对 Action、Effect、Resource 和 {@link Condition} 的描述。每次请求系统会逐条依次匹配检查，所有匹配成功的 Statement 会根据 Effect 的设置不同分为通过（Allow）、禁止（Deny），其中禁止（Deny）的优先。如果匹配成功的都为通过，该条请求即鉴权通过。如果匹配成功有一条禁止，或者没有任何条目匹配成功，该条请求被禁止访问。
 *
 * <p>复杂Policy示例,限制ip，请求头
 *
 * <pre>
 * {
 *     "Version": "1",
 *     "Statement": [
 *         {
 *             "Action": [
 *                 "oss:GetBucketAcl",
 *                 "oss:ListObjects"
 *             ],
 *             "Resource": [
 *                 "acs:oss:*:1775305056529849:mybucket"
 *             ],
 *             "Effect": "Allow"
 *         },
 *         {
 *             "Action": [
 *                 "oss:PutObject",
 *                 "oss:GetObject",
 *                 "oss:DeleteObject"
 *             ],
 *             "Resource": [
 *                 "acs:oss:*:1775305056529849:mybucket/file*"
 *             ],
 *             "Effect": "Allow"
 *         }
 *     ]
 * }
 * </pre>
 *
 * @author zhaoyunxing
 * @date: 2019-09-10 16:16
 * @desc:
 */
@Data
@ConfigurationProperties(prefix = "oss.sts.policy")
public class Policy {
    /**
     * Version定义了Policy的版本
     */
    @NotBlank(message = "请配置【version】属性")
    private String version = "1.0";
    /**
     * 通过Statement描述授权语义，
     */
    @Valid
    @JSONField(name = "Statement")
    private List<Statement> statement;
}
