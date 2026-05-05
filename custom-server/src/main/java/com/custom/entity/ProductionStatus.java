package com.custom.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("production_status")
public class ProductionStatus {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long orderId;

    private String statusNode;

    private String remark;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
