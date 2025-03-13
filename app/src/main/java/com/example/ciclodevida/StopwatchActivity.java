package com.example.ciclodevida;

import android.os.Bundle;
import android.os.Handler;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class StopwatchActivity extends AppCompatActivity {

    private Handler handler = new Handler();
    private int segundos = 0;
    private boolean ejecutando = false;
    private TextView tiempoTextView;
    private ArrayList<String> tiempoVueltas;
    private ArrayAdapter<String> adapter;
    private int contadorVueltas = 0;
    private int tiempoUltimaVuelta = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout);

        tiempoTextView = findViewById(R.id.tiempoTextView);
        tiempoVueltas = new ArrayList<>();
        ListView lapsListView = findViewById(R.id.lapsListView);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, tiempoVueltas);
        lapsListView.setAdapter(adapter);

        if (savedInstanceState != null) {
            segundos = savedInstanceState.getInt("segundos");
            tiempoVueltas = savedInstanceState.getStringArrayList("tiempoVueltas");
            contadorVueltas = savedInstanceState.getInt("contadorVueltas");
            tiempoUltimaVuelta = savedInstanceState.getInt("tiempoUltimaVuelta", 0);
            updateTimerDisplay();
        }

        Button btnIniciar = findViewById(R.id.btnIniciar);
        Button btnParar = findViewById(R.id.btnParar);
        Button btnVuelta = findViewById(R.id.btnVuelta);
        Button btnResetear = findViewById(R.id.btnResetear);

        btnIniciar.setOnClickListener(v -> iniciarTiempo());
        btnParar.setOnClickListener(v -> pararTiempo());
        btnVuelta.setOnClickListener(v -> guardarVuelta());
        btnResetear.setOnClickListener(v -> resetearTiempo()); 
    }

    private void iniciarTiempo() {
        if (!ejecutando) {
            ejecutando = true;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (ejecutando) {
                        segundos++;
                        updateTimerDisplay();
                        handler.postDelayed(this, 1000);
                    }
                }
            }, 1000);
        }
    }

    private void pararTiempo() {
        ejecutando = false;
    }

    private void guardarVuelta() {
        if (contadorVueltas < 5) {
            int tiempoVuelta = segundos - tiempoUltimaVuelta;
            tiempoUltimaVuelta = segundos;

            int minutos = segundos / 60;
            int segundosRestantes = segundos % 60;
            int minutosVuelta = tiempoVuelta / 60;
            int segundosVuelta = tiempoVuelta % 60;

            String lapEntry = String.format("Vuelta %d: %02d:%02d (+%02d:%02d)",
                    contadorVueltas + 1, minutos, segundosRestantes, minutosVuelta, segundosVuelta);

            tiempoVueltas.add(lapEntry);
            contadorVueltas++;
            adapter.notifyDataSetChanged();
        }

        if (contadorVueltas == 5) {
            pararTiempo();
            showtiempoVueltas();
        }
    }

    private void resetearTiempo() {
        ejecutando = false;
        segundos = 0;
        contadorVueltas = 0;
        tiempoUltimaVuelta = 0;
        tiempoVueltas.clear();
        adapter.notifyDataSetChanged();
        updateTimerDisplay();
    }

    private void showtiempoVueltas() {
        StringBuilder lapReport = new StringBuilder("Tiempo vuelta:\n");
        for (String tiempoVuelta : tiempoVueltas) {
            lapReport.append(tiempoVuelta).append("\n");
        }
        System.out.println(lapReport.toString());
    }

    private void updateTimerDisplay() {
        int minutos = segundos / 60;
        int segundosRestantes = segundos % 60;

        String time = String.format("%02d:%02d", minutos, segundosRestantes);
        tiempoTextView.setText(time);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("segundos", segundos);
        outState.putStringArrayList("tiempoVueltas", tiempoVueltas);
        outState.putInt("contadorVueltas", contadorVueltas);
        outState.putInt("tiempoUltimaVuelta", tiempoUltimaVuelta);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        segundos = savedInstanceState.getInt("segundos");
        tiempoVueltas = savedInstanceState.getStringArrayList("tiempoVueltas");
        contadorVueltas = savedInstanceState.getInt("contadorVueltas");
        tiempoUltimaVuelta = savedInstanceState.getInt("tiempoUltimaVuelta", 0);
        updateTimerDisplay();
    }
}
