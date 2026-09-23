package ru.mirea.ivanovrr.recyclerviewapp;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/** RecyclerView со списком исторических событий: адаптер + вертикальный LinearLayoutManager. */
public class MainActivity extends AppCompatActivity {

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

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setAdapter(new EventRecyclerViewAdapter(getListData()));
        // вертикальный список; для сетки достаточно заменить на GridLayoutManager(this, 2)
        recyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    private List<HistoricalEvent> getListData() {
        List<HistoricalEvent> list = new ArrayList<>();
        list.add(new HistoricalEvent(1147, "Первое упоминание Москвы",
                "Ипатьевская летопись сообщает о встрече Юрия Долгорукого с князем Святославом в Москве.",
                "event_kremlin"));
        list.add(new HistoricalEvent(1380, "Куликовская битва",
                "Войско Дмитрия Донского разбило армию Мамая на Куликовом поле.",
                "event_shield"));
        list.add(new HistoricalEvent(1703, "Основание Санкт-Петербурга",
                "Пётр I заложил крепость на Заячьем острове — будущую столицу империи.",
                "event_ship"));
        list.add(new HistoricalEvent(1812, "Бородинское сражение",
                "Крупнейшая битва Отечественной войны между армиями Кутузова и Наполеона.",
                "event_cannon"));
        list.add(new HistoricalEvent(1861, "Отмена крепостного права",
                "Манифест Александра II освободил более 20 миллионов крестьян.",
                "event_chain"));
        list.add(new HistoricalEvent(1945, "Победа в Великой Отечественной войне",
                "Подписан акт о безоговорочной капитуляции Германии.",
                "event_star"));
        list.add(new HistoricalEvent(1957, "Запуск первого спутника",
                "«Спутник-1» открыл космическую эру человечества.",
                "event_sputnik"));
        list.add(new HistoricalEvent(1961, "Полёт Юрия Гагарина",
                "Первый человек в космосе: 108 минут на корабле «Восток-1».",
                "event_rocket"));
        list.add(new HistoricalEvent(1969, "Высадка на Луну",
                "Нил Армстронг и Базз Олдрин ступили на поверхность Луны в ходе миссии «Аполлон-11».",
                "event_moon"));
        list.add(new HistoricalEvent(1991, "Появление Всемирной паутины",
                "Тим Бернерс-Ли открыл первый в мире веб-сайт для широкой публики.",
                "event_globe"));
        return list;
    }
}
