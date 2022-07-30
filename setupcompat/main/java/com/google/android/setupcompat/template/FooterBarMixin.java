/*
 * Copyright (C) 2017 The Android Open Source Project
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

package com.google.android.setupcompat.template;

import static com.google.android.setupcompat.internal.Preconditions.ensureOnMainThread;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.os.PersistableBundle;
import androidx.annotation.AttrRes;
import androidx.annotation.CallSuper;
import androidx.annotation.ColorInt;
import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;
import androidx.annotation.VisibleForTesting;
import android.util.AttributeSet;
import android.util.StateSet;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import com.google.android.setupcompat.PartnerCustomizationLayout;
import com.google.android.setupcompat.R;
import com.google.android.setupcompat.internal.FooterButtonPartnerConfig;
import com.google.android.setupcompat.internal.Preconditions;
import com.google.android.setupcompat.internal.TemplateLayout;
import com.google.android.setupcompat.logging.internal.FooterBarMixinMetrics;
import com.google.android.setupcompat.partnerconfig.PartnerConfig;
import com.google.android.setupcompat.partnerconfig.PartnerConfigHelper;
import com.google.android.setupcompat.template.FooterButton.ButtonType;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A {@link Mixin} for managing buttons. By default, the button bar expects that buttons on the
 * start (left for LTR) are "secondary" borderless buttons, while buttons on the end (right for LTR)
 * are "primary" accent-colored buttons.
 */
public class FooterBarMixin implements Mixin {

  private final Context context;

  @Nullable private final ViewStub footerStub;

  @VisibleForTesting final boolean applyPartnerResources;

  private LinearLayout buttonContainer;
  private FooterButton primaryButton;
  private FooterButton secondaryButton;
  @IdRes private int primaryButtonId;
  @IdRes private int secondaryButtonId;
  ColorStateList primaryDefaultTextColor = null;
  ColorStateList secondaryDefaultTextColor = null;
  @VisibleForTesting public FooterButtonPartnerConfig primaryButtonPartnerConfigForTesting;
  @VisibleForTesting public FooterButtonPartnerConfig secondaryButtonPartnerConfigForTesting;

  private int footerBarPaddingTop;
  private int footerBarPaddingBottom;
  @VisibleForTesting int defaultPadding;
  @ColorInt private final int footerBarPrimaryBackgroundColor;
  @ColorInt private final int footerBarSecondaryBackgroundColor;
  private boolean removeFooterBarWhenEmpty = true;

  private static final float DEFAULT_DISABLED_ALPHA = 0.26f;
  private static final AtomicInteger nextGeneratedId = new AtomicInteger(1);

  @VisibleForTesting public final FooterBarMixinMetrics metrics = new FooterBarMixinMetrics();

  private FooterButton.OnButtonEventListener createButtonEventListener(@IdRes int id) {

    return new FooterButton.OnButtonEventListener() {

      @Override
      public void onEnabledChanged(boolean enabled) {
        if (buttonContainer != null) {
          Button button = buttonContainer.findViewById(id);
          if (button != null) {
            button.setEnabled(enabled);
            if (applyPartnerResources) {
              updateButtonTextColorWithPartnerConfig(
                  button,
                  (id == primaryButtonId)
                      ? PartnerConfig.CONFIG_FOOTER_PRIMARY_BUTTON_TEXT_COLOR
                      : PartnerConfig.CONFIG_FOOTER_SECONDARY_BUTTON_TEXT_COLOR);
            }
          }
        }
      }

      @Override
      public void onVisibilityChanged(int visibility) {
        if (buttonContainer != null) {
          Button button = buttonContainer.findViewById(id);
          if (button != null) {
            button.setVisibility(visibility);
            autoSetButtonBarVisibility();
          }
        }
      }

      @Override
      public void onTextChanged(CharSequence text) {
        if (buttonContainer != null) {
          Button button = buttonContainer.findViewById(id);
          if (button != null) {
            button.setText(text);
          }
        }
      }
    };
  }

