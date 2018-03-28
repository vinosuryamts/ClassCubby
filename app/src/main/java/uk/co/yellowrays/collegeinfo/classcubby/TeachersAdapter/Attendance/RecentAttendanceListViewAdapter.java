package uk.co.yellowrays.collegeinfo.classcubby.TeachersAdapter.Attendance;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import uk.co.yellowrays.collegeinfo.classcubby.FontManager.CustomTabLayout;
import uk.co.yellowrays.collegeinfo.classcubby.MainActivity;
import uk.co.yellowrays.collegeinfo.classcubby.R;
import uk.co.yellowrays.collegeinfo.classcubby.configfiles.loginconfig;
import uk.co.yellowrays.collegeinfo.classcubby.configfiles.previousloginconfig;
import uk.co.yellowrays.collegeinfo.classcubby.customtabadapter.CustomViewPager;

import static uk.co.yellowrays.collegeinfo.classcubby.TeachersAdapter.Attendance.Teacher_Attendance_Main_Activity.deleteCache;

/**
 * Created by Vino on 9/27/2017.
 */

public class RecentAttendanceListViewAdapter extends BaseAdapter {


    Context context;
    LayoutInflater inflater = null;
    ListView attendancelist;
    GridView attendancegridview;
    TextView emptytext,attendancepagename;
    Button submitbutton;
    ViewHolder holder;
    TextView vkdepartmentname,vkhourname,vkpresentcount,vkabsentcount,vkondutycount,vktakendate;
    private FragmentActivity myContext;
    FloatingActionButton filter;


    ArrayList<String> classid,classnamelist,hourid,hourname,absentcountval,presentcountval,ondutycountval,takendate;
    public ArrayList<String> namelist,studentidlist,rollnumberlist,attendancevaluelist;
    public ArrayList<String> ImageList;
    ArrayList<String> attendancevaluesfromadapter;

    public static String[] prgmNameList1;
    public static String[] prgmImages;

    private JSONArray loginresult;
    String attendancetype,collegeid;

    View mainview;
    CustomViewPager viewpager;
    CustomTabLayout tabLayout;
    RelativeLayout attendancepagemaincontainer,innerfilterlayout;

    private int lastPosition = -1 ;
    Snackbar snackbar;
    String classname,classsection,hournamevalue,datevalue,loogedinusertypeid;



    static boolean isitemclicked,filterisOpen,newgenerateclicled,isgenerateclicked;
    static boolean notificationvalue;
    static boolean notification1value;
    static String attendancetypevalue,classnamevalue,classsectionvalue,classhourvalue,classdate;
    static ArrayList<String> idlist ,usernamelist,usermageList,userrollnumberlist,attendancelistvalue;
    static Activity activitycontext;
    public static int[] attendancevalues={R.drawable.presenticon,R.drawable.excusedabsenticon,R.drawable.absenticon,R.drawable.ondutyicon};
    public static final String MY_PREFERENCES = "loginconfig.shared_pref_name";
    public static final String Previous_PREFERENCES = "previousloginconfig.shared_pref_name";
    public static SharedPreferences sharedPreferences,previoussharedPreferences;
    public static SharedPreferences.Editor editor,previouseditor;

