package com.taotong.tuanfan.model;

import android.content.Context;

import com.ro.xdroid.mvp.XApp;
import com.ro.xdroid.net.ok.taotong.TTCommonParams;
import com.taotong.tuanfan.TTApp;
import com.taotong.tuanfan.Util.SPConstant;
import com.taotong.tuanfan.Util.SPUtils;

import java.io.Serializable;

/**
 * Created by android2 on 2017/1/10.
 */

public class MySelfInfo implements Serializable {
    /**
     * 获取用户信息
     * uid : 1690
     * nickname : 用户_1690
     * avatar : http://vod.doushow.com/dbh_avatar_default.png
     * signature : 这家伙很懒，什么签名都没写~
     * sex : 0
     * birthday :
     * status : 1                   会员状态
     * works : 0                    作品数
     * likes : 0
     * follows : 0                  关注数
     * fans : 0                     粉丝数
     * anchor_status : 1            0不是主播1为主播4为禁播
     * total_coins : 0              淘金币数
     * <p>
     * <p>
     * home 家乡
     */
    private String uid;
    private String nickname;    // 呢称
    private String avatar;      // 头像
    private String signature;      // 签名
    private String CosSig;
    private int anchor_status = 0;   //0不是主播1为主播4为禁播
    private int sex;
    private String birthday;
    private String status;
    private String works;
    private int likes;
    private int follows;
    private int fans;
    private String home;
    //    private String last_login_time;
    private String total_coins;
    private static boolean isCreateRoom = false;

    /**
     * 用户登录返回信息
     * uid : 471
     * nickname : 用户_2333344
     * avatar : http://vod.doushow.com/Fsoo1xQPqOVOLoAc3P4hncAAq2VP
     * total_coins : 0
     * anchor_status : 0
     * works : 0
     * likes : 3
     * follows : 0
     * signature : 这家伙很懒，什么签名都没写?
     * last_login_time : 1492512602
     * role : USER
     * session_id : 23t857rceohnma81emov41atj3
     * create_time : 1492512743
     * sig : "eJxlzl9PgzAUBfB3PgXpq8a0pZWyxAdi2DRhU6Zk*kRgLXhH*JNSEWb87k5cIon39XdOzv20bNtGz*HTVbrfN**1SczYKmQvbITR5R*2LcgkNYmj5T9UQwtaJWlulJ6QcM4pxvMMSFUbyOGcYC6ZYSfLZFr4bbNTlVJB6TwCxYTrIL69vyvEx1Yvs4dwrLTYHOIyKvqtGzZvPOJL-tjp3otzWZmj70PgY5WVq1fWePw62tCjAH7YvcRt19dkGAd2sRJrJlieeUF3M5s0UKnzQx7lhLrMmWmvdAdNPQUoJid38M8h68v6BsPDW7k_"
     * "token_entrypt": "881c8e5aa1301d684943dd08f9a5055a"
     */
    private String role;
    private String session_id;
    private int create_time;
    private String sig;
    private String token_entrypt;

    /**
     * 搜索用户
     * uid : 1243
     * nickname : 测试
     * avatar : http://vod.doushow.com/1492511972176/ios_a/180X181.jpg
     * signature : 1234567890098
     * is_like : 0
     */
    private int is_like;//有没有关注 -1没有登录 0 没有关注 1已经关注
    private int is_attem;//有没有关注 -1没有登录 0 没有关注 1已经关注
    private int is_register;//身份：0：无身份，1：机构，0：老师，0：家长，0：学员
    private int is_binding ;//是否绑定 0未 1绑定

    /**********************************************************************/
    private boolean bLiveAnimator;  // 渐隐动画

    private int id_status;

    private boolean roomIsCreated = false;

    private String myRoomNum = "";

    public int getAnchor_status() {
        return anchor_status;
    }

