package ru.mirea.ivanovrr.scrollviewapp;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.math.BigInteger;
import java.util.Locale;

/**
 * ScrollView: геометрическая прогрессия со знаменателем 2, 100 элементов.
 * Каждый элемент создаётся LayoutInflater'ом из item.xml и добавляется в LinearLayout.
 * 2^99 не влезает даже в long, поэтому считаем через BigInteger.
 */
public class MainActivity extends AppCompatActivity {

    private static final int COUNT = 100;
    private static final int RATIO = 2;

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

        LinearLayout wrapper = findViewById(R.id.wrapper);
        BigInteger value = BigInteger.ONE; // b1 = 1
        for (int i = 0; i < COUNT; i++) {
            View view = getLayoutInflater().inflate(R.layout.item, wrapper, false);
            TextView index = view.findViewById(R.id.textViewIndex);
            TextView text = view.findViewById(R.id.textView);
            index.setText(String.format(Locale.getDefault(), "Элемент %d · b%d = 2^%d", i + 1, i + 1, i));
            text.setText(value.toString());
            wrapper.addView(view);
            value = value.multiply(BigInteger.valueOf(RATIO)); // b(n+1) = bn * 2
        }
    }
}
