package uk.co.yellowrays.collegeinfo.classcubby.ParentsAdapter.Attendance;

import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dinuscxj.progressbar.CircleProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import uk.co.yellowrays.collegeinfo.classcubby.AppStatus;
import uk.co.yellowrays.collegeinfo.classcubby.MainActivity;
import uk.co.yellowrays.collegeinfo.classcubby.R;
import uk.co.yellowrays.collegeinfo.classcubby.configfiles.loginconfig;
import uk.co.yellowrays.collegeinfo.classcubby.customtabadapter.DatePickerFragment;

/**
 * Created by Vino on 10/24/2017.
 */

public class Parents_Attendance_Main extends android.support.v4.app.Fragment {

    View view,mainview;
    private RelativeLayout overallcontainerreport;
    private CircleProgressBar mCustomProgressBar1;
    private ImageView calendarimage;
    private ListView attendancelist;
    private TextView remainingleaves,availableleaves,datetext,emptycriteriamsg,attendancepercentagetextview;
    private TextView remainingleavestext,availableleavestext,hourname,attendancevaluetextview;



    private JSONArray loginresult;
    String progress;
    String dayscount;
    String availabledayscount;
    static final int DATE_DIALOG_ID = 0;
    private int pYear;
    private int pMonth;
    private int pDay;
    private int emptycount = 0;
    StringBuilder trackerdate;
    parentsattendacetrackerlistviewadapter adapter;

    String userid,studentuserid;
    String studentid, studentname, classname, classid, userimage;
    String[] strstudentid, strstudentname, strclassname, strclassid, struserimage;
    String assignmentschoolname, assignmentschoolid, assignmentclassid, assignmentclassname, assignmentstudentid, assignmentstudentname, assignmentcount, assignmentuserimage;
    ArrayList<String> arrassignmentschoolname, arrassignmentschoolid, arrassignmentclassid, arrassignmentclassname, arrassignmentstudentid, arrassignmentstudentname, arrassignmentcount, arrassignmentuserimage;

    String leaveschoolname, leaveschoolid, leavesclassid, leavesclassname, leavestudentid, leavestudentname, leavescount, leavesuserimage;
    ArrayList<String> arrleaveschoolname, arrleaveschoolid, arrleavesclassid, arrleavesclassname, arrleavestudentid, arrleavestudentname, arrleavescount, arrleavesuserimage;

    String  attendancepercentage, attendancehourid, attendancehourname, attendancevalue, attendanceleaveallowed, attendanceremainingleave,attendancecomments;
    String[] strattendancestudentname, strattendancestudentid, strattendancepercentage, strattendancehourid, strattendancehourname, strattendancevalue, strattendanceleaveallowed, strattendanceremainingleave,strattendancecomments;
    ArrayList<String> arrattendancepercentage, arrattendancehourid, arrattendancehourname, arrattendancevalue, arrattendanceleaveallowed, arrattendanceremainingleave,arrattendancecomments;


    String attendanceschoolname,attendanceschoolid,attendanceclassid,attendanceclassname,attendancestudentid,attendancestudentname,attendancepercent,attendanceuserimage;
    ArrayList<String> arrattendanceschoolname,arrattendanceschoolid,arrattendanceclassid,arrattendanceclassname,arrattendancestudentid,arrattendancestudentname,arrattendancepercent,arrattendanceuserimage;


    Snackbar snackbar;
    String loogedinusertypeid;
    public static final String MY_PREFERENCES = "loginconfig.shared_pref_name";
    public static final String Previous_PREFERENCES = "previousloginconfig.shared_pref_name";
    public static SharedPreferences sharedPreferences,previoussharedPreferences;
    public static SharedPreferences.Editor editor,previouseditor;


    public Parents_Attendance_Main() {
        // Required empty public constructor
    }

    public static Parents_Attendance_Main newInstance(String param1) {
        Parents_Attendance_Main fragment = new Parents_Attendance_Main();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.parents_attendance_main_xml, null);

        mCustomProgressBar1 = (CircleProgressBar) view.findViewById(R.id.custom_progress1);
        overallcontainerreport = (RelativeLayout) view.findViewById(R.id.overallcontainerreport);
        remainingleaves = (TextView) view.findViewById(R.id.remainingleaves);
        availableleaves = (TextView) view.findViewById(R.id.availableleaves);
        emptycriteriamsg = (TextView) view.findViewById(R.id.emptycriteriamsg);
        datetext = (TextView) view.findViewById(R.id.datetext);
        calendarimage = (ImageView) view.findViewById(R.id.calendarimage);
        attendancelist = (ListView) view.findViewById(R.id.attendancelist);
        attendancepercentagetextview = (TextView) view.findViewById(R.id.attendancepercentage);
        remainingleavestext = (TextView) view.findViewById(R.id.remainingleavestext);
        availableleavestext = (TextView) view.findViewById(R.id.availableleavestext);
        attendancevaluetextview = (TextView) view.findViewById(R.id.attendancevalue);
        hourname = (TextView) view.findViewById(R.id.hourname);
        mainview = view.findViewById(R.id.overallcontainerreport);

