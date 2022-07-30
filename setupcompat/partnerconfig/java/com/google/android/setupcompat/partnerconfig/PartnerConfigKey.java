/*
 * Copyright 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.android.setupcompat.partnerconfig;

import androidx.annotation.StringDef;
import androidx.annotation.VisibleForTesting;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/** Resource names that can be customized by partner overlay APK. */
@Retention(RetentionPolicy.SOURCE)
@StringDef({
  PartnerConfigKey.KEY_STATUS_BAR_BACKGROUND,
  PartnerConfigKey.KEY_LIGHT_STATUS_BAR,
  PartnerConfigKey.KEY_NAVIGATION_BAR_BG_COLOR,
  PartnerConfigKey.KEY_LIGHT_NAVIGATION_BAR,
  PartnerConfigKey.KEY_FOOTER_BAR_BG_COLOR,
  PartnerConfigKey.KEY_FOOTER_BUTTON_FONT_FAMILY,
  PartnerConfigKey.KEY_FOOTER_BUTTON_ICON_ADD_ANOTHER,
  PartnerConfigKey.KEY_FOOTER_BUTTON_ICON_CANCEL,
  PartnerConfigKey.KEY_FOOTER_BUTTON_ICON_CLEAR,
  PartnerConfigKey.KEY_FOOTER_BUTTON_ICON_DONE,
  PartnerConfigKey.KEY_FOOTER_BUTTON_ICON_NEXT,
  PartnerConfigKey.KEY_FOOTER_BUTTON_ICON_OPT_IN,
  PartnerConfigKey.KEY_FOOTER_BUTTON_ICON_SKIP,
  PartnerConfigKey.KEY_FOOTER_BUTTON_ICON_STOP,
  PartnerConfigKey.KEY_FOOTER_BUTTON_PADDING_TOP,
  PartnerConfigKey.KEY_FOOTER_BUTTON_PADDING_BOTTOM,
  PartnerConfigKey.KEY_FOOTER_BUTTON_RADIUS,
  PartnerConfigKey.KEY_FOOTER_BUTTON_RIPPLE_ALPHA,
  PartnerConfigKey.KEY_FOOTER_BUTTON_TEXT_SIZE,
  PartnerConfigKey.KEY_FOOTER_BUTTON_DISABLED_ALPHA,
  PartnerConfigKey.KEY_FOOTER_BUTTON_DISABLED_BG_COLOR,
  PartnerConfigKey.KEY_FOOTER_PRIMARY_BUTTON_BG_COLOR,
  PartnerConfigKey.KEY_FOOTER_PRIMARY_BUTTON_TEXT_COLOR,
  PartnerConfigKey.KEY_FOOTER_SECONDARY_BUTTON_BG_COLOR,
  PartnerConfigKey.KEY_FOOTER_SECONDARY_BUTTON_TEXT_COLOR,
  PartnerConfigKey.KEY_LAYOUT_BACKGROUND_COLOR,
  PartnerConfigKey.KEY_HEADER_TEXT_SIZE,
  PartnerConfigKey.KEY_HEADER_TEXT_COLOR,
  PartnerConfigKey.KEY_HEADER_FONT_FAMILY,
  PartnerConfigKey.KEY_HEADER_AREA_BACKGROUND_COLOR,
  PartnerConfigKey.KEY_LAYOUT_GRAVITY,
  PartnerConfigKey.KEY_DESCRIPTION_TEXT_SIZE,
  PartnerConfigKey.KEY_DESCRIPTION_TEXT_COLOR,
  PartnerConfigKey.KEY_DESCRIPTION_LINK_TEXT_COLOR,
  PartnerConfigKey.KEY_DESCRIPTION_FONT_FAMILY,
  PartnerConfigKey.KEY_CONTENT_TEXT_SIZE,
  PartnerConfigKey.KEY_CONTENT_TEXT_COLOR,
  PartnerConfigKey.KEY_CONTENT_LINK_TEXT_COLOR,
  PartnerConfigKey.KEY_CONTENT_FONT_FAMILY,
  PartnerConfigKey.KEY_CONTENT_LAYOUT_GRAVITY,
  PartnerConfigKey.KEY_PROGRESS_ILLUSTRATION_DEFAULT,
  PartnerConfigKey.KEY_PROGRESS_ILLUSTRATION_ACCOUNT,
  PartnerConfigKey.KEY_PROGRESS_ILLUSTRATION_CONNECTION,
  PartnerConfigKey.KEY_PROGRESS_ILLUSTRATION_UPDATE,
  PartnerConfigKey.KEY_PROGRESS_ILLUSTRATION_DISPLAY_MINIMUM_MS,
})
// TODO: can be removed and always reference PartnerConfig.getResourceName()?
@VisibleForTesting(otherwise = VisibleForTesting.PACKAGE_PRIVATE)
public @interface PartnerConfigKey {
  // Status bar background color or illustration.
  String KEY_STATUS_BAR_BACKGROUND = "setup_compat_status_bar_background";

