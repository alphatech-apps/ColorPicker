package com.jakir.presetscolorpicker.builder;

import com.jakir.presetscolorpicker.PresetsColorPickerView;
import com.jakir.presetscolorpicker.renderer.ColorWheelRenderer;
import com.jakir.presetscolorpicker.renderer.FlowerColorWheelRenderer;
import com.jakir.presetscolorpicker.renderer.SimpleColorWheelRenderer;

public class ColorWheelRendererBuilder {
	public static ColorWheelRenderer getRenderer(PresetsColorPickerView.WHEEL_TYPE wheelType) {
		switch (wheelType) {
			case CIRCLE:
				return new SimpleColorWheelRenderer();
			case FLOWER:
				return new FlowerColorWheelRenderer();
		}
		throw new IllegalArgumentException("wrong WHEEL_TYPE");
	}
}