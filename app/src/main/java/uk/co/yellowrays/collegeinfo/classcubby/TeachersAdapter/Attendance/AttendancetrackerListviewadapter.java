package uk.co.yellowrays.collegeinfo.classcubby.TeachersAdapter.Attendance;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import uk.co.yellowrays.collegeinfo.classcubby.R;


import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by user1 on 16-11-2016.
 */

public class AttendancetrackerListviewadapter extends BaseAdapter {

    ArrayList<String> idlist,dataList,imageList,rollnolist,values;
    static ArrayList<String> attendancelistvalue;
    Context context;
    LayoutInflater inflater = null;
    String[] testval;
    ViewHolder holder;
    int lva;
    int[] attendval;
    ImageView vk;
    CheckBox checkedbox;
    private int lastPosition = -1 ;
    private List<AttendanceList> studentlist;
    private static List<AttendanceList> originalstudentlist;
    private ArrayList<AttendanceList> arraylist = null;
    int imagepresent,imageexcusedabsent,imageonduty,imagesuspended,imageabsent;

    public AttendancetrackerListviewadapter(Context context, ArrayList<String> studentidlist,
                                            ArrayList<String> namelist, ArrayList<String> ImageList, ArrayList<String> rollnumberlist,
                                            ArrayList<String> attendancevaluelist, int[] attendancevalues,List<AttendanceList> patientlist,List<AttendanceList> patientoriginallist) {
        this.context = context;
        this.idlist = studentidlist;
        this.dataList = namelist;
        this.imageList = ImageList;
        this.rollnolist = rollnumberlist;
        this.values = attendancevaluelist;
        this.attendancelistvalue = attendancevaluelist;
        this.attendval = attendancevalues;
        this.arraylist = new ArrayList<AttendanceList>();
        this.arraylist.addAll(patientlist);
        this.studentlist = patientlist;
        this.originalstudentlist = patientoriginallist;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        testval = new String[dataList.size()];
    }

