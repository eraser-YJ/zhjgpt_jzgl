package com.jc.system.common.util;

import com.jc.foundation.util.MessageUtils;

import java.io.File;

/**
 * @author Administrator
 * @date 2020-06-30
 */
public class Constants {

	private Constants() {
		throw new IllegalStateException("Constants class");
	}

	
	/**
	 * 个人办公
	 */
	public static final int PO = 1;
	
	public static final String PO_TYPE_WEEK = "0";
	public static final String PO_TYPE_MONTH = "1";
	public static final String PO_TYPE_YEAR = "2";
	
	public static final String PO_PLANPART_SUM = "0";//工作计划内容：0-总结
	public static final String PO_PLANPART_PLAN = "1";//工作计划内容：1-计划 
	//public static final String[] PO_PLANTYPE_DEFAULT= {"0","1","2"};//默认周/月/年计划类型全选中
	//public static final String[] PO_PLANSTATE_DEFAULT= {"0","1"};//默认转发/接收全选中
	//public static final String[] PO_WORKTASK_TASKSTATUS= {"1","2","6"};//默认转发/接收全选中
	
	public static final Long PO_WEEKPLAN_RANGE= 7L;//周计划范围设定
	public static final Long PO_MONTHPLAN_RANGE= 31L;//月计划范围设定
	public static final Long PO_YEARPLAN_RANGE= 365L;//年计划范围设定

	public static final Integer PO_PLAN_WEEK_START= 1;//第一周
	public static final Integer PO_PLAN_MONTH_START= 1;//第一周
	public static final Integer PO_PLAN_MONTH_END= 12;//12月份
	public static final Integer PO_PLAN_WEEK_END= 52;//最后一周
	
	public static final String PO_PLAN_DIARY_CONTENT = "主要完成事项：<br/><br/>";
	public static final String PO_PLAN_DIARY_STANDARD = "<br/><br/>完成标准：<br/><br/>";
	public static final String PO_PLAN_CONTROLTYPE = "1";//维护范围
	public static final String PO_PLAN_PERMISSIONTYPE = "1";//访问权限类型(本人)
	public static final String PO_PLAN_TABLENAME = "tty_po_plan";//工作计划表
	public static final String PO_SUB_WEEK_JUMP_CONDITION = "diaryWatchWeekJump";//工作日程周计划下属
	public static final String PO_SUB_MONTH_JUMP_CONDITION = "diaryWatchMonthJump";//工作日程月计划下属
	public static final String PO_COLLECT_WEEK_JUMP_CONDITION = "weekCollectJump";//工作日程周计划汇总
	public static final String PO_COLLECT_MONTH_JUMP_CONDITION = "monthCollectJump";//工作日程月计划汇总
	public static final String PO_SQL_INFO_PLANSEND_COUNT = "queryPlanSendCount";//工作计划转发数量查询
	public static final String PO_SQL_INFO_PLANSEND = "queryPlanSend";//工作计划转发查询
	public static final String PO_SQL_INFO_MYPLANSEND_COUNT = "queryMyPlanSendCount";//工作计划转发数量查询(我的)
	public static final String PO_SQL_INFO_MYPLANSEND = "queryMyPlanSend";//工作计划转发查询(我的)
	public static final String PO_SQL_INFO_COUNT_COLLECT= "queryPlanCollectCount";//工作计划汇总数量查询
	public static final String PO_SQL_INFO_COLLECT= "queryPlanCollect";//工作计划汇总查询
	public static final String PO_SQL_INFO_INITYMD= "initPlanYMD";//初始化工作计划年月日
	public static final String PO_SQL_CONTRILLER= "planContriller";
	public static final String PO_SQL_PLANWORKFLOW= "workflowQuery";//工作计划工作流查询
	public static final String PO_SQL_DAYPLANCONTRILLER= "dayPlanContriller";//查看日报告状态
	public static final String PO_SQL_WEEKDATAECHO = "weekDataEcho";
	public static final String PO_SQL_BEFOREPLANDATA = "beforePlanData";
	public static final String PO_SQL_PLANANNO_COUNT = "queryWorkFlowAndAnnoCount";//带有批注信息的工作计划数量查询
	public static final String PO_SQL_WORKLOGANNO_COUNT = "queryWorklogAnnoCount";//带有批注信息的工作日志数量查询
	public static final String PO_SQL_PLANANNO = "queryWorkFlowAndAnno";//带有批注信息的工作计划查询
	public static final String PO_SQL_PLANSUBMIT_COUNT = "querySubmitOneCount";//工作计划是否审批通过
	public static final String PO_SQL_PLAN_PLANSEND_DELETE = "deleteLogicByPlanId";//工作计划级联删除工作计划转发
	public static final String PO_SQL_PLAN_SUBPLAN_QUERY_COUNT = "querySubPlanCount";//下属工作计划数量查询
	public static final String PO_SQL_PLAN_SUBPLAN_QUERY = "querySubPlan";//下属工作计划查询
	public static final String PO_SQL_PLAN_COUNT = "queryCount";
	public static final String PO_SQL_PLAN_QUERY = "query";
	public static final String PO_PROCESS_QUERY_FLAG = "2";
	public static final String PO_WEEKDIFFERENCE_Z = "0";
	public static final String PO_WEEKDIFFERENCE_W = "w";
	