  /**
   * Creates a mixin for managing buttons on the footer.
   *
   * @param layout The {@link TemplateLayout} containing this mixin.
   * @param attrs XML attributes given to the layout.
   * @param defStyleAttr The default style attribute as given to the constructor of the layout.
   */
  public FooterBarMixin(
      TemplateLayout layout, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
    context = layout.getContext();
    footerStub = layout.findManagedViewById(R.id.suc_layout_footer);
    this.applyPartnerResources =
        layout instanceof PartnerCustomizationLayout
            && ((PartnerCustomizationLayout) layout).shouldApplyPartnerResource();

    TypedArray a =
        context.obtainStyledAttributes(attrs, R.styleable.SucFooterBarMixin, defStyleAttr, 0);
    defaultPadding =
        a.getDimensionPixelSize(R.styleable.SucFooterBarMixin_sucFooterBarPaddingVertical, 0);
    footerBarPaddingTop =
        a.getDimensionPixelSize(
            R.styleable.SucFooterBarMixin_sucFooterBarPaddingTop, defaultPadding);
    footerBarPaddingBottom =
        a.getDimensionPixelSize(
            R.styleable.SucFooterBarMixin_sucFooterBarPaddingBottom, defaultPadding);
    footerBarPrimaryBackgroundColor =
        a.getColor(R.styleable.SucFooterBarMixin_sucFooterBarPrimaryFooterBackground, 0);
    footerBarSecondaryBackgroundColor =
        a.getColor(R.styleable.SucFooterBarMixin_sucFooterBarSecondaryFooterBackground, 0);

    int primaryBtn =
        a.getResourceId(R.styleable.SucFooterBarMixin_sucFooterBarPrimaryFooterButton, 0);
    int secondaryBtn =
        a.getResourceId(R.styleable.SucFooterBarMixin_sucFooterBarSecondaryFooterButton, 0);
    a.recycle();

    FooterButtonInflater inflater = new FooterButtonInflater(context);

    if (secondaryBtn != 0) {
      setSecondaryButton(inflater.inflate(secondaryBtn));
      metrics.logPrimaryButtonInitialStateVisibility(/* isVisible= */ true, /* isUsingXml= */ true);
    }

    if (primaryBtn != 0) {
      setPrimaryButton(inflater.inflate(primaryBtn));
      metrics.logSecondaryButtonInitialStateVisibility(
          /* isVisible= */ true, /* isUsingXml= */ true);
    }
  }

  private View addSpace() {
    LinearLayout buttonContainer = ensureFooterInflated();
    View space = new View(buttonContainer.getContext());
    space.setLayoutParams(new LayoutParams(0, 0, 1.0f));
    space.setVisibility(View.INVISIBLE);
    buttonContainer.addView(space);
    return space;
  }

  @NonNull
  private LinearLayout ensureFooterInflated() {
    if (buttonContainer == null) {
      if (footerStub == null) {
        throw new IllegalStateException("Footer stub is not found in this template");
      }
      buttonContainer = (LinearLayout) inflateFooter(R.layout.suc_footer_button_bar);
      onFooterBarInflated(buttonContainer);
      onFooterBarApplyPartnerResource(buttonContainer);
    }
    return buttonContainer;
  }

  /**
   * Notifies that the footer bar has been inflated to the view hierarchy. Calling super is
   * necessary while subclass implement it.
   */
  @CallSuper
  protected void onFooterBarInflated(LinearLayout buttonContainer) {
    if (buttonContainer == null) {
      // Ignore action since buttonContainer is null
      return;
    }
    if (Build.VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN_MR1) {
      buttonContainer.setId(View.generateViewId());
    } else {
      buttonContainer.setId(generateViewId());
    }
    updateFooterBarPadding(
        buttonContainer,
        buttonContainer.getPaddingLeft(),
        footerBarPaddingTop,
        buttonContainer.getPaddingRight(),
        footerBarPaddingBottom);
  }

