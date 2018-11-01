package org.drulabs.localdash;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import org.drulabs.localdash.model.CardModel;

import java.util.ArrayList;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class SectionListCardAdapter extends RecyclerView.Adapter<SectionListCardAdapter.SingleItemRowHolder>{

    private ArrayList<CardModel> itemModels;
    private Context mContext;
    Dialog myDialog;

    public SectionListCardAdapter(ArrayList<CardModel> itemModels, Context mContext) {
        this.itemModels = itemModels;
        this.mContext = mContext;
    }

    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_single_card, null);
        final SingleItemRowHolder singleItemRowHolder = new SingleItemRowHolder(v);

        myDialog = new Dialog(mContext);
        myDialog.setContentView(R.layout.card_layout);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        singleItemRowHolder.singleCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout layout = myDialog.findViewById(R.id.layout_card);
                TextView cardPower = myDialog.findViewById(R.id.cardPower);
                TextView cardName = myDialog.findViewById(R.id.cardName);
                TextView cardDesc = myDialog.findViewById(R.id.cardDesc);
                TextView cardBadstuff = myDialog.findViewById(R.id.cardBadStuff);

                CardModel card = itemModels.get(singleItemRowHolder.getAdapterPosition());
                cardName.setText(card.getName());

                if (card.getType() == CardModel.ENEMY){
                    cardPower.setText("Level " + card.getPower());
                    cardDesc.setText(card.getDescription());
                    cardBadstuff.setText("Bad stuff: " + itemModels.get(singleItemRowHolder.getAdapterPosition()).getBadstuff());
                }else{
                    cardPower.setText("Bonus " + card.getBonus());
                    cardDesc.setText(card.getDescription());
                    cardBadstuff.setVisibility(View.GONE);
                }

                layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myDialog.dismiss();
                    }
                });

                myDialog.show();
            }
        });

        singleItemRowHolder.singleCard.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(mContext instanceof MainActivity){
                    CardModel card = itemModels.get(singleItemRowHolder.getAdapterPosition());
                    ((MainActivity)mContext).equipItem(card);
                    return true;
                }
                return false;
            }
        });

        return singleItemRowHolder;
    }

    @Override
    public void onBindViewHolder(SingleItemRowHolder holder, int position) {
        CardModel itemModel = itemModels.get(position);
        holder.tvTitle.setText(itemModel.getName());
    }

    @Override
    public int getItemCount() {
        return (null != itemModels ? itemModels.size() : 0);
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        protected TextView tvTitle;
        protected String toast;
        protected ImageView itemImage;
        protected LinearLayout singleCard;

        public SingleItemRowHolder(View itemView) {
            super(itemView);
            this.singleCard = itemView.findViewById(R.id.singleCard);
            this.tvTitle = itemView.findViewById(R.id.tvTitle);
            this.itemImage = itemView.findViewById(R.id.itemImage);
        }
    }

}