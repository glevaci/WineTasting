package glevacic.winetasting.activities;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;

import com.bluejamesbond.text.DocumentView;

import glevacic.winetasting.R;

import static android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;

public class InstructionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructions);

        DocumentView documentView = (DocumentView) findViewById(R.id.a_instructions_dv_cards_description);

        SpannableStringBuilder spannableString = new SpannableStringBuilder();
        spannableString.append(getResources().getString(R.string.instructions_cards_description))
                .append(System.getProperty("line.separator"));

        String card = getResources().getString(R.string.instructions_card_action);
        int start = spannableString.length();
        int end = start + getEndingIndex(card);
        spannableString.append(card)
                .setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.colorAction)),
                        start, end, SPAN_EXCLUSIVE_EXCLUSIVE);

        spannableString.append(System.getProperty("line.separator"));

        card = getResources().getString(R.string.instructions_card_mandatory);
        start = spannableString.length();
        end = start + getEndingIndex(card);
        spannableString.append(card)
                .setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.colorMandatory)),
                        start, end, SPAN_EXCLUSIVE_EXCLUSIVE);

        spannableString.append(System.getProperty("line.separator"));

        card = getResources().getString(R.string.instructions_card_status);
        start = spannableString.length();
        end = start + getEndingIndex(card);
        spannableString.append(card)
                .setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.colorStatus)),
                        start, end, SPAN_EXCLUSIVE_EXCLUSIVE);

        spannableString.append(System.getProperty("line.separator"));

        documentView.setText(spannableString);
    }

    // return index of second space (second word separator - always our case) in string
    private int getEndingIndex(String string) {
        int cnt = 0;
        for (int i=0; i < string.length(); ++i) {
            if (Character.isWhitespace(string.charAt(i)))
                ++cnt;
            if (cnt == 2)
                return i;
        }
        return 0;
    }
}
