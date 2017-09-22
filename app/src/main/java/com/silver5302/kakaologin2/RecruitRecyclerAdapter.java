package com.silver5302.kakaologin2;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by alfo06-19 on 2017-09-22.
 */

public class RecruitRecyclerAdapter extends RecyclerView.Adapter {

    ArrayList<RecruitItem> recruitItems;
    Context context;

    public RecruitRecyclerAdapter(ArrayList<RecruitItem> recruitItems, Context context) {
        this.recruitItems = recruitItems;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.layout_recycler_recruit_item,parent,false);

        MyHolder holder=new MyHolder(view);


        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        MyHolder myHolder=(MyHolder)holder;
        myHolder.tv_teamName.setText(recruitItems.get(position).teamName);
        myHolder.tv_Type.setText(recruitItems.get(position).type);
        myHolder.tv_Region.setText(recruitItems.get(position).region);
        myHolder.tv_teamTime.setText(recruitItems.get(position).date+" "+recruitItems.get(position).time);

    }

    @Override
    public int getItemCount() {
        return recruitItems.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{

        TextView tv_teamName,tv_teamTime,tv_Region,tv_Type;
        public MyHolder(View itemView) {
            super(itemView);

            tv_teamName=(TextView)itemView.findViewById(R.id.tv_teamName);
            tv_teamTime=(TextView)itemView.findViewById(R.id.tv_time);
            tv_Region=(TextView)itemView.findViewById(R.id.tv_region);
            tv_Type=(TextView)itemView.findViewById(R.id.tv_type);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context,SelectedRecruitActivity.class);
                    intent.putExtra("teamname",recruitItems.get(getLayoutPosition()).teamName);
                    intent.putExtra("type",recruitItems.get(getLayoutPosition()).type);
                    intent.putExtra("time",recruitItems.get(getLayoutPosition()).time);
                    intent.putExtra("date",recruitItems.get(getLayoutPosition()).date);
                    intent.putExtra("introduce",recruitItems.get(getLayoutPosition()).introduce);
                    intent.putExtra("phone",recruitItems.get(getLayoutPosition()).phone);
                    intent.putExtra("region",recruitItems.get(getLayoutPosition()).region);
                    context.startActivity(intent);
                }
            });

        }
    }
}
