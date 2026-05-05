package com.custom.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.custom.common.PageResult;
import com.custom.dto.MaterialQueryDTO;
import com.custom.entity.Material;
import com.custom.mapper.MaterialMapper;
import com.custom.service.MaterialService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MaterialServiceImpl implements MaterialService {

    private final MaterialMapper materialMapper;

    @Override
    public PageResult<Material> listMaterials(MaterialQueryDTO query) {
        LambdaQueryWrapper<Material> wrapper = new LambdaQueryWrapper<>();

        if (query.getType() != null && !query.getType().isEmpty()) {
            wrapper.eq(Material::getType, query.getType());
        }
        if (query.getCategory() != null && !query.getCategory().isEmpty()) {
            wrapper.eq(Material::getCategory, query.getCategory());
        }
        wrapper.orderByAsc(Material::getSortOrder);

        Page<Material> page = materialMapper.selectPage(
                new Page<>(query.getCurrent(), query.getSize()),
                wrapper
        );

        return new PageResult<>(page.getRecords(), page.getTotal(), page.getCurrent(), page.getSize());
    }
}
