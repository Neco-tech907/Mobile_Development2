package ru.mirea.ivanovrr.listviewapp;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * ListView + ArrayAdapter. Разметка элемента — системная simple_list_item_2 (два TextView),
 * поэтому getView переопределён: в text1 идёт «номер. название», в text2 — автор.
 */
public class MainActivity extends AppCompatActivity {

    private final List<Book> books = Arrays.asList(
            new Book("Фёдор Достоевский", "Братья Карамазовы"),
            new Book("Фёдор Достоевский", "Идиот"),
            new Book("Лев Толстой", "Война и мир"),
            new Book("Лев Толстой", "Анна Каренина"),
            new Book("Михаил Булгаков", "Мастер и Маргарита"),
            new Book("Михаил Булгаков", "Собачье сердце"),
            new Book("Николай Гоголь", "Мёртвые души"),
            new Book("Иван Тургенев", "Отцы и дети"),
            new Book("Антон Чехов", "Рассказы"),
            new Book("Александр Пушкин", "Евгений Онегин"),
            new Book("Михаил Лермонтов", "Герой нашего времени"),
            new Book("Иван Гончаров", "Обломов"),
            new Book("Евгений Замятин", "Мы"),
            new Book("Братья Стругацкие", "Пикник на обочине"),
            new Book("Братья Стругацкие", "Понедельник начинается в субботу"),
            new Book("Джордж Оруэлл", "1984"),
            new Book("Олдос Хаксли", "О дивный новый мир"),
            new Book("Рэй Брэдбери", "451° по Фаренгейту"),
            new Book("Джон Толкин", "Властелин колец"),
            new Book("Фрэнк Герберт", "Дюна"),
            new Book("Айзек Азимов", "Основание"),
            new Book("Артур Кларк", "2001: Космическая одиссея"),
            new Book("Станислав Лем", "Солярис"),
            new Book("Дуглас Адамс", "Автостопом по галактике"),
            new Book("Эрих Мария Ремарк", "Три товарища"),
            new Book("Эрнест Хемингуэй", "Старик и море"),
            new Book("Джером Сэлинджер", "Над пропастью во ржи"),
            new Book("Габриэль Гарсиа Маркес", "Сто лет одиночества"),
            new Book("Умберто Эко", "Имя розы"),
            new Book("Харпер Ли", "Убить пересмешника"),
            new Book("Даниэль Канеман", "Думай медленно… решай быстро"),
            new Book("Роберт Мартин", "Чистая архитектура"),
            new Book("Эндрю Хант, Дэвид Томас", "Программист-прагматик"),
            new Book("Мартин Клеппман", "Высоконагруженные приложения"),
            new Book("Стивен Хокинг", "Краткая история времени"));

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

        ListView booksList = findViewById(R.id.books_list_view);

        // Контекст — сама Activity, разметка — системная с двумя TextView, данные — список книг
        ArrayAdapter<Book> adapter = new ArrayAdapter<Book>(this,
                android.R.layout.simple_list_item_2, android.R.id.text1, books) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = view.findViewById(android.R.id.text1);
                TextView text2 = view.findViewById(android.R.id.text2);

                Book book = getItem(position);
                text1.setText(String.format(Locale.getDefault(), "%d. %s",
                        position + 1, book == null ? "" : book.getTitle()));
                text2.setText(book == null ? "" : book.getAuthor());
                return view;
            }
        };
        booksList.setAdapter(adapter);

        booksList.setOnItemClickListener((parent, view, position, id) ->
                Toast.makeText(this, books.get(position).toString(), Toast.LENGTH_SHORT).show());
    }
}
