package uk.co.yellowrays.collegeinfo.classcubby.ParentsAdapter.Leavetracker;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.io.File;

import uk.co.yellowrays.collegeinfo.classcubby.R;

/**
 * Created by Vino on 10/24/2017.
 */

public class Parents_Leavetracker_rowitems_click extends android.support.v4.app.Fragment {

    View mainview,view;
    ImageView popupstudentimage;
    TextView popuptimesubmitted,popupstudentname,popupfromdate,popuptodate,popupleavesubject,popupleavedetail,popuprejectreasontext;
    TextView popuprejectreason,popupstatus,leavepagename;
    TextView popupfromdatetext,popuptodatetext,leavestatus,popupleavedesubject,popupleavedescriptiontext,popuprejectreasontextvalue;

    public static final String MY_PREFERENCES = "loginconfig.shared_pref_name";
    public static final String Previous_PREFERENCES = "previousloginconfig.shared_pref_name";
    public static SharedPreferences sharedPreferences,previoussharedPreferences;
    public static SharedPreferences.Editor editor,previouseditor;
    private static boolean isitemclicked,generateitemclicked;

    public Parents_Leavetracker_rowitems_click() {
        // Required empty public constructor
    }

    public static Parents_Leavetracker_rowitems_click newInstance(String param1) {
        Parents_Leavetracker_rowitems_click fragment = new Parents_Leavetracker_rowitems_click();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.parents_leavetracker_click_rowitems_xml, null);

        popupstudentimage = (ImageView) view.findViewById(R.id.popupstudentimage);
        popuptimesubmitted = (TextView)view.findViewById(R.id.popuptimesubmitted);
        popupstudentname = (TextView)view.findViewById(R.id.popupstudentname);
        popupfromdate = (TextView)view.findViewById(R.id.popupfromdate);
        popuptodate = (TextView)view.findViewById(R.id.popuptodate);
        popupleavesubject = (TextView)view.findViewById(R.id.popupleavesubject);
        popupleavedetail = (TextView)view.findViewById(R.id.popupleavedetail);
        popuprejectreason = (TextView)view.findViewById(R.id.popuprejectreason);
        popupstatus = (TextView)view.findViewById(R.id.status);
        popuprejectreasontext = (TextView)view.findViewById(R.id.popuprejectreasontext);
        leavepagename = (TextView)view.findViewById(R.id.leavepagename);

        popupfromdatetext = (TextView)view.findViewById(R.id.popupfromdatetext);
        popuptodatetext = (TextView)view.findViewById(R.id.popuptodatetext);
        leavestatus = (TextView)view.findViewById(R.id.leavestatus);
        popupleavedesubject = (TextView)view.findViewById(R.id.popupleavedesubject);
        popupleavedescriptiontext = (TextView)view.findViewById(R.id.popupleavedescriptiontext);


        mainview = view.findViewById(R.id.parentsdashboardnewwholecontainer);

        Typeface normaltypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/OpenSansRegular.ttf");
        Typeface boldtypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/OpenSansBold.ttf");
        leavepagename.setTypeface(boldtypeface);
        popupfromdatetext.setTypeface(boldtypeface);
        popuptodatetext.setTypeface(boldtypeface);
        leavestatus.setTypeface(boldtypeface);
        popupleavedesubject.setTypeface(boldtypeface);
        popupleavedescriptiontext.setTypeface(boldtypeface);
        popuprejectreasontext.setTypeface(boldtypeface);
        popuptimesubmitted.setTypeface(normaltypeface);
        popupstudentname.setTypeface(boldtypeface);
        popupfromdate.setTypeface(normaltypeface);
        popuptodate.setTypeface(normaltypeface);
        popupleavesubject.setTypeface(normaltypeface);
        popupleavedetail.setTypeface(normaltypeface);
        popuprejectreason.setTypeface(normaltypeface);
        popupstatus.setTypeface(normaltypeface);


        Bundle bundle = getArguments();
        String timesubmitted = bundle.getString("timesubmitted");
        String userimage = bundle.getString("userimage");
        String studentname = bundle.getString("studentname");
        String fromdate = bundle.getString("fromdate");
        String todate = bundle.getString("todate");
        String leavedetail = bundle.getString("leavedetail");
        String leavesubject = bundle.getString("leavesubject");
        String rejectreason = bundle.getString("rejectreason");
        String status = bundle.getString("status");

        Glide.with(getActivity()).load(userimage).listener(new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String s, Target<GlideDrawable> target, boolean b) {
                Log.i("GLIDE", "onException :", e);
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable glideDrawable, String s, Target<GlideDrawable> target, boolean b, boolean b1) {
                Log.i("GLIDE", "onResourceReady");
                return false;
            }
        }).into(popupstudentimage);

        popuptimesubmitted.setText(timesubmitted);
        popupstudentname.setText(studentname);
        popupfromdate.setText(fromdate);
        popuptodate.setText(todate);

        leavedetail = leavedetail.replace("`",",");
        rejectreason = rejectreason.replace("`",",");
        leavesubject = leavesubject.replace("`",",");

        popupleavedetail.setText(leavedetail);
        popupleavesubject.setText(leavesubject);

        if(rejectreason.trim().equals("")){
            popuprejectreasontext.setVisibility(View.GONE);
            popuprejectreason.setVisibility(View.GONE);
        } else{
            popuprejectreasontext.setVisibility(View.VISIBLE);
            popuprejectreason.setVisibility(View.VISIBLE);
            popuprejectreason.setText(rejectreason);
        }

        if(status.equals("Rejected")){
            popupstatus.setText(status);
            popupstatus.setTextColor(getResources().getColor(R.color.excusedabsent));
        }else if(status.equals("Accepted")){
            popupstatus.setText(status);
            popupstatus.setTextColor(getResources().getColor(R.color.present));
        }else{
            popupstatus.setText(status);
            popupstatus.setTextColor(getResources().getColor(R.color.grey));
        }


        return view;
    }

    public static void setclicked(boolean value){
        isitemclicked=value;
    }
    public static boolean getclicked(){
        return isitemclicked;
    }

    public static void setgenerateclicked(boolean value){
        generateitemclicked=value;
    }
    public static boolean getgenerateclicked(){
        return generateitemclicked;
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