	public static final String PO_PLAN_IS_READ = "1";//计划阅读状态
	public static final String PO_ANNO_IS_NOT_READ = "0";//批注未阅读状态
	public static final String PO_ANNO_IS_READ = "1";//批注已阅读状态
	public static final String PO_PLAN_DELETE_FORWARD="deleteForward";//转发删除
	
	public static final String PO_WORKTASK_NOTICETYPE = "消息提醒";
	
	/**------------工作日程begin------------*/
	/** 工作日程-周期时间节点：periodType 0 无定期 ; 1每天; 2 每周; 3 每月; 4每年
	 */
	public static final String PO_PERIODWAY_NONE = "0";
	public static final String PO_PERIODWAY_EVERYDAY = "1";
	public static final String PO_PERIODWAY_EVERYWEEK = "2";
	public static final String PO_PERIODWAY_EVERYMONTH = "3";
	public static final String PO_PERIODWAY_EVERYYEAR = "4";
	/** 工作日程-周期时间节点：periodType 0 无定期 ; 1每天; 2 每周; 3 每月; 4每年
	 */
	public static final String PO_PERIODTYPE_NONE = "0";
	/** 工作日程-周期日程修改时  0 修改当前周期性日程 ; 1修改所有周期性日程
	 */
	public static final String PO_MODIFY_ONE = "0";
	public static final String PO_MODIFY_ALL = "1";
	/** 工作日程-普通日程改成周期日程  0 普通日程修改 ; 1普通日程修改成周期性日程
	 */
	//0周期性编辑当前事件 1周期性编辑所有事件 2非周期性日程 3非周期性日程编辑成周期性日程 4周期性日程改成非周期性
	public static final String PO_ONE_TO_PERIOD_MODIFY_NO = "2";
	public static final String PO_ONE_TO_PERIOD_MODIFY_YES = "3";
	public static final String PO_PERIOD_TO_ONE_MODIFY = "4";
	public static final String PO_ONE_TO_MOVE_RESIZE_MODIFY = "5";
	
	/** 工作日程菜单标识-1日程安排 2下属日程 3共享查询 4领导日程
	 */
	public static final String PO_MENU_FLAG_MY_DIARY = "1";
	public static final String PO_MENU_FLAG_UNDERLING_DIARY = "2";
	public static final String PO_MENU_FLAG_SHARE_DIARY = "3";
	public static final String PO_MENU_FLAG_LEAD_DIARY = "4";
	/** 工作日程类型 0日程 1万年历
	 */
	public static final String PO_DIARY_TYPE_SCHEDULE = "0";
	public static final String PO_DIARY_TYPE_CHINESE_CALENDAR = "1";
	/** 工作日程删除标识 0 正常 1删除
	 */
	public static final Integer PO_DIARY_DELETE_FLAG = 0;
	public static final Integer PO_DIARY_DELETE_FLAG_DEL = 1;
	/** 工作日程共享标识 0 正常 1共享
	 */
	public static final String PO_DIARY_ISSHARE = "0";
	public static final String PO_DIARY_ISSHARE_NOT = "1";
	/** 工作日程模块来源 0-工作日程;1-日志导入;2-记事本导入;3-待办任务导入;4工作计划导入;5会务导入
	 */
	public static final String PO_DIARY_MODULEFLAG_SCHEDULE = "0";
	public static final String PO_DIARY_MODULEFLAG_LOG = "1";
	public static final String PO_DIARY_MODULEFLAG_NOTE = "2";
	public static final String PO_DIARY_MODULEFLAG_TASK = "3";
	public static final String PO_DIARY_MODULEFLAG_PLAN = "4";
	public static final String PO_DIARY_MODULEFLAG_MEET = "5";
	/** 用户是否是领导  0-非领导;1-领导
	 */
	public static final Integer PO_DIARY_IS_LEADER=1;
	/** 用户是否公开日程  0-不公开;1-公开
	 */
	public static final String PO_DIARY_OPEN_CALE = "1";
	/** 日程委托提醒方式  0-邮件;1-短信*/
	public static final String PO_DIARY_AGENT_MAIL = "0";
	public static final String PO_DIARY_AGENT_MSG = "1";
	/** 日程委托按键  0-不显示;1-显示*/
	public static final String PO_DIARY_AGENT_BTN_NONE="0";
	public static final String PO_DIARY_AGENT_BTN_SHOW="1";
	
