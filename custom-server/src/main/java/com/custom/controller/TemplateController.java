package com.custom.controller;

import com.custom.common.PageResult;
import com.custom.common.Result;
import com.custom.dto.TemplateQueryDTO;
import com.custom.entity.Template;
import com.custom.service.TemplateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/template")
@RequiredArgsConstructor
@Tag(name = "模板接口")
public class TemplateController {

    private final TemplateService templateService;

    @GetMapping("/list")
    @Operation(summary = "模板列表")
    public Result<PageResult<Template>> list(TemplateQueryDTO query) {
        return Result.success(templateService.listTemplates(query));
    }

    @GetMapping("/{id}")
    @Operation(summary = "模板详情")
    public Result<Template> detail(@PathVariable Long id) {
        return Result.success(templateService.getTemplateById(id));
    }

    @GetMapping("/categories")
    @Operation(summary = "获取分类列表")
    public Result<List<String>> categories() {
        return Result.success(Arrays.asList("ticket", "acrylic", "keychain", "badge"));
    }
}
