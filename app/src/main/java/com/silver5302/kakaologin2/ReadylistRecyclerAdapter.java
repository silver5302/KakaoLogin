package com.silver5302.kakaologin2;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by alfo06-19 on 2017-08-17.
 */

public class ReadylistRecyclerAdapter extends RecyclerView.Adapter {

    Context context;
    ArrayList<ReadyMember> readyMembers;
    String teamPermissonUrl="http://silver5302.dothome.co.kr/Team/teamPermission.php";

    public ReadylistRecyclerAdapter(Context context, ArrayList<ReadyMember> readyMembers) {
        this.context = context;
        this.readyMembers = readyMembers;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.layout_recycler_registlist_item,parent,false);

        MyHolder myHolder=new MyHolder(view);

        return myHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        MyHolder myHolder=(MyHolder)holder;

        myHolder.tvName.setText(readyMembers.get(position).name);
        myHolder.tvAge.setText(readyMembers.get(position).age);
        myHolder.tvInform.setText(readyMembers.get(position).introduce);
        String imgUrl=readyMembers.get(position).teamImg;
        if(imgUrl!=null){
            String ultimateImgUrl="http://silver5302.dothome.co.kr/Team/"+imgUrl;
            Glide.with(context).load(ultimateImgUrl).into(myHolder.imageView);
        }else{
            Glide.with(context).load(R.drawable.thumb_person).into(myHolder.imageView);
        }
    }

    @Override
    public int getItemCount() {
        return readyMembers.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{

        CircleImageView imageView;
        TextView tvName,tvAge,tvInform;

        public MyHolder(View itemView) {
            super(itemView);

            tvName=(TextView) itemView.findViewById(R.id.tv_name);
            tvAge=(TextView) itemView.findViewById(R.id.tv_age);
            tvInform=(TextView) itemView.findViewById(R.id.tv_inform);
            imageView=(CircleImageView) itemView.findViewById(R.id.iv);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder dialog=new AlertDialog.Builder(context);
                    dialog.setTitle("팀원수락");
                    dialog.setMessage("팀원으로 받아들이시겠습니까?");
                    dialog.setPositiveButton("수락", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            RequestQueue requestQue=Volley.newRequestQueue(context);
                            SimpleMultiPartRequest smpr=new SimpleMultiPartRequest(Request.Method.POST, teamPermissonUrl, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.e("dddd",response);
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                }
                            });
                            smpr.addStringParam("userId",readyMembers.get(getLayoutPosition()).userId);
                            smpr.addStringParam("teamName",G.captainTeam);
                            requestQue.add(smpr);
                        }
                    });
                    dialog.setNegativeButton("거절", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(context, "거절", Toast.LENGTH_SHORT).show();
                        }
                    });

                    dialog.create().show();

                }
            });

        }
    }
}
