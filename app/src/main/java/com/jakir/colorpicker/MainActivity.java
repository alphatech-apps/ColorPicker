package com.jakir.colorpicker;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.jakir.presetscolorpicker.PresetsColorPickerView;
import com.jakir.presetscolorpicker.builder.PresetsColorPickerDialogBuilder;

import jakir.customcolorpicker.CustomColorPickerDialog;

public class MainActivity extends AppCompatActivity {

    TextView sampleText;
    Button btnPickColor;
    int defaultColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        btnPickColor = findViewById(R.id.btnPickColor);
        sampleText = findViewById(R.id.sampleText);
        defaultColor = PrefThemeManager.getAccentColor(this);

        // Apply saved color
        int savedColor = PrefThemeManager.getAccentColor(this);
        sampleText.setTextColor(savedColor);
        btnPickColor.setBackgroundColor(savedColor);

        btnPickColor.setOnClickListener(v -> {
            openCustomColorPicker();
        });
    }

    private void openPresetsColorPicker() {
        PresetsColorPickerDialogBuilder.with(this).setTitle("Select a color").defaultColor(defaultColor).wheelType(PresetsColorPickerView.WHEEL_TYPE.FLOWER).density(10).setOnPresetsColorSelectedListener(selectedColor -> {
        }).setPositiveButton("OK", (dialog, selectedColor, allColors) -> {
            defaultColor = selectedColor;
            ApplyAndSaveColor(selectedColor);
        }).setNegativeButton("Cancel", (dialog, which) -> {
        }).setNeutralButton("Custom", (dialog, which) -> {
            openCustomColorPicker();
        }).build().show()
        ;

    }

    private void ApplyAndSaveColor(int selectedColor) {
        PrefThemeManager.saveAccentColor(MainActivity.this, selectedColor);

        sampleText.setTextColor(selectedColor);
        btnPickColor.setBackgroundColor(selectedColor);
    }

    private void openCustomColorPicker() {
        CustomColorPickerDialog colorDialog = new CustomColorPickerDialog(this, "Select a color", defaultColor, true, new CustomColorPickerDialog.OnCustomColorPickerListener() {
            @Override
            public void onSelected(CustomColorPickerDialog dialog, int color) {
                defaultColor = color;
                ApplyAndSaveColor(color);
            }

            @Override
            public void onChange(CustomColorPickerDialog dialog) {
                openPresetsColorPicker();
            }

            @Override
            public void onCancel(CustomColorPickerDialog dialog) {
            }
        });

        colorDialog.show();


    }
}