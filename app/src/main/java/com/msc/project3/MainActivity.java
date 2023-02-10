package com.msc.project3;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import android.speech.tts.TextToSpeech;

public class MainActivity extends AppCompatActivity {

    //region
    Button btnOtra, reproducir; /*Cambiar ventana*/
    MediaPlayer mp;
    //endregion

    Button btn_voz, btn_lectura, btn_aceptar;
    TextView txt_voz, txt_lectura, txt_timer, txt_intentos, txt_secuencia;
    TextToSpeech txtToSpeech;
    RadioButton rb_1, rb_2;
    ScrollView scrvw_1;
    boolean timer_bandera;
    Timer timer;
    TimerTask timerTask;
    Double time = 0.0;
    int intentos = 0;

    private int lstRespuestasID[] = {
            R.id.rd1A, R.id.rd1B, R.id.rd1C, R.id.rd1D,
            R.id.rd2A, R.id.rd2B, R.id.rd2C, R.id.rd2D,
            R.id.rd3A, R.id.rd3B, R.id.rd3C, R.id.rd3D
    };
    private int lstPreguntasID[] = {
            R.id.txtP1, R.id.txtP2, R.id.txtP3
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//region
        /*Cambiar ventana*/
        btnOtra = findViewById(R.id.btnAbrir);
        btnOtra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Activity2.class);
                startActivity(intent);
            }
        });
        /*Fin cambiar ventana*/

        /*Reproducir voz*/
        reproducir = (Button) findViewById(R.id.btnAudio);
        mp = MediaPlayer.create(this,R.raw.piano);
        reproducir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mp.start();
                Toast.makeText(MainActivity.this, "Repetir", Toast.LENGTH_SHORT).show();
            }
        });
        /*Fin reproducir voz*/
//endregion

        txt_voz = findViewById(R.id.txtActividad);
        btn_voz = findViewById(R.id.btnEscuchar);
        txt_lectura = findViewById(R.id.txtLectura);
        btn_lectura = findViewById(R.id.btnEscuchar2);
        btn_aceptar = findViewById(R.id.btnAceptar);
        timer = new Timer();
        txt_timer = findViewById(R.id.txtTime);
        txt_intentos = findViewById(R.id.txtIntentos);
        txt_secuencia = findViewById(R.id.txtNumAct);
//region
        rb_1 = (RadioButton) findViewById(R.id.rd1);
        rb_2 = (RadioButton) findViewById(R.id.rd2);
        scrvw_1 = findViewById(R.id.scrollView2);

        rb_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((RadioButton) v).isChecked();
                // Check which radiobutton was pressed
                if (checked){
                    rb_2.setChecked(false);
                    btn_lectura.setVisibility(View.VISIBLE);
                    txt_lectura.setVisibility(View.VISIBLE);
                    scrvw_1.setVisibility(View.INVISIBLE);
                }
            }
        });
        rb_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((RadioButton) v).isChecked();
                // Check which radiobutton was pressed
                if (checked){
                    rb_1.setChecked(false);
                    btn_lectura.setVisibility(View.INVISIBLE);
                    txt_lectura.setVisibility(View.INVISIBLE);
                    scrvw_1.setVisibility(View.VISIBLE);
                }
                if (timer_bandera == false) {
                    timer_bandera = true;
                    startTimer();
                }
                else {
                    timer_bandera = false;
                    timerTask.cancel();
                }
            }
        });

        /*Texto a Voz*/
        txtToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                Locale spanish = new Locale("es", "MX");
                if (i!=TextToSpeech.ERROR){
                    txtToSpeech.setLanguage(spanish);
                }
            }
        });

        btn_voz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtToSpeech.speak(txt_voz.getText().toString(), txtToSpeech.QUEUE_FLUSH,null);
            }
        });
        btn_lectura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtToSpeech.speak(txt_lectura.getText().toString(), txtToSpeech.QUEUE_FLUSH,null);
            }
        });
        /*Fin texto a voz*/
