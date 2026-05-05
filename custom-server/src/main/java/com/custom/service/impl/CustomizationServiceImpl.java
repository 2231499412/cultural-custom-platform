package com.custom.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.custom.common.PageResult;
import com.custom.dto.CustomizationSaveDTO;
import com.custom.entity.Customization;
import com.custom.mapper.CustomizationMapper;
import com.custom.service.CustomizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomizationServiceImpl implements CustomizationService {

    private final CustomizationMapper customizationMapper;

    @Override
    public Long saveCustomization(Long userId, CustomizationSaveDTO dto) {
        Customization customization = new Customization();
        customization.setUserId(userId);
        customization.setTemplateId(dto.getTemplateId());
        customization.setCustomDataJson(dto.getCustomDataJson());
        customization.setPreviewUrl(dto.getPreviewUrl());
        customizationMapper.insert(customization);
        return customization.getId();
    }

    @Override
    public Customization getCustomization(Long id, Long userId) {
        Customization customization = customizationMapper.selectById(id);
        if (customization == null || !customization.getUserId().equals(userId)) {
            throw new RuntimeException("定制记录不存在");
        }
        return customization;
    }

    @Override
    public PageResult<Customization> getMyCustomizations(Long userId, Integer current, Integer size) {
        LambdaQueryWrapper<Customization> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Customization::getUserId, userId)
               .orderByDesc(Customization::getCreateTime);

        Page<Customization> page = customizationMapper.selectPage(
                new Page<>(current, size), wrapper
        );

        return new PageResult<>(page.getRecords(), page.getTotal(), page.getCurrent(), page.getSize());
    }
}
