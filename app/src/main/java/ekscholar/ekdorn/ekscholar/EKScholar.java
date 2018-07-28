package ekscholar.ekdorn.ekscholar;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class EKScholar extends AppCompatActivity {
    public static final int loginCode = 42;
    public static final int scrollingCode = 64;

    ProgressBar progress;
    LinearLayout terra;
    Button backButton;
    TextView date;
    Button forthButton;
    RecyclerView week;

    DaysAdapter adapter;
    ArrayList<HomeWork> days;

    Calendar cl;
    private static int weekCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Intent signInIntent = new Intent();
            signInIntent.setClass(this, LoginActivity.class);
            startActivityForResult(signInIntent, loginCode);
        }

        setContentView(R.layout.activity_ekscholar);

        progress = (ProgressBar) findViewById(R.id.progress);
        terra = (LinearLayout) findViewById(R.id.terra);
        backButton = (Button) findViewById(R.id.button_back);
        date = (TextView) findViewById(R.id.date);
        forthButton = (Button) findViewById(R.id.button_forth);
        week = (RecyclerView) findViewById(R.id.week);

        cl = Calendar.getInstance();
        cl.setFirstDayOfWeek(Calendar.SUNDAY);
        loadAll();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                weekCounter--;
                loadAll();
            }
        });
        forthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                weekCounter++;
                loadAll();
            }
        });
    }

    private void loadAll() {
        Log.e("TAG", "loadAll: " + weekCounter);
        days = new ArrayList<>();
        adapter = new DaysAdapter(days, this);
        cl.add(Calendar.DATE, (-cl.get(Calendar.DAY_OF_WEEK) + 2 + (weekCounter * 7)));
        date.setText(cl.get(Calendar.WEEK_OF_MONTH) + "th week" + "\n" + cl.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())
                + " " + cl.get(Calendar.YEAR)); //
        fillDaysIn(cl);

        week.setLayoutManager(new LinearLayoutManager(this));
        week.setAdapter(adapter);

        showProgress(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("GOT");
        if (resultCode == RESULT_OK) {
            days.get(data.getIntExtra(DaysAdapter.flag, 0)).setMark(data.getIntExtra(DaysAdapter.mark, 0), this);
            InternetConnector.get().push(days.get(data.getIntExtra(DaysAdapter.flag, 0)));
            week.getAdapter().notifyItemChanged(data.getIntExtra(DaysAdapter.flag, 0));
        }
    }

    private void fillDaysIn(Calendar cldr) {
        System.out.println(cldr.get(Calendar.DAY_OF_WEEK));
        if (cldr.get(Calendar.DAY_OF_WEEK) > 1) {
            InternetConnector.get().setOnLoaded(new InternetConnector.onLoadedListener() {
                @Override
                public void onComplete(List<DocumentSnapshot> loaded, Calendar cl) {

                    days.add(new HomeWork( null,
                            cl.get(Calendar.DATE) + " " + cl.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()),
                            true));
                    for (int j = 0; j < loaded.size(); j++) {
                        HomeWork item = new HomeWork(loaded.get(j), null, false);
                        item.setMark(item.mark, EKScholar.this);
                        days.add(item);
                    }

                    cl.add(Calendar.DATE, 1);
                    fillDaysIn(cl);
                }
            }).loadByDate(cldr);
        } else {
            Log.e("TAG", "no data updated");
            cl = Calendar.getInstance();
            cl.setFirstDayOfWeek(Calendar.SUNDAY);
            adapter.notifyDataSetChanged();
            showProgress(false);
        }
    }

    private void showProgress(final boolean show) {
        progress.setVisibility(show ? View.VISIBLE : View.GONE);
        week.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_signout) {
            FirebaseAuth.getInstance().signOut();

            Intent signInIntent = new Intent();
            signInIntent.setClass(this, LoginActivity.class);
            startActivityForResult(signInIntent, loginCode);

            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}
