package com.silver5302.kakaologin2;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;

public class MatchingRegistActivity extends AppCompatActivity {

    CircleImageView imageView;
    TextView teamName,tvTime;
    RadioGroup radioGroup;
    EditText editTeamInform,editPhone;
    Button btnSelectTime;
    Toolbar toolbar;
    String date;
    RadioButton rb;
    ActionBar actionBar;
    String matchRegistURL="http://silver5302.dothome.co.kr/Team/matchRegist.php";
    String matchRegistInsertURL="http://silver5302.dothome.co.kr/Team/matchRegistInsert.php";
    String type=null;
    String time=null;
    String inform=null;
    String region=null;
    String phone=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matching_regist);

        Intent intent=getIntent();
        date=intent.getStringExtra("date");
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar=getSupportActionBar();
        actionBar.setTitle(date);
        actionBar.setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        imageView=(CircleImageView)findViewById(R.id.img);
        teamName=(TextView)findViewById(R.id.tv_teamname);
        radioGroup=(RadioGroup)findViewById(R.id.radio_group);
        editTeamInform=(EditText)findViewById(R.id.edit_teamIntroduce);
        editPhone=(EditText)findViewById(R.id.edit_phone);
        btnSelectTime=(Button)findViewById(R.id.btn_select_time);
        tvTime=(TextView)findViewById(R.id.tv_time);
        RequestQueue requestque=Volley.newRequestQueue(this);
        SimpleMultiPartRequest smpr=new SimpleMultiPartRequest(Request.Method.POST, matchRegistURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String[] str=response.split("&");

                if(str[0].equals("")){
                    Glide.with(MatchingRegistActivity.this).load(R.drawable.thumb_person).into(imageView);
                }else{
                    String ultimateImgUrl="http://silver5302.dothome.co.kr/Team/"+str[0];
                    Glide.with(MatchingRegistActivity.this).load(ultimateImgUrl).into(imageView);
                }
                teamName.setText(G.captainTeam);
                region=str[1];
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        smpr.addStringParam("teamName",G.captainTeam);
        requestque.add(smpr);




    }

    public void clickOk(View v){
        rb=(RadioButton)findViewById(radioGroup.getCheckedRadioButtonId());
        type=rb.getText().toString();
        inform=editTeamInform.getText().toString();
        phone=editPhone.getText().toString();
        if(type==null||time==null||inform==null||phone==null){
            Toast.makeText(this, "빈 칸을 모두 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        RequestQueue requestQue=Volley.newRequestQueue(this);
        SimpleMultiPartRequest smpr=new SimpleMultiPartRequest(Request.Method.POST, matchRegistInsertURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        smpr.addStringParam("teamName",G.captainTeam);
        smpr.addStringParam("type",type);
        smpr.addStringParam("time",time);
        smpr.addStringParam("inform",inform);
        smpr.addStringParam("date",date);
        smpr.addStringParam("region",region);
        smpr.addStringParam("phone",phone);
        requestQue.add(smpr);

        finish();
    }
    public void clickCancel(View v){
        finish();
    }
    public void clickSelectTime(View v){
        TimePickerDialog timePickerDialog=new TimePickerDialog(this,timeSetListener,15,24,false);
        timePickerDialog.show();

    }
    TimePickerDialog.OnTimeSetListener timeSetListener=new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            tvTime.setText(hourOfDay+"시 "+minute+"분");

            time=hourOfDay+"시"+minute+"분";
        }
    };
}
