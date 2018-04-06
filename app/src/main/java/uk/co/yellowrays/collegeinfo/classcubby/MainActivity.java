package uk.co.yellowrays.collegeinfo.classcubby;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wang.avi.AVLoadingIndicatorView;

import org.jsoup.Jsoup;

import uk.co.yellowrays.collegeinfo.classcubby.TeachersAdapter.userinformation.Teachers_Userinformation_View;
import uk.co.yellowrays.collegeinfo.classcubby.configfiles.loginconfig;

import static android.R.attr.data;
import static android.R.attr.exitFadeDuration;
import static android.R.attr.type;

public class MainActivity extends AppCompatActivity {

    TextView textView,loadingtext;
    AVLoadingIndicatorView loading;
    ImageView logo;
    Handler mHandler;
    Snackbar snackbar;
    View view;
    String currentVersion;
    RelativeLayout layoutButtons,layoutMain,layoutContent;
    private boolean isOpen = false;
    private static int SPLASH_TIME_OUT = 2400;
    private static int Close_TIME_OUT = 2400;
    String nusertypeid;
    String storeURL;
    Dialog dialog;

    public static final String MY_PREFERENCES = "loginconfig.shared_pref_name";
    public static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        view = findViewById(R.id.splashactivitymain);

        sharedPreferences = getSharedPreferences(MY_PREFERENCES, MainActivity.MODE_PRIVATE);
        nusertypeid = sharedPreferences.getString(loginconfig.key_usertypeid, "");

        logo = (ImageView) findViewById(R.id.Logo);
        textView = (TextView)findViewById(R.id.appnamevalue);

        layoutContent = (RelativeLayout) findViewById(R.id.wholecontainer);
        layoutMain = (RelativeLayout) findViewById(R.id.splashactivitymain);

        //loadingtext = (TextView)findViewById(R.id.loadingtext);
        loading = (AVLoadingIndicatorView)findViewById(R.id.avi);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/appfont.ttf");
        textView.setTypeface(typeface);

        startAnim();

        storeURL = "https://play.google.com/store/apps/details?id=uk.co.yellowrays.collegeinfo.classcubby";

        if (AppStatus.getInstance(MainActivity.this).isOnline()) {
            currentVersion = BuildConfig.VERSION_NAME;
            new GetVersionCode().execute();
        }else {
            Toast.makeText(MainActivity.this,"Please connect to internet and try again",Toast.LENGTH_LONG).show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            },Close_TIME_OUT);
        }



        /*Animation rotate = AnimationUtils.loadAnimation(MainActivity.this, R.anim.rotateclockwise);
        rotate.setFillAfter(true);
        logo.startAnimation(rotate);*/
        //logo.clearAnimation();

    }


    void startAnim(){
        loading.show();
        // or avi.smoothToShow();
    }

    void stopAnim(){
        loading.hide();
        // or avi.smoothToHide();
    }

    private class GetVersionCode extends AsyncTask<Void, String, String> {
        @Override
        protected String doInBackground(Void... voids) {



            String newVersion = null;
            try {
                newVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "&hl=en")
                        .timeout(30000)
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .referrer("http://www.google.com")
                        .get()
                        .select(".hAyfc .htlgb")
                        .get(2)
                        .ownText();
                return newVersion;
            } catch (Exception e) {
                return newVersion;
            }
        }

        @Override
        protected void onPostExecute(String onlineVersion) {
            super.onPostExecute(onlineVersion);
            if (onlineVersion != null && !onlineVersion.isEmpty()) {
                if (currentVersion.equals(onlineVersion)) {
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
                }else{
                    dialog = new Dialog(MainActivity.this);
                    // Include dialog.xml file
                    dialog.setContentView(R.layout.update_dialog);

                    TextView title = (TextView) dialog.findViewById(R.id.title);
                    TextView messagetext = (TextView) dialog.findViewById(R.id.messagetext);
                    TextView update = (TextView) dialog.findViewById(R.id.update);
                    TextView cancel = (TextView) dialog.findViewById(R.id.cancel);

                    Typeface normaltypeface = Typeface.createFromAsset(getAssets(), "fonts/ClearSansRegular.ttf");
                    Typeface boldtypeface = Typeface.createFromAsset(getAssets(), "fonts/ClearSansMedium.ttf");
                    title.setTypeface(boldtypeface);
                    messagetext.setTypeface(normaltypeface);
                    update.setTypeface(normaltypeface);
                    cancel.setTypeface(normaltypeface);

                    dialog.show();
                    dialog.setCancelable(false);

                    update.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=uk.co.yellowrays.collegeinfo.classcubby"));
                            startActivity(intent);
                        }
                    });

                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.setCancelable(true);
                            dialog.dismiss();
                            finish();
                        }
                    });
                }
            }
            Log.d("update", "Current version " + currentVersion + "playstore version " + onlineVersion);
        }

    }

}


