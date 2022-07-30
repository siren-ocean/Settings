/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.android.setupdesign.util;

import android.app.Activity;
import android.content.Intent;
import com.google.android.setupcompat.util.WizardManagerHelper;

/** The helper class holds the constant names of themes and util functions */
public class ThemeHelper {

  /**
   * Passed in a setup wizard intent as {@link WizardManagerHelper#EXTRA_THEME}. This is the dark
   * variant of the theme used in setup wizard for Nougat MR1.
   */
  public static final String THEME_GLIF = "glif";

  /**
   * Passed in a setup wizard intent as {@link WizardManagerHelper#EXTRA_THEME}. This is the default
   * theme used in setup wizard for Nougat MR1.
   */
  public static final String THEME_GLIF_LIGHT = "glif_light";

  /**
   * Passed in a setup wizard intent as {@link WizardManagerHelper#EXTRA_THEME}. This is the dark
   * variant of the theme used in setup wizard for O DR.
   */
  public static final String THEME_GLIF_V2 = "glif_v2";

  /**
   * Passed in a setup wizard intent as {@link WizardManagerHelper#EXTRA_THEME}. This is the default
   * theme used in setup wizard for O DR.
   */
  public static final String THEME_GLIF_V2_LIGHT = "glif_v2_light";

  /**
   * Passed in a setup wizard intent as {@link WizardManagerHelper#EXTRA_THEME}. This is the dark
   * variant of the theme used in setup wizard for P.
   */
  public static final String THEME_GLIF_V3 = "glif_v3";

  /**
   * Passed in a setup wizard intent as {@link WizardManagerHelper#EXTRA_THEME}. This is the default
   * theme used in setup wizard for P.
   */
  public static final String THEME_GLIF_V3_LIGHT = "glif_v3_light";

  public static final String THEME_HOLO = "holo";
  public static final String THEME_HOLO_LIGHT = "holo_light";
  public static final String THEME_MATERIAL = "material";
  public static final String THEME_MATERIAL_LIGHT = "material_light";

  /**
   * Checks the intent whether the extra indicates that the light theme should be used or not. If
   * the theme is not specified in the intent, or the theme specified is unknown, the value def will
   * be returned. Note that day-night themes are not taken into account by this method.
   *
   * @param intent The intent used to start the activity, which the theme extra will be read from.
   * @param def The default value if the theme is not specified.
   * @return True if the activity started by the given intent should use light theme.
   */
  public static boolean isLightTheme(Intent intent, boolean def) {
    final String theme = intent.getStringExtra(WizardManagerHelper.EXTRA_THEME);
    return isLightTheme(theme, def);
  }

  /**
   * Checks whether {@code theme} represents a light or dark theme. If the theme specified is
   * unknown, the value def will be returned. Note that day-night themes are not taken into account
   * by this method.
   *
   * @param theme The theme as specified from an intent sent from setup wizard.
   * @param def The default value if the theme is not known.
   * @return True if {@code theme} represents a light theme.
   */
  public static boolean isLightTheme(String theme, boolean def) {
    if (THEME_HOLO_LIGHT.equals(theme)
        || THEME_MATERIAL_LIGHT.equals(theme)
        || THEME_GLIF_LIGHT.equals(theme)
        || THEME_GLIF_V2_LIGHT.equals(theme)
        || THEME_GLIF_V3_LIGHT.equals(theme)) {
      return true;
    } else if (THEME_HOLO.equals(theme)
        || THEME_MATERIAL.equals(theme)
        || THEME_GLIF.equals(theme)
        || THEME_GLIF_V2.equals(theme)
        || THEME_GLIF_V3.equals(theme)) {
      return false;
    } else {
      return def;
    }
  }

  /**
   * Reads the theme from the intent, and applies the theme to the activity as resolved by {@link
   * ThemeResolver#getDefault()}.
   *
   * <p>If you require extra theme attributes, consider overriding {@link
   * android.app.Activity#onApplyThemeResource} in your activity and call {@link
   * android.content.res.Resources.Theme#applyStyle(int, boolean)} using your theme overlay.
   *
   * <pre>{@code
   * protected void onApplyThemeResource(Theme theme, int resid, boolean first) {
   *     super.onApplyThemeResource(theme, resid, first);
   *     theme.applyStyle(R.style.MyThemeOverlay, true);
   * }
   * }</pre>
   *
   * @param activity the activity to get the intent from and apply the resulting theme to.
   */
  public static void applyTheme(Activity activity) {
    ThemeResolver.getDefault().applyTheme(activity);
  }
}
