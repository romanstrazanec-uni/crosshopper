package sk.romanstrazanec.crosshopper.java;

import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    UpdateThread myThread;
    Handler updateHandler;
    private GameCanvas myCanvas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.getSupportActionBar().hide();
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        myCanvas = new GameCanvas(this);
        setContentView(myCanvas);
        createHandler();
        myThread = new UpdateThread(updateHandler);
        myThread.start();
    }

    private void createHandler() {
        updateHandler = new Handler() {
            public void handleMessage(Message msg) {
                myCanvas.update();
                myCanvas.invalidate();
                super.handleMessage(msg);
            }
        };
    }
}
