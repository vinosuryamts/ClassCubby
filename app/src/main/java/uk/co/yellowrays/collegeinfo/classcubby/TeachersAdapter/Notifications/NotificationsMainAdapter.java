package uk.co.yellowrays.collegeinfo.classcubby.TeachersAdapter.Notifications;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

import uk.co.yellowrays.collegeinfo.classcubby.AppStatus;
import uk.co.yellowrays.collegeinfo.classcubby.MainActivity;
import uk.co.yellowrays.collegeinfo.classcubby.R;
import uk.co.yellowrays.collegeinfo.classcubby.Teacher_Dashboard_Activity;
import uk.co.yellowrays.collegeinfo.classcubby.configfiles.loginconfig;

/**
 * Created by user1 on 16-11-2016.
 */

public class NotificationsMainAdapter extends BaseAdapter {

    String[] notificationid,notificationmsg,notificationtype,postedbyname,postedbyimage,notificationpostedtime,notificationreaddate;
    Context context;
    LayoutInflater inflater = null;
    ViewHolder holder;
    Timer t;
    boolean timervalue;

    String[] strstudentid;
    String newnotificationid;

    String assignmentschoolname,assignmentschoolid,assignmentclassid,assignmentclassname,assignmentstudentid,assignmentstudentname,assignmentcount,assignmentuserimage;
    ArrayList<String> arrassignmentschoolname,arrassignmentschoolid,arrassignmentclassid,arrassignmentclassname,arrassignmentstudentid,arrassignmentstudentname,arrassignmentcount,arrassignmentuserimage;
    String userid;
    String studentid,studentname ,classname,schoolid,schoolname;
    String leaveschoolname,leaveschoolid,leavesclassid,leavesclassname,leavestudentid,leavestudentname,leavescount,leavesuserimage;
    ArrayList<String> arrleaveschoolname,arrleaveschoolid,arrleavesclassid,arrleavesclassname,arrleavestudentid,arrleavestudentname,arrleavescount,arrleavesuserimage;
    String attendanceschoolname,attendanceschoolid,attendanceclassid,attendanceclassname,attendancestudentid,attendancestudentname,attendancepercent,attendanceuserimage;
    ArrayList<String> arrattendanceschoolname,arrattendanceschoolid,arrattendanceclassid,arrattendanceclassname,arrattendancestudentid,arrattendancestudentname,arrattendancepercent,arrattendanceuserimage;

    public static final String MY_PREFERENCES = "loginconfig.shared_pref_name";
    public static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor editor;
    private JSONArray loginresult;



    public NotificationsMainAdapter(Context context, String[] strnotificationid,
                                    String[] strnotificationmsg, String[] strnotificationtype, String[] strpostedbyname,
                                    String[] strpostedbyimage, String[] strnotificationpostedtime, String[] strnotificationreaddate,
                                    Timer timer, Boolean timerval) {
        this.context = context;
        this.notificationid = strnotificationid;
        this.notificationmsg = strnotificationmsg;
        this.notificationtype = strnotificationtype;
        this.postedbyname = strpostedbyname;
        this.postedbyimage = strpostedbyimage;
        this.notificationpostedtime = strnotificationpostedtime;
        this.notificationreaddate = strnotificationreaddate;
        this.t = timer;
        this.timervalue = timerval;
        inflater = LayoutInflater.from(context.getApplicationContext());

    }

