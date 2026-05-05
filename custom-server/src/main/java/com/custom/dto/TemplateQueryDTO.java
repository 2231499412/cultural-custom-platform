package com.custom.dto;

import lombok.Data;

@Data
public class TemplateQueryDTO {

    private String category;
    private String keyword;
    private Integer current = 1;
    private Integer size = 12;
}
