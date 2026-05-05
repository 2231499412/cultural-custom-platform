package com.custom.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("template")
public class Template {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    private String category;

    private String description;

    private String previewUrl;

    private String structureJson;

    private BigDecimal price;

    private Integer status;

    private Integer sortOrder;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private Integer deleted;
}
