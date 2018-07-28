package ekscholar.ekdorn.ekscholar;

import android.content.Context;
import android.content.Intent;
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

import com.google.common.collect.Lists;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class EKScholar extends AppCompatActivity {
    public static final int loginCode = 42;
    public static final int scrollingCode = 64;

    ProgressBar progress;
    LinearLayout terra;
    Button backButton;
    TextView date;
    Button forthButton;
    RecyclerView week;

    static DaysAdapter adapter;
    static ArrayList<HomeWork> days;

    int workingOn;

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

        if (days != null) System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" + Arrays.toString(days.toArray()));

        days = new ArrayList<>();
        adapter = new DaysAdapter(days);

        Calendar cl = Calendar.getInstance();
        cl.setFirstDayOfWeek(Calendar.SUNDAY);
        Log.e("sos ", String.valueOf(-cl.get(Calendar.DAY_OF_WEEK)));
        cl.add(Calendar.DATE, (-cl.get(Calendar.DAY_OF_WEEK) + 2));
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
            days.get(workingOn).setMark(data.getIntExtra(DaysAdapter.mark, 0), this);
            week.getAdapter().notifyItemChanged(workingOn);
        }
    }

    private void fillDaysIn(Calendar cl) {
        System.out.println(cl.get(Calendar.DAY_OF_WEEK));
        if (cl.get(Calendar.DAY_OF_WEEK) > 1) {
            InternetConnector.get(new InternetConnector.onLoadedListener() {
                @Override
                public void onComplete(List<DocumentSnapshot> loaded, Calendar cl) {

                    days.add(new HomeWork( null,
                            cl.get(Calendar.DATE) + " " + cl.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()),
                            true));
                    for (int j = 0; j < loaded.size(); j++) {
                        HomeWork item = new HomeWork(loaded.get(j), null, false);
                        item.setMark(0, EKScholar.this);
                        days.add(item);
                    }

                    cl.add(Calendar.DATE, 1);
                    fillDaysIn(cl);
                }

                @Override
                public void onComplete(Map<String, Object> loaded) {
                }
            }).loadByDate(cl);
        } else {
            Log.e("TAG", "no data updated");
            adapter.notifyDataSetChanged();
            showProgress(false);
        }
    }

    private void showProgress(final boolean show) {
        progress.setVisibility(show ? View.VISIBLE : View.GONE);
        week.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    public static ArrayList<HomeWork> getDays() {
        return days;
    }






    public class DaysAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        public static final String flag = "pos";
        public static final String mark = "mrk";

        public static final int SECTION_VIEW = 0;
        public static final int CONTENT_VIEW = 1;

        ArrayList<HomeWork> mHomeWorkList;

        public DaysAdapter(ArrayList<HomeWork> mHomeWorkList) {

            this.mHomeWorkList = mHomeWorkList;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == SECTION_VIEW) {
                return new SectionHeaderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_header_title, parent, false));
            } else {
                return new ItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_name, parent, false));
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (mHomeWorkList.get(position).isSection) {
                return SECTION_VIEW;
            } else {
                return CONTENT_VIEW;
            }
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

            Context context = EKScholar.this;
            if (context != null) {
                if (getItemViewType(position) == SECTION_VIEW) {
                    DaysAdapter.SectionHeaderViewHolder sectionHeaderViewHolder = (DaysAdapter.SectionHeaderViewHolder) holder;
                    HomeWork sectionItem = mHomeWorkList.get(position);
                    sectionHeaderViewHolder.headerTitleTextview.setText(sectionItem.name);
                } else {
                    ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
                    HomeWork current = mHomeWorkList.get(position);
                    itemViewHolder.nameTextview.setText(current.title);
                    itemViewHolder.subjectTextview.setText(current.name);
                    itemViewHolder.markTextview.setText(String.valueOf(current.mark));
                    itemViewHolder.markTextview.setBackgroundColor(current.back);
                }
            }
        }

        @Override
        public int getItemCount() {
            return mHomeWorkList.size();
        }



        public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            public TextView subjectTextview;
            public TextView nameTextview;
            public TextView markTextview;

            public ItemViewHolder(View itemView) {
                super(itemView);
                itemView.setOnClickListener(this);
                subjectTextview = (TextView) itemView.findViewById(R.id.subjectTextview);
                nameTextview = (TextView) itemView.findViewById(R.id.nameTextview);
                markTextview = (TextView) itemView.findViewById(R.id.markTextview);
            }

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(EKScholar.this, ScrollingActivity.class);
                intent.putExtra(flag, getAdapterPosition());
                workingOn = getAdapterPosition();
                EKScholar.this.startActivityForResult(intent, scrollingCode);
            }
        }

        public class SectionHeaderViewHolder extends RecyclerView.ViewHolder {
            TextView headerTitleTextview;

            public SectionHeaderViewHolder(View itemView) {
                super(itemView);
                headerTitleTextview = (TextView) itemView.findViewById(R.id.headerTitleTextview);
            }
        }
    }
}
