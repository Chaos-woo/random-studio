package per.chaos.infra.storage.models.sqlite;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import per.chaos.infra.runtime.models.files.enums.FileListTypeEnum;
import per.chaos.infra.runtime.models.files.enums.SystemFileTypeEnum;
import per.chaos.infra.storage.models.sqlite.base.BaseEntity;

/**
 * 文件引用实体
 */
@Getter
@Setter
@TableName(value = "ct_file_refer")
public class FileReferEntity extends BaseEntity {
    /**
     * 文件绝对路径
     */
    @TableField(value = "absolute_path")
    private String absolutePath;

    /**
     * 文件路径hash值
     */
    @TableField(value = "path_hash")
    private String pathHash;

    /**
     * 文件名
     */
    @TableField(value = "file_name")
    private String fileName;

    /**
     * 当前文件所属列表
     */
    @TableField(value = "file_list_type")
    private FileListTypeEnum fileListTypeEnum;

    /**
     * 文件所属类型
     */
    @TableField(value = "sys_file_type")
    private SystemFileTypeEnum systemFileTypeEnum;

    /**
     * 文件音频音色
     */
    @TableField(value = "timbre")
    private String timbre;
}
