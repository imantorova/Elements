package com.example.irhen.elements;
/*
Данная программа позволяет рассчитать количество элементов на окружности заданного радиуса.
Входными параметрами являются: радиус и ширина элемента. Также необходимым условием является то,
что расстояние между элементами не может быть менее 20% от ширины элемента. Программа рассчитывает
расстояние между элементами (минимальное/оптимальное), количество размещаемых на окружности элементов.
На экран выводится как результат работы программы: оптимальное расстояние между элементами,
количество элементов и координаты точек на окружности.
 */
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private EditText radius, elem;
    private TextView output_w, output_el;
    private Button calculation, exit;
    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addOnClickListener();
    }

    //Метод-обработчик нажатий кнопок
    public void addOnClickListener(){
        radius = (EditText)findViewById(R.id.radius);
        elem = (EditText)findViewById(R.id.elem);
        output_w = (TextView)findViewById(R.id.output_width);
        output_el = (TextView)findViewById(R.id.output_el);
        calculation = (Button)findViewById(R.id.button);
        exit = (Button)findViewById(R.id.exit);
        lv = (ListView)findViewById(R.id.listView);

        //проверка на правильность заполнения полей и вызов расчетчика
        calculation.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (radius.getText().toString().isEmpty()||
                            elem.getText().toString().isEmpty()){
                            AlertDialog.Builder builderFlag = new AlertDialog.Builder(MainActivity.this);

                            builderFlag.setMessage("Проверьте правильность ввода данных!")
                                    .setCancelable(false)
                                    .setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                        }
                                    });
                            AlertDialog alertDialog = builderFlag.create();
                            alertDialog.setTitle("Ошибка!");
                            alertDialog.show();
                        }else {
                            calc();
                        }
                    }
                }
        );
        //выход из приложения
        exit.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setMessage("Выйти из приложения?")
                                .setCancelable(false)
                                .setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                    }
                                })
                                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        finish();
                                    }
                                });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.setTitle("Закрытие приложения");
                        alertDialog.show();
                    }
                }
        );
    }

    //Метод- расчетчик расстояния между двумя элементами, с последующей оптимизацией и расчетом
    //координат
    public void calc(){
        double l_radius = Double.parseDouble(radius.getText().toString());
        int l_chord = Integer.parseInt(elem.getText().toString());

        //вычисляем центральный угол
        double a = 2 * Math.asin(l_chord / (2 * l_radius));//в радианах
        double a_grad = (a * 180)/Math.PI;//в градусах

        //расстояние между 2-мя соседними элементами (min)
        double a_el = 2 * Math.asin((l_chord * 0.2) / (2 * l_radius));//в радианах
        double a_el_grad = (a_el * 180)/Math.PI;//в градусах

        //расчет количества элементов, которое можно разместить
        double p_result = Math.floor(360/(a_grad + a_el_grad));
        int result = (int) p_result;//количество элементов
        output_el.setText(String.valueOf(result));

        //расчет расстояния между элементами (равномерное распределение)
        double new_a = (360 - a_grad * result) / result;//новый центральный угол в градусах
        double new_a_rad = (new_a * Math.PI) / 180;//новый центральный угол в радианах
        double new_width = 2 * l_radius * Math.sin(new_a_rad /2);
        output_w.setText(String.valueOf(new_width));

        //расчет координат против часовой стрелки с начальной точкой (R,0) и центром окружности (0,0)
        //исправленный вариант
        StringBuffer sb = new StringBuffer();
        int j = 1;
        int k = 0;
        for (int i = 0; i < result*2-1; ++i) {
            if (i != 0 && i%2 == 0)
                j++;
            if((i+1)%2 == 0 )
                k++;
            double y = l_radius * Math.sin(a*j + new_a_rad*k);
            double x = l_radius * Math.cos(a*j + new_a_rad*k);
            sb.append("x = ");
            sb.append(x);
            sb.append("   ");
            sb.append("y = ");
            sb.append(y);
            sb.append(";");
        }

        //Результат выводится на экран
        String[] w = sb.toString().trim().split( ";");
        ListAdapter adapter = new ArrayAdapter<String>(
                MainActivity.this, android.R.layout.simple_list_item_1, w);
        lv.setAdapter(adapter);
    }
}
