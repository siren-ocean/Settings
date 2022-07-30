/*
 * Copyright (C) 2019 The Android Open Source Project
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

import android.content.Context;
import android.graphics.Typeface;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.TypedValue;
import android.widget.TextView;
import com.google.android.setupcompat.partnerconfig.PartnerConfig;
import com.google.android.setupcompat.partnerconfig.PartnerConfigHelper;

/** Helper class to apply partner configurations to a textView. */
final class TextViewPartnerStyler {

  /** Applies given partner configurations {@code textPartnerConfigs} to the {@code textView}. */
  public static void applyPartnerCustomizationStyle(
      @NonNull TextView textView, @NonNull TextPartnerConfigs textPartnerConfigs) {

    if (textView == null || textPartnerConfigs == null) {
      return;
    }

    Context context = textView.getContext();
    if (textPartnerConfigs.getTextColorConfig() != null) {
      int textColor =
          PartnerConfigHelper.get(context)
              .getColor(context, textPartnerConfigs.getTextColorConfig());
      if (textColor != 0) {
        textView.setTextColor(textColor);
      }
    }

    if (textPartnerConfigs.getTextLinkedColorConfig() != null) {
      int linkTextColor =
          PartnerConfigHelper.get(context)
              .getColor(context, textPartnerConfigs.getTextLinkedColorConfig());
      if (linkTextColor != 0) {
        textView.setLinkTextColor(linkTextColor);
      }
    }

    if (textPartnerConfigs.getTextSizeConfig() != null) {
      float textSize =
          PartnerConfigHelper.get(context)
              .getDimension(context, textPartnerConfigs.getTextSizeConfig(), 0);
      if (textSize > 0) {
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
      }
    }

    if (textPartnerConfigs.getTextFontFamilyConfig() != null) {
      String fontFamilyName =
          PartnerConfigHelper.get(context)
              .getString(context, textPartnerConfigs.getTextFontFamilyConfig());
      Typeface font = Typeface.create(fontFamilyName, Typeface.NORMAL);
      if (font != null) {
        textView.setTypeface(font);
      }
    }

    textView.setGravity(textPartnerConfigs.getTextGravity());
  }

  /** Keeps the partner conflagrations for a textView. */
  public static class TextPartnerConfigs {
    private final PartnerConfig textColorConfig;
    private final PartnerConfig textLinkedColorConfig;
    private final PartnerConfig textSizeConfig;
    private final PartnerConfig textFontFamilyConfig;
    private final int textGravity;

    public TextPartnerConfigs(
        @Nullable PartnerConfig textColorConfig,
        @Nullable PartnerConfig textLinkedColorConfig,
        @Nullable PartnerConfig textSizeConfig,
        @Nullable PartnerConfig textFontFamilyConfig,
        int textGravity) {
      this.textColorConfig = textColorConfig;
      this.textLinkedColorConfig = textLinkedColorConfig;
      this.textSizeConfig = textSizeConfig;
      this.textFontFamilyConfig = textFontFamilyConfig;
      this.textGravity = textGravity;
    }

    public PartnerConfig getTextColorConfig() {
      return textColorConfig;
    }

    public PartnerConfig getTextLinkedColorConfig() {
      return textLinkedColorConfig;
    }

    public PartnerConfig getTextSizeConfig() {
      return textSizeConfig;
    }

    public PartnerConfig getTextFontFamilyConfig() {
      return textFontFamilyConfig;
    }

    public int getTextGravity() {
      return textGravity;
    }
  }

  private TextViewPartnerStyler() {}
}
