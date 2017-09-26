package com.silver5302.kakaologin2;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;

public class MakeTeamActivity extends AppCompatActivity {

    Spinner spinner;
    Spinner spinner2;
    CircleImageView iv;
    ArrayAdapter adapter, adapter2;
    RadioGroup radioGroup;
    InputMethodManager imm;
    LinearLayout linearLayout;
    Toolbar toolbar;
    ActionBar actionBar;

    Uri imgUri = null;
    String imgPath;
    RadioButton rb;


    Button btnOk, btnCanel;
    EditText editName, editInform;
    String name, type, region, number, inform, nickname,userId;



    String uploadImgUrl = "http://silver5302.dothome.co.kr/Team/uploadImg.php";

    final int REQUEST_IMAGE_PICK = 10;
    final int REQ_PERMI_EXTERNALCONTENT = 11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_team);

        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar=getSupportActionBar();
        actionBar.setTitle("팀만들기");
        actionBar.setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        nickname = G.nickName;
        userId=G.userId;
        editInform = (EditText) findViewById(R.id.edit_teamIntroduce);
        editName = (EditText) findViewById(R.id.edit_teamName);
        radioGroup = (RadioGroup) findViewById(R.id.radio_group);
        linearLayout = (LinearLayout) findViewById(R.id.linear);
        iv = (CircleImageView) findViewById(R.id.imgview);
        imm = ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE));

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imm.hideSoftInputFromWindow(editName.getWindowToken(), 0);
//                imm.hideSoftInputFromWindow(editInform.getWindowToken(), 0);
            }
        });

        spinner = (Spinner) findViewById(R.id.spinner);
        spinner2 = (Spinner) findViewById(R.id.spinner2);
        adapter = ArrayAdapter.createFromResource(this, R.array.datas_location, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner.setAdapter(adapter);

        adapter2 = ArrayAdapter.createFromResource(this, R.array.datas_number,R.layout.spinner_item);
        adapter2.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner2.setAdapter(adapter2);

        spinner.setOnItemSelectedListener(listener);
        spinner2.setOnItemSelectedListener(listener);

        btnOk = (Button) findViewById(R.id.btn_Ok);
        btnCanel = (Button) findViewById(R.id.btn_Cancel);

        btnOk.setOnClickListener(clicklistener);
        btnCanel.setOnClickListener(clicklistener);

        iv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    int checkPermisson = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
                    if (checkPermisson == PackageManager.PERMISSION_DENIED) {
                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQ_PERMI_EXTERNALCONTENT);
                    }
                }

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_IMAGE_PICK);


            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQ_PERMI_EXTERNALCONTENT:
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "권한이 없어 사진등록불가.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_IMAGE_PICK:
                if (resultCode == RESULT_CANCELED) return;
                imgUri = data.getData();
                imgPath = imgUri.toString();
                Glide.with(this).load(imgUri).into(iv);

                break;
        }

    }


    AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            if (parent.getId() == R.id.spinner) {
                region = parent.getSelectedItem().toString();
            } else if (parent.getId() == R.id.spinner2) {
                number = parent.getSelectedItem().toString();

            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    View.OnClickListener clicklistener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (v.getId() == R.id.btn_Ok) {

                name = editName.getText().toString();
                inform = editInform.getText().toString();
                rb = (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());
                type = rb.getText().toString();


                //서버에 php로 데이터 보내기
                if (editName.getText().toString().equals("") || editInform.getText().toString().equals("")||region.contains("선택")||number.contains("선택")) {
                    Toast.makeText(MakeTeamActivity.this, "빈 칸을 채워주세요.", Toast.LENGTH_SHORT).show();
                } else {
////                    new Thread() {
////                        @Override
////                        public void run() {
////                            super.run();
////                            try {
////                                nickname = URLEncoder.encode(nickname, "utf-8");
////                                name = URLEncoder.encode(name, "utf-8");
////                                region = URLEncoder.encode(region, "utf-8");
////                                type = URLEncoder.encode(type, "utf-8");
////                                number = URLEncoder.encode(number, "utf-8");
////                                inform = URLEncoder.encode(inform, "utf-8");
////                            } catch (UnsupportedEncodingException e) {
////                                e.printStackTrace();
////                            }
////
////                            try {
////                                URL url = new URL(insertUrl);
////                                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
////                                conn.setRequestMethod("POST");
////                                conn.setDoInput(true);
////                                conn.setDoOutput(true);
////                                conn.setUseCaches(false);
////
////                                String data = "name=" + name + "&type=" + type + "&region=" + region + "&number=" + number + "&inform=" + inform + "&nickname=" + nickname;
////                                OutputStream os = conn.getOutputStream();
////                                os.write(data.getBytes());
////                                os.flush();
////                                os.close();
////
////                                InputStream is = conn.getInputStream();
////                                InputStreamReader isr = new InputStreamReader(is);
////                                BufferedReader reader = new BufferedReader(isr);
////                                final StringBuffer buffer = new StringBuffer();
////                                String line = reader.readLine();
////                                while (line != null) {
////                                    buffer.append(line);
////                                    line = reader.readLine();
////                                }
////                                getActivity().runOnUiThread(new Runnable() {
////                                    @Override
////                                    public void run() {
////                                        Toast.makeText(getContext(), buffer.toString(), Toast.LENGTH_SHORT).show();
////
////                                    }
////                                });
////
////                            } catch (MalformedURLException e) {
////                                e.printStackTrace();
////                            } catch (IOException e) {
////                                e.printStackTrace();
////                            }
//
//
//                        }
//                    }.start();


                    //웹으로 전송하기.
                    RequestQueue requestQue = Volley.newRequestQueue(MakeTeamActivity.this);
                    SimpleMultiPartRequest smpr = new SimpleMultiPartRequest(Request.Method.POST, uploadImgUrl, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Toast.makeText(MakeTeamActivity.this, response, Toast.LENGTH_SHORT).show();
                            G.isCaptain=response;

                            editName.setText("");
                            editInform.setText("");

                            finish();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Toast.makeText(MakeTeamActivity.this, "에러!!!", Toast.LENGTH_SHORT).show();
                        }
                    });

                    smpr.addStringParam("name", name);
                    smpr.addStringParam("type", type);
                    smpr.addStringParam("region", region);
                    smpr.addStringParam("number", number).addStringParam("inform", inform).addStringParam("nickname", nickname);
                    smpr.addStringParam("userId",userId);
                    if(imgPath!=null) {
                        if (imgPath.contains("content://")) {

                            //이미지경로가 db로 저장되어있다는것이다.

                            //uri를 통하여 절대경로 알아오기.
                            ContentResolver resolver = getContentResolver();
                            Cursor cursor = resolver.query(imgUri, null, null, null, null);
                            cursor.moveToFirst();
                            imgPath = cursor.getString(cursor.getColumnIndex("_data"));
                            smpr.addFile("upload", imgPath);
                        }
                    }
                    requestQue.add(smpr);

                    SQLiteDatabase db=openOrCreateDatabase("teams.db",Context.MODE_PRIVATE,null);
                    db.execSQL("insert into teams(teamName,isCaptain,isJoin) values(?,?,?)",new String[]{name,"1","1"});
                    G.captainTeam=name;





                }

            } else if (v.getId() == R.id.btn_Cancel) {

                finish();

            }


        }
    };
}
