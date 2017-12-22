package com.ro.xdroid.kit;

/**
 * Created by roffee on 2017/9/14 18:28.
 * Contact with 460545614@qq.com
 */
public class FlagBitKit {
    public static final int FlagNone = 1;
    //step1:net common params
    public static final int FlagUpdateUserId = 1 << 1;
    public static final int FlagUpdateAccessToken = 1 << 2;

    public static final int FlagAll = 100 << 3;

    public static int localFlag;

    public static boolean isFlagBit(int flagBit){ return (localFlag & flagBit) != 0;}
    public static void addFlagBit(int flagBit){ localFlag |= flagBit;}
    public static void delFlagBit(int flagBit){ localFlag &= ~flagBit;}
    public static void resetFlag(){ localFlag = FlagNone; }

    public static boolean isFlagBit(int flag, int flagBit){ return (flag & flagBit) != 0;}
    public static void addFlagBit(int flag, int flagBit){ flag |= flagBit;}
    public static void delFlagBit(int flag, int flagBit){ flag &= ~flagBit;}
    public static void resetFlag(int flag ){ flag = FlagNone; }
}
