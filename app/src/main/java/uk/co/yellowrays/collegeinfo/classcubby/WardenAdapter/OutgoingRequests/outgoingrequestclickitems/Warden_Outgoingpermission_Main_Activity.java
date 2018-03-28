package uk.co.yellowrays.collegeinfo.classcubby.WardenAdapter.OutgoingRequests.outgoingrequestclickitems;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import uk.co.yellowrays.collegeinfo.classcubby.R;

/**
 * Created by Vino on 9/26/2017.
 */

public class Warden_Outgoingpermission_Main_Activity extends Fragment implements TabLayout.OnTabSelectedListener {

    View view, mainview;
    private TabLayout leavetabLayout;
    private ViewPager leaveviewPager;
    static int newinstance = 0;
    boolean alreadyExecuted = false;
    static boolean isitemclicked = false;
    static boolean value = false;
    static boolean onceexecuted = false;
    final Handler handler2 = new Handler();
    ProgressDialog loadingdialog;

    int check = 0;

    public Warden_Outgoingpermission_Main_Activity() {
        // Required empty public constructor
    }

    public static Warden_Outgoingpermission_Main_Activity newInstance(String param1) {
        newinstance = 0;
        Warden_Outgoingpermission_Main_Activity fragment = new Warden_Outgoingpermission_Main_Activity();
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
        view = inflater.inflate(R.layout.warden_leavetracker_main_xml, null);


        leaveviewPager = (ViewPager) view.findViewById(R.id.leaveviewpager);
        leavetabLayout = (TabLayout) view.findViewById(R.id.leavetabs);
        mainview = view.findViewById(R.id.teachersleavetrackercontainer);

        leavetabLayout.addTab(leavetabLayout.newTab().setText("Pending Permissions"), 0, true);
        leavetabLayout.addTab(leavetabLayout.newTab().setText("Accepted Permissions"), 1, false);
        leavetabLayout.addTab(leavetabLayout.newTab().setText("Rejected Permissions"), 2, false);
        leavetabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        leaveviewPager.removeAllViews();
        leaveviewPager.setAdapter(null);
        outgoingrequesttabpageadapter adapter = new outgoingrequesttabpageadapter(getActivity().getSupportFragmentManager(), leavetabLayout.getTabCount());
        leaveviewPager.setAdapter(adapter);
        leaveviewPager.setCurrentItem(0);
        leaveviewPager.beginFakeDrag();


        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                leavetabLayout.getTabAt(0).select();
            }
        };

        final Handler handler1 = new Handler();

        if (alreadyExecuted == false) {
            handler1.postDelayed(
                    new Runnable() {
                        @Override
                        public void run() {
                            leavetabLayout.getTabAt(0).select();
                            alreadyExecuted = true;
                            handler1.removeCallbacks(this);
                        }
                    }, 1000);
        } else {
            handler1.postDelayed(
                    new Runnable() {
                        @Override
                        public void run() {
                            leavetabLayout.getTabAt(0).select();
                            alreadyExecuted = false;
                            handler1.removeCallbacks(this);
                        }
                    }, 1000);
        }

        leavetabLayout.performClick();

        leavetabLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                leaveviewPager.getCurrentItem();
                return true;
            }
        });

        //Adding onTabSelectedListener to swipe views
        leavetabLayout.setOnTabSelectedListener(this);

        newinstance = 1;
        return view;
    }

    public static boolean getclicked(){
        return isitemclicked;
    }
    public static void setclicked(boolean value){
        isitemclicked=value;
    }

    public static boolean getitembacked(){
        return isitemclicked;
    }
    public static void setitembacked(boolean value){
        isitemclicked=value;
    }

    @Override
    public void onTabSelected(final TabLayout.Tab tab) {
        value = Warden_Outgoingpermission_Main_Activity.getclicked();
        final int position = tab.getPosition();
        if(value==true){
            Warden_Outgoingpermission_Main_Activity.setitembacked(true);
            getActivity().onBackPressed();
            loadingdialog = new ProgressDialog(getActivity());
            loadingdialog.setIndeterminate(true);
            loadingdialog.setMessage("Loading Please Wait!");
            loadingdialog.setCancelable(true);
            loadingdialog.setCanceledOnTouchOutside(true);
            loadingdialog.show();
            if (onceexecuted == false) {
                handler2.postDelayed(
                        new Runnable() {
                            @Override
                            public void run() {
                                leavetabLayout.getTabAt(position).select();
                                leaveviewPager.getAdapter().notifyDataSetChanged();
                                leaveviewPager.setCurrentItem(position);
                                handler2.removeCallbacks(this);
                                loadingdialog.dismiss();
                            }
                        }, 2500);
            }


        }else {
            leaveviewPager.getAdapter().notifyDataSetChanged();
            leaveviewPager.setCurrentItem(tab.getPosition());
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        leaveviewPager.getAdapter().notifyDataSetChanged();
        leaveviewPager.setCurrentItem(tab.getPosition());
    }


}
