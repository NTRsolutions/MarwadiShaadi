package com.example.sid.marwadishaadi.Dashboard_Reverse_Matching;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sid.marwadishaadi.R;

import java.util.List;

/**
 * Created by Sid on 31-May-17.
 */

public class ReverseAdapter extends RecyclerView.Adapter<ReverseAdapter.MyViewHolder> {

    private List<ReverseModel> reverseModelList;
    private Context context;

    public ReverseAdapter(List<ReverseModel> reverseModelList, Context context) {
        this.reverseModelList = reverseModelList;
        this.context = context;
    }


    @Override
    public ReverseAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reverse_row, parent, false);
        return new ReverseAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ReverseModel rev = reverseModelList.get(position);
        Glide.with(context)
                .load(rev.getImg_url())
                .into(holder.dp);
        holder.name.setText(rev.getName());
        holder.age.setText(String.valueOf(rev.getAge()) + " yrs");
    }


    @Override
    public int getItemCount() {
        return reverseModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        public ImageView dp;
        public TextView name;
        public TextView age;

        public MyViewHolder(View itemView) {
            super(itemView);

            dp = (ImageView) itemView.findViewById(R.id.user_profile_img);
            name = (TextView) itemView.findViewById(R.id.user_profile_name);
            age = (TextView) itemView.findViewById(R.id.user_profile_age);

        }

    }
}
