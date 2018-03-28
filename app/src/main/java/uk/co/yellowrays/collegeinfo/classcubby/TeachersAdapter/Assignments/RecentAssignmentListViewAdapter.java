package uk.co.yellowrays.collegeinfo.classcubby.TeachersAdapter.Assignments;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import uk.co.yellowrays.collegeinfo.classcubby.MainActivity;
import uk.co.yellowrays.collegeinfo.classcubby.R;
import uk.co.yellowrays.collegeinfo.classcubby.configfiles.loginconfig;

/**
 * Created by Vino on 10/5/2017.
 */

public class RecentAssignmentListViewAdapter extends BaseAdapter {

    Context context;
    ViewHolder holder;
    GridView assignmentgridview;
    Button submitbutton;
    TextView assignmentpagename;
    FloatingActionButton filter;
    RelativeLayout attendancepagemaincontainer,innerfilterlayout;
    LayoutInflater inflater = null;
    ArrayList<String> datelist,assignmentidlist,assignmenttitlelist,assignmentdesclist,assignmenttargetlist;
    ArrayList<String> assignmentsubjectlist,assignmentattachmentlist,departmentidlist,assignmentfilenamelist,departmentnamelist,sectionidlist,sectionnamelist;

    private int lastPosition = -1 ;
    private int randomAndroidColor ;
    private String initialstring;
    private SpannableString spannableString;
    static boolean isitemclicked,filterisOpen,isgenerateclicked,newgenerateclicled;
    String classname,classsection,hournamevalue,datevalue,targetdatevalue,pathvalue;
    static String attendancetypevalue,classnamevalue,classsectionvalue,classhourvalue,classdate,targetclassdate,pathassignmentvalue,assignmenttitle,assignmentdescription;

    public static final String MY_PREFERENCES = "loginconfig.shared_pref_name";
    public static final String Previous_PREFERENCES = "previousloginconfig.shared_pref_name";
    public static SharedPreferences sharedPreferences,previoussharedPreferences;
    public static SharedPreferences.Editor editor,previouseditor;
    public static Activity myActivity;
    private FragmentActivity myContext;

    TextView vkdepartmentname,vkfilename,vkhourname,vktakendate,vktargetdate,vkassignmenttitle,vkassignmentdesc,vkpathname,vkdectexthidden;



