
package com.workdance.core.data;

import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 实现Serializable接口的额外对象类，用于存储和管理额外的数据
 * 使用线程安全的映射来存储键值对，其中键为字符串，值为对象
 */
public class ExtraObject implements Serializable {

    // 使用Collections.synchronizedMap包装的线程安全的LinkedHashMap，用于存储额外的数据
    protected final Map<String, Object> mExtras = Collections.synchronizedMap(new LinkedHashMap<>());

    /**
     * 获取与指定键相关联的额外对象
     *
     * @param key 要检索的额外对象的键
     * @param clazz 期望的额外对象的类类型
     * @param <T> 期望的额外对象的类型
     * @return 与键相关联的额外对象，如果键不存在或类型不匹配，则返回null
     * @throws ClassCastException 如果与键相关联的对象不可转换为指定的类型
     */
    public <T> T getExtra(@NonNull String key, @NonNull Class<T> clazz) {
        Object extra = mExtras.get(key);
        if (extra != null) {
            if (clazz.isInstance(extra)) {
                return (T) extra;
            }
            throw new ClassCastException(extra.getClass() + " can't be cast to + " + clazz);
        }
        return null;
    }

    /**
     * 存储一个额外的对象，如果对象为null，则移除与键相关联的任何现有对象
     *
     * @param key 要存储的额外对象的键
     * @param extra 要存储的额外对象，如果为null，则表示移除
     * @throws IllegalArgumentException 如果extra对象的类型不支持序列化或不可写
     */
    public void putExtra(@NonNull String key, @Nullable Object extra) {
        if (extra == null) {
            mExtras.remove(key);
        } else {
            if (extra instanceof Serializable || extra instanceof Parcelable) {
                mExtras.put(key, extra);
            } else {
                throw new IllegalArgumentException("Unsupported type " + extra.getClass());
            }
        }
    }

    /**
     * 清除所有已存储的额外对象
     */
    public void clearExtras() {
        mExtras.clear();
    }
}
