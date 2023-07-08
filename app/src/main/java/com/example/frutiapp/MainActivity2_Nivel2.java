package com.example.frutiapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity2_Nivel2 extends AppCompatActivity {

    private TextView tv_nombre, tv_score;
    private ImageView iv_Auno, iv_Ados, iv_vidas;
    private EditText et_respuesta;
    private MediaPlayer mp, mp_great, mp_bad;

    int score, numAleatorio_uno, numAleatorio_dos, resultado, vidas = 3;
    String nombre_jugador, string_score, string_vidas;

    String numero[] = {"cero","uno","dos","tres","cuatro","cinco","seis","siete","ocho","nueve"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity2_nivel2);



        Toast.makeText(this, "Nivel 2 - Sumas moderadas ",Toast.LENGTH_SHORT).show();

        tv_nombre = findViewById(R.id.textView_nombre);
        tv_score = findViewById(R.id.textView_score);
        iv_vidas = findViewById(R.id.imageView_vidas);
        iv_Auno = findViewById(R.id.imageView_NumUno);
        iv_Ados = findViewById(R.id.imageView_NumDos);
        et_respuesta = findViewById(R.id.editText_resultado);

        // Guardo en la variable local el nombre del jugador, score y manzanas que se envia por al ACTIVITY anterior
        // y muestro en pantalla
        nombre_jugador = getIntent().getStringExtra("jugador");
        tv_nombre.setText("Jugador: "+ nombre_jugador);

        string_score = getIntent().getStringExtra("score");
        score = Integer.parseInt(string_score);
        tv_score.setText("Score: "+score);

        string_vidas = getIntent().getStringExtra("vidas");
        vidas = Integer.parseInt(string_vidas);
        if (vidas == 3 ){
            iv_vidas.setImageResource(R.drawable.tresvidas);
        } if (vidas == 2){
            iv_vidas.setImageResource(R.drawable.dosvidas);
        } if (vidas == 1){
            iv_vidas.setImageResource(R.drawable.unavida);
        }


        //cargo en imagen del juego en la barra superior
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);


        mp = MediaPlayer.create(this, R.raw.goats); // cargo la pista de audio
        mp.start(); // comenzar reproduccion de audio
        mp.setLooping(true); //ciclar la pista

        mp_great = MediaPlayer.create(this, R.raw.wonderful);
        mp_bad = MediaPlayer.create(this, R.raw.bad);

        NumAleatorio();
    }

    public void Comparar(View view){
        String respuesta = et_respuesta.getText().toString();

        if (!respuesta.equals("")){

            int respuesta_jugador = Integer.parseInt(respuesta);
            if (resultado == respuesta_jugador){

                mp_great.start();
                score++;
                tv_score.setText("Score: "+score);
                et_respuesta.setText("");

                BaseDeDatos();

            }else{

                mp_bad.start();
                vidas --;

                BaseDeDatos();

                switch (vidas){

                    case 3:
                        iv_vidas.setImageResource(R.drawable.tresvidas);
                        break;
                    case 2:
                        Toast.makeText(this, "Te quedan 2 manzanas", Toast.LENGTH_SHORT).show();
                        iv_vidas.setImageResource(R.drawable.dosvidas);
                        break;
                    case 1:
                        Toast.makeText(this, "Te queda 1 manzana", Toast.LENGTH_SHORT).show();
                        iv_vidas.setImageResource(R.drawable.unavida);
                        break;
                    case 0:
                        Toast.makeText(this, "Has perdido todas tus manzanas", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        mp.stop();
                        mp.release();


                        break;

                }

                et_respuesta.setText("");
            }

            NumAleatorio();

        }else{
            Toast.makeText(this,"Escribe tu respuesta", Toast.LENGTH_SHORT).show();
        }
    }


    public void NumAleatorio(){
        if (score<=19) {

            numAleatorio_uno = (int)(Math.random() * 10);
            numAleatorio_dos = (int)(Math.random() * 10);

            resultado = numAleatorio_uno + numAleatorio_dos;

                for (int i = 0; i < numero.length; i++){
                    int id = getResources().getIdentifier(numero[i], "drawable", getPackageName());

                    if (numAleatorio_uno == i){
                        iv_Auno.setImageResource(id); // carga la imagen
                    }
                    if (numAleatorio_dos == i){
                        iv_Ados.setImageResource(id);
                    }
                }

        }else{
            //si el score supera lo determido paso de nivel en la siguiente activity
            Intent intent = new Intent(this, MainActivity2_Nivel3.class);

            string_score = String.valueOf(score);
            string_vidas = String.valueOf(vidas);
            intent.putExtra("jugador", nombre_jugador);
            intent.putExtra("score", string_score);
            intent.putExtra("vidas", string_vidas);

            startActivity(intent);
            finish();
            mp.stop();
            mp.release(); // destruimos el objeto para liberar memoria

        }
    }

    public void BaseDeDatos(){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "BD", null, 1);
        SQLiteDatabase BD = admin.getWritableDatabase(); // apertura en modo lectura y escritura de nuestra base de datos

        Cursor consulta = BD.rawQuery("select *from puntaje where score = (select max(score) from puntaje)", null);

        if (consulta.moveToFirst()){  // si se encontro algun registro
            String temp_nombre = consulta.getString(0);
            String temp_score = consulta.getString(1);

            //cambiamos los valores string a enteros para poder hacer comparaciones

            int bestScore = Integer.parseInt(temp_score);

            if (score > bestScore){
                ContentValues modificacion = new ContentValues();
                modificacion.put("nombre", nombre_jugador);
                modificacion.put("score", score);

                BD.update("puntaje", modificacion,"score=" + bestScore, null);

            }

            BD.close();

        }else{
            ContentValues insertar = new ContentValues();

            insertar.put("nombre", nombre_jugador);
            insertar.put("score", score);

            BD.insert("puntaje", null, insertar);
            BD.close();

        }
    }

    @Override
    public void onBackPressed(){


    }

}