    public void setAnchor_status(int anchor_status) {
        this.anchor_status = anchor_status;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getWorks() {
        return works;
    }

    public void setWorks(String works) {
        this.works = works;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getFollows() {
        return follows;
    }

    public void setFollows(int follows) {
        this.follows = follows;
    }

    public int getFans() {
        return fans;
    }

    public void setFans(int fans) {
        this.fans = fans;
    }

    public String getTotal_coins() {
        return total_coins;
    }

    public void setTotal_coins(String total_coins) {
        this.total_coins = total_coins;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getHome() {
        return home;
    }

    public void setHome(String home) {
        this.home = home;
    }

    public String getMyRoomNum() {
        return myRoomNum;
    }

    public void setMyRoomNum(String myRoomNum) {
        this.myRoomNum = myRoomNum;
    }

    public String getCosSig() {
        return CosSig;
    }

    public void setCosSig(String cosSig) {
        CosSig = cosSig;
    }

    public int getIs_register() {
        return is_register;
    }

    public void setIs_register(int is_register) {
        this.is_register = is_register;
    }

    public int getIs_binding() {
        return is_binding;
    }

    public void setIs_binding(int is_binding) {
        this.is_binding = is_binding;
    }

    public void writeToCache(Context context) {
        if (uid != null && !uid.equals("")) {
            SPUtils.putString(context, SPConstant.USER_ID, uid);
        }
        if (sig != null && !sig.equals("")) {
            SPUtils.putString(context, SPConstant.USER_SIG, sig);
        }
        if (nickname != null && !nickname.equals("")) {
            SPUtils.putString(context, SPConstant.USER_NICK, nickname);
        }
        if (avatar != null && !avatar.equals("")) {
            SPUtils.putString(context, SPConstant.USER_AVATAR, avatar);
        }
        if (signature != null && !signature.equals("")) {
            SPUtils.putString(context, SPConstant.USER_SIGN, signature);
        }
        if (myRoomNum != null && !myRoomNum.equals("")) {
            SPUtils.putString(context, SPConstant.USER_ROOM_NUM, myRoomNum);
        }
        if (anchor_status != -1) SPUtils.putInt(context, SPConstant.ANCHOR_STATUS, anchor_status);
        if (total_coins != null) SPUtils.putString(context, SPConstant.TOTAL_COINS, total_coins);
        if (token_entrypt != null && !token_entrypt.equals("")) {
            SPUtils.putString(context, SPConstant.REQ_TOKEN, token_entrypt);
        }
        SPUtils.putInt(context, SPConstant.USER_IDENTITY, is_register);
        TTCommonParams.autoUpdateCommonParams();
//        FriendshipManagerPresenter.setMyNick(nickname);
//        FriendshipManagerPresenter.setMyAvatar(avatar);
//        FriendshipManagerPresenter.setMySign(signature);
    }

    public static void clearCache(Context context) {
        SPUtils.remove(context, SPConstant.USER_ID);
        SPUtils.remove(context, SPConstant.USER_IDENTITY);
        SPUtils.remove(context, SPConstant.USER_SIG);
        SPUtils.remove(context, SPConstant.USER_NICK);
//        SPUtils.remove(context, SPConstant.USER_AVATAR);
        SPUtils.remove(context, SPConstant.USER_SIGN);
        SPUtils.remove(context, SPConstant.USER_ROOM_NUM);
        SPUtils.remove(context, SPConstant.ANCHOR_STATUS);
        SPUtils.remove(context, SPConstant.LIVE_ANIMATOR);
        SPUtils.remove(context, SPConstant.TOTAL_COINS);
        SPUtils.remove(context, SPConstant.REQ_TOKEN);
        TTCommonParams.autoUpdateCommonParams();
    }

    public MySelfInfo getCache(Context context) {
        uid = SPUtils.getString(context, SPConstant.USER_ID, null);
        sig = SPUtils.getString(context, SPConstant.USER_SIG, null);
        myRoomNum = SPUtils.getString(context, SPConstant.USER_ROOM_NUM, null);
        nickname = SPUtils.getString(context, SPConstant.USER_NICK, "");
        avatar = SPUtils.getString(context, SPConstant.USER_AVATAR, null);
        signature = SPUtils.getString(context, SPConstant.USER_SIGN, "");
        anchor_status = SPUtils.getInt(context, SPConstant.ANCHOR_STATUS, 0);
        bLiveAnimator = SPUtils.getBoolean(context, SPConstant.LIVE_ANIMATOR, false);
        total_coins = SPUtils.getString(context, SPConstant.TOTAL_COINS, "0");
        token_entrypt = SPUtils.getString(context, SPConstant.REQ_TOKEN, null);
        is_register = SPUtils.getInt(context, SPConstant.USER_IDENTITY, 0);
        return this;
    }

    public static int getIdStatus() {
        return SPUtils.getInt(TTApp.getInstance().getApplicationContext(), SPConstant.MEMBER_TYPE, 0);
    }

    public static void setIdStatus(int id_status) {
        SPUtils.putInt(TTApp.getInstance().getApplicationContext(), SPConstant.MEMBER_TYPE, id_status);
    }

    public boolean isCreateRoom() {
        return isCreateRoom;
    }

    public void setJoinRoomWay(boolean isCreateRoom) {
        this.isCreateRoom = isCreateRoom;
    }

    //判断主播，聊天室是否创建成功或者在不在聊天室内
    public boolean getRoomIsCreated() {
        return roomIsCreated;
    }

    public void setRoomIsCreated(boolean roomIsCreated) {
        this.roomIsCreated = roomIsCreated;
    }


    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getSession_id() {
        return session_id;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }

    public int getCreate_time() {
        return create_time;
    }

    public void setCreate_time(int create_time) {
        this.create_time = create_time;
    }

    public String getSig() {
        return sig;
    }

    public void setSig(String sig) {
        this.sig = sig;
    }

    public String getToken_entrypt() {
        return token_entrypt;
    }

    public void setToken_entrypt(String token_entrypt) {
        this.token_entrypt = token_entrypt;
    }

    public int getIs_like() {
        return is_like;
    }

    public void setIs_like(int is_like) {
        this.is_like = is_like;
    }

    public int getIs_attem() {
        return is_attem;
    }

    public void setIs_attem(int is_attem) {
        this.is_attem = is_attem;
    }


    private static MySelfInfo instance;

    public static MySelfInfo getInstance() {
        if (instance == null) {
            instance = new MySelfInfo().getCache(XApp.getContext());

        }
        return instance;
    }

    public boolean isbLiveAnimator() {
        return bLiveAnimator;
    }

    public void setbLiveAnimator(boolean bLiveAnimator) {
        this.bLiveAnimator = bLiveAnimator;
    }

}
