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
import androidx.annotation.Nullable;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.android.setupcompat.partnerconfig.PartnerConfig;
import com.google.android.setupcompat.partnerconfig.PartnerConfigHelper;
import com.google.android.setupdesign.util.TextViewPartnerStyler.TextPartnerConfigs;

/**
 * Helper class to apply the partner customization for the header area widgets. The user needs to
 * check if the header area widgets should apply partner heavy theme before calling these methods.
 */
public final class HeaderAreaStyler {

  /** Applies the partner style of header text to the given textView {@code header}. */
  public static void applyPartnerCustomizationHeaderStyle(@Nullable TextView header) {

    if (header == null) {
      return;
    }
    TextViewPartnerStyler.applyPartnerCustomizationStyle(
        header,
        new TextPartnerConfigs(
            PartnerConfig.CONFIG_HEADER_TEXT_COLOR,
            null,
            PartnerConfig.CONFIG_HEADER_TEXT_SIZE,
            PartnerConfig.CONFIG_HEADER_FONT_FAMILY,
            PartnerStyleHelper.getLayoutGravity(header.getContext())));
  }

  /** Applies the partner style of header background to the given layout {@code headerArea}. */
  public static void applyPartnerCustomizationHeaderAreaStyle(ViewGroup headerArea) {

    if (headerArea == null) {
      return;
    }
    Context context = headerArea.getContext();
    int color =
        PartnerConfigHelper.get(context)
            .getColor(context, PartnerConfig.CONFIG_HEADER_AREA_BACKGROUND_COLOR);
    headerArea.setBackgroundColor(color);
  }

  /** Applies the partner style of header icon to the given {@code iconImage}. */
  public static void applyPartnerCustomizationIconStyle(@Nullable ImageView iconImage) {

    if (iconImage == null) {
      return;
    }

    int gravity = PartnerStyleHelper.getLayoutGravity(iconImage.getContext());
    if (gravity != 0) {
      setGravity(iconImage, gravity);
    }
  }

  private static void setGravity(ImageView icon, int gravity) {
    if (icon.getLayoutParams() instanceof LinearLayout.LayoutParams) {
      LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) icon.getLayoutParams();
      layoutParams.gravity = gravity;
      icon.setLayoutParams(layoutParams);
    }
  }

  private HeaderAreaStyler() {}
}
