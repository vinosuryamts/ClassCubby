package uk.co.yellowrays.collegeinfo.classcubby;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wang.avi.AVLoadingIndicatorView;

import uk.co.yellowrays.collegeinfo.classcubby.configfiles.loginconfig;

public class MainActivity extends AppCompatActivity {

    TextView textView,loadingtext;
    AVLoadingIndicatorView loading;
    ImageView logo;
    Handler mHandler;
    Snackbar snackbar;
    View view;
    RelativeLayout layoutButtons,layoutMain,layoutContent;
    private boolean isOpen = false;
    private static int SPLASH_TIME_OUT = 2400;

    public static final String MY_PREFERENCES = "loginconfig.shared_pref_name";
    public static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        view = findViewById(R.id.splashactivitymain);

        sharedPreferences = getSharedPreferences(MY_PREFERENCES, MainActivity.MODE_PRIVATE);
        final String nusertypeid = sharedPreferences.getString(loginconfig.key_usertypeid, "");

        logo = (ImageView) findViewById(R.id.Logo);
        textView = (TextView)findViewById(R.id.appnamevalue);

        layoutContent = (RelativeLayout) findViewById(R.id.wholecontainer);
        layoutMain = (RelativeLayout) findViewById(R.id.splashactivitymain);

        //loadingtext = (TextView)findViewById(R.id.loadingtext);
        loading = (AVLoadingIndicatorView)findViewById(R.id.avi);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/appfont.ttf");
        textView.setTypeface(typeface);

        startAnim();

        /*Animation rotate = AnimationUtils.loadAnimation(MainActivity.this, R.anim.rotateclockwise);
        rotate.setFillAfter(true);
        logo.startAnimation(rotate);*/
        //logo.clearAnimation();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                if (AppStatus.getInstance(MainActivity.this).isOnline()) {
                    if (nusertypeid.equals("")) {
                        Intent i = new Intent(MainActivity.this, LoginActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  //It is use to finish current activity
                        startActivity(i);
                        finish();
                        loading.hide();
                    } else if (nusertypeid.equals("2")) {
                        Intent m = new Intent(MainActivity.this, Teacher_Dashboard_Activity.class);
//                        m.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  //It is use to finish current activity
                        startActivity(m);
                        finish();
                        loading.hide();
                    }else if (nusertypeid.equals("3")||nusertypeid.equals("4")||nusertypeid.equals("9")||nusertypeid.equals("10")) {
                        Intent m = new Intent(MainActivity.this, Teacher_Dashboard_Activity.class);
//                        m.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  //It is use to finish current activity
                        startActivity(m);
                        finish();
                        loading.hide();
                    }
                }else{
                    snackbar = Snackbar.make(view,"Please Connect to the Internet and Login Again",Snackbar.LENGTH_LONG);
                    snackbar.show();
                }

            }

        },SPLASH_TIME_OUT);
    }


    void startAnim(){
        loading.show();
        // or avi.smoothToShow();
    }

    void stopAnim(){
        loading.hide();
        // or avi.smoothToHide();
    }

}


