package per.chaos.infra.storage.models.sqlite;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import per.chaos.infra.storage.models.sqlite.base.BaseEntity;

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
