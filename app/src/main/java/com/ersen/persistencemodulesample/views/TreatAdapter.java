package com.ersen.persistencemodulesample.views;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ersen.persistencemodulesample.R;
import com.ersen.persistencemodulesample.models.Treat;

import java.util.ArrayList;


public class TreatAdapter extends RecyclerView.Adapter<TreatAdapter.TreatViewHolder>{

    private ArrayList<Treat> mTreats;
    private Context mContext;

    public TreatAdapter(Context mContext, ArrayList<Treat> mTreats) {
        this.mContext = mContext;
        this.mTreats = mTreats;
    }

    @Override
    public TreatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TreatViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_treat,parent,false));
    }

    @Override
    public void onBindViewHolder(TreatViewHolder holder, int position) {
        Treat treat = mTreats.get(position);
        holder.getTreatName().setText(treat.getName());
        Glide.with(mContext)
                .load(treat)
                .centerCrop()
                .into(holder.getTreatImage());
    }

    @Override
    public int getItemCount() {
        return mTreats.size();
    }

    public static class TreatViewHolder extends RecyclerView.ViewHolder {

        private TextView mTreatName;
        private ImageView mTreatImage;

        public TreatViewHolder(View itemView) {
            super(itemView);
            mTreatName = (TextView)itemView.findViewById(R.id.text_treat_name);
            mTreatImage = (ImageView)itemView.findViewById(R.id.image_treat_picture);
        }

        public TextView getTreatName() {
            return mTreatName;
        }

        public ImageView getTreatImage() {
            return mTreatImage;
        }
    }
}
