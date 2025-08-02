package com.jakir.presetscolorpicker.builder;

import androidx.appcompat.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.InputFilter;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jakir.presetscolorpicker.PresetsColorPickerView;
import com.jakir.presetscolorpicker.OnPresetsColorChangedListener;
import com.jakir.presetscolorpicker.OnPresetsColorSelectedListener;
import com.jakir.presetscolorpicker.R;
import com.jakir.presetscolorpicker.Utils;
import com.jakir.presetscolorpicker.renderer.ColorWheelRenderer;
import com.jakir.presetscolorpicker.slider.AlphaSlider;
import com.jakir.presetscolorpicker.slider.LightnessSlider;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class PresetsColorPickerDialogBuilder {
	private MaterialAlertDialogBuilder builder;
	private LinearLayout pickerContainer;
	private PresetsColorPickerView presetsColorPickerView;
	private LightnessSlider lightnessSlider;
	private AlphaSlider alphaSlider;
	private EditText colorEdit;
	private LinearLayout colorPreview;

	private boolean isLightnessSliderEnabled = true;
	private boolean isAlphaSliderEnabled = true;
	private boolean isBorderEnabled = true;
	private boolean isColorEditEnabled = false;
	private boolean isPreviewEnabled = false;
	private int pickerCount = 1;
	private int defaultMargin = 0;
	private int defaultMarginTop = 0;
	private Integer[] initialColor = new Integer[]{null, null, null, null, null};

	private PresetsColorPickerDialogBuilder(Context context) {
		this(context, 0);
	}

	private PresetsColorPickerDialogBuilder(Context context, int theme) {
		defaultMargin = getDimensionAsPx(context, R.dimen.default_slider_margin);
		defaultMarginTop = getDimensionAsPx(context, R.dimen.default_margin_top);

		builder = new MaterialAlertDialogBuilder(context, theme);
		pickerContainer = new LinearLayout(context);
		pickerContainer.setOrientation(LinearLayout.VERTICAL);
		pickerContainer.setGravity(Gravity.CENTER_HORIZONTAL);
		pickerContainer.setPadding(defaultMargin, defaultMarginTop, defaultMargin, 0);

		LinearLayout.LayoutParams layoutParamsForColorPickerView = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
		layoutParamsForColorPickerView.weight = 1;
		presetsColorPickerView = new PresetsColorPickerView(context);

		pickerContainer.addView(presetsColorPickerView, layoutParamsForColorPickerView);

		builder.setView(pickerContainer);
	}

	public static PresetsColorPickerDialogBuilder with(Context context) {
		return new PresetsColorPickerDialogBuilder(context);
	}

	public static PresetsColorPickerDialogBuilder with(Context context, int theme) {
		return new PresetsColorPickerDialogBuilder(context, theme);
	}

	public PresetsColorPickerDialogBuilder setTitle(String title) {
		builder.setTitle(title);
		return this;
	}

	public PresetsColorPickerDialogBuilder setTitle(int titleId) {
		builder.setTitle(titleId);
		return this;
	}

	public PresetsColorPickerDialogBuilder defaultColor(int initialColor) {
		this.initialColor[0] = initialColor;
		return this;
	}

	public PresetsColorPickerDialogBuilder initialColors(int[] initialColor) {
		for (int i = 0; i < initialColor.length && i < this.initialColor.length; i++) {
			this.initialColor[i] = initialColor[i];
		}
		return this;
	}

	public PresetsColorPickerDialogBuilder wheelType(PresetsColorPickerView.WHEEL_TYPE wheelType) {
		ColorWheelRenderer renderer = ColorWheelRendererBuilder.getRenderer(wheelType);
		presetsColorPickerView.setRenderer(renderer);
		return this;
	}

	public PresetsColorPickerDialogBuilder density(int density) {
		presetsColorPickerView.setDensity(density);
		return this;
	}

	public PresetsColorPickerDialogBuilder setOnColorChangedListener(OnPresetsColorChangedListener onPresetsColorChangedListener) {
		presetsColorPickerView.addOnColorChangedListener(onPresetsColorChangedListener);
		return this;
	}

	public PresetsColorPickerDialogBuilder setOnPresetsColorSelectedListener(OnPresetsColorSelectedListener onPresetsColorSelectedListener) {
		presetsColorPickerView.addOnColorSelectedListener(onPresetsColorSelectedListener);
		return this;
	}

	public PresetsColorPickerDialogBuilder setPositiveButton(CharSequence text, final PresetsColorPickerClickListener onClickListener) {
		builder.setPositiveButton(text, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				positiveButtonOnClick(dialog, onClickListener);
			}
		});
		return this;
	}

	public PresetsColorPickerDialogBuilder setPositiveButton(int textId, final PresetsColorPickerClickListener onClickListener) {
		builder.setPositiveButton(textId, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				positiveButtonOnClick(dialog, onClickListener);
			}
		});
		return this;
	}

	public PresetsColorPickerDialogBuilder setNegativeButton(CharSequence text, DialogInterface.OnClickListener onClickListener) {
		builder.setNegativeButton(text, onClickListener);
		return this;
	}

	public PresetsColorPickerDialogBuilder setNegativeButton(int textId, DialogInterface.OnClickListener onClickListener) {
		builder.setNegativeButton(textId, onClickListener);
		return this;
	}
	public PresetsColorPickerDialogBuilder setNeutralButton(CharSequence text, DialogInterface.OnClickListener onClickListener) {
		builder.setNeutralButton(text, onClickListener);
		return this;
	}

	public PresetsColorPickerDialogBuilder setNeutralButton(int textId, DialogInterface.OnClickListener onClickListener) {
		builder.setNeutralButton(textId, onClickListener);
		return this;
	}
	public PresetsColorPickerDialogBuilder noSliders() {
		isLightnessSliderEnabled = false;
		isAlphaSliderEnabled = false;
		return this;
	}

	public PresetsColorPickerDialogBuilder alphaSliderOnly() {
		isLightnessSliderEnabled = false;
		isAlphaSliderEnabled = true;
		return this;
	}

	public PresetsColorPickerDialogBuilder lightnessSliderOnly() {
		isLightnessSliderEnabled = true;
		isAlphaSliderEnabled = false;
		return this;
	}

	public PresetsColorPickerDialogBuilder showAlphaSlider(boolean showAlpha) {
		isAlphaSliderEnabled = showAlpha;
		return this;
	}

	public PresetsColorPickerDialogBuilder showLightnessSlider(boolean showLightness) {
		isLightnessSliderEnabled = showLightness;
		return this;
	}

	public PresetsColorPickerDialogBuilder showBorder(boolean showBorder) {
		isBorderEnabled = showBorder;
		return this;
	}

	public PresetsColorPickerDialogBuilder showColorEdit(boolean showEdit) {
		isColorEditEnabled = showEdit;
		return this;
	}

	public PresetsColorPickerDialogBuilder setColorEditTextColor(int argb) {
		presetsColorPickerView.setColorEditTextColor(argb);
		return this;
	}

	public PresetsColorPickerDialogBuilder showColorPreview(boolean showPreview) {
		isPreviewEnabled = showPreview;
		if (!showPreview)
			pickerCount = 1;
		return this;
	}

	public PresetsColorPickerDialogBuilder setPickerCount(int pickerCount) throws IndexOutOfBoundsException {
		if (pickerCount < 1 || pickerCount > 5)
			throw new IndexOutOfBoundsException("Picker Can Only Support 1-5 Colors");
		this.pickerCount = pickerCount;
		if (this.pickerCount > 1)
			this.isPreviewEnabled = true;
		return this;
	}

	public AlertDialog build() {
		Context context = builder.getContext();
		presetsColorPickerView.setInitialColors(initialColor, getStartOffset(initialColor));
		presetsColorPickerView.setShowBorder(isBorderEnabled);

		if (isLightnessSliderEnabled) {
			LinearLayout.LayoutParams layoutParamsForLightnessBar = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getDimensionAsPx(context, R.dimen.default_slider_height));
			lightnessSlider = new LightnessSlider(context);
			lightnessSlider.setLayoutParams(layoutParamsForLightnessBar);
			pickerContainer.addView(lightnessSlider);
			presetsColorPickerView.setLightnessSlider(lightnessSlider);
			lightnessSlider.setColor(getStartColor(initialColor));
			lightnessSlider.setShowBorder(isBorderEnabled);
		}
		if (isAlphaSliderEnabled) {
			LinearLayout.LayoutParams layoutParamsForAlphaBar = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getDimensionAsPx(context, R.dimen.default_slider_height));
			alphaSlider = new AlphaSlider(context);
			alphaSlider.setLayoutParams(layoutParamsForAlphaBar);
			pickerContainer.addView(alphaSlider);
			presetsColorPickerView.setAlphaSlider(alphaSlider);
			alphaSlider.setColor(getStartColor(initialColor));
			alphaSlider.setShowBorder(isBorderEnabled);
		}
		if (isColorEditEnabled) {
			LinearLayout.LayoutParams layoutParamsForColorEdit = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			colorEdit = (EditText) View.inflate(context, R.layout.color_edit, null);
			colorEdit.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
			colorEdit.setSingleLine();
			colorEdit.setVisibility(View.GONE);

			// limit number of characters to hexColors
			int maxLength = isAlphaSliderEnabled ? 9 : 7;
			colorEdit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});

			pickerContainer.addView(colorEdit, layoutParamsForColorEdit);

			colorEdit.setText(Utils.getHexString(getStartColor(initialColor), isAlphaSliderEnabled));
			presetsColorPickerView.setColorEdit(colorEdit);
		}
		if (isPreviewEnabled) {
			colorPreview = (LinearLayout) View.inflate(context, R.layout.color_preview, null);
			colorPreview.setVisibility(View.GONE);
			pickerContainer.addView(colorPreview);

			if (initialColor.length == 0) {
				ImageView colorImage = (ImageView) View.inflate(context, R.layout.color_selector, null);
				colorImage.setImageDrawable(new ColorDrawable(Color.WHITE));
			} else {
				for (int i = 0; i < initialColor.length && i < this.pickerCount; i++) {
					if (initialColor[i] == null)
						break;
					LinearLayout colorLayout = (LinearLayout) View.inflate(context, R.layout.color_selector, null);
					ImageView colorImage = (ImageView) colorLayout.findViewById(R.id.image_preview);
					colorImage.setImageDrawable(new ColorDrawable(initialColor[i]));
					colorPreview.addView(colorLayout);
				}
			}
			colorPreview.setVisibility(View.VISIBLE);
			presetsColorPickerView.setColorPreview(colorPreview, getStartOffset(initialColor));
		}

		return builder.create();
	}

	private Integer getStartOffset(Integer[] colors) {
		Integer start = 0;
		for (int i = 0; i < colors.length; i++) {
			if (colors[i] == null) {
				return start;
			}
			start = (i + 1) / 2;
		}
		return start;
	}

	private int getStartColor(Integer[] colors) {
		Integer startColor = getStartOffset(colors);
		return startColor == null ? Color.WHITE : colors[startColor];
	}

	private static int getDimensionAsPx(Context context, int rid) {
		return (int) (context.getResources().getDimension(rid) + .5f);
	}

	private void positiveButtonOnClick(DialogInterface dialog, PresetsColorPickerClickListener onClickListener) {
		int selectedColor = presetsColorPickerView.getSelectedColor();
		Integer[] allColors = presetsColorPickerView.getAllColors();
		onClickListener.onClick(dialog, selectedColor, allColors);
	}

}