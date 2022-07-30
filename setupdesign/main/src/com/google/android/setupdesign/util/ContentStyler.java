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
import android.view.Gravity;
import android.widget.TextView;
import com.google.android.setupcompat.partnerconfig.PartnerConfig;
import com.google.android.setupcompat.partnerconfig.PartnerConfigHelper;
import com.google.android.setupdesign.util.TextViewPartnerStyler.TextPartnerConfigs;
import java.util.Locale;

/**
 * Applies the partner style of content to the given TextView {@code contentText}. The user needs to
 * check if the {@code contentText} should apply partner heavy theme before calling this method.
 */
public final class ContentStyler {
  public static void applyPartnerCustomizationStyle(TextView contentText) {

    TextViewPartnerStyler.applyPartnerCustomizationStyle(
        contentText,
        new TextPartnerConfigs(
            PartnerConfig.CONFIG_CONTENT_TEXT_COLOR,
            PartnerConfig.CONFIG_CONTENT_LINK_TEXT_COLOR,
            PartnerConfig.CONFIG_CONTENT_TEXT_SIZE,
            PartnerConfig.CONFIG_CONTENT_FONT_FAMILY,
            ContentStyler.getPartnerContentTextGravity(contentText.getContext())));
  }

  public static int getPartnerContentTextGravity(Context context) {
    String gravity =
        PartnerConfigHelper.get(context)
            .getString(context, PartnerConfig.CONFIG_CONTENT_LAYOUT_GRAVITY);
    if (gravity == null) {
      return 0;
    }
    switch (gravity.toLowerCase(Locale.ROOT)) {
      case "center":
        return Gravity.CENTER;
      case "start":
        return Gravity.START;
      case "end":
        return Gravity.END;
      default:
        return 0;
    }
  }

  private ContentStyler() {}
}
