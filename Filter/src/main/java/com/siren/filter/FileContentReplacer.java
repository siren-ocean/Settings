package com.siren.filter;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 文件处理工具
 * Created by Siren on 2025/8/2.
 */
public class FileContentReplacer {

    /**
     * 替换文件或目录中的指定字段
     */
    public static void replaceInPath(String path, String searchString, String replacement) {

        try {
            Path targetPath = Paths.get(path);
            if (Files.isRegularFile(targetPath)) {
                // 如果是单个文件，直接处理
                replaceInFile(targetPath, searchString, replacement);
            } else if (Files.isDirectory(targetPath)) {
                // 如果是目录，递归处理目录下的文件
                Files.walk(targetPath)
                        .filter(p -> Files.isRegularFile(p))
                        .forEach(p -> {
                            try {
                                replaceInFile(p, searchString, replacement);
                            } catch (IOException e) {
                                System.err.println("Error: " + p + " - " + e.getMessage());
                            }
                        });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 在单个文件中替换指定字段
     */
    private static void replaceInFile(Path filePath, String searchString, String replacement) throws IOException {
        // 读取文件内容
        String content = new String(Files.readAllBytes(filePath));
        // 检查是否包含要查找的字段
        if (content.contains(searchString)) {
            System.out.println("Replaced file: " + filePath);
            // 执行替换
            String newContent = content.replace(searchString, replacement);
            // 写回文件
            Files.write(filePath, newContent.getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
            ShellUtils.ignoreFile(System.getProperty("user.dir") + File.separator + filePath);
        }
    }

    /**
     * 替换?androidprv:attr 开头的标签内容
     */
    public static void replaceAndroidPrv(String directoryPath, String replacement) throws IOException {
        Path dir = Paths.get(directoryPath);
        Files.walk(dir)
                .filter(path -> {
                    String fileName = path.getFileName().toString();
                    return fileName.toLowerCase().endsWith(".xml");
                })
                .forEach(path -> {
                    try {
                        replaceAndroidPrv(path, replacement);
                    } catch (IOException e) {
                        System.err.println(path + " - " + e.getMessage());
                    }
                });
    }

    public static void replaceAndroidPrv(Path filePath, String replacement) throws IOException {
        // 读取文件内容
        byte[] fileBytes = Files.readAllBytes(filePath);
        String content = new String(fileBytes, StandardCharsets.UTF_8);

        // 匹配XML标签内容中的 ?androidprv:attr 模式
        Pattern pattern = Pattern.compile(">\\s*\\?androidprv:attr[^<]*<");
        Matcher matcher = pattern.matcher(content);

        StringBuilder result = new StringBuilder();
        boolean changed = false;

        while (matcher.find()) {
            changed = true;
            // 替换匹配的内容，保留标签结构
            String replacementText = ">" + replacement + "<";
            matcher.appendReplacement(result, Matcher.quoteReplacement(replacementText));
        }

        if (changed) {
            matcher.appendTail(result);
            // 写入文件
            Files.write(filePath, result.toString().getBytes(StandardCharsets.UTF_8));
            ShellUtils.ignoreFile(System.getProperty("user.dir") + File.separator + filePath);
            System.out.println("Replaced file: " + filePath);
        }
    }
}