	public static final String PO_DIARY_CONTROLSIDE_CONTROLTYPE="3";/**共享范围类型*/
	public static final String PO_DIARY_CONTROLSIDE_PERMISSIONTYPE="1";/**共享范围访问权限类型*/
	/**主日程标识*/
	public static final String PO_DIARY_MASTER = "0";
	
	/**------------工作日程end------------*/
	/**阅读状态 businessType 0工作计划 1工作日程 2工作日志*/
	public static final String PO_READING_STATUS_PLAN = "0";
	public static final String PO_READING_STATUS_DIARY = "1";
	/**------------督办协办begin------------*/
	public static final String TABLE_NAME = "TTY_PO_TASK";
	public static final String DELETE_FLAG_BY_ID="deleteFlagByIds";
	public static final String QUERY_TASK_TOT_COUNT="queryTaskTotCount";
	public static final String QUERY_TASK_TOTAL="queryTaskTotal";
	public static final String QUERY_WORK_TASK_LIST_OUT="queryWorkTaskListOut";
	public static final String QUERY_COUNT_OUT="queryCountOut";
	public static final String QUERY_COUNTUNION="queryCountUnion";
	public static final String QUERY_UNION="queryUnion";
	public static final String QUERY_COUNT_TASK="queryCount";
	public static final String QUERY_TASK="query";
	public static final String QUERY_TASK_PROC="queryTaskProc";
	public static final String GET_TASK_HIS_BY_ID_COUNT="getTaskHistByTaskIdCount";
	public static final String GET_TASK_HIS_LIST_BY_ID="getTaskHistListByTaskId";
	public static final String QUERY_PARENT_TASK="queryParentTask";
	public static final String CANCAL_TEMPLATE="cancalTemplate";
	public static final String QUERY_RELEVANCE="queryRelevance";
	public static final String QUERY_RELEVANCE_COUNT="queryRelevanceCount";
	public static final String TABLE_NAME_TEMP = "TTY_PO_TASK_TEMP";
	public static final String QUERY_ABEYANCE_TASK_LIST_OUT ="queryAbeyanceTaskListOut";
	public static final String QUERY_ZC_TASK_COUNT="queryZCTaskCount";
	public static final String QUERY_ZC_TASK="queryZCTask";
	public static final String QUERY_WORKTASK_COUNT ="queryWorkTaskCount";
	/**------------督办协办end------------*/
	
	/**
	 * 公文
	 */
	public static final int DOC = 2;

	/**
	 * 信息管理
	 */
	public static final int IM = 3;
	public static final String IM_INFO_TABLENAME = "tty_im_info";
	/**
	 * 互动交流
	 */
	public static final int INTERACT = 4;
	
	/**
	 * 行政办公
	 */
	public static final int GOVERN = 5;
	
	
	/**
	 * 知识管理
	 */
	public static final int KMS = 6;
	
	
	/**
	 * 人事管理
	 */
	public static final int HR = 7;
	
	/**
	 * 决策分析
	 */
	public static final int ANALYST = 8;
	
	/**
	 * 	流程管理
	 */
	public static final int WORK_FLOW = 9;
	
	/**
	 * 系统管理
	 */
	public static final int SYSTEM = 10;
	
	/**
	 * 文档管理
	 */
	public static final int ARCHIVE = 11;

	/**
	 * 公文流水号
	 */
	public static final int DOC_SEQ = 1;

	/**
	 *  发文
	 */
	public static final int DOC_SEND = 0;
	
	
	/**
	 * 收文
	 */
	public static final int DOC_RECEIVE = 1;
	
	
	/**
	 * 0 不提醒
            1 内部邮件
            2 短信
	 */
	public static final String NO_REMIND = "0";
	public static final String MAIL_REMIND = "2";
	public static final String MSG_REMIND = "1";
	/**
	 * 1 边界提醒 2固定间隔 3 周期日期 4 一次性
	 * */
	public static final String REMIND_SIDE = "1";
	public static final String REMIND_INTERVAL = "2";
	public static final String REMIND_CYCLE = "3";
	public static final String REMIND_ONCE = "4";
	
