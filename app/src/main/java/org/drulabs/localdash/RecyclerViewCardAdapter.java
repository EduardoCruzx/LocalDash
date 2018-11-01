package org.drulabs.localdash;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class RecyclerViewCardAdapter extends RecyclerView.Adapter<RecyclerViewCardAdapter.ItemRowHolder>{

    private ArrayList<SectionModel> dataList;
    private Context mContext;
    private RecyclerView.RecycledViewPool recycledViewPool;

    public RecyclerViewCardAdapter(ArrayList<SectionModel> dataList, Context mContext) {
        this.dataList = dataList;
        this.mContext = mContext;
        recycledViewPool = new RecyclerView.RecycledViewPool();
    }

    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_cards, parent, false);
        ItemRowHolder rowHolder = new ItemRowHolder(v);
        return rowHolder;
    }

    @Override
    public void onBindViewHolder(ItemRowHolder holder, int position) {
        final String sectionName = dataList.get(position).getName();
        ArrayList singleSectionItems = dataList.get(position).getAllCardsInSection();
        holder.itemTitle.setText(sectionName);
        SectionListCardAdapter adapter = new SectionListCardAdapter(singleSectionItems, mContext);
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        holder.recyclerView.setAdapter(adapter);
        holder.recyclerView.setRecycledViewPool(recycledViewPool);
    }

    @Override
    public int getItemCount() {
        return (null != dataList ? dataList.size() : 0);
    }

    public class ItemRowHolder extends RecyclerView.ViewHolder {
        protected TextView itemTitle;
        protected RecyclerView recyclerView;

        public ItemRowHolder(View itemView) {
            super(itemView);
            this.itemTitle = itemView.findViewById(R.id.itemTitle);
            this.recyclerView = itemView.findViewById(R.id.recycler_view_list);
        }
    }
}