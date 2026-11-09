package com.polina.love;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        FrameLayout root = new FrameLayout(this);
        HeartView heart = new HeartView(this);
        root.addView(heart);

        LinearLayout panel = new LinearLayout(this);
        panel.setOrientation(LinearLayout.VERTICAL);
        panel.setGravity(Gravity.CENTER_HORIZONTAL);
        int p = (int) (24 * getResources().getDisplayMetrics().density);
        panel.setPadding(p, p, p, p);

        EditText pass = new EditText(this);
        pass.setHint("Введи пароль");
        pass.setHintTextColor(Color.GRAY);
        pass.setTextColor(Color.WHITE);
        pass.setGravity(Gravity.CENTER);
        pass.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        Button btn = new Button(this);
        btn.setText("Открыть ❤");
        btn.setBackgroundColor(Color.rgb(200, 30, 60));
        btn.setTextColor(Color.WHITE);
        panel.addView(pass, new LinearLayout.LayoutParams(-1, -2));
        panel.addView(btn, new LinearLayout.LayoutParams(-1, -2));
        root.addView(panel, new FrameLayout.LayoutParams(-1, -2, Gravity.BOTTOM));

        btn.setOnClickListener(v -> {
            if (Native.checkPassword(pass.getText().toString().trim())) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(pass.getWindowToken(), 0);
                panel.setVisibility(View.GONE);
                heart.start();
            } else {
                pass.setText("");
                Toast.makeText(this, "Неверный пароль", Toast.LENGTH_SHORT).show();
            }
        });
        setContentView(root);
    }
}
