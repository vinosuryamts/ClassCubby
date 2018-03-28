package uk.co.yellowrays.collegeinfo.classcubby.WardenAdapter.OutgoingRequests.adapter;

import android.app.Activity;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import uk.co.yellowrays.collegeinfo.classcubby.R;

/**
 * Created by user1 on 19-11-2016.
 */

public class wardenrejectrowcontentadapter extends BaseAdapter {

    String[] transactionidvalue,studentnamevalue,classnamevalue,classsectionvalue,timesubmittedvalue,fromdatevalue,todatevalue;
    String[] totalleavedayscountvalue,leaverequestdetailsvalue,leavestatusnamevalue,userimagevalue,rejectreason;
    Context context;
    int textsize;
    private int lastPosition = -1 ;
    private static LayoutInflater inflater = null;

    public wardenrejectrowcontentadapter(Activity mainActivity, String[] transactionid, String[] studentname,
                                         String[] classname, String[] classsection, String[] timesubmitted, String[] fromdate,
                                         String[] todate, String[] totalleavedayscount, String[] leaverequestdetails, String[] leavestatusname,
                                         String[] userimage, String[] rejectreasonvalue) {
        // TODO Auto-generated constructor stub
        this.transactionidvalue = transactionid;
        this.studentnamevalue = studentname;
        this.classnamevalue =classname;
        this.classsectionvalue = classsection;
        this.timesubmittedvalue = timesubmitted;
        this.fromdatevalue = fromdate;
        this.todatevalue = todate;
        this.totalleavedayscountvalue = totalleavedayscount;
        this.leaverequestdetailsvalue = leaverequestdetails;
        this.leavestatusnamevalue = leavestatusname;
        this.userimagevalue = userimage;
        this.rejectreason = rejectreasonvalue;
        this.context = mainActivity;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return timesubmittedvalue.length;
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
        ImageView studentimage;
        TextView timesubmitted,studentname,fromdate,todate,leavedetail,fromdatetext,todatetext,leavedescriptiontext;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder;

        View rowView = convertView;
        Log.d("posiiton ImageAdapater:", "" + position);
        if (rowView == null) {
            rowView = inflater.inflate(R.layout.warden_outgoingpermission_reject_rowcontent, null);
            holder = new Holder();
            rowView.setTag(holder);
        } else {
            holder = (Holder) rowView.getTag();
        }

        holder.timesubmitted = (TextView) rowView.findViewById(R.id.timesubmitted);
        holder.studentname = (TextView) rowView.findViewById(R.id.studentname);
        holder.fromdate = (TextView) rowView.findViewById(R.id.fromdate);
        holder.todate = (TextView) rowView.findViewById(R.id.todate);
        holder.leavedetail = (TextView) rowView.findViewById(R.id.leavedetail);
        holder.studentimage = (ImageView) rowView.findViewById(R.id.studentimage);
        holder.fromdatetext = (TextView) rowView.findViewById(R.id.fromdatetext);
        holder.todatetext = (TextView) rowView.findViewById(R.id.todatetext);
        holder.leavedescriptiontext = (TextView) rowView.findViewById(R.id.leavedescriptiontext);

        Typeface normaltypeface = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSansRegular.ttf");
        Typeface semiboldtypeface = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSansSemibold.ttf");
        Typeface boldtypeface = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSansBold.ttf");
        holder.studentname.setTypeface(boldtypeface);
        holder.timesubmitted.setTypeface(boldtypeface);
        holder.fromdatetext.setTypeface(boldtypeface);
        holder.todatetext.setTypeface(boldtypeface);
        holder.leavedescriptiontext.setTypeface(boldtypeface);
        holder.fromdate.setTypeface(normaltypeface);
        holder.todate.setTypeface(normaltypeface);
        holder.leavedetail.setTypeface(normaltypeface);

//        holder.image.setBackgroundResource(imageId[position]);


        Glide.with(context).load(userimagevalue[position]).listener(new RequestListener<String, GlideDrawable>() {
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
        }).into(holder.studentimage);

        holder.timesubmitted.setText(timesubmittedvalue[position]);
        holder.studentname.setText(studentnamevalue[position]);
        holder.fromdate.setText(fromdatevalue[position]);
        holder.todate.setText(todatevalue[position]);

        String str = leaverequestdetailsvalue[position];
        str = str.replace("````",",");
        if (str.length() > 35)
            str = str.substring(0, 35) + "...";

        holder.leavedetail.setText(str);

        final View finalRow1 = rowView;
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

        return rowView;
    }

}