  /**
   * Notifies while the footer bar apply Partner Resource. Calling super is necessary while subclass
   * implement it.
   */
  @CallSuper
  protected void onFooterBarApplyPartnerResource(LinearLayout buttonContainer) {
    if (buttonContainer == null) {
      // Ignore action since buttonContainer is null
      return;
    }
    if (!applyPartnerResources) {
      return;
    }

    @ColorInt
    int color =
        PartnerConfigHelper.get(context)
            .getColor(context, PartnerConfig.CONFIG_FOOTER_BAR_BG_COLOR);
    buttonContainer.setBackgroundColor(color);

    footerBarPaddingTop =
        (int)
            PartnerConfigHelper.get(context)
                .getDimension(context, PartnerConfig.CONFIG_FOOTER_BUTTON_PADDING_TOP);
    footerBarPaddingBottom =
        (int)
            PartnerConfigHelper.get(context)
                .getDimension(context, PartnerConfig.CONFIG_FOOTER_BUTTON_PADDING_BOTTOM);
    updateFooterBarPadding(
        buttonContainer,
        buttonContainer.getPaddingLeft(),
        footerBarPaddingTop,
        buttonContainer.getPaddingRight(),
        footerBarPaddingBottom);
  }

  /**
   * Inflate FooterActionButton with layout "suc_button". Subclasses can implement this method to
   * modify the footer button layout as necessary.
   */
  @SuppressLint("InflateParams")
  protected FooterActionButton createThemedButton(Context context, @StyleRes int theme) {
    // Inflate a single button from XML, which when using support lib, will take advantage of
    // the injected layout inflater and give us AppCompatButton instead.
    LayoutInflater inflater = LayoutInflater.from(new ContextThemeWrapper(context, theme));
    return (FooterActionButton) inflater.inflate(R.layout.suc_button, null, false);
  }

  /** Sets primary button for footer. */
  @MainThread
  public void setPrimaryButton(FooterButton footerButton) {
    ensureOnMainThread("setPrimaryButton");
    ensureFooterInflated();

    // Setup button partner config
    FooterButtonPartnerConfig footerButtonPartnerConfig =
        new FooterButtonPartnerConfig.Builder(footerButton)
            .setPartnerTheme(
                getPartnerTheme(
                    footerButton,
                    /* defaultPartnerTheme= */ R.style.SucPartnerCustomizationButton_Primary,
                    /* buttonBackgroundColorConfig= */ PartnerConfig
                        .CONFIG_FOOTER_PRIMARY_BUTTON_BG_COLOR))
            .setButtonBackgroundConfig(PartnerConfig.CONFIG_FOOTER_PRIMARY_BUTTON_BG_COLOR)
            .setButtonDisableAlphaConfig(PartnerConfig.CONFIG_FOOTER_BUTTON_DISABLED_ALPHA)
            .setButtonDisableBackgroundConfig(PartnerConfig.CONFIG_FOOTER_BUTTON_DISABLED_BG_COLOR)
            .setButtonIconConfig(getDrawablePartnerConfig(footerButton.getButtonType()))
            .setButtonRadiusConfig(PartnerConfig.CONFIG_FOOTER_BUTTON_RADIUS)
            .setButtonRippleColorAlphaConfig(PartnerConfig.CONFIG_FOOTER_BUTTON_RIPPLE_COLOR_ALPHA)
            .setTextColorConfig(PartnerConfig.CONFIG_FOOTER_PRIMARY_BUTTON_TEXT_COLOR)
            .setTextSizeConfig(PartnerConfig.CONFIG_FOOTER_BUTTON_TEXT_SIZE)
            .setTextTypeFaceConfig(PartnerConfig.CONFIG_FOOTER_BUTTON_FONT_FAMILY)
            .build();

    FooterActionButton button = inflateButton(footerButton, footerButtonPartnerConfig);
    // update information for primary button. Need to update as long as the button inflated.
    primaryButtonId = button.getId();
    primaryDefaultTextColor = button.getTextColors();
    primaryButton = footerButton;
    primaryButtonPartnerConfigForTesting = footerButtonPartnerConfig;

    onFooterButtonInflated(button, footerBarPrimaryBackgroundColor);
    onFooterButtonApplyPartnerResource(button, footerButtonPartnerConfig);

    // Make sure the position of buttons are correctly and prevent primary button create twice or
    // more.
    repopulateButtons();
  }

  /** Returns the {@link FooterButton} of primary button. */
  public FooterButton getPrimaryButton() {
    return primaryButton;
  }

  @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
  public Button getPrimaryButtonView() {
    return buttonContainer == null ? null : buttonContainer.findViewById(primaryButtonId);
  }