//endregion

        /*Inicio*/
        String[] vPreguntas = getResources().getStringArray(R.array.tblPreguntas);
        String[] vRespuestas = getResources().getStringArray(R.array.tblRespuestas);
        int[] vCorrecto = getResources().getIntArray(R.array.tblCorrectRespuesta);

        for (int i = 0; i < lstRespuestasID.length; i++) {
            RadioButton rb = (RadioButton) findViewById(lstRespuestasID[i]);
            rb.setText(vRespuestas[i]);
        }
        for (int i = 0; i < lstPreguntasID.length; i++) {
            TextView txtR = findViewById(lstPreguntasID[i]);
            txtR.setText(vPreguntas[i]);
        }

        final int respuestaCorrecta1 = getResources().getInteger(R.integer.correct_respuesta1);
        final int respuestaCorrecta2 = getResources().getInteger(R.integer.correct_respuesta2);
        final int respuestaCorrecta3 = getResources().getInteger(R.integer.correct_respuesta3);
        final RadioGroup rdgroup1 = (RadioGroup) findViewById(R.id.rdgPregunta1);
        final RadioGroup rdgroup2 = (RadioGroup) findViewById(R.id.rdgPregunta2);
        final RadioGroup rdgroup3 = (RadioGroup) findViewById(R.id.rdgPregunta3);


        btn_aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id1 = rdgroup1.getCheckedRadioButtonId(), id2 = rdgroup2.getCheckedRadioButtonId(), id3 = rdgroup3.getCheckedRadioButtonId();
                int cont = 0, respuesta1 = -1, respuesta2 = -1, respuesta3 = -1;
                String mensaje = "Obtuviste: ";

                for(int i = 0; i < lstRespuestasID.length/lstPreguntasID.length; i++) {
                    if(id1 == -1) { respuesta1 = -1; }
                    else if(lstRespuestasID[i] == id1) { respuesta1 = i; }

                    if(id2 == -1) { respuesta2 = -1; }
                    else if(lstRespuestasID[i + (lstRespuestasID.length/lstPreguntasID.length)] == id2) { respuesta2 = i; }

                    if(id3 == -1) { respuesta3 = -1; }
                    else if(lstRespuestasID[i + (lstRespuestasID.length/lstPreguntasID.length) * 2] == id3) { respuesta3 = i; }
                }
                if(respuesta1 == -1 || respuesta2 == -1 || respuesta3 == -1) {
                    Toast.makeText(MainActivity.this, "Seleccione una opción", Toast.LENGTH_SHORT).show();
                }
                else if (respuesta1 == respuestaCorrecta1 || respuesta2 == respuestaCorrecta2 || respuesta3 == respuestaCorrecta3) {
                    if (respuesta1 == respuestaCorrecta1) { cont++; }
                    if (respuesta2 == respuestaCorrecta2) { cont++; }
                    if (respuesta3 == respuestaCorrecta3) { cont++; }
                    mensaje += cont + " preguntas correctas";
                    Toast.makeText(MainActivity.this, mensaje, Toast.LENGTH_SHORT).show();
                }

                /*AlertDialog.Builder resetAlert = new AlertDialog.Builder(this);
                resetAlert.setTitle("Reset Timer");
                resetAlert.setMessage("Estas seguro que deseas resetear?");
                resetAlert.setPositiveButton("Reset", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })*/

                if(cont == vPreguntas.length) {
                    intentos++;
                    timerTask.cancel();
                    time = 0.0;
                    timer_bandera = false;
                    txt_timer.setText(formatTime(0,0,0));
                    txt_intentos.setText("Intentos: 0");
                    intentos = 0;
                    Toast.makeText(MainActivity.this, "Felecidades!", Toast.LENGTH_SHORT).show();
                }
                else {
                    intentos++;
                    txt_intentos.setText("Intentos: " + intentos);
                    //Toast.makeText(MainActivity.this, "Vuelva a intentar", Toast.LENGTH_SHORT).show();
                }

            }
        });
        /*Fin*/
    }

    private void startTimer() {
        timerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        time++;
                        txt_timer.setText(getTimerText());
                    }
                });
            }
        };
        timer.scheduleAtFixedRate(timerTask,0,1000);
    }
    /*Timer*/
    private String getTimerText() {
        int rounded = (int) Math.round(time);
        int seconds = ((rounded % 86400) % 3600) % 60;
        int minutes = ((rounded % 86400) % 3600) / 60;
        int hour = (rounded % 86400) / 3600;

        return formatTime(seconds,minutes,hour);
    }

    private String formatTime(int seconds, int minutes, int hour) {
        return "Time: " + String.format("%02d", minutes) + ":" + String.format("%02d", seconds);
    }
    /*FinTimer*/
}