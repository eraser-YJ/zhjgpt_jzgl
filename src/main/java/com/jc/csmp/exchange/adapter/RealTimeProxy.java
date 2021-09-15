package com.jc.csmp.exchange.adapter;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.http.message.BasicNameValuePair;

import java.text.SimpleDateFormat;
import java.util.*;

public class RealTimeProxy {


    private static final String url = "http://118.31.23.115:8080/Hoist/v3/listDeviceDataByListDeviceId";

    private static final String url1 = "http://118.31.23.115:8080/TowerCrane/v3/listRealDeviceData";

    /**
     * 取得token
     *
     * @return
     * @throws Exception
     */
    public static List<Map<String, Object>> getRealInfo(String codeList, Date startDate, Date end) throws Exception {
        List<BasicNameValuePair> params = new ArrayList<>();
        SimpleDateFormat f = new SimpleDateFormat("yyyyMMddHHmmss");
        params.add(new BasicNameValuePair("listDeviceId", codeList));
        params.add(new BasicNameValuePair("startTime", f.format(startDate)));
        params.add(new BasicNameValuePair("endTime", f.format(end)));
        JsonNode jsonNode = ApiProxy.post(url, params);
        List<Map<String, Object>> listMap = new ArrayList<>();
        jsonNode.forEach((JsonNode node) -> {
            Map<String, Object> item = new HashMap<>();
            Iterator<Map.Entry<String, JsonNode>> jsonNodes = node.fields();
            while (jsonNodes.hasNext()) {
                Map.Entry<String, JsonNode> subNode = jsonNodes.next();
                item.put(subNode.getKey(), subNode.getValue().toString());
            }
            listMap.add(item);
        });
        return  listMap;
    }

    /**
     * 取得token
     *
     * @return
     * @throws Exception
     */
    public static List<Map<String, Object>> getRealInfo1(String codeList, Date startDate, Date end) throws Exception {
        List<BasicNameValuePair> params = new ArrayList<>();
        SimpleDateFormat f = new SimpleDateFormat("yyyyMMddHHmmss");
        params.add(new BasicNameValuePair("listDeviceId", codeList));
        params.add(new BasicNameValuePair("startTime", f.format(startDate)));
        params.add(new BasicNameValuePair("endTime", f.format(end)));
        JsonNode jsonNode = ApiProxy.post(url1, params);
        List<Map<String, Object>> listMap = new ArrayList<>();
        jsonNode.forEach((JsonNode node) -> {
            Map<String, Object> item = new HashMap<>();
            Iterator<Map.Entry<String, JsonNode>> jsonNodes = node.fields();
            while (jsonNodes.hasNext()) {
                Map.Entry<String, JsonNode> subNode = jsonNodes.next();
                item.put(subNode.getKey(), subNode.getValue().toString());
            }
            listMap.add(item);
        });
        return  listMap;
    }
//    hoist_box_id	出厂编号	String
//    cage_id	设备编号	byte
//    project_id	项目id		String
//    project_name	项目名		String
//    record_number	备案号		String
//    hoist_time	时间	时间戳格式	date
//    real_time_height	高度	m	int
//    height_percentage	高度百分比	%	byte
//    real_time_lifting_weight	重量	t	int
//    weight_percentage	重量百分比	%	byte
//    real_time_gradient1	倾斜度1	°	int
//    real_time_gradient2	倾斜度2	°	int
//    tilt_percentage1	倾斜百分比1	%	byte
//    tilt_percentage2	倾斜百分比2	%	byte
//    real_time_number_of_people	人数	个	byte
//    real_time_speed	速度的原始格式。可直接使用转换后的wind_speed	m/s	byte
//    real_time_speed_direction	速度方向	0停止,1上,2下	byte
//    wind_speed	速度	m/s	byte
//    lock_state	门锁状态	从右到左
//    第0位前门,1开启0关闭
//    第1位后门,1开启0关闭
//    第2位门锁异常指示,1有异常0无异常	String
//    hoist_system_state	系统状态的原始格式。可直接使用转换后的system_state
//    system_state	系统状态(本字段只存在于实时值)	从右到左
//0~1位重量,00正常,01预警,10报警
//2~3位高度限位,00正常,01预警,10报警
//4~5位超速,00正常,01预警,10报警
//6~7位人数,00正常,01预警,10报警
//8~9位倾斜,00正常,01预警,10报警
//10位前门锁状态,0正常,1异常
//11位后门锁状态,0正常,1异常	String
//    hoist_level	级别(本字段只存在于报警值)	0正常,1预警,2报警	int
//    hoist_alarm_reason	警报原因(本字段只存在于报警值)	1重量报警
//2高度（冲顶）报警
//3速度报警
//4人数报警
//5倾斜报警	byte
//    real_time_or_alarm	数据的类型	1实时值,2报警值
//    real_time_or_alarm的概念：实时值是设备固定10秒发送一次的数据，这个数据可能是正常/预警/报警。报警值是设备报警时立刻发送的预警/报警数据	byte
//    driver_identification_state	驾驶员身份认证结果	0为未认证,非0为驾驶员工号
}
