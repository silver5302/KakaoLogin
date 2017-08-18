package com.silver5302.kakaologin;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.IdRes;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegistActivity extends AppCompatActivity {

    CircleImageView imageView;
    TextView tv_teamName,tv_teamInform,tv_teamType;
    EditText edit_name,edit_introduce;
    Toolbar toolbar;
    RadioGroup radiogroup;
    String teamName;
    String teamImg;
    RadioButton rb;
    String name,age,introduce;


    String memberinsertUrl="http://silver5302.dothome.co.kr/Team/insertDB.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);

        toolbar=(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              finish();
            }
        });

        imageView=(CircleImageView)findViewById(R.id.imgview);
        tv_teamName=(TextView)findViewById(R.id.tv_teamname);
        tv_teamType=(TextView)findViewById(R.id.tv_type);
        tv_teamInform=(TextView)findViewById(R.id.tv_inform);

        edit_name=(EditText)findViewById(R.id.edit_Name);
        edit_introduce=(EditText)findViewById(R.id.edit_introduce);
        radiogroup=(RadioGroup)findViewById(R.id.radio_group);


        Intent intent=getIntent();
        teamName=intent.getStringExtra("name");
        String teamInform=intent.getStringExtra("inform");
        teamImg=intent.getStringExtra("img");
        String realTeamImg="http://silver5302.dothome.co.kr/Team/"+teamImg;
        String teamType=intent.getStringExtra("type");


        tv_teamType.setText(teamType);
        tv_teamName.setText(teamName);
        tv_teamInform.setText(teamInform);
        if(teamImg!=null) Glide.with(this).load(realTeamImg).into(imageView);


    }

    public void clickOk(View v){
        rb=(RadioButton) RegistActivity.this.findViewById(radiogroup.getCheckedRadioButtonId());

        name=edit_name.getText().toString();
        age=rb.getText().toString();
        introduce=edit_introduce.getText().toString();


        if(name.equals("")||introduce.equals("")){
            Toast.makeText(this, "빈칸을 채워주세요.", Toast.LENGTH_SHORT).show();
        }else{
            RequestQueue requestQueue=Volley.newRequestQueue(RegistActivity.this);
            SimpleMultiPartRequest smpr=new SimpleMultiPartRequest(Request.Method.POST, memberinsertUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Toast.makeText(RegistActivity.this, response, Toast.LENGTH_SHORT).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(RegistActivity.this, "에러!!", Toast.LENGTH_SHORT).show();
                }
            });

            smpr.addStringParam("teamName",teamName);
            smpr.addStringParam("userId",G.userId);
            smpr.addStringParam("nickname",G.nickName);
            smpr.addStringParam("name",name);
            smpr.addStringParam("age",age);
            smpr.addStringParam("introduce",introduce);
            smpr.addStringParam("teamImg",teamImg);

            requestQueue.add(smpr);

            SQLiteDatabase db=openOrCreateDatabase("teams.db", Context.MODE_PRIVATE,null);
            db.execSQL("insert into teams(teamName,isCaptain,isJoin) values(?,?,?)",new String[]{teamName,"0","0"});
            db.close();

            finish();

        }


    }

    public void clickCancel(View v){
        finish();
    }


}