  @VisibleForTesting
  boolean isPrimaryButtonVisible() {
    return getPrimaryButtonView() != null && getPrimaryButtonView().getVisibility() == View.VISIBLE;
  }

  /** Sets secondary button for footer. */
  @MainThread
  public void setSecondaryButton(FooterButton footerButton) {
    ensureOnMainThread("setSecondaryButton");
    ensureFooterInflated();

    // Setup button partner config
    FooterButtonPartnerConfig footerButtonPartnerConfig =
        new FooterButtonPartnerConfig.Builder(footerButton)
            .setPartnerTheme(
                getPartnerTheme(
                    footerButton,
                    /* defaultPartnerTheme= */ R.style.SucPartnerCustomizationButton_Secondary,
                    /* buttonBackgroundColorConfig= */ PartnerConfig
                        .CONFIG_FOOTER_SECONDARY_BUTTON_BG_COLOR))
            .setButtonBackgroundConfig(PartnerConfig.CONFIG_FOOTER_SECONDARY_BUTTON_BG_COLOR)
            .setButtonDisableAlphaConfig(PartnerConfig.CONFIG_FOOTER_BUTTON_DISABLED_ALPHA)
            .setButtonDisableBackgroundConfig(PartnerConfig.CONFIG_FOOTER_BUTTON_DISABLED_BG_COLOR)
            .setButtonIconConfig(getDrawablePartnerConfig(footerButton.getButtonType()))
            .setButtonRadiusConfig(PartnerConfig.CONFIG_FOOTER_BUTTON_RADIUS)
            .setButtonRippleColorAlphaConfig(PartnerConfig.CONFIG_FOOTER_BUTTON_RIPPLE_COLOR_ALPHA)
            .setTextColorConfig(PartnerConfig.CONFIG_FOOTER_SECONDARY_BUTTON_TEXT_COLOR)
            .setTextSizeConfig(PartnerConfig.CONFIG_FOOTER_BUTTON_TEXT_SIZE)
            .setTextTypeFaceConfig(PartnerConfig.CONFIG_FOOTER_BUTTON_FONT_FAMILY)
            .build();

    FooterActionButton button = inflateButton(footerButton, footerButtonPartnerConfig);
    // update information for secondary button. Need to update as long as the button inflated.
    secondaryButtonId = button.getId();
    secondaryDefaultTextColor = button.getTextColors();
    secondaryButton = footerButton;
    secondaryButtonPartnerConfigForTesting = footerButtonPartnerConfig;

    onFooterButtonInflated(button, footerBarSecondaryBackgroundColor);
    onFooterButtonApplyPartnerResource(button, footerButtonPartnerConfig);

    // Make sure the position of buttons are correctly and prevent secondary button create twice or
    // more.
    repopulateButtons();
  }

  /**
   * Corrects the order of footer buttons after the button has been inflated to the view hierarchy.
   * Subclasses can implement this method to modify the order of footer buttons as necessary.
   */
  protected void repopulateButtons() {
    LinearLayout buttonContainer = ensureFooterInflated();
    Button tempPrimaryButton = getPrimaryButtonView();
    Button tempSecondaryButton = getSecondaryButtonView();
    buttonContainer.removeAllViews();

    if (tempSecondaryButton != null) {
      buttonContainer.addView(tempSecondaryButton);
    }
    addSpace();
    if (tempPrimaryButton != null) {
      buttonContainer.addView(tempPrimaryButton);
    }
  }

  /**
   * Notifies that the footer button has been inInflated and add to the view hierarchy. Calling
   * super is necessary while subclass implement it.
   */
  @CallSuper
  protected void onFooterButtonInflated(Button button, @ColorInt int defaultButtonBackgroundColor) {
    // Try to set default background
    if (defaultButtonBackgroundColor != 0) {
      updateButtonBackground(button, defaultButtonBackgroundColor);
    } else {
      // TODO: get button background color from activity theme
    }
    buttonContainer.addView(button);
    autoSetButtonBarVisibility();
  }

