package uk.co.yellowrays.collegeinfo.classcubby;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.TypefaceSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import uk.co.yellowrays.collegeinfo.classcubby.configfiles.loginconfig;
import uk.co.yellowrays.collegeinfo.classcubby.configfiles.previousloginconfig;

public class LoginActivity extends AppCompatActivity {

    TextView textView,lanuagetext,showorhidetext,forgottext,signuptext,previoususernametext,enteremailtext,forgottitle,signupsupporttext;
    TextView successmessage,changetitle;
    TextView changesuccessmessage,confirmationtitle,passwordshoworhidetext,retypepasswordshoworhidetext;
    TextView changesuccesstitle,passchangesuccessmessage;
    ImageView logo,previoususerimage,successimage,changesuccessimage,passsuccessimage;
    Button signinbutton,previousloogedin,demobutton,alreadyhaveactivationbutton,gotologinbutton;
    EditText usernameedit,passwordedit,emailedittext,verificationemailedittext,firstverificationinput,secondverificationinput;
    EditText thirdverificationinput,fourthverificationinput,fifthverificationinput,sixthverificationinput;
    EditText changepasswordedittext,retypechangepasswordedittext;
    AVLoadingIndicatorView loading,nextload,thirdnextload;
    RelativeLayout loginpagecontainer,loadingcontainer,previousloggedincontainer,loginscreenmaincontainer,layoutButtons,forgotpasswordlayout;
    RelativeLayout circularimagecontainer,emailcontainer,forgotorcontainer,nextloadingcontainer,forgotpasswordfirstpage;
    RelativeLayout forgotpasswordsecondpage,successimagecontainer,emailidentercontainer,verificationinputfieldoverallcontainer,secondnextloadingcontainer;
    RelativeLayout forgotpasswordthirdpage,changeimagecontainer,changepasswordoverallcontainer,thirdnextloadingcontainer;
    RelativeLayout forgotpasswordfourthpage,changeimagesuccesscontainer;
    String previoususername,previouspassword,previoususer,previoususerimagevalue;
    String loggedinalready;
    private boolean forgotisOpen = false;
    private boolean firstopen = true;
    private boolean supportisOpen = false;
    private boolean isemailvalidateclicked = false;
    private boolean isemailvalidateclosed = false;
    private boolean isalreadyverificationvalidationclicked = false;
    FloatingActionButton fab,fabclose,nextarrow,secondpagenextarrow,thirdpagenextarrow;