    @Override
    public int getCount() {
        return notificationid.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder {
        TextView studentname,countvalue,notificationid,eventfor,eventtitle,evendescription,postedby;
        ImageView colorimage,imageview;
        RelativeLayout overallcontainer;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        Log.d("posiiton ImageAdapater:", "" + position);
        if (row == null) {
            row = inflater.inflate(R.layout.notifications_row_items_xml, null);
            holder = new ViewHolder();
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        holder.studentname = (TextView) row.findViewById(R.id.studentname);
        holder.countvalue = (TextView) row.findViewById(R.id.countvalue);
        holder.notificationid = (TextView) row.findViewById(R.id.notificationid);
        holder.colorimage = (ImageView) row.findViewById(R.id.colorimage);
        holder.imageview = (ImageView) row.findViewById(R.id.imageview);
        holder.overallcontainer = (RelativeLayout) row.findViewById(R.id.overallcontainer);


        Typeface semiboldtypeface = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSansSemibold.ttf");
        Typeface normaltypeface = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSansRegular.ttf");
        holder.studentname.setTypeface(semiboldtypeface);
        holder.countvalue.setTypeface(semiboldtypeface);


        holder.studentname.setText(notificationmsg[position]);
        holder.countvalue.setText(notificationpostedtime[position]);

        holder.notificationid.setText(notificationid[position]);

        if(notificationreaddate[position].equals("null")){
            holder.overallcontainer.setBackgroundColor(context.getResources().getColor(R.color.lightgreennew));
        }else{
            holder.overallcontainer.setBackgroundColor(context.getResources().getColor(R.color.white));
        }

        if(notificationtype[position].equals("Event")){
            holder.imageview.setBackground(context.getResources().getDrawable(R.drawable.notificationevent));
            holder.imageview.setTag("Event");
        }else if(notificationtype[position].equals("Leave Request")){
            holder.imageview.setBackground(context.getResources().getDrawable(R.drawable.notificationleaveicon));
            holder.imageview.setTag("Leave Request");
        }else if(notificationtype[position].equals("Attendance")){
            holder.imageview.setBackground(context.getResources().getDrawable(R.drawable.notificationattendance));
            holder.imageview.setTag("Attendance");
        }else if(notificationtype[position].equals("Assignment")){
            holder.imageview.setBackground(context.getResources().getDrawable(R.drawable.notificationassignment));
            holder.imageview.setTag("Assignment");
        }else if(notificationtype[position].equals("Outgoing Request")){
            holder.imageview.setBackground(context.getResources().getDrawable(R.drawable.notificationleaveicon));
            holder.imageview.setTag("Outgoing Request");
        }

        Glide.with(context).load(postedbyimage[position]).listener(new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String s, Target<GlideDrawable> target, boolean b) {
                Log.i("GLIDE", "onException :", e);
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable glideDrawable, String s, Target<GlideDrawable> target, boolean b, boolean b1) {
                Log.i("GLIDE", "onResourceReady");
                holder.colorimage.setVisibility(View.VISIBLE);
                return false;
            }
        }).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.colorimage);



        holder.overallcontainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView img = (ImageView) v.findViewById(R.id.imageview);
                TextView id = (TextView) v.findViewById(R.id.notificationid);
                String imageName = (String)img.getTag();
                newnotificationid = id.getText().toString().trim();

                sharedPreferences = context.getSharedPreferences(MY_PREFERENCES, MainActivity.MODE_PRIVATE);
                final String nusertypeid = sharedPreferences.getString(loginconfig.key_usertypeid, "");

