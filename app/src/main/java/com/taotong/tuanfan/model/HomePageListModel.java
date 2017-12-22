package com.taotong.tuanfan.model;

import java.util.List;

/**
 * Created by liu on 2017/10/11.
 */

public class HomePageListModel {

    /**
     * status : 1
     * info : 列表
     * data : [{"id":"1","uid":"71","content_id":"1789","org_id":"29","title":"✨ 墨云书画馆 ✨ 带你领略书画魅力！","url":"","cover_url":"","is_type":"1","is_like":0,"likes":0,"comments":"2412","org_name":"意飞艺术"},{"id":"2","uid":"2146","content_id":"177","org_id":"0","title":"直播开始了","url":"rtmp://pili-publish.bipai.tv/bipai-streams/bipai2146_1507691689?e=1507691750&token=BX3FxNDH3aFGwGSb8Yue745EgiumlqGpqthQ8x1u:CuKs4sYSh_68S8wJu4wHhY1HLe4=","cover_url":"http://vod.doushow.com/FtzCZL53q3iIpKB4eX8n2gpbpjal","is_type":"2","is_like":0,"likes":0,"comments":"2412","org_name":""}]
     */

    private int status;
    private String info;
    private List<DataBean> data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 1
         * uid : 71
         * content_id : 1789
         * org_id : 29
         * title : ✨ 墨云书画馆 ✨ 带你领略书画魅力！
         * url :
         * cover_url :
         * is_type : 1
         * is_like : 0
         * likes : 0
         * comments : 2412
         * org_name : 意飞艺术
         */
        private String video_url;//淘学视频分享url
        private String form_id;//标签
        private String id;
        private String uid;
        private String content_id;
        private String org_id;
        private String title;
        private String url; //播放地址，视频，直播推流，录播
        private String cover_url;
        private String is_type; //和机构首页动态列表的类型一样；is_type大于等于17的是模块展示：17秀场、18直播、19活动、20附近 21秀场、23精品推荐
        private int is_like; //是否点赞 0未点赞 1已点赞
        private int likes;
        private String comments; //评论数
        private String org_name;
        private String nickname; //用户昵称、模块名称
        private String avatar;
        private String tag;
        private int is_module;//是否是模块展示0不是 1是
        private int is_org;//是否为机构
        private String group_id; //聊天室id
        private String template_id; //模板id  is_type 为11时
        private String is_admin; //模板id  is_type 为11时
        private String chapter_id ; //知识点大章节id

        private String browse;
        private String apply_sum;
        private String is_crosss_creen ;// 横屏字段1横屏0竖屏
        private String topic_id ;// 话题id
        private String description; //描述
        private String topic_name; //描述

        public String getTopic_name() {
            return topic_name;
        }

        public void setTopic_name(String topic_name) {
            this.topic_name = topic_name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getTopic_id() {
            return topic_id;
        }

        public void setTopic_id(String topic_id) {
            this.topic_id = topic_id;
        }

        public String getIs_crosss_creen() {
            return is_crosss_creen;
        }

        public void setIs_crosss_creen(String is_crosss_creen) {
            this.is_crosss_creen = is_crosss_creen;
        }

        public String getVideo_url() {
            return video_url;
        }

        public void setVideo_url(String video_url) {
            this.video_url = video_url;
        }

        public String getForm_id() {
            return form_id;
        }

        public void setForm_id(String form_id) {
            this.form_id = form_id;
        }


        public String getChapter_id() {
            return chapter_id;
        }

        public void setChapter_id(String chapter_id) {
            this.chapter_id = chapter_id;
        }

        public String getGroup_id() {
            return group_id;
        }

        public void setGroup_id(String group_id) {
            this.group_id = group_id;
        }

        public String getTemplate_id() {
            return template_id;
        }

        public void setTemplate_id(String template_id) {
            this.template_id = template_id;
        }

        public String getIs_admin() {
            return is_admin;
        }

        public void setIs_admin(String is_admin) {
            this.is_admin = is_admin;
        }

        public int getIs_org() {
            return is_org;
        }

        public void setIs_org(int is_org) {
            this.is_org = is_org;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getContent_id() {
            return content_id;
        }

        public void setContent_id(String content_id) {
            this.content_id = content_id;
        }

        public String getOrg_id() {
            return org_id;
        }

        public void setOrg_id(String org_id) {
            this.org_id = org_id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getCover_url() {
            return cover_url;
        }

        public void setCover_url(String cover_url) {
            this.cover_url = cover_url;
        }

        public String getIs_type() {
            return is_type;
        }

        public void setIs_type(String is_type) {
            this.is_type = is_type;
        }

        public int getIs_like() {
            return is_like;
        }

        public void setIs_like(int is_like) {
            this.is_like = is_like;
        }

        public int getLikes() {
            return likes;
        }

        public void setLikes(int likes) {
            this.likes = likes;
        }

        public String getComments() {
            return comments;
        }

        public void setComments(String comments) {
            this.comments = comments;
        }

        public String getOrg_name() {
            return org_name;
        }

        public void setOrg_name(String org_name) {
            this.org_name = org_name;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public int getIs_module() {
            return is_module;
        }

        public void setIs_module(int is_module) {
            this.is_module = is_module;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public String getBrowse() {
            return browse;
        }

        public void setBrowse(String browse) {
            this.browse = browse;
        }

        public String getApply_sum() {
            return apply_sum;
        }

        public void setApply_sum(String apply_sum) {
            this.apply_sum = apply_sum;
        }
    }
}