  // The same as "WindowLightStatusBar". If set true, the status bar icons will be drawn such
  // that it is compatible with a light status bar background
  String KEY_LIGHT_STATUS_BAR = "setup_compat_light_status_bar";

  // Navigation bar background color
  String KEY_NAVIGATION_BAR_BG_COLOR = "setup_compat_navigation_bar_bg_color";

  // The same as "windowLightNavigationBar". If set true, the navigation bar icons will be drawn
  // such that it is compatible with a light navigation bar background.
  String KEY_LIGHT_NAVIGATION_BAR = "setup_compat_light_navigation_bar";

  // Background color of the footer bar.
  String KEY_FOOTER_BAR_BG_COLOR = "setup_compat_footer_bar_bg_color";

  // The font face used in footer buttons. This must be a string reference to a font that is
  // available in the system. Font references (@font or @xml) are not allowed.
  String KEY_FOOTER_BUTTON_FONT_FAMILY = "setup_compat_footer_button_font_family";

  // The icon for "add another" action. Can be "@null" for no icon.
  String KEY_FOOTER_BUTTON_ICON_ADD_ANOTHER = "setup_compat_footer_button_icon_add_another";

  // The icon for "cancel" action. Can be "@null" for no icon.
  String KEY_FOOTER_BUTTON_ICON_CANCEL = "setup_compat_footer_button_icon_cancel";

  // The icon for "clear" action. Can be "@null" for no icon.
  String KEY_FOOTER_BUTTON_ICON_CLEAR = "setup_compat_footer_button_icon_clear";

  // The icon for "done" action. Can be "@null" for no icon.
  String KEY_FOOTER_BUTTON_ICON_DONE = "setup_compat_footer_button_icon_done";

  // The icon for "next" action. Can be "@null" for no icon.
  String KEY_FOOTER_BUTTON_ICON_NEXT = "setup_compat_footer_button_icon_next";

  // The icon for "opt-in" action. Can be "@null" for no icon.
  String KEY_FOOTER_BUTTON_ICON_OPT_IN = "setup_compat_footer_button_icon_opt_in";

  // The icon for "skip" action. Can be "@null" for no icon.
  String KEY_FOOTER_BUTTON_ICON_SKIP = "setup_compat_footer_button_icon_skip";

  // The icon for "stop" action. Can be "@null" for no icon.
  String KEY_FOOTER_BUTTON_ICON_STOP = "setup_compat_footer_button_icon_stop";

  // Top padding of the footer buttons
  String KEY_FOOTER_BUTTON_PADDING_TOP = "setup_compat_footer_button_padding_top";

  // Bottom padding of the footer buttons
  String KEY_FOOTER_BUTTON_PADDING_BOTTOM = "setup_compat_footer_button_padding_bottom";

  // Corner radius of the footer buttons
  String KEY_FOOTER_BUTTON_RADIUS = "setup_compat_footer_button_radius";

  // Ripple color alpha of the footer button
  String KEY_FOOTER_BUTTON_RIPPLE_ALPHA = "setup_compat_footer_button_ripple_alpha";

  // Text size of the footer button
  String KEY_FOOTER_BUTTON_TEXT_SIZE = "setup_compat_footer_button_text_size";

