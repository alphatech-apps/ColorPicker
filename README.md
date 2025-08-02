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

Add view to your layout:
```xml
    <lib.jakir.codeview.CodeView
    android:id="@+id/code_view"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:layout_below="@id/toolbar" />
 ```

Setup JAVA:
 ```java
    CodeView mCodeView = findViewById(R.id.codeView);

        mCodeView.setCode(JAVA_CODE)
                .setTheme(Theme.ARDUINO_LIGHT)
                .setLanguage(Language.AUTO)
                .setZoomEnabled(true)
                .apply();
 ```

or add other >>>>

 ```java
    CodeView mCodeView = findViewById(R.id.codeView);

        mCodeView.setTheme(Theme.ARDUINO_LIGHT)
                .setCode(JAVA_CODE)
                .setLanguage(Language.AUTO)
                .setWrapLine(true)
                .setFontSize(14)
                .setZoomEnabled(true)
                .setShowLineNumber(true)
                .setStartLineNumber(1)
                .apply();
 ```

Listeners:

 ```java
 //Interface
private ProgressDialog mProgressDialog;

mCodeView.setOnHighlightListener(new CodeView.OnHighlightListener() {
    @Override
    public void onStartCodeHighlight() {
        mProgressDialog = ProgressDialog.show(getApplicationContext(), null, "Loading...", true);
    }

    @Override
    public void onFinishCodeHighlight() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
//                Toast.makeText(getApplicationContext(), "line count: " + mCodeView.getLineCount(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFontSizeChanged(int sizeInPx) {
//                Toast.makeText(getApplicationContext(), "Font size: " + sizeInPx, Toast.LENGTH_SHORT).show();
    }
});
 ```


Other Methods on Menu Preference:

```java

@Override
public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(lib.jakir.codeview.R.menu.menu_codeview_settings, menu);
    return true;
}

@Override
public boolean onPrepareOptionsMenu(Menu menu) {
    menu.findItem(lib.jakir.codeview.R.id.show_line_number_action).setChecked(mCodeView.isShowLineNumber());
    menu.findItem(lib.jakir.codeview.R.id.show_wrapline_action).setChecked(mCodeView.isWrapLine());
    menu.findItem(lib.jakir.codeview.R.id.zoom_enabled_action).setChecked(mCodeView.isZoomEnabled());
    return super.onPrepareOptionsMenu(menu);
}


@Override
public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    if (id == lib.jakir.codeview.R.id.change_theme_action) {
        new Theme("").dialog_show(this, mCodeView);
        return true;
    } else if (id == lib.jakir.codeview.R.id.show_line_number_action) {
        boolean newState = !item.isChecked();
        item.setChecked(newState);
        mCodeView.setShowLineNumber(newState).apply();
//            mCodeView.toggleLineNumber();
        return true;
    } else if (id == lib.jakir.codeview.R.id.show_wrapline_action) {
//            mCodeView.toggleWrapLine(); //or > mCodeView.setWrapLine(!mCodeView.isWrapLine()).apply();
        boolean newState = !item.isChecked();
        item.setChecked(newState);
        mCodeView.setWrapLine(newState).apply();
        return true;
    } else if (id == lib.jakir.codeview.R.id.zoom_enabled_action) {
        boolean newState = !item.isChecked();
        item.setChecked(newState);
        mCodeView.setZoomEnabled(newState).apply();
        return true;
    } else if (id == lib.jakir.codeview.R.id.change_daynight_action) {
        boolean newState = !item.isChecked();
        item.setChecked(newState);
        if (newState) {
            mCodeView.setTheme(Theme.ANDROIDSTUDIO).apply();
        } else {
            mCodeView.setTheme(Theme.ARDUINO_LIGHT).apply();
        }
        return true;
    }

    return super.onOptionsItemSelected(item);
}
```
.
.
.
.

## full activity for example
.....................
activity_main:
```xml
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:id="@+id/main"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <androidx.appcompat.widget.Toolbar
        android:id="@+id/toolbar"
        android:layout_width="match_parent"
        android:layout_height="?attr/actionBarSize"
        android:layout_alignParentTop="true"
        android:background="@android:color/transparent"
        app:title="Settings" />

    <lib.jakir.codeview.CodeView
        android:id="@+id/code_view"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:layout_below="@id/toolbar" />

</RelativeLayout>
 ```