    public RecentAttendanceListViewAdapter(Activity context, ArrayList<String> classidlist,
                                           ArrayList<String> classnamelist, ArrayList<String> houridlist, ArrayList<String> hournamelist,
                                           ArrayList<String> absentcountlist, ArrayList<String> presentcountlist,
                                           ArrayList<String> ondutycountlist, ArrayList<String> takendatelist,
                                           ListView attendancelistview, TextView empty, Button submit, String schoolid,
                                           TextView attendancepagename, View view, RelativeLayout attendancepagemaincontainer,
                                           FloatingActionButton filter, RelativeLayout innerfilterlayout, GridView attendancegridview) {
        this.context = context;
        this.activitycontext = context;
        this.classid = classidlist;
        this.classnamelist = classnamelist;
        this.hourid = houridlist;
        this.hourname = hournamelist;
        this.absentcountval = absentcountlist;
        this.presentcountval = presentcountlist;
        this.ondutycountval = ondutycountlist;
        this.takendate = takendatelist;
        this.attendancelist = attendancelistview;
        this.emptytext = empty;
        this.submitbutton = submit;
        this.collegeid = schoolid;
        this.attendancepagename = attendancepagename;
        this.attendancepagemaincontainer = attendancepagemaincontainer;
        this.mainview = view;
        this.filter = filter;
        this.attendancegridview = attendancegridview;
        this.innerfilterlayout = innerfilterlayout;
        this.newgenerateclicled = false;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return classnamelist.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public static boolean getclicked(){
        return isitemclicked;
    }
    public static boolean newgenerateclicked(){
        return newgenerateclicled;
    }
    public static boolean getgenerateclick(){
        return isgenerateclicked;
    }
    public static boolean getisfileropen(){
        return filterisOpen;
    }

    public static void setclicked(boolean value){
        isitemclicked=value;
    }
    public static void setFilterisOpen(boolean value){
        filterisOpen=value;
    }
    public static void setgenerateclicked(boolean value){
        isgenerateclicked=value;
    }
    public static void setNewgenerateclicled(boolean value){
        newgenerateclicled=value;
    }
    public static void setnotificationpreviousset(boolean value){
        notificationvalue=value;
    }

    public static String getattendancetype(){
        return attendancetypevalue;
    }

    public static String getClassnamevalue(){
        return classnamevalue;
    }
    public static String getClasssectionvalue(){
        return classsectionvalue;
    }

    public static String getClasshourvalue(){
        return classhourvalue;
    }

    public static String getClassdate(){
        return classdate;
    }
    public static ArrayList<String> getIdlist(){
        return idlist;
    }
    public static ArrayList<String> getUsernamelist(){
        return usernamelist;
    }
    public static ArrayList<String> getUsermageList(){
        return usermageList;
    }
    public static ArrayList<String> getUserrollnumberlist(){
        return userrollnumberlist;
    }
    public static ArrayList<String> getAttendancelistvalue(){
        return attendancelistvalue;
    }
    public static boolean getnotificationpreviousset(){
        return notificationvalue;
    }

    public static void setsecondnotificationpreviousset(boolean value){
        notification1value=value;
    }

    public static boolean getsecondnotificationpreviousset(){
        return notification1value;
    }

    static class ViewHolder {
        TextView departmentname,hourname,presentcount,absentcount,ondutycount,takendate;
        RelativeLayout recentattendanceoverallcontainer,presentcontainer,absentcontainer,ondutycontainer;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            row = inflater.inflate(R.layout.teacher_recent_gridview_attendance_xml, null);
            holder = new ViewHolder();
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        holder.departmentname = (TextView) row.findViewById(R.id.departmentname);
        holder.recentattendanceoverallcontainer = (RelativeLayout) row.findViewById(R.id.recentattendanceoverallcontainer);
        holder.presentcontainer = (RelativeLayout) row.findViewById(R.id.presentcontainer);
        holder.absentcontainer = (RelativeLayout) row.findViewById(R.id.absentcontainer);
        holder.ondutycontainer = (RelativeLayout) row.findViewById(R.id.ondutycontainer);
        holder.hourname = (TextView) row.findViewById(R.id.hourname);
        holder.presentcount = (TextView) row.findViewById(R.id.presentcount);
        holder.absentcount = (TextView) row.findViewById(R.id.absentcount);
        holder.ondutycount = (TextView) row.findViewById(R.id.ondutycount);
        holder.takendate = (TextView) row.findViewById(R.id.takendate);

        Typeface normaltypeface = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSansRegular.ttf");
        Typeface semiboldtypeface = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSansSemibold.ttf");
        Typeface boldtypeface = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSansBold.ttf");
        holder.departmentname.setTypeface(boldtypeface);
        holder.presentcount.setTypeface(boldtypeface);
        holder.absentcount.setTypeface(boldtypeface);
        holder.ondutycount.setTypeface(boldtypeface);
        holder.hourname.setTypeface(normaltypeface);
        holder.takendate.setTypeface(normaltypeface);

        holder.departmentname.setText(classnamelist.get(position));
        holder.hourname.setText(hourname.get(position));
        holder.takendate.setText(takendate.get(position));

        if(presentcountval.get(position).equals("null")){
            holder.presentcount.setText("0");
        }else {
            holder.presentcount.setText(presentcountval.get(position));
        }

        if(absentcountval.get(position).equals("null")){
            holder.absentcount.setText("0");
        }else {
            holder.absentcount.setText(absentcountval.get(position));
        }

        if(ondutycountval.get(position).equals("null")){
            holder.ondutycount.setText("0");
        }else {
            holder.ondutycount.setText(ondutycountval.get(position));
        }

        isitemclicked=false;
        filterisOpen = false;

        final View finalRow = row;
        holder.recentattendanceoverallcontainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(filterisOpen==true){
                    attendancelist.setEnabled(false);
                    attendancegridview.setEnabled(false);
                }else{
                    sharedPreferences = context.getSharedPreferences(MY_PREFERENCES, MainActivity.MODE_PRIVATE);
                    filterisOpen = sharedPreferences.getBoolean(loginconfig.key_attendancefilteropen, true);

                    if(filterisOpen==true){
                        attendancelist.setEnabled(true);
                        attendancegridview.setEnabled(true);

                        if(isitemclicked==true){
                            submitbutton.setVisibility(View.VISIBLE);
                            submitbutton.setClickable(true);
                        }


                        int x = attendancepagemaincontainer.getRight();
                        int y = attendancepagemaincontainer.getBottom();

                        int startRadius = Math.max(attendancepagemaincontainer.getWidth(), attendancepagemaincontainer.getHeight());
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

                        filter.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.blue)));
                        filter.setImageResource(R.drawable.filterwhite);
                    }

