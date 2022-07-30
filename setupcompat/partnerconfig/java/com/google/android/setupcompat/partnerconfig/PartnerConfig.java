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

// TODO: optimize the enum
/** Resources that can be customized by partner overlay APK. */
public enum PartnerConfig {

  // Status bar background color or illustration.
  CONFIG_STATUS_BAR_BACKGROUND(PartnerConfigKey.KEY_STATUS_BAR_BACKGROUND, ResourceType.DRAWABLE),

  // The same as "WindowLightStatusBar". If set true, the status bar icons will be drawn such
  // that it is compatible with a light status bar background
  CONFIG_LIGHT_STATUS_BAR(PartnerConfigKey.KEY_LIGHT_STATUS_BAR, ResourceType.BOOL),

  // Navigation bar background color
  CONFIG_NAVIGATION_BAR_BG_COLOR(PartnerConfigKey.KEY_NAVIGATION_BAR_BG_COLOR, ResourceType.COLOR),

  // Background color of the footer bar.
  CONFIG_FOOTER_BAR_BG_COLOR(PartnerConfigKey.KEY_FOOTER_BAR_BG_COLOR, ResourceType.COLOR),

  // The same as "windowLightNavigationBar". If set true, the navigation bar icons will be drawn
  // such that it is compatible with a light navigation bar background.
  CONFIG_LIGHT_NAVIGATION_BAR(PartnerConfigKey.KEY_LIGHT_NAVIGATION_BAR, ResourceType.BOOL),

  // The font face used in footer buttons. This must be a string reference to a font that is
  // available in the system. Font references (@font or @xml) are not allowed.
  CONFIG_FOOTER_BUTTON_FONT_FAMILY(
      PartnerConfigKey.KEY_FOOTER_BUTTON_FONT_FAMILY, ResourceType.STRING),

  // The icon for "add another" action. Can be "@null" for no icon.
  CONFIG_FOOTER_BUTTON_ICON_ADD_ANOTHER(
      PartnerConfigKey.KEY_FOOTER_BUTTON_ICON_ADD_ANOTHER, ResourceType.DRAWABLE),

  // The icon for "cancel" action. Can be "@null" for no icon.
  CONFIG_FOOTER_BUTTON_ICON_CANCEL(
      PartnerConfigKey.KEY_FOOTER_BUTTON_ICON_CANCEL, ResourceType.DRAWABLE),

  // The icon for "clear" action. Can be "@null" for no icon.
  CONFIG_FOOTER_BUTTON_ICON_CLEAR(
      PartnerConfigKey.KEY_FOOTER_BUTTON_ICON_CLEAR, ResourceType.DRAWABLE),

  // The icon for "done" action. Can be "@null" for no icon.
  CONFIG_FOOTER_BUTTON_ICON_DONE(
      PartnerConfigKey.KEY_FOOTER_BUTTON_ICON_DONE, ResourceType.DRAWABLE),

  // The icon for "next" action. Can be "@null" for no icon.
  CONFIG_FOOTER_BUTTON_ICON_NEXT(
      PartnerConfigKey.KEY_FOOTER_BUTTON_ICON_NEXT, ResourceType.DRAWABLE),

  // The icon for "opt-in" action. Can be "@null" for no icon.
  CONFIG_FOOTER_BUTTON_ICON_OPT_IN(
      PartnerConfigKey.KEY_FOOTER_BUTTON_ICON_OPT_IN, ResourceType.DRAWABLE),

  // The icon for "skip" action. Can be "@null" for no icon.
  CONFIG_FOOTER_BUTTON_ICON_SKIP(
      PartnerConfigKey.KEY_FOOTER_BUTTON_ICON_SKIP, ResourceType.DRAWABLE),

  // The icon for "stop" action. Can be "@null" for no icon.
  CONFIG_FOOTER_BUTTON_ICON_STOP(
      PartnerConfigKey.KEY_FOOTER_BUTTON_ICON_STOP, ResourceType.DRAWABLE),

