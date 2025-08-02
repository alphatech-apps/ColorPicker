package com.jakir.presetscolorpicker;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jakir.presetscolorpicker.builder.ColorWheelRendererBuilder;
import com.jakir.presetscolorpicker.builder.PaintBuilder;
import com.jakir.presetscolorpicker.renderer.ColorWheelRenderOption;
import com.jakir.presetscolorpicker.renderer.ColorWheelRenderer;
import com.jakir.presetscolorpicker.slider.AlphaSlider;
import com.jakir.presetscolorpicker.slider.LightnessSlider;

import java.util.ArrayList;

public class PresetsColorPickerView extends View {
	private static final float STROKE_RATIO = 1.5f;

	private Bitmap colorWheel;
	private Canvas colorWheelCanvas;
	private Bitmap currentColor;
	private Canvas currentColorCanvas;
	private boolean showBorder;
	private int density = 8;

	private float lightness = 1;
	private float alpha = 1;
	private int backgroundColor = 0x00000000;

	private Integer initialColors[] = new Integer[]{null, null, null, null, null};
	private int colorSelection = 0;
	private Integer initialColor;
	private Integer pickerColorEditTextColor;
	private Paint colorWheelFill = PaintBuilder.newPaint().color(0).build();
	private Paint selectorStroke = PaintBuilder.newPaint().color(0).build();
	private Paint alphaPatternPaint = PaintBuilder.newPaint().build();
	private PresetsColorCircle currentPresetsColorCircle;

	private ArrayList<OnPresetsColorChangedListener> colorChangedListeners = new ArrayList<>();
	private ArrayList<OnPresetsColorSelectedListener> listeners = new ArrayList<>();

	private LightnessSlider lightnessSlider;
	private AlphaSlider alphaSlider;
	private EditText colorEdit;
	private TextWatcher colorTextChange = new TextWatcher() {
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			try {
				int color = Color.parseColor(s.toString());

				// set the color without changing the edit text preventing stack overflow
				setColor(color, false);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public void afterTextChanged(Editable s) {
		}
	};
	private LinearLayout colorPreview;

	private ColorWheelRenderer renderer;

	private int alphaSliderViewId, lightnessSliderViewId;

	public PresetsColorPickerView(Context context) {
		super(context);
		initWith(context, null);
	}

	public PresetsColorPickerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initWith(context, attrs);
	}