    @Override
    public int getCount() {
        return studentlist.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public static List<AttendanceList> getAttendancelistvalue(){
        return originalstudentlist;
    }

    public ArrayList<String> getattendanceValues(){
        return values;
    }
    static class ViewHolder {
        TextView name,attendvalue;
        ImageView atttendanceimagevalue;
        ImageView usericon;
        CheckBox leavecommunicated;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        Log.d("posiiton ImageAdapater:", "" + position);
        if (row == null) {
            row = inflater.inflate(R.layout.teachers_attendancetracker_gridview_rowcontent_xml, null);
            holder = new ViewHolder();
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        imagepresent = attendval[0];
        imageexcusedabsent = attendval[1];
        imageabsent = attendval[2];
        imageonduty = attendval[3];


        holder.name = (TextView) row.findViewById(R.id.studentname);
        holder.usericon = (ImageView) row.findViewById(R.id.imageView1);
        holder.attendvalue = (TextView) row.findViewById(R.id.studentattendance);
        holder.atttendanceimagevalue = (ImageView) row.findViewById(R.id.attendancevalueimage);
        holder.leavecommunicated = (CheckBox) row.findViewById(R.id.leavecommunicated);

        Typeface normaltypeface = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSansRegular.ttf");
        Typeface boldtypeface = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSansBold.ttf");
        holder.attendvalue.setTypeface(normaltypeface);
        holder.name.setTypeface(boldtypeface);
        holder.leavecommunicated.setTypeface(normaltypeface);

        holder.name.setText(studentlist.get(position).getstudentname());


        Glide.with(context).load(studentlist.get(position).getstudentimage()).listener(new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String s, Target<GlideDrawable> target, boolean b) {
                Log.i("GLIDE", "onException :", e);
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable glideDrawable, String s, Target<GlideDrawable> target, boolean b, boolean b1) {
                Log.i("GLIDE", "onResourceReady");
                holder.usericon.setVisibility(View.VISIBLE);
                return false;
            }
        }).into(holder.usericon);

        String attendancevalue = studentlist.get(position).getattendancevalue();

        if (attendancevalue == null) {
            holder.atttendanceimagevalue.setImageResource(imagepresent);
            holder.attendvalue.setText("Present");
            holder.atttendanceimagevalue.setTag("Present");
            testval[position] = "Present";
            values.set(position, "Present");
            attendancelistvalue.set(position, "Present");
            studentlist.get(position).setattendancevalue("Present");

            int length = originalstudentlist.size();
            for(int i=0; i<length;i++){
                String id = originalstudentlist.get(i).getstudentid();
                if(id==studentlist.get(position).getstudentid()){
                    originalstudentlist.get(i).setattendancevalue(studentlist.get(position).getattendancevalue());
                    break;
                }
            }


            holder.leavecommunicated.setChecked(false);
            holder.attendvalue.setTextColor(context.getResources().getColor(R.color.present));
        } else {
            if(attendancevalue.equals("Present"))
            {
                holder.atttendanceimagevalue.setImageResource(Integer.valueOf(imagepresent));
                holder.attendvalue.setText("Present");
                holder.atttendanceimagevalue.setTag("Present");
                testval[position] = "Present";
                values.set(position, "Present");
                attendancelistvalue.set(position, "Present");
                studentlist.get(position).setattendancevalue("Present");

                int length = originalstudentlist.size();
                for(int i=0; i<length;i++){
                    String id = originalstudentlist.get(i).getstudentid();
                    if(id==studentlist.get(position).getstudentid()){
                        originalstudentlist.get(i).setattendancevalue(studentlist.get(position).getattendancevalue());
                        break;
                    }
                }

                holder.leavecommunicated.setChecked(false);
                holder.attendvalue.setTextColor(context.getResources().getColor(R.color.present));

            }else if(attendancevalue.equals("Absent"))
            {
                holder.atttendanceimagevalue.setImageResource(Integer.valueOf(imageabsent));
                holder.attendvalue.setText("Absent");
                holder.atttendanceimagevalue.setTag("Absent");
                testval[position] = "Absent";
                values.set(position, "Absent");
                attendancelistvalue.set(position, "Absent");
                studentlist.get(position).setattendancevalue("Absent");

                int length = originalstudentlist.size();
                for(int i=0; i<length;i++){
                    String id = originalstudentlist.get(i).getstudentid();
                    if(id==studentlist.get(position).getstudentid()){
                        originalstudentlist.get(i).setattendancevalue(studentlist.get(position).getattendancevalue());
                        break;
                    }
                }

                holder.leavecommunicated.setChecked(false);
                holder.attendvalue.setTextColor(context.getResources().getColor(R.color.absent));
            }else if(attendancevalue.equals("Excused Absent"))
            {
                holder.atttendanceimagevalue.setImageResource(Integer.valueOf(imageexcusedabsent));
                holder.attendvalue.setText("Excused Absent");
                holder.atttendanceimagevalue.setTag("Excused Absent");
                testval[position] = "Excused Absent";
                values.set(position, "Excused Absent");
                attendancelistvalue.set(position, "Excused Absent");
                studentlist.get(position).setattendancevalue("Excused Absent");

                int length = originalstudentlist.size();
                for(int i=0; i<length;i++){
                    String id = originalstudentlist.get(i).getstudentid();
                    if(id==studentlist.get(position).getstudentid()){
                        originalstudentlist.get(i).setattendancevalue(studentlist.get(position).getattendancevalue());
                        break;
                    }
                }

                holder.leavecommunicated.setChecked(true);
                holder.attendvalue.setTextColor(context.getResources().getColor(R.color.excusedabsent));
            }else if(attendancevalue.equals("On-Duty"))
            {
                holder.atttendanceimagevalue.setImageResource(Integer.valueOf(imageonduty));
                holder.attendvalue.setText("On-Duty");
                holder.atttendanceimagevalue.setTag("On-Duty");
                testval[position] = "On-Duty";
                values.set(position, "On-Duty");
                attendancelistvalue.set(position, "On-Duty");
                studentlist.get(position).setattendancevalue("On-Duty");

                int length = originalstudentlist.size();
                for(int i=0; i<length;i++){
                    String id = originalstudentlist.get(i).getstudentid();
                    if(id==studentlist.get(position).getstudentid()){
                        originalstudentlist.get(i).setattendancevalue(studentlist.get(position).getattendancevalue());
                        break;
                    }
                }

                holder.leavecommunicated.setChecked(false);
                holder.attendvalue.setTextColor(context.getResources().getColor(R.color.onduty));
            }else {
                holder.atttendanceimagevalue.setImageResource(imagepresent);
                holder.attendvalue.setText("Present");
                holder.atttendanceimagevalue.setTag("Present");
                testval[position] = "Present";
                values.set(position, "Present");
                attendancelistvalue.set(position, "Present");
                studentlist.get(position).setattendancevalue("Present");

                int length = originalstudentlist.size();
                for(int i=0; i<length;i++){
                    String id = originalstudentlist.get(i).getstudentid();
                    if(id==studentlist.get(position).getstudentid()){
                        originalstudentlist.get(i).setattendancevalue(studentlist.get(position).getattendancevalue());
                        break;
                    }
                }

                holder.leavecommunicated.setChecked(false);
                holder.attendvalue.setTextColor(context.getResources().getColor(R.color.present));
            }
        }

        final View finalRow = row;
        holder.atttendanceimagevalue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean filtervalueopen = RecentAttendanceListViewAdapter.getisfileropen();

                if(filtervalueopen==true){

                }else {
                    holder = (ViewHolder) finalRow.getTag();
                    vk = (ImageView) v;

                    String tagvalue = (String) vk.getTag();

                    if(tagvalue.equals("Present")){
                        vk.setImageResource(Integer.valueOf(imageabsent));
                        holder.attendvalue.setText("Absent");
                        vk.setTag("Absent");
                        values.set(position, "Absent");
                        attendancelistvalue.set(position, "Absent");
                        testval[position] = "Absent";
                        studentlist.get(position).setattendancevalue("Absent");

                        int length = originalstudentlist.size();
                        for(int i=0; i<length;i++){
                            String id = originalstudentlist.get(i).getstudentid();
                            if(id==studentlist.get(position).getstudentid()){
                                originalstudentlist.get(i).setattendancevalue(studentlist.get(position).getattendancevalue());
                                break;
                            }
                        }

                        holder.leavecommunicated.setChecked(false);
                        holder.attendvalue.setTextColor(context.getResources().getColor(R.color.absent));
                    }else if(tagvalue.equals("Absent")){
                        vk.setImageResource(Integer.valueOf(imageexcusedabsent));
                        holder.attendvalue.setText("Excused Absent");
                        vk.setTag("Excused Absent");
                        values.set(position, "Excused Absent");
                        attendancelistvalue.set(position, "Excused Absent");
                        testval[position] = "Excused Absent";
                        studentlist.get(position).setattendancevalue("Excused Absent");

                        int length = originalstudentlist.size();
                        for(int i=0; i<length;i++){
                            String id = originalstudentlist.get(i).getstudentid();
                            if(id==studentlist.get(position).getstudentid()){
                                originalstudentlist.get(i).setattendancevalue(studentlist.get(position).getattendancevalue());
                                break;
                            }
                        }

                        holder.leavecommunicated.setChecked(true);
                        holder.attendvalue.setTextColor(context.getResources().getColor(R.color.excusedabsent));
                    }else if(tagvalue.equals("Excused Absent")){
                        vk.setImageResource(Integer.valueOf(imageonduty));
                        holder.attendvalue.setText("On-Duty");
                        vk.setTag("On-Duty");
                        values.set(position, "On-Duty");
                        attendancelistvalue.set(position, "On-Duty");
                        testval[position] = "On-Duty";
                        studentlist.get(position).setattendancevalue("On-Duty");

                        int length = originalstudentlist.size();
                        for(int i=0; i<length;i++){
                            String id = originalstudentlist.get(i).getstudentid();
                            if(id==studentlist.get(position).getstudentid()){
                                originalstudentlist.get(i).setattendancevalue(studentlist.get(position).getattendancevalue());
                                break;
                            }
                        }

                        holder.leavecommunicated.setChecked(false);
                        holder.attendvalue.setTextColor(context.getResources().getColor(R.color.onduty));
                    }else if(tagvalue.equals("On-Duty")){
                        vk.setImageResource(Integer.valueOf(imagepresent));
                        holder.attendvalue.setText("Present");
                        vk.setTag("Present");
                        values.set(position, "Present");
                        attendancelistvalue.set(position, "Present");
                        testval[position] = "Present";
                        studentlist.get(position).setattendancevalue("Present");

                        int length = originalstudentlist.size();
                        for(int i=0; i<length;i++){
                            String id = originalstudentlist.get(i).getstudentid();
                            if(id==studentlist.get(position).getstudentid()){
                                originalstudentlist.get(i).setattendancevalue(studentlist.get(position).getattendancevalue());
                                break;
                            }
                        }

                        holder.leavecommunicated.setChecked(false);
                        holder.attendvalue.setTextColor(context.getResources().getColor(R.color.present));
                    }

                }

            }
        });


        final View checkedrow = row;
        holder.leavecommunicated.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            boolean filtervalueopen = RecentAttendanceListViewAdapter.getisfileropen();

                holder = (ViewHolder) checkedrow.getTag();
                checkedbox = (CheckBox) v;

            if(filtervalueopen==true){
                if(checkedbox.isChecked()){
                    checkedbox.setChecked(false);
                }else{
                    checkedbox.setChecked(true);
                }

            }else {
                    if (checkedbox.isChecked()) {
                        checkedbox.setChecked(true);
                        holder.attendvalue.setText("Excused Absent");
                        holder.atttendanceimagevalue.setImageResource(Integer.valueOf(imageexcusedabsent));
                        holder.atttendanceimagevalue.setTag("Excused Absent");
                        values.set(position, "Excused Absent");
                        attendancelistvalue.set(position, "Excused Absent");
                        studentlist.get(position).setattendancevalue("Excused Absent");

                        int length = originalstudentlist.size();
                        for(int i=0; i<length;i++){
                            String id = originalstudentlist.get(i).getstudentid();
                            if(id==studentlist.get(position).getstudentid()){
                                originalstudentlist.get(i).setattendancevalue(studentlist.get(position).getattendancevalue());
                                break;
                            }
                        }

                        testval[position] = "Excused Absent";
                        holder.attendvalue.setTextColor(context.getResources().getColor(R.color.excusedabsent));
                    } else {
                        checkedbox.setChecked(false);
                        holder.attendvalue.setText("Present");
                        holder.atttendanceimagevalue.setImageResource(Integer.valueOf(imagepresent));
                        holder.atttendanceimagevalue.setTag("Present");
                        values.set(position, "Present");
                        attendancelistvalue.set(position, "Present");
                        studentlist.get(position).setattendancevalue("Present");

                        int length = originalstudentlist.size();
                        for(int i=0; i<length;i++){
                            String id = originalstudentlist.get(i).getstudentid();
                            if(id==studentlist.get(position).getstudentid()){
                                originalstudentlist.get(i).setattendancevalue(studentlist.get(position).getattendancevalue());
                                break;
                            }
                        }

                        testval[position] = "Present";
                        holder.attendvalue.setTextColor(context.getResources().getColor(R.color.present));
                    }
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

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        studentlist.clear();
        if (charText.length() == 0) {
            studentlist.addAll(arraylist);
        }
        else
        {
            for (AttendanceList wp : arraylist)
            {
                if (wp.getstudentname().toLowerCase(Locale.getDefault()).contains(charText) || wp.getrollnumber().toLowerCase(Locale.getDefault()).contains(charText) )
                {
                    studentlist.add(wp);
                }
            }
        }

        notifyDataSetChanged();
    }

}