  // Top padding of the footer buttons
  CONFIG_FOOTER_BUTTON_PADDING_TOP(
      PartnerConfigKey.KEY_FOOTER_BUTTON_PADDING_TOP, ResourceType.DIMENSION),

  // Bottom padding of the footer buttons
  CONFIG_FOOTER_BUTTON_PADDING_BOTTOM(
      PartnerConfigKey.KEY_FOOTER_BUTTON_PADDING_BOTTOM, ResourceType.DIMENSION),

  // Corner radius of the footer buttons
  CONFIG_FOOTER_BUTTON_RADIUS(PartnerConfigKey.KEY_FOOTER_BUTTON_RADIUS, ResourceType.DIMENSION),

  // Ripple color alpha the footer buttons
  CONFIG_FOOTER_BUTTON_RIPPLE_COLOR_ALPHA(
      PartnerConfigKey.KEY_FOOTER_BUTTON_RIPPLE_ALPHA, ResourceType.FRACTION),

  // Text size of the primary footer button
  CONFIG_FOOTER_BUTTON_TEXT_SIZE(
      PartnerConfigKey.KEY_FOOTER_BUTTON_TEXT_SIZE, ResourceType.DIMENSION),

  // Disabled background alpha of the footer buttons
  CONFIG_FOOTER_BUTTON_DISABLED_ALPHA(
      PartnerConfigKey.KEY_FOOTER_BUTTON_DISABLED_ALPHA, ResourceType.FRACTION),

  // Disabled background color of the footer buttons
  CONFIG_FOOTER_BUTTON_DISABLED_BG_COLOR(
      PartnerConfigKey.KEY_FOOTER_BUTTON_DISABLED_BG_COLOR, ResourceType.COLOR),

  // Background color of the primary footer button
  CONFIG_FOOTER_PRIMARY_BUTTON_BG_COLOR(
      PartnerConfigKey.KEY_FOOTER_PRIMARY_BUTTON_BG_COLOR, ResourceType.COLOR),

  // Text color of the primary footer button
  CONFIG_FOOTER_PRIMARY_BUTTON_TEXT_COLOR(
      PartnerConfigKey.KEY_FOOTER_PRIMARY_BUTTON_TEXT_COLOR, ResourceType.COLOR),

  // Background color of the secondary footer button
  CONFIG_FOOTER_SECONDARY_BUTTON_BG_COLOR(
      PartnerConfigKey.KEY_FOOTER_SECONDARY_BUTTON_BG_COLOR, ResourceType.COLOR),

  // Text color of the secondary footer button
  CONFIG_FOOTER_SECONDARY_BUTTON_TEXT_COLOR(
      PartnerConfigKey.KEY_FOOTER_SECONDARY_BUTTON_TEXT_COLOR, ResourceType.COLOR),

  // Background color of layout
  CONFIG_LAYOUT_BACKGROUND_COLOR(PartnerConfigKey.KEY_LAYOUT_BACKGROUND_COLOR, ResourceType.COLOR),

  // Text color of the header
  CONFIG_HEADER_TEXT_COLOR(PartnerConfigKey.KEY_HEADER_TEXT_COLOR, ResourceType.COLOR),

  // Text size of the header
  CONFIG_HEADER_TEXT_SIZE(PartnerConfigKey.KEY_HEADER_TEXT_SIZE, ResourceType.DIMENSION),

  // Font family of the header
  CONFIG_HEADER_FONT_FAMILY(PartnerConfigKey.KEY_HEADER_FONT_FAMILY, ResourceType.STRING),

  // Gravity of the header, icon and description
  CONFIG_LAYOUT_GRAVITY(PartnerConfigKey.KEY_LAYOUT_GRAVITY, ResourceType.STRING),

  // Background color of the header area
  CONFIG_HEADER_AREA_BACKGROUND_COLOR(
      PartnerConfigKey.KEY_HEADER_AREA_BACKGROUND_COLOR, ResourceType.COLOR),

