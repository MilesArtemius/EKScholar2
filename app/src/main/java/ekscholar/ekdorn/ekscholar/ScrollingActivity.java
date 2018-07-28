package ekscholar.ekdorn.ekscholar;

import android.app.Activity;
import android.content.Intent;
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

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ScrollingActivity extends AppCompatActivity {
    TextView task;
    LinearLayout answers;
    Button complete;
    FloatingActionButton mark;

    HomeWork hw;
    List<String> done;

    int number;
    int mistakes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);

        number = this.getIntent().getIntExtra(DaysAdapter.flag, 0);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mark = (FloatingActionButton) findViewById(R.id.fab);
        mark.setEnabled(false);

        task = findViewById(R.id.task);
        answers = findViewById(R.id.answers);
        complete = findViewById(R.id.apply);

        hw = (HomeWork) this.getIntent().getSerializableExtra(DaysAdapter.home);
        task.setText(hw.taskT);
        for (int i = 0; i < hw.answers.size(); i++) {
            EditText answer = new EditText(ScrollingActivity.this);
            answer.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            if (hw.mark == 0) {
                answer.setHint("Answer for question " + (i + 1));
            } else {
                answer.setText(hw.answersAct.get(i));
                answer.setEnabled(false);
            }
            answers.addView(answer);
        }
        done = new ArrayList<>();

        if (hw.mark != 0) {
            switch (hw.mark) {
                case 1:
                    mark.setImageResource(R.drawable.mark1ico);
                    break;
                case 2:
                    mark.setImageResource(R.drawable.mark2ico);
                    break;
                case 3:
                    mark.setImageResource(R.drawable.mark3ico);
                    break;
                case 4:
                    mark.setImageResource(R.drawable.mark4ico);
                    break;
                case 5:
                    mark.setImageResource(R.drawable.mark5ico);
                    break;
                default:
                    mark.setImageResource(R.drawable.mark0ico);
                    break;
            }
        }

        if (hw.mark == 0) {
            complete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mistakes = 0;
                    for (int i = 0; i < answers.getChildCount(); i++) {
                        EditText current = (EditText) answers.getChildAt(i);
                        String ans = current.getText().toString();
                        done.add(ans);
                        if (ans.equals(hw.answers.get(i))) {
                            System.out.println("recht");
                        } else {
                            mistakes++;
                            current.setError(hw.answers.get(i));
                        }
                    }
                    hw.setAnswers(done);
                    Toast.makeText(ScrollingActivity.this, "You made " + mistakes + " mistakes!", Toast.LENGTH_SHORT).show();

                    complete.setText("Return");
                    complete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent();
                            intent.putExtra(DaysAdapter.mark, (5 - mistakes < 1) ? 1 : (5 - mistakes));
                            intent.putExtra(DaysAdapter.flag, number);
                            System.out.println("INTENTING");
                            ScrollingActivity.this.setResult(Activity.RESULT_OK, intent);
                            ScrollingActivity.this.finish();
                        }
                    });
                }
            });
        } else {
            complete.setText("Return");
            complete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    System.out.println("INTENTING");
                    ScrollingActivity.this.setResult(Activity.RESULT_CANCELED, intent);
                    ScrollingActivity.this.finish();
                }
            });
        }
    }
}