    public static final String MY_PREFERENCES = "loginconfig.shared_pref_name";
    public static final String Previous_PREFERENCES = "previousloginconfig.shared_pref_name";
    public static SharedPreferences sharedPreferences,previoussharedPreferences;
    public static SharedPreferences.Editor editor,previouseditor;
    private JSONArray loginresult;
    public Context context;
    Snackbar snackbar;
    View view,forgotpasswordview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main);

        //login page elements
        lanuagetext = (TextView)findViewById(R.id.lanuagetext);
        logo = (ImageView) findViewById(R.id.loginlogo);
        textView = (TextView)findViewById(R.id.appnamevalue);
        usernameedit = (EditText)findViewById(R.id.usernameedittext);
        passwordedit = (EditText)findViewById(R.id.passwordedittext);
        showorhidetext = (TextView)findViewById(R.id.showorhidetext);
        signinbutton = (Button) findViewById(R.id.signinbutton);
        loading = (AVLoadingIndicatorView)findViewById(R.id.avi);
        forgottext = (TextView)findViewById(R.id.forgottext);
        demobutton = (Button) findViewById(R.id.demobutton);
        previoususerimage = (ImageView) findViewById(R.id.previoususerimage);
        previoususernametext = (TextView)findViewById(R.id.previoususernametext);
        signuptext = (TextView)findViewById(R.id.signuptext);

        //contact us page elements
        loginpagecontainer = (RelativeLayout) findViewById(R.id.loginpagecontainer);
        loadingcontainer = (RelativeLayout) findViewById(R.id.loadingcontainer);
        previousloggedincontainer = (RelativeLayout) findViewById(R.id.previousloggedincontainer);
        loginscreenmaincontainer = (RelativeLayout) findViewById(R.id.loginscreenmaincontainer);
        layoutButtons = (RelativeLayout) findViewById(R.id.layoutButtons);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        signupsupporttext = (TextView) findViewById(R.id.signupsupporttext);


        //forgot password page elements
        forgotpasswordlayout = (RelativeLayout) findViewById(R.id.forgotpasswordlayout);
        nextloadingcontainer = (RelativeLayout) findViewById(R.id.nextloadingcontainer);
        circularimagecontainer = (RelativeLayout) findViewById(R.id.circularimagecontainer);
        emailcontainer = (RelativeLayout) findViewById(R.id.emailcontainer);
        //forgotorcontainer = (RelativeLayout) findViewById(R.id.forgotorcontainer);
        forgotpasswordfirstpage = (RelativeLayout) findViewById(R.id.forgotpasswordfirstpage);
        emailedittext = (EditText)findViewById(R.id.emailedittext);
        enteremailtext = (TextView) findViewById(R.id.enteremailtext);
        forgottitle = (TextView) findViewById(R.id.forgottitle);
        fabclose = (FloatingActionButton) findViewById(R.id.fabclose);
        nextload = (AVLoadingIndicatorView)findViewById(R.id.nextload);
        nextarrow = (FloatingActionButton) findViewById(R.id.nextarrow);
        alreadyhaveactivationbutton = (Button) findViewById(R.id.alreadyhaveactivationbutton);

        //second page navigation elements
        forgotpasswordsecondpage = (RelativeLayout) findViewById(R.id.forgotpasswordsecondpage);
        successimagecontainer = (RelativeLayout) findViewById(R.id.successimagecontainer);
        emailidentercontainer = (RelativeLayout) findViewById(R.id.emailidentercontainer);
        verificationinputfieldoverallcontainer = (RelativeLayout) findViewById(R.id.verificationinputfieldoverallcontainer);
        secondnextloadingcontainer = (RelativeLayout) findViewById(R.id.secondnextloadingcontainer);
        verificationemailedittext = (EditText) findViewById(R.id.verificationemailedittext);
        firstverificationinput = (EditText) findViewById(R.id.firstverificationinput);
        secondverificationinput = (EditText) findViewById(R.id.secondverificationinput);
        thirdverificationinput = (EditText) findViewById(R.id.thirdverificationinput);
        fourthverificationinput = (EditText) findViewById(R.id.fourthverificationinput);
        successmessage = (TextView) findViewById(R.id.successmessage);
        confirmationtitle = (TextView) findViewById(R.id.confirmationtitle);
        successimage = (ImageView) findViewById(R.id.successimage);
        secondpagenextarrow = (FloatingActionButton) findViewById(R.id.secondpagenextarrow);


        //third page navigation elements
        forgotpasswordthirdpage = (RelativeLayout) findViewById(R.id.forgotpasswordthirdpage);
        changeimagecontainer = (RelativeLayout) findViewById(R.id.changeimagecontainer);
        changepasswordoverallcontainer = (RelativeLayout) findViewById(R.id.changepasswordoverallcontainer);
        thirdnextloadingcontainer = (RelativeLayout) findViewById(R.id.thirdnextloadingcontainer);
        changepasswordedittext = (EditText) findViewById(R.id.changepasswordedittext);
        retypechangepasswordedittext = (EditText) findViewById(R.id.retypechangepasswordedittext);
        changesuccessmessage = (TextView) findViewById(R.id.changesuccessmessage);
        passwordshoworhidetext = (TextView) findViewById(R.id.passwordshoworhidetext);
        retypepasswordshoworhidetext = (TextView) findViewById(R.id.retypepasswordshoworhidetext);
        changetitle = (TextView) findViewById(R.id.changetitle);
        changesuccessimage = (ImageView) findViewById(R.id.changesuccessimage);
        thirdpagenextarrow = (FloatingActionButton) findViewById(R.id.thirdpagenextarrow);

        //final page navigation elements
        forgotpasswordfourthpage = (RelativeLayout) findViewById(R.id.forgotpasswordfourthpage);
        changeimagesuccesscontainer = (RelativeLayout) findViewById(R.id.changeimagesuccesscontainer);
        changesuccesstitle = (TextView) findViewById(R.id.changesuccesstitle);
        passchangesuccessmessage = (TextView) findViewById(R.id.passchangesuccessmessage);
        passsuccessimage = (ImageView) findViewById(R.id.passsuccessimage);
        gotologinbutton = (Button) findViewById(R.id.gotologinbutton);

        view = findViewById(R.id.loginscreenmaincontainer);
        forgotpasswordview = findViewById(R.id.forgotpasswordlayout);


        //logo rotate animation
        /*Animation rotate = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.rotateclockwise);
        rotate.setFillAfter(true);
        logo.startAnimation(rotate);*/


        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/appfont.ttf");
        Typeface normaltypeface = Typeface.createFromAsset(getAssets(), "fonts/OpenSansSemibold.ttf");
        Typeface boldtypeface = Typeface.createFromAsset(getAssets(), "fonts/OpenSansBold.ttf");
        textView.setTypeface(typeface);
        lanuagetext.setTypeface(normaltypeface);
        forgottext.setTypeface(normaltypeface);
        signuptext.setTypeface(normaltypeface);
        showorhidetext.setTypeface(boldtypeface);
        usernameedit.setTypeface(normaltypeface);
        passwordedit.setTypeface(normaltypeface);
        emailedittext.setTypeface(normaltypeface);
        enteremailtext.setTypeface(normaltypeface);
        signupsupporttext.setTypeface(normaltypeface);
        forgottitle.setTypeface(boldtypeface);
        changesuccessmessage.setTypeface(boldtypeface);
        passwordshoworhidetext.setTypeface(boldtypeface);
        retypepasswordshoworhidetext.setTypeface(boldtypeface);
        changetitle.setTypeface(boldtypeface);
        changesuccesstitle.setTypeface(boldtypeface);
        passchangesuccessmessage.setTypeface(boldtypeface);
        gotologinbutton.setTypeface(boldtypeface);


        //sign in page actions
        signinbutton.setEnabled(false);

        String forgottextvalue = forgottext.getText().toString().trim();
        String signuptextvalue = signuptext.getText().toString().trim();

        SpannableStringBuilder str = new SpannableStringBuilder(forgottextvalue);
        str.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.textColorPrimary)), 27, forgottext.getText().toString().trim().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        TypefaceSpan typefaceSpan = new TypefaceSpan("fonts/OpenSansBold.ttf");
        str.setSpan(typefaceSpan, 27, forgottext.getText().toString().trim().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        forgottext.setText(str);

        SpannableStringBuilder str1 = new SpannableStringBuilder(signuptextvalue);
        str1.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.textColorPrimary)), 23, signuptext.getText().toString().trim().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        TypefaceSpan typefaceSpan1 = new TypefaceSpan("fonts/OpenSansBold.ttf");
        str1.setSpan(typefaceSpan1, 23, signuptext.getText().toString().trim().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        signuptext.setText(str1);


        previoussharedPreferences = getSharedPreferences(Previous_PREFERENCES, MainActivity.MODE_PRIVATE);
        loggedinalready = previoussharedPreferences.getString(previousloginconfig.isloggedinalready, "");


        //previous login functionality check
        if(loggedinalready.equals("false")||loggedinalready.equals("")){
            demobutton.setVisibility(View.VISIBLE);
        }else{
            previoususername = previoussharedPreferences.getString(previousloginconfig.key_loginname,"");
            previouspassword = previoussharedPreferences.getString(previousloginconfig.key_password,"");
            previoususer = previoussharedPreferences.getString(previousloginconfig.key_username,"");
            previoususerimagevalue = previoussharedPreferences.getString(previousloginconfig.key_userimage,"");

            previousloggedincontainer.setVisibility(View.VISIBLE);
            previoususernametext.setText(previoususer);

            Glide.with(LoginActivity.this).load(previoususerimagevalue).listener(new RequestListener<String, GlideDrawable>() {
                @Override
                public boolean onException(Exception e, String s, Target<GlideDrawable> target, boolean b) {
                    Log.i("GLIDE", "onException :", e);
                    return false;
                }

                @Override
                public boolean onResourceReady(GlideDrawable glideDrawable, String s, Target<GlideDrawable> target, boolean b, boolean b1) {
                    Log.i("GLIDE", "onResourceReady");
                    previoususerimage.setVisibility(View.VISIBLE);
                    return false;
                }
            }).into(previoususerimage);
        }

        previousloggedincontainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usernameedit.setText(previoususername);
                passwordedit.setText(previouspassword);
                signinbutton.performClick();
            }
        });

        usernameedit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (usernameedit.getText().length()>5 && passwordedit.getText().length()>5){
                    signinbutton.setEnabled(true);
                    signinbutton.setTextColor(getResources().getColor(R.color.white));
                    signinbutton.setBackground(getResources().getDrawable(R.drawable.enabledsigninbutton));
                }else{
                    signinbutton.setEnabled(false);
                    signinbutton.setBackground(getResources().getDrawable(R.drawable.signinbutton));
                    signinbutton.setTextColor(getResources().getColor(R.color.dimblue));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        passwordedit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (usernameedit.getText().length()>5 && passwordedit.getText().length()>5){
                    signinbutton.setEnabled(true);
                    signinbutton.setTextColor(getResources().getColor(R.color.white));
                    signinbutton.setBackground(getResources().getDrawable(R.drawable.enabledsigninbutton));
                }else{
                    signinbutton.setEnabled(false);
                    signinbutton.setBackground(getResources().getDrawable(R.drawable.signinbutton));
                    signinbutton.setTextColor(getResources().getColor(R.color.dimblue));
                }

                if(showorhidetext.getVisibility()== View.INVISIBLE && passwordedit.getText().length()>1){
                    showorhidetext.setVisibility(View.VISIBLE);
                    Selection.setSelection(passwordedit.getText(), passwordedit.getText().length());
                }if(showorhidetext.getVisibility()== View.VISIBLE && passwordedit.getText().length()>1){
                    showorhidetext.setVisibility(View.VISIBLE);
                    Selection.setSelection(passwordedit.getText(), passwordedit.getText().length());
                }else{
                    showorhidetext.setVisibility(View.INVISIBLE);
                    showorhidetext.setText("Show");
                    Selection.setSelection(passwordedit.getText(), passwordedit.getText().length());
                    passwordedit.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        showorhidetext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(showorhidetext.getText().toString().trim().equals("Show")){
                    passwordedit.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    showorhidetext.setText("Hide");
                }else{
                    passwordedit.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    showorhidetext.setText("Show");
                }
            }
        });


        changepasswordedittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                /*if (changepasswordedittext.getText().length()>5 && retypechangepasswordedittext.getText().length()>5 && changepasswordedittext.getText().toString().trim()==retypechangepasswordedittext.getText().toString().trim()){
                    thirdpagenextarrow.setEnabled(true);
                }else{
                    thirdpagenextarrow.setEnabled(false);
                }*/

                if(passwordshoworhidetext.getVisibility()== View.INVISIBLE && changepasswordedittext.getText().length()>5){
                    passwordshoworhidetext.setVisibility(View.VISIBLE);
                    Selection.setSelection(changepasswordedittext.getText(), changepasswordedittext.getText().length());
                }if(passwordshoworhidetext.getVisibility()== View.VISIBLE && changepasswordedittext.getText().length()>5){
                    passwordshoworhidetext.setVisibility(View.VISIBLE);
                    Selection.setSelection(changepasswordedittext.getText(), changepasswordedittext.getText().length());
                }else{
                    passwordshoworhidetext.setVisibility(View.INVISIBLE);
                    passwordshoworhidetext.setText("Show");
                    Selection.setSelection(changepasswordedittext.getText(), changepasswordedittext.getText().length());
                    changepasswordedittext.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        passwordshoworhidetext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(passwordshoworhidetext.getText().toString().trim().equals("Show")){
                    changepasswordedittext.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    passwordshoworhidetext.setText("Hide");
                }else{
                    changepasswordedittext.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    passwordshoworhidetext.setText("Show");
                }
            }
        });


        retypechangepasswordedittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                /*if (changepasswordedittext.getText().length()>5 && retypechangepasswordedittext.getText().length()>5 && changepasswordedittext.getText().toString().trim()==retypechangepasswordedittext.getText().toString().trim()){
                    thirdpagenextarrow.setEnabled(true);
                }else{
                    thirdpagenextarrow.setEnabled(false);
                }*/

                if(retypepasswordshoworhidetext.getVisibility()== View.INVISIBLE && retypechangepasswordedittext.getText().length()>5){
                    retypepasswordshoworhidetext.setVisibility(View.VISIBLE);
                    Selection.setSelection(retypechangepasswordedittext.getText(), retypechangepasswordedittext.getText().length());
                }if(retypepasswordshoworhidetext.getVisibility()== View.VISIBLE && retypechangepasswordedittext.getText().length()>5){
                    retypepasswordshoworhidetext.setVisibility(View.VISIBLE);
                    Selection.setSelection(retypechangepasswordedittext.getText(), retypechangepasswordedittext.getText().length());
                }else{
                    retypepasswordshoworhidetext.setVisibility(View.INVISIBLE);
                    retypepasswordshoworhidetext.setText("Show");
                    Selection.setSelection(retypechangepasswordedittext.getText(), retypechangepasswordedittext.getText().length());
                    retypechangepasswordedittext.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        retypepasswordshoworhidetext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(retypepasswordshoworhidetext.getText().toString().trim().equals("Show")){
                    retypechangepasswordedittext.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    retypepasswordshoworhidetext.setText("Hide");
                }else{
                    retypechangepasswordedittext.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    retypepasswordshoworhidetext.setText("Show");
                }
            }
        });



        signinbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (AppStatus.getInstance(LoginActivity.this).isOnline()) {

                    if(!validate()){
                        signinbutton.setVisibility(View.VISIBLE);
                        usernameedit.setEnabled(true);
                        passwordedit.setEnabled(true);
                        loadingcontainer.setVisibility(View.INVISIBLE);
                        return;
                    }

                    signinbutton.setVisibility(View.INVISIBLE);
                    usernameedit.setEnabled(false);
                    passwordedit.setEnabled(false);
                    loadingcontainer.setVisibility(View.VISIBLE);

                    new android.os.Handler().postDelayed(
                            new Runnable() {
                                public void run() {
                                    onLoginSuccess();
                                }
                            }, 100);

                }else{
                    snackbar = Snackbar.make(view,"Unable to connect to Internet! Please try again.",Snackbar.LENGTH_LONG);
                    snackbar.show();
                }


            }
        });

        signuptext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewMenu();
            }
        });


        forgottext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewforgotpasswordMenu();
                //Intent i = new Intent(LoginActivity.this,Forgot_Password_Activity.class);
                //startActivity(i);
            }
        });



        //Support page navigations
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginpagecontainer.setVisibility(View.VISIBLE);
                int x = layoutButtons.getRight();
                int y = layoutButtons.getBottom();

                int startRadius = Math.max(loginscreenmaincontainer.getWidth(), loginscreenmaincontainer.getHeight());
                int endRadius = 0;

                Animator anim = ViewAnimationUtils.createCircularReveal(layoutButtons, x, y, startRadius, endRadius);
                anim.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        layoutButtons.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                });
                anim.start();

                supportisOpen = false;

            }
        });


        //forgot password navigations
        fabclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginpagecontainer.setVisibility(View.VISIBLE);
                int x = forgotpasswordlayout.getRight();
                int y = forgotpasswordlayout.getBottom();

                int startRadius = Math.max(loginscreenmaincontainer.getWidth(), loginscreenmaincontainer.getHeight());
                int endRadius = 0;

                Animator anim = ViewAnimationUtils.createCircularReveal(forgotpasswordlayout, x, y, startRadius, endRadius);
                anim.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        forgottitle.clearAnimation();
                        circularimagecontainer.clearAnimation();
                        successimagecontainer.clearAnimation();
                        enteremailtext.clearAnimation();
                        emailcontainer.clearAnimation();
                        nextarrow.clearAnimation();
                        nextloadingcontainer.clearAnimation();
                        alreadyhaveactivationbutton.clearAnimation();
                        emailedittext.setText("");
                        verificationemailedittext.setText("");

                        firstverificationinput.setText("");
                        secondverificationinput.setText("");
                        thirdverificationinput.setText("");
                        fourthverificationinput.setText("");

                        isalreadyverificationvalidationclicked = false;

                        confirmationtitle.clearAnimation();
                        successimagecontainer.clearAnimation();
                        successmessage.clearAnimation();
                        emailidentercontainer.clearAnimation();
                        verificationinputfieldoverallcontainer.clearAnimation();

                        nextarrow.setClickable(true);
                        secondpagenextarrow.setClickable(false);
                        secondnextloadingcontainer.setClickable(false);
                        alreadyhaveactivationbutton.setClickable(true);
                        emailedittext.setEnabled(true);

                        secondpagenextarrow.clearAnimation();
                        secondnextloadingcontainer.clearAnimation();
                        secondpagenextarrow.setClickable(true);

                        if(isemailvalidateclicked==true){
                            isemailvalidateclosed = true;
                        }
                        isemailvalidateclicked = false;

                        forgottitle.setVisibility(View.INVISIBLE);
                        circularimagecontainer.setVisibility(View.INVISIBLE);
                        successimagecontainer.setVisibility(View.INVISIBLE);
                        enteremailtext.setVisibility(View.INVISIBLE);
                        emailcontainer.setVisibility(View.INVISIBLE);
                        nextarrow.setVisibility(View.INVISIBLE);
                        nextloadingcontainer.setVisibility(View.INVISIBLE);
                        //forgotorcontainer.setVisibility(View.INVISIBLE);
                        alreadyhaveactivationbutton.setVisibility(View.INVISIBLE);
                        forgotpasswordlayout.setVisibility(View.GONE);


                        confirmationtitle.setVisibility(View.INVISIBLE);
                        successimagecontainer.setVisibility(View.INVISIBLE);
                        successmessage.setVisibility(View.INVISIBLE);
                        emailidentercontainer.setVisibility(View.INVISIBLE);
                        verificationinputfieldoverallcontainer.setVisibility(View.INVISIBLE);
                        secondpagenextarrow.setVisibility(View.INVISIBLE);
                        secondnextloadingcontainer.setVisibility(View.INVISIBLE);

                        changetitle.clearAnimation();
                        changesuccessmessage.clearAnimation();
                        changesuccessimage.clearAnimation();
                        changepasswordoverallcontainer.clearAnimation();
                        changeimagecontainer.clearAnimation();

                        changepasswordedittext.setText("");
                        retypechangepasswordedittext.setText("");
                        thirdpagenextarrow.clearAnimation();
                        thirdnextloadingcontainer.clearAnimation();
                        thirdpagenextarrow.setClickable(true);

                        changetitle.setVisibility(View.INVISIBLE);
                        changesuccessmessage.setVisibility(View.INVISIBLE);
                        changeimagecontainer.setVisibility(View.INVISIBLE);
                        changepasswordoverallcontainer.setVisibility(View.INVISIBLE);
                        thirdpagenextarrow.setVisibility(View.INVISIBLE);
                        thirdnextloadingcontainer.setVisibility(View.INVISIBLE);


                        changesuccesstitle.clearAnimation();
                        changeimagesuccesscontainer.clearAnimation();
                        passchangesuccessmessage.clearAnimation();
                        gotologinbutton.clearAnimation();

                        changesuccesstitle.setVisibility(View.INVISIBLE);
                        passchangesuccessmessage.setVisibility(View.INVISIBLE);
                        changeimagesuccesscontainer.setVisibility(View.INVISIBLE);
                        gotologinbutton.setVisibility(View.INVISIBLE);

                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                });
                anim.start();

                forgotisOpen = false;

            }
        });


        gotologinbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginpagecontainer.setVisibility(View.VISIBLE);
                int x = forgotpasswordlayout.getRight();
                int y = forgotpasswordlayout.getBottom();

                int startRadius = Math.max(loginscreenmaincontainer.getWidth(), loginscreenmaincontainer.getHeight());
                int endRadius = 0;

                Animator anim = ViewAnimationUtils.createCircularReveal(forgotpasswordlayout, x, y, startRadius, endRadius);
                anim.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        forgottitle.clearAnimation();
                        circularimagecontainer.clearAnimation();
                        successimagecontainer.clearAnimation();
                        enteremailtext.clearAnimation();
                        emailcontainer.clearAnimation();
                        nextarrow.clearAnimation();
                        nextloadingcontainer.clearAnimation();
                        alreadyhaveactivationbutton.clearAnimation();
                        emailedittext.setText("");
                        verificationemailedittext.setText("");

                        firstverificationinput.setText("");
                        secondverificationinput.setText("");
                        thirdverificationinput.setText("");
                        fourthverificationinput.setText("");

                        isalreadyverificationvalidationclicked = false;

                        confirmationtitle.clearAnimation();
                        successimagecontainer.clearAnimation();
                        successmessage.clearAnimation();
                        emailidentercontainer.clearAnimation();
                        verificationinputfieldoverallcontainer.clearAnimation();

                        nextarrow.setClickable(true);
                        secondpagenextarrow.setClickable(false);
                        secondnextloadingcontainer.setClickable(false);
                        alreadyhaveactivationbutton.setClickable(true);
                        emailedittext.setEnabled(true);

                        secondpagenextarrow.clearAnimation();
                        secondnextloadingcontainer.clearAnimation();
                        secondpagenextarrow.setClickable(true);

                        if(isemailvalidateclicked==true){
                            isemailvalidateclosed = true;
                        }
                        isemailvalidateclicked = false;

                        forgottitle.setVisibility(View.INVISIBLE);
                        circularimagecontainer.setVisibility(View.INVISIBLE);
                        successimagecontainer.setVisibility(View.INVISIBLE);
                        enteremailtext.setVisibility(View.INVISIBLE);
                        emailcontainer.setVisibility(View.INVISIBLE);
                        nextarrow.setVisibility(View.INVISIBLE);
                        nextloadingcontainer.setVisibility(View.INVISIBLE);
                        //forgotorcontainer.setVisibility(View.INVISIBLE);
                        alreadyhaveactivationbutton.setVisibility(View.INVISIBLE);
                        forgotpasswordlayout.setVisibility(View.GONE);


                        confirmationtitle.setVisibility(View.INVISIBLE);
                        successimagecontainer.setVisibility(View.INVISIBLE);
                        successmessage.setVisibility(View.INVISIBLE);
                        emailidentercontainer.setVisibility(View.INVISIBLE);
                        verificationinputfieldoverallcontainer.setVisibility(View.INVISIBLE);
                        secondpagenextarrow.setVisibility(View.INVISIBLE);
                        secondnextloadingcontainer.setVisibility(View.INVISIBLE);

                        changetitle.clearAnimation();
                        changesuccessmessage.clearAnimation();
                        changesuccessimage.clearAnimation();
                        changepasswordoverallcontainer.clearAnimation();
                        changeimagecontainer.clearAnimation();

                        changepasswordedittext.setText("");
                        retypechangepasswordedittext.setText("");
                        thirdpagenextarrow.clearAnimation();
                        thirdnextloadingcontainer.clearAnimation();
                        thirdpagenextarrow.setClickable(true);

                        changetitle.setVisibility(View.INVISIBLE);
                        changesuccessmessage.setVisibility(View.INVISIBLE);
                        changeimagecontainer.setVisibility(View.INVISIBLE);
                        changepasswordoverallcontainer.setVisibility(View.INVISIBLE);
                        thirdpagenextarrow.setVisibility(View.INVISIBLE);
                        thirdnextloadingcontainer.setVisibility(View.INVISIBLE);


                        changesuccesstitle.clearAnimation();
                        changeimagesuccesscontainer.clearAnimation();
                        passchangesuccessmessage.clearAnimation();
                        gotologinbutton.clearAnimation();

                        changesuccesstitle.setVisibility(View.INVISIBLE);
                        passchangesuccessmessage.setVisibility(View.INVISIBLE);
                        changeimagesuccesscontainer.setVisibility(View.INVISIBLE);
                        gotologinbutton.setVisibility(View.INVISIBLE);

                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                });
                anim.start();

                forgotisOpen = false;

            }
        });


        nextarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                v.clearAnimation();

                if (AppStatus.getInstance(LoginActivity.this).isOnline()) {

                    if(!emailvalidate()){
                        nextarrow.setVisibility(View.VISIBLE);
                        nextloadingcontainer.setVisibility(View.INVISIBLE);
                        return;
                    }

                    nextloadingcontainer.setVisibility(View.VISIBLE);
                    nextarrow.setVisibility(View.INVISIBLE);

                    new android.os.Handler().postDelayed(
                            new Runnable() {
                                public void run() {
                                    onsuccessemailvalidation();
                                }
                            }, 100);

                }else{
                    nextarrow.setVisibility(View.VISIBLE);
                    nextloadingcontainer.setVisibility(View.INVISIBLE);
                    snackbar = Snackbar.make(forgotpasswordview,"Unable to connect to Internet! Please try again.",Snackbar.LENGTH_LONG);
                    snackbar.show();
                }


            }
        });

        secondpagenextarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                v.clearAnimation();

                if (AppStatus.getInstance(LoginActivity.this).isOnline()) {

                    if(isalreadyverificationvalidationclicked==true) {
                        if (!verifyemailvalidate()) {
                            secondpagenextarrow.setVisibility(View.VISIBLE);
                            secondnextloadingcontainer.setVisibility(View.INVISIBLE);
                            return;
                        }
                    }

                    if(!verifyemptyverification()){
                        secondpagenextarrow.setVisibility(View.VISIBLE);
                        secondnextloadingcontainer.setVisibility(View.INVISIBLE);
                        return;
                    }


                    secondnextloadingcontainer.setVisibility(View.VISIBLE);
                    secondpagenextarrow.setVisibility(View.INVISIBLE);

                    new android.os.Handler().postDelayed(
                            new Runnable() {
                                public void run() {
                                    onsuccessverificationvalidation();
                                }
                            }, 100);

                }else{
                    secondpagenextarrow.setVisibility(View.VISIBLE);
                    secondnextloadingcontainer.setVisibility(View.INVISIBLE);
                    snackbar = Snackbar.make(forgotpasswordview,"Unable to connect to Internet! Please try again.",Snackbar.LENGTH_LONG);
                    snackbar.show();
                }


            }
        });


        thirdpagenextarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                v.clearAnimation();

                if (AppStatus.getInstance(LoginActivity.this).isOnline()) {

                    if(!verifypasswordmatch()){
                        thirdpagenextarrow.setVisibility(View.VISIBLE);
                        thirdnextloadingcontainer.setVisibility(View.INVISIBLE);
                        return;
                    }


                    thirdnextloadingcontainer.setVisibility(View.VISIBLE);
                    thirdpagenextarrow.setVisibility(View.INVISIBLE);

                    new android.os.Handler().postDelayed(
                            new Runnable() {
                                public void run() {
                                    onsuccesschangepassword();
                                }
                            }, 100);

                }else{
                    secondpagenextarrow.setVisibility(View.VISIBLE);
                    secondnextloadingcontainer.setVisibility(View.INVISIBLE);
                    snackbar = Snackbar.make(forgotpasswordview,"Unable to connect to Internet! Please try again.",Snackbar.LENGTH_LONG);
                    snackbar.show();
                }


            }
        });



        firstverificationinput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()==1){
                    secondverificationinput.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        secondverificationinput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()==1){
                    thirdverificationinput.requestFocus();
                }else if (count == 0) {
                    firstverificationinput.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()==0){
                    firstverificationinput.requestFocus();
                }
            }
        });

        thirdverificationinput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()==1){
                    fourthverificationinput.requestFocus();
                }else if (count == 0) {
                    secondverificationinput.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()==0){
                    secondverificationinput.requestFocus();
                }
            }
        });


        fourthverificationinput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == 0) {
                    thirdverificationinput.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()==0){
                    thirdverificationinput.requestFocus();
                }
            }
        });

        alreadyhaveactivationbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isalreadyverificationvalidationclicked = true;

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Animation sliderighttoleftanim = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.sliderighttoleft);
                        sliderighttoleftanim.setFillAfter(true);
                        forgottitle.startAnimation(sliderighttoleftanim);
                    }
                },400);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Animation sliderighttoleftanim = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.sliderighttoleft);
                        sliderighttoleftanim.setFillAfter(true);
                        circularimagecontainer.startAnimation(sliderighttoleftanim);
                    }
                },500);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Animation sliderighttoleftanim = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.sliderighttoleft);
                        sliderighttoleftanim.setFillAfter(true);
                        successimagecontainer.startAnimation(sliderighttoleftanim);
                    }
                },600);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Animation sliderighttoleftanim = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.sliderighttoleft);
                        sliderighttoleftanim.setFillAfter(true);
                        enteremailtext.startAnimation(sliderighttoleftanim);
                    }
                },700);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Animation sliderighttoleftanim = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.sliderighttoleft);
                        sliderighttoleftanim.setFillAfter(true);
                        emailcontainer.startAnimation(sliderighttoleftanim);
                    }
                },800);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Animation sliderighttoleftanim = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.sliderighttoleft);
                        sliderighttoleftanim.setFillAfter(true);
                        nextarrow.startAnimation(sliderighttoleftanim);
                        nextarrow.setVisibility(View.INVISIBLE);
                        nextloadingcontainer.startAnimation(sliderighttoleftanim);

                    }
                },900);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Animation sliderighttoleftanim = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.sliderighttoleft);
                        sliderighttoleftanim.setFillAfter(true);
                        //forgotorcontainer.startAnimation(sliderighttoleftanim);
                    }
                },1000);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Animation sliderighttoleftanim = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.sliderighttoleft);
                        sliderighttoleftanim.setFillAfter(true);
                        alreadyhaveactivationbutton.startAnimation(sliderighttoleftanim);
                    }
                },1100);



                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //forgotpasswordfirstpage.setVisibility(View.GONE);
                        forgotisOpen = true;
                    }
                },1500);


                isemailvalidateclicked = true;


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        confirmationtitle.setVisibility(View.VISIBLE);
                        Animation sliderighttoleftanim = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.slidelefttoright);
                        sliderighttoleftanim.setFillAfter(true);
                        confirmationtitle.startAnimation(sliderighttoleftanim);
                    }
                },1800);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        successimagecontainer.setVisibility(View.VISIBLE);
                        successimage.setVisibility(View.VISIBLE);

                        String value = "Success";
                        int animicon;

                        if(value.equals("Success")){
                            animicon = R.drawable.tickanimation;
                        }else{
                            animicon = R.drawable.failedicon;
                        }
                        Glide.with(LoginActivity.this).load(animicon).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).crossFade().into(successimage);


                        Animation sliderighttoleftanim = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.slidelefttoright);
                        sliderighttoleftanim.setFillAfter(true);
                        successimagecontainer.startAnimation(sliderighttoleftanim);
                    }
                },1900);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        nextarrow.setClickable(false);
                        alreadyhaveactivationbutton.setClickable(false);
                        emailedittext.setEnabled(false);
                        successmessage.setVisibility(View.VISIBLE);
                        successmessage.setText("Already a verification code? Please enter your emailid linked with classcubby and verification code below to proceed.");
                        Animation sliderighttoleftanim = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.slidelefttoright);
                        sliderighttoleftanim.setFillAfter(true);
                        successmessage.startAnimation(sliderighttoleftanim);
                    }
                },2000);


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        emailidentercontainer.setVisibility(View.VISIBLE);
                        emailidentercontainer.setClickable(true);

                        Animation sliderighttoleftanim = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.slidelefttoright);
                        sliderighttoleftanim.setFillAfter(true);
                        emailidentercontainer.startAnimation(sliderighttoleftanim);
                    }
                },2100);


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        verificationinputfieldoverallcontainer.setVisibility(View.VISIBLE);
                        verificationinputfieldoverallcontainer.setClickable(true);

                        Animation sliderighttoleftanim = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.slidelefttoright);
                        sliderighttoleftanim.setFillAfter(true);
                        verificationinputfieldoverallcontainer.startAnimation(sliderighttoleftanim);
                    }
                },2200);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        secondpagenextarrow.setVisibility(View.VISIBLE);
                        secondpagenextarrow.setClickable(true);
                        Animation sliderighttoleftanim = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.slidelefttoright);
                        sliderighttoleftanim.setFillAfter(true);
                        secondpagenextarrow.startAnimation(sliderighttoleftanim);
                        secondnextloadingcontainer.startAnimation(sliderighttoleftanim);
                    }
                },2300);

            }
        });


    }


    private void viewMenu() {

        if (!supportisOpen) {
            int x = loginscreenmaincontainer.getRight();
            int y = loginscreenmaincontainer.getBottom();

            int startRadius = 0;
            int endRadius = (int) Math.hypot(loginscreenmaincontainer.getWidth(), loginscreenmaincontainer.getHeight());

            Animator anim = ViewAnimationUtils.createCircularReveal(layoutButtons, x, y, startRadius, endRadius);

            layoutButtons.setVisibility(View.VISIBLE);
            anim.start();

            supportisOpen = true;

            loginpagecontainer.setVisibility(View.GONE);
        }

    }

    private void viewforgotpasswordMenu() {

        if (!forgotisOpen) {
            int x = loginscreenmaincontainer.getRight();
            int y = loginscreenmaincontainer.getBottom();

            int startRadius = 0;
            int endRadius = (int) Math.hypot(loginscreenmaincontainer.getWidth(), loginscreenmaincontainer.getHeight());

            Animator anim = ViewAnimationUtils.createCircularReveal(forgotpasswordlayout, x, y, startRadius, endRadius);

            forgotpasswordlayout.setVisibility(View.VISIBLE);
            anim.start();


            forgotisOpen = true;

            if(firstopen==true){
                forgottitle.setVisibility(View.INVISIBLE);
                circularimagecontainer.setVisibility(View.INVISIBLE);
                successimagecontainer.setVisibility(View.INVISIBLE);
                enteremailtext.setVisibility(View.INVISIBLE);
                emailcontainer.setVisibility(View.INVISIBLE);
                nextarrow.setVisibility(View.INVISIBLE);
                nextloadingcontainer.setVisibility(View.INVISIBLE);
                //forgotorcontainer.setVisibility(View.INVISIBLE);
                alreadyhaveactivationbutton.setVisibility(View.INVISIBLE);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Animation sliderighttoleftanim = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.slidelefttoright);
                        sliderighttoleftanim.setFillAfter(true);
                        forgottitle.startAnimation(sliderighttoleftanim);
                    }
                },400);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Animation sliderighttoleftanim = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.slidelefttoright);
                        sliderighttoleftanim.setFillAfter(true);
                        circularimagecontainer.startAnimation(sliderighttoleftanim);
                    }
                },500);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Animation sliderighttoleftanim = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.slidelefttoright);
                        sliderighttoleftanim.setFillAfter(true);
                        successimagecontainer.startAnimation(sliderighttoleftanim);
                    }
                },600);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Animation sliderighttoleftanim = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.slidelefttoright);
                        sliderighttoleftanim.setFillAfter(true);
                        enteremailtext.startAnimation(sliderighttoleftanim);
                    }
                },700);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Animation sliderighttoleftanim = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.slidelefttoright);
                        sliderighttoleftanim.setFillAfter(true);
                        emailcontainer.startAnimation(sliderighttoleftanim);
                    }
                },800);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Animation sliderighttoleftanim = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.slidelefttoright);
                        sliderighttoleftanim.setFillAfter(true);
                        if(nextarrow.getVisibility()==View.INVISIBLE) {
                            nextarrow.startAnimation(sliderighttoleftanim);
                        }else {
                            nextloadingcontainer.startAnimation(sliderighttoleftanim);
                        }
                    }
                },900);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Animation sliderighttoleftanim = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.slidelefttoright);
                        sliderighttoleftanim.setFillAfter(true);
                        //forgotorcontainer.startAnimation(sliderighttoleftanim);
                    }
                },1000);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Animation sliderighttoleftanim = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.slidelefttoright);
                        sliderighttoleftanim.setFillAfter(true);
                        alreadyhaveactivationbutton.startAnimation(sliderighttoleftanim);
                    }
                },1100);

                nextarrow.setClickable(true);
                alreadyhaveactivationbutton.setClickable(true);
                emailedittext.setEnabled(true);

                firstopen=false;
            }else if(isemailvalidateclicked==true && isemailvalidateclosed ==false){
                //forgotpasswordfirstpage.setVisibility(View.VISIBLE);
                forgottitle.setVisibility(View.VISIBLE);
                circularimagecontainer.setVisibility(View.VISIBLE);
                successimagecontainer.setVisibility(View.VISIBLE);
                enteremailtext.setVisibility(View.VISIBLE);
                emailcontainer.setVisibility(View.VISIBLE);
                nextarrow.setVisibility(View.VISIBLE);
                nextloadingcontainer.setVisibility(View.INVISIBLE);
                //forgotorcontainer.setVisibility(View.VISIBLE);
                alreadyhaveactivationbutton.setVisibility(View.VISIBLE);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Animation sliderighttoleftanim = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.slidelefttoright);
                        sliderighttoleftanim.setFillAfter(true);
                        forgottitle.startAnimation(sliderighttoleftanim);
                    }
                },400);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Animation sliderighttoleftanim = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.slidelefttoright);
                        sliderighttoleftanim.setFillAfter(true);
                        circularimagecontainer.startAnimation(sliderighttoleftanim);
                    }
                },500);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Animation sliderighttoleftanim = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.slidelefttoright);
                        sliderighttoleftanim.setFillAfter(true);
                        successimagecontainer.startAnimation(sliderighttoleftanim);
                    }
                },600);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Animation sliderighttoleftanim = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.slidelefttoright);
                        sliderighttoleftanim.setFillAfter(true);
                        enteremailtext.startAnimation(sliderighttoleftanim);
                    }
                },700);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Animation sliderighttoleftanim = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.slidelefttoright);
                        sliderighttoleftanim.setFillAfter(true);
                        emailcontainer.startAnimation(sliderighttoleftanim);
                    }
                },800);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Animation sliderighttoleftanim = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.slidelefttoright);
                        sliderighttoleftanim.setFillAfter(true);
                        nextarrow.startAnimation(sliderighttoleftanim);
                        nextloadingcontainer.startAnimation(sliderighttoleftanim);
                    }
                },900);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Animation sliderighttoleftanim = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.slidelefttoright);
                        sliderighttoleftanim.setFillAfter(true);
                        //forgotorcontainer.startAnimation(sliderighttoleftanim);
                    }
                },1000);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Animation sliderighttoleftanim = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.slidelefttoright);
                        sliderighttoleftanim.setFillAfter(true);
                        alreadyhaveactivationbutton.startAnimation(sliderighttoleftanim);
                    }
                },1100);

                nextarrow.setClickable(true);
                alreadyhaveactivationbutton.setClickable(true);
                emailedittext.setEnabled(true);

            }else if(isemailvalidateclosed==true){

                nextarrow.setClickable(true);
                alreadyhaveactivationbutton.setClickable(true);
                emailedittext.setEnabled(true);

                confirmationtitle.setVisibility(View.INVISIBLE);
                successimagecontainer.setVisibility(View.INVISIBLE);
                successimage.setVisibility(View.INVISIBLE);
                successmessage.setVisibility(View.INVISIBLE);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Animation sliderighttoleftanim = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.slidelefttoright);
                        sliderighttoleftanim.setFillAfter(true);
                        forgottitle.startAnimation(sliderighttoleftanim);
                    }
                },400);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Animation sliderighttoleftanim = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.slidelefttoright);
                        sliderighttoleftanim.setFillAfter(true);
                        circularimagecontainer.startAnimation(sliderighttoleftanim);
                    }
                },500);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Animation sliderighttoleftanim = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.slidelefttoright);
                        sliderighttoleftanim.setFillAfter(true);
                        successimagecontainer.startAnimation(sliderighttoleftanim);
                    }
                },600);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Animation sliderighttoleftanim = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.slidelefttoright);
                        sliderighttoleftanim.setFillAfter(true);
                        enteremailtext.startAnimation(sliderighttoleftanim);
                    }
                },700);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Animation sliderighttoleftanim = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.slidelefttoright);
                        sliderighttoleftanim.setFillAfter(true);
                        emailcontainer.startAnimation(sliderighttoleftanim);
                    }
                },800);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Animation sliderighttoleftanim = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.slidelefttoright);
                        sliderighttoleftanim.setFillAfter(true);
                        nextarrow.startAnimation(sliderighttoleftanim);
                        nextloadingcontainer.startAnimation(sliderighttoleftanim);
                    }
                },900);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Animation sliderighttoleftanim = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.slidelefttoright);
                        sliderighttoleftanim.setFillAfter(true);
                        //forgotorcontainer.startAnimation(sliderighttoleftanim);
                    }
                },1000);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Animation sliderighttoleftanim = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.slidelefttoright);
                        sliderighttoleftanim.setFillAfter(true);
                        alreadyhaveactivationbutton.startAnimation(sliderighttoleftanim);
                    }
                },1100);
            }else {

                nextarrow.setClickable(true);
                alreadyhaveactivationbutton.setClickable(true);
                emailedittext.setEnabled(true);

                forgottitle.setVisibility(View.VISIBLE);
                circularimagecontainer.setVisibility(View.VISIBLE);
                successimagecontainer.setVisibility(View.VISIBLE);
                enteremailtext.setVisibility(View.VISIBLE);
                emailcontainer.setVisibility(View.VISIBLE);
                nextarrow.setVisibility(View.VISIBLE);
                nextloadingcontainer.setVisibility(View.INVISIBLE);
                //forgotorcontainer.setVisibility(View.VISIBLE);
                alreadyhaveactivationbutton.setVisibility(View.VISIBLE);
            }

            emailedittext.setError(null);
            loginpagecontainer.setVisibility(View.GONE);
        }

    }


    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public boolean containNumbersOnly(String source){
        boolean result = false;
        Pattern pattern = Pattern.compile("[0-9]+.[0-9]+"); //correct pattern for both float and integer.
        pattern = Pattern.compile("\\d+.\\d+"); //correct pattern for both float and integer.

        result = pattern.matcher(source).matches();

        return result;
    }

    private boolean validate() {
        boolean valid = true;

        String usernamevalue = usernameedit.getText().toString().trim();
        String passwordvalue = passwordedit.getText().toString().trim();

        if (!(isEmailValid(usernamevalue))) {

            if(!(containNumbersOnly(usernamevalue)) || usernamevalue.length() < 5 || usernamevalue.length() > 13) {
                usernameedit.setError("Enter a Valid Email Address or Phone Number");
                valid = false;
            }else{
                usernameedit.setError(null);
            }

        } else {
            usernameedit.setError(null);
        }


        if (passwordvalue.isEmpty() || passwordvalue.length() < 5 || passwordedit.length() > 10) {
            passwordedit.setError("Password should be between 5 to 10 Alphanumeric Characters");
            valid = false;
        } else {
            passwordedit.setError(null);
        }

        return valid;

    }


    public void onLoginSuccess() {

        deleteCache(this);

        final String edittextusername = usernameedit.getText().toString().trim();
        final String edittextpassword = passwordedit.getText().toString().trim();

        if (!(edittextusername.equals("")) && !(edittextpassword.equals("")))
        {

            StringRequest stringRequest = new StringRequest(Request.Method.POST, loginconfig.key_login_url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            JSONObject j = null;
                            try {
                                j = new JSONObject(s);
                                loginresult = j.optJSONArray("result");


                                if(loginresult.length()>0)
                                {
                                    JSONObject json = loginresult.getJSONObject(0);

                                    String id = json.optString("id");
                                    String arrusername = json.optString("username");
                                    String arrusertypeid = json.optString("usertypeid");
                                    String arruserimage = json.optString("userimage");
                                    String arrloginname = json.optString("loginname");
                                    String arrpassword = json.optString("password");
                                    String arrname = json.optString("name");

                                    sharedPreferences = getSharedPreferences(MY_PREFERENCES, MainActivity.MODE_PRIVATE);
                                    previoussharedPreferences = getSharedPreferences(Previous_PREFERENCES, MainActivity.MODE_PRIVATE);

                                    //creating editor to store values of shared preferences
                                    editor = sharedPreferences.edit();
                                    previouseditor = previoussharedPreferences.edit();

                                    editor.putBoolean(loginconfig.login_success, true);
                                    editor.putString(loginconfig.key_userid, id);
                                    editor.putString(loginconfig.key_username, arrusername);
                                    editor.putString(loginconfig.key_username, arrusername);
                                    editor.putString(loginconfig.key_usertypeid, arrusertypeid);
                                    editor.putString(loginconfig.key_userimage, arruserimage);
                                    editor.putString(loginconfig.key_loginname, arrloginname);
                                    editor.putString(loginconfig.key_password, arrpassword);
                                    editor.putString(loginconfig.key_name, arrname);
                                    editor.apply();


                                    previoussharedPreferences = getSharedPreferences(Previous_PREFERENCES, MainActivity.MODE_PRIVATE);

                                    //creating editor to store values of shared preferences
                                    previouseditor = previoussharedPreferences.edit();

                                    // Clearing all data from Shared Preferences
                                    previouseditor.clear();

                                    //Saving the sharedpreferences
                                    previouseditor.commit();

                                    previouseditor.putBoolean(previousloginconfig.login_success, true);
                                    previouseditor.putString(previousloginconfig.key_userid, id);
                                    previouseditor.putString(previousloginconfig.key_username, arrusername);
                                    previouseditor.putString(previousloginconfig.key_usertypeid, arrusertypeid);
                                    previouseditor.putString(previousloginconfig.key_userimage, arruserimage);
                                    previouseditor.putString(previousloginconfig.key_loginname, arrloginname);
                                    previouseditor.putString(previousloginconfig.key_name, arrname);
                                    previouseditor.putString(previousloginconfig.key_password, arrpassword);
                                    previouseditor.putString(previousloginconfig.isloggedinalready, "true");
                                    previouseditor.apply();

                                    signinbutton.setVisibility(View.VISIBLE);
                                    usernameedit.setEnabled(true);
                                    passwordedit.setEnabled(true);
                                    loadingcontainer.setVisibility(View.INVISIBLE);

                                    sharedPreferences = getSharedPreferences(MY_PREFERENCES, MainActivity.MODE_PRIVATE);
                                    final String nusertypeid = sharedPreferences.getString(loginconfig.key_usertypeid, "");

                                    if(nusertypeid.equals("2")) {
                                        getschoolname();
                                    }else if(nusertypeid.equals("3")||nusertypeid.equals("4")||nusertypeid.equals("9")||nusertypeid.equals("10")) {
                                        getschoolnameforparentsandstudents();
                                    }

                                    /*if(nusertypeid.equals("2")) {
                                        getschoolname();
                                    }else if(nusertypeid.equals("3")||nusertypeid.equals("4")){
                                        getdashboarditems();
                                    }*/

                                }
                                else
                                {
                                    signinbutton.setVisibility(View.VISIBLE);
                                    usernameedit.setEnabled(true);
                                    passwordedit.setEnabled(true);
                                    loadingcontainer.setVisibility(View.INVISIBLE);
                                    snackbar = Snackbar.make(view,"Invalid Username or Password",Snackbar.LENGTH_LONG);
                                    snackbar.show();
                                }

                            } catch (JSONException e) {
                                signinbutton.setVisibility(View.VISIBLE);
                                usernameedit.setEnabled(true);
                                passwordedit.setEnabled(true);
                                loadingcontainer.setVisibility(View.INVISIBLE);
                                snackbar = Snackbar.make(view,"Unable to recieve data from server due to Technical issues.",Snackbar.LENGTH_LONG);
                                snackbar.show();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    signinbutton.setVisibility(View.VISIBLE);
                    usernameedit.setEnabled(true);
                    passwordedit.setEnabled(true);
                    loadingcontainer.setVisibility(View.INVISIBLE);
                    snackbar = Snackbar.make(view,"Unable to Connect with server due to Technical Issues. Kindly Contact Administrator for more details.",Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            })

            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();

                    params.clear();

                    params.put(loginconfig.key_username, edittextusername);
                    params.put(loginconfig.key_password, edittextpassword);

                    return params;
                }

            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }

    }



    private void getschoolname() {

        deleteCache(this);

        sharedPreferences = getSharedPreferences(MY_PREFERENCES, MainActivity.MODE_PRIVATE);
        final String userid = sharedPreferences.getString(loginconfig.key_userid, "");
        final String usertypeid = sharedPreferences.getString(loginconfig.key_usertypeid, "");

        StringRequest newschoolrequest = new StringRequest(Request.Method.POST, loginconfig.key_Schooldetails_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        JSONObject j = null;
                        try
                        {
                            j = new JSONObject(s);
                            loginresult = j.optJSONArray("result");


                            if(loginresult.length()>0) {
                                for (int i = 0; i < loginresult.length(); i++) {

                                    JSONObject json = loginresult.getJSONObject(i);

                                    String id = json.optString("school_id");
                                    String schoolname = json.optString("school_name");

                                    sharedPreferences = getSharedPreferences(MY_PREFERENCES, MainActivity.MODE_PRIVATE);

                                    //creating editor to store values of shared preferences
                                    editor = sharedPreferences.edit();

                                    editor.putString(loginconfig.key_school_id, id);
                                    editor.putString(loginconfig.key_school_name, schoolname);
                                    editor.apply();

                                    getclasslist();

                                }

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(LoginActivity.this, "Connection Response Error", Toast.LENGTH_LONG).show();
            }
        })

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.clear();

                params.put(loginconfig.key_userid, userid);
                params.put(loginconfig.key_usertypeid, usertypeid);

                return params;
            }
        };
        RequestQueue schoolrequestque = Volley.newRequestQueue(this);
        schoolrequestque.add(newschoolrequest);
    }

    private void getschoolnameforparentsandstudents() {

        deleteCache(this);

        sharedPreferences = getSharedPreferences(MY_PREFERENCES, MainActivity.MODE_PRIVATE);
        final String userid = sharedPreferences.getString(loginconfig.key_userid, "");
        final String usertypeid = sharedPreferences.getString(loginconfig.key_usertypeid, "");

        StringRequest newschoolrequestparent = new StringRequest(Request.Method.POST, loginconfig.key_Schooldetails_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        JSONObject j = null;
                        try
                        {
                            j = new JSONObject(s);
                            loginresult = j.optJSONArray("result");


                            if(loginresult.length()>0) {
                                for (int i = 0; i < loginresult.length(); i++) {

                                    JSONObject json = loginresult.getJSONObject(i);

                                    String id = json.optString("school_id");
                                    String schoolname = json.optString("school_name");

                                    sharedPreferences = getSharedPreferences(MY_PREFERENCES, MainActivity.MODE_PRIVATE);

                                    //creating editor to store values of shared preferences
                                    editor = sharedPreferences.edit();

                                    editor.putString(loginconfig.key_school_id, id);
                                    editor.putString(loginconfig.key_school_name, schoolname);
                                    editor.apply();

                                    Intent parentpage = new Intent(LoginActivity.this,Teacher_Dashboard_Activity.class);
                                    startActivity(parentpage);
                                    finish();

                                }

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(LoginActivity.this, "Connection Response Error", Toast.LENGTH_LONG).show();
            }
        })

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.clear();

                params.put(loginconfig.key_userid, userid);
                params.put(loginconfig.key_usertypeid, usertypeid);

                return params;
            }
        };
        RequestQueue schoolrequestqueparent = Volley.newRequestQueue(this);
        schoolrequestqueparent.add(newschoolrequestparent);
    }


    private void getclasslist() {

        sharedPreferences = getSharedPreferences(MY_PREFERENCES, MainActivity.MODE_PRIVATE);
        final String classschoolid = sharedPreferences.getString(loginconfig.key_school_id, "");
        final String userid = sharedPreferences.getString(loginconfig.key_userid, "");
        final String school = classschoolid;

        StringRequest classstringrequest = new StringRequest(Request.Method.POST, loginconfig.key_Classdetails_url,

                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        JSONObject j =null;
                        try
                        {
                            j = new JSONObject(response);
                            loginresult = j.optJSONArray("result");

                            sharedPreferences = getSharedPreferences(MY_PREFERENCES, MainActivity.MODE_PRIVATE);

                            //creating editor to store values of shared preferences
                            editor = sharedPreferences.edit();

                            editor.putString(loginconfig.key_class_name, loginresult.toString());
                            editor.apply();

                            Intent i = new Intent(LoginActivity.this,Teacher_Dashboard_Activity.class);
                            startActivity(i);
                            finish();


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError volleyError)
                    {
                        Toast.makeText(LoginActivity.this, "Connection Response Error", Toast.LENGTH_LONG).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.clear();
                params.put(loginconfig.key_school_id,school);
                params.put(loginconfig.key_userid,userid);
                return params;
            }
        };
        RequestQueue classr1 = Volley.newRequestQueue(this);
        classr1.add(classstringrequest);
    }


    private boolean emailvalidate() {
        boolean valid = true;

        String emailvalidationvalue = emailedittext.getText().toString().trim();

        if (!(isEmailValid(emailvalidationvalue))) {

            emailedittext.setError("Enter a Valid Email Address");
            valid = false;
        } else {
            emailedittext.setError(null);
        }

        return valid;

    }

    private boolean verifyemailvalidate() {
        boolean valid = true;

        String emailvalidationvalue = verificationemailedittext.getText().toString().trim();

        if (!(isEmailValid(emailvalidationvalue))) {

            verificationemailedittext.setError("Enter a Valid Email Address");
            valid = false;
        } else {
            verificationemailedittext.setError(null);
        }

        return valid;

    }

    private boolean verifyemptyverification() {
        boolean valid = true;

        String firstvalue = firstverificationinput.getText().toString().trim();
        String secondvalue = secondverificationinput.getText().toString().trim();
        String thirdvalue = thirdverificationinput.getText().toString().trim();
        String fourthvalue = fourthverificationinput.getText().toString().trim();

        if (firstvalue.equals("") || secondvalue.equals("") || thirdvalue.equals("") || fourthvalue.equals("")) {
            fourthverificationinput.setError("Enter a verification value and then continue");
            valid = false;
        } else {
            fourthverificationinput.setError(null);
        }

        return valid;

    }

    private boolean verifypasswordmatch() {
        boolean valid = true;

        String firstvalue = changepasswordedittext.getText().toString().trim();
        String secondvalue = retypechangepasswordedittext.getText().toString().trim();

        if (!(firstvalue.equals(secondvalue))) {
            retypechangepasswordedittext.setError("Password and Re-Type Password must match");
            valid = false;
        } else if(firstvalue.length()!= secondvalue.length()) {
            retypechangepasswordedittext.setError("Password and Re-Type Password must match");
            valid = false;
        }else {
            retypechangepasswordedittext.setError(null);
        }

        return valid;

    }

    public void onsuccessemailvalidation() {

        deleteCache(this);

        final String forgotemailid = emailedittext.getText().toString().trim();

        if (!(forgotemailid.equals("")))
        {

            StringRequest fortemailcheckrequest = new StringRequest(Request.Method.POST, loginconfig.key_forgot_emailcheck_url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            JSONObject j = null;
                            try {
                                j = new JSONObject(s);
                                loginresult = j.optJSONArray("result");


                                if(loginresult.length()>0)
                                {
                                    JSONObject json = loginresult.getJSONObject(0);

                                    String resultvalue = json.optString("emailcheckresult");

                                    if(resultvalue.equals("1")){

                                        new Handler().postDelayed(new Runnable() {
                                              @Override
                                              public void run() {
                                                  Animation sliderighttoleftanim = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.sliderighttoleft);
                                                  sliderighttoleftanim.setFillAfter(true);
                                                  forgottitle.startAnimation(sliderighttoleftanim);
                                              }
                                        },400);

                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                Animation sliderighttoleftanim = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.sliderighttoleft);
                                                sliderighttoleftanim.setFillAfter(true);
                                                circularimagecontainer.startAnimation(sliderighttoleftanim);
                                            }
                                        },500);

                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                Animation sliderighttoleftanim = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.sliderighttoleft);
                                                sliderighttoleftanim.setFillAfter(true);
                                                successimagecontainer.startAnimation(sliderighttoleftanim);
                                            }
                                        },600);

                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                Animation sliderighttoleftanim = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.sliderighttoleft);
                                                sliderighttoleftanim.setFillAfter(true);
                                                enteremailtext.startAnimation(sliderighttoleftanim);
                                            }
                                        },700);

                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                Animation sliderighttoleftanim = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.sliderighttoleft);
                                                sliderighttoleftanim.setFillAfter(true);
                                                emailcontainer.startAnimation(sliderighttoleftanim);
                                            }
                                        },800);

                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                Animation sliderighttoleftanim = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.sliderighttoleft);
                                                sliderighttoleftanim.setFillAfter(true);
                                                nextarrow.startAnimation(sliderighttoleftanim);
                                                nextarrow.setVisibility(View.INVISIBLE);
                                                nextloadingcontainer.startAnimation(sliderighttoleftanim);

                                            }
                                        },900);

                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                Animation sliderighttoleftanim = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.sliderighttoleft);
                                                sliderighttoleftanim.setFillAfter(true);
                                                //forgotorcontainer.startAnimation(sliderighttoleftanim);
                                            }
                                        },1000);

                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                Animation sliderighttoleftanim = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.sliderighttoleft);
                                                sliderighttoleftanim.setFillAfter(true);
                                                alreadyhaveactivationbutton.startAnimation(sliderighttoleftanim);
                                            }
                                        },1100);



                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                //forgotpasswordfirstpage.setVisibility(View.GONE);
                                                forgotisOpen = true;
                                            }
                                        },1500);


                                        isemailvalidateclicked = true;


                                        sendotp();

                                        /*nextarrow.setVisibility(View.VISIBLE);
                                        nextloadingcontainer.setVisibility(View.INVISIBLE);
                                        snackbar = Snackbar.make(forgotpasswordview,"Email id is valid and linked",Snackbar.LENGTH_LONG);
                                        snackbar.show();*/
                                    }else{
                                        nextarrow.setVisibility(View.VISIBLE);
                                        nextloadingcontainer.setVisibility(View.INVISIBLE);
                                        emailedittext.setError("This email id is not linked with Classcubby, Contact your college administration for getting a new account in your college");
                                        //snackbar = Snackbar.make(forgotpasswordview,"This email id is not linked with Classcubby, Contact your college administration for getting a new account in your college",Snackbar.LENGTH_LONG);
                                        //snackbar.show();
                                    }

                                } else {
                                    nextarrow.setVisibility(View.VISIBLE);
                                    nextloadingcontainer.setVisibility(View.INVISIBLE);
                                    snackbar = Snackbar.make(forgotpasswordview,"IUnable to Process right now. Pleasee try again",Snackbar.LENGTH_LONG);
                                    snackbar.show();
                                }

                            } catch (JSONException e) {
                                nextarrow.setVisibility(View.VISIBLE);
                                nextloadingcontainer.setVisibility(View.INVISIBLE);
                                snackbar = Snackbar.make(forgotpasswordview,"Unable to recieve data from server due to Technical issues.",Snackbar.LENGTH_LONG);
                                snackbar.show();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    nextarrow.setVisibility(View.VISIBLE);
                    nextloadingcontainer.setVisibility(View.INVISIBLE);
                    snackbar = Snackbar.make(forgotpasswordview,"Unable to Connect with server due to Technical Issues. Kindly Contact Administrator for more details.",Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            })

            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();

                    params.clear();

                    params.put(loginconfig.key_forgotemailid, forgotemailid);

                    return params;
                }

            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(fortemailcheckrequest);
        }

    }



    public void onsuccesschangepassword() {

        deleteCache(this);

        sharedPreferences = getSharedPreferences(MY_PREFERENCES, MainActivity.MODE_PRIVATE);
        final String loggedinid;

        if(isalreadyverificationvalidationclicked == true){
            loggedinid = verificationemailedittext.getText().toString().trim();
        }else{
            loggedinid = emailedittext.getText().toString().trim();
        }

        final String changepasswordvalue = changepasswordedittext.getText().toString().trim();

        if (!(changepasswordvalue.equals("")))
        {

            StringRequest changepasswordrequest = new StringRequest(Request.Method.POST, loginconfig.key_forgot_changepassword_url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            JSONObject j = null;
                            try {
                                j = new JSONObject(s);
                                loginresult = j.optJSONArray("result");


                                if(loginresult.length()>0)
                                {
                                    JSONObject json = loginresult.getJSONObject(0);

                                    String resultvalue = json.optString("otpsuccessresult");

                                    if(resultvalue.equals("1")){

                                        new Handler().postDelayed(new Runnable() {
                                              @Override
                                              public void run() {
                                                  Animation sliderighttoleftanim = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.sliderighttoleft);
                                                  sliderighttoleftanim.setFillAfter(true);
                                                  changetitle.startAnimation(sliderighttoleftanim);
                                              }
                                        },400);

                                        new Handler().postDelayed(new Runnable() {
                                              @Override
                                              public void run() {
                                                  Animation sliderighttoleftanim = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.sliderighttoleft);
                                                  sliderighttoleftanim.setFillAfter(true);
                                                  changeimagecontainer.startAnimation(sliderighttoleftanim);
                                              }
                                        },500);

                                        new Handler().postDelayed(new Runnable() {
                                              @Override
                                              public void run() {
                                                  Animation sliderighttoleftanim = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.sliderighttoleft);
                                                  sliderighttoleftanim.setFillAfter(true);
                                                  changesuccessimage.startAnimation(sliderighttoleftanim);
                                              }
                                        },600);

                                        new Handler().postDelayed(new Runnable() {
                                              @Override
                                              public void run() {
                                                  Animation sliderighttoleftanim = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.sliderighttoleft);
                                                  sliderighttoleftanim.setFillAfter(true);
                                                  changesuccessmessage.startAnimation(sliderighttoleftanim);
                                              }
                                        },700);

                                        new Handler().postDelayed(new Runnable() {
                                              @Override
                                              public void run() {
                                                  Animation sliderighttoleftanim = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.sliderighttoleft);
                                                  sliderighttoleftanim.setFillAfter(true);
                                                  changepasswordoverallcontainer.startAnimation(sliderighttoleftanim);
                                              }
                                        },800);

                                        new Handler().postDelayed(new Runnable() {
                                              @Override
                                              public void run() {
                                                  thirdpagenextarrow.setClickable(false);
                                                  thirdpagenextarrow.setVisibility(View.INVISIBLE);
                                                  Animation sliderighttoleftanim = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.sliderighttoleft);
                                                  sliderighttoleftanim.setFillAfter(true);
                                                  thirdpagenextarrow.startAnimation(sliderighttoleftanim);
                                              }
                                        },900);

                                        new Handler().postDelayed(new Runnable() {
                                              @Override
                                              public void run() {
                                                  thirdnextloadingcontainer.setClickable(false);
                                                  thirdnextloadingcontainer.setVisibility(View.INVISIBLE);
                                                  Animation sliderighttoleftanim = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.sliderighttoleft);
                                                  sliderighttoleftanim.setFillAfter(true);
                                                  thirdnextloadingcontainer.startAnimation(sliderighttoleftanim);
                                              }
                                        },1000);

                                        new Handler().postDelayed(new Runnable() {
                                              @Override
                                              public void run() {
                                                  changesuccesstitle.setVisibility(View.VISIBLE);
                                                  Animation sliderighttoleftanim = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.slidelefttoright);
                                                  sliderighttoleftanim.setFillAfter(true);
                                                  changesuccesstitle.startAnimation(sliderighttoleftanim);
                                              }
                                        },1100);

                                        new Handler().postDelayed(new Runnable() {
                                              @Override
                                              public void run() {
                                                  changeimagesuccesscontainer.setVisibility(View.VISIBLE);
                                                  passsuccessimage.setVisibility(View.VISIBLE);

                                                  String value = "Success";
                                                  int animicon;

                                                  if(value.equals("Success")){
                                                      animicon = R.drawable.tickanimation;
                                                  }else{
                                                      animicon = R.drawable.failedicon;
                                                  }
                                                  Glide.with(LoginActivity.this).load(animicon).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).crossFade().into(passsuccessimage);

                                                  Animation sliderighttoleftanim = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.slidelefttoright);
                                                  sliderighttoleftanim.setFillAfter(true);
                                                  changeimagesuccesscontainer.startAnimation(sliderighttoleftanim);
                                              }
                                        },1200);

                                        new Handler().postDelayed(new Runnable() {
                                              @Override
                                              public void run() {
                                                  passchangesuccessmessage.setVisibility(View.VISIBLE);
                                                  Animation sliderighttoleftanim = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.slidelefttoright);
                                                  sliderighttoleftanim.setFillAfter(true);
                                                  passchangesuccessmessage.startAnimation(sliderighttoleftanim);
                                              }
                                        },1300);

                                        new Handler().postDelayed(new Runnable() {
                                              @Override
                                              public void run() {
                                                  gotologinbutton.setVisibility(View.VISIBLE);
                                                  gotologinbutton.setClickable(true);
                                                  Animation sliderighttoleftanim = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.slidelefttoright);
                                                  sliderighttoleftanim.setFillAfter(true);
                                                  gotologinbutton.startAnimation(sliderighttoleftanim);
                                              }
                                        },1400);



                                    }else{
                                        thirdpagenextarrow.setVisibility(View.VISIBLE);
                                        thirdnextloadingcontainer.setVisibility(View.INVISIBLE);
                                        //emailedittext.setError("This email id is not linked with Classcubby, Contact your college administration for getting a new account in your college");
                                        snackbar = Snackbar.make(forgotpasswordview,"Unable to Change Password now. Please try again Later!",Snackbar.LENGTH_LONG);
                                        snackbar.show();
                                    }

                                } else {
                                    thirdpagenextarrow.setVisibility(View.VISIBLE);
                                    thirdnextloadingcontainer.setVisibility(View.INVISIBLE);
                                    snackbar = Snackbar.make(forgotpasswordview,"Unable to Process right now. Please try again",Snackbar.LENGTH_LONG);
                                    snackbar.show();
                                }

                            } catch (JSONException e) {
                                thirdpagenextarrow.setVisibility(View.VISIBLE);
                                thirdnextloadingcontainer.setVisibility(View.INVISIBLE);
                                snackbar = Snackbar.make(forgotpasswordview,"Unable to recieve data from server due to Technical issues.",Snackbar.LENGTH_LONG);
                                snackbar.show();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    thirdpagenextarrow.setVisibility(View.VISIBLE);
                    thirdnextloadingcontainer.setVisibility(View.INVISIBLE);
                    snackbar = Snackbar.make(forgotpasswordview,"Unable to Connect with server due to Technical Issues. Kindly Contact Administrator for more details.",Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            })

            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();

                    params.clear();

                    params.put(loginconfig.key_userid, loggedinid);
                    params.put(loginconfig.key_changepasswordvalue, changepasswordvalue);

                    return params;
                }

            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(changepasswordrequest);
        }

    }


    public void onsuccessverificationvalidation(){
        deleteCache(this);

        final String verificationforgotemailid;

        if(isalreadyverificationvalidationclicked == true){
            verificationforgotemailid = verificationemailedittext.getText().toString().trim();
        }else{
            verificationforgotemailid = emailedittext.getText().toString().trim();
        }

        final String verificationnumber = firstverificationinput.getText().toString().trim() + secondverificationinput.getText().toString().trim() + thirdverificationinput.getText().toString().trim()+fourthverificationinput.getText().toString().trim();

        if (!(verificationforgotemailid.equals(""))){
            StringRequest fortemailcheckrequest = new StringRequest(Request.Method.POST, loginconfig.key_forgot_checkotp_url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            JSONObject j = null;
                            try {
                                j = new JSONObject(s);
                                loginresult = j.optJSONArray("result");


                                if(loginresult.length()>0)
                                {
                                    JSONObject json = loginresult.getJSONObject(0);

                                    String resultvalue = json.optString("otpsuccessresult");

                                    if(resultvalue.equals("1")){
                                        secondpagenextarrow.setVisibility(View.VISIBLE);
                                        secondnextloadingcontainer.setVisibility(View.INVISIBLE);

                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                confirmationtitle.setVisibility(View.INVISIBLE);
                                                Animation sliderighttoleftanim = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.sliderighttoleft);
                                                sliderighttoleftanim.setFillAfter(true);
                                                confirmationtitle.startAnimation(sliderighttoleftanim);
                                            }
                                        },100);

                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                successimagecontainer.setVisibility(View.INVISIBLE);
                                                Animation sliderighttoleftanim = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.sliderighttoleft);
                                                sliderighttoleftanim.setFillAfter(true);
                                                successimagecontainer.startAnimation(sliderighttoleftanim);
                                            }
                                        },200);

                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                successmessage.setVisibility(View.INVISIBLE);
                                                Animation sliderighttoleftanim = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.sliderighttoleft);
                                                sliderighttoleftanim.setFillAfter(true);
                                                successmessage.startAnimation(sliderighttoleftanim);
                                            }
                                        },300);

                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                emailidentercontainer.setVisibility(View.INVISIBLE);
                                                Animation sliderighttoleftanim = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.sliderighttoleft);
                                                sliderighttoleftanim.setFillAfter(true);
                                                emailidentercontainer.startAnimation(sliderighttoleftanim);
                                            }
                                        },400);

                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                verificationinputfieldoverallcontainer.setVisibility(View.INVISIBLE);
                                                Animation sliderighttoleftanim = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.sliderighttoleft);
                                                sliderighttoleftanim.setFillAfter(true);
                                                verificationinputfieldoverallcontainer.startAnimation(sliderighttoleftanim);
                                            }
                                        },500);

                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                secondpagenextarrow.setVisibility(View.INVISIBLE);
                                                secondpagenextarrow.setClickable(false);
                                                Animation sliderighttoleftanim = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.sliderighttoleft);
                                                sliderighttoleftanim.setFillAfter(true);
                                                secondpagenextarrow.startAnimation(sliderighttoleftanim);
                                            }
                                        },600);

                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                secondnextloadingcontainer.setVisibility(View.INVISIBLE);
                                                Animation sliderighttoleftanim = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.sliderighttoleft);
                                                sliderighttoleftanim.setFillAfter(true);
                                                secondnextloadingcontainer.startAnimation(sliderighttoleftanim);
                                            }
                                        },700);



                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                changetitle.setVisibility(View.VISIBLE);
                                                Animation sliderighttoleftanim = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.slidelefttoright);
                                                sliderighttoleftanim.setFillAfter(true);
                                                changetitle.startAnimation(sliderighttoleftanim);
                                            }
                                        },800);

                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                changeimagecontainer.setVisibility(View.VISIBLE);
                                                changesuccessimage.setVisibility(View.VISIBLE);

                                                String value = "Success";
                                                int animicon;

                                                if(value.equals("Success")){
                                                    animicon = R.drawable.tickanimation;
                                                }else{
                                                    animicon = R.drawable.failedicon;
                                                }
                                                Glide.with(LoginActivity.this).load(animicon).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).crossFade().into(changesuccessimage);


                                                Animation sliderighttoleftanim = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.slidelefttoright);
                                                sliderighttoleftanim.setFillAfter(true);
                                                changeimagecontainer.startAnimation(sliderighttoleftanim);
                                            }
                                        },900);

                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                changesuccessmessage.setVisibility(View.VISIBLE);
                                                Animation sliderighttoleftanim = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.slidelefttoright);
                                                sliderighttoleftanim.setFillAfter(true);
                                                changesuccessmessage.startAnimation(sliderighttoleftanim);
                                            }
                                        },1000);

                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                changepasswordoverallcontainer.setVisibility(View.VISIBLE);
                                                Animation sliderighttoleftanim = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.slidelefttoright);
                                                sliderighttoleftanim.setFillAfter(true);
                                                changepasswordoverallcontainer.startAnimation(sliderighttoleftanim);
                                            }
                                        },1100);

                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                thirdpagenextarrow.setVisibility(View.VISIBLE);
                                                thirdpagenextarrow.setClickable(true);
                                                Animation sliderighttoleftanim = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.slidelefttoright);
                                                sliderighttoleftanim.setFillAfter(true);
                                                thirdpagenextarrow.startAnimation(sliderighttoleftanim);
                                            }
                                        },1200);

                                    }else{
                                        secondpagenextarrow.setVisibility(View.VISIBLE);
                                        secondnextloadingcontainer.setVisibility(View.INVISIBLE);
                                        snackbar = Snackbar.make(forgotpasswordview,"Verification Failed",Snackbar.LENGTH_LONG);
                                        snackbar.show();
                                    }

                                } else {
                                    secondpagenextarrow.setVisibility(View.VISIBLE);
                                    secondnextloadingcontainer.setVisibility(View.INVISIBLE);
                                    snackbar = Snackbar.make(forgotpasswordview,"Unable to Process right now. Pleasee try again",Snackbar.LENGTH_LONG);
                                    snackbar.show();
                                }

                            } catch (JSONException e) {
                                secondpagenextarrow.setVisibility(View.VISIBLE);
                                secondnextloadingcontainer.setVisibility(View.INVISIBLE);
                                snackbar = Snackbar.make(forgotpasswordview,"Unable to recieve data from server due to Technical issues.",Snackbar.LENGTH_LONG);
                                snackbar.show();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    secondpagenextarrow.setVisibility(View.VISIBLE);
                    secondnextloadingcontainer.setVisibility(View.INVISIBLE);
                    snackbar = Snackbar.make(forgotpasswordview,"Unable to Connect with server due to Technical Issues. Kindly Contact Administrator for more details.",Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            })

            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();

                    params.clear();

                    params.put(loginconfig.key_checkemailid, verificationforgotemailid);
                    params.put(loginconfig.key_checknumber, verificationnumber);

                    return params;
                }

            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(fortemailcheckrequest);

        }


    }

    public void sendotp() {

        deleteCache(this);

        final String verificationforgotemailid = emailedittext.getText().toString().trim();

        if (!(verificationforgotemailid.equals("")))
        {

            StringRequest fortemailcheckrequest = new StringRequest(Request.Method.POST, loginconfig.key_forgot_sendotp_url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            JSONObject j = null;
                            try {
                                j = new JSONObject(s);
                                loginresult = j.optJSONArray("result");


                                if(loginresult.length()>0)
                                {
                                    JSONObject json = loginresult.getJSONObject(0);

                                    String resultvalue = json.optString("otpsuccessresult");

                                    sharedPreferences = getSharedPreferences(MY_PREFERENCES, MainActivity.MODE_PRIVATE);
                                    previoussharedPreferences = getSharedPreferences(Previous_PREFERENCES, MainActivity.MODE_PRIVATE);

                                    //creating editor to store values of shared preferences
                                    editor = sharedPreferences.edit();
                                    previouseditor = previoussharedPreferences.edit();

                                    editor.putString(loginconfig.key_emailsentstatus, resultvalue);

                                    editor.apply();

                                    if(resultvalue.equals("1")){

                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                confirmationtitle.setVisibility(View.VISIBLE);
                                                Animation sliderighttoleftanim = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.slidelefttoright);
                                                sliderighttoleftanim.setFillAfter(true);
                                                confirmationtitle.startAnimation(sliderighttoleftanim);
                                            }
                                        },1800);

                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                successimagecontainer.setVisibility(View.VISIBLE);
                                                successimage.setVisibility(View.VISIBLE);

                                                String value = "Success";
                                                int animicon;

                                                if(value.equals("Success")){
                                                    animicon = R.drawable.tickanimation;
                                                }else{
                                                    animicon = R.drawable.failedicon;
                                                }
                                                Glide.with(LoginActivity.this).load(animicon).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).crossFade().into(successimage);


                                                Animation sliderighttoleftanim = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.slidelefttoright);
                                                sliderighttoleftanim.setFillAfter(true);
                                                successimagecontainer.startAnimation(sliderighttoleftanim);
                                            }
                                        },1900);

                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                nextarrow.setClickable(false);
                                                alreadyhaveactivationbutton.setClickable(false);
                                                emailedittext.setEnabled(false);

                                                successmessage.setVisibility(View.VISIBLE);
                                                successmessage.setText("A confimation email has been sent to your mail Successfully. Kindly Check and enter the activation code below to reset password.");

                                                Animation sliderighttoleftanim = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.slidelefttoright);
                                                sliderighttoleftanim.setFillAfter(true);
                                                successmessage.startAnimation(sliderighttoleftanim);
                                            }
                                        },2000);


                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                emailidentercontainer.setVisibility(View.INVISIBLE);
                                                emailidentercontainer.setClickable(false);

                                                verificationinputfieldoverallcontainer.setVisibility(View.VISIBLE);
                                                verificationinputfieldoverallcontainer.setClickable(true);

                                                Animation sliderighttoleftanim = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.slidelefttoright);
                                                sliderighttoleftanim.setFillAfter(true);
                                                verificationinputfieldoverallcontainer.startAnimation(sliderighttoleftanim);
                                            }
                                        },2100);


                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                secondpagenextarrow.setVisibility(View.VISIBLE);
                                                secondpagenextarrow.setClickable(true);
                                                Animation sliderighttoleftanim = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.slidelefttoright);
                                                sliderighttoleftanim.setFillAfter(true);
                                                secondpagenextarrow.startAnimation(sliderighttoleftanim);
                                                secondnextloadingcontainer.startAnimation(sliderighttoleftanim);
                                            }
                                        },2200);


                                    }else if(resultvalue.equals("2")){

                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                confirmationtitle.setVisibility(View.VISIBLE);
                                                Animation sliderighttoleftanim = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.slidelefttoright);
                                                sliderighttoleftanim.setFillAfter(true);
                                                confirmationtitle.startAnimation(sliderighttoleftanim);
                                            }
                                        },1800);

                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                successimagecontainer.setVisibility(View.VISIBLE);
                                                successimage.setVisibility(View.VISIBLE);

                                                String value = "Success";
                                                int animicon;

                                                if(value.equals("Success")){
                                                    animicon = R.drawable.tickanimation;
                                                }else{
                                                    animicon = R.drawable.failedicon;
                                                }
                                                Glide.with(LoginActivity.this).load(animicon).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).crossFade().into(successimage);


                                                Animation sliderighttoleftanim = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.slidelefttoright);
                                                sliderighttoleftanim.setFillAfter(true);
                                                successimagecontainer.startAnimation(sliderighttoleftanim);
                                            }
                                        },1900);

                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                secondpagenextarrow.setClickable(false);
                                                alreadyhaveactivationbutton.setClickable(false);
                                                emailedittext.setEnabled(false);
                                                successmessage.setVisibility(View.VISIBLE);
                                                successmessage.setText("Already a verification email has been sent to "+verificationforgotemailid +" Please enter the verification code below to proceed.");
                                                Animation sliderighttoleftanim = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.slidelefttoright);
                                                sliderighttoleftanim.setFillAfter(true);
                                                successmessage.startAnimation(sliderighttoleftanim);
                                            }
                                        },2000);


                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                emailidentercontainer.setVisibility(View.INVISIBLE);
                                                emailidentercontainer.setClickable(false);
                                            }
                                        },2100);


                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                verificationinputfieldoverallcontainer.setVisibility(View.VISIBLE);
                                                verificationinputfieldoverallcontainer.setClickable(true);

                                                Animation sliderighttoleftanim = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.slidelefttoright);
                                                sliderighttoleftanim.setFillAfter(true);
                                                verificationinputfieldoverallcontainer.startAnimation(sliderighttoleftanim);
                                            }
                                        },2200);

                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                secondpagenextarrow.setVisibility(View.VISIBLE);
                                                secondpagenextarrow.setClickable(true);
                                                Animation sliderighttoleftanim = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.slidelefttoright);
                                                sliderighttoleftanim.setFillAfter(true);
                                                secondpagenextarrow.startAnimation(sliderighttoleftanim);
                                                secondnextloadingcontainer.startAnimation(sliderighttoleftanim);
                                            }
                                        },2200);

                                    }else{

                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                confirmationtitle.setVisibility(View.VISIBLE);
                                                Animation sliderighttoleftanim = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.slidelefttoright);
                                                sliderighttoleftanim.setFillAfter(true);
                                                confirmationtitle.startAnimation(sliderighttoleftanim);
                                            }
                                        },1800);

                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                successimagecontainer.setVisibility(View.VISIBLE);
                                                successimage.setVisibility(View.VISIBLE);

                                                String value = "failed";
                                                int animicon;

                                                if(value.equals("Success")){
                                                    animicon = R.drawable.tickanimation;
                                                }else{
                                                    animicon = R.drawable.failedicon;
                                                }
                                                Glide.with(LoginActivity.this).load(animicon).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).crossFade().into(successimage);


                                                Animation sliderighttoleftanim = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.slidelefttoright);
                                                sliderighttoleftanim.setFillAfter(true);
                                                successimagecontainer.startAnimation(sliderighttoleftanim);
                                            }
                                        },1900);

                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                nextarrow.setClickable(false);
                                                alreadyhaveactivationbutton.setClickable(false);
                                                emailedittext.setEnabled(false);

                                                successmessage.setVisibility(View.VISIBLE);

                                                emailidentercontainer.setVisibility(View.INVISIBLE);
                                                emailidentercontainer.setClickable(false);

                                                verificationinputfieldoverallcontainer.setVisibility(View.INVISIBLE);
                                                verificationinputfieldoverallcontainer.setClickable(false);


                                                successmessage.setText("Unable to send otp to the mail id "+verificationforgotemailid +" due to technical error. Please try again later.");
                                                Animation sliderighttoleftanim = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.slidelefttoright);
                                                sliderighttoleftanim.setFillAfter(true);
                                                successmessage.startAnimation(sliderighttoleftanim);
                                            }
                                        },2000);

                                    }

                                } else {
                                    nextarrow.setVisibility(View.VISIBLE);
                                    nextloadingcontainer.setVisibility(View.INVISIBLE);
                                    snackbar = Snackbar.make(forgotpasswordview,"Invalid Username or Password",Snackbar.LENGTH_LONG);
                                    snackbar.show();
                                }

                            } catch (JSONException e) {
                                nextarrow.setVisibility(View.VISIBLE);
                                nextloadingcontainer.setVisibility(View.INVISIBLE);
                                snackbar = Snackbar.make(forgotpasswordview,"Unable to recieve data from server due to Technical issues.",Snackbar.LENGTH_LONG);
                                snackbar.show();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    nextarrow.setVisibility(View.VISIBLE);
                    nextloadingcontainer.setVisibility(View.INVISIBLE);
                    snackbar = Snackbar.make(forgotpasswordview,"Unable to Connect with server due to Technical Issues. Kindly Contact Administrator for more details.",Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            })

            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();

                    params.clear();

                    params.put(loginconfig.key_verificationemailid, verificationforgotemailid);

                    return params;
                }

            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(fortemailcheckrequest);
        }

    }





    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {}
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if(dir!= null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }

    @Override
    public void onBackPressed() {

        if(supportisOpen==true){
            loginpagecontainer.setVisibility(View.VISIBLE);
            int x = layoutButtons.getRight();
            int y = layoutButtons.getBottom();

            int startRadius = Math.max(loginscreenmaincontainer.getWidth(), loginscreenmaincontainer.getHeight());
            int endRadius = 0;

            Animator anim = ViewAnimationUtils.createCircularReveal(layoutButtons, x, y, startRadius, endRadius);
            anim.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    layoutButtons.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
            anim.start();

            supportisOpen = false;
        }else if(forgotisOpen==true){
            loginpagecontainer.setVisibility(View.VISIBLE);
            int x = forgotpasswordlayout.getRight();
            int y = forgotpasswordlayout.getBottom();

            int startRadius = Math.max(loginscreenmaincontainer.getWidth(), loginscreenmaincontainer.getHeight());
            int endRadius = 0;

            Animator anim = ViewAnimationUtils.createCircularReveal(forgotpasswordlayout, x, y, startRadius, endRadius);
            anim.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    forgottitle.clearAnimation();
                    circularimagecontainer.clearAnimation();
                    successimagecontainer.clearAnimation();
                    enteremailtext.clearAnimation();
                    emailcontainer.clearAnimation();
                    nextarrow.clearAnimation();
                    nextloadingcontainer.clearAnimation();
                    alreadyhaveactivationbutton.clearAnimation();
                    confirmationtitle.clearAnimation();
                    successimagecontainer.clearAnimation();
                    successmessage.clearAnimation();
                    emailidentercontainer.clearAnimation();
                    verificationinputfieldoverallcontainer.clearAnimation();
                    emailedittext.setText("");
                    verificationemailedittext.setText("");
                    firstverificationinput.setText("");
                    secondverificationinput.setText("");
                    thirdverificationinput.setText("");
                    fourthverificationinput.setText("");
                    isalreadyverificationvalidationclicked = false;


                    secondpagenextarrow.clearAnimation();
                    secondnextloadingcontainer.clearAnimation();
                    secondpagenextarrow.setClickable(true);

                    nextarrow.setClickable(true);
                    alreadyhaveactivationbutton.setClickable(true);
                    emailedittext.setEnabled(true);

                    if(isemailvalidateclicked==true){
                        isemailvalidateclosed = true;
                    }

                    isemailvalidateclicked = false;

                    forgottitle.setVisibility(View.INVISIBLE);
                    circularimagecontainer.setVisibility(View.INVISIBLE);
                    successimagecontainer.setVisibility(View.INVISIBLE);
                    enteremailtext.setVisibility(View.INVISIBLE);
                    emailcontainer.setVisibility(View.INVISIBLE);
                    nextarrow.setVisibility(View.INVISIBLE);
                    nextloadingcontainer.setVisibility(View.INVISIBLE);
                    //forgotorcontainer.setVisibility(View.INVISIBLE);
                    alreadyhaveactivationbutton.setVisibility(View.INVISIBLE);
                    forgotpasswordlayout.setVisibility(View.GONE);
                    confirmationtitle.setVisibility(View.INVISIBLE);
                    successimagecontainer.setVisibility(View.INVISIBLE);
                    successmessage.setVisibility(View.INVISIBLE);
                    emailidentercontainer.setVisibility(View.INVISIBLE);
                    verificationinputfieldoverallcontainer.setVisibility(View.INVISIBLE);

                    changetitle.clearAnimation();
                    changesuccessmessage.clearAnimation();
                    changesuccessimage.clearAnimation();
                    changepasswordoverallcontainer.clearAnimation();
                    changeimagecontainer.clearAnimation();

                    changepasswordedittext.setText("");
                    retypechangepasswordedittext.setText("");
                    thirdpagenextarrow.clearAnimation();
                    thirdnextloadingcontainer.clearAnimation();
                    thirdpagenextarrow.setClickable(true);

                    changetitle.setVisibility(View.INVISIBLE);
                    changesuccessmessage.setVisibility(View.INVISIBLE);
                    changeimagecontainer.setVisibility(View.INVISIBLE);
                    changepasswordoverallcontainer.setVisibility(View.INVISIBLE);
                    thirdpagenextarrow.setVisibility(View.INVISIBLE);
                    thirdnextloadingcontainer.setVisibility(View.INVISIBLE);

                    changesuccesstitle.clearAnimation();
                    changeimagesuccesscontainer.clearAnimation();
                    passchangesuccessmessage.clearAnimation();
                    gotologinbutton.clearAnimation();

                    changesuccesstitle.setVisibility(View.INVISIBLE);
                    passchangesuccessmessage.setVisibility(View.INVISIBLE);
                    changeimagesuccesscontainer.setVisibility(View.INVISIBLE);
                    gotologinbutton.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
            anim.start();

            forgotisOpen = false;
        }else{
            super.onBackPressed();
        }


    }




}


