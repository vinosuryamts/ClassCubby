package uk.co.yellowrays.collegeinfo.classcubby.TeachersAdapter.Events;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.json.JSONArray;
import org.json.simple.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import uk.co.yellowrays.collegeinfo.classcubby.R;


/**
 * Created by Vino on 10-02-2016.
 */
public class Staggeredgridviewapdapter extends BaseAdapter {

    String[] result;
    Context context;
    Activity newcontext;
    Holder holder;
    int eventcount;
    String schoollist,idlist,userimagelist,eventimagelist,eventdescriptionlist,eventpostedbylist,eventtitlelist;
    String eventdatelist,eventpostedtimelist,eventtypelist,serveruri;
    private static LayoutInflater inflater = null;
    String[] eventimageslist;
    String[] eventimagesplitter,eventimagesplitternewvalue;
    JSONObject mainObj,jo;
    JSONArray ja;
    String filename,filetype;
    ProgressDialog loginDialog;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public Staggeredgridviewapdapter(Activity mainActivity, String[] prgmNameList, int count, String schoolname,
                                     String eventid, String userimages, String eventimages,
                                     String eventdescription, String eventpostedby, String eventTitle, String eventDate,
                                     String eventpostedtime, String eventtype) {
        // TODO Auto-generated constructor stub
        this.result = prgmNameList;
        this.context = mainActivity;
        this.newcontext = mainActivity;
        this.eventcount = count;
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
        ImageView image;
        TextView textview;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        View row = convertView;
        Log.d("posiiton ImageAdapater:", "" + position);
        if (row == null) {
            row = inflater.inflate(R.layout.events_staggered_image_rowcontent_xml, null);
            holder = new Holder();
            row.setTag(holder);
        } else {
            holder = (Holder) row.getTag();
        }

        holder.image = (ImageView) row.findViewById(R.id.mImageView);
        holder.textview = (TextView) row.findViewById(R.id.textview);

//        holder.image.setBackgroundResource(imageId[position]);
        if (eventcount == 1) {
            holder.textview.setVisibility(View.INVISIBLE);
        } else {
            if(position==0){
                holder.textview.setVisibility(View.VISIBLE);
                holder.textview.setText("+ " + (eventcount-1));
                holder.textview.getBackground().setAlpha(150);
            }else {
                holder.textview.setVisibility(View.INVISIBLE);
            }
        }

        Glide.with(context).load(result[position]).listener(new RequestListener<String, GlideDrawable>() {
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
        }).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.image);

        jo = new JSONObject();
        jo.put("schoolname", schoollist);
        jo.put("eventid", idlist);
        jo.put("userimages", userimagelist);
        jo.put("eventdescription", eventdescriptionlist);
        jo.put("eventpostedby", eventpostedbylist);
        jo.put("eventTitle", eventtitlelist);
        jo.put("eventDate", eventdatelist);
        jo.put("eventpostedtime", eventpostedtimelist);
        jo.put("eventtype", eventtypelist);

        ja = new JSONArray();
        ja.put(jo);

        mainObj = new JSONObject();
        mainObj.put("valueslist", ja);