  private int getPartnerTheme(
      FooterButton footerButton,
      int defaultPartnerTheme,
      PartnerConfig buttonBackgroundColorConfig) {
    int overrideTheme = footerButton.getTheme();

    // Set the default theme if theme is not set, or when running in setup flow.
    if (footerButton.getTheme() == 0 || applyPartnerResources) {
      overrideTheme = defaultPartnerTheme;
    }
    // TODO: Make sure customize attributes in theme can be applied during setup flow.
    // If sets background color to full transparent, the button changes to colored borderless ink
    // button style.
    if (applyPartnerResources) {
      int color = PartnerConfigHelper.get(context).getColor(context, buttonBackgroundColorConfig);
      if (color == Color.TRANSPARENT) {
        overrideTheme = R.style.SucPartnerCustomizationButton_Secondary;
      } else if (color != Color.TRANSPARENT) {
        // TODO: remove the constrain (color != Color.WHITE), need to check all pages
        // go well without customization. It should be fine since the default value of secondary bg
        // color is set as transparent.
        overrideTheme = R.style.SucPartnerCustomizationButton_Primary;
      }
    }
    return overrideTheme;
  }

  @VisibleForTesting
  public LinearLayout getButtonContainer() {
    return buttonContainer;
  }

  /** Returns the {@link FooterButton} of secondary button. */
  public FooterButton getSecondaryButton() {
    return secondaryButton;
  }

  /**
   * Sets whether the footer bar should be removed when there are no footer buttons in the bar.
   *
   * @param value True if footer bar is gone, false otherwise.
   */
  public void setRemoveFooterBarWhenEmpty(boolean value) {
    removeFooterBarWhenEmpty = value;
    autoSetButtonBarVisibility();
  }

  /**
   * Checks the visibility state of footer buttons to set the visibility state of this footer bar
   * automatically.
   */
  private void autoSetButtonBarVisibility() {
    Button primaryButton = getPrimaryButtonView();
    Button secondaryButton = getSecondaryButtonView();
    boolean primaryVisible = primaryButton != null && primaryButton.getVisibility() == View.VISIBLE;
    boolean secondaryVisible =
        secondaryButton != null && secondaryButton.getVisibility() == View.VISIBLE;

    if (buttonContainer != null) {
      buttonContainer.setVisibility(
          primaryVisible || secondaryVisible
              ? View.VISIBLE
              : removeFooterBarWhenEmpty ? View.GONE : View.INVISIBLE);
    }
  }

  /** Returns the visibility status for this footer bar. */
  @VisibleForTesting
  public int getVisibility() {
    return buttonContainer.getVisibility();
  }

  @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
  public Button getSecondaryButtonView() {
    return buttonContainer == null ? null : buttonContainer.findViewById(secondaryButtonId);
  }

  @VisibleForTesting
  boolean isSecondaryButtonVisible() {
    return getSecondaryButtonView() != null
        && getSecondaryButtonView().getVisibility() == View.VISIBLE;
  }

  private static int generateViewId() {
    for (; ; ) {
      final int result = nextGeneratedId.get();
      // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
      int newValue = result + 1;
      if (newValue > 0x00FFFFFF) {
        newValue = 1; // Roll over to 1, not 0.
      }
      if (nextGeneratedId.compareAndSet(result, newValue)) {
        return result;
      }
    }
  }

  private FooterActionButton inflateButton(
      FooterButton footerButton, FooterButtonPartnerConfig footerButtonPartnerConfig) {
    FooterActionButton button =
        createThemedButton(context, footerButtonPartnerConfig.getPartnerTheme());
    if (Build.VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN_MR1) {
      button.setId(View.generateViewId());
    } else {
      button.setId(generateViewId());
    }

    // apply initial configuration into button view.
    button.setText(footerButton.getText());
    button.setOnClickListener(footerButton);
    button.setVisibility(footerButton.getVisibility());
    button.setEnabled(footerButton.isEnabled());
    button.setFooterButton(footerButton);

    footerButton.setOnButtonEventListener(createButtonEventListener(button.getId()));
    return button;
  }

