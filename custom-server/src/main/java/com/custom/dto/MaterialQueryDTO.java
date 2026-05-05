package com.custom.dto;

import lombok.Data;

@Data
public class MaterialQueryDTO {

    private String type;
    private String category;
    private Integer current = 1;
    private Integer size = 20;
}
