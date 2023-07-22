package per.chaos.app.configs;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.Collection;

public interface MyBaseMapper<T> extends BaseMapper<T> {
    int insertBatchSomeColumn(Collection<T> entityList);
}
