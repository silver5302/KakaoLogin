package com.silver5302.kakaologin2;

import android.app.DatePickerDialog;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class RegistSupportActivity extends AppCompatActivity {

    Toolbar toolbar;
    RadioGroup radioGroup;
    RadioButton rb;
    TextView tv_date;
    Spinner spinner;
    EditText edit_phone,edit_introduce;
    ArrayAdapter adapter;

    String supportInsertURL="http://silver5302.dothome.co.kr/Team/supportInsert.php";
    String type=null;
    String date=null;
    String region=null;
    String introduce=null;
    String phone=null;
    int year,month,day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist_support);

        toolbar=(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("용병지원");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        GregorianCalendar calendar = new GregorianCalendar();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day= calendar.get(Calendar.DAY_OF_MONTH);

        radioGroup=(RadioGroup)findViewById(R.id.radio_group);
        tv_date=(TextView)findViewById(R.id.tv_date);
        spinner=(Spinner)findViewById(R.id.spinner);
        edit_phone=(EditText)findViewById(R.id.edit_phone);
        edit_introduce=(EditText)findViewById(R.id.edit_introduce);

        String date=String.format("%d년%02d월%02d일",year,month+1,day);
        tv_date.setText(date);

        adapter = ArrayAdapter.createFromResource(this, R.array.datas_location, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                region=parent.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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

    public void clickOk(View v){
        rb=(RadioButton)findViewById(radioGroup.getCheckedRadioButtonId());
        type=rb.getText().toString();
        phone=edit_phone.getText().toString();
        introduce=edit_introduce.getText().toString();

        if(region.contains("선택")||edit_introduce.getText().toString().equals("")||edit_phone.getText().toString().equals("")){
            Toast.makeText(this, "빈 칸을 모두 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }else{
            RequestQueue requestque=Volley.newRequestQueue(this);
            SimpleMultiPartRequest smpr=new SimpleMultiPartRequest(Request.Method.POST,supportInsertURL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Toast.makeText(RegistSupportActivity.this, response, Toast.LENGTH_SHORT).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });

            smpr.addStringParam("type",type);
            smpr.addStringParam("region",region);
            smpr.addStringParam("phone",phone);
            smpr.addStringParam("introduce",introduce);
            smpr.addStringParam("date",date);
            smpr.addStringParam("nickname",G.nickName);
            requestque.add(smpr);

            finish();
        }
    }
    public void clickCancel(View v){

        finish();
    }
}
