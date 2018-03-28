package uk.co.yellowrays.collegeinfo.classcubby;

import android.animation.Animator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Forgot_Password_Activity extends AppCompatActivity {

    FloatingActionButton nextarrow,fabclose;
    FrameLayout forgotpasswordlayout,forgotpasswordmain;
    EditText emailedittext;
    TextView enteremailtext,forgottitle;
    View forgotpasswordview,view;
    private boolean isOpen = false;
    Snackbar snackbar;

    public static final String MY_PREFERENCES = "loginconfig.shared_pref_name";
    public static final String Previous_PREFERENCES = "previousloginconfig.shared_pref_name";
    public static SharedPreferences sharedPreferences,previoussharedPreferences;
    public static SharedPreferences.Editor editor,previouseditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgotpassword_main);


        forgotpasswordmain = (FrameLayout) findViewById(R.id.forgotpasswordmain);
        forgotpasswordlayout = (FrameLayout) findViewById(R.id.forgotpasswordlayout);
        emailedittext = (EditText)findViewById(R.id.emailedittext);
        enteremailtext = (TextView) findViewById(R.id.enteremailtext);
        forgottitle = (TextView) findViewById(R.id.forgottitle);
        fabclose = (FloatingActionButton) findViewById(R.id.fabclose);
        nextarrow = (FloatingActionButton) findViewById(R.id.nextarrow);

        view = findViewById(R.id.loginscreenmaincontainer);
        forgotpasswordview = findViewById(R.id.forgotpasswordlayout);


        if (savedInstanceState == null) {
            forgotpasswordmain.setVisibility(View.INVISIBLE);

            ViewTreeObserver viewTreeObserver = forgotpasswordmain.getViewTreeObserver();
            if (viewTreeObserver.isAlive()) {
                viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        viewforgotpasswordMenu();
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                            forgotpasswordmain.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        } else {
                            forgotpasswordmain.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        }
                    }
                });
            }
        }

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/appfont.ttf");
        Typeface normaltypeface = Typeface.createFromAsset(getAssets(), "fonts/OpenSansSemibold.ttf");
        Typeface boldtypeface = Typeface.createFromAsset(getAssets(), "fonts/OpenSansBold.ttf");
        emailedittext.setTypeface(normaltypeface);
        enteremailtext.setTypeface(normaltypeface);
        forgottitle.setTypeface(boldtypeface);

        Animation rotate = AnimationUtils.loadAnimation(Forgot_Password_Activity.this, R.anim.rotate);
        nextarrow.startAnimation(rotate);

        //viewforgotpasswordMenu();


        fabclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int x = forgotpasswordmain.getRight();
                int y = forgotpasswordmain.getBottom();

                int startRadius = Math.max(forgotpasswordmain.getWidth(), forgotpasswordmain.getHeight());
                int endRadius = 0;

                Animator anim = ViewAnimationUtils.createCircularReveal(forgotpasswordmain, x, y, startRadius, endRadius);
                anim.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        forgotpasswordmain.setVisibility(View.GONE);
                        Intent i = new Intent(Forgot_Password_Activity.this,LoginActivity.class);
                        startActivity(i);
                        finish();
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                });
                anim.start();

                isOpen = false;

            }
        });

        nextarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!emailvalidate()){
                    return;
                }else{
                    snackbar = Snackbar.make(forgotpasswordview,"Email is Valid",Snackbar.LENGTH_LONG);
                    snackbar.show();
                }

            }
        });

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

    private void viewforgotpasswordMenu() {

            int x = forgotpasswordmain.getRight();
            int y = forgotpasswordmain.getBottom();

            int startRadius = 0;
            int endRadius = (int) Math.hypot(forgotpasswordmain.getWidth(), forgotpasswordmain.getHeight());

            Animator anim = ViewAnimationUtils.createCircularReveal(forgotpasswordmain, x, y, startRadius, endRadius);
            anim.setDuration(1000);

            forgotpasswordmain.setVisibility(View.VISIBLE);
            anim.start();

            isOpen = true;

    }


    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    @Override
    public void onBackPressed() {
        int x = forgotpasswordmain.getRight();
        int y = forgotpasswordmain.getBottom();

        int startRadius = Math.max(forgotpasswordmain.getWidth(), forgotpasswordmain.getHeight());
        int endRadius = 0;

        Animator anim = ViewAnimationUtils.createCircularReveal(forgotpasswordmain, x, y, startRadius, endRadius);
        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                forgotpasswordmain.setVisibility(View.GONE);
                Intent i = new Intent(Forgot_Password_Activity.this,LoginActivity.class);
                startActivity(i);
                finish();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        anim.start();

        isOpen = false;
    }
}


