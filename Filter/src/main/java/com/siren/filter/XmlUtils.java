package com.siren.filter;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Xml工具
 * Created by Siren on 2025/7/26.
 */
public class XmlUtils {

    /**
     * 修改标签内容
     */
    public static void modifyXmlValue(String filePath, String tag, String name, String value) {
        try {
            Path path = Paths.get(filePath);
            List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
            String regex = String.format(
                    "<%s\\s+name=\"%s\">.*</%s>",
                    tag, name, tag
            );

            // 遍历并修改匹配的行
            for (int i = 0; i < lines.size(); i++) {
                if (lines.get(i).matches(".*" + regex + ".*")) {
                    String newLine = lines.get(i).replaceAll(">.*</" + tag + ">", ">" + value + "</" + tag + ">");
                    lines.set(i, newLine);
                    break;
                }
            }

            // 写回文件
            Files.write(path, lines, StandardCharsets.UTF_8);
            ShellUtils.ignoreFile(System.getProperty("user.dir") + File.separator + path);//忽略文件
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 将属性抽取为resources层级的属性
     */
    public static void extractAttr(String path, String name, String format) {
        try {
            String content = Files.readString(Paths.get(path));
            Pattern styleableBlockPattern = Pattern.compile(
                    "<declare-styleable[\\s\\S]*?</declare-styleable>",
                    Pattern.DOTALL
            );

            Matcher blockMatcher = styleableBlockPattern.matcher(content);
            StringBuffer result = new StringBuffer();

            Pattern attrPattern = Pattern.compile(
                    "^(\\s*<attr\\s+name=\"" + name + "\")\\s+format=\"" + format + "\"(\\s*/>)$",
                    Pattern.MULTILINE
            );

            while (blockMatcher.find()) {
                String block = blockMatcher.group();
                Matcher attrMatcher = attrPattern.matcher(block);
                String modifiedBlock = attrMatcher.replaceAll("$1$2");
                blockMatcher.appendReplacement(result, Matcher.quoteReplacement(modifiedBlock));
            }

            blockMatcher.appendTail(result);
            Files.writeString(Paths.get(path), result.toString());
            insertNewLine(path, name, format);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void insertNewLine(String path, String name, String format) throws IOException {
        String content = Files.readString(Paths.get(path));
        String attrLine = "<attr name=\"" + name + "\" format=\"" + format + "\" />";

        if (!content.contains(attrLine)) {
            int insertPos = content.lastIndexOf("</resources>");
            if (insertPos != -1) {
                // 插入 attr 行（带缩进，换行）
                String toInsert = "    " + attrLine + System.lineSeparator();  // 4空格缩进
                content = content.substring(0, insertPos) + toInsert + content.substring(insertPos);
                System.out.println("insert " + attrLine);
            }
        }
        Files.writeString(Paths.get(path), content);
    }
}
