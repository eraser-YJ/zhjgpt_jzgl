package com.jc.csmp.rabbitmq;

import com.jc.common.kit.StringUtil;
import com.jc.crypto.utils.SM2Utils;
import com.jc.digitalchina.domain.CmUserRelation;
import com.jc.digitalchina.domain.DigitalBean;
import com.jc.digitalchina.service.ICmUserRelationService;
import com.jc.digitalchina.util.AesUtils;
import com.jc.foundation.util.GlobalContext;
import com.jc.foundation.util.SpringContextHolder;
import com.jc.system.event.UserAddEvent;
import com.jc.system.event.UserDeleteEvent;
import com.jc.system.event.UserUpdateEvent;
import com.jc.system.security.SystemSecurityUtils;
import com.jc.system.security.domain.Unique;
import com.jc.system.security.domain.User;
import com.jc.system.security.service.IUniqueService;
import com.jc.system.security.service.IUserService;
import com.jc.system.sys.service.IPinUserService;
import com.jc.workflow.util.JsonUtil;
import com.rabbitmq.client.Channel;
import org.apache.log4j.Logger;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Author 常鹏
 * @Date 2020/9/11 14:01
 * @Version 1.0
 */
public class MqRecvHandler implements ChannelAwareMessageListener {
    protected final transient Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private IUserService userService;
    @Autowired
    private ICmUserRelationService cmUserRelationService;
    @Autowired
    private IUniqueService uniqueService;
    @Autowired
    private IPinUserService pinUserService;

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        boolean flag = false;
        try {
            String body = new String(message.getBody(), "UTF-8");
            if (!StringUtil.isEmpty(body)) {
                System.out.println("**************************************");
                System.out.println("****** body: " + body + " ********************************");
                System.out.println("**************************************");
                String decryptStr = AesUtils.aesDecryptBySalt(body, "qIY0HdNJytu4cxbLtNPZo/yc1mzY4v/UrrXYt6TKQpKpUrkuO2ztAn/dSEmSbYKa");
                if (!StringUtil.isEmpty(decryptStr)) {
                    System.out.println("**************************************");
                    System.out.println("****** decryptStr: " + decryptStr + " ********************************");
                    System.out.println("**************************************");
                    DigitalBean digitalBean = (DigitalBean) JsonUtil.json2Java(decryptStr, DigitalBean.class);
                    if (digitalBean != null) {
                        System.out.println("**************************************");
                        System.out.println("****** realName: " + digitalBean.getRealName() + " ********************************");
                        System.out.println("**************************************");
                        if(!StringUtil.isEmpty(digitalBean.getUserName())){
                            String checkUserName = ",security,audit,manager,admin,";
                            if (checkUserName.indexOf("," + digitalBean.getUserName() + ",") > -1) {
                                //避免与内置用户冲突
                                digitalBean.setUserName(digitalBean.getUserName() + "_admin");
                            }
                            log.error("userName: " + digitalBean.getUserName());
                            CmUserRelation relation = new CmUserRelation();
                            relation.setUuid(digitalBean.getUserGuid());
                            relation.setDeleteFlag(null);
                            relation = this.cmUserRelationService.get(relation);
                            User user = new User();
                            user.setLoginName(digitalBean.getUserName());
                            user.setDeleteFlag(null);
                            user = this.userService.get(user);
                            if (digitalBean.getOptType().intValue() == 3) {
                                //删除
                                log.error("userName: " + digitalBean.getUserName() + "删除");
                                if (user != null) {
                                    user.setPrimaryKeys(new String[]{ user.getId() });
                                    SpringContextHolder.getApplicationContext().publishEvent(new UserDeleteEvent(user));
                                    this.userService.delete(user);
                                }
                                if (relation != null) {
                                    //删除关系
                                    relation.setPrimaryKeys(new String[]{relation.getId()});
                                    this.cmUserRelationService.deleteByIds(relation);
                                }
                            } else {
                                //新增或修改
                                if (user == null) {
                                    user = new User(); user.setDeptId("100001"); user.setLoginName(digitalBean.getUserName()); user.setDisplayName(digitalBean.getRealName());
                                    user.setMobile(digitalBean.getPhone()); user.setWeight(70); user.setExtStr1("secret_type_0");
                                    user.setOrderNo(100);
                                    Unique unique = uniqueService.getOne(new Unique());
                                    if (unique != null) {
                                        user.setCode(unique.getUuid());
                                    }
                                    user.setStatus("status_0"); user.setSex("99"); user.setKind("KIND"); user.setEthnic("ethnic_01");
                                    user.setIsDriver(0); user.setIsLeader(0); user.setIsCheck("1"); user.setLockType(0); user.setIsAdmin(0);
                                    log.error("userName: " + digitalBean.getUserName() + "新增用户");
                                    SM2Utils.generateKeyPair();
                                    if(!com.jc.foundation.util.StringUtil.isEmpty(GlobalContext.getProperty("password.default.value"))){
                                        user.setPassword(SystemSecurityUtils.entryptPassword(GlobalContext.getProperty("password.default.value"), SM2Utils.getPubKey()));
                                    } else {
                                        user.setPassword(SystemSecurityUtils.entryptPassword(GlobalContext.PASSWORD_DEFAULT_VALUE, SM2Utils.getPubKey()));
                                    }
                                    user.setModifyPwdFlag(0);
                                    user.setKeyCode(SM2Utils.getPriKey());
                                    if (userService.save(user) == 1) {
                                        unique.setState("1");
                                        uniqueService.update(unique);
                                        if (relation == null) {
                                            relation = new CmUserRelation();
                                            relation.setUuid(digitalBean.getUserGuid());
                                            relation.setUserId(user.getId());
                                            this.cmUserRelationService.saveEntity(relation);
                                        } else {
                                            relation.setUserId(user.getId());
                                            relation.setDeleteFlag(0);
                                            this.cmUserRelationService.updateEntity(relation);
                                        }
                                        try { pinUserService.infoLoading(); } catch (Exception ex) {ex.printStackTrace();}
                                        SpringContextHolder.getApplicationContext().publishEvent(new UserAddEvent(user));
                                    }
                                } else {
                                    //用户存在得情况 修改信息
                                    log.error("userName: " + digitalBean.getUserName() + "修改用户");
                                    user.setLoginName(digitalBean.getUserName());
                                    user.setDisplayName(digitalBean.getRealName());
                                    user.setMobile(digitalBean.getPhone());
                                    if (user.getDeleteFlag().intValue() == 0) {
                                        this.userService.update2(user);
                                    } else {
                                        user.setPrimaryKeys(new String[] { user.getId() });
                                        this.userService.deleteBack(user);
                                    }
                                    SpringContextHolder.getApplicationContext().publishEvent(new UserUpdateEvent(user));
                                    if (relation == null) {
                                        relation = new CmUserRelation();
                                        relation.setUuid(digitalBean.getUserGuid());
                                        relation.setUserId(user.getId());
                                        this.cmUserRelationService.saveEntity(relation);
                                    } else {
                                        if (!relation.getUuid().equals(digitalBean.getUserGuid())) {
                                            relation.setUuid(digitalBean.getUserGuid());
                                        }
                                        relation.setUserId(user.getId());
                                        relation.setDeleteFlag(0);
                                        this.cmUserRelationService.updateEntity(relation);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            MessageProperties properties = message.getMessageProperties();
            long tag = properties.getDeliveryTag();
            if (flag) {
                //消息确认，发送成功
                channel.basicAck(tag, false);
            } else {
                channel.basicNack(tag, false, true);
            }
            //channel.basicNack(tag, false, true);
        }
    }
}
