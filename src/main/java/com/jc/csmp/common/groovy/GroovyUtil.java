package com.jc.csmp.common.groovy;

import java.util.Map;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import com.jc.foundation.util.JsonUtil;

public class GroovyUtil {

	/**
	 * @description 脚本执行
	 * @param scriptTxt
	 * @param paraMap
	 * @return
	 * @throws Exception
	 */
	public static Object action(String scriptTxt, Map<String, Object> paraMap, Map<String, Object> stdMap) throws Exception {
		ScriptEngineManager factory = new ScriptEngineManager();
		// 每次生成一个engine实例
		ScriptEngine engine = factory.getEngineByName("groovy");
		engine.eval(scriptTxt);

		Object value = ((Invocable) engine).invokeFunction("process", paraMap,stdMap);
		return value;
	}
}
