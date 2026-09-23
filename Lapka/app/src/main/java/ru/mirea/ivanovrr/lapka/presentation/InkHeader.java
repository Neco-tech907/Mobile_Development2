package ru.mirea.ivanovrr.lapka.presentation;

import android.view.View;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/**
 * Отступы для экранов с тёмной шапкой: шапка заходит под статус-бар и растёт на его высоту,
 * а низ экрана поднимается над навигацией и клавиатурой.
 */
final class InkHeader {

    private InkHeader() {
    }

    static void applyInsets(View root, View header) {
        int headerMinHeight = header.getMinimumHeight();
        ViewCompat.setOnApplyWindowInsetsListener(root, (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            Insets ime = insets.getInsets(WindowInsetsCompat.Type.ime());
            header.setMinimumHeight(headerMinHeight + bars.top);
            header.setPadding(header.getPaddingLeft(), bars.top,
                    header.getPaddingRight(), header.getPaddingBottom());
            v.setPadding(bars.left, 0, bars.right, Math.max(bars.bottom, ime.bottom));
            return insets;
        });
    }
}