	/*提醒周期
    1 天
    2 周
    3 月
    4 年*/
	//天
	public static final String CYCLE_DAY = "1";
	//周
	public static final String CYCLE_WEEK = "2";

	// 月
	public static final String CYCLE_MONTH = "3";

	//年
	public static final String CYCLE_YEAR = "4";
	
	/**
	 * 流水号类型：0发文1收文2匿名（供文号使用）
	 */
	public static final String SEQ_SPECIES_SEND = "0";
	public static final String SEQ_SPECIES_GET = "1";
	public static final String SEQ_SPECIES_ANONYMITY = "2";
	
	
	public static final String SELECTED_INDEX_ONE = "1";
	public static final String SELECTED_INDEX_TWO = "2";
	public static final String SELECTED_INDEX_THREE = "3";
	public static final String SELECTED_INDEX_FOUR = "4";
	
	public static final int MILLISECOND_ONE_MINUTE = 60 * 1000;
	
	/**
	 * 批注信息类型  批注信息类型[0-工作计划;1-工作日程;2-工作日志;3-工作任务]
	 */
	public static final int ANNOTYPE_PLAN = 0;
	public static final int ANNOTYPE_DIARY = 1;
	public static final int ANNOTYPE_WORKLOG = 2;
	public static final int ANNOTYPE_WORKTASK = 3;
	/**
	 * 工作日志内容：CONTENT_TYPE 内容类型(0-今日日志;1-明日提醒)
	 */
	public static final int WORKLOG_CONTENT_TYPE_TODAY = 0;
	public static final int WORKLOG_CONTENT_TYPE_TOMORROW = 1;
	
/**----------------公文管理Start--------------*/	
	/**
	 * 公文元素：0禁用1启用
	 */
	public static final String BOOKREMARK_USE_END = "0";
	public static final String BOOKREMARK_USE_START = "1";
	/**
	 * 公文交换>>发送公文：0未发送1已发送
	 */	
	public static final String EXCHANGESEND_STATUS_NOT = "0";
	public static final String EXCHANGESEND_STATUS_SEND = "1";	
	/**
	 * 公文交换>>接收公文：0未签收1收回2拒收3签收
	 */	
	public static final String EXCHANGERECEIVE_STATUS_NOT = "0";
	public static final String EXCHANGERECEIVE_STATUS_BACK = "1";	
	public static final String EXCHANGERECEIVE_STATUS_REJECT = "2";	
	public static final String EXCHANGERECEIVE_STATUS_ACCEPT = "3";	
	/**
	 * 公文分发>>接收公文：0未读1收回2拒收3已读
	 */	
	public static final String INSIDEOUT_STATUS_NORMAL = "0";
	public static final String INSIDEOUT_STATUS_BACK = "1";	
	public static final String INSIDEOUT_STATUS_REJECT = "2";
	public static final String INSIDEOUT_STATUS_READ = "3";
	/**
	 * 表名
	 */		
	public static final String DOC_SEND_TABLE_NAME = "tty_doc_send";
	public static final String DOC_RECEIVE_TABLE_NAME = "tty_doc_receive";
	
/**----------------公文管理End--------------*/	
	
/**----------------行政办公Start--------------*/
	/**
	 * 行政办公:[0-车辆不在库;1-车辆在库]
	 */
	public static final String CAR_IS_HERE = "1";
	public static final String CAR_IS_NOT_HERE = "0";
	
	/**
	 * 行政办公:提醒默认值0-不提醒;1-短信;2-邮件
	 */
	public static final Integer DEFAULT_REMIND_VALUE = 0;
	public static final Integer REMIND_VALUE_MSG = 1;
	public static final Integer REMIND_VALUE_MAIL = 2;
	
	/**
	 * 行政办公:暂存状态[0-否，1-是]
	 */
	public static final String TEMPARARY_STATUS_NO = "0";
	public static final String TEMPARARY_STATUS_YES = "1";
	/**
	 * 行政办公:车辆出/归车状态[0-未出车，1-已出车 2-归车 3-不出车]
	 */
	public static final String CAR_READY = "0";
	public static final String CAR_OUT = "1";
	public static final String CAR_IN = "2";
	public static final String CAR_NO_OUT = "3";
	