        emptycriteriamsg.setVisibility(View.INVISIBLE);
        //new DownloadImageTask(overallcontainerreport).execute(image);

        Typeface normaltypeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSansRegular.ttf");
        Typeface semiboldtypeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSansSemibold.ttf");
        Typeface boldtypeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSansBold.ttf");
        datetext.setTypeface(semiboldtypeface);
        emptycriteriamsg.setTypeface(semiboldtypeface);
        attendancepercentagetextview.setTypeface(semiboldtypeface);
        remainingleavestext.setTypeface(semiboldtypeface);
        availableleavestext.setTypeface(semiboldtypeface);
        attendancevaluetextview.setTypeface(semiboldtypeface);
        hourname.setTypeface(semiboldtypeface);

        Drawable drawable = getResources().getDrawable(R.drawable.newimage);
        overallcontainerreport.setBackgroundDrawable(drawable);

        calendarimage.setEnabled(true);
        datetext.setEnabled(true);
        calendarimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });
        datetext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        final Calendar cal = Calendar.getInstance();
        pYear = cal.get(Calendar.YEAR);
        pMonth = cal.get(Calendar.MONTH);
        pDay = cal.get(Calendar.DAY_OF_MONTH);
        updateDisplay();

        trackerdate = new StringBuilder()
                // Month is 0 based so add 1
                .append(pDay).append("-")
                .append(pMonth + 1).append("-")
                .append(pYear);

        if (AppStatus.getInstance(getActivity().getApplicationContext()).isOnline()) {

            getAttendanceListValues();

            datetext.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count,
                                              int after) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void afterTextChanged(Editable s) {
                    // TODO Auto-generated method stub
                    emptycount = 0;
                    trackerdate = new StringBuilder()
                            // Month is 0 based so add 1
                            .append(pDay).append("-")
                            .append(pMonth + 1).append("-")
                            .append(pYear);
                    getAttendanceListValues();
                }
            });
        } else {
            snackbar = Snackbar.make(mainview, "Please Connect to the Internet and Try Again", Snackbar.LENGTH_LONG);
            snackbar.show();
        }

        return view;
    }


    private void getAttendanceListValues() {
        deleteCache(getActivity().getApplicationContext());
        studentuserid = MainActivity.sharedPreferences.getString(loginconfig.key_userid, "");

        studentuserid = studentuserid.replace("[", "");
        studentuserid = studentuserid.replace("]", "");
        studentuserid = studentuserid.replace("\"", "");
        studentuserid = studentuserid.replace(", ", ",");

        StringRequest studentrequest1 = new StringRequest(Request.Method.POST, loginconfig.key_user_attendancetrackersinglevalue_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        JSONObject j = null;
                        try {
                            j = new JSONObject(s);
                            loginresult = j.getJSONArray("result");

                            if (loginresult.length() > 0) {

                                arrattendancestudentname = new ArrayList<String>(loginresult.length());
                                arrattendancestudentid = new ArrayList<String>(loginresult.length());
                                arrattendancepercentage = new ArrayList<String>(loginresult.length());
                                arrattendancehourid = new ArrayList<String>(loginresult.length());
                                arrattendancehourname = new ArrayList<String>(loginresult.length());
                                arrattendancevalue = new ArrayList<String>(loginresult.length());
                                arrattendanceleaveallowed = new ArrayList<String>(loginresult.length());
                                arrattendanceremainingleave = new ArrayList<String>(loginresult.length());
                                arrattendancecomments = new ArrayList<String>(loginresult.length());

                                for (int i = 0; i < loginresult.length(); i++) {
                                    JSONObject json = loginresult.getJSONObject(i);

                                    attendancestudentname = json.optString("studentname");
                                    attendancestudentid = json.optString("studentid");
                                    attendancepercentage = json.optString("attendancepercent");
                                    attendancehourid = json.optString("hourid");
                                    attendancehourname = json.optString("hourname");
                                    attendancevalue = json.optString("attendancevalue");
                                    attendanceleaveallowed = json.optString("leaveallowed");
                                    attendanceremainingleave = json.optString("remainingleave");
                                    attendancecomments = json.optString("comments");

                                    arrattendancestudentname.add(attendancestudentname);
                                    arrattendancestudentid.add(attendancestudentid);
                                    arrattendancepercentage.add(attendancepercentage);
                                    arrattendancehourid.add(attendancehourid);
                                    arrattendancehourname.add(attendancehourname);
                                    arrattendancevalue.add(attendancevalue);
                                    arrattendanceleaveallowed.add(attendanceleaveallowed);
                                    arrattendanceremainingleave.add(attendanceremainingleave);
                                    arrattendancecomments.add(attendancecomments);
                                }


                                if(attendanceremainingleave.equals("null")){
                                    dayscount="0";
                                }else{
                                    dayscount = attendanceremainingleave;
                                }

                                if(attendanceleaveallowed.equals("null")){
                                    availabledayscount="0";
                                }else{
                                    availabledayscount = attendanceleaveallowed;
                                }

                                if(attendancepercentage.equals("null")){
                                    progress="0";
                                }else{
                                    progress = attendancepercentage;
                                }

                                sharedPreferences = getActivity().getSharedPreferences(MY_PREFERENCES, MainActivity.MODE_PRIVATE);

                                //creating editor to store values of shared preferences
                                editor = sharedPreferences.edit();
                                editor.putString(loginconfig.key_attendancehourname, arrattendancehourname.toString());
                                editor.putString(loginconfig.key_attendancevalue, arrattendancevalue.toString());
                                editor.putString(loginconfig.key_attendancecomments, arrattendancecomments.toString());
                                editor.apply();

                                attendancehourname = MainActivity.sharedPreferences.getString(loginconfig.key_attendancehourname, "");
                                attendancevalue = MainActivity.sharedPreferences.getString(loginconfig.key_attendancevalue, "");
                                attendancecomments = MainActivity.sharedPreferences.getString(loginconfig.key_attendancecomments, "");

                                attendancehourname = attendancehourname.replace("[", "");
                                attendancehourname = attendancehourname.replace("]", "");
                                attendancehourname = attendancehourname.replace("\"", "");
                                attendancehourname = attendancehourname.replace(", ", ",");
                                strattendancehourname = attendancehourname.split(",");

                                attendancevalue = attendancevalue.replace("[", "");
                                attendancevalue = attendancevalue.replace("]", "");
                                attendancevalue = attendancevalue.replace("\"", "");
                                attendancevalue = attendancevalue.replace(", ", ",");
                                strattendancevalue = attendancevalue.split(",");

                                attendancecomments = attendancecomments.replace("[", "");
                                attendancecomments = attendancecomments.replace("]", "");
                                attendancecomments = attendancecomments.replace("\"", "");
                                attendancecomments = attendancecomments.replace(", ", ",");
                                strattendancecomments = attendancecomments.split(",");

                                simulateProgress();

                                for(int o=0;o<strattendancevalue.length;o++){
                                    if(strattendancevalue[o].equals("null")){
                                        emptycount = emptycount+0;
                                    }else{
                                        emptycount = emptycount+1;
                                    }
                                }

                                if(emptycount==0){
                                    emptycriteriamsg.setVisibility(View.VISIBLE);
                                    attendancelist.setVisibility(View.INVISIBLE);
                                }else {
                                    emptycriteriamsg.setVisibility(View.INVISIBLE);
                                    attendancelist.setVisibility(View.VISIBLE);
                                    adapter = new parentsattendacetrackerlistviewadapter(getActivity(), strattendancehourname, strattendancevalue, strattendancecomments);
                                    attendancelist.setAdapter(adapter);
                                }
                            } else {
                                emptycriteriamsg.setVisibility(View.VISIBLE);
                                attendancelist.setVisibility(View.INVISIBLE);
                            }

                        } catch (JSONException e) {
                            snackbar = Snackbar.make(mainview, "Unable to reach Server Please Try Again", Snackbar.LENGTH_LONG);
                            snackbar.show();
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                snackbar = Snackbar.make(mainview, "Unable Connect to Internet. Please Try Again", Snackbar.LENGTH_LONG);
                snackbar.show();
                volleyError.printStackTrace();
            }
        })

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.clear();

                params.put(loginconfig.key_studentid, studentuserid);
                params.put(loginconfig.key_attendancedatevalue, trackerdate.toString());

                return params;
            }
        };
        RequestQueue studentlist = Volley.newRequestQueue(getActivity().getApplicationContext());
        studentlist.add(studentrequest1);
    }


    private void showDatePicker() {
        DatePickerFragment date = new DatePickerFragment();
        /**
         * Set Up Current Date Into dialog
         */
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        date.setArguments(args);
        /**
         * Set Call back to capture selected date
         */
        date.setCallBack(ondate);
        date.show(getFragmentManager(), "Date Picker");
    }

    DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            pYear = year;
            pMonth = monthOfYear;
            pDay = dayOfMonth;
            updateDisplay();
        }
    };

    /** Updates the date in the TextView */
    private void updateDisplay() {


        StringBuilder newstr = new StringBuilder()
                // Month is 0 based so add 1
                .append(pDay).append("-")
                .append(pMonth + 1).append("-")
                .append(pYear);

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

        Date date = null;
        try {
            date = (Date)formatter.parse(newstr.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
        String dateString = sdf.format(date);

        datetext.setText(dateString);
    }

    private void simulateProgress() {

        final double progresstext = Double.valueOf(progress);

        if (progresstext==(int)progresstext){
            ValueAnimator animator6 = ValueAnimator.ofInt(0, (int)progresstext);
            animator6.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mCustomProgressBar1.setProgress((int)progresstext);
                    if((int)progresstext<85){
                        mCustomProgressBar1.setProgressStartColor(getActivity().getResources().getColor(R.color.absent));
                        mCustomProgressBar1.setProgressEndColor(getActivity().getResources().getColor(R.color.absent));
                        mCustomProgressBar1.setProgressTextColor(getActivity().getResources().getColor(R.color.absent));
                    }else{
                        mCustomProgressBar1.setProgressStartColor(getActivity().getResources().getColor(R.color.present));
                        mCustomProgressBar1.setProgressEndColor(getActivity().getResources().getColor(R.color.present));
                        mCustomProgressBar1.setProgressTextColor(getActivity().getResources().getColor(R.color.present));
                    }

                }
            });
            animator6.setDuration(3000);
            animator6.start();

        }else{
            ValueAnimator animator7 = new ValueAnimator();
            animator7.setObjectValues(0d, progresstext);
            animator7.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    mCustomProgressBar1.setProgress((int)progresstext);
                    if((int)progresstext<85) {
                        mCustomProgressBar1.setProgressStartColor(getActivity().getResources().getColor(R.color.absent));
                        mCustomProgressBar1.setProgressEndColor(getActivity().getResources().getColor(R.color.absent));
                        mCustomProgressBar1.setProgressTextColor(getActivity().getResources().getColor(R.color.absent));
                    }else{
                        mCustomProgressBar1.setProgressStartColor(getActivity().getResources().getColor(R.color.present));
                        mCustomProgressBar1.setProgressEndColor(getActivity().getResources().getColor(R.color.present));
                        mCustomProgressBar1.setProgressTextColor(getActivity().getResources().getColor(R.color.present));
                    }
                }
            });
            animator7.setEvaluator(new TypeEvaluator<Double>() { // problem here
                @Override
                public Double evaluate(float fraction, Double startValue, Double endValue) {
                    double val = (startValue + (double) ((endValue - startValue) * fraction));
                    int r = (int) Math.round(val*100);
                    double f = r / 100.0;

                    return  f;
                }
            });
            animator7.setDuration(3000);
            animator7.start();
        }

        double text = Double.valueOf(dayscount);

        if (text==(int)text){
            ValueAnimator animator1 = ValueAnimator.ofInt(0, (int)text);
            animator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    remainingleaves.setText(String.valueOf(animation.getAnimatedValue()));
                }
            });
            animator1.setDuration(1500);
            animator1.start();

        }else{
            ValueAnimator animator2 = new ValueAnimator();
            animator2.setObjectValues(0d, text);
            animator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    remainingleaves.setText(String.valueOf(animation.getAnimatedValue()));
                }
            });
            animator2.setEvaluator(new TypeEvaluator<Double>() { // problem here
                @Override
                public Double evaluate(float fraction, Double startValue, Double endValue) {
                    double val = (startValue + (double) ((endValue - startValue) * fraction));
                    int r = (int) Math.round(val*100);
                    double f = r / 100.0;

                    return  f;
                }
            });
            animator2.setDuration(3000);
            animator2.start();
        }


        double leavetext = Double.valueOf(availabledayscount);

        if (leavetext==(int)leavetext){
            ValueAnimator animator1 = ValueAnimator.ofInt(0, (int)leavetext);
            animator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    availableleaves.setText(String.valueOf(animation.getAnimatedValue()));
                }
            });
            animator1.setDuration(1500);
            animator1.start();

        }else{
            ValueAnimator animator2 = new ValueAnimator();
            animator2.setObjectValues(0d, leavetext);
            animator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    availableleaves.setText(String.valueOf(animation.getAnimatedValue()));
                }
            });
            animator2.setEvaluator(new TypeEvaluator<Double>() { // problem here
                @Override
                public Double evaluate(float fraction, Double startValue, Double endValue) {
                    double val = (startValue + (double) ((endValue - startValue) * fraction));
                    int r = (int) Math.round(val*100);
                    double f = r / 100.0;

                    return  f;
                }
            });
            animator2.setDuration(3000);
            animator2.start();
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
}
