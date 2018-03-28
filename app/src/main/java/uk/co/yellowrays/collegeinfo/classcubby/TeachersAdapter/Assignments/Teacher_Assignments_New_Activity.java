package uk.co.yellowrays.collegeinfo.classcubby.TeachersAdapter.Assignments;

import android.Manifest;
import android.animation.Animator;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
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
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import uk.co.yellowrays.collegeinfo.classcubby.AppStatus;
import uk.co.yellowrays.collegeinfo.classcubby.MainActivity;
import uk.co.yellowrays.collegeinfo.classcubby.R;
import uk.co.yellowrays.collegeinfo.classcubby.TeachersAdapter.Attendance.RecentAttendanceListViewAdapter;
import uk.co.yellowrays.collegeinfo.classcubby.configfiles.loginconfig;
import uk.co.yellowrays.collegeinfo.classcubby.customtabadapter.DatePickerFragment;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Vino on 10/4/2017.
 */

public class Teacher_Assignments_New_Activity extends android.support.v4.app.Fragment{

    View view;
    TextView attendancedate,datetext,browsetext,browsetextname,targetdatetextname,studentslidtview,dectexthidden,datetext1,filetypetext,filenametext;
    TextView filetypenamepreview,filesizevalue,downloadtext,downloadcanceltext,imagesizevalue,imagetypenamepreview,imagedownloadtext,imagedownloadcanceltext;
    CircularProgressBar filedownloadingprogress,imagedownloadingprogress;
    EditText titletext,descriptiontext;
    ImageView calendarimage,calendarimage1,browseimage,iv_bitmap,iconpreviewer,iconpreviewer1;
    Button submitstudentslist,generatestudentslist;
    TextInputLayout descriptionboxcontainer,titleboxtextcontainer;
    RelativeLayout innerimagecardviewcontainer,innerfilescardviewcontainer,assignmentsmaincontainer,innerfilterlayout,downloadbackgroundcolor;
    FloatingActionButton filter;
    Spinner classspinner,hourspinner;

    String sectionname,assignmentsubmittype,hourname,assignmentdate,assignmenttargetdate,serverpathvalue,assignmenttitle,assignmentfilename,assignmentdescription;
    Boolean baseitemclicked;
    ScrollView mscroll;

    private int pDay,pMonth,pYear;
    static final int DATE_DIALOG_ID = 0;
    static final int DATE_DIALOG_ID1 = 1;
    private static int REQUEST_FOR_IMAGES_VIDEOS = 1;
    private static int REQUEST_FOR_FILES = 2;
    String filename,filetype,fileabsolutepath;
    String attachmenttype = "Image";
    Uri fileuri;
    long filesize;
    Bitmap mBitmap;
    String savedPath;
    int previouslistcountvalue=0,currentlistcountvalue=0;
    private boolean iscountstarted = false;
    private boolean filterisOpen = false;
    boolean value,generatevalue;
    private JSONArray loginresult;
    private boolean isgenerateclicked = false;
    public ArrayList<String> classlist;
    public ArrayList<String> hourlist;
    public ArrayAdapter<String> hourdataAdapter,classdataAdapter;
    public static String[] classnewlist;
    public static String[] hournewlist;
    public static String[] prgmNameList1;
    public static String[] prgmImages;
    int filesizenewvalue;
    public String filedownladed,filepath;
    Snackbar snackbar;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    ArrayList<String> datelist1,assignmentidlist1,assignmenttitlelist1,assignmentdesclist1,assignmenttargetlist1,
            assignmentsubjectlist1,assignmentattachmentlist1,assignmentfilenamelist1,departmentidlist1,departmentnamelist1,
            sectionidlis1t,sectionnamelist1;
    public static final String MY_PREFERENCES = "loginconfig.shared_pref_name";
    public static SharedPreferences sharedPreferences,previoussharedPreferences;
    public static SharedPreferences.Editor editor;
    String upLoadServerUri = null;
    int serverResponseCode = 0;
    File sourceFile;
    int totalSize;
    String uploadedfilename,uploadedurl,targetdate,title,description;
    String classname,classsection,schoolid,datevalue,hourlistspinner,classlistspinner,assignmentgeneratevalue;



    public Teacher_Assignments_New_Activity() {
        // Required empty public constructor
    }

    public static Teacher_Assignments_New_Activity newInstance(String param1) {
        Teacher_Assignments_New_Activity fragment = new Teacher_Assignments_New_Activity();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.teachers_assignments_new_xml, null);

        mscroll = (ScrollView) view.findViewById(R.id.mscroll);
        attendancedate = (TextView) view.findViewById(R.id.attendancedate);
        datetext = (TextView) view.findViewById(R.id.datetext);
        datetext1 = (TextView) view.findViewById(R.id.datetext1);
        browsetext = (TextView) view.findViewById(R.id.browsetext);
        browsetextname = (TextView) view.findViewById(R.id.browsetextname);
        targetdatetextname = (TextView) view.findViewById(R.id.targetdatetextname);
        studentslidtview = (TextView) view.findViewById(R.id.studentslidtview);
        titletext = (EditText) view.findViewById(R.id.titletext);
        descriptiontext = (EditText) view.findViewById(R.id.descriptiontext);
        calendarimage = (ImageView) view.findViewById(R.id.calendarimage);
        calendarimage1 = (ImageView) view.findViewById(R.id.calendarimage1);
        //browseimage = (ImageView) view.findViewById(R.id.browseimage);
        submitstudentslist = (Button) view.findViewById(R.id.submitstudentslist);
        generatestudentslist = (Button) view.findViewById(R.id.generatestudentslist);
        titleboxtextcontainer = (TextInputLayout) view.findViewById(R.id.titleboxtextcontainer);
        descriptionboxcontainer = (TextInputLayout) view.findViewById(R.id.descriptionboxcontainer);

        filter = (FloatingActionButton) view.findViewById(R.id.filter);
        classspinner = (Spinner) view.findViewById(R.id.classspinner);
        hourspinner = (Spinner) view.findViewById(R.id.hourspinner);

        assignmentsmaincontainer = (RelativeLayout) view.findViewById(R.id.assignmentsmaincontainer);
        innerfilterlayout = (RelativeLayout) view.findViewById(R.id.innerfilterlayout);
        innerimagecardviewcontainer = (RelativeLayout) view.findViewById(R.id.innerimagecardviewcontainer);
        iv_bitmap = (ImageView) view.findViewById(R.id.iv_bitmap);
        imagesizevalue = (TextView) view.findViewById(R.id.imagesizevalue);
        imagetypenamepreview = (TextView) view.findViewById(R.id.imagetypenamepreview);
        imagedownloadtext = (TextView) view.findViewById(R.id.imagedownloadtext);
        imagedownloadcanceltext = (TextView) view.findViewById(R.id.imagedownloadcanceltext);

