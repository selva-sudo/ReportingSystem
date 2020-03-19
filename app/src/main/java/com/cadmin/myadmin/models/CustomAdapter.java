package com.cadmin.myadmin.models;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cadmin.myadmin.R;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private List<ComplaintModel> dataSet;
    private Context mContext;
    private IItemClickListener mListener;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName;
        TextView textViewVersion;
        TextView textViewStatus;
        ImageView imageViewIcon;
        TextView textViewId;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.textViewName = (TextView) itemView.findViewById(R.id.textViewName);
            this.textViewVersion = (TextView) itemView.findViewById(R.id.textViewVersion);
            this.imageViewIcon = (ImageView) itemView.findViewById(R.id.imageView);
            this.textViewStatus = (TextView) itemView.findViewById(R.id.textVieStatus);
            this.textViewId = (TextView) itemView.findViewById(R.id.textViewId);
        }
    }

    public CustomAdapter(List<ComplaintModel> data, Context context, Context mainActivity) {
        this.dataSet = data;
        this.mContext = context;
        this.mListener = (IItemClickListener) mainActivity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cards_layout, parent, false);


        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        TextView textViewName = holder.textViewName;
        TextView textViewVersion = holder.textViewVersion;
        ImageView imageView = holder.imageViewIcon;
        TextView textViewStatus = holder.textViewStatus;
        TextView textViewId = holder.textViewId;

        textViewName.setText("Complaint: "+dataSet.get(listPosition).getComplaint());
        textViewVersion.setText("Area: "+dataSet.get(listPosition).getArea());
        textViewStatus.setText(dataSet.get(listPosition).getStatus());
        textViewId.setText("ID: "+dataSet.get(listPosition).getComplaintId());

        Glide.with(mContext)
                .load(dataSet.get(listPosition).getImageUrl())
                .into(imageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener!=null){
                    mListener.onItemClick(dataSet.get(listPosition));
                }
            }
        });
        //imageView.setImageResource(dataSet.get(listPosition).getImageUrl());
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public interface IItemClickListener{
         void onItemClick(ComplaintModel pos);
    }

    public void updateList(List<ComplaintModel> data){
        this.dataSet = new ArrayList<>();
        this.dataSet = data;
        notifyDataSetChanged();
    }
}
