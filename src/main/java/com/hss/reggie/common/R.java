package com.hss.reggie.common;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
@Data
public class R<T> implements Serializable {//序列化
    private Integer code; //编码：1成功，0和其它数字为失败

    private String msg; //错误信息

    private T data; //数据

    private Map map = new HashMap(); //动态数据

    public static <T> R<T> success(T object) {
        R<T> r = new R<T>();
        r.setData(object);
        r.setCode(1);
        return r;
    }

    public static <T> R<T> error(String msg) {
        R<T> r = new R<>();
        r.setMsg(msg);
        r.setCode(0);
        return r;
    }

    public R<T> add(String key, Object value) {
        this.map.put(key, value);
        return this;
    }
}