    public RecentAssignmentListViewAdapter(Activity context, ArrayList<String> datelist,
                                           ArrayList<String> assignmentidlist, ArrayList<String> assignmenttitlelist,
                                           ArrayList<String> assignmentdesclist,
                                           ArrayList<String> assignmenttargetlist, ArrayList<String> assignmentsubjectlist,
                                           ArrayList<String> assignmentattachmentlist, ArrayList<String> departmentidlist,
                                           ArrayList<String> departmentnamelist, ArrayList<String> sectionidlist,
                                           ArrayList<String> sectionnamelist,GridView assignmentgridview,
                                           Button submit,RelativeLayout attendancepagemaincontainer,
                                           RelativeLayout innerfilterlayout,FloatingActionButton filter,TextView assignmentpagename,
                                           ArrayList<String> assignmentfilenamelist) {
        this.context = context;
        this.datelist = datelist;
        this.assignmentidlist = assignmentidlist;
        this.assignmenttitlelist = assignmenttitlelist;
        this.assignmentdesclist = assignmentdesclist;
        this.assignmenttargetlist = assignmenttargetlist;
        this.assignmentsubjectlist = assignmentsubjectlist;
        this.assignmentattachmentlist = assignmentattachmentlist;
        this.departmentidlist = departmentidlist;
        this.departmentnamelist = departmentnamelist;
        this.assignmentfilenamelist = assignmentfilenamelist;
        this.sectionidlist = sectionidlist;
        this.sectionnamelist = sectionnamelist;
        this.assignmentgridview = assignmentgridview;
        this.attendancepagemaincontainer = attendancepagemaincontainer;
        this.submitbutton = submit;
        this.innerfilterlayout = innerfilterlayout;
        this.filter = filter;
        this.myContext = (FragmentActivity)context;
        this.assignmentpagename = assignmentpagename;
        this.newgenerateclicled = false;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return datelist.size();
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
    public static void setclicked(boolean value){
        isitemclicked=value;
    }

    public static boolean getgenerateclick(){
        return isgenerateclicked;
    }
    public static void setgenerateclicked(boolean value){
        isgenerateclicked=value;
    }

    public static boolean getisfileropen(){
        return filterisOpen;
    }
    public static void setFilterisOpen(boolean value){
        filterisOpen=value;
    }

    public static void setNewgenerateclicled(boolean value){
        newgenerateclicled=value;
    }
    public static boolean newgenerateclicked(){
        return newgenerateclicled;
    }

    static class ViewHolder {
        TextView dectexthidden,departmentname,hourname,takendate,targetdate,filename,assignmenttitle,assignmentdesc,pathname,assignmentdescname,assignmenttitlename,targetdatename;
        RelativeLayout recentassignmentsoverallcontainer,colorindicatorelayout;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            row = inflater.inflate(R.layout.teacher_recent_gridview_assignments_xml, null);
            holder = new ViewHolder();
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        holder.recentassignmentsoverallcontainer = (RelativeLayout) row.findViewById(R.id.recentassignmentsoverallcontainer);
        holder.colorindicatorelayout = (RelativeLayout) row.findViewById(R.id.colorindicatorelayout);
        holder.departmentname = (TextView) row.findViewById(R.id.departmentname);
        holder.dectexthidden = (TextView) row.findViewById(R.id.dectexthidden);
        holder.hourname = (TextView) row.findViewById(R.id.hourname);
        holder.takendate = (TextView) row.findViewById(R.id.takendate);
        holder.targetdate = (TextView) row.findViewById(R.id.targetdate);
        holder.assignmenttitle = (TextView) row.findViewById(R.id.assignmenttitle);
        holder.filename = (TextView) row.findViewById(R.id.filename);
        holder.assignmentdesc = (TextView) row.findViewById(R.id.assignmentdesc);
        holder.pathname = (TextView) row.findViewById(R.id.pathname);

        holder.targetdatename = (TextView) row.findViewById(R.id.targetdatename);
        holder.assignmenttitlename = (TextView) row.findViewById(R.id.assignmenttitlename);
        holder.assignmentdescname = (TextView) row.findViewById(R.id.assignmenttitlename);

        Typeface normaltypeface = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSansRegular.ttf");
        Typeface semiboldtypeface = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSansSemibold.ttf");
        Typeface boldtypeface = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSansBold.ttf");
        holder.departmentname.setTypeface(boldtypeface);
        holder.hourname.setTypeface(normaltypeface);
        holder.takendate.setTypeface(normaltypeface);
        holder.targetdate.setTypeface(normaltypeface);
        holder.assignmenttitle.setTypeface(normaltypeface);
        holder.assignmentdesc.setTypeface(normaltypeface);
        holder.targetdatename.setTypeface(semiboldtypeface);
        holder.assignmenttitlename.setTypeface(semiboldtypeface);
        holder.assignmentdescname.setTypeface(semiboldtypeface);
        holder.dectexthidden.setTypeface(semiboldtypeface);



        if(position==0||(position%4)==0){
            randomAndroidColor = context.getResources().getColor(R.color.present);
        }else if((position%4)==1){
            randomAndroidColor = context.getResources().getColor(R.color.absent);
        }else if((position%4)==2){
            randomAndroidColor = context.getResources().getColor(R.color.excusedabsent);
        }else if((position%4)==3){
            randomAndroidColor = context.getResources().getColor(R.color.blue);
        }


        holder.departmentname.setText(departmentnamelist.get(position));
        holder.departmentname.setTextColor(randomAndroidColor);
        //holder.colorindicatorelayout.setBackgroundColor(context.getResources().getColor(R.color.blue));

        holder.hourname.setText(assignmentsubjectlist.get(position));
        holder.takendate.setText(datelist.get(position));
        holder.targetdate.setText(assignmenttargetlist.get(position));
        holder.pathname.setText(assignmentattachmentlist.get(position));
        holder.filename.setText(assignmentfilenamelist.get(position));

        if(assignmenttitlelist.get(position).length()>12){
            String firststring = assignmenttitlelist.get(position).substring(0, 12);
            initialstring = firststring.concat(" ...");

            spannableString = new SpannableString(initialstring);

            holder.assignmenttitle.setText(spannableString);
        }else {
            holder.assignmenttitle.setText(assignmenttitlelist.get(position));
        }

        if(assignmentdesclist.get(position).length()>25){
            holder.dectexthidden.setText(assignmentdesclist.get(position));
            String firststring = assignmentdesclist.get(position).substring(0, 25);
            initialstring = firststring.concat(" ...");

            spannableString = new SpannableString(initialstring);

            holder.assignmentdesc.setText(spannableString);
        }else {
            holder.dectexthidden.setText(assignmentdesclist.get(position));
            holder.assignmentdesc.setText(assignmentdesclist.get(position));
        }


        final View finalRow = row;
        holder.recentassignmentsoverallcontainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(filterisOpen==true){
                    assignmentgridview.setEnabled(false);
                }else {
                    sharedPreferences = context.getSharedPreferences(MY_PREFERENCES, MainActivity.MODE_PRIVATE);
                    filterisOpen = sharedPreferences.getBoolean(loginconfig.key_attendancefilteropen, true);

                    if (filterisOpen == true) {
                        assignmentgridview.setEnabled(true);

                        if (isitemclicked == true) {
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
                    vktargetdate = (TextView) v.findViewById(R.id.targetdate);
                    vkpathname = (TextView) v.findViewById(R.id.pathname);
                    vkassignmenttitle = (TextView) v.findViewById(R.id.assignmenttitle);
                    vkassignmentdesc = (TextView) v.findViewById(R.id.assignmentdesc);
                    vkdectexthidden = (TextView) v.findViewById(R.id.dectexthidden);
                    vkfilename = (TextView) v.findViewById(R.id.filename);

                    isitemclicked = true;

                    assignmentpagename.setText("Assignment for Class" + vkdepartmentname.getText().toString().trim() );

                    String[] parts = vkdepartmentname.getText().toString().trim().split(" - ");
                    classname = parts[0]; // 004
                    classnamevalue = classname;

                    classsection = parts[1];
                    classsectionvalue = classsection;

                    hournamevalue = vkhourname.getText().toString().trim();
                    classhourvalue = hournamevalue;

                    datevalue = vktakendate.getText().toString().trim();
                    classdate = datevalue;

                    targetdatevalue = vktargetdate.getText().toString().trim();
                    targetclassdate = targetdatevalue;

                    pathvalue = vkpathname.getText().toString().trim();
                    pathassignmentvalue = pathvalue;

                    assignmenttitle = vkassignmenttitle.getText().toString().trim();
                    assignmentdescription = vkdectexthidden.getText().toString().trim();

                    filter.setVisibility(View.INVISIBLE);

                    Teacher_Assignments_New_Activity innerFragment = new Teacher_Assignments_New_Activity ().newInstance("Teacher Assignments");
                    Bundle bundle = new Bundle();
                    FragmentManager fragmentManager = myContext.getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    bundle.putString("classname",classnamevalue);
                    bundle.putString("sectionname",classsectionvalue);
                    bundle.putString("hourname",classhourvalue);
                    bundle.putString("assignmentdate",classdate);
                    bundle.putString("assignmenttargetdate",targetclassdate);
                    bundle.putString("serverpathvalue",pathassignmentvalue);
                    bundle.putString("assignmenttitle",assignmenttitle);
                    bundle.putString("assignmentdescription",assignmentdescription);
                    bundle.putString("assignmentfilename",vkfilename.getText().toString().trim());
                    bundle.putString("assignmenttypevalue","1");
                    bundle.putBoolean("baseitemclicked",isitemclicked);
                    innerFragment.setArguments(bundle);
                    fragmentTransaction.add(R.id.attendancepagemaincontainer, innerFragment);
                    fragmentTransaction.addToBackStack("new");
                    fragmentTransaction.commit();

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
}
