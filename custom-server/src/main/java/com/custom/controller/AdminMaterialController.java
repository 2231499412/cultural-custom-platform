package com.custom.controller;

import com.custom.common.PageResult;
import com.custom.common.Result;
import com.custom.dto.MaterialQueryDTO;
import com.custom.entity.Material;
import com.custom.mapper.MaterialMapper;
import com.custom.service.MaterialService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/material")
@RequiredArgsConstructor
@Tag(name = "后台素材管理")
public class AdminMaterialController {

    private final MaterialService materialService;
    private final MaterialMapper materialMapper;

    @GetMapping("/list")
    @Operation(summary = "素材列表（后台）")
    public Result<PageResult<Material>> list(MaterialQueryDTO query) {
        return Result.success(materialService.listMaterials(query));
    }

    @PostMapping("/save")
    @Operation(summary = "新增/更新素材")
    public Result<Void> save(@RequestBody Material material) {
        if (material.getId() != null) {
            materialMapper.updateById(material);
        } else {
            materialMapper.insert(material);
        }
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除素材")
    public Result<Void> delete(@PathVariable Long id) {
        materialMapper.deleteById(id);
        return Result.success();
    }
}
