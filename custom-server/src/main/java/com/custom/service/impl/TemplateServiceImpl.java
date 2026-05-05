package com.custom.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.custom.common.PageResult;
import com.custom.dto.TemplateQueryDTO;
import com.custom.entity.Template;
import com.custom.mapper.TemplateMapper;
import com.custom.service.TemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TemplateServiceImpl implements TemplateService {

    private final TemplateMapper templateMapper;

    @Override
    public PageResult<Template> listTemplates(TemplateQueryDTO query) {
        LambdaQueryWrapper<Template> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Template::getStatus, 1);

        if (query.getCategory() != null && !query.getCategory().isEmpty()) {
            wrapper.eq(Template::getCategory, query.getCategory());
        }
        if (query.getKeyword() != null && !query.getKeyword().isEmpty()) {
            wrapper.like(Template::getName, query.getKeyword());
        }
        wrapper.orderByAsc(Template::getSortOrder);

        Page<Template> page = templateMapper.selectPage(
                new Page<>(query.getCurrent(), query.getSize()),
                wrapper
        );

        return new PageResult<>(page.getRecords(), page.getTotal(), page.getCurrent(), page.getSize());
    }

    @Override
    public Template getTemplateById(Long id) {
        Template template = templateMapper.selectById(id);
        if (template == null) {
            throw new RuntimeException("模板不存在");
        }
        return template;
    }
}
