package com.silver5302.kakaologin2;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.DatePicker;
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

import java.util.Calendar;
import java.util.GregorianCalendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegistRecruitActivity extends AppCompatActivity {

    Toolbar toolbar;
    CircleImageView img;
    TextView tv_teamName,tv_date,tv_time;
    RadioGroup radioGroup;
    RadioButton rb;
    EditText edit_phone,edit_teamintroduce;
    String loadTeamImgURL="http://silver5302.dothome.co.kr/Team/loadTeamImg.php";

    String teamName=null;
    String type=null;
    String date=null;
    String time=null;
    String phone=null;
    String introduce=null;
    int year,month,day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist_recruit);

        toolbar=(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("용병모집");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        img=(CircleImageView)findViewById(R.id.img);
        tv_teamName=(TextView)findViewById(R.id.tv_teamname);
        radioGroup=(RadioGroup)findViewById(R.id.radio_group);
        tv_date=(TextView)findViewById(R.id.tv_date);
        tv_time=(TextView)findViewById(R.id.tv_time);
        edit_phone=(EditText)findViewById(R.id.edit_phone);
        edit_teamintroduce=(EditText)findViewById(R.id.edit_teamIntroduce);
        GregorianCalendar calendar = new GregorianCalendar();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day= calendar.get(Calendar.DAY_OF_MONTH);
        String date=String.format("%d년%02d월%02d일",year,month+1,day);
        tv_date.setText(date);



        RequestQueue requestQueue=Volley.newRequestQueue(this);
        SimpleMultiPartRequest smpr=new SimpleMultiPartRequest(Request.Method.GET, loadTeamImgURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals(""))return;
                String imgurl="http://silver5302.dothome.co.kr/Team/"+response;
                Glide.with(RegistRecruitActivity.this).load(imgurl).into(img);
                tv_teamName.setText(G.Team.get(0));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        smpr.addStringParam("teamName",G.Team.get(0));
        requestQueue.add(smpr);

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(G.Team.size()>1){

                    final CharSequence[] items =  G.Team.toArray(new String[ G.Team.size()]);

                    AlertDialog.Builder builder = new AlertDialog.Builder(RegistRecruitActivity.this);
                    builder.setTitle("팀선택");
                    builder.setItems(items, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int pos) {
                            teamName = items[pos].toString();

                            RequestQueue requestQueue=Volley.newRequestQueue(RegistRecruitActivity.this);
                            SimpleMultiPartRequest smpr=new SimpleMultiPartRequest(Request.Method.POST, loadTeamImgURL, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if(response.equals(""))return;
                                    String imgurl="http://silver5302.dothome.co.kr/Team/"+response;
                                    Glide.with(RegistRecruitActivity.this).load(imgurl).into(img);
                                    tv_teamName.setText(teamName);
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                }
                            });
                            smpr.addStringParam("teamName",teamName);
                            requestQueue.add(smpr);


                        }
                    });
                    builder.show();

                }else{
                    Toast.makeText(RegistRecruitActivity.this, "변경할 팀이 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });





    }

    public void clickSelectDate(View v){
        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                date=String.format("%d년%02d월%02d일",year,month+1,dayOfMonth);
                tv_date.setText(date);

            }
        }, year, month, day).show();

    }
    public void clickSelectTime(View v){
        TimePickerDialog timePickerDialog=new TimePickerDialog(this,timeSetListener,15,24,false);
        timePickerDialog.show();

    }
    TimePickerDialog.OnTimeSetListener timeSetListener=new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            tv_time.setText(hourOfDay+"시 "+minute+"분");

            time=hourOfDay+"시"+minute+"분";
        }
    };

    public void clickOk(View v){


    }
    public void clickCancel(View v){

    }
}
