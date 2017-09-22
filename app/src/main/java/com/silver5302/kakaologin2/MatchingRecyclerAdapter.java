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
 * Created by alfo06-19 on 2017-08-23.
 */

public class MatchingRecyclerAdapter extends RecyclerView.Adapter {

    Context context;
    ArrayList<MatchingTeamItem> matchingTeamItems;

    public MatchingRecyclerAdapter(Context context, ArrayList<MatchingTeamItem> matchingTeamItems) {
        this.context = context;
        this.matchingTeamItems = matchingTeamItems;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.layout_recycler_matching_item,parent,false);
        MatchingHolder holder=new MatchingHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MatchingHolder matchingHolder = (MatchingHolder)holder;
        matchingHolder.region.setText(matchingTeamItems.get(position).region);
        String time=matchingTeamItems.get(position).date+" "+matchingTeamItems.get(position).time;
        matchingHolder.time.setText(time);
        matchingHolder.teamName.setText(matchingTeamItems.get(position).teamName);
        matchingHolder.type.setText(matchingTeamItems.get(position).type);
        if(matchingTeamItems.get(position).isMatch.equals("0")){
            matchingHolder.isMatch.setText("대기중");
        }else if(matchingTeamItems.get(position).isMatch.equals("1")){
            matchingHolder.isMatch.setText("매칭완료");
        }
    }

    @Override
    public int getItemCount() {
        return matchingTeamItems.size();
    }
    class MatchingHolder extends RecyclerView.ViewHolder{

        TextView region,time,teamName,type,isMatch;

        public MatchingHolder(View itemView) {
            super(itemView);
            region=(TextView) itemView.findViewById(R.id.tv_region);
            time=(TextView) itemView.findViewById(R.id.tv_time);
            teamName=(TextView) itemView.findViewById(R.id.tv_teamName);
            type=(TextView) itemView.findViewById(R.id.tv_type);
            isMatch=(TextView) itemView.findViewById(R.id.tv_isMatch);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context,SelectedMatchingActivity.class);
                    intent.putExtra("teamName",matchingTeamItems.get(getLayoutPosition()).teamName);
                    intent.putExtra("date",matchingTeamItems.get(getLayoutPosition()).date);
                    intent.putExtra("time",matchingTeamItems.get(getLayoutPosition()).time);
                    intent.putExtra("type",matchingTeamItems.get(getLayoutPosition()).type);
                    intent.putExtra("inform",matchingTeamItems.get(getLayoutPosition()).inform);
                    intent.putExtra("region",matchingTeamItems.get(getLayoutPosition()).region);
                    intent.putExtra("phone",matchingTeamItems.get(getLayoutPosition()).phone);
                    context.startActivity(intent);
                }
            });
        }
    }
}
