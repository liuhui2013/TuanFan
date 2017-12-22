package com.taotong.tuanfan.Util;

/**
 * Created by roffee on 2017/5/23 16:58.
 * Contact with 460545614@qq.com
 *
 * @IPConstants: 主要写入ip与url恒量
 */
public class IPConstants {
    public static final String SERVER_NORMAL = "http://doubihai.com/";//默认正式服务器---------GlobalConstants.IP_TYPE = 0
    public static final String SERVER_TEST = "http://118.178.235.173:6060/";//测试服务器----GlobalConstants.IP_TYPE = 1
    public static final String SERVER_SPARE = "http://118.178.235.173/";//备用环境服务器-----------GlobalConstants.IP_TYPE = 2
    public static final String SERVER = GlobalConstants.IP_TYPE == 1 ? SERVER_TEST : GlobalConstants.IP_TYPE == 2 ? SERVER_SPARE : SERVER_NORMAL;
    public static final String QINIU = "http://vod.doushow.com/";

    //    TODO 0.检测是否登录
    public static final String IS_LOGIN = SERVER + "?m=home&c=user&a=islogin";
    //    TODO 0.检测是否需要更新
    public static final String APP_VERSION = SERVER + "index.php?m=home&c=user&a=appversion";
    //TODO 1.会员手机号登录333
//    public static final String URL_METHOD = SERVER + "index.php?m=home&c=user&a=login";
    //TODO 1.登录新接口
    public static final String LOGIN_APP = SERVER + "index.php?m=home&c=user&a=loginapp";
    //TODO 2.创建群组(group_name,cover_url)
    public static final String CREATE_GROUP = SERVER + "?m=home&c=group&a=creategroup";
    //TODO 4.动态详情
    public static final String VIEW_CONTENT = SERVER + "?m=home&c=content&a=viewcontent";
    //TODO  更新当前登录用户信息
    public static final String UPDATE_USERINFO = SERVER + "index.php?m=home&c=user&a=updateuser";
    //更新群组信息
    public static final String EDITGROUP = SERVER + "?m=home&c=group&a=editgroup";
    //TODO 9.会员手机号注册
    public static final String LOGIN_REGISTER = SERVER + "index.php?m=home&c=user&a=register";
    //TODO 获取手机验证码                               index.php?m=home&c=user&a=getsmsverify
    public static final String LOGIN_GAIN_INFO = SERVER + "index.php?m=home&c=user&a=getsmsverify";
    //TODO 10.加入班级
    public static final String JOIN_GROUP = SERVER + "?m=home&c=group&a=joingroup";
    //TODO 11.消息列表
    public static final String MESSAGE_LIST = SERVER + "?m=home&c=message&a=messagelist";
    //TODO 12.管理员同意群组加入申请
    public static final String CONFIRM_JOIN_GROUP = SERVER + "?m=home&c=group&a=confirmjoingroup";
    //TODO 14.群组信息
    public static final String GROUP_INFO = SERVER + "?m=home&c=group&a=groupindexinfo";
    //TODO 15.发布动态
    public static final String GROUP_Content = SERVER + "?m=home&c=content&a=pubcontent";
    //TODO 17。获得上传图片的接口 http://42.120.11.155/?m=home&c=index&a=gettoken
    public static final String GROUP_CLASSS_TOKEN = SERVER + "?m=home&c=index&a=gettoken";
    //TODO 20.退出登录
    public static final String LOGOUT = SERVER + "?m=home&c=user&a=logout";
    //TODO 21.用户反馈信息
    public static final String FEEDBACK = SERVER + "?m=home&c=user&a=feedback";
    //TODO 22.首页的点赞
    public static final String LIKE = SERVER + "?m=home&c=content&a=like";

    public static final String UNLIKE = SERVER + "?m=home&c=content&a=unlike";

    public static final String TOP_HOME = SERVER + "?m=home&c=content&a=topslider";
    //TODO 24.动态详情页发布作品评论
    public static final String PUBCOMMENT = SERVER + "?m=home&c=content&a=pubcomment";
    //TODO 26.班级搜索成员
    public static final String SERACH_MEMBER = SERVER + "?m=home&c=group&a=searchgroupmember";
    //TODO 27.管理员解散群组
    public static final String DELETE_GROUP = SERVER + "?m=home&c=group&a=deletegroup";
    //TODO 29.删除群组成员
    public static final String DELG_GROUPMEMBER = SERVER + "?m=home&c=group&a=delgroupmember";
    //TODO 用户通过手机验证找回密码
    public static final String RESET_PWD = SERVER + "index.php?m=home&c=user&a=resetpwd";
    /*机构创建的接口*/
    //TODO 32,创建机构
    public static final String CREATE_CREATEORG = SERVER + "?m=home&c=orgnization&a=createorg";
    //TODO 33.我创建的机构
    public static final String ORGNIZATION_MYORG = SERVER + "?m=home&c=orgnization&a=myorg";
    //TODO 35.机构设置首页信息
    public static final String ORGNIZATION_INFO = SERVER + "?m=home&c=orgnization&a=orgindexinfo";
    //更新机构信息
    public static final String EDITORG = SERVER + "?m=home&c=orgnization&a=editorg";
    //TODO 36.机构搜索
    public static final String SEARCH_ORG = SERVER + "?m=home&c=orgnization&a=searchorg";
    //TODO 10.加入机构
    public static final String JOIN_ORG = SERVER + "?m=home&c=orgnization&a=joinorg";
    //TODO 36.机构动态列表
    public static final String ORGNIZATION_LIST = SERVER + "?m=home&c=content&a=orgcontentlist";
    public static final String ORG_JOIN = SERVER + "?m=home&c=orgnization&a=orgjoined";
    //TODO 某机构明星学员列表
    public static final String STARMEBER = SERVER + "?m=home&c=orgnization&a=starmember";
    //TODO 机构发布的信息列表
    public static final String ORG_CONTENT = SERVER + "?m=home&c=orgnization&a=orgcontent";
    //TODO 某机构成员列表
    public static final String ORG_MEMBER = SERVER + "?m=home&c=orgnization&a=org_member";
    //TODO 搜索机构成员
    public static final String SERACH_ORG_MEMBER = SERVER + "?m=home&c=orgnization&a=searchorgmember";
    //TODO 删除机构成员
    public static final String DEL_ORG_MEMBER = SERVER + "?m=home&c=orgnization&a=delorgmember";
    //TODO 机构管理员
    public static final String ORG_ADMIN = SERVER + "?m=home&c=orgnization&a=orgadminlist";
    //TODO 添加机构管理员
    public static final String ADD_ADMIN = SERVER + "?m=home&c=orgnization&a=addorgadmin";
    //TODO 删除机构管理员
    public static final String DEL_ADMIN = SERVER + "?m=home&c=orgnization&a=delorgadmin";
    //TODO 明星学员
    public static final String ORG_STAR = SERVER + "?m=home&c=orgnization&a=starmember";
    //TODO 添加明星学员
    public static final String ADD_STAR = SERVER + "?m=home&c=orgnization&a=addstarmember";
    //TODO 删除明星学员
    public static final String DEL_STAR = SERVER + "?m=home&c=orgnization&a=delstarmember";
    //TODO 机构发布内容标签
    public static final String ORG_STATUS_TAG = SERVER + "?m=home&c=content&a=orgtags";

