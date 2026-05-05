package com.custom.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TemplateSaveDTO {

    private Long id;

    @NotBlank(message = "模板名称不能为空")
    private String name;

    @NotBlank(message = "分类不能为空")
    private String category;

    private String description;

    @NotBlank(message = "预览图不能为空")
    private String previewUrl;

    @NotBlank(message = "模板结构不能为空")
    private String structureJson;

    @NotNull(message = "价格不能为空")
    private BigDecimal price;

    private Integer status = 1;

    private Integer sortOrder = 0;
}