	/**
	 * 行政办公：历史版本号默认值-1
	 */
	public static final Long REGIME_OLD_VERSION_DEFAULT = -1L;
	/**
	 * 行政办公：历史版本号默认值1
	 */
	public static final Long REGIME_VERSION_DEFAULT = 1L;
	
	/**
	 * 行政办公：人员是否是司机 1-是
	 */
	public static final Integer IS_DRIVER = 1;
	
	/**
	 * 行政办公：会议状态 0-申请 1-开始 2-结束
	 */
	public static final String MEETING_APPLY = "0";
	public static final String MEETING_START = "1";
	public static final String MEETING_END = "2";
	
	/**
	 * 行政办公：会议参与角色 0-召集人 1-参会人 2-纪要人3主持人
	 */
	public static final String MEETING_CONVENER = "0";
	public static final String MEETING_PERSON = "1";
	public static final String MEETING_RECO = "2";
	public static final String MEETING_HOST = "3";
	
	/**
	 * 行政办公：日程提醒 0-是 1-否
	 */
	public static final String MEETING_DAY_REMIND_YES = "0";
	public static final String MEETING_DAY_REMIND_NO = "1";
	public static final String PLAN_DIARY_CONTENT = "主要完成事项：";
	public static final String PLAN_DIARY_STANDARD = "完成标准：";
	
	/**
	 * 行政办公：召集人提醒 0-开始前 1-结束前
	 */
	public static final String MEETING_EVE_REMIND_YES = "0";
	public static final String MEETING_EVE_REMIND_NO = "1";
	/**
	 * 表名
	 */
	public final static String MEETING_INFO_TABLE_NAME = "TTY_AO_MEETING_INFO";
	public final static String CAR_INFO_TABLE_NAME = "TTY_AO_CAR_INFO";
	public static final String CAR_APPLY_TABLE_NAME = "TTY_AO_CAR_APPLY";
	public final static String REGIME_TABLE_NAME = "TTY_AO_REGIME";
	
	/**
	 * 制度状态 0-起草 1-审批通过 3-废止
	 */
	public final static String REFIME_STATUS_NO_PRACTISE = "regimestatus_0";
	public final static String REFIME_STATUS_NO_PUBLISH = "regimestatus_1";
	public final static String REFIME_STATUS_OFF = "regimestatus_3";
	
	/**
	 * SQL_ID
	 */
	public final static String SQL_QUERY_APP = "queryApp";
	public final static String SQL_QUERY_APP_COUNT = "queryAppCount";
	public static final String SQL_DISPATCHNODE = "queryDispatchNode";
	public static final String SQL_DISPATCHNODECOUNT = "queryDispatchNodeCount";
	public static final String SQL_DISPATCH = "queryDispatch";
	public static final String SQL_DISPATCHCOUNT = "queryDispatchCount";
	public static final String SQL_USE_APP = "queryAll";
	public static final String SQL_USE_APP_COUNT = "queryAllCount";
	public static final String SQL_MYAPPLY = "queryMyApply";
	public static final String SQL_MYAPPLY_COUNT = "queryMyApplyCount";
	public static final String SQL_USE_SEARCH = "queryUseSearch";
	public static final String SQL_USE_SEARCH_COUNT = "queryUseSearchCount";
	public static final String SQL_CAR_INFO_COUNT = "queryCarInfoCount";
	public static final String SQL_CAR_INFO = "queryCarInfo";
	public final static String SQL_MY_APPLY = "queryMyApply";
	public final static String SQL_MY_APPLY_COUNT = "queryMyApplyCount";
	public final static String SQL_SEARCH = "querySearch";
	public final static String SQL_SEARCH_COUNT = "querySearchCount";
	public final static String SQL_APPROVAL = "queryApproval";
	public final static String SQL_APPROVAL_COUNT = "queryApprovalCount";
	
	
	/**
	 * 日程设置
	 * 
	 */
	public final static String DAY_TITLE = "会议提醒";
	public final static String INNERMAN_CONTENT = "按时参加";
	public final static String CONNVENER_CONTENT = "会议召集";
	public final static String HOST_CONTENT = "会议主持";
	public final static String RECO_CONTENT = "会议纪要";
	