    //获取发布任务标签
    public static final String GROUP_PUB_TAG = SERVER + "?m=home&c=content&a=getpubtasktag";

    //TODO 机构发布内容
    public static final String ORG_CREATE_STATUS = SERVER + "?m=home&c=orgnization&a=puborgcontent";
    //TODO 发布练习
    public static final String PUB_TASK = SERVER + "?m=home&c=content&a=pubtask";
    /*发布练习*/
    //TODO  班级任务列表(每日任务/历史任务)【机构新 学生端查看批阅】
    public static final String GROUP_LIST = SERVER + "index.php?m=home&c=content&a=grouptasklist";
    //TODO  任务详情
    public static final String TASK_DETAIL = SERVER + "index.php?m=home&c=content&a=taskdetail";
    //TODO  用户练习列表（最新最赞）
    public static final String MEMBER_TASK = SERVER + "index.php?m=home&c=content&a=membertasklist";
    //TODO 批阅练习列表
    public static final String READ_TASK_LIST = SERVER + "index.php?m=home&c=content&a=readtasklist";
    public static final String READ_COMMENT = SERVER + "?m=home&c=content&a=viewreadtask  ";
    //TODO 删除消息
    public static final String DELETE_MESSAGE = SERVER + "?m=home&c=message&a=deletemessage ";
    //TODO 删除动态
    public static final String DELETE_CONTENT = SERVER + "?m=home&c=content&a=deletecontent";
    //TODO 录音播放页面背景图
    public static final String RECORD_PLAYER_BACKGROUND = SERVER + "index.php?m=home&c=material&a=getrecordcover";
    // TODO 编辑动态
    public static final String EDIT_CONTENT = SERVER + "index.php?m=home&c=content&a=editcontent";
    public static final String CLIENT_ID = SERVER + "index.php?m=home&c=user&a=addclientid";
    // TODO 获取腾讯IM的SIG值
    public static final String GET_SIG = SERVER + "index.php?m=home&c=user&a=gettxsignature";
    //TODO 付费精品获取课程列表 http://doubihai.com/index.php?m=home&c=course&a=courseList
    public static final String PAY_BOUTIQUE_COURSE = SERVER + "index.php?m=home&c=course&a=courseList";
    //TODO 付费精品获取课程分类 http://doubihai.com/?m=home&c=course&a=topCourseCategoryList
    public static final String PAY_BOUTIQUE_COURSE_CLASSIFY = SERVER + "?m=home&c=course&a=topCourseCategoryList";
    //TODO 名人堂
    public static final String TOP_SLIDER = SERVER + "index.php?m=home&c=call&a=topSlider";
    //todo 名人堂详情页    "http://doubihai.com/index.php?m=home&c=call&a=detailpage"
    public static final String DETAIL_PAGE = SERVER + "index.php?m=home&c=call&a=detailpage";
    //TODO 付费精品获取推荐课程列表 http://doubihai.com/?m=home&c=course&a=recommendCourseList
    public static final String PAY_BOUTIQUE_RECOMMEND_CLASSIFY = SERVER + "?m=home&c=course&a=recommendCourseList";
    //TODO 获取课程详情                  http://doubihai.com/index.php?m=home&c=course&a=courseDetails&course_id=16
    public static final String PAY_DETAIL_COURSE = SERVER + "index.php?m=home&c=course&a=courseDetails&course_id=16";
    //TODO 获取课程评论列表                http://doubihai.com/index.php?m=home&c=course&a=courseComment
    public static final String PAY_DETAIL_EVALUATE = SERVER + "index.php?m=home&c=course&a=courseComment";
    //TODO 课程评论                     http://doubihai.com/?m=home&c=course&a=paycomment
    public static final String COURSE_EVALUATE = SERVER + "?m=home&c=course&a=paycomment";
    //TODO 获取更多课程课程列表 http://doubihai.com/index.php?m=home&c=course&a=userCourseMore
    public static final String PAY_MORE_COURSE = SERVER + "index.php?m=home&c=course&a=userCourseMore";
    //TODO 新版 获取当前登录用户信息   http://42.120.11.155/index.php?m=home&c=user&a=userinfo
    public static final String USER_INFO = SERVER + "index.php?m=home&c=user&a=userinfo";
    //TODO 申请直播审核  http://doubihai.com/index.php?m=home&c=call&a=authentication
    public static final String AUTHER = SERVER + "index.php?m=home&c=call&a=authentication";
    //TODO 主播状态（验证是否是主播） http://doubihai.com/index.php?m=home&c=live&a=isAnchor
    //个人中心
    public static final String PERSON_CENTER = SERVER + "index.php?m=home&c=call&a=personage";
    //个人中心更多视频
    public static final String PERSON_ALL_VIDEO = SERVER + "index.php?m=home&c=course&a=AllVideo";
    //个人中心更换头像
    public static final String PERSON_BG = SERVER + "index.php?m=home&c=call&a=altersetting";
    //个人中心更多动态
    public static final String PERSON_ALL_STATUS = SERVER + "?m=home&c=work&a=userwork";
    //首页热门视频推荐
    public static final String RECOMMEND_VIDEO = SERVER + "index.php?m=home&c=content&a=recommendvideo";
    //首页今日课堂
    public static final String TODAY_CLASS = SERVER + "index.php?m=home&c=call&a=todayclass";
    //首页热门搜索关键词
    public static final String HOT_KEY_WORD = SERVER + "index.php?m=home&c=call&a=hotkeyword";
    //TODO 获取直播标题和封面 http://doubihai.com/index.php?m=home&c=live&a=getlastlive
    public static final String LIVE_TITLE_COVER = SERVER + "index.php?m=home&c=live&a=getlastlive";
    //TODO 直播礼物列表 http://doubihai.com/index.php?m=home&c=gift&a=getgiftlist
    public static final String LIVE_GIFT_LIST = SERVER + "index.php?m=home&c=gift&a=getgiftlist";
    //TODO 开始直播           http://doubihai.com/index.php?m=home&c=live&a=startlive
    public static final String START_LIVE = SERVER + "index.php?m=home&c=live&a=startlive";
    //TODO 更新直播状态 http://doubihai.com/?m=home&c=live&a=updatelivestatus
    public static final String UPDATE_LIVE_STATUS = SERVER + "?m=home&c=live&a=updatelivestatus";
    //TODO 直播列表              http://doubihai.com/index.php?m=home&c=live&a=livelistinfo
    public static final String LIVE_LIST = SERVER + "index.php?m=home&c=live&a=livelistinfo ";
    //TODO 礼物打赏 http://doubihai.com/index.php?m=home&c=gift&a=rewardgift
    public static final String LIVE_GIFT_PLAY_TOUR = SERVER + "index.php?m=home&c=gift&a=rewardgift";
    //TODO 充值套餐列表 http://doubihai.com/index.php?m=home&c=gift&a=getgiftcombo
    public static final String LIVE_GIFT_RECHARGE = SERVER + "index.php?m=home&c=gift&a=getgiftcombo";
    //TODO 观众查看主播/其它观众 http://doubihai.com/index.php?m=home&c=live&a=liveuserinfo
    public static final String LIVE_ANCHOR_AVATAR = SERVER + "index.php?m=home&c=live&a=liveuserinfo";
    //TODO 关注                   http://doubihai.com/index.php?m=home&c=live&a=follow
    public static final String LIVE_ATTENTION = SERVER + "index.php?m=home&c=live&a=follow";
    //TODO 查看本场直播结果   result  http://doubihai.com/index.php?m=home&c=live&a=finishlive
    public static final String LIVE_RESULT_INCOME = SERVER + "index.php?m=home&c=live&a=finishlive";
    //TODO 查看收益       http://doubihai.com/index.php?m=home&c=live&a=viewgains
    public static final String LIVE_INCOME = SERVER + "index.php?m=home&c=live&a=viewgains";
    //TODO 提现接口     http://doubihai.com/index.php?m=home&c=live&a=withdrawcash
    public static final String LIVE_WITHDRAW = SERVER + "index.php?m=home&c=live&a=withdrawcash";
    //TODO 直播配置接口             http://doubihai.com/index.php?m=home&c=live&a=liveconfig
    public static final String LIVE_ALLOCATION = SERVER + "index.php?m=home&c=live&a=liveconfig";
    //TODO 直播间在线人数统计        http://doubihai.com/index.php?m=home&c=live&a=onlineusers
    public static final String LIVE_ON_LINE_NUM = SERVER + "index.php?m=home&c=live&a=onlineusers";
    //TODO 视频详情页
    public static final String VIDEO_DETAILS = SERVER + "index.php?m=home&c=course&a=videodetails";
    //TODO 视频点赞
    public static final String VIDEO_LIKE = SERVER + "index.php?m=home&c=course&a=videounlike";
    //TODO 专辑视频点赞
    public static final String ALBUM_VIDEO_LIKE = SERVER + "index.php?m=home&c=course&a=specialunlike";
    //TODO 视频分享
    public static final String VIDEO_SHARE = SERVER + "index.php?m=home&c=course&a=share";
    //TODO 专辑视频分享
    public static final String ALBUM_VIDEO_SHARE = SERVER + "index.php?m=home&c=course&a=specialunshare";
    //TODO 视频详情页最新评论
    public static final String COMMENT_LIKE = SERVER + "index.php?m=home&c=course&a=NewUnIike";
    //TODO 直播视频页类型id
    public static final String VIDEO_TYPE = SERVER + "index.php?m=home&c=course&a=form";
    //TODO 直播视频页列表
    public static final String VIDEO_LIST = SERVER + "index.php?m=home&c=course&a=videodisplay";
    //TODO 送礼排行榜             http://doubihai.com/index.php?m=home&c=live&a=rankinglist
    public static final String LIVE_SEND_GIFT_RANKING = SERVER + "index.php?m=home&c=live&a=rankinglist";
    //TODO 直播专辑页列表
    public static final String ALBUM_LIST = SERVER + "index.php?m=home&c=course&a=specialshow";
    //TODO 专辑详情页
    public static final String ALBUM_DETAILS = SERVER + "index.php?m=home&c=course&a=specialdatail";
    //TODO 专辑视频详情页
    public static final String ALBUM_VIDEO_DETAILS = SERVER + "index.php?m=home&c=course&a=specialvideodetails";
    //TODO 上传视频
    public static final String UP_VIDEO = SERVER + "index.php?m=home&c=course&a=video";
    //TODO 搜索视频
    public static final String SEARCH_VIDEO = SERVER + "index.php?m=home&c=call&a=video";
    //TODO 搜索付费课程
    public static final String SEARCH_PAY = SERVER + "index.php?m=home&c=call&a=courseSearch";
    //TODO 搜索动态
    public static final String SEARCH_STATUS = SERVER + "index.php?m=home&c=call&a=state";
    //TODO 搜索用户
    public static final String SEARCH_USER = SERVER + "index.php?m=home&c=call&a=searchUser";
    //TODO  举报接口           http://doubihai.com/index.php?m=home&c=live&a=livetipoff
    public static final String LIVE_REPORT = SERVER + "index.php?m=home&c=live&a=livetipoff";
    //TODO  关注
    public static final String FOLLOW = SERVER + "?m=home&c=sns&a=follow";
    //TODO  取消关注
    public static final String UNFOLLOW = SERVER + "?m=home&c=sns&a=unfollow ";
    //是否已关注当前用户
    public static final String IS_FOLLOW = SERVER + "?m=home&c=sns&a=isfollow";
    //已关注列表
    public static final String FOLLOW_LIST = SERVER + "?m=home&c=sns&a=myfollow";
    //已关注列表
    public static final String FANS_LIST = SERVER + "?m=home&c=sns&a=myfans";
    //TODO   发现_热门
    public static final String DISCOVER_HOT = SERVER + "index.php?m=home&c=Content&a=HotPosts";
    //TODO   发现_最新动态
    public static final String DISCOVER_NEW = SERVER + "index.php?m=home&c=Content&a=latestrelease";
    //TODO   发现_关注
    public static final String DISCOVER_FOLLOW = SERVER + "/index.php?m=home&c=Content&a=followUserWorki";
    //TODO   发现_发布动态
    public static final String DISCOVER_PUBLISH = SERVER + "index.php?m=home&c=content&a=pubcontentInFind";
    //TODO   支付订单             http://doubihai.com/index.php?m=home&c=gift&a=createorder
    public static final String PAY_INDENT = SERVER + "index.php?m=home&c=gift&a=createorder";
    //TODO   支付信息（支付宝、微信）         http://doubihai.com/index.php?m=home&c=gift&a=payinfo
    public static final String PAY_INFO = SERVER + "index.php?m=home&c=gift&a=payinfo";
    //TODO  分享
    public static final String SHARE = SERVER + "index.php?m=home&c=Commodity&a=share";
    //TODO  已付课程             http://doubihai.com/index.php?m=home&c=course&a=specialunpurchase
    public static final String PAY_COURSE = SERVER + "index.php?m=home&c=course&a=specialunpurchase";
    //TODO  机构视频列表
    public static final String ORG_VIDEO = SERVER + "index.php?m=home&c=orgnization&a=Orgnizationvideolist";
    //TODO  机构发布视频
    public static final String ORG_UP_VIDEO = SERVER + "index.php?m=home&c=orgnization&a=Orgnizationvideo";
    //TODO  删除班级管理员
    public static final String DEL_CLASS_ADMIN = SERVER + "?m=home&c=orgnization&a=delgroupadmin";
    //TODO  机构身份下显示班级 【新增】
    public static final String CLASS_ORG = SERVER + "?m=home&c=orgnization&a=Classorg";
    //TODO  身份状态【机构新】               index.php?m=home&c=orgnization&a=orgidentity
    public static final String STATUS = SERVER + "index.php?m=home&c=orgnization&a=orgidentity";
    //TODO 课程表列表【课程表增】                        /index.php?m=home&c=schedule&a=scheduleList
    public static final String SCHEDULE_LIST = SERVER + "/index.php?m=home&c=schedule&a=scheduleList";
    //TODO 请假列表
    public static final String LEAVE_LIST = SERVER + "/index.php?m=home&c=schedule&a=leaveList";
    //TODO 课程表提醒开关设置【课程表增】                       /index.php?m=home&c=schedule&a=scheduleremind
    public static final String SCHEDULE_LIST_SWITCHBTN = SERVER + "/index.php?m=home&c=schedule&a=scheduleremind ";
    //TODO 课程表创建和编辑【课程表增】
    public static final String ADD_SYLLABUS = SERVER + "index.php?m=home&c=schedule&a=scheduleoperate";
    //TODO 删除课表
    public static final String Del_SYLLABUS = SERVER + "index.php?m=home&c=schedule&a=scheduledel";
    //TODO 请假信息（类型和通知人）                       /index.php?m=home&c=schedule&a=leaveinfo
    public static final String LEAVE_TYPE_PEOPLE = SERVER + "/index.php?m=home&c=schedule&a=leaveinfo";
    //TODO 请假提交
    public static final String LEAVE_TYPE_PUT = SERVER + "/index.php?m=home&c=schedule&a=leave";
    //TODO 请假审批【请假增】
    public static final String APPROVAL = SERVER + "index.php?m=home&c=schedule&a=leavecheck";
    //TODO 请假详情【请假增】
    public static final String LEAVE_DETAILS = SERVER + "index.php?m=home&c=schedule&a=leavedetail";
    //TODO 老师心语
    public static final String TEACHER_COMMENT = SERVER + "?m=home&c=content&a=readcomment";
    //TODO 课程表详情【课程表增】
    public static final String DETAIL_SCHEDULE = SERVER + "index.php?m=home&c=schedule&a=scheduledetail";
    //TODO 班级分享
    public static final String GROUP_SHARE = SERVER + "?m=home&c=group&a=share_group";
    //TODO 知识点详情最新最赞
    public static final String VIDEO_SORT = SERVER + "?m=home&c=orgnization&a=videosort";
    //TODO 班级编码查班级
    public static final String GROUP_CODE = SERVER + "?m=home&c=group&a=join_group";
    //大章节添加，编辑，删除
    public static final String ADD_SECTION = SERVER + "index.php?m=home&c=chapter&a=chapteroperate";
    //大章节设置信息【知识点】                           ?m=home&c=chapter&a=chaptersetinfo
    public static final String SETTING_SECTION = SERVER + "?m=home&c=chapter&a=chaptersetinfo";
    //大章节设置【知识点】
    public static final String ALTER = SERVER + "?m=home&c=chapter&a=chapterset";
    //小章节列表 添加小章节【知识点】                 ?m=home&c=chapter&a=ChapterSetsection
    public static final String SECTION_LIST = SERVER + "?m=home&c=chapter&a=ChapterSetsection";
    //大章节列表【知识点】
    public static final String KP_SECTION_LIST = SERVER + "/?m=home&c=chapter&a=chapterlist";
    //榜单添加，删除【榜单】
    public static final String ADD_LIST = SERVER + "?m=home&c=billboard&a=billboardoperate";
    //榜单设置信息内容【榜单】
    public static final String LIST_SETTING = SERVER + "?m=home&c=billboard&a=billboardsetinfo";
    //视频编辑更新【知识点】
    public static final String UPDATE_VIDEO = SERVER + "?m=home&c=orgnization&a=compilevideo";
    //榜单设置【榜单】
    public static final String EDIT_RANK = SERVER + "?m=home&c=billboard&a=billboardset";
    //榜单区块管理【榜单】
    public static final String RANK_BLOCKS = SERVER + "?m=home&c=billboard&a=billboardblock";
    //TODO 视频删除【知识点】                             ?m=home&c=orgnization&a=deletevideo
    public static final String VIDEO_DELETE = SERVER + "?m=home&c=orgnization&a=deletevideo";
    //榜单列表，单个榜单列表【榜单】
    public static final String RANK_LIST = SERVER + "?m=home&c=billboard&a=billboardlist";
    //上榜成员列表【榜单】
    public static final String RANK_Member = SERVER + "?m=home&c=billboard&a=billboardmember";
    //榜单区块，榜单人员排序【榜单】
    public static final String EDIT_RANK_BLOCKS = SERVER + "?m=home&c=billboard&a=billboardsort";
    //机构弹框取消
    public static final String POP_UP = SERVER + "?m=home&c=orgnization&a=pop_up";
    //评论删除【综合】
    public static final String COMMTENTS_DELETE = SERVER + "?m=home&c=course&a=DeleteComment";
    //评论接口【综合】-提交
    public static final String COMMTENTS_COMMIT = SERVER + "?m=home&c=course&a=SynthesizeComment";
    //评论列表【综合】
    public static final String COMMTENTS_GAIN_LIST = SERVER + "?m=home&c=course&a=SynthesizeList";
    //TODO 机构活动列表
    public static final String EXAM_LIST = SERVER + "index.php?m=home&c=course&a=ExaminationQuestions";
    //默认通知人未加、已加列表【请假增】
    public static final String LEAVE_NOTIFIER = SERVER + "index.php?m=home&c=schedule&a=leavedefaultinform";
    //默认通知人设置【请假增】
    public static final String ADD_LEAVE_NOTIFIER = SERVER + "index.php?m=home&c=schedule&a=leaveinformset";
    //删除默认通知人【请假增】
    public static final String DEL_LEAVE_NOTIFIER = SERVER + "index.php?m=home&c=schedule&a=delleaveinform";
    //提醒开关状态【课程表、批阅练习提醒】
    public static final String SWITCH_STATE = SERVER + "index.php?m=home&c=schedule&a=orgremindstatus";
    //提醒开关设置【课程表、批阅练习提醒】
    public static final String EDIT_SWITCH_STATE = SERVER + "index.php?m=home&c=schedule&a=orgremindset";
    //TODO 班级学员【机构管理员可添加】(排序)
    public static final String CLASS_ADD_MEMBER = SERVER + "?m=home&c=orgnization&a=groupmembers";
    //TODO 班级学员搜索【按字母排序】
    public static final String SEARCH_MEMBER = SERVER + "?m=home&c=orgnization&a=searchGroupMember";
    //TODO 批量添加班级学员
    public static final String SAVE_ADD_MEMBER = SERVER + "index.php?m=home&c=group&a=batchjoingroup ";
    //TODO 聊天联系人列表
    public static final String CONTACTS = SERVER + "?m=home&c=orgnization&a=contacts";
    //TODO 机构活动添加
    public static final String EXAM_ADD = SERVER + "index.php?m=home&c=course&a=OperateExamination";
    //TODO 添加频道
    public static final String ADD_CHANNEL = SERVER + "?m=home&c=content&a=AddChannel";
    //TODO 点名-学员列表、插班生信息【点名】
    public static final String CALL_STUDENT_LIST = SERVER + "index.php?m=home&c=CallRoll&a=userslesson";
    //TODO 点名-管理员、插班生列表
    public static final String CALL_MANAGE_STU_LIST = SERVER + "index.php?m=home&c=callRoll&a=userlist ";
    //点名-未点名列表【点名】
    public static final String CALL_LIST = SERVER + "?m=home&c=callRoll&a=callrollschedule";
    //点名日历【点名】
    public static final String CALENDAR = SERVER + "?m=home&c=callRoll&a=callrollcalendar";
    //点名-课卡操作【点名】
    public static final String LESSONOPERATE = SERVER + "?m=home&c=callRoll&a=lessonoperate";
    //点名详情【点名】
    public static final String CALL_ROLL_DETAIL = SERVER + "?m=home&c=callRoll&a=callrolldetail";
    //TODO 点名-提交点交，                             index.php?m=home&c=callRoll&a=callrolloperate
    public static final String CALL_SUBMIT = SERVER + "index.php?m=home&c=callRoll&a=callrolloperate";
    //TODO 历史记录
    public static final String CALL_RECORD = SERVER + "index.php?m=home&c=callRoll&a=callrollhistory ";
    //TODO 课卡管理【课卡】
    public static final String LESSON_MANAGE = SERVER + "index.php?m=home&c=CallRoll&a=lessonmanage";
    //TODO 课时记录（学生端课卡管理）                       index.php?m=home&c=CallRoll&a=lessondetailuser
    public static final String LESSON_RECORD_STU = SERVER + "index.php?m=home&c=CallRoll&a=lessondetailuser";
    //课卡详情管理端【课卡】
    public static final String LESSON_DETAIL_ADMIN = SERVER + "?m=home&c=CallRoll&a=lessondetailadmin";
    //异地登陆重登时更新token
    public static final String UPDATE_TOKEN = SERVER + "?m=home&c=user&a=updatetoken";
    //TODO 布置练习信息列表
    public static final String TASK_MESSAGE_LIST = SERVER + "index.php?m=home&c=content&a=tasklist";
    //TODO 练习搜索时间列表
    public static final String TASK_TIME_LIST = SERVER + "index.php?m=home&c=content&a=serachtime";
    //TODO 班级练习详情
    public static final String CLASS_TASK_LIST = SERVER + "index.php?m=home&c=content&a=grouptaskdetail";
    //TODO 一键提醒
    public static final String CLASS_TASK_REMIND = SERVER + "index.php?m=home&c=content&a=oneclickremind ";
    //TODO 班级成员列表【添加会员】
    public static final String CLASS_LIST = SERVER + "?m=home&c=Member&a=ClassList";
    //添加删除分类
    public static final String ADD_PARSE = SERVER + "?m=home&c=orgnization&a=AddParse";
    //分类列表 分类班级
    public static final String PARSE_LIST = SERVER + "?m=home&c=orgnization&a=ParseList";
    //班级加入（删除）分类
    public static final String ADD_GROUP_PARSE = SERVER + "?m=home&c=orgnization&a=AddGroupParse";
    //机构下班级【分类】
    public static final String ORG_CLASSORG = SERVER + "?m=home&c=orgnization&a=OrgClassorg";
    //TODO 会员福利列表
    public static final String WEAL_MMEMBER = SERVER + "?m=home&c=orgnization&a=MemberBenefits";
    //TODO 动态支持【机构活动】
    public static final String SUPPORT_STATUS = SERVER + "?m=home&c=content&a=support";
    //TODO 动态支持列表【机构活动】
    public static final String SUPPORT_STATUS_LIST = SERVER + "?m=home&c=content&a=SupportList";
    //TODO 会员背景色【待定】
    public static final String BACKGROUND_COLOUR = SERVER + "?m=home&c=Member&a=BackgroundColour";
    //会员， 积分列表【待定】
    public static final String MEMBER_LIST = SERVER + "?m=home&c=Member&a=MemberList";
    //积分设置【待定】
    public static final String MEMBER_SET = SERVER + "?m=home&c=Member&a=MemberSet";
    //会员详情页【待定】
    public static final String MEMBER_DETAIL = SERVER + "?m=home&c=Member&a=MemberDetail";
    //会员等级成员列表【待定】
    public static final String MEMBER_GRADE = SERVER + "?m=home&c=Member&a=MemberGrade";
    //会员创建
    public static final String MEMBER_ESTABLISH = SERVER + "?m=home&c=Member&a=MemberEstablish";
    //加入会员 删除会员
    public static final String ADD_MEMBER = SERVER + "?m=home&c=Member&a=AddMember";
    //加入会员 删除会员
    public static final String ORG_CLASS_SEEK = SERVER + "/?m=home&c=orgnization&a=OrgClassSeek";
    //频道公有
    public static final String EDIT_COLUMN = SERVER + "?m=home&c=content&a=EditColumn";
    //频道排序
    public static final String SORT_COLUMN = SERVER + "?m=home&c=content&a=SortColumn";
    //添加榜单成员列表
    public static final String LIST_MEMBER_LIST = SERVER + "?m=home&c=billboard&a=ListMemberList";
    //榜单搜索列表
    public static final String ORG_LIST_SEEK = SERVER + "?m=home&c=orgnization&a=OrgListSeek";
    //同步栏目（加权限）
    public static final String SYNCHRONIZATION = SERVER + "?m=home&c=content&a=Synchronization";
    //动态移动
    public static final String MOVE_STATUS = SERVER + "?m=home&c=content&a=Move";
    //TODO 课卡搜索
    public static final String LESSON_SEEK = SERVER + "?m=home&c=CallRoll&a=LessonSeek";
    //TODO 文件夹添加、重命名、删除
    public static final String FILE_ADD_DELETE = SERVER + "index.php?m=home&c=file&a=fileoperate";
    //TODO 机构文件夹上传图片，视频
    public static final String FILE_ADD_VIDEO_PIC = SERVER + "index.php?m=home&c=file&a=DocumentUpload";
    //TODO 文件目录列表
    public static final String ALL_FILE_LIST = SERVER + "index.php?m=home&c=file&a=DirectoryList";
    //TODO 文件夹列表
    public static final String FILE_LIST = SERVER + "/index.php?m=home&c=file&a=FileList";
    //TODO 文件移动、删除
    public static final String FILE_MOVE_DELETE = SERVER + "index.php?m=home&c=file&a=DocumentOperate ";
    //TODO 文件搜索
    public static final String FILE_SEARCH = SERVER + "index.php?m=home&c=file&a=DocumentSearch";
    //TODO 进行中的活动编辑
    public static final String EXAM_EDIT = SERVER + "index.php?m=home&c=course&a=EditExamination";
    //音乐列表，搜索列表【音乐】
    public static final String ORG_MUSIC = SERVER + "?m=home&c=orgnization&a=orgmusic";
    //音乐标签列表【音乐】
    public static final String ORG_TAGS = SERVER + "?m=home&c=Orgnization&a=orgtags";
    //删除视频
    public static final String DEL_VIDEO = SERVER + "?m=home&c=content&a=deletelive";
    //扫码福利兑换【2.0.9版】
    public static final String ORG_EXCHANGE = SERVER + "?m=home&c=Orgnization&a=orgexchange";
    //机构工具栏
    public static final String ORG_FUNCTIONS = SERVER + "?m=home&c=orgnization&a=OrgFunctions";
    //是否启用、位置、介绍、电话、机构成果信息【机构主页】
    public static final String ORG_SHOW = SERVER + "?m=home&c=Orgnization&a=orgshow";
    //TODO 是否启用、位置、介绍、电话、机构成果、机构活动设置
    public static final String ORG_SET = SERVER + "?m=home&c=Orgnization&a=orgset";
    //TODO 机构活动 机构主页
    public static final String EXAM_ORG_LIST = SERVER + "index.php?m=home&c=Orgnization&a=ExaminationList";
    //TODO 预约报名 机构主页
    public static final String ORDER_EXPERIENCE = SERVER + "index.php?m=home&c=User&a=order_experience";
    //TODO 机构主页信息
    public static final String ORG_MAIN_INFO = SERVER + "?m=home&c=orgnization&a=OrgHomePage2 ";
    //TODO 机构主页列表
    public static final String ORG_MAIN_STATUS = SERVER + "?m=home&c=orgnization&a=OrgHomePage";
    //轮播图添加图片【机构主页】
    public static final String ORG_TOPPICADD = SERVER + "?m=home&c=orgnization&a=orgtoppicadd";
    //轮播图列表【机构主页】
    public static final String ORG_TOPPICLIST = SERVER + "?m=home&c=orgnization&a=OrgTopPicList";
    //轮播图删除【机构主页】
    public static final String ORG_TOPPICDEL = SERVER + "?m=home&c=orgnization&a=OrgTopPicDel";
    //添加明星教师、学生列表
    public static final String MEMBER_STAR = SERVER + "?m=home&c=Member&a=star";
    //预约通知列表
    public static final String SYSTEM_MESSAGE = SERVER + "?m=home&c=User&a=system_message";
    //预约通知详情
    public static final String MESSAGE_DETAIL = SERVER + "?m=home&c=User&a=message_detail";
    //TODO 机构主页 活动全部
    public static final String ORG_MAIN_EXAM_ALL = SERVER + "?m=home&c=content&a=OrgExamination";
    //TODO 直播付费配置【直播付费】
    public static final String LIVE_PAY_CONFIG = SERVER + "index.php?m=home&c=live&a=liveFeeConfig ";
    //TODO 直播观看时间设置
    public static final String LIVE_TIME_SET = SERVER + "index.php?m=home&c=live&a=FreeTimeSet";
    //TODO 机构首页榜单和活动预告
    public static final String ORG_RANK_EXAM_LIST = SERVER + "/index.php?m=home&c=Orgnization&a=OrgBillboardExamination";
    //机构老师列表
    public static final String TEACHER_LIST = SERVER + "?m=home&c=orgnization&a=teacher_list";
    //设置机构老师
    public static final String SET_TEACHER = SERVER + "?m=home&c=orgnization&a=set_teacher";
    //删除机构老师
    public static final String DEL_TEACHER = SERVER + "?m=home&c=orgnization&a=del_teacher";
    //班级学员，机构成员列表，机构管理员添加列表【机构管理员可添加】
    public static final String ORG_GROUP_MEMBERS = SERVER + "?m=home&c=orgnization&a=groupmembers";
    //点赞列表【2.1.0优化项】
    public static final String LIKE_LIST = SERVER + "index.php?m=home&c=content&a=likelist ";
    //TODO 首页类型                                       /index.php?m=home&c=call&a=PageType
    public static final String HOME_TAB_TYPE = SERVER + "/index.php?m=home&c=call&a=PageType";
    //TODO 获取课程模式
    public static final String RECOMMEND_CATEGORY_LIST = SERVER + "?m=home&c=course&a=topCourseCategoryList_new";
    //TODO 淘童首页
    public static final String HOME_PAGE = SERVER + "/index.php?m=home&c=content&a=HomePage";
    //TODO 直播预告
    public static final String LIVE_LIST_INFO2 = SERVER + "index.php?m=home&c=live&a=livelistinfo2";
    //TODO 直播预约
    public static final String LIVE_SUBSCRIBE = SERVER + "index.php?m=home&c=live&a=subscribe";
    //TODO 直播取消预约
    public static final String LIVE_CANCLE_SUBSCRIBE = SERVER + "index.php?m=home&c=live&a=CancelSubscribe";
    //TODO 直播列表
    public static final String LIVE_LIST_INFO3 = SERVER + "/index.php?m=home&c=live&a=livelistinfo3";
    //TODO 直播标签列表
    public static final String LIVE_TAG_LIST = SERVER + "index.php?m=home&c=live&a=LiveLabel";
    //我发布的，报名的活动筛选列表【2.2.1】
    public static final String SEARCH_CONDITION = SERVER + "?m=home&c=course&a=SearchCondition";
    //我发布的，报名的活动
    public static final String MY_ACTIVITIES = SERVER + "?m=home&c=course&a=MyActivities";
    //我发布的，报名的活动搜索【2.2.1】
    public static final String SEARCH_ACTIVITIES = SERVER + "?m=home&c=course&a=SearchActivities";
    //TODO 活动收费项列表
    public static final String ACT_FEES = SERVER + "index.php?m=home&c=course&a=PackageFee";
    //关闭，开启报名
    public static final String IS_CLOSE_ACTIVITY = SERVER + "?m=home&c=course&a=IsCloseActivity";
    //活动管理
    public static final String ACTIVITY_MANAGE = SERVER + "?m=home&c=course&a=ActivityManage";
    //报名管理
    public static final String REGISTE_MANAGE = SERVER + "?m=home&c=course&a=RegisteManage";
    //报名信息填写
    public static final String APPLY_FILLOUT = SERVER + "index.php?m=home&c=course&a=Apply_Fillout";
    //查看电子票
    public static final String LOOK_UP_TICKET = SERVER + "?m=home&c=course&a=LookUpTicket";
    //报名凭证二维码检验
    public static final String SCAN_TICKET = SERVER + "?m=home&c=course&a=ScanTicket";
    //导出名单报名列表
    public static final String OUTPUT_REGISTE = SERVER + "?m=home&c=course&a=OutputRegiste";
    //发送邮件
    public static final String POST_REGISTE = SERVER + "?m=home&c=course&a=PostRegiste";
    //支付宝绑定、解绑、验证
    public static final String ZFB_BIND = SERVER + "/index.php?m=home&c=live&a=AccountOperate";
    //收益明细
    public static final String INCOME_DETAIL = SERVER + "?m=home&c=live&a=IncomeDetail";
    //TODO 余额提现到支付宝
    public static final String BALANCE_OPERATE = SERVER + "index.php?m=home&c=live&a=BalanceOperate";
    //支付宝授权
    public static final String ZFB_AUTH = SERVER + "/index.php?m=home&c=gift&a=ZhifubaoAuth";
    //支付宝授权2
    public static final String ZFB_AUTH2 = SERVER + "/index.php?m=home&c=gift&a=ZhifubaoAuth2";
    //可用的班级列表（加权限）
    public static final String MY_CLASS_LIST = SERVER + "?m=home&c=content&a=VisibleClass";
    //TODO 会员缴费编辑
    public static final String WEAL_PAY_CREATE = SERVER + "?m=home&c=content&a=MembershipFee";
    //课程表创建和编辑
    public static final String SCHEDULE_OPERATE = SERVER + "?m=home&c=schedule&a=scheduleoperate";
    //上一次的排课记录
    public static final String LAST_SCHEDULE_INFO = SERVER + "?m=home&c=schedule&a=LastScheduleInfo";
    // 附近的机构列表
    public static final String NEAR_ORG = SERVER + "?m=home&c=orgnization&a=nearby_org";
    // 上课教室和上课老师的历史记录【2.2.2版】
    public static final String SCHEDULE_HISTORY = SERVER + "/index.php?m=home&c=schedule&a=ScheduleHistory ";
    //TODO 课卡预警
    public static final String LESSON_WARNING = SERVER + "/index.php?m=home&c=CallRoll&a=LessonWarning";
    //TODO 课卡预警详情
    public static final String LESSON_WARNING_DETAIL = SERVER + "/index.php?m=home&c=CallRoll&a=LessonWarningDetail";
    //解散亲友团
    public static final String DELETE_RELATIVES = SERVER + "?m=home&c=OrgRelatives&a=DeleteRelatives";
    //创建亲友团
    public static final String CREATE_RELATIVES = SERVER + "?m=home&c=OrgRelatives&a=CreateRelatives";
    //亲友团列表 成员列表
    public static final String ORG_RELATIVES_LIST = SERVER + "?m=home&c=OrgRelatives&a=OrgRelativesList";
    //亲友团转让
    public static final String TURN_RELATIVES = SERVER + "?m=home&c=OrgRelatives&a=TurnRelatives";
    //亲友团踢出
    public static final String REMOVE_RELATIVES = SERVER + "?m=home&c=OrgRelatives&a=RemoveRelatives";
    //TODO 常用语列表
    public static final String USEFUL_STATEMENT = SERVER + "?m=home&c=Course&a=UsefulExpressions";
    //TODO 常用语添加,编辑
    public static final String ADD_EDIT_USEFUL = SERVER + "?m=home&c=course&a=AddUsefulExpressions";
    //TODO 常用语删除
    public static final String DELETE_USEFUL = SERVER + "?m=home&c=course&a=DeleteUsefulExpressions";
    //清空教师和教室的历史记录
    public static final String CLEAR_SCHEDULE_HISTROY = SERVER + "?m=home&c=schedule&a=clearScheduleHistroy";
    //班级默认课时设置【2.2.2】
    public static final String SET_DEFAULT_LESSONS = SERVER + "?m=home&c=group&a=setDefaultLessons";
    //TODO 新版登录
    public static final String LOGIN_NEW = SERVER + "/index.php?m=home&c=user&a=RegisterApp";
    //TODO 新版登录选择身份
    public static final String IDENTITY = SERVER + "/index.php?m=home&c=user&a=IdentityBinding";
    //赞，评论，分享消息列表【2.2.2】
    public static final String MESSAGE_SHARE = SERVER + "?m=home&c=message&a=messageshare";
    //消息数量【2.2.2】
    public static final String MESSAGE_NUM = SERVER + "?m=home&c=message&a=messageNum";
    //TODO 机构消息列表   2.2.2
    public static final String ORG_MESSAGE = SERVER + "?m=home&c=message&a=messageOrg";
    //获取孩子的信息
    public static final String CHILD_INFO = SERVER + "index.php?m=home&c=user&a=Child";
    //分享接口
    public static final String APP_SHARE = SERVER + "?m=home&c=share&a=share";
    //分享结果上报
    public static final String APP_SHARE_RESULT = SERVER + "?m=home&c=share&a=add_message";
    //清除消息
    public static final String CLEAR_MESSAGE = SERVER + "?m=home&c=message&a=messagereset";
    //TODO 参与话题列表   2.2.3
    public static final String JOIN_TOPIC_LIST = SERVER + "?m=home&c=content&a=TopicList";
    //根据聊天室id获取班级id
    public static final String CHAT_TO_GROUP = SERVER + "index.php?m=home&c=group&a=chattogroup";
    //TODO 首页搜索提示
    public static final String HOME_PAGE_HINT = SERVER + "index.php?m=home&c=call&a=SeekHint";
    //扫码登录（抽奖）
    public static final String LOTTO_LOGIN = SERVER + "?m=home&c=Lotto&a=login";
    //活动状态
    public static final String ACTIVITY_STATE = SERVER + "?m=home&c=course&a=ActivityState";


}
