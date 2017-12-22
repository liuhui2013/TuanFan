package com.taotong.tuanfan.Util;

import com.ro.xdroid.kit.ToolsKit;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class StringUtils {
//    private static final char[] BR_TAG = "<BR>".toCharArray();
//    private static final char SINGLE_QUOTE_TAG = '\'';
//    private static final char DOUBLE_QUOTE_TAG = '\"';

//    private static final char[] HexChars = {'0', '1', '2', '3', '4', '5', '6',
//            '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
//    private static final char[] IncertitudeChars = {'*', '\\', '/', '\r',
//            '\n', '|', '$', '&', '@', '(', ')', '&', '#', ' '};

    /**
     * @param time 计算时间
     * @return
     */
    public static String generateTime(long time) {
        int totalSeconds = (int) (time / 1);
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        return hours > 0 ? String.format("%02d:%02d:%02d", hours, minutes, seconds) : String.format("%02d:%02d", minutes, seconds);
    }


    public static String substringAfter(String str, String separator) {
        if (isNULL(str)) {
            return str;
        }
        if (separator == null) {
            return "";
        }
        int pos = str.indexOf(separator);
        if (pos == -1) {
            return "";
        }
        return str.substring(pos + separator.length());
    }

    /**
     * <p>
     * Replaces all occurrences of a String within another String.
     * </p>
     * <p/>
     * <p>
     * DetailPageModel <code>null</code> reference passed to this method is a no-op.
     * </p>
     * <p/>
     * <p/>
     * <pre>
     * StringUtils.replace(null, *, *)        = null
     * StringUtils.replace("", *, *)          = ""
     * StringUtils.replace("any", null, *)    = "any"
     * StringUtils.replace("any", *, null)    = "any"
     * StringUtils.replace("any", "", *)      = "any"
     * StringUtils.replace("aba", "a", null)  = "aba"
     * StringUtils.replace("aba", "a", "")    = "b"
     * StringUtils.replace("aba", "a", "z")   = "zbz"
     * </pre>
     *
     * @param text text to search and replace in, may be null
     * @param repl the String to search for, may be null
     * @param with the String to replace with, may be null
     * @return the text with any replacements processed, <code>null</code> if
     * null String input
     */
    public static String replace(String text, String repl, String with) {
        return replace(text, repl, with, -1);
    }

    /**
     * <p>
     * Replaces a String with another String inside a larger String, for the
     * first <code>max</code> values of the search String.
     * </p>
     * <p/>
     * <p>
     * DetailPageModel <code>null</code> reference passed to this method is a no-op.
     * </p>
     * <p/>
     * <p/>
     * <pre>
     * StringUtils.replace(null, *, *, *)         = null
     * StringUtils.replace("", *, *, *)           = ""
     * StringUtils.replace("any", null, *, *)     = "any"
     * StringUtils.replace("any", *, null, *)     = "any"
     * StringUtils.replace("any", "", *, *)       = "any"
     * StringUtils.replace("any", *, *, 0)        = "any"
     * StringUtils.replace("abaa", "a", null, -1) = "abaa"
     * StringUtils.replace("abaa", "a", "", -1)   = "b"
     * StringUtils.replace("abaa", "a", "z", 0)   = "abaa"
     * StringUtils.replace("abaa", "a", "z", 1)   = "zbaa"
     * StringUtils.replace("abaa", "a", "z", 2)   = "zbza"
     * StringUtils.replace("abaa", "a", "z", -1)  = "zbzz"
     * </pre>
     *
     * @param text text to search and replace in, may be null
     * @param repl the String to search for, may be null
     * @param with the String to replace with, may be null
     * @param max  maximum number of values to replace, or <code>-1</code> if no
     *             maximum
     * @return the text with any replacements processed, <code>null</code> if
     * null String input
     */
    public static String replace(String text, String repl, String with, int max) {
        if (isNULL(text) || isNULL(repl) || with == null || max == 0) {
            return text;
        }
        int start = 0;
        int end = text.indexOf(repl, start);
        if (end == -1) {
            return text;
        }
        int replLength = repl.length();
        int increase = with.length() - replLength;
        increase = (increase < 0 ? 0 : increase);
        increase *= (max < 0 ? 16 : (max > 64 ? 64 : max));
        StringBuilder buf = new StringBuilder(text.length() + increase);
        while (end != -1) {
            buf.append(text.substring(start, end)).append(with);
            start = end + replLength;
            if (--max == 0) {
                break;
            }
            end = text.indexOf(repl, start);
        }
        buf.append(text.substring(start));
        return buf.toString();
    }

    /*
     * 判断一字段值是否数字都是字符
     */
    public static boolean isNumber(String numStr) {
        if (isNULL(numStr))
            return false;
        String s = numStr.replaceAll("[0-9;]+", "");
        return s.trim().equals("");
    }
    public static boolean isStandardNumber(String str) {
        if (isNULL(str)) {
            return false;
        }
        char[] chars = str.toCharArray();
        int sz = chars.length;
        boolean hasExp = false;
        boolean hasDecPoint = false;
        boolean allowSigns = false;
        boolean foundDigit = false;
        // deal with any possible sign up front
        int start = (chars[0] == '-') ? 1 : 0;
        if (sz > start + 1) {
            if (chars[start] == '0' && chars[start + 1] == 'x') {
                int i = start + 2;
                if (i == sz) {
                    return false; // str == "0x"
                }
                // checking hex (it can't be anything else)
                for (; i < chars.length; i++) {
                    if ((chars[i] < '0' || chars[i] > '9')
                            && (chars[i] < 'a' || chars[i] > 'f')
                            && (chars[i] < 'A' || chars[i] > 'F')) {
                        return false;
                    }
                }
                return true;
            }
        }
        sz--; // don't want to loop to the last char, check it afterwords
        // for type qualifiers
        int i = start;
        // loop to the next to last char or to the last char if we need another
        // digit to
        // make a valid number (e.g. chars[0..5] = "1234E")
        while (i < sz || (i < sz + 1 && allowSigns && !foundDigit)) {
            if (chars[i] >= '0' && chars[i] <= '9') {
                foundDigit = true;
                allowSigns = false;

            } else if (chars[i] == '.') {
                if (hasDecPoint || hasExp) {
                    // two decimal points or dec in exponent
                    return false;
                }
                hasDecPoint = true;
            } else if (chars[i] == 'e' || chars[i] == 'E') {
                // we've already taken care of hex.
                if (hasExp) {
                    // two E's
                    return false;
                }
                if (!foundDigit) {
                    return false;
                }
                hasExp = true;
                allowSigns = true;
            } else if (chars[i] == '+' || chars[i] == '-') {
                if (!allowSigns) {
                    return false;
                }
                allowSigns = false;
                foundDigit = false; // we need a digit after the E
            } else {
                return false;
            }
            i++;
        }
        if (i < chars.length) {
            if (chars[i] >= '0' && chars[i] <= '9') {
                // no type qualifier, OK
                return true;
            }
            if (chars[i] == 'e' || chars[i] == 'E') {
                // can't have an E at the last byte
                return false;
            }
            if (!allowSigns
                    && (chars[i] == 'd' || chars[i] == 'D' || chars[i] == 'f' || chars[i] == 'F')) {
                return foundDigit;
            }
            if (chars[i] == 'l' || chars[i] == 'L') {
                // not allowing L with an exponent
                return foundDigit && !hasExp;
            }
            // last zhex is illegal
            return false;
        }
        // allowSigns is true iff the val ends in 'E'
        // found digit it to make sure weird stuff like '.' and '1E-' doesn't
        // pass
        return !allowSigns && foundDigit;
    }

    /**
     * 切分字符串
     *
     * @param source 切分字符串
     * @param delim  切分表示
     * @return String[] 返回切分结果
     */
    public static String[] split(String source, String delim) {
        if (isNULL(delim))
            delim = ";";
        if (source == null)
            return new String[0];
        return source.split(delim);
    }



    /**
     * 字符串数组中是否包含指定的字符串。
     *
     * @param strings       字符串数组
     * @param string        字符串
     * @param caseSensitive 是否大小写敏感
     * @return 包含时返回true，否则返回false
     * @since 0.4
     */
    public static boolean contains(String[] strings, String string,
                                   boolean caseSensitive) {
        for (String string1 : strings) {
            if (caseSensitive) {
                if (string1.equals(string)) {
                    return true;
                }
            } else {
                if (string1.equalsIgnoreCase(string)) {
                    return true;
                }
            }
        }
        return false;
    }

    /*
     * 判断是否为空
     */
    public static boolean isNULL(String value) {
        return ToolsKit.isEmpty(value);
    }

    /**
     * 中文按照两个的长处理
     *
     * @param ibein
     * @param iend
     * @return String
     */
    static public String substring(String str, int ibein, int iend) {
        StringBuilder resultString = new StringBuilder();
        StringBuilder buff = new StringBuilder(str);
        int result = 0;
        for (int i = 0; i < buff.length(); i++) {
            if (buff.substring(i, i + 1).matches("[\\u4E00-\\u9FA5]+")) {
                result = result + 2;
            } else {
                result++;
            }
            if (result > ibein && result <= iend) {
                resultString.append(buff.substring(i, i + 1));
            }
        }
        return resultString.toString();
    }


    /**
     * 提供把字符串转为整数，失败返回0
     *
     * @param value 转换的数字
     * @return 四舍五入后的结果
     */
    public static int toInt(String value) {
        return toInt(value, 0);
    }

    /**
     * 提供把字符串转为整数
     *
     * @param value 转换的数字
     * @param nint  空备用返回
     * @return 四舍五入后的结果
     */
    public static int toInt(String value, int nint) {
        if (value == null)
            return nint;
        if (!StringUtils.isStandardNumber(value))
            return nint;
        if (value.indexOf(".") != -1) {
            try {
                return (int) Double.parseDouble(value);
            } catch (NumberFormatException e) {
                return nint;
            }
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return nint;
        }
    }


    /**
     * @param s ip字符串
     * @return 判断是否为IP
     */
    static public boolean isIPAddress(String s) {
        if (isNULL(s))
            return false;
        String[] ips = s.split("\\.");
        if (ips == null || ips.length != 4)
            return false;
        for (String ip : ips) {
            int xx = StringUtils.toInt(ip);
            if (0 < xx || xx > 255)
                return false;
        }
        return true;
    }

    /**
     * 是否是手机号码
     *  验证号码 手机号
     * @param
     * @return
     */

    public static boolean isPhoneNumberValid(String phoneNumber) {
        boolean isValid = false;

        String expression = "((^(13|15|18)[0-9]{9}$)|" +
                "(^0[1,2]{1}\\d{1}-?\\d{8}$)|" +
                "(^0[3-9] {1}\\d{2}-?\\d{7,8}$)|" +
                "(^0[1,2]{1}\\d{1}-?\\d{8}-(\\d{1,4})$)|" +
                "(^0[3-9]{1}\\d{2}-? \\d{7,8}-(\\d{1,4})$))";
        CharSequence inputStr = phoneNumber;

        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches() ) {
            isValid = true;
        }
        return isValid;

    }

    /**
     * @param sip IP 专 IP 数字
     * @return String String 返回
     */
    static public long toIpNumber(String sip) {
        if (!isIPAddress(sip))
            sip = "127.0.0.1";
        String[] ip = sip.split("\\.");
        long a = Integer.parseInt(ip[0]);
        long b = Integer.parseInt(ip[1]);
        long c = Integer.parseInt(ip[2]);
        long d = Integer.parseInt(ip[3]);
        return a * 256 * 256 * 256 + b * 256 * 256 + c * 256 + d;
    }

    /**
     * 是否是数字
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }


    public static boolean isBlank(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if ((Character.isWhitespace(str.charAt(i)) == false)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 等同于 !isBlank(str)
     *
     * @param str
     * @return
     */
    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }


    /**
     * 过滤 汉字、数字、字母、下划线以外的字符
     *
     * @param str
     * @return
     */
    public static String stringFilter(String str) throws PatternSyntaxException {
        String reg = "[^a-zA-Z0-9\u4E00-\u9FA5_]";
        return str.replaceAll(reg, "");
    }
}