MainActivity:
```java
package com.jakir.codeview;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import lib.jakir.codeview.CodeView;
import lib.jakir.codeview.Language;
import lib.jakir.codeview.Theme;

public class MainActivity extends AppCompatActivity {
    private static final String JAVA_CODE = "package com.example.android.bluetoothchat;\n" + "\n" + "import android.os.Bundle;\n" + "import android.support.v4.app.FragmentTransaction;\n" + "import android.view.Menu;\n" + "import android.view.MenuItem;\n" + "import android.widget.ViewAnimator;\n" + "\n" + "import com.example.android.common.activities.SampleActivityBase;\n" + "import com.example.android.common.logger.Log;\n" + "import com.example.android.common.logger.LogFragment;\n" + "import com.example.android.common.logger.LogWrapper;\n" + "import com.example.android.common.logger.MessageOnlyLogFilter;\n" + "\n" + "/**\n" + " * A simple launcher activity containing a summary sample description, sample log and a custom\n" + " * {@link android.support.v4.app.Fragment} which can display a view.\n" + " * <p>\n" + " * For devices with displays with a width of 720dp or greater, the sample log is always visible,\n" + " * on other devices it's visibility is controlled by an item on the Action Bar.\n" + " */\n" + "public class MainActivity extends SampleActivityBase {\n" + "\n" + "    public static final String TAG = \"MainActivity\";\n" + "\n" + "    // Whether the Log Fragment is currently shown\n" + "    private boolean mLogShown;\n" + "\n" + "    @Override\n" + "    protected void onCreate(Bundle savedInstanceState) {\n" + "        super.onCreate(savedInstanceState);\n" + "        setContentView(R.layout.activity_main);\n" + "\n" + "        if (savedInstanceState == null) {\n" + "            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();\n" + "            BluetoothChatFragment fragment = new BluetoothChatFragment();\n" + "            transaction.replace(R.id.sample_content_fragment, fragment);\n" + "            transaction.commit();\n" + "        }\n" + "    }\n" + "\n" + "    @Override\n" + "    public boolean onCreateOptionsMenu(Menu menu) {\n" + "        getMenuInflater().inflate(R.menu.main, menu);\n" + "        return true;\n" + "    }\n" + "\n" + "    @Override\n" + "    public boolean onPrepareOptionsMenu(Menu menu) {\n" + "        MenuItem logToggle = menu.findItem(R.id.menu_toggle_log);\n" + "        logToggle.setVisible(findViewById(R.id.sample_output) instanceof ViewAnimator);\n" + "        logToggle.setTitle(mLogShown ? R.string.sample_hide_log : R.string.sample_show_log);\n" + "\n" + "        return super.onPrepareOptionsMenu(menu);\n" + "    }\n" + "\n" + "    @Override\n" + "    public boolean onPrepareOptionsMenu(Menu menu) {\n" + "        MenuItem logToggle = menu.findItem(R.id.menu_toggle_log);\n" + "        logToggle.setVisible(findViewById(R.id.sample_output) instanceof ViewAnimator);\n" + "        logToggle.setTitle(mLogShown ? R.string.sample_hide_log : R.string.sample_show_log);\n" + "\n" + "        return super.onPrepareOptionsMenu(menu);\n" + "    }\n" + "\n" + "    @Override\n" + "    public boolean onPrepareOptionsMenu(Menu menu) {\n" + "        MenuItem logToggle = menu.findItem(R.id.menu_toggle_log);\n" + "        logToggle.setVisible(findViewById(R.id.sample_output) instanceof ViewAnimator);\n" + "        logToggle.setTitle(mLogShown ? R.string.sample_hide_log : R.string.sample_show_log);\n" + "\n" + "        return super.onPrepareOptionsMenu(menu);\n" + "    }\n" + "\n" + "    @Override\n" + "    public boolean onOptionsItemSelected(MenuItem item) {\n" + "        switch(item.getItemId()) {\n" + "            case R.id.menu_toggle_log:\n" + "                mLogShown = !mLogShown;\n" + "                ViewAnimator output = (ViewAnimator) findViewById(R.id.sample_output);\n" + "                if (mLogShown) {\n" + "                    output.setDisplayedChild(1);\n" + "                } else {\n" + "                    output.setDisplayedChild(0);\n" + "                }\n" + "                supportInvalidateOptionsMenu();\n" + "                return true;\n" + "        }\n" + "        return super.onOptionsItemSelected(item);\n" + "    }\n" + "\n" + "    /** Create a chain of targets that will receive log data */\n" + "    @Override\n" + "    public void initializeLogging() {\n" + "        // Wraps Android's native log framework.\n" + "        LogWrapper logWrapper = new LogWrapper();\n" + "        // Using Log, front-end to the logging chain, emulates android.util.log method signatures.\n" + "        Log.setLogNode(logWrapper);\n" + "\n" + "        // Filter strips out everything except the message text.\n" + "        MessageOnlyLogFilter msgFilter = new MessageOnlyLogFilter();\n" + "        logWrapper.setNext(msgFilter);\n" + "\n" + "        // On screen logging via a fragment with a TextView.\n" + "        LogFragment logFragment = (LogFragment) getSupportFragmentManager()\n" + "                .findFragmentById(R.id.log_fragment);\n" + "        msgFilter.setNext(logFragment.getLogView());\n" + "\n" + "        Log.i(TAG, \"Ready\");\n" + "    }\n" + "}";
    CodeView mCodeView;
    private ProgressDialog mProgressDialog;

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

        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getString(R.string.app_name));
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mCodeView = findViewById(R.id.code_view);

        mCodeView.setTheme(Theme.ARDUINO_LIGHT).setCode(JAVA_CODE).setLanguage(Language.AUTO).setZoomEnabled(true).apply();

//        mCodeView.setTheme(Theme.ARDUINO_LIGHT).setCode(JAVA_CODE).setLanguage(Language.AUTO).setWrapLine(true).setFontSize(14).setZoomEnabled(true).setShowLineNumber(true).setStartLineNumber(1).apply();

        mCodeView.setOnHighlightListener(new CodeView.OnHighlightListener() {
            @Override
            public void onStartCodeHighlight() {
                mProgressDialog = ProgressDialog.show(getApplicationContext(), null, "Loading...", true);
            }

            @Override
            public void onFinishCodeHighlight() {
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
//                Toast.makeText(getApplicationContext(), "line count: " + mCodeView.getLineCount(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFontSizeChanged(int sizeInPx) {
//                Toast.makeText(getApplicationContext(), "Font size: " + sizeInPx, Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public boolean onSupportNavigateUp() {
        finish(); // Handle back button click
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(lib.jakir.codeview.R.menu.menu_codeview_settings, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(lib.jakir.codeview.R.id.show_line_number_action).setChecked(mCodeView.isShowLineNumber());
        menu.findItem(lib.jakir.codeview.R.id.show_wrapline_action).setChecked(mCodeView.isWrapLine());
        menu.findItem(lib.jakir.codeview.R.id.zoom_enabled_action).setChecked(mCodeView.isZoomEnabled());
        return super.onPrepareOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == lib.jakir.codeview.R.id.change_theme_action) {
            new Theme("").dialog_show(this, mCodeView);
            return true;
        } else if (id == lib.jakir.codeview.R.id.show_line_number_action) {
            boolean newState = !item.isChecked();
            item.setChecked(newState);
            mCodeView.setShowLineNumber(newState).apply();
//            mCodeView.toggleLineNumber();
            return true;
        } else if (id == lib.jakir.codeview.R.id.show_wrapline_action) {
//            mCodeView.toggleWrapLine(); //or > mCodeView.setWrapLine(!mCodeView.isWrapLine()).apply();
            boolean newState = !item.isChecked();
            item.setChecked(newState);
            mCodeView.setWrapLine(newState).apply();
            return true;
        } else if (id == lib.jakir.codeview.R.id.zoom_enabled_action) {
            boolean newState = !item.isChecked();
            item.setChecked(newState);
            mCodeView.setZoomEnabled(newState).apply();
            return true;
        } else if (id == lib.jakir.codeview.R.id.change_daynight_action) {
            boolean newState = !item.isChecked();
            item.setChecked(newState);
            if (newState) {
                mCodeView.setTheme(Theme.ANDROIDSTUDIO).apply();
            } else {
                mCodeView.setTheme(Theme.ARDUINO_LIGHT).apply();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}

 ```

## Screenshots
![](https://github.com/alphatech-apps/ColorPicker/blob/main/screenshots/Screenshot_20250802-160156.png)
![](https://github.com/alphatech-apps/ColorPicker/blob/main/screenshots/Screenshot_20250802-160143.png)
