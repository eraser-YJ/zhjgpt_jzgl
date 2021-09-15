package com.jc.csmp.plan.kit;

import java.util.UUID;

public class IdUtil {

    public static String createId() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