  // Text size of the description
  CONFIG_DESCRIPTION_TEXT_SIZE(PartnerConfigKey.KEY_DESCRIPTION_TEXT_SIZE, ResourceType.DIMENSION),

  // Text color of the description
  CONFIG_DESCRIPTION_TEXT_COLOR(PartnerConfigKey.KEY_DESCRIPTION_TEXT_COLOR, ResourceType.COLOR),

  // Link text color of the description
  CONFIG_DESCRIPTION_LINK_TEXT_COLOR(
      PartnerConfigKey.KEY_DESCRIPTION_LINK_TEXT_COLOR, ResourceType.COLOR),

  // Font family of the description
  CONFIG_DESCRIPTION_FONT_FAMILY(PartnerConfigKey.KEY_DESCRIPTION_FONT_FAMILY, ResourceType.STRING),

  // Text size of the body content text
  CONFIG_CONTENT_TEXT_SIZE(PartnerConfigKey.KEY_CONTENT_TEXT_SIZE, ResourceType.DIMENSION),

  // Text color of the body content text
  CONFIG_CONTENT_TEXT_COLOR(PartnerConfigKey.KEY_CONTENT_TEXT_COLOR, ResourceType.COLOR),

  // Link text color of the body content text
  CONFIG_CONTENT_LINK_TEXT_COLOR(PartnerConfigKey.KEY_CONTENT_LINK_TEXT_COLOR, ResourceType.COLOR),

  // Font family of the body content text
  CONFIG_CONTENT_FONT_FAMILY(PartnerConfigKey.KEY_CONTENT_FONT_FAMILY, ResourceType.STRING),

  // Gravity of the body content text
  CONFIG_CONTENT_LAYOUT_GRAVITY(PartnerConfigKey.KEY_CONTENT_LAYOUT_GRAVITY, ResourceType.STRING),

  // The animation of loading screen used in those activities which is non of below type.
  CONFIG_PROGRESS_ILLUSTRATION_DEFAULT(
      PartnerConfigKey.KEY_PROGRESS_ILLUSTRATION_DEFAULT, ResourceType.ILLUSTRATION),

  // The animation of loading screen used in those activity which is processing account info or
  // related functions.
  // For example:com.google.android.setupwizard.LOAD_ADD_ACCOUNT_INTENT
  CONFIG_PROGRESS_ILLUSTRATION_ACCOUNT(
      PartnerConfigKey.KEY_PROGRESS_ILLUSTRATION_ACCOUNT, ResourceType.ILLUSTRATION),

  // The animation of loading screen used in those activity which is processing data connection.
  // For example:com.android.setupwizard.CAPTIVE_PORTAL
  CONFIG_PROGRESS_ILLUSTRATION_CONNECTION(
      PartnerConfigKey.KEY_PROGRESS_ILLUSTRATION_CONNECTION, ResourceType.ILLUSTRATION),

  // The animation of loading screen used in those activities which is updating device.
  // For example:com.google.android.setupwizard.COMPAT_EARLY_UPDATE
  CONFIG_PROGRESS_ILLUSTRATION_UPDATE(
      PartnerConfigKey.KEY_PROGRESS_ILLUSTRATION_UPDATE, ResourceType.ILLUSTRATION),

  CONFIG_PROGRESS_ILLUSTRATION_DISPLAY_MINIMUM_MS(
      PartnerConfigKey.KEY_PROGRESS_ILLUSTRATION_DISPLAY_MINIMUM_MS, ResourceType.INTEGER);

  public enum ResourceType {
    INTEGER,
    BOOL,
    COLOR,
    DRAWABLE,
    STRING,
    DIMENSION,
    FRACTION,
    ILLUSTRATION;
  }

  private final String resourceName;
  private final ResourceType resourceType;

  public ResourceType getResourceType() {
    return resourceType;
  }

  public String getResourceName() {
    return resourceName;
  }

  PartnerConfig(@PartnerConfigKey String resourceName, ResourceType type) {
    this.resourceName = resourceName;
    this.resourceType = type;
  }
}
