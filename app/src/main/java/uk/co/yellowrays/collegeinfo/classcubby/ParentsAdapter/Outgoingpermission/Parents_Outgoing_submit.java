package uk.co.yellowrays.collegeinfo.classcubby.ParentsAdapter.Outgoingpermission;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.File;
import java.util.Calendar;
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

public class Parents_Outgoing_submit extends android.support.v4.app.Fragment {

    View mainview,view;
    private TextView fromdatetext,todatetext,leavepagename,fromdatetextvalue,fromhouttextvalue;
    private TextView todatetextvalue,tohouttextvalue;
    private ImageView fromcalendarimage,tocalendarimage;
    private TextView fromhourspinner,tohourspinner;
    private EditText subjecttextvalue,leavedetailtextvalue;
    private Button submit;
    Snackbar snackbar;


    public String[] fromdatehourlist,todatehourlist;
    private int pDay,pMonth,pYear,mHour,mMinute,Mampm;
    private String blockCharacterSet = "~`\"|[]";
    public ArrayAdapter<String> adpfromdatehourlist,adpfromdatehouridlist,adptodatehourlist,adptodatehouridlist;
    String studentid;
    String fromhourname,tohourname,fromhourid,tohourid,fromdate,todate,subject,leavedetail;


    private InputFilter filter = new InputFilter() {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            if (source != null && blockCharacterSet.contains(("" + source))) {
                return "";
            }
            return null;
        }

    };

    public static final String MY_PREFERENCES = "loginconfig.shared_pref_name";
    public static final String Previous_PREFERENCES = "previousloginconfig.shared_pref_name";
    public static SharedPreferences sharedPreferences,previoussharedPreferences;
    public static SharedPreferences.Editor editor,previouseditor;
    private static boolean isitemclicked;

    public Parents_Outgoing_submit() {
        // Required empty public constructor
    }

    public static Parents_Outgoing_submit newInstance(String param1) {
        Parents_Outgoing_submit fragment = new Parents_Outgoing_submit();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.students_outgoingpermission_newrequest_xml, null);

        leavepagename = (TextView)view.findViewById(R.id.leavepagename);
        fromdatetext = (TextView)view.findViewById(R.id.fromdatetext);
        todatetext = (TextView)view.findViewById(R.id.todatetext);
        fromhourspinner = (TextView) view.findViewById(R.id.fromhourspinner);
        tohourspinner = (TextView) view.findViewById(R.id.tohourspinner);
        fromcalendarimage = (ImageView)view.findViewById(R.id.fromcalendarimage);
        tocalendarimage = (ImageView)view.findViewById(R.id.tocalendarimage);
        subjecttextvalue = (EditText)view.findViewById(R.id.subjecttextvalue);
        leavedetailtextvalue = (EditText)view.findViewById(R.id.leavedetailtextvalue);
        submit = (Button)view.findViewById(R.id.submit);

        fromdatetextvalue = (TextView)view.findViewById(R.id.fromdatetextvalue);
        todatetextvalue = (TextView)view.findViewById(R.id.todatetextvalue);
        fromhouttextvalue = (TextView)view.findViewById(R.id.fromhouttextvalue);
        tohouttextvalue = (TextView)view.findViewById(R.id.tohouttextvalue);

        mainview = view.findViewById(R.id.cardcontainer);


        Typeface normaltypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/OpenSansRegular.ttf");
        Typeface boldtypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/OpenSansBold.ttf");
        leavepagename.setTypeface(boldtypeface);
        fromdatetextvalue.setTypeface(boldtypeface);
        todatetextvalue.setTypeface(boldtypeface);
        fromhouttextvalue.setTypeface(boldtypeface);
        tohouttextvalue.setTypeface(boldtypeface);
        fromdatetext.setTypeface(normaltypeface);
        submit.setTypeface(boldtypeface);
        todatetext.setTypeface(normaltypeface);
        subjecttextvalue.setTypeface(normaltypeface);
        leavedetailtextvalue.setTypeface(normaltypeface);
        leavedetailtextvalue.setTypeface(normaltypeface);




        submit.setVisibility(View.VISIBLE);
        submit.setClickable(true);

        subjecttextvalue.setFilters(new InputFilter[] { filter });
        leavedetailtextvalue.setFilters(new InputFilter[] { filter });

        fromcalendarimage.setEnabled(true);
        fromcalendarimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        tocalendarimage.setEnabled(true);
        tocalendarimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker1();
            }
        });

        fromhourspinner.setEnabled(true);
        fromhourspinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showtimepicker();
            }
        });

        tohourspinner.setEnabled(true);
        tohourspinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showtimepicker1();
            }
        });

        final Calendar cal = Calendar.getInstance();
        pYear = cal.get(Calendar.YEAR);
        pMonth = cal.get(Calendar.MONTH);
        pDay = cal.get(Calendar.DAY_OF_MONTH);
        mHour = cal.get(Calendar.HOUR_OF_DAY);
        mMinute = cal.get(Calendar.MINUTE);
        Mampm = cal.get(Calendar.AM_PM);
        updateDisplay();
        updateDisplay1();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                submit.setClickable(false);

                if (AppStatus.getInstance(getActivity()).isOnline()) {

                    //Validating errors
                    if(!validate()){
                        return;
                    }


                    //Main login activity start
                    new android.os.Handler().postDelayed(
                            new Runnable() {
                                public void run() {
                                    submitdetails();
                                }
                            }, 100);

                } else {
                    snackbar = Snackbar.make(mainview,"Please Connect to the Internet and Login Again",Snackbar.LENGTH_LONG);
                    snackbar.show();
                }


            }
        });

        return view;
    }

    private boolean validate() {
        boolean valid = true;

        String fromdate = fromdatetext.getText().toString().trim();
        String todate = todatetext.getText().toString().trim();
        String fromhour = fromhourspinner.getText().toString().trim();
        String tohour = tohourspinner.getText().toString().trim();
        String subject = subjecttextvalue.getText().toString().trim();
        String leavedetail = leavedetailtextvalue.getText().toString().trim();

        if (fromdate.isEmpty() && todate.isEmpty()) {
            snackbar = Snackbar.make(mainview,"Please select Permission From date and To date",Snackbar.LENGTH_LONG);
            snackbar.show();
            valid = false;
        }else if (fromdate.isEmpty()) {
            snackbar = Snackbar.make(mainview,"Please select Permission From date",Snackbar.LENGTH_LONG);
            snackbar.show();
            valid = false;
        }else if (todate.isEmpty()) {
            snackbar = Snackbar.make(mainview,"Please select Permission To date",Snackbar.LENGTH_LONG);
            snackbar.show();
            valid = false;
        }


        if (fromhour.isEmpty() && tohour.isEmpty()) {
            snackbar = Snackbar.make(mainview,"Please select Time From and Time To",Snackbar.LENGTH_LONG);
            snackbar.show();
            valid = false;
        }else if (fromhour.isEmpty()) {
            snackbar = Snackbar.make(mainview,"Please select Time From",Snackbar.LENGTH_LONG);
            snackbar.show();
            valid = false;
        }else if (tohour.isEmpty()) {
            snackbar = Snackbar.make(mainview,"Please select Time To",Snackbar.LENGTH_LONG);
            snackbar.show();
            valid = false;
        }

        if (subject.isEmpty()) {

            subjecttextvalue.setError("Please Enter Permission Title and then Proceed");
            valid = false;
        } else {
            subjecttextvalue.setError(null);
        }

        if (leavedetail.isEmpty()) {
            leavedetailtextvalue.setError("Please Enter Permission Details and then Proceed");
            valid = false;
        } else {
            leavedetailtextvalue.setError(null);
        }

        if(valid == false) {
            submit.setClickable(true);
        }

        return valid;

    }

    private void submitdetails() {
        studentid = MainActivity.sharedPreferences.getString(loginconfig.key_userid, "");

        studentid = studentid.replace("[","");
        studentid = studentid.replace("]","");
        studentid = studentid.replace("\"","");

        fromhourname = fromhourspinner.getText().toString().trim();
        tohourname = tohourspinner.getText().toString().trim();

        fromdate = fromdatetext.getText().toString().trim();
        todate = todatetext.getText().toString().trim();
        subject = subjecttextvalue.getText().toString().trim();
        leavedetail = leavedetailtextvalue.getText().toString().trim();


        StringRequest studentrequest1 = new StringRequest(Request.Method.POST, loginconfig.key_parentsoutgoing_submit_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        JSONObject j = null;
                        try {

                            snackbar = Snackbar.make(mainview, "Leave Request has been Sent Successfully", Snackbar.LENGTH_LONG);
                            snackbar.show();
                            submit.setClickable(true);

                            submit.setVisibility(View.INVISIBLE);

                            Parents_Outgoing_main newFragment = new Parents_Outgoing_main().newInstance("test");
                            Bundle bundle = new Bundle();
                            FragmentTransaction transaction = getFragmentManager().beginTransaction();
                            transaction.replace(R.id.cardcontainer, newFragment);
                            transaction.addToBackStack("new");
                            transaction.commit();

                        } catch (Exception e) {
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

                params.put(loginconfig.key_leavesinglestudentid, studentid);
                params.put(loginconfig.key_leave_fromHourName, fromhourname);
                params.put(loginconfig.key_leave_toHourName, tohourname);
                params.put(loginconfig.key_leave_leaveFrom, fromdate);
                params.put(loginconfig.key_leave_leaveTo, todate);
                params.put(loginconfig.key_leave_Leavesubject, subject);
                params.put(loginconfig.key_leave_LeaveDetail, leavedetail);

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

            fromdatetext.setText(String.valueOf(dayOfMonth) + "-" + String.valueOf(monthOfYear+1)
                    + "-" + String.valueOf(year));
        }
    };


    private void updateDisplay() {
        fromdatetext.setText(
                new StringBuilder()
                        // Month is 0 based so add 1
                        .append(pDay).append("-")
                        .append(pMonth + 1).append("-")
                        .append(pYear).append(" "));
        String ampm;

        if(mHour==0 && mMinute ==0) {
            fromhourspinner.setText(String.valueOf(12)+ ":00 AM");
        }else if(mHour==0 && mMinute !=0) {
            fromhourspinner.setText(String.valueOf(12)+ ":"+(String.valueOf(mMinute)+" AM"));
        }else if(mHour<12) {
            if(mMinute<10){
                fromhourspinner.setText(String.valueOf(mHour)+ ":"+(String.valueOf("0"+mMinute)+" AM"));    
            }else {
                fromhourspinner.setText(String.valueOf(mHour) + ":" + (String.valueOf(mMinute) + " AM"));
            }
        }else if(mHour==12 && mMinute ==0) {
            fromhourspinner.setText("12"+ ":00 PM");
        }else if(mHour==12 && mMinute !=0) {
            if(mMinute<10){
                fromhourspinner.setText(String.valueOf(12)+ ":"+(String.valueOf("0"+mMinute)+" PM"));
            }else {
                fromhourspinner.setText(String.valueOf(12) + ":" + (String.valueOf(mMinute) + " PM"));
            }
        }else if(mHour>12) {
            if(mMinute<10){
                fromhourspinner.setText(String.valueOf(mHour-12)+ ":"+(String.valueOf("0"+mMinute)+" PM"));
            }else {
                fromhourspinner.setText(String.valueOf(mHour-12) + ":" + (String.valueOf(mMinute) + " PM"));
            }
        }


    }


    private void showDatePicker1() {
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
        date.setCallBack(ondate1);
        date.show(getFragmentManager(), "Date Picker");
    }

    DatePickerDialog.OnDateSetListener ondate1 = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            todatetext.setText(String.valueOf(dayOfMonth) + "-" + String.valueOf(monthOfYear+1)
                    + "-" + String.valueOf(year));
        }
    };

    private void updateDisplay1() {
        todatetext.setText(
                new StringBuilder()
                        // Month is 0 based so add 1
                        .append(pDay).append("-")
                        .append(pMonth + 1).append("-")
                        .append(pYear).append(" "));
        String ampm;


        if(mHour==0 && mMinute ==0) {
            tohourspinner.setText(String.valueOf(12)+ ":00 AM");
        }else if(mHour==0 && mMinute !=0) {
            tohourspinner.setText(String.valueOf(12)+ ":"+(String.valueOf(mMinute)+" AM"));
        }else if(mHour<12) {
            if(mMinute<10){
                tohourspinner.setText(String.valueOf(mHour)+ ":"+(String.valueOf("0"+mMinute)+" AM"));
            }else {
                tohourspinner.setText(String.valueOf(mHour) + ":" + (String.valueOf(mMinute) + " AM"));
            }
        }else if(mHour==12 && mMinute ==0) {
            tohourspinner.setText("12"+ ":00 PM");
        }else if(mHour==12 && mMinute !=0) {
            if(mMinute<10){
                tohourspinner.setText(String.valueOf(12)+ ":"+(String.valueOf("0"+mMinute)+" PM"));
            }else {
                tohourspinner.setText(String.valueOf(12) + ":" + (String.valueOf(mMinute) + " PM"));
            }
        }else if(mHour>12) {
            if(mMinute<10){
                tohourspinner.setText(String.valueOf(mHour-12)+ ":"+(String.valueOf("0"+mMinute)+" PM"));
            }else {
                tohourspinner.setText(String.valueOf(mHour-12) + ":" + (String.valueOf(mMinute) + " PM"));
            }
        }



    }


    private void showtimepicker() {

        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getActivity().getFragmentManager(),"TimePicker");

    }

    @SuppressLint("ValidFragment")
    public static class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            return new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));
        }

        @Override
        public void onTimeSet(TimePicker view, int hourofDay, int minute) {
            TextView tv = (TextView) getActivity().findViewById(R.id.fromhourspinner);


            if(hourofDay==0 && minute ==0) {
                tv.setText(String.valueOf(12)+ ":00 AM");
            }else if(hourofDay==0 && minute !=0) {
                tv.setText(String.valueOf(12)+ ":"+(String.valueOf(minute)+" AM"));
            }else if(hourofDay<12) {
                if(minute<10){
                    tv.setText(String.valueOf(hourofDay)+ ":"+(String.valueOf("0"+minute)+" AM"));
                }else {
                    tv.setText(String.valueOf(hourofDay) + ":" + (String.valueOf(minute) + " AM"));
                }
            }else if(hourofDay==12 && minute ==0) {
                tv.setText("12"+ ":00 PM");
            }else if(hourofDay==12 && minute !=0) {
                if(minute<10){
                    tv.setText(String.valueOf(12)+ ":"+(String.valueOf("0"+minute)+" PM"));
                }else {
                    tv.setText(String.valueOf(12) + ":" + (String.valueOf(minute) + " PM"));
                }
            }else if(hourofDay>12) {
                if(minute<10){
                    tv.setText(String.valueOf(hourofDay-12)+ ":"+(String.valueOf("0"+minute)+" PM"));
                }else {
                    tv.setText(String.valueOf(hourofDay-12) + ":" + (String.valueOf(minute) + " PM"));
                }
            }


        }

    }

    private void showtimepicker1() {

        DialogFragment newFragment = new TimePickerFragment1();
        newFragment.show(getActivity().getFragmentManager(),"TimePicker");

    }

    @SuppressLint("ValidFragment")
    public static class TimePickerFragment1 extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            return new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));
        }

        @Override
        public void onTimeSet(TimePicker view, int hourofDay, int minute) {
            TextView tv = (TextView) getActivity().findViewById(R.id.tohourspinner);

            if(hourofDay==0 && minute ==0) {
                tv.setText(String.valueOf(12)+ ":00 AM");
            }else if(hourofDay==0 && minute !=0) {
                tv.setText(String.valueOf(12)+ ":"+(String.valueOf(minute)+" AM"));
            }else if(hourofDay<12) {
                if(minute<10){
                    tv.setText(String.valueOf(hourofDay)+ ":"+(String.valueOf("0"+minute)+" AM"));
                }else {
                    tv.setText(String.valueOf(hourofDay) + ":" + (String.valueOf(minute) + " AM"));
                }
            }else if(hourofDay==12 && minute ==0) {
                tv.setText("12"+ ":00 PM");
            }else if(hourofDay==12 && minute !=0) {
                if(minute<10){
                    tv.setText(String.valueOf(12)+ ":"+(String.valueOf("0"+minute)+" PM"));
                }else {
                    tv.setText(String.valueOf(12) + ":" + (String.valueOf(minute) + " PM"));
                }
            }else if(hourofDay>12) {
                if(minute<10){
                    tv.setText(String.valueOf(hourofDay-12)+ ":"+(String.valueOf("0"+minute)+" PM"));
                }else {
                    tv.setText(String.valueOf(hourofDay-12) + ":" + (String.valueOf(minute) + " PM"));
                }
            }

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
