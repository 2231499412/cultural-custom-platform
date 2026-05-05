package com.custom.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CustomizationSaveDTO {

    @NotNull(message = "模板ID不能为空")
    private Long templateId;

    @NotBlank(message = "定制数据不能为空")
    private String customDataJson;

    private String previewUrl;
}
