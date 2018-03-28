package uk.co.yellowrays.collegeinfo.classcubby.TeachersAdapter.Events;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.etsy.android.grid.StaggeredGridView;

import uk.co.yellowrays.collegeinfo.classcubby.R;

/**
 * Created by user1 on 16-11-2016.
 */

public class ParentsEventsMainAdapter extends BaseAdapter {

    String[] schoollist,idlist,userimagelist,eventimagelist,eventdescriptionlist,eventpostedbylist,eventtitlelist;
    String[] eventdatelist,eventpostedtimelist,eventtypelist;
    Context context;
    Activity newcontext;
    LayoutInflater inflater = null;
    String[] testval;
    ViewHolder holder;
    String initialstring;
    SpannableString spannableString;
    String[] eventimagesplitter,eventimagesplitternewvalue;
    Staggeredgridviewapdapter adapternew;


    public ParentsEventsMainAdapter(Activity context, String[] schoolname,
                                    String[] eventid, String[] userimages, String[] eventimages,
                                    String[] eventdescription, String[] eventpostedby, String[] eventTitle, String[] eventDate,
                                    String[] eventpostedtime, String[] eventtype) {
        this.context = context;
        this.newcontext = context;
        this.schoollist = schoolname;
        this.idlist = eventid;
        this.userimagelist = userimages;
        this.eventimagelist = eventimages;
        this.eventdescriptionlist = eventdescription;
        this.eventpostedbylist = eventpostedby;
        this.eventtitlelist = eventTitle;
        this.eventdatelist = eventDate;
        this.eventpostedtimelist = eventpostedtime;
        this.eventtypelist = eventtype;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        testval = new String[idlist.length];
    }

    @Override
    public int getCount() {
        return idlist.length;
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
        TextView postedtime,schoolname,eventdate,eventfor,eventtitle,evendescription,postedby;
        TextView postedbytext,eventdatetext,eventfortext,eventtitletext,evendescriptiontext;
        ImageView posterimage;
        StaggeredGridView eventimages;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        Log.d("posiiton ImageAdapater:", "" + position);
        if (row == null) {
            row = inflater.inflate(R.layout.events_rowcontent_xml, null);
            holder = new ViewHolder();
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        holder.postedbytext = (TextView) row.findViewById(R.id.postedbytext);
        holder.eventdatetext = (TextView) row.findViewById(R.id.eventdatetext);
        holder.eventfortext = (TextView) row.findViewById(R.id.eventfortext);
        holder.eventtitletext = (TextView) row.findViewById(R.id.eventtitletext);
        holder.evendescriptiontext = (TextView) row.findViewById(R.id.evendescriptiontext);
        holder.postedtime = (TextView) row.findViewById(R.id.postedtime);
        holder.schoolname = (TextView) row.findViewById(R.id.schoolname);
        holder.eventdate = (TextView) row.findViewById(R.id.eventdate);
        holder.eventfor = (TextView) row.findViewById(R.id.eventfor);
        holder.eventtitle = (TextView) row.findViewById(R.id.eventtitle);
        holder.evendescription = (TextView) row.findViewById(R.id.evendescription);
        holder.postedby = (TextView) row.findViewById(R.id.postedby);
        holder.posterimage = (ImageView) row.findViewById(R.id.posterimage);
        holder.eventimages = (StaggeredGridView) row.findViewById(R.id.eventimages);

        Typeface normaltypeface = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSansRegular.ttf");
        Typeface semiboldtypeface = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSansSemibold.ttf");
        Typeface boldtypeface = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSansBold.ttf");
        holder.postedbytext.setTypeface(semiboldtypeface);
        holder.eventdatetext.setTypeface(semiboldtypeface);
        holder.eventfortext.setTypeface(semiboldtypeface);
        holder.eventtitletext.setTypeface(semiboldtypeface);
        holder.evendescriptiontext.setTypeface(semiboldtypeface);
        holder.schoolname.setTypeface(boldtypeface);
        holder.postedtime.setTypeface(normaltypeface);
        holder.eventfor.setTypeface(normaltypeface);
        holder.eventdate.setTypeface(normaltypeface);
        holder.eventtitle.setTypeface(normaltypeface);
        holder.evendescription.setTypeface(normaltypeface);
        holder.postedby.setTypeface(normaltypeface);


        holder.postedtime.setText(eventpostedtimelist[position]);
        holder.schoolname.setText(schoollist[position]);
        holder.eventdate.setText(eventdatelist[position]);
        holder.postedby.setText(eventpostedbylist[position]);

        if(eventtypelist[position].equals("null")){
            holder.eventfor.setText("College");
        }else {
            holder.eventfor.setText(eventtypelist[position]);
        }

        holder.eventtitle.setText(eventtitlelist[position]);


        if(eventdescriptionlist[position].length()>120) {
            String firststring = eventdescriptionlist[position].substring(0, 120);
            String secondstring = eventdescriptionlist[position].substring(120);
            initialstring = firststring.concat(" ...Show More");

            int length = initialstring.length();

            spannableString = new SpannableString(initialstring);

            spannableString.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    TextView tv = (TextView)widget;
                    String firststring = eventdescriptionlist[position].substring(0, 120);
                    String secondstring = eventdescriptionlist[position].substring(120);
                    initialstring = eventdescriptionlist[position];
                    tv.setText(initialstring);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                }
            },121,length,0);

