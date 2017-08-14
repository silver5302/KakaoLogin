package com.silver5302.kakaologin;

import android.content.Context;
import android.content.Intent;
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
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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


    FragmentManager fragmentManager;
    LoginButton loginButton;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle drawerToggle;
    Toolbar toolbar;
    NavigationView navi;


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
        drawerToggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.app_name,R.string.app_name);

        ActionBar actionBar = getSupportActionBar();



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


    public void clickMakeTeam(View v){

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


    }
    public void clickGeneral(View v){


    }


}

