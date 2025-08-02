package com.jakir.presetscolorpicker;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.preference.Preference;
import androidx.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.jakir.presetscolorpicker.builder.PresetsColorPickerClickListener;
import com.jakir.presetscolorpicker.builder.PresetsColorPickerDialogBuilder;

public class PresetsColorPickerPreference extends Preference {

	protected boolean alphaSlider;
	protected boolean lightSlider;
	protected boolean border;

	protected int selectedColor = 0;

	protected PresetsColorPickerView.WHEEL_TYPE wheelType;
	protected int density;

	private boolean pickerColorEdit;
	private String pickerTitle;
	private String pickerButtonCancel;
	private String pickerButtonOk;

	protected ImageView colorIndicator;

	public PresetsColorPickerPreference(Context context) {
		super(context);
	}

	public PresetsColorPickerPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		initWith(context, attrs);
	}

	public PresetsColorPickerPreference(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initWith(context, attrs);
	}

	private void initWith(Context context, AttributeSet attrs) {
		final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ColorPickerPreference);

		try {
			alphaSlider = typedArray.getBoolean(R.styleable.ColorPickerPreference_alphaSlider, false);
			lightSlider = typedArray.getBoolean(R.styleable.ColorPickerPreference_lightnessSlider, false);
			border = typedArray.getBoolean(R.styleable.ColorPickerPreference_border, true);

			density = typedArray.getInt(R.styleable.ColorPickerPreference_density, 8);
			wheelType = PresetsColorPickerView.WHEEL_TYPE.indexOf(typedArray.getInt(R.styleable.ColorPickerPreference_wheelType, 0));

			selectedColor = typedArray.getInt(R.styleable.ColorPickerPreference_initialColor, 0xffffffff);

			pickerColorEdit = typedArray.getBoolean(R.styleable.ColorPickerPreference_pickerColorEdit, true);
			pickerTitle = typedArray.getString(R.styleable.ColorPickerPreference_pickerTitle);
			if (pickerTitle==null)
				pickerTitle = "Choose color";

			pickerButtonCancel = typedArray.getString(R.styleable.ColorPickerPreference_pickerButtonCancel);
			if (pickerButtonCancel==null)
				pickerButtonCancel = "cancel";

			pickerButtonOk = typedArray.getString(R.styleable.ColorPickerPreference_pickerButtonOk);
			if (pickerButtonOk==null)
				pickerButtonOk = "ok";

		} finally {
			typedArray.recycle();
		}

		setWidgetLayoutResource(R.layout.color_widget);
	}


	@Override
	protected void onBindView(@NonNull View view) {
		super.onBindView(view);

		int tmpColor = isEnabled()
				? selectedColor
				: darken(selectedColor, .5f);

		colorIndicator = (ImageView) view.findViewById(R.id.color_indicator);

		PresetsColorCircleDrawable colorChoiceDrawable = null;
		Drawable currentDrawable = colorIndicator.getDrawable();
		if (currentDrawable != null && currentDrawable instanceof PresetsColorCircleDrawable)
			colorChoiceDrawable = (PresetsColorCircleDrawable) currentDrawable;

		if (colorChoiceDrawable == null)
			colorChoiceDrawable = new PresetsColorCircleDrawable(tmpColor);

		colorIndicator.setImageDrawable(colorChoiceDrawable);
	}

	public void setValue(int value) {
		if (callChangeListener(value)) {
			selectedColor = value;
			persistInt(value);
			notifyChanged();
		}
	}

	@Override
	protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
		setValue(restoreValue ? getPersistedInt(0) : (Integer) defaultValue);
	}

	@Override
	protected void onClick() {
		PresetsColorPickerDialogBuilder builder = PresetsColorPickerDialogBuilder
			.with(getContext())
			.setTitle(pickerTitle)
			.defaultColor(selectedColor)
			.showBorder(border)
			.wheelType(wheelType)
			.density(density)
			.showColorEdit(pickerColorEdit)
			.setPositiveButton(pickerButtonOk, new PresetsColorPickerClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int selectedColorFromPicker, Integer[] allColors) {
					setValue(selectedColorFromPicker);
				}
			})
			.setNegativeButton(pickerButtonCancel, null);

		if (!alphaSlider && !lightSlider) builder.noSliders();
		else if (!alphaSlider) builder.lightnessSliderOnly();
		else if (!lightSlider) builder.alphaSliderOnly();

		builder
			.build()
			.show();
	}

	public static int darken(int color, float factor) {
		int a = Color.alpha(color);
		int r = Color.red(color);
		int g = Color.green(color);
		int b = Color.blue(color);

		return Color.argb(a,
			Math.max((int)(r * factor), 0),
			Math.max((int)(g * factor), 0),
			Math.max((int)(b * factor), 0));
	}
}
