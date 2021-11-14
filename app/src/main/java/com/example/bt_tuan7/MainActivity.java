package com.example.bt_tuan7;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private EditText editTextInput;
    private TextView textProgress;
    private ProgressBar progressBar;
    private Button btnRepeat;

    //    Thread status
    private boolean isRunning;
    //    Number of work (loop)
    private int nWork;
    //    Number of work (loop) done
    private int accum;
    //    Percentage work (loop) done
    private int progress;

    private final int MAX_PROGRESS = 100;
    private final int progressStep = 1;

    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextInput = (EditText) findViewById(R.id.editTextInput);
        textProgress = (TextView) findViewById(R.id.textProgress);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnRepeat = (Button) findViewById(R.id.btnRepeat);

        btnRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onStart();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        reset();

        nWork = Integer.parseInt(editTextInput.getText().toString());
        progressBar.setMax(MAX_PROGRESS);
        isRunning = false;

        Thread thread = new Thread(backGroundTask, "backAlias1");
        thread.start();
    }

    private Runnable foreGroundTask = new Runnable() {
        @Override
        public void run() {
            try {
                if (isRunning) {
//                    Update UI
                    textProgress.setText(String.valueOf(progress) + "%");
                    progressBar.setProgress(progress);
                    btnRepeat.setEnabled(false);
                } else {
                    reset();
                }
            } catch (Exception e) {
                Log.e("Foreground Task: ", e.getMessage());
            }
        }
    };

    private Runnable backGroundTask = new Runnable() {
        @Override
        public void run() {
            try {
//                Start doing background tasks
                isRunning = true;

                for (int i = 0; i < nWork; ++i) {
                    Thread.sleep(0);

                    accum += progressStep;
                    progress = (int) Math.round(1.0 * accum / nWork * MAX_PROGRESS);

                    handler.post(foreGroundTask);
                }

//                Background tasks finished

                isRunning = false;
            } catch (Exception e) {
                Log.e("Background Task: ", e.getMessage());
            }
        }
    };

    //    Reset every value
    private void reset() {
        accum = 0;
        progress = 0;
        textProgress.setText("0%");
        progressBar.setProgress(0);
        btnRepeat.setEnabled(true);
    }
}