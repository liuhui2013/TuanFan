package com.taotong.tuanfan.Util;

/**
 * Created by roffee on 2017/6/19 15:02.
 * Contact with 460545614@qq.com
 */
public class SpanRefreshHandler {
    public static final int UPTEDATE_NONE = 1;//什么都不做
    public static final int KNOWLEDGEPONIT_ADD = 1 << 1;//添加知识点
    public static final int KNOWLEDGEPONIT_DELETE = 1 << 2;//删除知识点
    public static final int KNOWLEDGEPONIT_MODIFY = 1 << 3;//修改知识点

    public int updateFlag;
    public Object updateProtocol;

    public SpanRefreshHandler(){}
    /**
     *checkFlag: 检测目标标志位是否存在
     * @param targetFlag: 目标flag
     */
    public boolean checkFlag(int targetFlag) {
        return (updateFlag & targetFlag) != 0;
    }
    /**
     * checkFlag: 检测目标标志位是否存在
     * @param sourceFlag: 原目标flag
     * @param targetFlag: 目标flag
     */
    public boolean checkFlag(int sourceFlag, int targetFlag) {
        return (sourceFlag & targetFlag) != 0;
    }
    /**
     * addFlag: 添加某标志
     * @param targetFlag: 目标flag
     */
    public void addFlag(int targetFlag) {
        updateFlag |= targetFlag;
    }
    /**
     * delFlag: 清除某标志
     * @param targetFlag: 目标flag
     */
    public void delFlag(int targetFlag) {
        updateFlag &= ~targetFlag;
    }
    /**
     * resetFlag: 重置
     */
    public void resetFlag() {
        updateFlag = UPTEDATE_NONE;
    }
}
