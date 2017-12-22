package com.ro.xdroid.net.ok.taotong;

import com.lzy.okgo.convert.Converter;
import com.ro.xdroid.XDroidConfig;
import com.ro.xdroid.kit.JsonKit;
import com.ro.xdroid.kit.LoggerKit;
import com.ro.xdroid.kit.SecurityKit;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by roffee on 2017/8/1 09:53.
 * Contact with 460545614@qq.com
 */
public class TTConvert<T> implements Converter<T> {
    private Type type;
    private Class<T> clazz;
    private boolean hasDesParams;

    public TTConvert() {

    }

    public TTConvert(boolean hasDesParams) {
        this.hasDesParams = hasDesParams;
    }

    public TTConvert(boolean hasDesParams, Class<T> clazz) {
        this.hasDesParams = hasDesParams;
        this.clazz = clazz;
    }

    public TTConvert(boolean hasDesParams, Type type) {
        this.hasDesParams = hasDesParams;
        this.type = type;
    }

    public TTConvert(boolean hasDesParams, Class<T> clazz, Type type) {
        this.hasDesParams = hasDesParams;
        this.clazz = clazz;
        this.type = type;
    }

    /**
     * 该方法是子线程处理，不能做ui相关的工作
     * 主要作用是解析网络返回的 response 对象，生成onSuccess回调中需要的数据对象
     * 这里的解析工作不同的业务逻辑基本都不一样,所以需要自己实现,以下给出的时模板代码,实际使用根据需要修改
     */
    @Override
    public T convertResponse(Response response) throws Throwable {
        if (response == null || response.body() == null) throw new Exception("请求响应体为空");

        if (clazz != null) {
            return parseClass(response, clazz);
        } else if (type != null) {
            if (type instanceof ParameterizedType) {
                return parseParameterizedType(response, (ParameterizedType) type);
            } else if (type instanceof Class) {
                //noinspection unchecked
                return parseClass(response, (Class<T>) type);
            } else {
                return parseType(response, type);
            }
        } else {
            //noinspection unchecked
            return (T) directResponseData(response.body());
        }
    }

    private String desData(String s) throws Exception {
        try {
            JSONObject jsonObject = new JSONObject(s);
            if (jsonObject.has(XDroidConfig.ReqParamKeyEntrypt)) {
                return SecurityKit.desDecode(jsonObject.getString(XDroidConfig.ReqParamKeyEntrypt));
            } else {
                return s;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("数据解密失败");
        }
    }

    private String directResponseData(ResponseBody responseBody) throws Exception {
        if (hasDesParams) {
            return desData(responseBody.string());
        } else {
            return responseBody.string();
        }
    }

    private T parseClass(Response response, Class<T> clazz) throws Exception {
        if (clazz == null) throw new Exception("泛型类型为空");

        String rt;
        if (hasDesParams) rt = desData(response.body().string());
        else rt = response.body().string();
        LoggerKit.json(rt);
        try {
            if (clazz == String.class) {
                //noinspection unchecked
                return (T) rt;
            } else if (clazz == JSONObject.class) {
                //noinspection unchecked
                return (T) new JSONObject(rt);
            } else if (clazz == JSONArray.class) {
                //noinspection unchecked
                return (T) new JSONArray(rt);
            } else {
                T t = JsonKit.fromJson(rt, clazz);
//                if(hasDesParams){
//                    t = JsonKit.fromJson(rt, (Type) rawType);
//                }else{
//                    t = JsonKit.fromJson(new JsonReader(response.body().charStream()), rawType);
//                }
//                response.close();
                return t;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Json解析失败");
        } finally {
            response.close();
        }
    }

    private T parseType(Response response, Type type) throws Exception {
        try {
            T t;
            if (hasDesParams) {
                String dt = desData(response.body().string());
                LoggerKit.json(dt);
                t = JsonKit.fromJson(dt, type);
            } else {
//                t = JsonKit.fromJson(new JsonReader(response.body().charStream()), type);
                t = JsonKit.fromJson(response.body().string(), type);
            }
            response.close();
            return t;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Json解析失败");
        }
//        finally {
//            response.close();
//        }
    }

    private T parseParameterizedType(Response response, ParameterizedType type) throws Exception {
//        Type rawType = type.getRawType();                     // 泛型的实际类型
//        Type typeArgument = type.getActualTypeArguments()[0]; // 泛型的参数
        try {
            T t;
            if (hasDesParams) {
                String dt = desData(response.body().string());
                LoggerKit.json(dt);
                t = JsonKit.fromJson(dt, type);
            } else {
//                t = JsonKit.fromJson(new JsonReader(body.charStream()), type);
                t = JsonKit.fromJson(response.body().string(), type);
            }
            response.close();
            return t;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Json解析失败");
        }
    }
}
