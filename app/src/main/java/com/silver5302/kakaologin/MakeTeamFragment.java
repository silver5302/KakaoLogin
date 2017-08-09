package com.silver5302.kakaologin;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.kakao.usermgmt.response.model.UserProfile;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by alfo06-19 on 2017-07-26.
 */

public class MakeTeamFragment extends Fragment {


    Spinner spinner;
    Spinner spinner2;
    CircleImageView iv;
    ArrayAdapter adapter, adapter2;
    RadioGroup radioGroup;

    FragmentManager fm;

    Button btnOk, btnCanel;
    EditText editName, editInform;
    String name, type, region, number, inform, nickname;

    String insertUrl = "http://silver5302.dothome.co.kr/Team/insertDB.php";

    final int REQUEST_IMAGE_PICK = 10;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        nickname = bundle.getString("nickname");
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_make_team, container, false);


        fm = getActivity().getSupportFragmentManager();
        editInform = (EditText) view.findViewById(R.id.edit_teamIntroduce);
        editName = (EditText) view.findViewById(R.id.edit_teamName);
        radioGroup = (RadioGroup) view.findViewById(R.id.radio_group);

        iv = (CircleImageView) view.findViewById(R.id.imgview);

        spinner = (Spinner) view.findViewById(R.id.spinner);
        spinner2 = (Spinner) view.findViewById(R.id.spinner2);
        adapter = ArrayAdapter.createFromResource(getContext(), R.array.datas_location, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner.setAdapter(adapter);

        adapter2 = ArrayAdapter.createFromResource(getContext(), R.array.datas_number, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner2.setAdapter(adapter2);

        spinner.setOnItemSelectedListener(listener);
        spinner2.setOnItemSelectedListener(listener);

        btnOk = (Button) view.findViewById(R.id.btn_Ok);
        btnCanel = (Button) view.findViewById(R.id.btn_Cancel);

        btnOk.setOnClickListener(clicklistener);
        btnCanel.setOnClickListener(clicklistener);

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });


        return view;
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
                RadioButton rb = (RadioButton) getActivity().findViewById(radioGroup.getCheckedRadioButtonId());
                type = rb.getText().toString();


                //서버에 php로 데이터 보내기
                if (editName.getText().toString().equals("") || editInform.getText().toString().equals("")) {
                    Toast.makeText(getContext(), "빈 칸을 채워주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            try {
                                nickname = URLEncoder.encode(nickname, "utf-8");
                                name = URLEncoder.encode(name, "utf-8");
                                region = URLEncoder.encode(region, "utf-8");
                                type = URLEncoder.encode(type, "utf-8");
                                number = URLEncoder.encode(number, "utf-8");
                                inform = URLEncoder.encode(inform, "utf-8");
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }

                            try {
                                URL url = new URL(insertUrl);
                                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                                conn.setRequestMethod("POST");
                                conn.setDoInput(true);
                                conn.setDoOutput(true);
                                conn.setUseCaches(false);

                                String data = "name=" + name + "&type=" + type + "&region=" + region + "&number=" + number + "&inform=" + inform + "&nickname=" + nickname;
                                OutputStream os = conn.getOutputStream();
                                os.write(data.getBytes());
                                os.flush();
                                os.close();

                                InputStream is = conn.getInputStream();
                                InputStreamReader isr = new InputStreamReader(is);
                                BufferedReader reader = new BufferedReader(isr);
                                final StringBuffer buffer = new StringBuffer();
                                String line = reader.readLine();
                                while (line != null) {
                                    buffer.append(line);
                                    line = reader.readLine();
                                }
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getContext(), buffer.toString(), Toast.LENGTH_SHORT).show();
                                        editName.setText("");
                                        editInform.setText("");

                                        FragmentTransaction tran = fm.beginTransaction();
                                        tran.remove(MakeTeamFragment.this);
                                        tran.commit();
                                    }
                                });

                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                        }
                    }.start();
                }

            } else if (v.getId() == R.id.btn_Cancel) {

                FragmentTransaction tran = fm.beginTransaction();
                tran.remove(MakeTeamFragment.this);
                tran.commit();

            }


        }
    };


}
