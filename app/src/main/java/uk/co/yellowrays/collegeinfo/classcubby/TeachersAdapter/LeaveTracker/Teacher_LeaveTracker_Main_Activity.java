package uk.co.yellowrays.collegeinfo.classcubby.TeachersAdapter.LeaveTracker;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import uk.co.yellowrays.collegeinfo.classcubby.R;

/**
 * Created by Vino on 9/26/2017.
 */

public class Teacher_LeaveTracker_Main_Activity extends android.support.v4.app.Fragment implements TabLayout.OnTabSelectedListener {

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

    public Teacher_LeaveTracker_Main_Activity() {
        // Required empty public constructor
    }

    public static Teacher_LeaveTracker_Main_Activity newInstance(String param1) {
        newinstance = 0;
        Teacher_LeaveTracker_Main_Activity fragment = new Teacher_LeaveTracker_Main_Activity();
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
        view = inflater.inflate(R.layout.teacher_leavetracker_main_xml, null);


        leaveviewPager = (ViewPager) view.findViewById(R.id.leaveviewpager);
        leavetabLayout = (TabLayout) view.findViewById(R.id.leavetabs);
        mainview = view.findViewById(R.id.teachersleavetrackercontainer);

        leavetabLayout.addTab(leavetabLayout.newTab().setText("Pending Leaves"), 0, true);
        leavetabLayout.addTab(leavetabLayout.newTab().setText("Accepted Leaves"), 1, false);
        leavetabLayout.addTab(leavetabLayout.newTab().setText("Rejected Leaves"), 2, false);
        leavetabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        leaveviewPager.removeAllViews();
        leaveviewPager.setAdapter(null);
        teacherleavetabpageadapter adapter = new teacherleavetabpageadapter(getActivity().getSupportFragmentManager(), leavetabLayout.getTabCount());
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

        //Adding adapter to pager


/*
        leaveviewPager.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                leaveviewPager.getCurrentItem();
                return true;
            }
        });


        leaveviewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                leaveviewPager.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        setupTabIcons();

        leavetabLayout.setOnTabSelectedListener(this);*/


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

/*
    private void setupTabIcons() {

            TextView tabOne = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.leavetabcustomtext, null);
            tabOne.setText("Pending Leaves");
            tabOne.setTextColor(getResources().getColor(R.color.textcolor));
            tabOne.setTextSize(12);
            leavetabLayout.getTabAt(0).setCustomView(tabOne);

            TextView tabtwo = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.leavetabcustomtext, null);
            tabOne.setText("Accepted Leaves");
            tabtwo.setTextColor(getResources().getColor(R.color.lightdimtextcolor));
            tabtwo.setTextSize(10);
            leavetabLayout.getTabAt(1).setCustomView(tabtwo);


            TextView tabthree = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.leavetabcustomtext, null);
            tabOne.setText("Rejected Leaves");
            tabthree.setTextColor(getResources().getColor(R.color.lightdimtextcolor));
            tabthree.setTextSize(10);
            leavetabLayout.getTabAt(2).setCustomView(tabthree);
    }


    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        int position = tab.getPosition();

        check = position;

        leavetabLayout.getTabAt(position).setCustomView(null);

        leavetabLayout.setTabTextColors(getResources().getColor(R.color.lightdimtextcolor),getResources().getColor(R.color.textcolor));

        leaveviewPager.setCurrentItem(tab.getPosition());

        if(position==0){
            TextView tabOne = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.leavetabcustomtext, null);
            tabOne.setText("Pending Leaves");
            tabOne.setTextColor(getResources().getColor(R.color.textcolor));
            tabOne.setTextSize(12);
            leavetabLayout.getTabAt(position).setCustomView(tabOne);
        }else if(position==1){
            TextView tabOne = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.leavetabcustomtext, null);
            tabOne.setText("Accepted Leaves");
            tabOne.setTextColor(getResources().getColor(R.color.textcolor));
            tabOne.setTextSize(12);
            leavetabLayout.getTabAt(position).setCustomView(tabOne);
        }else if(position==2){
            TextView tabOne = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.leavetabcustomtext, null);
            tabOne.setText("Rejected Leaves");
            tabOne.setTextColor(getResources().getColor(R.color.textcolor));
            tabOne.setTextSize(12);
            leavetabLayout.getTabAt(position).setCustomView(tabOne);
        }

    }


    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        int position = tab.getPosition();
        leavetabLayout.getTabAt(position).setCustomView(null);

        if(position==0){
            TextView tabOne = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.leavetabcustomtext, null);
            tabOne.setText("Pending Leaves");
            tabOne.setTextColor(getResources().getColor(R.color.lightdimtextcolor));
            tabOne.setTextSize(10);
            leavetabLayout.getTabAt(position).setCustomView(tabOne);
        }

        if(position==1){
            TextView tabOne = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.leavetabcustomtext, null);
            tabOne.setText("Accepted Leaves");
            tabOne.setTextColor(getResources().getColor(R.color.lightdimtextcolor));
            tabOne.setTextSize(10);
            leavetabLayout.getTabAt(position).setCustomView(tabOne);
        }

        if(position==2){
            TextView tabOne = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.leavetabcustomtext, null);
            tabOne.setText("Rejected Leaves");
            tabOne.setTextColor(getResources().getColor(R.color.lightdimtextcolor));
            tabOne.setTextSize(10);
            leavetabLayout.getTabAt(position).setCustomView(tabOne);
        }

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }*/

    @Override
    public void onTabSelected(final TabLayout.Tab tab) {
        value = Teacher_LeaveTracker_Main_Activity.getclicked();
        final int position = tab.getPosition();
        if(value==true){
            Teacher_LeaveTracker_Main_Activity.setitembacked(true);
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
