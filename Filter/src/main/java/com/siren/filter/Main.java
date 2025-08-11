package com.siren.filter;

import java.io.File;

/**
 * 主程序
 * Created by Siren on 2022/4/7.
 */
public class Main {

    /**
     * 执行过滤任务
     */
    public static void main(String[] args) {
        filterResource();
        filterAndroidPrv();
        filterAttrs();
        addSuffixForImage();
    }

    /**
     * 过滤资源文件
     */
    private static void filterResource() {
        String[] arr = new String[]{
                "res",
                "SettingsLib/res",
                "WifiTrackerLib/res",
                "SettingsLib/HelpUtils/res",
                "SettingsLib/RestrictedLockUtils/res",
                "SettingsLib/SearchWidget/res"
        };

        for (String name : arr) {
            String path = System.getProperty("user.dir") + File.separator + name;
            // 可选项：清除多余的国际化语言，可提高编译效率
            FilterMultiLang.filter(path);
            // 必选项：清除string里面的product属性，如tablet、device等，因为AS无法识别该属性，会编译不通过
            FilterAttribute.filter(path);
        }
    }

    /**
     * 过滤?androidprv:attr属性的配置，填充一个默认透明值
     */
    private static void filterAndroidPrv() {
        XmlUtils.modifyXmlValue("res/values/colors.xml", "color", "search_bar_background", "#00ffffff");
        XmlUtils.modifyXmlValue("res/values/colors.xml", "color", "user_avatar_color_bg", "#00ffffff");
        XmlUtils.modifyXmlValue("res/values-night/colors.xml", "color", "search_bar_background", "#00ffffff");
        XmlUtils.modifyXmlValue("res/values/styles.xml", "item", "cardBackgroundColor", "#00ffffff");
    }

    /**
     * 过滤冲突的属性, 提取出来作为全局attr
     */
    private static void filterAttrs() {
        String path = "res/values/attrs.xml";
        XmlUtils.extractAttr(path, "messageText", "reference");
        XmlUtils.extractAttr(path, "title", "reference");
        ShellUtils.ignoreFile(System.getProperty("user.dir") + File.separator + path);
    }

    /**
     * 给无后缀名的图片加上后缀名
     */
    private static void addSuffixForImage() {
        String folderPath = System.getProperty("user.dir") + "/res/drawable-nodpi/";
        String[] paths = new File(folderPath).list();
        for (String s : paths) {
            if (!s.endsWith(".jpg") && !s.endsWith(".png")) {
                File oldFile = new File(folderPath, s);
                File newFile = new File(folderPath, s + ".jpg");
                oldFile.renameTo(newFile);
                //忽略文件
                ShellUtils.ignoreFile(oldFile.getPath());
            }
        }
    }
}