  // TODO: Make sure customize attributes in theme can be applied during setup flow.
  @TargetApi(VERSION_CODES.Q)
  private void onFooterButtonApplyPartnerResource(
      Button button, FooterButtonPartnerConfig footerButtonPartnerConfig) {
    if (!applyPartnerResources) {
      return;
    }
    updateButtonTextColorWithPartnerConfig(
        button, footerButtonPartnerConfig.getButtonTextColorConfig());
    updateButtonTextSizeWithPartnerConfig(
        button, footerButtonPartnerConfig.getButtonTextSizeConfig());
    updateButtonTypeFaceWithPartnerConfig(
        button, footerButtonPartnerConfig.getButtonTextTypeFaceConfig());
    updateButtonBackgroundWithPartnerConfig(
        button,
        footerButtonPartnerConfig.getButtonBackgroundConfig(),
        footerButtonPartnerConfig.getButtonDisableAlphaConfig(),
        footerButtonPartnerConfig.getButtonDisableBackgroundConfig());
    updateButtonRadiusWithPartnerConfig(button, footerButtonPartnerConfig.getButtonRadiusConfig());
    updateButtonIconWithPartnerConfig(button, footerButtonPartnerConfig.getButtonIconConfig());
    updateButtonRippleColorWithPartnerConfig(button, footerButtonPartnerConfig);
  }

  private void updateButtonTextColorWithPartnerConfig(
      Button button, PartnerConfig buttonTextColorConfig) {
    if (button.isEnabled()) {
      @ColorInt
      int color = PartnerConfigHelper.get(context).getColor(context, buttonTextColorConfig);
      if (color != Color.TRANSPARENT) {
        button.setTextColor(ColorStateList.valueOf(color));
      }
    } else {
      // disable state will use the default disable state color
      button.setTextColor(
          button.getId() == primaryButtonId ? primaryDefaultTextColor : secondaryDefaultTextColor);
    }
  }

  private void updateButtonTextSizeWithPartnerConfig(
      Button button, PartnerConfig buttonTextSizeConfig) {
    float size = PartnerConfigHelper.get(context).getDimension(context, buttonTextSizeConfig);
    if (size > 0) {
      button.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
    }
  }

  private void updateButtonTypeFaceWithPartnerConfig(
      Button button, PartnerConfig buttonTextTypeFaceConfig) {
    String fontFamilyName =
        PartnerConfigHelper.get(context).getString(context, buttonTextTypeFaceConfig);
    Typeface font = Typeface.create(fontFamilyName, Typeface.NORMAL);
    if (font != null) {
      button.setTypeface(font);
    }
  }

  @TargetApi(VERSION_CODES.Q)
  private void updateButtonBackgroundWithPartnerConfig(
      Button button,
      PartnerConfig buttonBackgroundConfig,
      PartnerConfig buttonDisableAlphaConfig,
      PartnerConfig buttonDisableBackgroundConfig) {
    Preconditions.checkArgument(
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q,
        "Update button background only support on sdk Q or higher");
    @ColorInt int color;
    @ColorInt int disabledColor;
    float disabledAlpha;
    int[] DISABLED_STATE_SET = {-android.R.attr.state_enabled};
    int[] ENABLED_STATE_SET = {};
    color = PartnerConfigHelper.get(context).getColor(context, buttonBackgroundConfig);
    disabledAlpha =
        PartnerConfigHelper.get(context).getFraction(context, buttonDisableAlphaConfig, 0f);
    disabledColor =
        PartnerConfigHelper.get(context).getColor(context, buttonDisableBackgroundConfig);

    if (color != Color.TRANSPARENT) {
      if (disabledAlpha <= 0f) {
        // if no partner resource, fallback to theme disable alpha
        float alpha;
        TypedArray a = context.obtainStyledAttributes(new int[] {android.R.attr.disabledAlpha});
        alpha = a.getFloat(0, DEFAULT_DISABLED_ALPHA);
        a.recycle();
        disabledAlpha = alpha;
      }
      if (disabledColor == Color.TRANSPARENT) {
        // if no partner resource, fallback to button background color
        disabledColor = color;
      }

      // Set text color for ripple.
      ColorStateList colorStateList =
          new ColorStateList(
              new int[][] {DISABLED_STATE_SET, ENABLED_STATE_SET},
              new int[] {convertRgbToArgb(disabledColor, disabledAlpha), color});

      // b/129482013: When a LayerDrawable is mutated, a new clone of its children drawables are
      // created, but without copying the state from the parent drawable. So even though the
      // parent is getting the correct drawable state from the view, the children won't get those
      // states until a state change happens.
      // As a workaround, we mutate the drawable and forcibly set the state to empty, and then
      // refresh the state so the children will have the updated states.
      button.getBackground().mutate().setState(new int[0]);
      button.refreshDrawableState();
      button.setBackgroundTintList(colorStateList);
    }
  }

