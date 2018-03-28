package uk.co.yellowrays.collegeinfo.classcubby.ParentsAdapter.Outgoingpermission;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import uk.co.yellowrays.collegeinfo.classcubby.R;

/**
 * Created by user1 on 19-11-2016.
 */

public class parentsoutgoingtrackerlistviewadapter extends BaseAdapter {

    String[] studentid,studentname ,classname,classid,schoolid,schoolname,leaveorderdate,fromdate,todate,fromhour,tohour;
    String[] totaldays,leavereason,rejectedreason,leavesubject,leavestatus,leaverequesteddate;
    Context context;
    String dateString,leavevaluefromdate,leavevaluetodate,fromhourvalue,tohourvalue;
    int textsize;
    private static LayoutInflater inflater = null;

    public parentsoutgoingtrackerlistviewadapter(Activity mainActivity, String[] strschoolid, String[] strschoolname,
                                                 String[] strclassid, String[] strclassname, String[] strstudentid, String[] strstudentname,
                                                 String[] strleaveorderdate, String[] strfromdate, String[] strtodate,
                                                 String[] strfromhour, String[] strtohour, String[] strtotaldays,
                                                 String[] strleavereason, String[] strrejectedreason,
                                                 String[] strleavesubject, String[] strleavestatus, String[] strleaverejecteddate) {

        // TODO Auto-generated constructor stub
        this.schoolid = strschoolid;
        this.schoolname = strschoolname;
        this.classid =strclassid;
        this.classname = strclassname;
        this.studentid = strstudentid;
        this.studentname = strstudentname;
        this.leaveorderdate = strleaveorderdate;
        this.fromdate = strfromdate;
        this.todate = strtodate;
        this.fromhour = strfromhour;
        this.tohour = strtohour;
        this.totaldays = strtotaldays;
        this.leavereason = strleavereason;
        this.rejectedreason = strrejectedreason;
        this.leavesubject = strleavesubject;
        this.leavestatus = strleavestatus;
        this.leaverequesteddate = strleaverejecteddate;

        this.context = mainActivity;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return schoolid.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder {
        TextView changeddate,leavesubject,leavedayscount,leavedate,status;
        RelativeLayout innerrelview;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder;

        View rowView = convertView;
        Log.d("posiiton ImageAdapater:", "" + position);
        if (rowView == null) {
            rowView = inflater.inflate(R.layout.students_outgoingpermission_list_rowcontent_xml, null);
            holder = new Holder();
            rowView.setTag(holder);
        } else {
            holder = (Holder) rowView.getTag();
        }

        holder.changeddate = (TextView) rowView.findViewById(R.id.changeddate);
        holder.leavesubject = (TextView) rowView.findViewById(R.id.leavesubject);
        holder.leavedate = (TextView) rowView.findViewById(R.id.leavedate);
        holder.status = (TextView) rowView.findViewById(R.id.status);
        holder.innerrelview = (RelativeLayout)rowView.findViewById(R.id.innerrelview);

        Typeface normaltypeface = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSansRegular.ttf");
        Typeface boldtypeface = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSansBold.ttf");
        holder.changeddate.setTypeface(boldtypeface);
        holder.leavesubject.setTypeface(boldtypeface);
        holder.leavedate.setTypeface(normaltypeface);
        holder.status.setTypeface(boldtypeface);


        DateFormat formatter ;
        Date date ;

        formatter = new SimpleDateFormat("dd-MM-yyyy");
        try {
            date = (Date)formatter.parse(leaveorderdate[position]);
            SimpleDateFormat sdf = new SimpleDateFormat("\b dd\b\nMMM");
            dateString = sdf.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.changeddate.setText(dateString);


        String str = leavesubject[position];
        if (str.length() > 25)
            str = str.substring(0, 25) + "...";

        str = str.replace("`",",");
        holder.leavesubject.setText(str);

        //holder.leavedayscount.setText(totaldays[position] + " day(s)");


        try{
            date=(Date)formatter.parse(fromdate[position]);
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM");
            leavevaluefromdate = sdf.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try{
            date=(Date)formatter.parse(todate[position]);
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM");
            leavevaluetodate = sdf.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        if(fromhour[position].equals("Forenoon")){
            fromhourvalue = fromhour[position];
        }else{
            fromhourvalue = fromhour[position];
        }

        if(tohour[position].equals("Forenoon")){
            tohourvalue = tohour[position];
        }else{
            tohourvalue = tohour[position];
        }

        holder.leavedate.setText(leavevaluefromdate +" "+ fromhourvalue +" to "+leavevaluetodate +" "+ tohourvalue);

        holder.status.setText(leavestatus[position]);

        GradientDrawable bgShape = (GradientDrawable)holder.changeddate.getBackground();
        //bgShape.setColor(context.getResources().getColor(R.color.grey));

        if(leavestatus[position].equals("Accepted")){
            holder.status.setBackgroundColor(context.getResources().getColor(R.color.present));
            bgShape.setColor(context.getResources().getColor(R.color.present));
        }else if(leavestatus[position].equals("Rejected")){
            holder.status.setBackgroundColor(context.getResources().getColor(R.color.excusedabsent));
            bgShape.setColor(context.getResources().getColor(R.color.excusedabsent));
        }else{
            holder.status.setBackgroundColor(context.getResources().getColor(R.color.lightblack));
            bgShape.setColor(context.getResources().getColor(R.color.lightblack));
        }

        return rowView;
    }

}
