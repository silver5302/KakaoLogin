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

public class SupportRecyclerAdapter extends RecyclerView.Adapter {

    ArrayList<SupportItem> supportItems;
    Context context;

    public SupportRecyclerAdapter(ArrayList<SupportItem> supportItems, Context context) {
        this.supportItems = supportItems;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.layout_recycler_support_item,parent,false);

        MyHolder holder=new MyHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder=(MyHolder)holder;
        myHolder.tv_nickname.setText(supportItems.get(position).nickname);
        myHolder.tv_date.setText(supportItems.get(position).date);
        myHolder.tv_region.setText(supportItems.get(position).region);
        myHolder.tv_type.setText(supportItems.get(position).type);
    }

    @Override
    public int getItemCount() {
        return supportItems.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{

        TextView tv_nickname,tv_date,tv_region,tv_type;

        public MyHolder(View itemView) {
            super(itemView);

            tv_nickname=(TextView) itemView.findViewById(R.id.tv_nickName);
            tv_region=(TextView) itemView.findViewById(R.id.tv_region);
            tv_date=(TextView) itemView.findViewById(R.id.tv_date);
            tv_type=(TextView) itemView.findViewById(R.id.tv_type);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context,SelectedSupportActivity.class);
                    intent.putExtra("nickname",supportItems.get(getLayoutPosition()).nickname);
                    intent.putExtra("type",supportItems.get(getLayoutPosition()).type);
                    intent.putExtra("date",supportItems.get(getLayoutPosition()).date);
                    intent.putExtra("region",supportItems.get(getLayoutPosition()).region);
                    intent.putExtra("introduce",supportItems.get(getLayoutPosition()).introduce);
                    intent.putExtra("phone",supportItems.get(getLayoutPosition()).phone);
                    context.startActivity(intent);
                }
            });

        }
    }
}
