package com.jc.system.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Administrator
 * @date 2020-06-30
 */
public class CustomObjectMapper extends ObjectMapper{

	/**
	 * <mvc:annotation-driven>
		<mvc:message-converters>
	        <bean class="org.springframework.http.converter.json.MappingJacksonHttpMessageConverter">  
	            <property name="objectMapper" ref="customObjectMapper" />  
	        </bean>
	    </mvc:message-converters>
	</mvc:annotation-driven>
  
	<!-- 自定义的JSON ObjectMapper -->  
	<bean id="customObjectMapper" class="com.jc.system.common.util.CustomObjectMapper" />
	 */
	
	public CustomObjectMapper(){
        super();
        // 设置输入时忽略在JSON字符串中存在但Java对象实际没有的属性  
        //this.disable(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES);  
        //this.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
        // 允许单引号
        //this.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        // 字段和值都加引号
        //this.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        // 数字也加引号
        //this.configure(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS, true);
        //this.configure(JsonGenerator.Feature.QUOTE_NON_NUMERIC_NUMBERS, true);
        // 空值处理为空串
        /*
        this.getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>()
        {
            @Override
            public void serialize(
                    Object value,
                    JsonGenerator jg,
                    SerializerProvider sp) throws IOException, JsonProcessingException
            {
                jg.writeString("");
            }
        });
        */
        /*
        CustomSerializerFactory factory = new CustomSerializerFactory();  
        factory.addGenericMapping(Date.class, new JsonSerializer<Date>(){  
            @Override  
            public void serialize(Date value,   
                    JsonGenerator jsonGenerator,   
                    SerializerProvider provider)  
                    throws IOException, JsonProcessingException {  
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
                jsonGenerator.writeString(sdf.format(value));  
            }  
        });  
        this.setSerializerFactory(factory);
        */
    }  
	
}
