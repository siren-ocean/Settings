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

package com.google.android.setupcompat;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.PersistableBundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import com.google.android.setupcompat.internal.LifecycleFragment;
import com.google.android.setupcompat.internal.PersistableBundles;
import com.google.android.setupcompat.internal.TemplateLayout;
import com.google.android.setupcompat.logging.CustomEvent;
import com.google.android.setupcompat.logging.MetricKey;
import com.google.android.setupcompat.logging.SetupMetricsLogger;
import com.google.android.setupcompat.partnerconfig.PartnerConfigHelper;
import com.google.android.setupcompat.template.FooterBarMixin;
import com.google.android.setupcompat.template.FooterButton;
import com.google.android.setupcompat.template.StatusBarMixin;
import com.google.android.setupcompat.template.SystemNavBarMixin;
import com.google.android.setupcompat.util.WizardManagerHelper;

/** A templatization layout with consistent style used in Setup Wizard or app itself. */
public class PartnerCustomizationLayout extends TemplateLayout {
  // Log tags can have at most 23 characters on N or before.
  private static final String TAG = "PartnerCustomizedLayout";

  /**
   * Attribute indicating whether usage of partner theme resources is allowed. This corresponds to
   * the {@code app:sucUsePartnerResource} XML attribute. Note that when running in setup wizard,
   * this is always overridden to true.
   */
  private boolean usePartnerResourceAttr;

  private Activity activity;

  public PartnerCustomizationLayout(Context context) {
    this(context, 0, 0);
  }

  public PartnerCustomizationLayout(Context context, int template) {
    this(context, template, 0);
  }

  public PartnerCustomizationLayout(Context context, int template, int containerId) {
    super(context, template, containerId);
    init(null, R.attr.sucLayoutTheme);
  }

  public PartnerCustomizationLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(attrs, R.attr.sucLayoutTheme);
  }

  @TargetApi(VERSION_CODES.HONEYCOMB)
  public PartnerCustomizationLayout(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(attrs, defStyleAttr);
  }

  private void init(AttributeSet attrs, int defStyleAttr) {

    TypedArray a =
        getContext()
            .obtainStyledAttributes(
                attrs, R.styleable.SucPartnerCustomizationLayout, defStyleAttr, 0);

    boolean layoutFullscreen =
        a.getBoolean(R.styleable.SucPartnerCustomizationLayout_sucLayoutFullscreen, true);

    a.recycle();

    if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP && layoutFullscreen) {
      setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    registerMixin(
        StatusBarMixin.class, new StatusBarMixin(this, activity.getWindow(), attrs, defStyleAttr));
    registerMixin(SystemNavBarMixin.class, new SystemNavBarMixin(this, activity.getWindow()));
    registerMixin(FooterBarMixin.class, new FooterBarMixin(this, attrs, defStyleAttr));

    getMixin(SystemNavBarMixin.class).applyPartnerCustomizations(attrs, defStyleAttr);

    // Override the FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS, FLAG_TRANSLUCENT_STATUS,
    // FLAG_TRANSLUCENT_NAVIGATION and SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN attributes of window forces
    // showing status bar and navigation bar.
    if (Build.VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
      activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
      activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
      activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
    }
  }

  @Override
  protected View onInflateTemplate(LayoutInflater inflater, int template) {
    if (template == 0) {
      template = R.layout.partner_customization_layout;
    }
    return inflateTemplate(inflater, 0, template);
  }

  /**
   * {@inheritDoc}
   *
   * <p>This method sets all these flags before onTemplateInflated since it will be too late and get
   * incorrect flag value on PartnerCustomizationLayout if sets them after onTemplateInflated.
   */
  @Override
  protected void onBeforeTemplateInflated(AttributeSet attrs, int defStyleAttr) {

    boolean isSetupFlow;

    // Sets default value to true since this timing
    // before PartnerCustomization members initialization
    usePartnerResourceAttr = true;

    activity = lookupActivityFromContext(getContext());

    isSetupFlow = WizardManagerHelper.isAnySetupWizard(activity.getIntent());

    TypedArray a =
        getContext()
            .obtainStyledAttributes(
                attrs, R.styleable.SucPartnerCustomizationLayout, defStyleAttr, 0);

    if (!a.hasValue(R.styleable.SucPartnerCustomizationLayout_sucUsePartnerResource)) {
      // TODO: Enable Log.WTF after other client already set sucUsePartnerResource.
      Log.e(TAG, "Attribute sucUsePartnerResource not found in " + activity.getComponentName());
    }

    usePartnerResourceAttr =
        isSetupFlow
            || a.getBoolean(R.styleable.SucPartnerCustomizationLayout_sucUsePartnerResource, true);

    a.recycle();

    if (Log.isLoggable(TAG, Log.DEBUG)) {
      Log.d(
          TAG,
          "activity="
              + activity.getClass().getSimpleName()
              + " isSetupFlow="
              + isSetupFlow
              + " enablePartnerResourceLoading="
              + enablePartnerResourceLoading()
              + " usePartnerResourceAttr="
              + usePartnerResourceAttr);
    }
  }

  @Override
  protected ViewGroup findContainer(int containerId) {
    if (containerId == 0) {
      containerId = R.id.suc_layout_content;
    }
    return super.findContainer(containerId);
  }

  @Override
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    LifecycleFragment.attachNow(activity);
    getMixin(FooterBarMixin.class).onAttachedToWindow();
  }

  @Override
  protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP
        && Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
        && WizardManagerHelper.isAnySetupWizard(activity.getIntent())) {
      FooterBarMixin footerBarMixin = getMixin(FooterBarMixin.class);
      footerBarMixin.onDetachedFromWindow();
      FooterButton primaryButton = footerBarMixin.getPrimaryButton();
      FooterButton secondaryButton = footerBarMixin.getSecondaryButton();
      PersistableBundle primaryButtonMetrics =
          primaryButton != null
              ? primaryButton.getMetrics("PrimaryFooterButton")
              : PersistableBundle.EMPTY;
      PersistableBundle secondaryButtonMetrics =
          secondaryButton != null
              ? secondaryButton.getMetrics("SecondaryFooterButton")
              : PersistableBundle.EMPTY;

      PersistableBundle persistableBundle =
          PersistableBundles.mergeBundles(
              footerBarMixin.getLoggingMetrics(), primaryButtonMetrics, secondaryButtonMetrics);

      SetupMetricsLogger.logCustomEvent(
          getContext(),
          CustomEvent.create(MetricKey.get("SetupCompatMetrics", activity), persistableBundle));
    }
  }

  private static Activity lookupActivityFromContext(Context context) {
    if (context instanceof Activity) {
      return (Activity) context;
    } else if (context instanceof ContextWrapper) {
      return lookupActivityFromContext(((ContextWrapper) context).getBaseContext());
    } else {
      throw new IllegalArgumentException("Cannot find instance of Activity in parent tree");
    }
  }

  /**
   * Returns true if partner resource loading is enabled. If true, and other necessary conditions
   * for loading theme attributes are met, this layout will use customized theme attributes from OEM
   * overlays. This is intended to be used with flag-based development, to allow a flag to control
   * the rollout of partner resource loading.
   */
  protected boolean enablePartnerResourceLoading() {
    return true;
  }

  /** Returns if the current layout/activity applies partner customized configurations or not. */
  public boolean shouldApplyPartnerResource() {
    if (!enablePartnerResourceLoading()) {
      return false;
    }
    if (!usePartnerResourceAttr) {
      return false;
    }
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
      return false;
    }
    if (!PartnerConfigHelper.get(getContext()).isAvailable()) {
      return false;
    }
    return true;
  }
}
