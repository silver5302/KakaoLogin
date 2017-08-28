package com.silver5302.kakaologin2;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;
import com.androidquery.AQuery;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.LoginButton;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private SessionCallback callback;
    TextView user_nickname,user_email;
    CircleImageView user_img;
    LinearLayout success_layout;
    Fragment fragment;
    UserProfile profile;

    SQLiteDatabase db;
    Cursor cursor2;

    FragmentManager fragmentManager;
    LoginButton loginButton;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle drawerToggle;
    Toolbar toolbar;
    NavigationView navi;
    ActionBar actionBar;


    AQuery aQuery;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        aQuery = new AQuery(this);
        callback = new SessionCallback();
        Session.getCurrentSession().addCallback(callback);


        fragmentManager=getSupportFragmentManager();
        navi=(NavigationView)findViewById(R.id.navi);
        drawerLayout=(DrawerLayout)findViewById(R.id.layout_drawer);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar=getSupportActionBar();
        drawerToggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.app_name,R.string.app_name){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                Menu menu=navi.getMenu();
                MenuItem menuItem=menu.findItem(R.id.header_menu_regiList);
                MenuItem menuItem1=menu.findItem(R.id.header_menu_matchList);
                if(G.isCaptain!=null&&G.isCaptain.equals("captain")){
                    menuItem.setVisible(true);
                    menuItem1.setVisible(true);
                }
            }
        };

        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        // 카카오톡 로그인 버튼
        loginButton = (LoginButton)findViewById(R.id.com_kakao_login);
        loginButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    if(!isConnected()){
                        Toast.makeText(MainActivity.this,"인터넷 연결을 확인해주세요",Toast.LENGTH_SHORT).show();
                    }
                }

                if(isConnected()){
                    return false;
                }else{
                    return true;
                }
            }
        });

        // 로그인 성공 시 사용할 뷰
        View headerView=navi.getHeaderView(0);
        success_layout = (LinearLayout)findViewById(R.id.success_layout);
        user_nickname =(TextView)headerView.findViewById(R.id.tv_header_name);
        user_img =(CircleImageView) headerView.findViewById(R.id.img_header);
        user_email =(TextView)headerView.findViewById(R.id.tv_header_mail);




        if(Session.getCurrentSession().isOpened()){
            requestMe();
        }else{
            success_layout.setVisibility(View.GONE);
            loginButton.setVisibility(View.VISIBLE);
        }


        navi.setNavigationItemSelectedListener(this);


    }


    //인터넷 연결상태 확인
    public boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }

        return false;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id=item.getItemId();
        if(id==R.id.header_menu_logout){
            if(Session.getCurrentSession().isOpened()) {
                requestLogout();
                FragmentTransaction fragmentTran=fragmentManager.beginTransaction();
                if(fragment!=null) fragmentTran.remove(fragment);
                fragmentTran.commit();
            }
        }else if(id==R.id.header_menu_regiList){

            Intent intent=new Intent(this,ReadyListActivity.class);
            startActivity(intent);
        }else if(id==R.id.header_menu_matchList){
            Intent intent=new Intent(this,MatchPermissonActivity.class);
            startActivity(intent);
        }


        drawerLayout.closeDrawer(navi);

        return true;
    }


    private class SessionCallback implements ISessionCallback {

        @Override
        public void onSessionOpened() {
            //access token을 성공적으로 발급 받아 valid access token을 가지고 있는 상태. 일반적으로 로그인 후의 다음 activity로 이동한다.
            if(Session.getCurrentSession().isOpened()){ // 한 번더 세션을 체크해주었습니다.
                requestMe();
                Toast.makeText(MainActivity.this, "로그인됨", Toast.LENGTH_SHORT).show();

            }
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            if(exception != null) {
                Logger.e(exception);
            }
        }
    }

    private void requestLogout() {
        success_layout.setVisibility(View.GONE);
        loginButton.setVisibility(View.VISIBLE);
        UserManagement.requestLogout(new LogoutResponseCallback() {
            @Override
            public void onCompleteLogout() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "로그아웃 성공", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void requestMe() {
        success_layout.setVisibility(View.VISIBLE);
        loginButton.setVisibility(View.GONE);

        UserManagement.requestMe(new MeResponseCallback() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                Log.e("onFailure", errorResult + "");
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                Log.e("onSessionClosed",errorResult + "");
            }

            @Override
            public void onSuccess(UserProfile userProfile) {
                profile = userProfile;
                Log.e("onSuccess",userProfile.toString());
                G.nickName=userProfile.getNickname();
                G.userId=userProfile.getId()+"";
                user_nickname.setText(userProfile.getNickname());
                user_email.setText(userProfile.getEmail());
                aQuery.id(user_img).image(userProfile.getThumbnailImagePath()); // <- 프로필 작은 이미지 , userProfile.getProfileImagePath() <- 큰 이미지
                makeAndfindMysql();

            }

            @Override
            public void onNotSignedUp() {
                Log.e("onNotSignedUp","onNotSignedUp");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(callback);
    }

    public void makeAndfindMysql(){
        db=openOrCreateDatabase("teams.db",Context.MODE_PRIVATE,null);
        db.execSQL("create table if not exists teams(no integer primary key autoincrement,teamName text,isCaptain integer,isJoin integer)");
        //주장팀 찾기
        Cursor cursor=db.rawQuery("select * from teams where isCaptain=1",null);
        if(cursor!=null&&cursor.getCount()>0){
            cursor.moveToFirst();
            String teamName=cursor.getString(cursor.getColumnIndex("teamName"));

            G.isCaptain="captain";
            G.captainTeam=teamName;
            Log.e("captain",teamName);
        }

        //가입신청한팀 찾기
        cursor2=db.rawQuery("select * from teams where isCaptain=0",null);
        if(cursor2!=null){
            while (cursor2.moveToNext()){
                final String teamName=cursor2.getString(cursor2.getColumnIndex("teamName"));
                Log.e("teamname",teamName);
                int isJoin=cursor2.getInt(cursor2.getColumnIndex("isJoin"));
                if(isJoin==1){
                    Log.e("가입된팀",teamName);
                }else if(isJoin==0){
                    //팀멤버리스트 테이블에 내가 속해있는지 확인하기
                    RequestQueue requestQue= Volley.newRequestQueue(this);
                    String serverUrl="http://silver5302.dothome.co.kr/Team/checkDB.php";
                    SimpleMultiPartRequest smpr=new SimpleMultiPartRequest(Request.Method.POST, serverUrl, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("respo",response);
                            if(response.equals("1")){ //가입된 상황
                                db=openOrCreateDatabase("teams.db",Context.MODE_PRIVATE,null);
                                db.execSQL("update teams set isJoin=1 where teamName=?",new String[]{teamName});
                            }else{ //미가입 상황

                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    });

                    smpr.addStringParam("userId",G.userId);
                    smpr.addStringParam("tableName",teamName);

                    requestQue.add(smpr);
                }

            }
        }
        db.close();
    }
    public void clickMakeTeam(View v){

        if(G.isCaptain!=null&&G.isCaptain.equals("captain")){
            Toast.makeText(this, "이미 한 팀의 주장입니다.", Toast.LENGTH_SHORT).show();
            return;
        }
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragment=new MakeTeamFragment();
        fragmentTransaction.add(R.id.success_layout,fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }
    public void clickFindTeam(View v){

        FragmentTransaction tran=fragmentManager.beginTransaction();
        fragment=new SearchTeamFragment();
        tran.add(R.id.success_layout,fragment);
        tran.addToBackStack(null);
        tran.commit();


    }
    public void clickMatchingTeam(View v){
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragment=new MatchingFragment();
        fragmentTransaction.add(R.id.success_layout,fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();





    }
    public void clickGeneral(View v){


    }


}

