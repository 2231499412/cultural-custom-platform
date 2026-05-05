package com.custom.controller;

import com.custom.common.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@RestController
@RequestMapping("/file")
@Tag(name = "文件接口")
public class FileController {

    @Value("${file.upload-dir:./uploads/}")
    private String uploadDir;

    @PostMapping("/upload")
    @Operation(summary = "上传文件")
    public Result<String> upload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return Result.error("请选择文件");
        }

        // 检查文件类型
        String originalName = file.getOriginalFilename();
        String suffix = "";
        if (originalName != null && originalName.contains(".")) {
            suffix = originalName.substring(originalName.lastIndexOf("."));
        }
        String[] allowedTypes = {".jpg", ".jpeg", ".png", ".gif", ".webp", ".svg"};
        boolean isAllowed = false;
        for (String type : allowedTypes) {
            if (type.equalsIgnoreCase(suffix)) {
                isAllowed = true;
                break;
            }
        }
        if (!isAllowed) {
            return Result.error("仅支持 jpg/png/gif/webp/svg 格式");
        }

        // 按日期生成子目录
        String dateDir = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        String newFileName = UUID.randomUUID().toString().replace("-", "") + suffix;

        // 使用绝对路径
        Path basePath = Paths.get(uploadDir).toAbsolutePath();
        Path targetPath = basePath.resolve(dateDir).resolve(newFileName);

        // 确保目录存在
        try {
            Files.createDirectories(targetPath.getParent());
        } catch (IOException e) {
            return Result.error("创建目录失败: " + e.getMessage());
        }

        try {
            file.transferTo(targetPath.toFile());
        } catch (IOException e) {
            return Result.error("文件上传失败: " + e.getMessage());
        }

        // 返回访问路径
        String url = "/uploads/" + dateDir + "/" + newFileName;
        return Result.success(url);
    }
}
