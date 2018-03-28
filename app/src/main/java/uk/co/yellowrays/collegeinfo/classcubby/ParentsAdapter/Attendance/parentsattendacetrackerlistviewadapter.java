package uk.co.yellowrays.collegeinfo.classcubby.ParentsAdapter.Attendance;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import uk.co.yellowrays.collegeinfo.classcubby.R;

/**
 * Created by user1 on 19-11-2016.
 */

public class parentsattendacetrackerlistviewadapter extends BaseAdapter {

    String[] hournamevalue,attendancevalue ,commentsvalue;
    Context context;
    private static LayoutInflater inflater = null;

    public parentsattendacetrackerlistviewadapter(Activity mainActivity, String[] strhourname, String[] strattendancevalue,
                                                  String[] strcomments) {

        // TODO Auto-generated constructor stub
        this.hournamevalue = strhourname;
        this.attendancevalue = strattendancevalue;
        this.commentsvalue =strcomments;

        this.context = mainActivity;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return hournamevalue.length;
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
        TextView hourname,attendancevalue,comments;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder;

        View rowView = convertView;
        Log.d("posiiton ImageAdapater:", "" + position);
        if (rowView == null) {
            rowView = inflater.inflate(R.layout.parents_attendancetracker_rowitems, null);
            holder = new Holder();
            rowView.setTag(holder);
        } else {
            holder = (Holder) rowView.getTag();
        }

        holder.hourname = (TextView) rowView.findViewById(R.id.hourname);
        holder.attendancevalue = (TextView) rowView.findViewById(R.id.attendancevalue);

        Typeface normaltypeface = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSansRegular.ttf");
        Typeface semiboldtypeface = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSansSemibold.ttf");
        Typeface boldtypeface = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSansBold.ttf");
        holder.hourname.setTypeface(semiboldtypeface);
        holder.attendancevalue.setTypeface(semiboldtypeface);


        holder.hourname.setText(hournamevalue[position]);
        holder.attendancevalue.setText(attendancevalue[position]);

        if(attendancevalue[position].equals("Not Taken")){
            holder.attendancevalue.setTextColor(context.getResources().getColor(R.color.colorAccent));
        }else if(attendancevalue[position].equals("Present")){
            holder.attendancevalue.setTextColor(context.getResources().getColor(R.color.present));
        }else if(attendancevalue[position].equals("Absent")){
            holder.attendancevalue.setTextColor(context.getResources().getColor(R.color.absent));
        }else if(attendancevalue[position].equals("Excused Absent")){
            holder.attendancevalue.setTextColor(context.getResources().getColor(R.color.excusedabsent));
        }else if(attendancevalue[position].equals("On-Duty")){
            holder.attendancevalue.setTextColor(context.getResources().getColor(R.color.onduty));
        }

        return rowView;
    }

}
