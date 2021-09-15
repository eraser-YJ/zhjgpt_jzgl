package com.jc.csmp.doc.common;

import java.util.UUID;

public class IdUtil {

    /**
     * 主键id
     *
     * @return
     */
    public static String createId() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