  // Disabled background alpha of the footer buttons
  String KEY_FOOTER_BUTTON_DISABLED_ALPHA = "setup_compat_footer_button_disabled_alpha";

  // Disabled background color of the footer buttons
  String KEY_FOOTER_BUTTON_DISABLED_BG_COLOR = "setup_compat_footer_button_disabled_bg_color";

  // Background color of the primary footer button
  String KEY_FOOTER_PRIMARY_BUTTON_BG_COLOR = "setup_compat_footer_primary_button_bg_color";

  // Text color of the primary footer button
  String KEY_FOOTER_PRIMARY_BUTTON_TEXT_COLOR = "setup_compat_footer_primary_button_text_color";

  // Background color of the secondary footer button
  String KEY_FOOTER_SECONDARY_BUTTON_BG_COLOR = "setup_compat_footer_secondary_button_bg_color";

  // Text color of the secondary footer button
  String KEY_FOOTER_SECONDARY_BUTTON_TEXT_COLOR = "setup_compat_footer_secondary_button_text_color";

  // Background color of layout
  String KEY_LAYOUT_BACKGROUND_COLOR = "setup_design_layout_bg_color";

  // Text size of the header
  String KEY_HEADER_TEXT_SIZE = "setup_design_header_text_size";

  // Text color of the header
  String KEY_HEADER_TEXT_COLOR = "setup_design_header_text_color";

  // Font family of the header
  String KEY_HEADER_FONT_FAMILY = "setup_design_header_font_family";

  // Gravity of the header, icon and description
  String KEY_LAYOUT_GRAVITY = "setup_design_layout_gravity";

  // Background color of the header area
  String KEY_HEADER_AREA_BACKGROUND_COLOR = "setup_design_header_area_background_color";

  // Text size of the description
  String KEY_DESCRIPTION_TEXT_SIZE = "setup_design_description_text_size";

  // Text color of the description
  String KEY_DESCRIPTION_TEXT_COLOR = "setup_design_description_text_color";

  // Link text color of the description
  String KEY_DESCRIPTION_LINK_TEXT_COLOR = "setup_design_description_link_text_color";

  // Font family of the description
  String KEY_DESCRIPTION_FONT_FAMILY = "setup_design_description_font_family";

  // Text size of the body content text
  String KEY_CONTENT_TEXT_SIZE = "setup_design_content_text_size";

  // Text color of the body content text
  String KEY_CONTENT_TEXT_COLOR = "setup_design_content_text_color";

  // Link text color of the body content text
  String KEY_CONTENT_LINK_TEXT_COLOR = "setup_design_content_link_text_color";

  // Font family of the body content text
  String KEY_CONTENT_FONT_FAMILY = "setup_design_content_font_family";

  // Gravity of the body content text
  String KEY_CONTENT_LAYOUT_GRAVITY = "setup_design_content_layout_gravity";

  // The animation of loading screen used in those activities which is non of below type.
  String KEY_PROGRESS_ILLUSTRATION_DEFAULT = "progress_illustration_custom_default";

  // The animation of loading screen used in those activity which is processing account info or
  // related functions.
  // For example:com.google.android.setupwizard.LOAD_ADD_ACCOUNT_INTENT
  String KEY_PROGRESS_ILLUSTRATION_ACCOUNT = "progress_illustration_custom_account";

  // The animation of loading screen used in those activity which is processing data connection.
  // For example:com.android.setupwizard.CAPTIVE_PORTAL
  String KEY_PROGRESS_ILLUSTRATION_CONNECTION = "progress_illustration_custom_connection";

  // The animation of loading screen used in those activities which is updating device.
  // For example:com.google.android.setupwizard.COMPAT_EARLY_UPDATE
  String KEY_PROGRESS_ILLUSTRATION_UPDATE = "progress_illustration_custom_update";

  // The minimum illustration display time, set to 0 may cause the illustration stuck
  String KEY_PROGRESS_ILLUSTRATION_DISPLAY_MINIMUM_MS = "progress_illustration_display_minimum_ms";
}