        innerfilescardviewcontainer = (RelativeLayout) view.findViewById(R.id.innerfilescardviewcontainer);
        downloadbackgroundcolor = (RelativeLayout) view.findViewById(R.id.downloadbackgroundcolor);
        iconpreviewer1 = (ImageView) view.findViewById(R.id.iconpreviewer1);
        filenametext = (TextView) view.findViewById(R.id.filenametext);
        filetypenamepreview = (TextView) view.findViewById(R.id.filetypenamepreview);
        filesizevalue = (TextView) view.findViewById(R.id.filesizevalue);
        downloadtext = (TextView) view.findViewById(R.id.downloadtext);
        downloadcanceltext = (TextView) view.findViewById(R.id.downloadcanceltext);
        filedownloadingprogress = (CircularProgressBar) view.findViewById(R.id.filedownloadingprogress);
        imagedownloadingprogress = (CircularProgressBar) view.findViewById(R.id.imagedownloadingprogress);


        innerimagecardviewcontainer.setVisibility(View.GONE);
        innerfilescardviewcontainer.setVisibility(View.GONE);
        baseitemclicked = RecentAssignmentListViewAdapter.getclicked();

        upLoadServerUri = "http://www.gamcngl.com/Webservice/teacher/UploadToServer.php";


        if(baseitemclicked ==true) {
            Bundle bundle = getArguments();
            classname = bundle.getString("classname");
            sectionname = bundle.getString("sectionname");
            hourname = bundle.getString("hourname");
            assignmentdate = bundle.getString("assignmentdate");
            assignmenttargetdate = bundle.getString("assignmenttargetdate");
            serverpathvalue = bundle.getString("serverpathvalue");
            baseitemclicked = bundle.getBoolean("baseitemclicked");
            assignmenttitle = bundle.getString("assignmenttitle");
            assignmentfilename = bundle.getString("assignmentfilename");
            assignmentdescription = bundle.getString("assignmentdescription");
            assignmentsubmittype = bundle.getString("assignmenttypevalue");
            if(serverpathvalue!=null && serverpathvalue!="") {
                getserverpathuri(serverpathvalue);
            }
        }


        filter.setVisibility(View.VISIBLE);
        submitstudentslist.setVisibility(View.VISIBLE);

        Typeface normaltypeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSansRegular.ttf");
        Typeface semiboldtypeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSansSemibold.ttf");
        Typeface boldtypeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSansBold.ttf");
        Typeface fontawesome = Typeface.createFromAsset(getActivity().getAssets(), "fonts/fontawesome.ttf");
        attendancedate.setTypeface(boldtypeface);
        titleboxtextcontainer.setTypeface(semiboldtypeface);
        descriptionboxcontainer.setTypeface(semiboldtypeface);
        browsetextname.setTypeface(semiboldtypeface);
        targetdatetextname.setTypeface(semiboldtypeface);
        datetext.setTypeface(normaltypeface);
        browsetext.setTypeface(normaltypeface);
        studentslidtview.setTypeface(boldtypeface);
        titletext.setTypeface(normaltypeface);
        descriptiontext.setTypeface(normaltypeface);
        submitstudentslist.setTypeface(boldtypeface);
        filenametext.setTypeface(normaltypeface);
        downloadtext.setTypeface(fontawesome);
        downloadcanceltext.setTypeface(fontawesome);
        imagedownloadtext.setTypeface(fontawesome);
        imagedownloadcanceltext.setTypeface(fontawesome);

        final Calendar cal = Calendar.getInstance();
        pYear = cal.get(Calendar.YEAR);
        pMonth = cal.get(Calendar.MONTH);
        pDay = cal.get(Calendar.DAY_OF_MONTH);

        if(baseitemclicked==true) {
            attendancedate.setText(" Assignment for "+classname+" - "+sectionname+" on "+assignmentdate);
            titletext.setText(assignmenttitle);
            descriptiontext.setText(assignmentdescription);
            if(assignmenttargetdate.equals("")|| assignmenttargetdate==null){
                updateDisplay();
            }else {
                datetext.setText(assignmenttargetdate);
            }
        }