                if(nusertypeid.equals("2")) {
                    if (imageName.equals("Event")) {
                        if(timervalue==true){
                            t.cancel();
                            t = null;
                            timervalue = false;
                        }
                        getnotificationupdate();

                        final Bundle bundle = new Bundle();
                        final Intent intent = new Intent(context, Teacher_Dashboard_Activity.class);
                        bundle.putString("TabNumber", "4");
                        intent.putExtras(bundle);
                        (context).startActivity(intent);
                        ((Activity)context).finish();

                    } else if (imageName.equals("Leave Request")) {
                        if (AppStatus.getInstance(context).isOnline()) {
                            if(timervalue==true){
                                t.cancel();
                                t = null;
                                timervalue = false;
                            }
                            getnotificationupdate();

                            final Bundle bundle = new Bundle();
                            final Intent intent = new Intent(context, Teacher_Dashboard_Activity.class);
                            bundle.putString("TabNumber", "3");
                            intent.putExtras(bundle);
                            (context).startActivity(intent);
                            ((Activity)context).finish();

                        }
                    } else if (imageName.equals("Attendance")) {
                        if (AppStatus.getInstance(context).isOnline()) {
                            if(timervalue==true){
                                t.cancel();
                                t = null;
                                timervalue = false;
                            }
                            getnotificationupdate();

                            final Bundle bundle = new Bundle();
                            final Intent intent = new Intent(context, Teacher_Dashboard_Activity.class);
                            bundle.putString("TabNumber", "1");
                            intent.putExtras(bundle);
                            (context).startActivity(intent);
                            ((Activity)context).finish();

                        }
                    } else if (imageName.equals("Assignment")) {
                        if (AppStatus.getInstance(context).isOnline()) {
                            if(timervalue==true){
                                t.cancel();
                                t = null;
                                timervalue = false;
                            }
                            getnotificationupdate();

                            final Bundle bundle = new Bundle();
                            final Intent intent = new Intent(context, Teacher_Dashboard_Activity.class);
                            bundle.putString("TabNumber", "2");
                            intent.putExtras(bundle);
                            (context).startActivity(intent);
                            ((Activity)context).finish();

                        }
                    }
                }else if(nusertypeid.equals("3")||nusertypeid.equals("4")){
                    if (imageName.equals("Event")) {
                        if(timervalue==true){
                            t.cancel();
                            t = null;
                            timervalue = false;
                        }
                        getnotificationupdate();

                        final Bundle bundle = new Bundle();
                        final Intent intent = new Intent(context, Teacher_Dashboard_Activity.class);
                        bundle.putString("TabNumber", "4");
                        intent.putExtras(bundle);
                        (context).startActivity(intent);
                        ((Activity)context).finish();

                    } else if (imageName.equals("Leave Request")) {
                        if (AppStatus.getInstance(context).isOnline()) {
                            if(timervalue==true){
                                t.cancel();
                                t = null;
                                timervalue = false;
                            }
                            getnotificationupdate();

                            final Bundle bundle = new Bundle();
                            final Intent intent = new Intent(context, Teacher_Dashboard_Activity.class);
                            bundle.putString("TabNumber", "3");
                            intent.putExtras(bundle);
                            (context).startActivity(intent);
                            ((Activity)context).finish();

                        }
                    } else if (imageName.equals("Attendance")) {
                        if (AppStatus.getInstance(context).isOnline()) {
                            if(timervalue==true){
                                t.cancel();
                                t = null;
                                timervalue = false;
                            }
                            getnotificationupdate();

                            final Bundle bundle = new Bundle();
                            final Intent intent = new Intent(context, Teacher_Dashboard_Activity.class);
                            bundle.putString("TabNumber", "1");
                            intent.putExtras(bundle);
                            (context).startActivity(intent);
                            ((Activity)context).finish();

                        }
                    } else if (imageName.equals("Assignment")) {
                        if (AppStatus.getInstance(context).isOnline()) {
                            if(timervalue==true){
                                t.cancel();
                                t = null;
                                timervalue = false;
                            }
                            getnotificationupdate();

                            final Bundle bundle = new Bundle();
                            final Intent intent = new Intent(context, Teacher_Dashboard_Activity.class);
                            bundle.putString("TabNumber", "2");
                            intent.putExtras(bundle);
                            (context).startActivity(intent);
                            ((Activity)context).finish();

                        }
                    }else if (imageName.equals("Outgoing Request")) {
                        if (AppStatus.getInstance(context).isOnline()) {
                            if(timervalue==true){
                                t.cancel();
                                t = null;
                                timervalue = false;
                            }
                            getnotificationupdate();

                            final Bundle bundle = new Bundle();
                            final Intent intent = new Intent(context, Teacher_Dashboard_Activity.class);
                            bundle.putString("TabNumber", "5");
                            intent.putExtras(bundle);
                            (context).startActivity(intent);
                            ((Activity)context).finish();

                        }
                    }
                }
            }
        });
        return row;
    }


    private void getnotificationupdate() {
        userid = MainActivity.sharedPreferences.getString(loginconfig.key_userid, "");

        StringRequest studentrequest1 = new StringRequest(Request.Method.POST, loginconfig.key_user_notificationupdate_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        JSONObject j = null;
                        try {
                            j = new JSONObject(s);
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

                params.put(loginconfig.key_userid, userid);
                params.put(loginconfig.key_notification_idvalue, newnotificationid);

                return params;
            }
        };
        RequestQueue studentlist = Volley.newRequestQueue(context);
        studentlist.add(studentrequest1);
    }


}
