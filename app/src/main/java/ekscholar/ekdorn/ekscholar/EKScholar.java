package ekscholar.ekdorn.ekscholar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class EKScholar extends AppCompatActivity {

    ProgressBar progress;
    LinearLayout terra;
    Button backButton;
    TextView date;
    Button forthButton;
    RecyclerView week;

    DaysAdapter adapter;
    ArrayList<DaysAdapter.CountriesModel> days;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Intent signInIntent = new Intent();
            signInIntent.setClass(this, LoginActivity.class);
            startActivity(signInIntent);
        }

        setContentView(R.layout.activity_ekscholar);

        progress = (ProgressBar) findViewById(R.id.progress);
        terra = (LinearLayout) findViewById(R.id.terra);
        backButton = (Button) findViewById(R.id.button_back);
        date = (TextView) findViewById(R.id.date);
        forthButton = (Button) findViewById(R.id.button_forth);
        week = (RecyclerView) findViewById(R.id.week);

        days = new ArrayList<>();
        adapter = new DaysAdapter(days, this);

        Calendar cl = Calendar.getInstance();
        cl.setFirstDayOfWeek(Calendar.MONDAY);
        cl.add(Calendar.DATE, -Calendar.getInstance().get(Calendar.DAY_OF_WEEK) + 1);
        fillDaysIn(cl);

        week.setLayoutManager(new LinearLayoutManager(this));
        week.setAdapter(adapter);

        showProgress(true);
    }

    private void fillDaysIn(Calendar cl) {
        if (cl.get(Calendar.DAY_OF_WEEK) <= 6) {
            InternetConnector.get(new InternetConnector.onLoadedListener() {
                @Override
                public void onComplete(List<DocumentSnapshot> loaded, Calendar cl) {

                    days.add(new DaysAdapter.CountriesModel(
                            cl.get(Calendar.DATE) + " " + cl.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()),
                            true));
                    for (int j = 0; j < loaded.size(); j++) {
                        days.add(new DaysAdapter.CountriesModel(loaded.get(j).get("title").toString(), false));
                    }

                    cl.add(Calendar.DATE, 1);
                    fillDaysIn(cl);
                }

                @Override
                public void onComplete(Map<String, Object> loaded) {
                }
            }).loadByDate(cl);
        } else {
            adapter.notifyDataSetChanged();
            showProgress(false);
        }
    }

    private void showProgress(final boolean show) {
        progress.setVisibility(show ? View.VISIBLE : View.GONE);
        week.setVisibility(show ? View.GONE : View.VISIBLE);
    }

}