  private void updateButtonBackground(Button button, @ColorInt int color) {
    button.getBackground().mutate().setColorFilter(color, Mode.SRC_ATOP);
  }

  private void updateButtonRadiusWithPartnerConfig(
      Button button, PartnerConfig buttonRadiusConfig) {
    if (Build.VERSION.SDK_INT >= VERSION_CODES.N) {
      float radius = PartnerConfigHelper.get(context).getDimension(context, buttonRadiusConfig);
      GradientDrawable gradientDrawable = getGradientDrawable(button);
      if (gradientDrawable != null) {
        gradientDrawable.setCornerRadius(radius);
      }
    }
  }

  private void updateButtonRippleColorWithPartnerConfig(
      Button button, FooterButtonPartnerConfig footerButtonPartnerConfig) {
    // RippleDrawable is available after sdk 21. And because on lower sdk the RippleDrawable is
    // unavailable. Since Stencil customization provider only works on Q+, there is no need to
    // perform any customization for versions 21.
    if (Build.VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
      RippleDrawable rippleDrawable = getRippleDrawable(button);
      if (rippleDrawable == null) {
        return;
      }

      int[] pressedState = {android.R.attr.state_pressed};
      @ColorInt int color;
      // Get partner text color.
      color =
          PartnerConfigHelper.get(context)
              .getColor(context, footerButtonPartnerConfig.getButtonTextColorConfig());

      float alpha =
          PartnerConfigHelper.get(context)
              .getFraction(context, footerButtonPartnerConfig.getButtonRippleColorAlphaConfig());

      // Set text color for ripple.
      ColorStateList colorStateList =
          new ColorStateList(
              new int[][] {pressedState, StateSet.NOTHING},
              new int[] {convertRgbToArgb(color, alpha), Color.TRANSPARENT});
      rippleDrawable.setColor(colorStateList);
    }
  }

  private void updateButtonIconWithPartnerConfig(Button button, PartnerConfig buttonIconConfig) {
    if (button == null) {
      return;
    }
    Drawable icon = null;
    if (buttonIconConfig != null) {
      icon = PartnerConfigHelper.get(context).getDrawable(context, buttonIconConfig);
    }
    setButtonIcon(button, icon);
  }

  private void setButtonIcon(Button button, Drawable icon) {
    if (button == null) {
      return;
    }

    if (icon != null) {
      // TODO: restrict the icons to a reasonable size
      int h = icon.getIntrinsicHeight();
      int w = icon.getIntrinsicWidth();
      icon.setBounds(0, 0, w, h);
    }

    Drawable iconStart = null;
    Drawable iconEnd = null;
    if (button.getId() == primaryButtonId) {
      iconEnd = icon;
    } else if (button.getId() == secondaryButtonId) {
      iconStart = icon;
    }
    if (Build.VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN_MR1) {
      button.setCompoundDrawablesRelative(iconStart, null, iconEnd, null);
    } else {
      button.setCompoundDrawables(iconStart, null, iconEnd, null);
    }
  }

  private static PartnerConfig getDrawablePartnerConfig(@ButtonType int buttonType) {
    PartnerConfig result;
    switch (buttonType) {
      case ButtonType.ADD_ANOTHER:
        result = PartnerConfig.CONFIG_FOOTER_BUTTON_ICON_ADD_ANOTHER;
        break;
      case ButtonType.CANCEL:
        result = PartnerConfig.CONFIG_FOOTER_BUTTON_ICON_CANCEL;
        break;
      case ButtonType.CLEAR:
        result = PartnerConfig.CONFIG_FOOTER_BUTTON_ICON_CLEAR;
        break;
      case ButtonType.DONE:
        result = PartnerConfig.CONFIG_FOOTER_BUTTON_ICON_DONE;
        break;
      case ButtonType.NEXT:
        result = PartnerConfig.CONFIG_FOOTER_BUTTON_ICON_NEXT;
        break;
      case ButtonType.OPT_IN:
        result = PartnerConfig.CONFIG_FOOTER_BUTTON_ICON_OPT_IN;
        break;
      case ButtonType.SKIP:
        result = PartnerConfig.CONFIG_FOOTER_BUTTON_ICON_SKIP;
        break;
      case ButtonType.STOP:
        result = PartnerConfig.CONFIG_FOOTER_BUTTON_ICON_STOP;
        break;
      case ButtonType.OTHER:
      default:
        result = null;
        break;
    }
    return result;
  }