                    holder = (ViewHolder) finalRow.getTag();
                    vkdepartmentname = (TextView) v.findViewById(R.id.departmentname);
                    vkhourname = (TextView) v.findViewById(R.id.hourname);
                    vktakendate = (TextView) v.findViewById(R.id.takendate);

                    isitemclicked = true;

                    attendancepagename.setText("Attendance for Class" + vkdepartmentname.getText().toString().trim() );

                    sharedPreferences = context.getSharedPreferences(MY_PREFERENCES, MainActivity.MODE_PRIVATE);

                    //creating editor to store values of shared preferences
                    editor = sharedPreferences.edit();

                    String[] parts = vkdepartmentname.getText().toString().trim().split(" - ");
                    classname = parts[0]; // 004
                    classnamevalue = classname;
                    classsection = parts[1];
                    classsectionvalue = classsection;
                    hournamevalue = vkhourname.getText().toString().trim();
                    classhourvalue = hournamevalue;
                    datevalue = vktakendate.getText().toString().trim();
                    classdate = datevalue;

                    editor.putString(loginconfig.key_class, classname);
                    editor.putString(loginconfig.key_section, classsection);
                    editor.putString(loginconfig.key_attendancedate, datevalue);
                    editor.apply();

                    StringRequest studentrequest1 = new StringRequest(Request.Method.POST, loginconfig.key_studentdetails_url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String s) {
                                    JSONObject j = null;
                                    try {
                                        j = new JSONObject(s);
                                        loginresult = j.getJSONArray("result");

                                        namelist = new ArrayList<String>(loginresult.length());
                                        idlist = new ArrayList<String>(loginresult.length());
                                        usernamelist = new ArrayList<String>(loginresult.length());
                                        studentidlist = new ArrayList<String>(loginresult.length());
                                        ImageList = new ArrayList<String>(loginresult.length());
                                        usermageList = new ArrayList<String>(loginresult.length());
                                        rollnumberlist = new ArrayList<String>(loginresult.length());
                                        userrollnumberlist = new ArrayList<String>(loginresult.length());
                                        attendancevaluelist = new ArrayList<String>(loginresult.length());
                                        attendancelistvalue = new ArrayList<String>(loginresult.length());

                                        if (loginresult.length() > 0) {
                                            for (int i = 0; i < loginresult.length(); i++) {
                                                JSONObject json = loginresult.getJSONObject(i);
                                                String studentid = json.optString("userid");
                                                String studentname = json.optString("username");
                                                String studentimage = json.optString("userimage");
                                                String rollnumber = json.optString("rollnumber");
                                                String attendancevalue = json.optString("attendancevalue");
                                                attendancetype = json.optString("value");

                                                attendancetypevalue = attendancetype;

                                                studentidlist.add(studentid);
                                                idlist.add(studentid);
                                                namelist.add(studentname);
                                                usernamelist.add(studentname);
                                                ImageList.add(studentimage);
                                                usermageList.add(studentimage);
                                                rollnumberlist.add(rollnumber);
                                                userrollnumberlist.add(rollnumber);

                                                if (attendancetype.equals("1")) {
                                                    attendancevaluelist.add(attendancevalue);
                                                    attendancelistvalue.add(attendancevalue);
                                                } else {
                                                    if (attendancevalue.equals("Excused Absent")) {
                                                        attendancevaluelist.add("Excused Absent");
                                                        attendancelistvalue.add("Excused Absent");
                                                    } else {
                                                        attendancevaluelist.add("Present");
                                                        attendancelistvalue.add("Present");
                                                    }
                                                }
                                            }

                                            emptytext.setVisibility(View.INVISIBLE);
                                            attendancelist.setVisibility(View.VISIBLE);
                                            attendancegridview.setVisibility(View.VISIBLE);
                                            submitbutton.setVisibility(View.VISIBLE);

                                            prgmNameList1 = new String[namelist.size()];
                                            prgmNameList1 = (String[]) namelist.toArray(prgmNameList1);

                                            prgmImages = new String[ImageList.size()];
                                            prgmImages = (String[]) ImageList.toArray(prgmImages);

                                            submitbutton.setClickable(true);

                                            attendancegridview.setVisibility(View.VISIBLE);
                                            //attendancelist.setAdapter(new AttendancetrackerListviewadapter(context, studentidlist, namelist, ImageList, rollnumberlist, attendancevaluelist, attendancevalues));
                                            attendancegridview.setAdapter(new AttendancetrackerListviewadapter(context, studentidlist, namelist, ImageList, rollnumberlist, attendancevaluelist, attendancevalues));
                                            attendancegridview.setNumColumns(2);


                                            attendancegridview.setOnScrollListener(new AbsListView.OnScrollListener() {
                                                @Override
                                                public void onScrollStateChanged(AbsListView view, int scrollState) {

                                                }

                                                @Override
                                                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                                                    boolean filtervalueopen = RecentAttendanceListViewAdapter.getisfileropen();

                                                    if(filtervalueopen==true){
                                                        attendancelist.setEnabled(false);
                                                    }else{

                                                    }
                                                }
                                            });


                                        } else {

                                            emptytext.setText("No Students are mapped to this class.");
                                            emptytext.setVisibility(View.VISIBLE);
                                            attendancelist.setVisibility(View.INVISIBLE);
                                            attendancegridview.setVisibility(View.INVISIBLE);
                                            submitbutton.setVisibility(View.INVISIBLE);

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

                            params.put(loginconfig.key_school_id, collegeid);
                            params.put(loginconfig.key_class, classname);
                            params.put(loginconfig.key_section, classsection);
                            params.put(loginconfig.key_hour_name, hournamevalue);
                            params.put(loginconfig.key_attendancedate, datevalue);

                            return params;
                        }
                    };
                    RequestQueue studentlist = Volley.newRequestQueue(context);
                    studentlist.add(studentrequest1);
                }


            }
        });

        final View finalRow1 = row;
        finalRow1.setVisibility(View.INVISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Animation animation = AnimationUtils.loadAnimation(context, (position > lastPosition) ? R.anim.textlefttoright : R.anim.textlefttoright);
                finalRow1.startAnimation(animation);
                lastPosition = position;
                finalRow1.setVisibility(View.VISIBLE);
            }
        },100);

        return row;
    }


    private void existingupload(JSONObject jObject) {

        deleteCache(context);

        final String existinguploadvalue = jObject.toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, loginconfig.key_studentexistingupload_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        JSONObject j = null;
                        try {
                            attendancelist.setVisibility(View.VISIBLE);
                            emptytext.setVisibility(View.INVISIBLE);
                            submitbutton.setVisibility(View.INVISIBLE);

                            snackbar = Snackbar.make(mainview,"Attendance Edited Successfully",Snackbar.LENGTH_LONG);
                            snackbar.show();

                            previoussharedPreferences = context.getSharedPreferences(Previous_PREFERENCES, MainActivity.MODE_PRIVATE);
                            loogedinusertypeid = previoussharedPreferences.getString(previousloginconfig.key_usertypeid, "");

                            filter.setVisibility(View.INVISIBLE);

                            myContext=(FragmentActivity) context;

                            Teacher_Attendance_Main_Activity innerFragment = new Teacher_Attendance_Main_Activity ().newInstance("new");

                            FragmentManager fragmentManager = myContext.getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.add(R.id.attendancepagemaincontainer, innerFragment);
                            fragmentTransaction.addToBackStack("new");
                            fragmentTransaction.commit();

                        } catch (Exception e) {
                            snackbar = Snackbar.make(mainview,"Kindly check with administrator for this error",Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                snackbar = Snackbar.make(mainview,"Unable to contact webservice",Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        })

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.clear();

                params.put(loginconfig.key_existingupload, existinguploadvalue);

                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);

    }

    private void newupload(JSONObject jObject) {

        deleteCache(context);

        final String existinguploadvalue = jObject.toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, loginconfig.key_studentattendancenewupload_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        JSONObject j = null;
                        try {
                            attendancelist.setVisibility(View.VISIBLE);
                            emptytext.setVisibility(View.INVISIBLE);
                            submitbutton.setVisibility(View.INVISIBLE);

                            snackbar = Snackbar.make(mainview,"Attendance Created Successfully",Snackbar.LENGTH_LONG);
                            snackbar.show();

                            filter.setVisibility(View.INVISIBLE);

                            Teacher_Attendance_Main_Activity innerFragment = new Teacher_Attendance_Main_Activity ().newInstance("new");

                            FragmentManager fragmentManager = myContext.getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.add(R.id.attendancepagemaincontainer, innerFragment);
                            fragmentTransaction.addToBackStack("new");
                            fragmentTransaction.commit();

                        } catch (Exception e) {
                            snackbar = Snackbar.make(mainview,"Kindly check with administrator for this error",Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                snackbar = Snackbar.make(mainview,"Unable to contact webservice",Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        })

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.clear();

                params.put(loginconfig.key_existingupload, existinguploadvalue);

                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);

    }

}
