package com.custom.controller;

import com.custom.common.PageResult;
import com.custom.common.Result;
import com.custom.dto.TemplateQueryDTO;
import com.custom.dto.TemplateSaveDTO;
import com.custom.entity.Template;
import com.custom.mapper.TemplateMapper;
import com.custom.service.TemplateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/template")
@RequiredArgsConstructor
@Tag(name = "后台模板管理")
public class AdminTemplateController {

    private final TemplateMapper templateMapper;
    private final TemplateService templateService;

    @GetMapping("/list")
    @Operation(summary = "模板列表（后台）")
    public Result<PageResult<Template>> list(TemplateQueryDTO query) {
        return Result.success(templateService.listTemplates(query));
    }

    @PostMapping("/save")
    @Operation(summary = "新增/更新模板")
    public Result<Void> save(@Valid @RequestBody TemplateSaveDTO dto) {
        Template template = new Template();
        BeanUtils.copyProperties(dto, template);
        if (dto.getId() != null) {
            templateMapper.updateById(template);
        } else {
            templateMapper.insert(template);
        }
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除模板")
    public Result<Void> delete(@PathVariable Long id) {
        templateMapper.deleteById(id);
        return Result.success();
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "上下架模板")
    public Result<Void> updateStatus(@PathVariable Long id, @RequestParam Integer status) {
        Template template = new Template();
        template.setId(id);
        template.setStatus(status);
        templateMapper.updateById(template);
        return Result.success();
    }
}