	/**
	 * 提醒类型 1-邮件 2-短信
	 * 
	 */
	public static final Long MEET_MAIL_REMIND = 1L;
	public static final Long MEET_MSG_REMIND = 2L;
/**----------------行政办公End--------------*/
	
/**----------------互动交流Start--------------*/
	    /**
	     * 邮件包含附件标识--是
	     */
		public static final Integer IC_MAIL_ISFILE_YES=1;
		 /**
	     * 邮件包含附件标识--否
	     */
		public static final Integer IC_MAIL_ISFILE_NO=0;
		/**
		 * 邮件星标标识--是
		 */
		public static final Integer IC_MAIL_STARMAIL_YES=1;
		/**
		 * 邮件星标标识--否
		 */
		public static final Integer IC_MAIL_STARMAIL_NO=0;
		/**
		 * 邮件短信提醒标识--是
		 */
		public static final Integer IC_MAIL_SMSALERT_YES=1;
		/**
		 * 邮件短信提醒标识--否
		 */
		public static final Integer IC_MAIL_SMSALERT_NO=0;
		/**
		 * 邮件紧急标识--是
		 */
		public static final Integer IC_MAIL_PRESSING_YES=1;
		/**
		 * 邮件紧急标识--否
		 */
		public static final Integer IC_MAIL_PRESSING_NO=0;
		/**
		 * 邮件加密标识--是
		 */
		public static final Integer IC_MAIL_ENCRYPTION_YES=1;
		/**
		 * 邮件加密标识--否
		 */
		public static final Integer IC_MAIL_ENCRYPTION_NO=0;
		/**
		 * 邮件回复提醒标识--是
		 */
		public static final Integer IC_MAIL_REPLYTEXTING_YES=1;
		/**
		 * 邮件回复提醒标识--否
		 */
		public static final Integer IC_MAIL_REPLYTEXTING_NO=0;
		/**
		 * 邮件是否读取标识--是
		 */
		public static final Integer IC_MAIL_MAILSTATUS_READED=1;
		/**
		 * 邮件是否读取标识--否
		 */
		public static final Integer IC_MAIL_MAILSTATUS_UNREAD=0;
		/**
		 * 邮件是否加签名--是
		 */
		public static final Integer IC_MAIL_SIGNATURE_YES=1;
		/**
		 * 邮件是否加签名--否
		 */
		public static final Integer IC_MAIL_SIGNATURE_NO=0;
		
		/**
		 * 邮件文件夹：子表内部邮箱草稿箱
		 */
		public static final Long IC_MAIL_MAILFOLDERID_DRAFT=0L;
		
		/**
		 * 邮件文件夹：已发送
		 */
		public static final Long IC_MAIL_MAILFOLDER_INBOX=1L;
		/**
		 * 邮件文件夹：草稿箱
		 */
		public static final Long IC_MAIL_MAILFOLDER_DRAFT=2L;
		/**
		 * 邮件文件夹：发件箱
		 */
		public static final Long IC_MAIL_MAILFOLDER_SENDED=3L;
		/**
		 * 邮件文件夹：废件箱
		 */
		public static final Long IC_MAIL_MAILFOLDER_DELETED=4L;
		/**
		 * 邮件文件夹：追回
		 */
		public static final Long IC_MAIL_MAILFOLDER_RECOVER=5L;
		
		/**
		 * 邮箱配置：内部邮箱
		 */
		public static final Long IC_MAIL_MAILBOX_INNER=1L;
		
		/**
		 * 0 正常收件人 
		 */
		public static final Integer IC_MAIL_RECEIVETYPE_TO=0;
		
		/**
		 * 1 抄送接收人 
		 */
		public static final Integer IC_MAIL_RECEIVETYPE_CC=1;
		/**
		 * 2 密送接收人 
		 */
		public static final Integer IC_MAIL_RECEIVETYPE_BCC=2;
		/**
		 * 3 群发单显
		 */
		public static final Integer IC_MAIL_RECEIVETYPE_SHOWSINGLE=3;
		/**
		 * 4内部邮件发件人
		 */
		public static final Integer IC_MAIL_RECEIVETYPE_INNERSENDER=4;
		
		/**
		 * 内部邮件提醒标记 0-未提醒
		 */
		public static final Integer IC_MAIL_UNREMIND_FLAG=0;
		/**
		 * 内部邮件提醒标记 1-已提醒
		 */
		public static final Integer IC_MAIL_REMIND_FLAG=1;
		
		/**
		 * 邮件回复标记 0-未回复
		 */
		public static final Integer IC_MAIL_UNREPLY_FLAG=0;
		/**
		 * 邮件回复标记 1-已回复
		 */
		public static final Integer IC_MAIL_REPLY_FLAG=1;
		/**
		 * 邮件追回标记 0-未追回
		 */
		public static final Integer IC_MAIL_UNRECOVER_FLAG=0;
		/**
		 * 邮件追回标记 1-已追回
		 */
		public static final Integer IC_MAIL_RECOVER_FLAG=1;
		
