# oss-spring-boot-starter

主要对阿里云的OSS封装下，按照spring boot starter规范开发

### 快速开始

* pom
```xml
<dependency>
    <groupId>io.github.sunny</groupId>
    <artifactId>oss-spring-boot-starter</artifactId>
    <version>1.0</version>
</dependency>
```
* application.yml

[policy](https://help.aliyun.com/document_detail/100680.html?spm=a2c4g.11186623.2.8.320841f0tGGFLR#h2-url-7)跟控制台配置取交集，如果不配置则是全部权限都有。

```yaml
oss:
  sts:
    access-key-id: LTAxxx
    access-key-secret: 0trwmxxx
    role-arn: acs:ram::xxx:role/xxx
    role-session-name: xxx
    policy:
      version: 10
      statement:
        - effect: Allow
          resource:
            - acs:oss:*:*:xxx
            - acs:oss:*:*:xxx/*
          action:
            - oss:ListObjects
            - oss:GetObject
  access-key-id: LTAxxx
  access-key-secret: 0trwmxxx
  bucket-name: xxx
  file-prefix: xxx/
```
> sts用的accessId最好单独出来不要混合使用

* java
```java
@RestController
@Slf4j
public class OssController {

    private final OssService ossService;

    @Autowired
    public OssController(OssService ossService) {
        this.ossService = ossService;
    }
    // sts临时授权模式
    @GetMapping("/sts")
    public String securityToken() {
        return ossService.getSecurityToken();
    }
    // web直传，服务器签名
    @GetMapping("/oss")
    public String signature() {
        return ossService.getSignature();
    }
}
```
