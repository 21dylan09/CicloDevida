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
    private int seconds = 0;
    private boolean isRunning = false; // Para controlar el estado del cronómetro
    private TextView timerTextView;
    private ArrayList<String> lapTimes; // Lista para almacenar los tiempos de las vueltas
    private ArrayAdapter<String> adapter; // Adaptador para mostrar las vueltas
    private int lapCount = 0; // Contador de las vueltas

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout);

        timerTextView = findViewById(R.id.timerTextView);
        lapTimes = new ArrayList<>();
        ListView lapsListView = findViewById(R.id.lapsListView);

        // Configuración del ListView para mostrar las vueltas
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, lapTimes);
        lapsListView.setAdapter(adapter);

        // Recuperar el estado guardado si la actividad es recreada
        if (savedInstanceState != null) {
            seconds = savedInstanceState.getInt("seconds");
            lapTimes = savedInstanceState.getStringArrayList("lapTimes");
            lapCount = savedInstanceState.getInt("lapCount");
            updateTimerDisplay();
        }

        Button startButton = findViewById(R.id.startButton);
        Button stopButton = findViewById(R.id.stopButton);
        Button lapButton = findViewById(R.id.lapButton);

        startButton.setOnClickListener(v -> startTimer());
        stopButton.setOnClickListener(v -> stopTimer());
        lapButton.setOnClickListener(v -> recordLap());
    }

    // Método para iniciar el cronómetro
    private void startTimer() {
        if (!isRunning) {
            isRunning = true;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (isRunning) {
                        seconds++;
                        updateTimerDisplay();
                        handler.postDelayed(this, 1000);  // Actualiza cada segundo
                    }
                }
            }, 1000);
        }
    }

    // Método para detener el cronómetro
    private void stopTimer() {
        isRunning = false;
    }

    // Método para registrar una vuelta
    private void recordLap() {
        if (lapCount < 5) {
            int minutes = seconds / 60;
            int remainingSeconds = seconds % 60;
            String lapTime = String.format("Lap %d: %02d:%02d", lapCount + 1, minutes, remainingSeconds);
            lapTimes.add(lapTime);
            lapCount++;
            adapter.notifyDataSetChanged();
        }

        if (lapCount == 5) {
            stopTimer();
            // Al completar 5 vueltas, mostramos los tiempos parciales
            showLapTimes();
        }
    }

    // Mostrar los tiempos parciales
    private void showLapTimes() {
        StringBuilder lapReport = new StringBuilder("Lap Times:\n");
        for (String lapTime : lapTimes) {
            lapReport.append(lapTime).append("\n");
        }
        // Puedes mostrar el reporte de las vueltas de la forma que prefieras, como en un Toast o un AlertDialog
        System.out.println(lapReport.toString());  // Solo para pruebas en consola
    }

    // Actualiza la interfaz de usuario con el tiempo transcurrido
    private void updateTimerDisplay() {
        int minutes = seconds / 60;
        int remainingSeconds = seconds % 60;

        String time = String.format("%02d:%02d", minutes, remainingSeconds);
        timerTextView.setText(time);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("seconds", seconds); // Guardar el estado del cronómetro
        outState.putStringArrayList("lapTimes", lapTimes); // Guardar los tiempos de las vueltas
        outState.putInt("lapCount", lapCount); // Guardar el contador de vueltas
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        seconds = savedInstanceState.getInt("seconds");
        lapTimes = savedInstanceState.getStringArrayList("lapTimes");
        lapCount = savedInstanceState.getInt("lapCount");
        updateTimerDisplay();  // Recuperar el tiempo guardado
    }
}

