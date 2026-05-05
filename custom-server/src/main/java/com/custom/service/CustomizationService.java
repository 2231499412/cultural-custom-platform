package com.custom.service;

import com.custom.common.PageResult;
import com.custom.dto.CustomizationSaveDTO;
import com.custom.entity.Customization;

public interface CustomizationService {

    Long saveCustomization(Long userId, CustomizationSaveDTO dto);

    Customization getCustomization(Long id, Long userId);

    PageResult<Customization> getMyCustomizations(Long userId, Integer current, Integer size);
}
