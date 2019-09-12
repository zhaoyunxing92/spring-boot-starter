/**
 * Copyright(C) 2019 Hangzhou zhaoyunxing Technology Co., Ltd. All rights reserved.
 */
package io.github.sunny.spring.boot;

/**
 * @author zhaoyunxing
 * @date: 2019-09-12 16:29
 * @desc:
 */
public class OssStarterVersion {
    private OssStarterVersion() {
    }
    /**
     * Return the full version string of the present oss starter codebase, or {@code null}
     * if it cannot be determined.
     * @return the version of oss starter or {@code null}
     * @see Package#getImplementationVersion()
     */
    public static String getVersion() {
        Package pkg = OssStarterVersion.class.getPackage();
        return (pkg != null) ? pkg.getImplementationVersion() : null;
    }
}