		public static final String IC_MAIL_SEND_MESSAGE = "您有新的邮件，请注意查收";
		
/**----------------互动交流End--------------*/
		

		/**------------公共范围控制start----------*/
		/**
		 * 1-部门 0-人员
		 */
		public static final String DEPT_TYPE = "1";
		public static final String USER_TYPE = "0";
		
		/**
		 * 1-维护范围 2-使用范围 3-查看范围
		 */
		public static final String REPAIR_TYPE = "1";
		public static final String USE_TYPE = "2";
		public static final String EDIT_TYPE = "3";
		
		public static final String CURRENT_USER = "1";
		public static final String DEPT = "2";
		public static final String ROLE = "3";
		public static final String ELSE = "4";
		/**------------公共范围控制end----------*/
		
		public static final String TEMPLATE_TABLE = "tty_doc_template";
		public static final String DJ_UPLOAD_DIR = "upload" + File.separator + "office";
		public static final String NO_TABLE = "tty_doc_no";
		public static final String SEQ_TABLE = "tty_doc_seq";
		
		/** 文档管理 Start**/
		
		/**
		 * 文件夹类型：公共文档
		 */
		public static final String ARC_FOLDER_TYPE_PUB_DOC="0";
		/**
		 * 文件夹类型：我的文档
		 */
		public static final String ARC_FOLDER_TYPE_MY_DOC="1";
		/**
		 * 文件夹类型：收藏文档
		 */
		public static final String ARC_FOLDER_TYPE_FAVOR_DOC="2";
		/**
		 * 文件夹类型：知识文档
		 */
		public static final String ARC_FOLDER_TYPE_KNOWLAGE_DOC="3";
		/**
		 * 文件夹类型：归档文档
		 */
		public static final String ARC_FOLDER_TYPE_FILE_DOC="4";
		/**
		 * 文件存放类型：0文档
		 */
		public static final String ARC_DOC_FILETYPE_DOC="0";
		/**
		 * 文件存放类型：1 知识 
		 */
		public static final String ARC_DOC_FILETYPE_KNOWLAGE="1";
		/**
		 * 文件存放类型：2 链接
		 */
		public static final String ARC_DOC_FILETYPE_LINK="2";
		/**
		 * 文件存放类型：3 收藏
		 */
		public static final String ARC_DOC_FILETYPE_FAVOR="3";
		/**
		 * 文件存放类型：4 归档
		 */
		public static final String ARC_DOC_FILETYPE_FILING="4";
		/**
		 * 文档内容类型 0 未知
		 */
		public static final String ARC_DOC_CONTENTTYPE_UNKNOWN="0";
		/**
		 * 文档内容类型 1  word
		 */
		public static final String ARC_DOC_CONTENTTYPE_WORD="1";
		/**
		 * 文档内容类型 2 excel
		 */
		public static final String ARC_DOC_CONTENTTYPE_EXCEL="2";
		/**
		 * 文档内容类型 3 ppt
		 */
		public static final String ARC_DOC_CONTENTTYPE_POWERPOINT="3";
		/**
		 * 文档管理_文档锁定状态(0-未锁定)
		 */
		public static final String ARC_DOC_LOCKSTATUS_UNLOCK="0";
		/**
		 * 文档管理_文档锁定状态(1-锁定)
		 */
		public static final String ARC_DOC_LOCKSTATUS_LOCKED="1";
		/**
		 * 文档管理_是否在回收站状态(1-是)
		 */
		public static final Integer ARC_DM_IN_RECYCLE_YES=1;
		/**
		 * 文档管理_是否在回收站状态(0-否)
		 */
		public static final Integer ARC_DM_IN_RECYCLE_NO=0;
		/**
		 * 文档管理_审计类型: 1 浏览
		 */
		public static final String ARC_AUDITHIS_VIEW="1";
		/**
		 * 文档管理_审计类型: 2 新建/上传/下载
		 */
		public static final String ARC_AUDITHIS_NEWUPDOWN="2";
		/**
		 * 文档管理_审计类型: 3 编辑
		 */
		public static final String ARC_AUDITHIS_EDIT="3";
		/**
		 * 文档管理_审计类型: 4 删除
		 */
		public static final String ARC_AUDITHIS_DELETE="4";
		/**
		 * 文档管理_审计类型: 5 复制/剪切
		 */
		public static final String ARC_AUDITHIS_COPYCUT="5";
		/**
		 * 文档管理_审计类型: 6 重命名
		 */
		public static final String ARC_AUDITHIS_RENAME="6";
		/**
		 * 文档管理_审计类型: 7 收藏
		 */
		public static final String ARC_AUDITHIS_COLLECT="7";
		/**
		 * 文档管理_审计类型: 8 版本管理
		 */
		public static final String ARC_AUDITHIS_VERSION="8";
		/**
		 * 文档管理_审计类型: 9 文档审计
		 */
		public static final String ARC_AUDITHIS_AUDI="9";
		/**
		 * 文档管理_审计类型: 10 文档关联
		 */
		public static final String ARC_AUDITHIS_RELATE="10";
		/**
		 * 文档管理_审计类型: 11 授权
		 */
		public static final String ARC_AUDITHIS_AUTH="11";
		/**
		 * 文档管理_审计类型: 12 回收站
		 */
		public static final String ARC_AUDITHIS_RECYCLE="12";
		/**
		 * 文档管理 审计 数据类型：0 文件夹
		 */
		public static final Integer ARC_AUDITHIS_DATATYPE_DIR=0;
		/**
		 * 文档管理 审计 数据类型：1 文档
		 */
		public static final Integer ARC_AUDITHIS_DATATYPE_DOC=1;
		/**
		 * 文档管理 归档门面参数：归档文件夹Id
		 */
		public static final String ARC_FACADE_PARAM_FOLDERID="ARC_FACADE_PARAM_FOLDERID";
		/**
		 * 文档管理 归档门面参数：链接
		 */
		public static final String ARC_FACADE_PARAM_DMLINK="ARC_FACADE_PARAM_DMLINK";
		/**
		 * 文档管理 归档门面参数：归档名称
		 */
		public static final String ARC_FACADE_PARAM_DMNAME="ARC_FACADE_PARAM_DMNAME";
		/**
		 * 文档管理 归档附件类型：表单
		 */
		public static final Integer ARC_ARCHIVE_FILETYPE_FORM=0;
		/**
		 * 文档管理 归档附件类型：正文
		 */
		public static final Integer ARC_ARCHIVE_FILETYPE_BODY=1;
		/**
		 * 文档管理 归档附件类型：附件
		 */
		public static final Integer ARC_ARCHIVE_FILETYPE_ATTACH=2;
		/** 文档管理 End**/
		
