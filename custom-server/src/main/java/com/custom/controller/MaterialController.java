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
@RequestMapping("/material")
@RequiredArgsConstructor
@Tag(name = "素材接口")
public class MaterialController {

    private final MaterialService materialService;
    private final MaterialMapper materialMapper;

    @GetMapping("/list")
    @Operation(summary = "素材列表")
    public Result<PageResult<Material>> list(MaterialQueryDTO query) {
        return Result.success(materialService.listMaterials(query));
    }

    @PostMapping("/save")
    @Operation(summary = "新增素材")
    public Result<Void> save(@RequestBody Material material) {
        materialMapper.insert(material);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除素材")
    public Result<Void> delete(@PathVariable Long id) {
        materialMapper.deleteById(id);
        return Result.success();
    }
}
