package com.jakir.presetscolorpicker.renderer;

import com.jakir.presetscolorpicker.PresetsColorCircle;

import java.util.List;

public interface ColorWheelRenderer {
	float GAP_PERCENTAGE = 0.025f;

	void draw();

	ColorWheelRenderOption getRenderOption();

	void initWith(ColorWheelRenderOption colorWheelRenderOption);

	List<PresetsColorCircle> getColorCircleList();
}
