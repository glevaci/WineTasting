package glevacic.winetasting.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebView;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

import glevacic.winetasting.R;

@EActivity
public class StartupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);

        String htmlText = "<html><body style=\"text-align:justify\"> %s </body></Html>";
        WebView webView = (WebView) findViewById(R.id.a_startup_wv_description);
        String description = String.format(htmlText, getResources().getString(R.string.startup_description));
        webView.loadData(description, "text/html; charset=utf-8", "utf-8");
    }

    @Click(R.id.a_startup_btn_start)
    void startMainActivity() {
        Intent intent = new Intent(this, MainActivity_.class);
        startActivity(intent);
    }

    @Click(R.id.a_startup_btn_instructions)
    void startRulesActivity() {
        Intent intent = new Intent(this, InstructionsActivity.class);
        startActivity(intent);
    }
}