        holder.textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent i = new Intent(context, Parents_Events_Images_Click.class);
                Bundle bundle = new Bundle();
                bundle.putString("values", mainObj.toString());
                bundle.putString("images", eventimagelist);
                i.putExtras(bundle);
                context.startActivity(i);*/
            }
        });

        final int newposition = position;
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent i = new Intent(context, Parents_Events_Images_Click.class);
                Bundle bundle = new Bundle();
                bundle.putString("values", mainObj.toString());
                bundle.putString("images", eventimagelist);
                i.putExtras(bundle);
                context.startActivity(i);*/
                serveruri = result[newposition];
                loginDialog = new ProgressDialog(newcontext);
                loginDialog.setIndeterminate(true);
                loginDialog.setTitle("Please Wait");
                loginDialog.setMessage("Loading Image...");
                loginDialog.show();
                loginDialog.setCancelable(false);
                loginDialog.setCanceledOnTouchOutside(false);
                getserverpathuri(result[newposition]);

            }
        });

        return row;
    }


    private void getserverpathuri(final String serverpathvalue) {
        Uri extUrl =  Uri.parse(serverpathvalue);


        //PATH: /photos-ak-snc1/v315/224/13/659629384/s659629384_752969_4472.jpg
        String path = extUrl.getPath();

        //Checks for both forward and/or backslash
        //NOTE:**While backslashes are not supported in URL's
        //most browsers will autoreplace them with forward slashes
        //So technically if you're parsing an html page you could run into
        //a backslash , so i'm accounting for them here;
        String[] pathContents = path.split("[\\\\/]");
        int pathContentsLength;
        if(pathContents != null){
            pathContentsLength = pathContents.length;
            System.out.println("Path Contents Length: " + pathContentsLength);
            for (int i = 0; i < pathContents.length; i++) {
                System.out.println("Path " + i + ": " + pathContents[i]);
            }
            //lastPart: s659629384_752969_4472.jpg
            String lastPart = pathContents[pathContentsLength-1];
            String[] lastPartContents = lastPart.split("\\.");
            if(lastPartContents != null && lastPartContents.length > 1){
                int lastPartContentLength = lastPartContents.length;
                System.out.println("Last Part Length: " + lastPartContentLength);
                //filenames can contain . , so we assume everything before
                //the last . is the name, everything after the last . is the
                //extension
                String name = "";
                for (int i = 0; i < lastPartContentLength; i++) {
                    System.out.println("Last Part " + i + ": "+ lastPartContents[i]);
                    if(i < (lastPartContents.length -1)){
                        name += lastPartContents[i] ;
                        if(i < (lastPartContentLength -2)){
                            name += ".";
                        }
                    }
                }
                filetype = lastPartContents[lastPartContentLength -1];

                filename =  name;

                new DownloadFileAsync().execute(serveruri);

            }
        }

    }

    class DownloadFileAsync extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... aurl) {
            int count;

            try {

                verifyStoragePermissions(newcontext);

                URL url = new URL(aurl[0]);
                URLConnection conexion = url.openConnection();
                conexion.connect();

                int lenghtOfFile = conexion.getContentLength();
                Log.d("ANDRO_ASYNC", "Lenght of file: " + lenghtOfFile);

                InputStream input = new BufferedInputStream(url.openStream(), 10240);

                File cacheFile = null;
                if(isExternalStorageWritable()==true && isExternalStorageReadable()==true) {
                    cacheFile = new File(Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_DCIM), "Classcubby/");
                    if (!cacheFile.mkdirs()) {
                        //Toast.makeText(getActivity().getApplicationContext(),"Directory not created",Toast.LENGTH_LONG).show();
                    }
                }

                File file = new File(String.valueOf(new File(cacheFile, filename+"."+filetype)));

                if (file.exists()){
                    loginDialog.dismiss();
                    FileOpen.openFile(context.getApplicationContext(), file);
                }else {
                    FileOutputStream output = new FileOutputStream(file);

                    byte buffer[] = new byte[1024];
                    int dataSize;
                    int loadedSize = 0;
                    while ((dataSize = input.read(buffer)) != -1) {
                        loadedSize += dataSize;
                        publishProgress(String.valueOf((int) ((loadedSize * 100) / lenghtOfFile)));
                        output.write(buffer, 0, dataSize);
                    }
                    output.close();

                    loginDialog.dismiss();

                    FileOpen.openFile(context.getApplicationContext(), file);
                }

            } catch (Exception e) {
                Toast.makeText(context.getApplicationContext(),e.getMessage().toString(),Toast.LENGTH_LONG).show();
            }
            return null;

        }
        protected void onProgressUpdate(String... progress) {
            Log.d("ANDRO_ASYNC",progress[0]);

        }

        @Override
        protected void onPostExecute(String unused) {

        }
    }


    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    public static class FileOpen {
        public static void openFile(Context context, File url) throws IOException {
            // Create URI
            File file=url;
            Uri uri = Uri.fromFile(file);

            Intent intent = new Intent(Intent.ACTION_VIEW);
            // Check what kind of file you are trying to open, by comparing the url with extensions.
            // When the if condition is matched, plugin sets the correct intent (mime) type,
            // so Android knew what application to use to open the file
            if (url.toString().contains(".doc") || url.toString().contains(".docx")) {
                // Word document
                intent.setDataAndType(uri, "application/msword");
            } else if(url.toString().contains(".pdf")) {
                // PDF file
                intent.setDataAndType(uri, "application/pdf");
            } else if(url.toString().contains(".ppt") || url.toString().contains(".pptx")) {
                // Powerpoint file
                intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
            } else if(url.toString().contains(".xls") || url.toString().contains(".xlsx")) {
                // Excel file
                intent.setDataAndType(uri, "application/vnd.ms-excel");
            } else if(url.toString().contains(".zip") || url.toString().contains(".rar")) {
                // WAV audio file
                intent.setDataAndType(uri, "application/x-wav");
            } else if(url.toString().contains(".rtf")) {
                // RTF file
                intent.setDataAndType(uri, "application/rtf");
            } else if(url.toString().contains(".wav") || url.toString().contains(".mp3")) {
                // WAV audio file
                intent.setDataAndType(uri, "audio/x-wav");
            } else if(url.toString().contains(".gif")) {
                // GIF file
                intent.setDataAndType(uri, "image/gif");
            } else if(url.toString().contains(".jpg") || url.toString().contains(".jpeg") || url.toString().contains(".png")) {
                // JPG file
                intent.setDataAndType(uri, "image/jpeg");
            } else if(url.toString().contains(".txt")) {
                // Text file
                intent.setDataAndType(uri, "text/plain");
            } else if(url.toString().contains(".3gp") || url.toString().contains(".mpg") || url.toString().contains(".mpeg") || url.toString().contains(".mpe") || url.toString().contains(".mp4") || url.toString().contains(".avi")) {
                // Video files
                intent.setDataAndType(uri, "video/*");
            } else {
                //if you want you can also define the intent type for any other file

                //additionally use else clause below, to manage other unknown extensions
                //in this case, Android will show all applications installed on the device
                //so you can choose which application to use
                intent.setDataAndType(uri, "*/*");
            }

            if(url.getPath().endsWith(".jpg") || url.getPath().endsWith(".jpeg")|| url.getPath().endsWith(".png"))
            {
                intent.setDataAndType(uri,"image/*");
            }

            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }
}

