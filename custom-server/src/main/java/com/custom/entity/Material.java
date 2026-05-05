package com.custom.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("material")
public class Material {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    private String type;

    private String category;

    private String url;

    private Integer sortOrder;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableLogic
    private Integer deleted;
}
