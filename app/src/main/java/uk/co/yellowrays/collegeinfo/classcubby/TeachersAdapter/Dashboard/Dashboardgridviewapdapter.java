package uk.co.yellowrays.collegeinfo.classcubby.TeachersAdapter.Dashboard;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import uk.co.yellowrays.collegeinfo.classcubby.FontManager.CustomTabLayout;
import uk.co.yellowrays.collegeinfo.classcubby.R;


/**
 * Created by Vino on 10-02-2016.
 */
public class Dashboardgridviewapdapter extends BaseAdapter {

    String[] result;
    Activity context;
    String[] imageId;
    int textsize;
    Holder holder;
    private int lastPosition = -1 ;
    private static LayoutInflater inflater = null;

    public Dashboardgridviewapdapter(Activity mainActivity, String[] prgmNameList, String[] prgmImages) {
        // TODO Auto-generated constructor stub
        this.result = prgmNameList;
        this.context = mainActivity;
        this.imageId = prgmImages;
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return result.length;
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
        RelativeLayout dashboarditemcontainer;
        ImageView image;
        TextView tv;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View rowView = convertView;

        Log.d("posiiton ImageAdapater:", "" + position);
        if (rowView == null) {
            rowView = inflater.inflate(R.layout.teacher_dashboard_gridview_adapter, null);
            holder = new Holder();
            rowView.setTag(holder);
        } else {
            holder = (Holder) rowView.getTag();
        }

        holder.tv = (TextView) rowView.findViewById(R.id.item_text);
        holder.image = (ImageView) rowView.findViewById(R.id.dashboarditemimage);
        holder.dashboarditemcontainer = (RelativeLayout) rowView.findViewById(R.id.dahboarditemcontainer);

//        holder.image.setBackgroundResource(imageId[position]);

        Typeface normaltypeface = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSansRegular.ttf");
        Typeface boldtypeface = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSansBold.ttf");
        holder.tv.setTypeface(normaltypeface);

        holder.tv.setText(result[position]);

        Glide.with(context).load(imageId[position]).listener(new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String s, Target<GlideDrawable> target, boolean b) {
                Log.i("GLIDE", "onException :", e);
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable glideDrawable, String s, Target<GlideDrawable> target, boolean b, boolean b1) {
                Log.i("GLIDE", "onResourceReady");
                holder.image.setVisibility(View.VISIBLE);
                return false;
            }
        }).into(holder.image);

        //Finding Screen size by inch diagonally
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();

        int widthPixels = metrics.widthPixels;
        int heightPixels = metrics.heightPixels;

        float widthDpi = metrics.xdpi;
        float heightDpi = metrics.ydpi;

        float widthInches = widthPixels / widthDpi;
        float heightInches = heightPixels / heightDpi;

        double diagonalInches = Math.sqrt((widthInches * widthInches) + (heightInches * heightInches));

        if (diagonalInches >= 7) {
            textsize = 16;
        }else{
            textsize = 13;
        }

        holder.tv.setTextSize(textsize);


        holder.dashboarditemcontainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewPager viewPager = (ViewPager) context.findViewById(R.id.viewpager);
                TabLayout tab = (CustomTabLayout) context.findViewById(R.id.tabs);
                tab.getTabAt(position+1).select();
                viewPager.setCurrentItem(position+1);
            }
        });


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
        },50);

        return rowView;
    }

}