		/**----------------考勤管理Start--------------*/
		/**
		 * 考勤状态正常
		 */
		public static final Integer HR_ATT_NORMAL=0;
		/**
		 * 考勤状态早退
		 */
		public static final Integer HR_LEAVE_EARLY=1;
		/**
		 * 考勤状态迟到
		 */
		public static final Integer HR_BE_LATE=2;
		/**
		 * 考勤状态缺勤半天
		 */
		public static final Integer HR_ABSENCE_HALFDAY=4;
		/**
		 * 考勤状态缺勤一天
		 */
		public static final Integer HR_ABSENCE_DAYLONG=8;
		/**----------------考勤管理end--------------*/
		
		
		/**
		 * 方法描述：得到邮件标题
		 * @param busName    业务名称
		 * @return    邮件标题
		 * @author 王世元
		 * @version  2014年7月22日下午3:32:24
		 * @see
		 */
		public static String getEmailSubject(String busName){
			String[] param = new String[]{busName};
			
			String subject = MessageUtils.getMessage("JC_OA_COMMON_009",param);
			
			return subject;
		}
		
		/**
		 * 方法描述：得到邮件内容
		 * @param busName    业务名称
		 * @param content    业务内容
		 * @return    邮件内容
		 * @author 王世元
		 * @version  2014年7月22日下午3:35:18
		 * @see
		 */
		public static String getEmailContent(String busName,String content){
			String[] param = new String[]{busName,content};
			
			String eContent = MessageUtils.getMessage("JC_OA_COMMON_010",param);
			
			return eContent;
		}
		
		
		
		
		/**
		 * 方法描述：得到短信文本
		 * @param busName    业务名称
		 * @param content    业务内容
		 * @return    短信文本
		 * @author 王世元
		 * @version  2014年7月22日下午3:36:38
		 * @see
		 */
		public static String getSmsText(String busName,String content){
			String[] param = new String[]{busName,content};
			
			String smsText = MessageUtils.getMessage("JC_OA_COMMON_011",param);
			
			return smsText;
		}
}
