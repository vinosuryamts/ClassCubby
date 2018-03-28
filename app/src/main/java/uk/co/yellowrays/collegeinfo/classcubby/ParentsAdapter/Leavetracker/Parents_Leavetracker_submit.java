package uk.co.yellowrays.collegeinfo.classcubby.ParentsAdapter.Leavetracker;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

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

public class Parents_Leavetracker_submit extends android.support.v4.app.Fragment {

    View mainview,view;
    private TextView fromdatetext,todatetext,leavepagename,fromdatetextvalue,fromhouttextvalue;
    private TextView todatetextvalue,tohouttextvalue;
    private ImageView fromcalendarimage,tocalendarimage;
    private Spinner fromhourspinner,tohourspinner;
    private EditText subjecttextvalue,leavedetailtextvalue;
    private Button submit;
    Snackbar snackbar;


    public String[] fromdatehourlist,todatehourlist;
    private int pDay,pMonth,pYear;
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

    public Parents_Leavetracker_submit() {
        // Required empty public constructor
    }

    public static Parents_Leavetracker_submit newInstance(String param1) {
        Parents_Leavetracker_submit fragment = new Parents_Leavetracker_submit();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.parents_leavetracker_newrequest_xml, null);

        leavepagename = (TextView)view.findViewById(R.id.leavepagename);
        fromdatetext = (TextView)view.findViewById(R.id.fromdatetext);
        todatetext = (TextView)view.findViewById(R.id.todatetext);
        fromhourspinner = (Spinner) view.findViewById(R.id.fromhourspinner);
        tohourspinner = (Spinner) view.findViewById(R.id.tohourspinner);
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

        fromdatehourlist = new String[]{"Forenoon", "Afternoon"};
        todatehourlist = new String[]{"Forenoon", "Afternoon"};

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

        final Calendar cal = Calendar.getInstance();
        pYear = cal.get(Calendar.YEAR);
        pMonth = cal.get(Calendar.MONTH);
        pDay = cal.get(Calendar.DAY_OF_MONTH);
        updateDisplay();
        updateDisplay1();

        adpfromdatehourlist = new ArrayAdapter<String>(getActivity(),
                R.layout.spinner_item, fromdatehourlist);
        adpfromdatehourlist.setDropDownViewResource(R.layout.spinner_item);
        fromhourspinner.setAdapter(adpfromdatehourlist);

        adptodatehourlist = new ArrayAdapter<String>(getActivity(),
                R.layout.spinner_item, todatehourlist);
        adptodatehourlist.setDropDownViewResource(R.layout.spinner_item);
        tohourspinner.setAdapter(adptodatehourlist);

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
        String fromhour = fromhourspinner.getSelectedItem().toString().trim();
        String tohour = tohourspinner.getSelectedItem().toString().trim();
        String subject = subjecttextvalue.getText().toString().trim();
        String leavedetail = leavedetailtextvalue.getText().toString().trim();

        if (fromdate.isEmpty() && todate.isEmpty()) {
            snackbar = Snackbar.make(mainview,"Please select Leave From date and To date",Snackbar.LENGTH_LONG);
            snackbar.show();
            valid = false;
        }else if (fromdate.isEmpty()) {
            snackbar = Snackbar.make(mainview,"Please select Leave From date",Snackbar.LENGTH_LONG);
            snackbar.show();
            valid = false;
        }else if (todate.isEmpty()) {
            snackbar = Snackbar.make(mainview,"Please select Leave To date",Snackbar.LENGTH_LONG);
            snackbar.show();
            valid = false;
        }

        if(fromdate.equals(todate)){
            if(fromhour.equals("Afternoon") && tohour.equals("Forenoon")){
                snackbar = Snackbar.make(mainview,"Period To cannot be previous hour",Snackbar.LENGTH_LONG);
                snackbar.show();
                valid = false;
            }
        }
        if (fromhour.isEmpty() && tohour.isEmpty()) {
            snackbar = Snackbar.make(mainview,"Please select Period From and  Period To",Snackbar.LENGTH_LONG);
            snackbar.show();
            valid = false;
        }else if (fromhour.isEmpty()) {
            snackbar = Snackbar.make(mainview,"Please select Period From",Snackbar.LENGTH_LONG);
            snackbar.show();
            valid = false;
        }else if (tohour.isEmpty()) {
            snackbar = Snackbar.make(mainview,"Please select Period To",Snackbar.LENGTH_LONG);
            snackbar.show();
            valid = false;
        }

        if (subject.isEmpty()) {

            subjecttextvalue.setError("Please Enter Subject and then Proceed");
            valid = false;
        } else {
            subjecttextvalue.setError(null);
        }

        if (leavedetail.isEmpty()) {
            leavedetailtextvalue.setError("Please Enter Leave Details and then Proceed");
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

        fromhourname = fromhourspinner.getSelectedItem().toString().trim();
        tohourname = tohourspinner.getSelectedItem().toString().trim();

        fromdate = fromdatetext.getText().toString().trim();
        todate = todatetext.getText().toString().trim();
        subject = subjecttextvalue.getText().toString().trim();
        leavedetail = leavedetailtextvalue.getText().toString().trim();


        StringRequest studentrequest1 = new StringRequest(Request.Method.POST, loginconfig.key_parentsleave_submit_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        JSONObject j = null;
                        try {

                            snackbar = Snackbar.make(mainview, "Leave Request has been Sent Successfully", Snackbar.LENGTH_LONG);
                            snackbar.show();
                            submit.setClickable(true);

                            submit.setVisibility(View.INVISIBLE);

                            Parents_Leavetracker_main newFragment = new Parents_Leavetracker_main().newInstance("test");
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
