package com.example.frutiapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLData;

public class MainActivity extends AppCompatActivity {

    private EditText et_nombre;
    private ImageView iv_personaje;
    private TextView tv_bestScore;
    private MediaPlayer mp;

    int num = (int) (Math.random() * 10);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_nombre = (EditText) findViewById(R.id.txt_nombre);
        iv_personaje = (ImageView) findViewById(R.id.imageView_Personaje);
        tv_bestScore = (TextView) findViewById(R.id.textView_BestScore);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        int id;

        //CREACION DE IMAGENES DINAMICAS
        if (num == 0 || num == 10) {
            id = getResources().getIdentifier("mango", "drawable", getPackageName());
            iv_personaje.setImageResource(id);
        }else if (num == 1 || num == 9) {
                id = getResources().getIdentifier("fresa", "drawable", getPackageName());
                iv_personaje.setImageResource(id);
            }else if (num == 2 || num == 8) {
                    id = getResources().getIdentifier("manzana", "drawable", getPackageName());
                    iv_personaje.setImageResource(id);
                }else if (num == 3 || num == 7) {
                        id = getResources().getIdentifier("sandia", "drawable", getPackageName());
                        iv_personaje.setImageResource(id);
                    }else if (num == 4 || num == 5 || num == 6) {
                            id = getResources().getIdentifier("uva", "drawable", getPackageName());
                            iv_personaje.setImageResource(id);

                        }

            //
            AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "BD", null, 1);
            SQLiteDatabase BD = admin.getWritableDatabase();//metodo para agregar escritura a la base de datos

            Cursor consulta = BD.rawQuery(
                    "select * from puntaje where score = (select max(score) from puntaje)", null);

            if (consulta.moveToFirst()){
                String temp_nombre = consulta.getString(0);
                String temp_score = consulta.getString(1);
                tv_bestScore.setText("Record: "+temp_score + " de "+temp_nombre);
                BD.close();
            } else {
                BD.close();
            }




            mp = MediaPlayer.create(this, R.raw.alphabet_song);
            mp.start();
            mp.setLooping(true);

            }

            public void Jugar(View view){

                String nombre = et_nombre.getText().toString();

                if (!nombre.equals("")){
                    mp.stop(); // finaliza la reproduccion
                    mp.release(); // libera el recurso utilizado

                    Intent intent = new Intent(this, MainActivity2_Nivel1.class); // pasa a la segunda activity

                    intent.putExtra("jugador", nombre);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(this, "Primero debes escribir tu nombre", Toast.LENGTH_SHORT).show();

                    et_nombre.requestFocus();
                    InputMethodManager imm=(InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(et_nombre, InputMethodManager.SHOW_IMPLICIT);
                }
            }

            // metodo para bloquear el boton de REGRESAR
            @Override
            public void onBackPressed(){

            }
}
