package com.taotong.tuanfan.Util;

import static java.lang.annotation.ElementType.PACKAGE;

/**
 * Created by Administrator on 2016/8/12 0012.
 * 保存SharedPreferences常量
 */
public class SPConstant {
    /**
     * sp的KEY常量
     */
    public static final String KEY_BIPAI = "BIPAI";//主SP，每次退出登录要清空
    public static final String KEY_SECONDARY = "SECONDARY";//次SP，主要用来处理手势引导等，退出不清空

    /**
     * 用户登录的sp
     */
    public static final String LOGIN_TYPE = "LOGIN_TYPE";//0未登录，1已登录

    //login get token save to SharedPreferences
    public static final String REQ_TOKEN = "REQ_TOKEN";

    public static final String USER_ID = "USER_ID";//用户的id
    public static final String USER_SIG = "USER_SIG";//腾讯IM分配给用户的sig
    public static final String USER_IDENTITY = "user_identity";//身份：0：无身份，1：机构，0：老师，0：家长，0：学员
    public static final String USER_NICK = "user_nick";
    public static final String USER_PHONE = "phone_number";//用户登录使用的手机号
    public static final String ORG_NICK = "org_nick";
    public static final String USER_SIGN = "user_sign";
    public static final String USER_AVATAR = "user_avatar";
    public static final String USER_ROOM_NUM = "user_room_num";
    public static final String LIVE_ANIMATOR = "live_animator";
    public static final String ANCHOR_STATUS = "anchor_status";
    public static final String TOTAL_COINS = "total_coins";
    public static final String LEVEL_BEAUTY = "beautyLevel";//美颜等级
    public static final String WHITEN_BEAUTY = "whiten"; //美白
    public static final String REDDEN_BEAUTY = "redden"; //红润

    public static final int IM_SDK_APPID = 1400022822;//腾讯IMsdk的APPID
    public static final int IM_ACCOUNT_TYPE = 9946;//腾讯IM
    public static final String APPLY_CHATROOM = "申请加入";
    public static final int IS_NEED_LOGIN = 6013;//错误码，SDK 未初始化或者用户未登录成功，请先登录，成功回调之后重试
    public static final int IS_ALREADY_MEMBER = 10013;//错误码，被邀请加入的用户已经是群成员
    public static final int HOST = 1;//主播
    public static final int MEMBER = 0;//观众
    public static final String MEMBER_TYPE = "member_type";  //成员状态：1主播，0观众
    /**
     * 腾讯IM消息key值
     */
    public static final String MSG_KEY_TYPE = "messageType";         // 下方的消息类型 : MSG_TYPE_
    public static final String MSG_KEY_UID = "userid_from";          // 发送信息的用户id
    public static final String MSG_KEY_NAME = "nick_from";           // 发送信息的用户昵称
    public static final String MSG_KEY_CONTENT = "sendMessageString";           // 发送的信息内容

    public static final String MSG_KEY_GIFT_NAME = "gift_name";           // 礼物名称
    public static final String MSG_KEY_GIFT_PIC = "gift_pic";           // 礼物图片
    public static final String MSG_KEY_GIFT_ID = "gift_id";           // 礼物ID
    public static final String MSG_KEY_NICK_FROM = "nick_from";           // 打赏人昵称
    public static final String MSG_KEY_NICK_FROM_AVATAR = "nick_from_avatar";           // 打赏人昵称
    public static final String MSG_KEY_COUNT = "count";           // 人数

    /**
     * 腾讯IM消息类型
     */
    public static final int MSG_TYPE_TEXT = 1;         // 普通的聊天消息
    public static final int MSG_TYPE_MEMBER_ENTER = 2;          // 用户加入直播, Group消息
    public static final int MSG_TYPE_THUMB = 3;           // 点赞消息, Demo中使用Group消息
    public static final int MSG_TYPE_HOST_FINISH = 4;           // 主播下播
    public static final int MSG_TYPE_HOST_NOTICE = 5;           // 主播公告
    public static final int MSG_TYPE_ENTER_TIP = 6;           // 用户或主播进入聊天室时的提示信息
    public static final int MSG_TYPE_ATTENTION = 7;           // 7为关注
    public static final int MSG_TYPE_PEOPLE_NUM = 8;           // 8为进出人数
    public static final int MSG_TYPE_GIFT_PLAY_TOUR = 9;          // 9为打赏

    public static final int MSG_TYPE_MORE = 13;           // 扩展字段，需自增，不可占用

    public static final String DEL_STATUS_ID = "DEL_STATUS_ID";//被删除的动态id
    public static final String ACTION_HOST_LEAVE = PACKAGE
            + ".ACTION_HOST_LEAVE";

    public static final String SEARCH_HISTORY = "searchHistory";           // 首页搜索历史
    public static final String LATITUDE = "Latitude";           // 维度
    public static final String LONGITUDE = "Longitude";           // 经度
    public static final String TEACH_JOB_HINT = "teach_job_hint";     //老师评语首次进入提示
    public static final String FIRST_CLASS_MANAGE = "class_first_gone";     //首次进入班级管理:0第一次
    public static final String FIRST_ORG_MEMBRE = "orgmember_first_gone";     //首次进入机构成员:0第一次
    public static final String HAVE_ORG = "is_user_org_showed";//用户启动时是否直接显示机构页面

    /**
     * 手势引导图等
     */
    public static final String FIRST_START = "is_user_guide_showed";//用户第一次启动时展示引导页：:true隐藏false展示
    public static final String FIRST_PARENTS = "parents_first_gone";     //家长首次进入提示修改孩子信息:0第一次
}
