package ekscholar.ekdorn.ekscholar;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class DaysAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final String flag = "pos";
    public static final String mark = "mrk";
    public static final String home = "hw";

    public static final int SECTION_VIEW = 0;
    public static final int CONTENT_VIEW = 1;

    ArrayList<HomeWork> mHomeWorkList;
    WeakReference<EKScholar> ref;

    public DaysAdapter(ArrayList<HomeWork> mHomeWorkList, EKScholar eks) {
        this.ref = new WeakReference<>(eks);
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

        Context context = ref.get();
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
            intent.setClass(DaysAdapter.this.ref.get(), ScrollingActivity.class);
            intent.putExtra(flag, getAdapterPosition());
            intent.putExtra(home, DaysAdapter.this.ref.get().days.get(getAdapterPosition()));
            DaysAdapter.this.ref.get().startActivityForResult(intent, EKScholar.scrollingCode);
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
