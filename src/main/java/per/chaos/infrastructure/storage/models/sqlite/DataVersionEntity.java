package per.chaos.infrastructure.storage.models.sqlite;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

/**
 * 数据版本实体
 */
@Getter
@Setter
@TableName(value = "ct_data_version")
public class DataVersionEntity extends BaseEntity {
    /**
     * 数据版本
     */
    @TableField(value = "data_version")
    private String dataVersion;
}
