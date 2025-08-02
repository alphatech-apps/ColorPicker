# ColorPicker
### LATEST-VERSION

[![](https://jitpack.io/v/alphatech-apps/ColorPicker.svg)](https://jitpack.io/#alphatech-apps/ColorPicker)

## Install
Add it in your root `build.gradle` at the end of repositories:
```gradle
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }
}
```
Add the dependency:
```gradle
dependencies {
	        implementation 'com.github.alphatech-apps:ColorPicker:LATEST-VERSION'
	}
```

## Features
* Day Night

## Usage

Setup JAVA:
 ```java
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

 ```
.
.
.
.
.
.

## full activity for example
.....................
activity_main:
```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/main"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:gravity="center"
    android:orientation="vertical"
    tools:context=".MainActivity">
    <Button
        android:id="@+id/btnPickColor"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Pick a Color"/>

    <TextView
        android:id="@+id/sampleText"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="This will be accent colored"
        android:textSize="24sp"
        android:textStyle="bold"
        android:layout_marginTop="20dp"/>


</LinearLayout>
 ```

MainActivity:
```java
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

 ```

## Screenshots
![](https://github.com/alphatech-apps/ColorPicker/blob/main/screenshots/Screenshot_20250802-160156.png)
![](https://github.com/alphatech-apps/ColorPicker/blob/main/screenshots/Screenshot_20250802-160143.png)
