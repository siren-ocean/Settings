package com.siren.filter;

import java.io.File;
import java.io.IOException;

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
        filterAndroidPrvAttr();
        replaceContent();
    }

    /**
     * 过滤资源文件
     */
    private static void filterResource() {
        String[] stringArray = new String[]{
                "res",
                "res-product"
        };

        for (String name : stringArray) {
            String path = System.getProperty("user.dir") + File.separator + name;
            // 可选项：清除多余的国际化语言，可提高编译效率
            FilterMultiLang.filter(path);
            // 必选项：清除string里面的product属性，如tablet、device等，因为AS无法识别该属性，会编译不通过
            FilterAttribute.filter(path);
        }

        String configPath = System.getProperty("user.dir") + File.separator + "res-product/values/config.xml";
        FilterAttribute.eliminate(new File(configPath), "item");
        String drawablePath = System.getProperty("user.dir") + File.separator + "res-product/values/drawables.xml";
        FilterAttribute.eliminate(new File(drawablePath), "drawable");
    }

    private static void filterAndroidPrvAttr() {
        String[] pathArray = new String[]{
                "res/values/colors.xml",
                "res/values-night/colors.xml",
                "res/values/themes.xml",
                "res/values/styles.xml",
                "SettingsLib/res/values/styles.xml",
        };

        try {
            for (String path : pathArray) {
                FileContentReplacer.replaceAndroidPrv(path, "#00000000");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 暴力过滤，解决一些资源属性引用失败问题，android14之后，资源引用收紧，可能还有别的问题，遇到了再加
     */
    private static void replaceContent() {
        // 处理对*android:lockPatternStyle的赋值会导致崩溃
        FileContentReplacer.replaceInPath("res/values/themes.xml",
                "<item name=\"*android:lockPatternStyle\">@style/LockPatternStyle</item>",
                "<item name=\"*android:lockPatternStyle\">@null</item>");

        FileContentReplacer.replaceInPath("res/values/themes_suw.xml",
                "<item name=\"*android:lockPatternStyle\">@style/LockPatternStyle</item>",
                "<item name=\"*android:lockPatternStyle\">@null</item>");

        /*
         *************************************处理config_dialogCornerRadius引用失败问题************************************
         */
        FileContentReplacer.replaceInPath("res/values/themes.xml",
                "<item name=\"dialogCornerRadius\">@*android:dimen/config_dialogCornerRadius</item>",
                "<item name=\"dialogCornerRadius\">28dp</item>");

        FileContentReplacer.replaceInPath("res/values/dimens.xml",
                "<dimen name=\"contextual_card_corner_radius\">@*android:dimen/config_dialogCornerRadius</dimen>",
                "<dimen name=\"contextual_card_corner_radius\">2dp</dimen>");

        /*
         *************************************处理音量条引用异常问题************************************
         */
        FileContentReplacer.replaceInPath("res/layout/preference_volume_slider.xml",
                "android:id=\"@*android:id/seekbar\"",
                "android:id=\"@+id/seekbar\"");

        FileContentReplacer.replaceInPath("src/com/android/settings/widget/SeekBarPreference.java",
                "com.android.internal.R.id.seekbar",
                "com.android.settings.R.id.seekbar");

        FileContentReplacer.replaceInPath("src/com/android/settings/notification/VolumeSeekBarPreference.java",
                "com.android.internal.R.id.seekbar",
                "com.android.settings.R.id.seekbar");
    }
}