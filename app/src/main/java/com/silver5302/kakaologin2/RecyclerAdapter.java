package com.silver5302.kakaologin2;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by alfo06-19 on 2017-08-09.
 */

public class RecyclerAdapter extends RecyclerView.Adapter {

    ArrayList<TeamList> teamLists;
    Context context;

    public RecyclerAdapter(ArrayList<TeamList> teamLists, Context context) {
        this.teamLists = teamLists;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.layout_recycler_search_item,parent,false);

        MyHolder holder=new MyHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyHolder h = (MyHolder)holder;


        h.teamName.setText(teamLists.get(position).name);
        h.teamType.setText(teamLists.get(position).type);
        h.teamRegion.setText(teamLists.get(position).region);
        if(teamLists.get(position).img==null){
            Glide.with(context).load(R.drawable.thumb_person).into(h.iv);
        }else{
            String s="http://silver5302.dothome.co.kr/Team/"+teamLists.get(position).img;
            Glide.with(context).load(s).into(h.iv);
        }







    }

    @Override
    public int getItemCount() {
        return teamLists.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{

        TextView teamName,teamType,teamRegion;
        CircleImageView iv;

        public MyHolder(View itemView) {
            super(itemView);
            iv=(CircleImageView)itemView.findViewById(R.id.img_team);
            teamName=(TextView) itemView.findViewById(R.id.tv_teamname);
            teamType=(TextView) itemView.findViewById(R.id.tv_teamtype);
            teamRegion=(TextView)itemView.findViewById(R.id.tv_teamregion);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context,RegistMemberActivity.class);
                    intent.putExtra("name",teamLists.get(getLayoutPosition()).name);
                    intent.putExtra("inform",teamLists.get(getLayoutPosition()).inform);
                    intent.putExtra("img",teamLists.get(getLayoutPosition()).img);
                    intent.putExtra("type",teamLists.get(getLayoutPosition()).type);
                    context.startActivity(intent);

                }
            });


        }
    }
}
