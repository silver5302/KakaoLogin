package com.silver5302.kakaologin2;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
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
 * Created by alfo06-19 on 2017-08-25.
 */

public class MatchingListRecyclerAdapter extends RecyclerView.Adapter {
    Context context;
    ArrayList<MatchListItem> matchListItems;
    String matchInformImgURL="http://silver5302.dothome.co.kr/Team/matchInformImg.php";
    String matchOkURL="http://silver5302.dothome.co.kr/Team/matchOk.php";

    public MatchingListRecyclerAdapter(Context context, ArrayList<MatchListItem> matchListItems) {
        this.context = context;
        this.matchListItems = matchListItems;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.layout_recycler_matchlist_item,parent,false);

        MatchlistHolder holder=new MatchlistHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final MatchlistHolder holder1=(MatchlistHolder)holder;

        RequestQueue requestQueue=Volley.newRequestQueue(context);
        SimpleMultiPartRequest smpr=new SimpleMultiPartRequest(Request.Method.POST, matchInformImgURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals(""))return;
                String imgurl="http://silver5302.dothome.co.kr/Team/"+response;
                Glide.with(context).load(imgurl).into(holder1.imageView);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "에러", Toast.LENGTH_SHORT).show();
            }
        });
        smpr.addStringParam("teamName",G.captainTeam);
        requestQueue.add(smpr);

        holder1.teamName.setText(G.captainTeam);
        holder1.time.setText(matchListItems.get(position).date+" "+matchListItems.get(position).time);
        holder1.type.setText(matchListItems.get(position).type);
        holder1.region.setText(matchListItems.get(position).region);

    }

    @Override
    public int getItemCount() {
        return matchListItems.size();
    }

    class MatchlistHolder extends RecyclerView.ViewHolder{

        CircleImageView imageView;
        TextView teamName,type,region,time;

        public MatchlistHolder(View itemView) {
            super(itemView);

            imageView=(CircleImageView) itemView.findViewById(R.id.iv);
            teamName=(TextView) itemView.findViewById(R.id.tv_teamName);
            type=(TextView) itemView.findViewById(R.id.tv_type);
            region=(TextView) itemView.findViewById(R.id.tv_region);
            time=(TextView) itemView.findViewById(R.id.tv_time);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder dialog=new AlertDialog.Builder(context);
                    dialog.setTitle("매칭성사");
                    dialog.setMessage("매칭이 성사되었습니까?");
                    dialog.setPositiveButton("네", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            RequestQueue requestQueue=Volley.newRequestQueue(context);
                            SimpleMultiPartRequest smpr=new SimpleMultiPartRequest(Request.Method.POST, matchOkURL, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                }
                            });
                            smpr.addStringParam("teamName",G.captainTeam);
                            requestQueue.add(smpr);
                        }
                    });
                    dialog.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    dialog.create().show();
                }
            });
        }
    }

}

