package ekscholar.ekdorn.ekscholar;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ScrollingActivity extends AppCompatActivity {
    TextView task;
    LinearLayout answers;
    Button complete;

    HomeWork hw;
    ArrayList<String> done;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setEnabled(false);
        //fab set text "mark"

        System.out.println("uuu");

        int date = 10000 * Calendar.getInstance().get(Calendar.YEAR) +
                100 * (Calendar.getInstance().get(Calendar.MONTH)+1) +
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

        task = findViewById(R.id.task);
        answers = findViewById(R.id.answers);
        complete = findViewById(R.id.apply);

        hw = new HomeWork(date, new HomeWork.TimeTaking() {
            @Override
            public void onComplete() {
                task.setText(hw.task);

                for (int i = 0; i < hw.answers.size(); i++) {
                    EditText answer = new EditText(ScrollingActivity.this);
                    answer.setHint("Answer for question " + (i+1));
                    answers.addView(answer);
                }

                done = hw.answers;
            }
        });



        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int mistakes = 0;
                for (int i = 0; i < answers.getChildCount(); i++) {
                    EditText current = (EditText) answers.getChildAt(i);
                    String ans = current.getText().toString();
                    if (ans.equals(done.get(i))) {
                        System.out.println("recht");
                    } else {
                        mistakes++;
                        current.setError("wrong!");
                    }
                }
                Toast.makeText(ScrollingActivity.this, "You made " + mistakes + " mistakes!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