  GradientDrawable getGradientDrawable(Button button) {
    // RippleDrawable is available after sdk 21, InsetDrawable#getDrawable is available after
    // sdk 19. So check the sdk is higher than sdk 21 and since Stencil customization provider only
    // works on Q+, there is no need to perform any customization for versions 21.
    if (Build.VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
      Drawable drawable = button.getBackground();
      if (drawable instanceof InsetDrawable) {
        LayerDrawable layerDrawable = (LayerDrawable) ((InsetDrawable) drawable).getDrawable();
        return (GradientDrawable) layerDrawable.getDrawable(0);
      } else if (drawable instanceof RippleDrawable) {
        InsetDrawable insetDrawable = (InsetDrawable) ((RippleDrawable) drawable).getDrawable(0);
        return (GradientDrawable) insetDrawable.getDrawable();
      }
    }
    return null;
  }

  RippleDrawable getRippleDrawable(Button button) {
    // RippleDrawable is available after sdk 21. And because on lower sdk the RippleDrawable is
    // unavailable. Since Stencil customization provider only works on Q+, there is no need to
    // perform any customization for versions 21.
    if (Build.VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
      Drawable drawable = button.getBackground();
      if (drawable instanceof InsetDrawable) {
        return (RippleDrawable) ((InsetDrawable) drawable).getDrawable();
      } else if (drawable instanceof RippleDrawable) {
        return (RippleDrawable) drawable;
      }
    }
    return null;
  }

  @ColorInt
  private static int convertRgbToArgb(@ColorInt int color, float alpha) {
    return Color.argb((int) (alpha * 255), Color.red(color), Color.green(color), Color.blue(color));
  }

  protected View inflateFooter(@LayoutRes int footer) {
    if (Build.VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN) {
      LayoutInflater inflater =
          LayoutInflater.from(
              new ContextThemeWrapper(context, R.style.SucPartnerCustomizationButtonBar_Stackable));
      footerStub.setLayoutInflater(inflater);
    }
    footerStub.setLayoutResource(footer);
    return footerStub.inflate();
  }

  private void updateFooterBarPadding(
      LinearLayout buttonContainer, int left, int top, int right, int bottom) {
    if (buttonContainer == null) {
      // Ignore action since buttonContainer is null
      return;
    }
    buttonContainer.setPadding(left, top, right, bottom);
  }

  /** Returns the paddingTop of footer bar. */
  @VisibleForTesting
  int getPaddingTop() {
    return (buttonContainer != null) ? buttonContainer.getPaddingTop() : footerStub.getPaddingTop();
  }

  /** Returns the paddingBottom of footer bar. */
  @VisibleForTesting
  int getPaddingBottom() {
    return (buttonContainer != null)
        ? buttonContainer.getPaddingBottom()
        : footerStub.getPaddingBottom();
  }

  /** Uses for notify mixin the view already attached to window. */
  public void onAttachedToWindow() {
    metrics.logPrimaryButtonInitialStateVisibility(
        /* isVisible= */ isPrimaryButtonVisible(), /* isUsingXml= */ false);
    metrics.logSecondaryButtonInitialStateVisibility(
        /* isVisible= */ isSecondaryButtonVisible(), /* isUsingXml= */ false);
  }

  /** Uses for notify mixin the view already detached from window. */
  public void onDetachedFromWindow() {
    metrics.updateButtonVisibility(isPrimaryButtonVisible(), isSecondaryButtonVisible());
  }

  /**
   * Assigns logging metrics to bundle for PartnerCustomizationLayout to log metrics to SetupWizard.
   */
  @TargetApi(VERSION_CODES.Q)
  public PersistableBundle getLoggingMetrics() {
    return metrics.getMetrics();
  }
}