        calendarimage.setEnabled(true);
        calendarimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        studentslidtview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startIntentForFilePick();
                //startIntentForPick();
            }
        });

        submitstudentslist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(filepath!=null && !(filepath.equals(""))){
                    targetdate = datetext.getText().toString().trim();
                    title = titletext.getText().toString().trim();
                    description = descriptiontext.getText().toString().trim();

                    if(title.equals("")){
                        titletext.setError("Enter Assignment Title and Proceed");
                        if(description.equals("")){
                            descriptiontext.setError("Enter Description Title and Proceed");
                        }
                    }else if(description.equals("")){
                        descriptiontext.setError("Enter Description Title and Proceed");
                    }else {
                        new UploadFileToServer().execute();
                    }
                }else{

                    targetdate = datetext.getText().toString().trim();
                    title = titletext.getText().toString().trim();
                    description = descriptiontext.getText().toString().trim();
                    sharedPreferences = getActivity().getSharedPreferences(MY_PREFERENCES, MainActivity.MODE_PRIVATE);
                    final String userid = sharedPreferences.getString(loginconfig.key_userid, "");
                    final String collegeid = sharedPreferences.getString(loginconfig.key_school_id, "");
                    uploadedurl = "Existing url";
                    uploadedfilename = "";

                    if(title.equals("")){
                        titletext.setError("Enter Assignment Title and Proceed");
                        if(description.equals("")){
                            descriptiontext.setError("Enter Description Title and Proceed");
                        }
                    }else if(description.equals("")){
                        descriptiontext.setError("Enter Description Title and Proceed");
                    }else {
                        submitassignments(collegeid,userid,targetdate,title,description,assignmentdate,hourname,classname,sectionname,assignmentsubmittype,uploadedurl,uploadedfilename);
                    }

                }
            }
        });

        generatestudentslist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(classspinner.getSelectedItem() != null) {
                    classlistspinner = classspinner.getSelectedItem().toString().trim();
                }else{
                    classlistspinner = "";
                }

                if(hourspinner.getSelectedItem() != null) {
                    hourlistspinner = hourspinner.getSelectedItem().toString().trim();
                }else{
                    hourlistspinner = "";
                }

                datevalue = datetext.getText().toString();


                if(classlistspinner.equals("") || hourlistspinner.equals("") || hourlistspinner.equals("")){

                    if(filterisOpen==true){

                        if(value==true) {
                            submitstudentslist.setVisibility(View.VISIBLE);
                            submitstudentslist.setClickable(true);
                        }else if(value==false){
                            submitstudentslist.setVisibility(View.INVISIBLE);
                            submitstudentslist.setClickable(false);
                        }

                        int x = assignmentsmaincontainer.getRight();
                        int y = assignmentsmaincontainer.getBottom();

                        int startRadius = Math.max(assignmentsmaincontainer.getWidth(), assignmentsmaincontainer.getHeight());
                        int endRadius = 0;

                        Animator anim = ViewAnimationUtils.createCircularReveal(innerfilterlayout, x, y, startRadius, endRadius);
                        anim.addListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animator) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animator) {
                                innerfilterlayout.setVisibility(View.INVISIBLE);
                            }

                            @Override
                            public void onAnimationCancel(Animator animator) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animator) {

                            }
                        });
                        anim.start();

                        filterisOpen = false;

                        RecentAssignmentListViewAdapter.setFilterisOpen(false);

                        filter.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.blue)));
                        filter.setImageResource(R.drawable.filterwhite);
                    }

                    snackbar = Snackbar.make(view,"Selected date doesn't have an hour mapped. Kindly select a working day as per your routine and proceed. ",Snackbar.LENGTH_LONG);
                    snackbar.show();

                }else{

                    if(value==true){
                        RecentAssignmentListViewAdapter.setclicked(false);
                        RecentAssignmentListViewAdapter.setgenerateclicked(true);
                    }

                    if(filterisOpen==true){


                        if(value==true) {
                            submitstudentslist.setVisibility(View.VISIBLE);
                            submitstudentslist.setClickable(true);
                        }else if(value==false){
                            submitstudentslist.setVisibility(View.INVISIBLE);
                            submitstudentslist.setClickable(false);
                        }

                        int x = assignmentsmaincontainer.getRight();
                        int y = assignmentsmaincontainer.getBottom();

                        int startRadius = Math.max(assignmentsmaincontainer.getWidth(), assignmentsmaincontainer.getHeight());
                        int endRadius = 0;

                        Animator anim = ViewAnimationUtils.createCircularReveal(innerfilterlayout, x, y, startRadius, endRadius);
                        anim.addListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animator) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animator) {
                                innerfilterlayout.setVisibility(View.INVISIBLE);
                            }

                            @Override
                            public void onAnimationCancel(Animator animator) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animator) {

                            }
                        });
                        anim.start();

                        filterisOpen = false;

                        RecentAssignmentListViewAdapter.setFilterisOpen(false);

                        filter.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.blue)));
                        filter.setImageResource(R.drawable.filterwhite);
                    }


                    classlistspinner = classspinner.getSelectedItem().toString().trim();
                    hourlistspinner = hourspinner.getSelectedItem().toString().trim();
                    datevalue = datetext.getText().toString();
                    schoolid = MainActivity.sharedPreferences.getString(loginconfig.key_school_id, "");


                    String delimiters = "-";
                    String[] wholeclassname = classlistspinner.split(delimiters);

                    classname = wholeclassname[0].trim();
                    classsection = wholeclassname[1].trim();

                    sharedPreferences = getActivity().getSharedPreferences(MY_PREFERENCES, MainActivity.MODE_PRIVATE);

                    //creating editor to store values of shared preferences
                    editor = sharedPreferences.edit();

                    editor.putString(loginconfig.key_class, classname);
                    editor.putString(loginconfig.key_section, classsection);
                    editor.putString(loginconfig.key_attendancedate, datevalue);
                    editor.apply();

                    StringRequest studentrequest1 = new StringRequest(Request.Method.POST, loginconfig.key_generateassignments_url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String s) {
                                    JSONObject j = null;
                                    try {
                                        j = new JSONObject(s);
                                        loginresult = j.getJSONArray("result");

                                        datelist1 = new ArrayList<String>(loginresult.length());
                                        assignmentidlist1 = new ArrayList<String>(loginresult.length());
                                        assignmenttitlelist1 = new ArrayList<String>(loginresult.length());
                                        assignmentdesclist1 = new ArrayList<String>(loginresult.length());
                                        assignmenttargetlist1 = new ArrayList<String>(loginresult.length());
                                        assignmentsubjectlist1 = new ArrayList<String>(loginresult.length());
                                        assignmentattachmentlist1 = new ArrayList<String>(loginresult.length());
                                        assignmentfilenamelist1 = new ArrayList<String>(loginresult.length());

                                        if (loginresult.length() > 0) {
                                            for (int i = 0; i < loginresult.length(); i++) {
                                                JSONObject json = loginresult.getJSONObject(i);
                                                String date = json.optString("date");
                                                String assignmentid = json.optString("Assignmentid");
                                                String assignmenttitle = json.optString("AssignmentTitle");
                                                String assignmentdesc = json.optString("AssignmentDesc");
                                                String assignmenttarget = json.optString("AssignmentTarget");
                                                String assignmentsubject = json.optString("AssignmentSubject");
                                                String assignmentattachment = json.optString("AssignmentAttachment");
                                                String assignmentfilename = json.optString("AssignmentFileName");
                                                String value = json.optString("value");

                                                datelist1.add(date);
                                                assignmentidlist1.add(assignmentid);
                                                assignmenttitlelist1.add(assignmenttitle);
                                                assignmentdesclist1.add(assignmentdesc);
                                                assignmenttargetlist1.add(assignmenttarget);
                                                assignmentsubjectlist1.add(assignmentsubject);
                                                assignmentattachmentlist1.add(assignmentattachment);
                                                assignmentfilenamelist1.add(assignmentfilename);
                                                if(value.equals("0")){
                                                    assignmentgeneratevalue = "New";
                                                }else{
                                                    assignmentgeneratevalue = "Existing";
                                                }

                                            }

                                            //submit.setClickable(true);
                                            RecentAssignmentListViewAdapter.setNewgenerateclicled(true);
                                            RecentAssignmentListViewAdapter.setgenerateclicked(true);
                                            RecentAssignmentListViewAdapter.setclicked(true);

                                            filter.setVisibility(View.INVISIBLE);

                                            boolean isitemclicked = true;

                                            Teacher_Assignments_New_Activity newFragment = new Teacher_Assignments_New_Activity().newInstance("test");
                                            Bundle bundle = new Bundle();
                                            FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                            bundle.putString("classname",classname);
                                            bundle.putString("sectionname",classsection);
                                            bundle.putString("hourname",hourlistspinner);
                                            bundle.putString("assignmentdate",datevalue);
                                            bundle.putString("assignmenttargetdate",assignmenttargetlist1.get(0));
                                            bundle.putString("serverpathvalue",assignmentattachmentlist1.get(0));
                                            bundle.putString("assignmenttitle",assignmenttitlelist1.get(0));
                                            bundle.putString("assignmentdescription",assignmentdesclist1.get(0));
                                            bundle.putString("assignmentfilename",assignmentfilenamelist1.get(0));
                                            bundle.putString("assignmenttypevalue",assignmentgeneratevalue);
                                            bundle.putBoolean("baseitemclicked",isitemclicked);
                                            newFragment.setArguments(bundle);
                                            transaction.replace(R.id.attendancepagemaincontainer, newFragment);
                                            transaction.addToBackStack("new");
                                            transaction.commit();


                                        } else {
                                            submitstudentslist.setClickable(true);
                                            RecentAssignmentListViewAdapter.setNewgenerateclicled(true);
                                            RecentAssignmentListViewAdapter.setgenerateclicked(true);
                                            RecentAssignmentListViewAdapter.setclicked(true);

                                            filter.setVisibility(View.INVISIBLE);

                                            boolean isitemclicked = true;

                                            Teacher_Assignments_New_Activity newFragment = new Teacher_Assignments_New_Activity().newInstance("test");
                                            Bundle bundle = new Bundle();
                                            FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                            bundle.putString("classname",classname);
                                            bundle.putString("sectionname",classsection);
                                            bundle.putString("hourname",hourlistspinner);
                                            bundle.putString("assignmentdate",datevalue);
                                            bundle.putString("assignmenttargetdate","");
                                            bundle.putString("serverpathvalue","");
                                            bundle.putString("assignmenttitle","");
                                            bundle.putString("assignmentdescription","");
                                            bundle.putString("assignmentfilename","");
                                            bundle.putString("assignmenttypevalue","0");
                                            bundle.putBoolean("baseitemclicked",isitemclicked);
                                            newFragment.setArguments(bundle);
                                            transaction.replace(R.id.attendancepagemaincontainer, newFragment);
                                            transaction.addToBackStack("new");
                                            transaction.commit();
                                        }


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            volleyError.printStackTrace();
                        }
                    })

                    {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();

                            params.clear();

                            params.put(loginconfig.key_school_id, schoolid);
                            params.put(loginconfig.key_class, classname);
                            params.put(loginconfig.key_section, classsection);
                            params.put(loginconfig.key_hour_name, hourlistspinner);
                            params.put(loginconfig.key_attendancedate, datevalue);

                            return params;
                        }
                    };
                    RequestQueue studentlist = Volley.newRequestQueue(getActivity().getApplicationContext());
                    studentlist.add(studentrequest1);

                }

            }
        });

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                value = RecentAssignmentListViewAdapter.getclicked();
                generatevalue = RecentAssignmentListViewAdapter.getgenerateclick();

                if (AppStatus.getInstance(getContext()).isOnline()) {

                    if (!filterisOpen) {
                        mscroll.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                mscroll.setVerticalScrollBarEnabled(false);
                                mscroll.setHorizontalScrollBarEnabled(false);
                                return true;
                            }
                        });

                        RecentAttendanceListViewAdapter.setFilterisOpen(true);

                        if(value==true || generatevalue==true){
                            submitstudentslist.setVisibility(View.INVISIBLE);
                            submitstudentslist.setClickable(false);
                        }

                        sharedPreferences = getActivity().getSharedPreferences(MY_PREFERENCES, MainActivity.MODE_PRIVATE);

                        //creating editor to store values of shared preferences
                        editor = sharedPreferences.edit();

                        editor.putBoolean(loginconfig.key_attendancefilteropen, true);
                        editor.apply();

                        int x = assignmentsmaincontainer.getRight();
                        int y = assignmentsmaincontainer.getBottom();

                        int startRadius = 0;
                        int endRadius = (int) Math.hypot(assignmentsmaincontainer.getWidth(), assignmentsmaincontainer.getHeight());

                        Animator anim = ViewAnimationUtils.createCircularReveal(innerfilterlayout, x, y, startRadius, endRadius);

                        innerfilterlayout.setVisibility(View.VISIBLE);
                        anim.start();

                        filterisOpen = true;

                        filter.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
                        filter.setImageResource(R.drawable.closepopup);

                        final String classarray = MainActivity.sharedPreferences.getString(loginconfig.key_class_name, "classnamesection");
                        try {
                            loginresult = new JSONArray(classarray);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if (classarray != null) {
                            try {
                                classlist = new ArrayList<String>(loginresult.length());

                                for (int i = 0; i < loginresult.length(); i++)
                                    try {
                                        JSONObject json = loginresult.getJSONObject(i);

                                        String dashboardtype = json.optString("classname");

                                        classlist.add(dashboardtype);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }


                                classnewlist = new String[classlist.size()];
                                classnewlist = (String[]) classlist.toArray(classnewlist);

                                classdataAdapter = new ArrayAdapter<String>(getContext(),
                                        R.layout.spinner_item, classnewlist);
                                classdataAdapter.setDropDownViewResource(R.layout.spinner_item);
                                classspinner.setAdapter(classdataAdapter);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }


                        calendarimage1.setEnabled(true);
                        calendarimage1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                showDatePicker1();
                            }
                        });

                        final Calendar cal = Calendar.getInstance();
                        pYear = cal.get(Calendar.YEAR);
                        pMonth = cal.get(Calendar.MONTH);
                        pDay = cal.get(Calendar.DAY_OF_MONTH);
                        updateDisplay1();

                        gethourlist();


                    }else{

                        mscroll.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                mscroll.setVerticalScrollBarEnabled(true);
                                mscroll.setHorizontalScrollBarEnabled(true);
                                return false;
                            }
                        });
                        RecentAssignmentListViewAdapter.setFilterisOpen(false);

                        if(value==true|| generatevalue==true) {
                            submitstudentslist.setVisibility(View.VISIBLE);
                            submitstudentslist.setClickable(true);
                        }else if(value==false){
                            submitstudentslist.setVisibility(View.INVISIBLE);
                            submitstudentslist.setClickable(false);
                        }

                        sharedPreferences = getActivity().getSharedPreferences(MY_PREFERENCES, MainActivity.MODE_PRIVATE);

                        //creating editor to store values of shared preferences
                        editor = sharedPreferences.edit();

                        editor.putBoolean(loginconfig.key_attendancefilteropen, false);
                        editor.apply();

                        int x = assignmentsmaincontainer.getRight();
                        int y = assignmentsmaincontainer.getBottom();

                        int startRadius = Math.max(assignmentsmaincontainer.getWidth(), assignmentsmaincontainer.getHeight());
                        int endRadius = 0;

                        Animator anim = ViewAnimationUtils.createCircularReveal(innerfilterlayout, x, y, startRadius, endRadius);
                        anim.addListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animator) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animator) {
                                innerfilterlayout.setVisibility(View.INVISIBLE);
                            }

                            @Override
                            public void onAnimationCancel(Animator animator) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animator) {

                            }
                        });
                        anim.start();

                        filterisOpen = false;

                        filter.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.blue)));
                        filter.setImageResource(R.drawable.filterwhite);

                    }


                }else {
                    snackbar = Snackbar.make(view,"Please Connect to the Internet and Try Again",Snackbar.LENGTH_LONG);
                    snackbar.show();
                }


            }
        });

        return view;
    }

    private void startIntentForFilePick() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, "Pick files"), REQUEST_FOR_FILES);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == REQUEST_FOR_FILES){
                if(data.getData() != null) {
                    fileuri = data.getData();
                    String mimeType = getActivity().getContentResolver().getType(fileuri);
                    if (mimeType == null) {
                        String path = null;
                        path = getPath(getActivity().getApplicationContext(), fileuri);
                        File file = new File(path);
                        filename = file.getName();
                    } else {
                        Uri returnUri = data.getData();

                        Cursor returnCursor = getActivity().getContentResolver().query(returnUri, null, null, null, null);
                        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                        int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
                        returnCursor.moveToFirst();
                        //filename = returnCursor.getString(nameIndex);
                        filesize = Long.parseLong(returnCursor.getString(sizeIndex));
                        //String size = Long.toString(returnCursor.getLong(sizeIndex));

                        if (fileuri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
                            final MimeTypeMap mime = MimeTypeMap.getSingleton();
                            filetype = mime.getExtensionFromMimeType(getActivity().getContentResolver().getType(fileuri));
                        } else {
                            filetype = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(new File(fileuri.getPath())).toString());
                        }

                        if(filetype.equals("docx") || filetype.equals("doc")|| filetype.equals("xlsx") || filetype.equals("pdf")|| filetype.equals("pptx")|| filetype.equals("txt")|| filetype.equals("csv")){
                            innerimagecardviewcontainer.setVisibility(View.GONE);
                            innerfilescardviewcontainer.setVisibility(View.VISIBLE);

                            if(filetype.equals("docx") || filetype.equals("doc")){
                                iconpreviewer1.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.wordicon));
                            }else if(filetype.equals("xlsx")||filetype.equals("xls")){
                                iconpreviewer1.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.excelicon));
                            }else if(filetype.equals("pdf")){
                                iconpreviewer1.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.pdficon));
                            }else if(filetype.equals("pptx")){
                                iconpreviewer1.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ppticon));
                            }else if(filetype.equals("txt")){
                                iconpreviewer1.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.notepadicon));
                            }

                            File file = new File(getRealPathFromURI(returnUri));
                            filepath = getRealPathFromURI(returnUri);
                            filename = file.getName();

                            filenametext.setText(filename);
                            downloadtext.setVisibility(View.INVISIBLE);
                            filedownloadingprogress.setVisibility(View.INVISIBLE);
                            downloadcanceltext.setVisibility(View.INVISIBLE);

                            String newfilesize = getfilesize(filesize);
                            filesizevalue.setText(newfilesize);
                            filetypenamepreview.setText(filetype);

                        }else{

                            String path = getPath(getActivity().getApplicationContext(), fileuri);
                            File file = new File(path);
                            filename = file.getName();

                            filepath = path;

                            Bitmap bitmap = null;
                            Bitmap thumbBitmap = null;
                            try {
                                bitmap = MediaStore.Images.Media.getBitmap(this.getActivity().getContentResolver(), fileuri);
                                thumbBitmap = ThumbnailUtils.extractThumbnail(bitmap,500,500);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                            innerimagecardviewcontainer.setVisibility(View.VISIBLE);
                            innerfilescardviewcontainer.setVisibility(View.GONE);
                            imagedownloadingprogress.setVisibility(View.GONE);
                            imagedownloadtext.setVisibility(View.GONE);
                            imagedownloadcanceltext.setVisibility(View.GONE);
                            downloadbackgroundcolor.setVisibility(View.INVISIBLE);

                            iv_bitmap.setImageBitmap(thumbBitmap);
                            String newfilesize = getfilesize(filesize);
                            imagesizevalue.setText(newfilesize);
                            imagetypenamepreview.setText(filetype);
                        }

                    }

                    /*File fileSave = getActivity().getExternalFilesDir(null);
                    String sourcePath = getActivity().getExternalFilesDir(null).toString();

                    try {
                        copyFileStream(new File(sourcePath + "/" + filename), fileuri,getActivity().getApplicationContext());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }*/

                }
            }
        }
    }


    private class UploadFileToServer extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            // setting progress bar to zero
            sourceFile = new File(filepath);
            totalSize = (int)filesize;
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(String... progress) {
            Log.d("PROG", progress[0]);
            int value = Integer.parseInt(progress[0]);
            //filedownloadingprogress.setVisibility(View.VISIBLE);
            //filedownloadingprogress.setProgress(value);
            //donut_progress.setProgress(Integer.parseInt(progress[0])); //Updating progress


        }

        @Override
        protected String doInBackground(String... args) {
            verifyStoragePermissions(getActivity());

            HttpURLConnection.setFollowRedirects(false);
            HttpURLConnection connection = null;
            String fileName = sourceFile.getName();

            try {
                connection = (HttpURLConnection) new URL(upLoadServerUri).openConnection();
                connection.setRequestMethod("POST");
                String boundary = "---------------------------boundary";
                String tail = "\r\n--" + boundary + "--\r\n";
                connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
                connection.setDoOutput(true);

                String metadataPart = "--" + boundary + "\r\n"
                        + "Content-Disposition: form-data; name=\"metadata\"\r\n\r\n"
                        + "" + "\r\n";

                String fileHeader1 = "--" + boundary + "\r\n"
                        + "Content-Disposition: form-data; name=\"fileToUpload\"; filename=\""
                        + fileName + "\"\r\n"
                        + "Content-Type: application/octet-stream\r\n"
                        + "Content-Transfer-Encoding: binary\r\n";

                long fileLength = sourceFile.length() + tail.length();
                String fileHeader2 = "Content-length: " + fileLength + "\r\n";
                String fileHeader = fileHeader1 + fileHeader2 + "\r\n";
                String stringData = metadataPart + fileHeader;

                long requestLength = stringData.length() + fileLength;
                connection.setRequestProperty("Content-length", "" + requestLength);
                connection.setFixedLengthStreamingMode((int) requestLength);
                connection.connect();

                DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                out.writeBytes(stringData);
                out.flush();

                int progress = 0;
                int bytesRead = 0;
                byte buf[] = new byte[1024];
                BufferedInputStream bufInput = new BufferedInputStream(new FileInputStream(sourceFile));
                while ((bytesRead = bufInput.read(buf)) != -1) {
                    // write output
                    out.write(buf, 0, bytesRead);
                    out.flush();
                    progress += bytesRead; // Here progress is total uploaded bytes

                    publishProgress(""+(int)((progress*100)/totalSize)); // sending progress percent to publishProgress
                }

                // Write closing boundary and close stream
                out.writeBytes(tail);
                out.flush();
                out.close();

                // Get server response
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = "";
                StringBuilder builder = new StringBuilder();
                while((line = reader.readLine()) != null) {
                    builder.append(line);
                    uploadedfilename = filename;
                    uploadedurl = line;

                    sharedPreferences = getActivity().getSharedPreferences(MY_PREFERENCES, MainActivity.MODE_PRIVATE);
                    final String userid = sharedPreferences.getString(loginconfig.key_userid, "");
                    final String collegeid = sharedPreferences.getString(loginconfig.key_school_id, "");

                    submitassignments(collegeid,userid,targetdate,title,description,assignmentdate,hourname,classname,sectionname,assignmentsubmittype,uploadedurl,uploadedfilename);

                }

            } catch (Exception e) {
                // Exception
            } finally {
                if (connection != null) connection.disconnect();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.e("Response", "Response from server: " + result);
            super.onPostExecute(result);
        }

    }


    private void submitassignments(final String collegeid, final String userid, final String targetdate, final String title,
                                   final String description, final String assignmentdate,
                                   final String hourname, final String classname, final String sectionname,
                                   final String assignmentsubmittype,
                                   final String uploadedurl, final String uploadedfilename) {

        deleteCache(getActivity().getApplicationContext());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, loginconfig.key_submitassignments_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        JSONObject j = null;
                        try {

                            RecentAssignmentListViewAdapter.setclicked(false);
                            RecentAssignmentListViewAdapter.setgenerateclicked(false);

                            Teacher_Assignments_Main_Activity newFragment = new Teacher_Assignments_Main_Activity().newInstance("test");
                            FragmentTransaction transaction = getFragmentManager().beginTransaction();
                            transaction.replace(R.id.attendancepagemaincontainer, newFragment);
                            transaction.addToBackStack("new");
                            transaction.commit();

                        } catch (Exception e) {
                            snackbar = Snackbar.make(view,"Kindly check with administrator for this error",Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                snackbar = Snackbar.make(view,"Unable to contact webservice",Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        })

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.clear();

                params.put(loginconfig.key_school_id, collegeid);
                params.put(loginconfig.key_userid, userid);
                params.put(loginconfig.key_assignmenttargetdate, targetdate);
                params.put(loginconfig.key_assignmenttitletext, title);
                params.put(loginconfig.key_assignmentdescriptionvalue, description);
                params.put(loginconfig.key_assignmentdate, assignmentdate);
                params.put(loginconfig.key_subjectname, hourname);
                params.put(loginconfig.key_assignmentclassname, classname);
                params.put(loginconfig.key_assignmentclasssection, sectionname);
                params.put(loginconfig.key_inserttype, assignmentsubmittype);
                params.put(loginconfig.key_assignmentattachment, uploadedurl);
                params.put(loginconfig.key_assignmentfilename, uploadedfilename);

                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        requestQueue.add(stringRequest);

    }

    public static String getPath(Context context, Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else
            if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    private void getserverpathuri(final String serverpathvalue) {
        Uri extUrl =  Uri.parse(serverpathvalue);


        //PATH: /photos-ak-snc1/v315/224/13/659629384/s659629384_752969_4472.jpg
        String path = extUrl.getPath();

        //Checks for both forward and/or backslash
        //NOTE:**While backslashes are not supported in URL's
        //most browsers will autoreplace them with forward slashes
        //So technically if you're parsing an html page you could run into
        //a backslash , so i'm accounting for them here;
        String[] pathContents = path.split("[\\\\/]");
        int pathContentsLength;
        if(pathContents != null){
            pathContentsLength = pathContents.length;
            System.out.println("Path Contents Length: " + pathContentsLength);
            for (int i = 0; i < pathContents.length; i++) {
                System.out.println("Path " + i + ": " + pathContents[i]);
            }
            //lastPart: s659629384_752969_4472.jpg
            String lastPart = pathContents[pathContentsLength-1];
            String[] lastPartContents = lastPart.split("\\.");
            if(lastPartContents != null && lastPartContents.length > 1){
                int lastPartContentLength = lastPartContents.length;
                System.out.println("Last Part Length: " + lastPartContentLength);
                //filenames can contain . , so we assume everything before
                //the last . is the name, everything after the last . is the
                //extension
                String name = "";
                for (int i = 0; i < lastPartContentLength; i++) {
                    System.out.println("Last Part " + i + ": "+ lastPartContents[i]);
                    if(i < (lastPartContents.length -1)){
                        name += lastPartContents[i] ;
                        if(i < (lastPartContentLength -2)){
                            name += ".";
                        }
                    }
                }
                filetype = lastPartContents[lastPartContentLength -1];

                if(assignmentfilename.equals("")||assignmentfilename==null){
                    filename =  name;
                }else {
                    filename = assignmentfilename;
                }

                if(filetype.equals("docx") || filetype.equals("doc")|| filetype.equals("xlsx") || filetype.equals("pdf")|| filetype.equals("pptx")|| filetype.equals("txt")|| filetype.equals("csv")){
                    innerimagecardviewcontainer.setVisibility(View.GONE);
                    innerfilescardviewcontainer.setVisibility(View.VISIBLE);

                    if(filetype.equals("docx") || filetype.equals("doc")){
                        iconpreviewer1.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.wordicon));
                    }else if(filetype.equals("xlsx")){
                        iconpreviewer1.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.excelicon));
                    }else if(filetype.equals("pdf")){
                        iconpreviewer1.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.pdficon));
                    }else if(filetype.equals("pptx")){
                        iconpreviewer1.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ppticon));
                    }else if(filetype.equals("txt")){
                        iconpreviewer1.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.notepadicon));
                    }

                    filenametext.setText(filename+"."+filetype);

                    attachmenttype = "Files";

                    File cacheFile = null;
                    if(isExternalStorageWritable()==true && isExternalStorageReadable()==true) {
                        cacheFile = new File(Environment.getExternalStoragePublicDirectory(
                                Environment.DIRECTORY_DOCUMENTS), "Classcubby/");
                        if (!cacheFile.mkdirs()) {
                            //Toast.makeText(getActivity().getApplicationContext(),"Directory not created",Toast.LENGTH_LONG).show();
                        }
                    }

                    final File file = new File(String.valueOf(new File(cacheFile, filename+"."+filetype)));

                    if (file.exists()) {
                        downloadtext.setVisibility(View.INVISIBLE);
                        filedownloadingprogress.setVisibility(View.INVISIBLE);
                    }else{
                        downloadtext.setVisibility(View.VISIBLE);
                        filedownloadingprogress.setVisibility(View.VISIBLE);
                    }

                    downloadcanceltext.setVisibility(View.INVISIBLE);
                    new DownloadFilesizeAsync().execute(serverpathvalue);

                    filetypenamepreview.setText(filetype);

                    filedownladed = "No";

                    final File finalCacheFile = cacheFile;
                    filenametext.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(file.exists()){
                                File file = new File(String.valueOf(new File(finalCacheFile, filename+"."+filetype)));
                                try {
                                    FileOpen.openFile(getActivity().getApplicationContext(), file);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });

                    downloadtext.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            downloadtext.setVisibility(View.INVISIBLE);
                            filedownloadingprogress.setVisibility(View.VISIBLE);
                            downloadcanceltext.setVisibility(View.VISIBLE);
                            new DownloadFileAsync().execute(serverpathvalue);

                        }
                    });


                }else{

                    attachmenttype = "Image";

                    innerimagecardviewcontainer.setVisibility(View.VISIBLE);
                    innerfilescardviewcontainer.setVisibility(View.GONE);

                    File cacheFile = null;
                    if(isExternalStorageWritable()==true && isExternalStorageReadable()==true) {
                        cacheFile = new File(Environment.getExternalStoragePublicDirectory(
                                Environment.DIRECTORY_DCIM), "Classcubby/");
                        if (!cacheFile.mkdirs()) {
                            //Toast.makeText(getActivity().getApplicationContext(),"Directory not created",Toast.LENGTH_LONG).show();
                        }
                    }

                    final File file = new File(String.valueOf(new File(cacheFile, filename+"."+filetype)));

                    if (file.exists()) {
                        imagedownloadtext.setVisibility(View.INVISIBLE);
                        imagedownloadingprogress.setVisibility(View.INVISIBLE);
                        downloadbackgroundcolor.setVisibility(View.INVISIBLE);

                        Glide.with(getActivity().getApplicationContext()).load(serverpathvalue).listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String s, Target<GlideDrawable> target, boolean b) {
                                Log.i("GLIDE", "onException :", e);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable glideDrawable, String s, Target<GlideDrawable> target, boolean b, boolean b1) {
                                Log.i("GLIDE", "onResourceReady");
                                iv_bitmap.setVisibility(View.VISIBLE);
                                return false;
                            }
                        }).into(iv_bitmap);

                    }else{
                        imagedownloadtext.setVisibility(View.VISIBLE);
                        imagedownloadingprogress.setVisibility(View.VISIBLE);
                        downloadbackgroundcolor.setVisibility(View.VISIBLE);

                        Glide.with(getActivity().getApplicationContext()).load(serverpathvalue).listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String s, Target<GlideDrawable> target, boolean b) {
                                Log.i("GLIDE", "onException :", e);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable glideDrawable, String s, Target<GlideDrawable> target, boolean b, boolean b1) {
                                Log.i("GLIDE", "onResourceReady");
                                iv_bitmap.setVisibility(View.VISIBLE);
                                return false;
                            }
                        }).override(92, 92).into(iv_bitmap);
                    }

                    imagedownloadcanceltext.setVisibility(View.GONE);

                    new DownloadFilesizeAsync().execute(serverpathvalue);
                    imagetypenamepreview.setText(filetype);

                    final File finalCacheFile = cacheFile;
                    iv_bitmap.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(file.exists()){
                                File file = new File(String.valueOf(new File(finalCacheFile, filename+"."+filetype)));
                                try {
                                    FileOpen.openFile(getActivity().getApplicationContext(), file);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });

                    imagedownloadtext.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            filedownloadingprogress.setVisibility(View.GONE);
                            imagedownloadtext.setVisibility(View.INVISIBLE);
                            imagedownloadingprogress.setVisibility(View.VISIBLE);
                            imagedownloadcanceltext.setVisibility(View.VISIBLE);
                            new DownloadFileAsync().execute(serverpathvalue);

                        }
                    });

                }

            }
        }

    }



    private String getRealPathFromURI(Uri returnUri) {
        String result;
        Cursor cursor = getActivity().getApplicationContext().getContentResolver().query(returnUri, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = returnUri.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    private static String getfilesize(long size) {
        if (size <= 0)
            return "0";
        final String[] units = new String[] { "B", "KB", "MB", "GB", "TB" };
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    public static class FileOpen {
        public static void openFile(Context context, File url) throws IOException {
            // Create URI
            File file=url;
            Uri uri = Uri.fromFile(file);

            Intent intent = new Intent(Intent.ACTION_VIEW);
            // Check what kind of file you are trying to open, by comparing the url with extensions.
            // When the if condition is matched, plugin sets the correct intent (mime) type,
            // so Android knew what application to use to open the file
            if (url.toString().contains(".doc") || url.toString().contains(".docx")) {
                // Word document
                intent.setDataAndType(uri, "application/msword");
            } else if(url.toString().contains(".pdf")) {
                // PDF file
                intent.setDataAndType(uri, "application/pdf");
            } else if(url.toString().contains(".ppt") || url.toString().contains(".pptx")) {
                // Powerpoint file
                intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
            } else if(url.toString().contains(".xls") || url.toString().contains(".xlsx")) {
                // Excel file
                intent.setDataAndType(uri, "application/vnd.ms-excel");
            } else if(url.toString().contains(".zip") || url.toString().contains(".rar")) {
                // WAV audio file
                intent.setDataAndType(uri, "application/x-wav");
            } else if(url.toString().contains(".rtf")) {
                // RTF file
                intent.setDataAndType(uri, "application/rtf");
            } else if(url.toString().contains(".wav") || url.toString().contains(".mp3")) {
                // WAV audio file
                intent.setDataAndType(uri, "audio/x-wav");
            } else if(url.toString().contains(".gif")) {
                // GIF file
                intent.setDataAndType(uri, "image/gif");
            } else if(url.toString().contains(".jpg") || url.toString().contains(".jpeg") || url.toString().contains(".png")) {
                // JPG file
                intent.setDataAndType(uri, "image/jpeg");
            } else if(url.toString().contains(".txt")) {
                // Text file
                intent.setDataAndType(uri, "text/plain");
            } else if(url.toString().contains(".3gp") || url.toString().contains(".mpg") || url.toString().contains(".mpeg") || url.toString().contains(".mpe") || url.toString().contains(".mp4") || url.toString().contains(".avi")) {
                // Video files
                intent.setDataAndType(uri, "video/*");
            } else {
                //if you want you can also define the intent type for any other file

                //additionally use else clause below, to manage other unknown extensions
                //in this case, Android will show all applications installed on the device
                //so you can choose which application to use
                intent.setDataAndType(uri, "*/*");
            }

            if(url.getPath().endsWith(".jpg") || url.getPath().endsWith(".jpeg")|| url.getPath().endsWith(".png"))
            {
                intent.setDataAndType(uri,"image/*");
            }

            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }


    class DownloadFileAsync extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... aurl) {
            int count;

            try {

                verifyStoragePermissions(getActivity());

                URL url = new URL(aurl[0]);
                URLConnection conexion = url.openConnection();
                conexion.connect();

                int lenghtOfFile = conexion.getContentLength();
                Log.d("ANDRO_ASYNC", "Lenght of file: " + lenghtOfFile);

                InputStream input = new BufferedInputStream(url.openStream(), 10240);

                if(filetype.equals("docx") || filetype.equals("doc")|| filetype.equals("xlsx") || filetype.equals("pdf")|| filetype.equals("pptx")|| filetype.equals("txt")|| filetype.equals("csv")){
                    File cacheFile = null;
                    if(isExternalStorageWritable()==true && isExternalStorageReadable()==true) {
                        cacheFile = new File(Environment.getExternalStoragePublicDirectory(
                                Environment.DIRECTORY_DOCUMENTS), "Classcubby/");
                        if (!cacheFile.mkdirs()) {
                            Toast.makeText(getActivity().getApplicationContext(),"Directory not created",Toast.LENGTH_LONG).show();
                        }
                    }
                    File file = new File(String.valueOf(new File(cacheFile, filename+"."+filetype)));

                    if (file.exists())
                        file.delete();

                    FileOutputStream output = new FileOutputStream(file);

                    byte buffer[] = new byte[1024];
                    int dataSize;
                    int loadedSize = 0;
                    while ((dataSize = input.read(buffer)) != -1) {
                        loadedSize += dataSize;
                        publishProgress(String.valueOf((int)((loadedSize*100)/lenghtOfFile)));
                        output.write(buffer, 0, dataSize);
                    }
                    output.close();
                }else{
                    File cacheFile = null;
                    if(isExternalStorageWritable()==true && isExternalStorageReadable()==true) {
                        cacheFile = new File(Environment.getExternalStoragePublicDirectory(
                                Environment.DIRECTORY_DCIM), "Classcubby/");
                        if (!cacheFile.mkdirs()) {
                            //Toast.makeText(getActivity().getApplicationContext(),"Directory not created",Toast.LENGTH_LONG).show();
                        }
                    }

                    File file = new File(String.valueOf(new File(cacheFile, filename+"."+filetype)));

                    if (file.exists())
                        file.delete();

                    FileOutputStream output = new FileOutputStream(file);

                    byte buffer[] = new byte[1024];
                    int dataSize;
                    int loadedSize = 0;
                    while ((dataSize = input.read(buffer)) != -1) {
                        loadedSize += dataSize;
                        publishProgress(String.valueOf((int)((loadedSize*100)/lenghtOfFile)));
                        output.write(buffer, 0, dataSize);
                    }
                    output.close();

                }


            } catch (Exception e) {
                Toast.makeText(getActivity().getApplicationContext(),e.getMessage().toString(),Toast.LENGTH_LONG).show();
            }
            return null;

        }
        protected void onProgressUpdate(String... progress) {
            Log.d("ANDRO_ASYNC",progress[0]);
            if(filedownloadingprogress.getVisibility()==View.VISIBLE) {
                filedownloadingprogress.setProgressWithAnimation(Integer.parseInt(progress[0]), 100);
            }else if(imagedownloadingprogress.getVisibility()==View.VISIBLE){
                imagedownloadingprogress.setProgressWithAnimation(Integer.parseInt(progress[0]), 100);
            }
        }

        @Override
        protected void onPostExecute(String unused) {
            if(filedownloadingprogress.getVisibility()==View.VISIBLE) {
                downloadtext.setVisibility(View.INVISIBLE);
                filedownloadingprogress.setVisibility(View.GONE);
                downloadcanceltext.setVisibility(View.INVISIBLE);
            }else if(imagedownloadingprogress.getVisibility()==View.VISIBLE){
                imagedownloadtext.setVisibility(View.INVISIBLE);
                imagedownloadingprogress.setVisibility(View.GONE);
                imagedownloadcanceltext.setVisibility(View.INVISIBLE);
                downloadbackgroundcolor.setVisibility(View.INVISIBLE);

                Glide.with(getActivity().getApplicationContext()).load(serverpathvalue).listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String s, Target<GlideDrawable> target, boolean b) {
                        Log.i("GLIDE", "onException :", e);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable glideDrawable, String s, Target<GlideDrawable> target, boolean b, boolean b1) {
                        Log.i("GLIDE", "onResourceReady");
                        iv_bitmap.setVisibility(View.VISIBLE);
                        return false;
                    }
                }).into(iv_bitmap);
            }
        }
    }

    class DownloadFilesizeAsync extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... aurl) {
            int count;

            try {

                verifyStoragePermissions(getActivity());

                URL url = new URL(aurl[0]);
                URLConnection conexion = url.openConnection();
                conexion.connect();

                int lenghtOfFile = conexion.getContentLength();
                Log.d("ANDRO_ASYNC", "Lenght of file: " + lenghtOfFile);

                InputStream input = new BufferedInputStream(url.openStream(), 10240);

                byte buffer[] = new byte[1024];
                int dataSize;
                int loadedSize = 0;
                while ((dataSize = input.read(buffer)) != -1) {
                    loadedSize += dataSize;
                    publishProgress(String.valueOf((int)((loadedSize*100)/lenghtOfFile)));
                    filesizenewvalue = loadedSize;
                }



            } catch (Exception e) {
                Toast.makeText(getActivity().getApplicationContext(),e.getMessage().toString(),Toast.LENGTH_LONG).show();
            }
            return null;

        }
        protected void onProgressUpdate(String... progress) {

        }

        @Override
        protected void onPostExecute(String unused) {
            if(attachmenttype.equals("Files")) {
                Long tempfilesize = Long.parseLong(String.valueOf(filesizenewvalue));
                String newfilesize = getfilesize(tempfilesize);
                filesizevalue.setText(newfilesize);
            }else {
                Long tempfilesize = Long.parseLong(String.valueOf(filesizenewvalue));
                String newfilesize = getfilesize(tempfilesize);
                imagesizevalue.setText(newfilesize);
            }
        }
    }


    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = { column };
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }


    private void gethourlist() {

        deleteCache(getActivity().getApplicationContext());

        if (classspinner != null && classspinner.getSelectedItem() !=null) {

            sharedPreferences = getActivity().getSharedPreferences(MY_PREFERENCES, MainActivity.MODE_PRIVATE);
            final String classschoolid = sharedPreferences.getString(loginconfig.key_school_id, "");
            final String userid = sharedPreferences.getString(loginconfig.key_userid, "");
            final String school = classschoolid;
            final String initialvalue = classspinner.getSelectedItem().toString().trim();
            final String initialdate = datetext1.getText().toString().trim();


            StringRequest newhourstringrequest = new StringRequest(Request.Method.POST, loginconfig.key_Subjectdetails_url,

                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            JSONObject j = null;
                            try {
                                j = new JSONObject(response);
                                loginresult = j.optJSONArray("result");

                                sharedPreferences = getActivity().getSharedPreferences(MY_PREFERENCES, MainActivity.MODE_PRIVATE);

                                //creating editor to store values of shared preferences
                                editor = sharedPreferences.edit();

                                editor.putString(loginconfig.key_subjectname_name, loginresult.toString());
                                editor.apply();

                                sethourspinner();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            snackbar = Snackbar.make(view, "Connection Response Error", Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.clear();
                    params.put(loginconfig.key_userid, userid);
                    params.put(loginconfig.key_spinnerclassname, initialvalue);
                    params.put(loginconfig.key_spinnerselecteddate, initialdate);
                    return params;
                }
            };
            RequestQueue hourreq = Volley.newRequestQueue(getActivity().getApplicationContext());
            hourreq.add(newhourstringrequest);
        }

    }

    private void sethourspinner() {

        final String hourarray = MainActivity.sharedPreferences.getString(loginconfig.key_subjectname_name, "sectionname");
        try {
            loginresult = new JSONArray(hourarray);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (hourarray != null) {
            try {
                hourlist = new ArrayList<String>(loginresult.length());

                for (int i = 0; i < loginresult.length(); i++)
                    try {
                        JSONObject json = loginresult.getJSONObject(i);

                        String dashboardtype = json.optString("subjectname");

                        hourlist.add(dashboardtype);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                hournewlist = new String[hourlist.size()];
                hournewlist = (String[]) hourlist.toArray(hournewlist);

                hourdataAdapter = new ArrayAdapter<String>(getContext(),
                        R.layout.spinner_item, hournewlist);
                hourdataAdapter.setDropDownViewResource(R.layout.spinner_item);
                hourspinner.setAdapter(hourdataAdapter);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


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

    DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            datetext.setText(String.valueOf(dayOfMonth) + "-" + String.valueOf(monthOfYear+1)
                    + "-" + String.valueOf(year));
        }
    };

    DatePickerDialog.OnDateSetListener ondate1 = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            datetext1.setText(String.valueOf(dayOfMonth) + "-" + String.valueOf(monthOfYear+1)
                    + "-" + String.valueOf(year));
        }
    };

    /** Updates the date in the TextView */
    private void updateDisplay() {
        datetext.setVisibility(View.VISIBLE);
        datetext.setText(
                new StringBuilder()
                        // Month is 0 based so add 1
                        .append(pDay).append("-")
                        .append(pMonth + 1).append("-")
                        .append(pYear).append(" "));
    }
    private void updateDisplay1() {
        datetext1.setVisibility(View.VISIBLE);
        datetext1.setText(
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
