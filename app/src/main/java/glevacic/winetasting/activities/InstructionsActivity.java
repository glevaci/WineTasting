package glevacic.winetasting.activities;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spannable;

import com.bluejamesbond.text.DocumentView;

import glevacic.winetasting.R;

public class InstructionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructions);

        String htmlString = getResources().getString(R.string.instructions_cards);
        htmlString = String.format(htmlString,
                ContextCompat.getColor(this, R.color.colorAction),
                ContextCompat.getColor(this, R.color.colorMandatory),
                ContextCompat.getColor(this, R.color.colorStatus));

        Spannable spannable = (Spannable) Html.fromHtml(htmlString);
        DocumentView dv = (DocumentView) findViewById(R.id.a_instructions_dv);
        dv.setText(spannable);
    }
}
