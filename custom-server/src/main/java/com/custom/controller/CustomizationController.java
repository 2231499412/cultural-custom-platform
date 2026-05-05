package com.custom.controller;

import com.custom.common.PageResult;
import com.custom.common.Result;
import com.custom.dto.CustomizationSaveDTO;
import com.custom.entity.Customization;
import com.custom.service.CustomizationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customization")
@RequiredArgsConstructor
@Tag(name = "定制接口")
public class CustomizationController {

    private final CustomizationService customizationService;

    @PostMapping("/save")
    @Operation(summary = "保存定制方案")
    public Result<Long> save(@Valid @RequestBody CustomizationSaveDTO dto, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        Long id = customizationService.saveCustomization(userId, dto);
        return Result.success(id);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取定制详情")
    public Result<Customization> detail(@PathVariable Long id, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return Result.success(customizationService.getCustomization(id, userId));
    }

    @GetMapping("/my")
    @Operation(summary = "我的定制列表")
    public Result<PageResult<Customization>> my(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return Result.success(customizationService.getMyCustomizations(userId, current, size));
    }
}
