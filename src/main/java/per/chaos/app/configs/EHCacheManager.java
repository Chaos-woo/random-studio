package per.chaos.app.configs;

import org.ehcache.Cache;
import org.ehcache.PersistentCacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.EntryUnit;
import org.ehcache.config.units.MemoryUnit;
import per.chaos.app.configs.cache.models.TTSCache;
import per.chaos.infrastructure.utils.PathUtils;

import java.io.File;

/**
 * EHCache缓存
 */
public class EHCacheManager {
    /**
     * 持久化缓存
     */
    public static PersistentCacheManager persistentCacheManager = CacheManagerBuilder.newCacheManagerBuilder()
            // 设置持久化文件
            .with(CacheManagerBuilder.persistence(new File(PathUtils.getProjectAbsolutePath(), "rclibCacheData")))
            // 设置TTS缓存管理器
            .withCache(
                    // 缓存管理器名字
                    TTSCache.CacheManager.TTS_CACHE_NAME,
                    // 构建缓存配置
                    CacheConfigurationBuilder.newCacheConfigurationBuilder(
                            // Key类型
                            String.class,
                            // Value类型
                            String.class,
                            // 资源池构建
                            ResourcePoolsBuilder.newResourcePoolsBuilder()
                                    // 堆容量
                                    .heap(10, EntryUnit.ENTRIES)
                                    // 硬盘容量
                                    .disk(20, MemoryUnit.MB, true)
                    )
            )
            .build(true);

    /**
     * 获取TTS缓存中的内容
     *
     * @param key 指定key
     */
    public static String getTTSCache(String key) {
        Cache<String, String> cache = persistentCacheManager.getCache(TTSCache.CacheManager.TTS_CACHE_NAME, String.class, String.class);
        return cache.get(key);
    }

    /**
     * 设置TTS缓存中的内容
     *
     * @param key   指定key
     * @param value 指定内容
     */
    public static void putTTSCache(String key, String value) {
        Cache<String, String> cache = persistentCacheManager.getCache(TTSCache.CacheManager.TTS_CACHE_NAME, String.class, String.class);
        cache.put(key, value);
    }
}
