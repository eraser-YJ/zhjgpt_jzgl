package com.jc.csmp.common.tool;

import com.jc.csmp.warn.rule.domain.WarnRule;

public class RuleContext {
    //规则缓存
    private static CacheManager<WarnRule> ruleCache = new CacheManager<WarnRule>();

    public static WarnRule get(String key) {
        return ruleCache.get(key);
    }

    public static void put(String key, WarnRule rule) {
        ruleCache.put(key, rule);
    }

    public static void remove(String key) {
        ruleCache.clear(key);
    }

    public static void removeAll() {
        ruleCache.clearAll();
    }
}