	public PresetsColorPickerView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initWith(context, attrs);
	}

	@TargetApi(21)
	public PresetsColorPickerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		initWith(context, attrs);
	}

	private void initWith(Context context, AttributeSet attrs) {
		final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ColorPickerPreference);

		density = typedArray.getInt(R.styleable.ColorPickerPreference_density, 10);
		initialColor = typedArray.getInt(R.styleable.ColorPickerPreference_initialColor, 0xffffffff);

		pickerColorEditTextColor = typedArray.getInt(R.styleable.ColorPickerPreference_pickerColorEditTextColor, 0xffffffff);

		WHEEL_TYPE wheelType = WHEEL_TYPE.indexOf(typedArray.getInt(R.styleable.ColorPickerPreference_wheelType, 0));
		ColorWheelRenderer renderer = ColorWheelRendererBuilder.getRenderer(wheelType);

		alphaSliderViewId = typedArray.getResourceId(R.styleable.ColorPickerPreference_alphaSliderView, 0);
		lightnessSliderViewId = typedArray.getResourceId(R.styleable.ColorPickerPreference_lightnessSliderView, 0);

		setRenderer(renderer);
		setDensity(density);
		setInitialColor(initialColor, true);

		typedArray.recycle();
	}

	@Override
	public void onWindowFocusChanged(boolean hasWindowFocus) {
		super.onWindowFocusChanged(hasWindowFocus);
		updateColorWheel();
		currentPresetsColorCircle = findNearestByColor(initialColor);
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);

		if (alphaSliderViewId != 0)
			setAlphaSlider((AlphaSlider) getRootView().findViewById(alphaSliderViewId));
		if (lightnessSliderViewId != 0)
			setLightnessSlider((LightnessSlider) getRootView().findViewById(lightnessSliderViewId));

		updateColorWheel();
		currentPresetsColorCircle = findNearestByColor(initialColor);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		updateColorWheel();
	}

	private void updateColorWheel() {
		int width = getMeasuredWidth();
		int height = getMeasuredHeight();

		if (height < width)
			width = height;
		if (width <= 0)
			return;
		if (colorWheel == null || colorWheel.getWidth() != width) {
			colorWheel = Bitmap.createBitmap(width, width, Bitmap.Config.ARGB_8888);
			colorWheelCanvas = new Canvas(colorWheel);
			alphaPatternPaint.setShader(PaintBuilder.createAlphaPatternShader(26));
		}
		if (currentColor == null || currentColor.getWidth() != width) {
			currentColor = Bitmap.createBitmap(width, width, Bitmap.Config.ARGB_8888);
			currentColorCanvas = new Canvas(currentColor);
		}
		drawColorWheel();
		invalidate();
	}

	private void drawColorWheel() {
		colorWheelCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
		currentColorCanvas.drawColor(0, PorterDuff.Mode.CLEAR);

		if (renderer == null) return;

		float half = colorWheelCanvas.getWidth() / 2f;
		float strokeWidth = STROKE_RATIO * (1f + ColorWheelRenderer.GAP_PERCENTAGE);
		float maxRadius = half - strokeWidth - half / density;
		float cSize = maxRadius / (density - 1) / 2;

		ColorWheelRenderOption colorWheelRenderOption = renderer.getRenderOption();
		colorWheelRenderOption.density = this.density;
		colorWheelRenderOption.maxRadius = maxRadius;
		colorWheelRenderOption.cSize = cSize;
		colorWheelRenderOption.strokeWidth = strokeWidth;
		colorWheelRenderOption.alpha = alpha;
		colorWheelRenderOption.lightness = lightness;
		colorWheelRenderOption.targetCanvas = colorWheelCanvas;

		renderer.initWith(colorWheelRenderOption);
		renderer.draw();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int width = 0;
		if (widthMode == MeasureSpec.UNSPECIFIED)
			width = widthMeasureSpec;
		else if (widthMode == MeasureSpec.AT_MOST)
			width = MeasureSpec.getSize(widthMeasureSpec);
		else if (widthMode == MeasureSpec.EXACTLY)
			width = MeasureSpec.getSize(widthMeasureSpec);

		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int height = 0;
		if (heightMode == MeasureSpec.UNSPECIFIED)
			height = heightMeasureSpec;
		else if (heightMode == MeasureSpec.AT_MOST)
			height = MeasureSpec.getSize(heightMeasureSpec);
		else if (heightMode == MeasureSpec.EXACTLY)
			height = MeasureSpec.getSize(heightMeasureSpec);
		int squareDimen = width;
		if (height < width)
			squareDimen = height;
		setMeasuredDimension(squareDimen, squareDimen);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
			case MotionEvent.ACTION_MOVE: {
				int lastSelectedColor = getSelectedColor();
				currentPresetsColorCircle = findNearestByPosition(event.getX(), event.getY());
				int selectedColor = getSelectedColor();

				callOnColorChangedListeners(lastSelectedColor, selectedColor);

				initialColor = selectedColor;
				setColorToSliders(selectedColor);
				updateColorWheel();
				invalidate();
				break;
			}
			case MotionEvent.ACTION_UP: {
				int selectedColor = getSelectedColor();
				if (listeners != null) {
					for (OnPresetsColorSelectedListener listener : listeners) {
						try {
							listener.onColorSelected(selectedColor);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				setColorToSliders(selectedColor);
				setColorText(selectedColor);
				setColorPreviewColor(selectedColor);
				invalidate();
				break;
			}
		}
		return true;
	}

	protected void callOnColorChangedListeners(int oldColor, int newColor) {
		if (colorChangedListeners != null && oldColor != newColor) {
			for (OnPresetsColorChangedListener listener : colorChangedListeners) {
				try {
					listener.onColorChanged(newColor);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawColor(backgroundColor);

		float maxRadius = canvas.getWidth() / (1f + ColorWheelRenderer.GAP_PERCENTAGE);
		float size = maxRadius / density / 2;
		if (colorWheel != null && currentPresetsColorCircle != null) {
			colorWheelFill.setColor(Color.HSVToColor(currentPresetsColorCircle.getHsvWithLightness(this.lightness)));
			colorWheelFill.setAlpha((int) (alpha * 0xff));

			// a separate canvas is used to erase an issue with the alpha pattern around the edges
			// draw circle slightly larger than it needs to be, then erase edges to proper dimensions
			currentColorCanvas.drawCircle(currentPresetsColorCircle.getX(), currentPresetsColorCircle.getY(), size + 4, alphaPatternPaint);
			currentColorCanvas.drawCircle(currentPresetsColorCircle.getX(), currentPresetsColorCircle.getY(), size + 4, colorWheelFill);

			selectorStroke = PaintBuilder.newPaint().color(0xffffffff).style(Paint.Style.STROKE).stroke(size * (STROKE_RATIO - 1)).xPerMode(PorterDuff.Mode.CLEAR).build();

			if (showBorder) colorWheelCanvas.drawCircle(currentPresetsColorCircle.getX(), currentPresetsColorCircle.getY(), size + (selectorStroke.getStrokeWidth() / 2f), selectorStroke);
			canvas.drawBitmap(colorWheel, 0, 0, null);

			currentColorCanvas.drawCircle(currentPresetsColorCircle.getX(), currentPresetsColorCircle.getY(), size + (selectorStroke.getStrokeWidth() / 2f), selectorStroke);
			canvas.drawBitmap(currentColor, 0, 0, null);
		}
	}

	private PresetsColorCircle findNearestByPosition(float x, float y) {
		PresetsColorCircle near = null;
		double minDist = Double.MAX_VALUE;

		for (PresetsColorCircle presetsColorCircle : renderer.getColorCircleList()) {
			double dist = presetsColorCircle.sqDist(x, y);
			if (minDist > dist) {
				minDist = dist;
				near = presetsColorCircle;
			}
		}

		return near;
	}

	private PresetsColorCircle findNearestByColor(int color) {
		float[] hsv = new float[3];
		Color.colorToHSV(color, hsv);
		PresetsColorCircle near = null;
		double minDiff = Double.MAX_VALUE;
		double x = hsv[1] * Math.cos(hsv[0] * Math.PI / 180);
		double y = hsv[1] * Math.sin(hsv[0] * Math.PI / 180);

		for (PresetsColorCircle presetsColorCircle : renderer.getColorCircleList()) {
			float[] hsv1 = presetsColorCircle.getHsv();
			double x1 = hsv1[1] * Math.cos(hsv1[0] * Math.PI / 180);
			double y1 = hsv1[1] * Math.sin(hsv1[0] * Math.PI / 180);
			double dx = x - x1;
			double dy = y - y1;
			double dist = dx * dx + dy * dy;
			if (dist < minDiff) {
				minDiff = dist;
				near = presetsColorCircle;
			}
		}

		return near;
	}

	public int getSelectedColor() {
		int color = 0;
		if (currentPresetsColorCircle != null)
			color = Utils.colorAtLightness(currentPresetsColorCircle.getColor(), this.lightness);
		return Utils.adjustAlpha(this.alpha, color);
	}

	public Integer[] getAllColors() {
		return initialColors;
	}

	public void setInitialColors(Integer[] colors, int selectedColor) {
		this.initialColors = colors;
		this.colorSelection = selectedColor;
		Integer initialColor = this.initialColors[this.colorSelection];
		if (initialColor == null) initialColor = 0xffffffff;
		setInitialColor(initialColor, true);
	}

	public void setInitialColor(int color, boolean updateText) {
		float[] hsv = new float[3];
		Color.colorToHSV(color, hsv);

		this.alpha = Utils.getAlphaPercent(color);
		this.lightness = hsv[2];
		this.initialColors[this.colorSelection] = color;
		this.initialColor = color;
		setColorPreviewColor(color);
		setColorToSliders(color);
		if (this.colorEdit != null && updateText)
			setColorText(color);
		currentPresetsColorCircle = findNearestByColor(color);
	}

	public void setLightness(float lightness) {
		int lastSelectedColor = getSelectedColor();

		this.lightness = lightness;
		if (currentPresetsColorCircle != null) {
            this.initialColor = Color.HSVToColor(Utils.alphaValueAsInt(this.alpha), currentPresetsColorCircle.getHsvWithLightness(lightness));
            if (this.colorEdit != null)
                this.colorEdit.setText(Utils.getHexString(this.initialColor, this.alphaSlider != null));
            if (this.alphaSlider != null && this.initialColor != null)
                this.alphaSlider.setColor(this.initialColor);

            callOnColorChangedListeners(lastSelectedColor, this.initialColor);

            updateColorWheel();
            invalidate();
        }
	}

	public void setColor(int color, boolean updateText) {
		setInitialColor(color, updateText);
		updateColorWheel();
		invalidate();
	}

	public void setAlphaValue(float alpha) {
		int lastSelectedColor = getSelectedColor();

		this.alpha = alpha;
		this.initialColor = Color.HSVToColor(Utils.alphaValueAsInt(this.alpha), currentPresetsColorCircle.getHsvWithLightness(this.lightness));
		if (this.colorEdit != null)
			this.colorEdit.setText(Utils.getHexString(this.initialColor, this.alphaSlider != null));
		if (this.lightnessSlider != null && this.initialColor != null)
			this.lightnessSlider.setColor(this.initialColor);

		callOnColorChangedListeners(lastSelectedColor, this.initialColor);

		updateColorWheel();
		invalidate();
	}

	public void addOnColorChangedListener(OnPresetsColorChangedListener listener) {
		this.colorChangedListeners.add(listener);
	}

	public void addOnColorSelectedListener(OnPresetsColorSelectedListener listener) {
		this.listeners.add(listener);
	}

	public void setLightnessSlider(LightnessSlider lightnessSlider) {
		this.lightnessSlider = lightnessSlider;
		if (lightnessSlider != null) {
			this.lightnessSlider.setColorPicker(this);
			this.lightnessSlider.setColor(getSelectedColor());
		}
	}

	public void setAlphaSlider(AlphaSlider alphaSlider) {
		this.alphaSlider = alphaSlider;
		if (alphaSlider != null) {
			this.alphaSlider.setColorPicker(this);
			this.alphaSlider.setColor(getSelectedColor());
		}
	}

	public void setColorEdit(EditText colorEdit) {
		this.colorEdit = colorEdit;
		if (this.colorEdit != null) {
			this.colorEdit.setVisibility(View.VISIBLE);
			this.colorEdit.addTextChangedListener(colorTextChange);
			setColorEditTextColor(pickerColorEditTextColor);
		}
	}

	public void setColorEditTextColor(int argb) {
		this.pickerColorEditTextColor = argb;
		if (colorEdit != null)
			colorEdit.setTextColor(argb);
	}

	public void setDensity(int density) {
		this.density = Math.max(2, density);
		invalidate();
	}

	public void setRenderer(ColorWheelRenderer renderer) {
		this.renderer = renderer;
		invalidate();
	}

	public void setColorPreview(LinearLayout colorPreview, Integer selectedColor) {
		if (colorPreview == null)
			return;
		this.colorPreview = colorPreview;
		if (selectedColor == null)
			selectedColor = 0;
		int children = colorPreview.getChildCount();
		if (children == 0 || colorPreview.getVisibility() != View.VISIBLE)
			return;

		for (int i = 0; i < children; i++) {
			View childView = colorPreview.getChildAt(i);
			if (!(childView instanceof LinearLayout))
				continue;
			LinearLayout childLayout = (LinearLayout) childView;
			if (i == selectedColor) {
				childLayout.setBackgroundColor(Color.WHITE);
			}
			ImageView childImage = (ImageView) childLayout.findViewById(R.id.image_preview);
			childImage.setClickable(true);
			childImage.setTag(i);
			childImage.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (v == null)
						return;
					Object tag = v.getTag();
					if (tag == null || !(tag instanceof Integer))
						return;
					setSelectedColor((int) tag);
				}
			});
		}
	}

	public void setSelectedColor(int previewNumber) {
		if (initialColors == null || initialColors.length < previewNumber)
			return;
		this.colorSelection = previewNumber;
		setHighlightedColor(previewNumber);
		Integer color = initialColors[previewNumber];
		if (color == null)
			return;
		setColor(color, true);
	}

	public void setShowBorder(boolean showBorder) {
		this.showBorder = showBorder;
	}

	private void setHighlightedColor(int previewNumber) {
		int children = colorPreview.getChildCount();
		if (children == 0 || colorPreview.getVisibility() != View.VISIBLE)
			return;

		for (int i = 0; i < children; i++) {
			View childView = colorPreview.getChildAt(i);
			if (!(childView instanceof LinearLayout))
				continue;
			LinearLayout childLayout = (LinearLayout) childView;
			if (i == previewNumber) {
				childLayout.setBackgroundColor(Color.WHITE);
			} else {
				childLayout.setBackgroundColor(Color.TRANSPARENT);
			}
		}
	}

	private void setColorPreviewColor(int newColor) {
		if (colorPreview == null || initialColors == null || colorSelection > initialColors.length || initialColors[colorSelection] == null)
			return;

		int children = colorPreview.getChildCount();
		if (children == 0 || colorPreview.getVisibility() != View.VISIBLE)
			return;

		View childView = colorPreview.getChildAt(colorSelection);
		if (!(childView instanceof LinearLayout))
			return;
		LinearLayout childLayout = (LinearLayout) childView;
		ImageView childImage = (ImageView) childLayout.findViewById(R.id.image_preview);
		childImage.setImageDrawable(new PresetsColorCircleDrawable(newColor));
	}

	private void setColorText(int argb) {
		if (colorEdit == null)
			return;
		colorEdit.setText(Utils.getHexString(argb, this.alphaSlider != null));
	}

	private void setColorToSliders(int selectedColor) {
		if (lightnessSlider != null)
			lightnessSlider.setColor(selectedColor);
		if (alphaSlider != null)
			alphaSlider.setColor(selectedColor);
	}

	public enum WHEEL_TYPE {
		FLOWER, CIRCLE;

		public static WHEEL_TYPE indexOf(int index) {
			switch (index) {
				case 0:
					return FLOWER;
				case 1:
					return CIRCLE;
			}
			return FLOWER;
		}
	}
}
