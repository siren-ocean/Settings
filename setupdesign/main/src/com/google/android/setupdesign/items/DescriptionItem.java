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

package com.google.android.setupdesign.items;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import com.google.android.setupdesign.R;
import com.google.android.setupdesign.util.DescriptionStyler;

/**
 * Definition of an item in an {@link ItemHierarchy}. An item is usually defined in XML and inflated
 * using {@link ItemInflater}.
 */
public class DescriptionItem extends Item {

  private boolean applyPartnerDescriptionStyle = false;

  public DescriptionItem() {
    super();
  }

  public DescriptionItem(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public boolean shouldApplyPartnerDescriptionStyle() {
    return applyPartnerDescriptionStyle;
  }

  /**
   * Applies partner description style on the title of the item, i.e. the TextView with {@code
   * R.id.sud_items_title}.
   */
  public void setApplyPartnerDescriptionStyle(boolean applyPartnerDescriptionStyle) {
    this.applyPartnerDescriptionStyle = applyPartnerDescriptionStyle;
    notifyItemChanged();
  }

  @Override
  public void onBindView(View view) {
    super.onBindView(view);
    TextView label = (TextView) view.findViewById(R.id.sud_items_title);
    if (shouldApplyPartnerDescriptionStyle()) {
      DescriptionStyler.applyPartnerCustomizationStyle(label);
    }
  }
}