            holder.evendescription.setText(spannableString);
            holder.evendescription.setMovementMethod(LinkMovementMethod.getInstance());

        }else{
            holder.evendescription.setText(eventdescriptionlist[position]);
        }


        String eventsingleimage = eventimagelist[position];
        eventsingleimage = eventsingleimage.replace("[", "");
        eventsingleimage = eventsingleimage.replace("]", "");
        eventsingleimage = eventsingleimage.replace("\"", "");
        eventsingleimage = eventsingleimage.replace(", ", ",");
        eventimagesplitter = eventsingleimage.split("@");

        eventimagesplitternewvalue = new String[2];

        if(!(eventsingleimage.equals("null"))) {
            if(!(eventimagesplitter.length==1)) {
                for (int i = 0; i < 2; i++) {
                    eventimagesplitternewvalue[i] = eventimagesplitter[i];
                }
            }else{
                eventimagesplitternewvalue = new String[1];
                eventimagesplitternewvalue = eventimagesplitter;
            }
        }

        View v;
        StaggeredGridView et;
        v = row;
        et = (StaggeredGridView) v.findViewById(R.id.eventimages);

        if(eventsingleimage.equals("null")) {
            holder.eventimages.setVisibility(View.GONE);
        }else{
            holder.eventimages.setVisibility(View.VISIBLE);
            adapternew = new Staggeredgridviewapdapter(newcontext,eventimagesplitternewvalue,eventimagesplitter.length,schoollist[position],idlist[position],userimagelist[position],eventimagelist[position],eventdescriptionlist[position],eventpostedbylist[position],eventtitlelist[position],eventdatelist[position],eventpostedtimelist[position],eventtypelist[position]);
            if(eventimagesplitter.length>=1) {
                et.setColumnCount(1);
                et.setAdapter(adapternew);
            }else{
                et.setColumnCount(eventimagesplitter.length);
                et.setAdapter(adapternew);
            }

        }

        Glide.with(context).load(userimagelist[position]).listener(new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String s, Target<GlideDrawable> target, boolean b) {
                Log.i("GLIDE", "onException :", e);
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable glideDrawable, String s, Target<GlideDrawable> target, boolean b, boolean b1) {
                Log.i("GLIDE", "onResourceReady");
                holder.posterimage.setVisibility(View.VISIBLE);
                return false;
            }
        }).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.posterimage);

        return row;
    }

}
