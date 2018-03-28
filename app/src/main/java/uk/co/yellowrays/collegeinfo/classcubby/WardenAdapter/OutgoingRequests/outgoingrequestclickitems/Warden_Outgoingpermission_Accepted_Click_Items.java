package uk.co.yellowrays.collegeinfo.classcubby.WardenAdapter.OutgoingRequests.outgoingrequestclickitems;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import uk.co.yellowrays.collegeinfo.classcubby.AppStatus;
import uk.co.yellowrays.collegeinfo.classcubby.MainActivity;
import uk.co.yellowrays.collegeinfo.classcubby.R;
import uk.co.yellowrays.collegeinfo.classcubby.configfiles.loginconfig;

/**
 * Created by Vino on 10/23/2017.
 */

public class Warden_Outgoingpermission_Accepted_Click_Items extends android.support.v4.app.Fragment {

    View view,mainview;
    ImageView popupstudentimage;
    EditText popuprejectreason;
    Button accpet,reject,close;
    TextView popuptimesubmitted,popupstudentname,popupfromdate,popuptodate,popupleavedetail;
    RelativeLayout popupthirdrowfiltercontainer;
    Snackbar snackbar;
    String userid;

    private String blockCharacterSet = "~`\"|[]";

    private InputFilter filter = new InputFilter() {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            if (source != null && blockCharacterSet.contains(("" + source))) {
                return "";
            }
            return null;
        }

    };

    public Warden_Outgoingpermission_Accepted_Click_Items() {
        // Required empty public constructor
    }

    public static Warden_Outgoingpermission_Accepted_Click_Items newInstance(String param1) {
        Warden_Outgoingpermission_Accepted_Click_Items fragment = new Warden_Outgoingpermission_Accepted_Click_Items();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.warden_outgoingpermission_accept_list_click_rowitems_xml, null);

        popupstudentimage = (ImageView) view.findViewById(R.id.popupstudentimage);
        popuptimesubmitted = (TextView)view.findViewById(R.id.popuptimesubmitted);
        popupstudentname = (TextView)view.findViewById(R.id.popupstudentname);
        popupfromdate = (TextView)view.findViewById(R.id.popupfromdate);
        popuptodate = (TextView)view.findViewById(R.id.popuptodate);
        popupleavedetail = (TextView)view.findViewById(R.id.popupleavedetail);
        popuprejectreason = (EditText)view.findViewById(R.id.popuprejectreason);
        reject = (Button)view.findViewById(R.id.reject);
        popupthirdrowfiltercontainer = (RelativeLayout) view.findViewById(R.id.popupthirdrowfiltercontainer);

        mainview = view.findViewById(R.id.parentsdashboardnewwholecontainer);


        popuprejectreason.setFilters(new InputFilter[] { filter });

        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

        Bundle bundle = getArguments();
        String imagevalue = bundle.getString("imagevalue");
        String timesubmitted = bundle.getString("timesubmitted");
        String studentname = bundle.getString("studentname");
        String fromdate = bundle.getString("fromdate");
        String todate = bundle.getString("todate");
        String leavedetail = bundle.getString("leavedetail");
        String rejectreason = bundle.getString("rejectreason");
        final String transactionidvalue = bundle.getString("transactionid");

        leavedetail = leavedetail.replace("````",",");
        rejectreason = rejectreason.replace("````",",");

        reject.setClickable(true);

        Glide.with(getActivity().getApplicationContext()).load(imagevalue).listener(new RequestListener<String, GlideDrawable>() {
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
        popupleavedetail.setText(leavedetail);
        popuprejectreason.setText(rejectreason);

        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String popuprejectreasonvalue = popuprejectreason.getText().toString().trim();

                if (popuprejectreasonvalue.isEmpty()) {
                    snackbar = Snackbar.make(mainview,"Please Enter Reject Reason and Try Again",Snackbar.LENGTH_LONG);
                    snackbar.show();
                }else {

                    if (AppStatus.getInstance(getActivity().getBaseContext()).isOnline()) {
                        reject.setClickable(false);
                        String rejectreas = popuprejectreason.getText().toString().trim();
                        rejectpendingvalues(transactionidvalue,rejectreas);
                    }else {
                        snackbar = Snackbar.make(mainview,"Please Connect to the Internet and Try Again",Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                }


            }
        });

        return view;
    }

    private void rejectpendingvalues(String transactionid, String rejectreason) {

        deleteCache(getActivity().getApplicationContext());

        userid = MainActivity.sharedPreferences.getString(loginconfig.key_userid, "");

        final String trans = transactionid;
        final String reason = rejectreason;


        StringRequest studentrequest1 = new StringRequest(Request.Method.POST, loginconfig.key_warden_leavetrackerreject_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        JSONObject j = null;
                        try {
                            snackbar = Snackbar.make(mainview,"Outgoing Permission Rejected Successfully",Snackbar.LENGTH_SHORT);
                            snackbar.show();

                            getActivity().onBackPressed();
/*
                            Teacher_LeaveTracker_Main_Activity newFragment = new Teacher_LeaveTracker_Main_Activity().newInstance("Pending");
                            FragmentTransaction transaction = getFragmentManager().beginTransaction();
                            transaction.replace(((ViewGroup)getView().getParent()).getId(), newFragment);
                            transaction.addToBackStack(null);
                            transaction.commitAllowingStateLoss();*/

                        } catch (Exception e) {
                            snackbar = Snackbar.make(mainview,"Unable to reach Server Please Try Again",Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                snackbar = Snackbar.make(mainview,"Unable to reach Server Please Try Again",Snackbar.LENGTH_LONG);
                snackbar.show();
                volleyError.printStackTrace();
            }
        })

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.clear();

                params.put(loginconfig.key_reject_transactionid, trans);
                params.put(loginconfig.key_reject_reason, reason);
                params.put(loginconfig.key_userid, userid);

                return params;
            }
        };
        RequestQueue studentlist = Volley.newRequestQueue(getActivity().getApplicationContext());
        studentlist.add(studentrequest1);

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
