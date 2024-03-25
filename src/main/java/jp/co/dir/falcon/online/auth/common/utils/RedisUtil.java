package jp.co.dir.falcon.online.auth.common.utils;

import jakarta.annotation.Resource;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
@Order(-1)
public final class RedisUtil {

    @Resource
    private RedisTemplate redisTemplate;

    /**
     * キャッシュの有効期限を指定する
     *
     * @param key  鍵
     * @param time 時間(秒)
     * @return 0
     */

    public boolean expire(String key, long time) {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * キーに基づいて有効期限を取得する
     *
     * @param key キーを null にすることはできません
     * @return 時間 (秒) 0 を返すと永続的に有効であることを意味します
     */
    public long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * キーが存在するかどうかを確認する
     *
     * @param key 鍵
     * @return true 存在します false 存在しません
     */
    public boolean hasKey(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * キャッシュの削除
     *
     * @param key 1 つまたは複数の値を渡すことができます
     */
    @SuppressWarnings("unchecked")
    public void del(String... key) {

        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete(CollectionUtils.arrayToList(key));
            }
        }
    }

    /**
     * 通常のキャッシュ取得
     *
     * @param key 鍵
     * @return 価値
     */
    public Object get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    /**
     * 通常のキャッシュプット
     *
     * @param key   鍵
     * @param value 価値
     * @return true成功 false失敗
     */
    public boolean set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 通常のキャッシュの書き込みと設定時間
     *
     * @param key   鍵
     * @param value 価値
     * @param time  時間 (秒) time は 0 より大きくなければなりません。time が 0 以下の場合は、無期限に設定されます。
     * @return true成功 false 失敗
     */
    public boolean set(String key, Object value, long time) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * インクリメント
     *
     * @param key   鍵
     * @param delta どれだけ増やすか (0 より大きい)
     * @return
     */
    public long incr(String key, long delta) {

        if (delta < 0) {
            throw new RuntimeException("増分係数は 0 より大きくなければなりません");
        }
        return redisTemplate.opsForValue().increment(key, delta);
    }
}


