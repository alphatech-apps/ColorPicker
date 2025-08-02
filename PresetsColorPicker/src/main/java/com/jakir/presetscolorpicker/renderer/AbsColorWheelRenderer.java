package com.jakir.presetscolorpicker.renderer;

import com.jakir.presetscolorpicker.PresetsColorCircle;

import java.util.ArrayList;
import java.util.List;

public abstract class AbsColorWheelRenderer implements ColorWheelRenderer {
	protected ColorWheelRenderOption colorWheelRenderOption;
	protected List<PresetsColorCircle> presetsColorCircleList = new ArrayList<>();

	public void initWith(ColorWheelRenderOption colorWheelRenderOption) {
		this.colorWheelRenderOption = colorWheelRenderOption;
		this.presetsColorCircleList.clear();
	}

	@Override
	public ColorWheelRenderOption getRenderOption() {
		if (colorWheelRenderOption == null) colorWheelRenderOption = new ColorWheelRenderOption();
		return colorWheelRenderOption;
	}

	public List<PresetsColorCircle> getColorCircleList() {
		return presetsColorCircleList;
	}

	protected int getAlphaValueAsInt() {
		return Math.round(colorWheelRenderOption.alpha * 255);
	}

	protected int calcTotalCount(float radius, float size) {
		return Math.max(1, (int) ((1f - GAP_PERCENTAGE) * Math.PI / (Math.asin(size / radius)) + 0.5f));
	}
}