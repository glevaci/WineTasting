package glevacic.winetasting;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button buttonNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        android.os.Debug.waitForDebugger();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonNext = (Button) findViewById(R.id.buttonNext);
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView txtView = (TextView) findViewById(R.id.textViewTask);
                txtView.setText("VIDI UKELJIH TEKXT!");
            }
        });
    }
}
