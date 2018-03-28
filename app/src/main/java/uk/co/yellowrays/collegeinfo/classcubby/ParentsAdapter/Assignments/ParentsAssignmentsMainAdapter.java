package uk.co.yellowrays.collegeinfo.classcubby.ParentsAdapter.Assignments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;

import uk.co.yellowrays.collegeinfo.classcubby.R;

/**
 * Created by user1 on 16-11-2016.
 */

public class ParentsAssignmentsMainAdapter extends BaseAdapter {

    String[] assignmentdate,assignmentid,assignmenttitle,assignmentdesc,assignmenttarget,assignmentsubject,assignmentattachment,assignmentfilename;
    String[] eventdatelist,eventpostedtimelist,eventtypelist;
    Context context;
    Activity newcontext;
    LayoutInflater inflater = null;
    String[] testval;
    ViewHolder holder;
    String initialstring;
    SpannableString spannableString;
    String filename,filetype,assignmentfilenamevalue,serverpathvalue;
    String attachmenttype = "Image";
    Uri fileuri;
    long filesize;
    int filesizenewvalue;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    RelativeLayout innerimagecardviewcontainer,innerfilescardviewcontainer,assignmentsmaincontainer,innerfilterlayout,downloadbackgroundcolor;
    CircularProgressBar filedownloadingprogress,imagedownloadingprogress;
    ImageView calendarimage,calendarimage1,browseimage,iv_bitmap,iconpreviewer,iconpreviewer1;
    TextView filetypenamepreview,filesizevalue,downloadtext,downloadcanceltext,imagesizevalue,filenametext,imagetypenamepreview,imagedownloadtext,imagedownloadcanceltext;
    ViewHolder holder1,holder2;

    public ParentsAssignmentsMainAdapter(Activity context, String[] assignmentdate,
                                         String[] assignmentid, String[] assignmenttitle, String[] assignmentdesc,
                                         String[] assignmenttarget, String[] assignmentsubject, String[] assignmentattachment,
                                         String[] assignmentfilename) {
        this.context = context;
        this.newcontext = context;
        this.assignmentdate = assignmentdate;
        this.assignmentid = assignmentid;
        this.assignmenttitle = assignmenttitle;
        this.assignmentdesc = assignmentdesc;
        this.assignmenttarget = assignmenttarget;
        this.assignmentsubject = assignmentsubject;
        this.assignmentattachment = assignmentattachment;
        this.assignmentfilename = assignmentfilename;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        testval = new String[assignmentid.length];
    }

    @Override
    public int getCount() {
        return assignmentid.length;
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
        TextView postedtime,subjectname,postedbytext,postedby,targettitlename,targettitle,titlename,targetnamevalue;
        TextView title,descriptiontitlename,descriptiontext,targetfilenamevalue,targetfiletypevalue;
        RelativeLayout innerimagecardviewcontainer,innerfilescardviewcontainer,assignmentsmaincontainer,innerfilterlayout,downloadbackgroundcolor;
        CircularProgressBar filedownloadingprogress,imagedownloadingprogress;
        ImageView calendarimage,calendarimage1,browseimage,iv_bitmap,iconpreviewer,iconpreviewer1;
        TextView filetypenamepreview,filesizevalue,downloadtext,downloadcanceltext,imagesizevalue,filenametext,imagetypenamepreview,imagedownloadtext,imagedownloadcanceltext;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        Log.d("posiiton ImageAdapater:", "" + position);
        if (row == null) {
            row = inflater.inflate(R.layout.parents_assignments_rowcontent_xml, null);
            holder = new ViewHolder();
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        holder.postedtime = (TextView) row.findViewById(R.id.postedtime);
        holder.subjectname = (TextView) row.findViewById(R.id.subjectname);
        holder.targettitlename = (TextView) row.findViewById(R.id.targettitlename);
        holder.targettitle = (TextView) row.findViewById(R.id.targettitle);
        holder.titlename = (TextView) row.findViewById(R.id.titlename);
        holder.title = (TextView) row.findViewById(R.id.title);
        holder.descriptiontitlename = (TextView) row.findViewById(R.id.descriptiontitlename);
        holder.descriptiontext = (TextView) row.findViewById(R.id.descriptiontext);
        holder.targetnamevalue = (TextView) row.findViewById(R.id.targetnamevalue);
        holder.targetfilenamevalue = (TextView) row.findViewById(R.id.targetfilenamevalue);
        holder.targetfiletypevalue = (TextView) row.findViewById(R.id.targetfiletypevalue);


        holder.assignmentsmaincontainer = (RelativeLayout) row.findViewById(R.id.assignmentsmaincontainer);
        holder.innerfilterlayout = (RelativeLayout) row.findViewById(R.id.innerfilterlayout);
        holder.innerimagecardviewcontainer = (RelativeLayout) row.findViewById(R.id.innerimagecardviewcontainer);
        holder.iv_bitmap = (ImageView) row.findViewById(R.id.iv_bitmap);
        holder.imagesizevalue = (TextView) row.findViewById(R.id.imagesizevalue);
        holder.imagetypenamepreview = (TextView) row.findViewById(R.id.imagetypenamepreview);
        holder.imagedownloadtext = (TextView) row.findViewById(R.id.imagedownloadtext);
        holder.imagedownloadcanceltext = (TextView) row.findViewById(R.id.imagedownloadcanceltext);

        holder.innerfilescardviewcontainer = (RelativeLayout) row.findViewById(R.id.innerfilescardviewcontainer);
        holder.downloadbackgroundcolor = (RelativeLayout) row.findViewById(R.id.downloadbackgroundcolor);
        holder.iconpreviewer1 = (ImageView) row.findViewById(R.id.iconpreviewer1);
        holder.filenametext = (TextView) row.findViewById(R.id.filenametext);
        holder.filetypenamepreview = (TextView) row.findViewById(R.id.filetypenamepreview);
        holder.filesizevalue = (TextView) row.findViewById(R.id.filesizevalue);
        holder.downloadtext = (TextView) row.findViewById(R.id.downloadtext);
        holder.downloadcanceltext = (TextView) row.findViewById(R.id.downloadcanceltext);
        holder.filedownloadingprogress = (CircularProgressBar) row.findViewById(R.id.filedownloadingprogress);
        holder.imagedownloadingprogress = (CircularProgressBar) row.findViewById(R.id.imagedownloadingprogress);


        Typeface normaltypeface = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSansRegular.ttf");
        Typeface semiboldtypeface = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSansSemibold.ttf");
        Typeface boldtypeface = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSansBold.ttf");
        Typeface fontawesome = Typeface.createFromAsset(context.getAssets(), "fonts/fontawesome.ttf");
        holder.descriptiontext.setTypeface(normaltypeface);
        holder.filenametext.setTypeface(normaltypeface);
        holder.downloadtext.setTypeface(fontawesome);
        holder.downloadcanceltext.setTypeface(fontawesome);
        holder.imagedownloadtext.setTypeface(fontawesome);
        holder.imagedownloadcanceltext.setTypeface(fontawesome);


        holder.postedtime.setText(assignmentdate[position]);
        holder.subjectname.setText(assignmentsubject[position]);
        holder.targettitle.setText(assignmenttarget[position]);
        holder.title.setText(assignmenttitle[position]);

        if(assignmentdesc[position].length()>120) {
            String firststring = assignmentdesc[position].substring(0, 120);
            String secondstring = assignmentdesc[position].substring(120);
            initialstring = firststring.concat(" ...Show More");

            int length = initialstring.length();

            spannableString = new SpannableString(initialstring);

            spannableString.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    TextView tv = (TextView)widget;
                    String firststring = assignmentdesc[position].substring(0, 120);
                    String secondstring = assignmentdesc[position].substring(120);
                    initialstring = assignmentdesc[position];
                    tv.setText(initialstring);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                }
            },121,length,0);

            holder.descriptiontext.setText(spannableString);
            holder.descriptiontext.setMovementMethod(LinkMovementMethod.getInstance());

        }else{
            holder.descriptiontext.setText(assignmentdesc[position]);
        }

        holder.targetnamevalue.setText(assignmentattachment[position].trim());
        final View finalRow = row;
        Uri extUrl = Uri.parse(assignmentattachment[position].trim());
        if(assignmentattachment[position].trim().equals("")){
            holder = (ViewHolder) finalRow.getTag();
            holder.innerimagecardviewcontainer.setVisibility(View.GONE);
            holder.innerfilescardviewcontainer.setVisibility(View.GONE);
        }else {
            //PATH: /photos-ak-snc1/v315/224/13/659629384/s659629384_752969_4472.jpg
            verifyStoragePermissions(newcontext);

            String path = extUrl.getPath();

            //Checks for both forward and/or backslash
            //NOTE:**While backslashes are not supported in URL's
            //most browsers will autoreplace them with forward slashes
            //So technically if you're parsing an html page you could run into
            //a backslash , so i'm accounting for them here;
            String[] pathContents = path.split("[\\\\/]");
            int pathContentsLength;
            if (pathContents != null) {
                pathContentsLength = pathContents.length;
                System.out.println("Path Contents Length: " + pathContentsLength);
                for (int i = 0; i < pathContents.length; i++) {
                    System.out.println("Path " + i + ": " + pathContents[i]);
                }
                //lastPart: s659629384_752969_4472.jpg
                String lastPart = pathContents[pathContentsLength - 1];
                String[] lastPartContents = lastPart.split("\\.");
                if (lastPartContents != null && lastPartContents.length > 1) {
                    int lastPartContentLength = lastPartContents.length;
                    System.out.println("Last Part Length: " + lastPartContentLength);
                    //filenames can contain . , so we assume everything before
                    //the last . is the name, everything after the last . is the
                    //extension
                    String name = "";
                    for (int i = 0; i < lastPartContentLength; i++) {
                        System.out.println("Last Part " + i + ": " + lastPartContents[i]);
                        if (i < (lastPartContents.length - 1)) {
                            name += lastPartContents[i];
                            if (i < (lastPartContentLength - 2)) {
                                name += ".";
                            }
                        }
                    }
                    holder = (ViewHolder) finalRow.getTag();
                    serverpathvalue = holder.targetnamevalue.getText().toString().trim();

                    filetype = lastPartContents[lastPartContentLength - 1];

                    if (assignmentfilenamevalue != null && !assignmentfilenamevalue.isEmpty()) {
                        filename = assignmentfilenamevalue;
                    } else {
                        filename = assignmentfilename[position];
                    }

                    holder.targetfilenamevalue.setText(assignmentfilename[position]);
                    holder.targetfiletypevalue.setText(filetype);

                    if (filetype.equals("docx") || filetype.equals("doc") || filetype.equals("xlsx") || filetype.equals("pdf") || filetype.equals("pptx") || filetype.equals("txt") || filetype.equals("csv")) {
                        holder.innerimagecardviewcontainer.setVisibility(View.GONE);
                        holder.innerfilescardviewcontainer.setVisibility(View.VISIBLE);

                        if (filetype.equals("docx") || filetype.equals("doc")) {
                            holder.iconpreviewer1.setImageDrawable(context.getResources().getDrawable(R.drawable.wordicon));
                        } else if (filetype.equals("xlsx")) {
                            holder.iconpreviewer1.setImageDrawable(context.getResources().getDrawable(R.drawable.excelicon));
                        } else if (filetype.equals("pdf")) {
                            holder.iconpreviewer1.setImageDrawable(context.getResources().getDrawable(R.drawable.pdficon));
                        } else if (filetype.equals("pptx")) {
                            holder.iconpreviewer1.setImageDrawable(context.getResources().getDrawable(R.drawable.ppticon));
                        } else if (filetype.equals("txt")) {
                            holder.iconpreviewer1.setImageDrawable(context.getResources().getDrawable(R.drawable.notepadicon));
                        }

                        holder.filenametext.setText(filename + "." + filetype);

                        attachmenttype = "Files";

                        File cacheFile = null;
                        if (isExternalStorageWritable() == true && isExternalStorageReadable() == true) {
                            cacheFile = new File(Environment.getExternalStoragePublicDirectory(
                                    Environment.DIRECTORY_DOCUMENTS), "Classcubby/");
                            if (!cacheFile.mkdirs()) {
                                //Toast.makeText(getActivity().getApplicationContext(),"Directory not created",Toast.LENGTH_LONG).show();
                            }
                        }

                        final File file = new File(String.valueOf(new File(cacheFile, filename + "." + filetype)));

                        if (file.exists()) {
                            holder.downloadtext.setVisibility(View.INVISIBLE);
                            holder.filedownloadingprogress.setVisibility(View.INVISIBLE);
                        } else {
                            holder.downloadtext.setVisibility(View.VISIBLE);
                            holder.filedownloadingprogress.setVisibility(View.VISIBLE);
                        }

                        holder.downloadcanceltext.setVisibility(View.INVISIBLE);

                        holder.filetypenamepreview.setText(filetype);


                        final View finalRow2 = finalRow;
                        holder1 = (ViewHolder) finalRow2.getTag();

                        new DownloadFilesizeAsync().execute(serverpathvalue);

                        final View finalRow3 = finalRow;
                        holder2 = (ViewHolder) finalRow3.getTag();

                        final File finalCacheFile = cacheFile;
                        holder1.filenametext.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                TextView filenametext = (TextView)finalRow3.findViewById(R.id.targetfilenamevalue);
                                TextView filetypetext = (TextView)finalRow3.findViewById(R.id.targetfiletypevalue);

                                filename = filenametext.getText().toString().trim();
                                filetype = filetypetext.getText().toString().trim();

                                if (file.exists()) {
                                    File file = new File(String.valueOf(new File(finalCacheFile, filename + "." + filetype)));
                                    try {
                                        FileOpen.openFile(context.getApplicationContext(), file);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });

                        final View finalRow1 = finalRow;
                        holder1.downloadtext.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                holder1 = (ViewHolder) finalRow1.getTag();
                                serverpathvalue = holder1.targetnamevalue.getText().toString().trim();
                                filename = holder1.targetfilenamevalue.getText().toString().trim();
                                filetype = holder1.targetfiletypevalue.getText().toString().trim();

                                holder1.downloadtext.setVisibility(View.INVISIBLE);
                                holder1.filedownloadingprogress.setVisibility(View.VISIBLE);
                                holder1.downloadcanceltext.setVisibility(View.VISIBLE);
                                new DownloadFileAsync().execute(serverpathvalue);

                            }
                        });


                    } else {

                        /*holder.innerimagecardviewcontainer.setVisibility(View.VISIBLE);
                        holder.innerfilescardviewcontainer.setVisibility(View.GONE);
                        holder.imagedownloadtext.setVisibility(View.INVISIBLE);
                        holder.imagedownloadingprogress.setVisibility(View.INVISIBLE);
                        holder.downloadbackgroundcolor.setVisibility(View.INVISIBLE);

                        Glide.with(context).load(serverpathvalue).listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String s, Target<GlideDrawable> target, boolean b) {
                                Log.i("GLIDE", "onException :", e);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable glideDrawable, String s, Target<GlideDrawable> target, boolean b, boolean b1) {
                                Log.i("GLIDE", "onResourceReady");
                                holder.iv_bitmap.setVisibility(View.VISIBLE);
                                return false;
                            }
                        }).into(holder.iv_bitmap);*/



                        attachmenttype = "Image";

                        holder.innerimagecardviewcontainer.setVisibility(View.VISIBLE);
                        holder.innerfilescardviewcontainer.setVisibility(View.GONE);

                        File cacheFile = null;
                        if (isExternalStorageWritable() == true && isExternalStorageReadable() == true) {
                            cacheFile = new File(Environment.getExternalStoragePublicDirectory(
                                    Environment.DIRECTORY_DCIM), "Classcubby/");
                            if (!cacheFile.mkdirs()) {
                                //Toast.makeText(getActivity().getApplicationContext(),"Directory not created",Toast.LENGTH_LONG).show();
                            }
                        }

                        final File file = new File(String.valueOf(new File(cacheFile, filename + "." + filetype)));


                        if (file.exists()) {
                            final View finalRow2 = finalRow;
                            holder1 = (ViewHolder) finalRow2.getTag();
                            holder.imagedownloadtext.setVisibility(View.INVISIBLE);
                            holder.imagedownloadingprogress.setVisibility(View.INVISIBLE);
                            holder.downloadbackgroundcolor.setVisibility(View.INVISIBLE);
                            serverpathvalue = holder1.targetnamevalue.getText().toString().trim();

                            Glide.with(context.getApplicationContext()).load(serverpathvalue).listener(new RequestListener<String, GlideDrawable>() {
                                @Override
                                public boolean onException(Exception e, String s, Target<GlideDrawable> target, boolean b) {
                                    Log.i("GLIDE", "onException :", e);
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(GlideDrawable glideDrawable, String s, Target<GlideDrawable> target, boolean b, boolean b1) {
                                    Log.i("GLIDE", "onResourceReady");
                                    holder.iv_bitmap.setVisibility(View.VISIBLE);
                                    return false;
                                }
                            }).into(holder.iv_bitmap);

                        } else {
                            final View finalRow2 = finalRow;
                            holder1 = (ViewHolder) finalRow2.getTag();

                            holder.imagedownloadtext.setVisibility(View.VISIBLE);
                            holder.imagedownloadingprogress.setVisibility(View.VISIBLE);
                            holder.downloadbackgroundcolor.setVisibility(View.VISIBLE);
                            holder.iv_bitmap.setVisibility(View.VISIBLE);
                            serverpathvalue = holder1.targetnamevalue.getText().toString().trim();

                            Glide.with(context).load(serverpathvalue).listener(new RequestListener<String, GlideDrawable>() {
                                @Override
                                public boolean onException(Exception e, String s, Target<GlideDrawable> target, boolean b) {
                                    Log.i("GLIDE", "onException :", e);
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(GlideDrawable glideDrawable, String s, Target<GlideDrawable> target, boolean b, boolean b1) {
                                    Log.i("GLIDE", "onResourceReady");
                                    holder.iv_bitmap.setVisibility(View.VISIBLE);
                                    return false;
                                }
                            }).override(92, 92).into(holder.iv_bitmap);
                        }

                        holder.imagedownloadcanceltext.setVisibility(View.GONE);
                        holder.imagetypenamepreview.setText(filetype);


                        new DownloadFilesizeAsync().execute(serverpathvalue);

                        final View finalRow3 = finalRow;
                        holder2 = (ViewHolder) finalRow3.getTag();
                        final File finalCacheFile = cacheFile;
                        holder1.iv_bitmap.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                TextView filenametext = (TextView)finalRow3.findViewById(R.id.targetfilenamevalue);
                                TextView filetypetext = (TextView)finalRow3.findViewById(R.id.targetfiletypevalue);

                                filename = filenametext.getText().toString().trim();
                                filetype = filetypetext.getText().toString().trim();

                                if (file.exists()) {
                                    File file = new File(String.valueOf(new File(finalCacheFile, filename + "." + filetype)));
                                    try {
                                        FileOpen.openFile(newcontext.getApplicationContext(), file);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });

                        final View finalRow1 = finalRow;
                        holder1.imagedownloadtext.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                holder1 = (ViewHolder) finalRow1.getTag();
                                serverpathvalue = holder1.targetnamevalue.getText().toString().trim();
                                filename = holder1.targetfilenamevalue.getText().toString().trim();
                                filetype = holder1.targetfiletypevalue.getText().toString().trim();

                                holder1.filedownloadingprogress.setVisibility(View.GONE);
                                holder1.imagedownloadtext.setVisibility(View.INVISIBLE);
                                holder1.imagedownloadingprogress.setVisibility(View.VISIBLE);
                                holder1.imagedownloadcanceltext.setVisibility(View.VISIBLE);
                                new DownloadFileAsync().execute(serverpathvalue);

                            }
                        });

                    }

                }
            }
        }

        return row;
    }

    private void getserverpathuri(final String serverpath,final View finalRow) {


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

                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                if(filetype.equals("docx") || filetype.equals("doc")|| filetype.equals("xlsx") || filetype.equals("pdf")|| filetype.equals("pptx")|| filetype.equals("txt")|| filetype.equals("csv")){
                    File cacheFile = null;
                    if(isExternalStorageWritable()==true && isExternalStorageReadable()==true) {
                        cacheFile = new File(Environment.getExternalStoragePublicDirectory(
                                Environment.DIRECTORY_DOCUMENTS), "Classcubby/");
                        if (!cacheFile.mkdirs()) {
                            //Toast.makeText(newcontext.getApplicationContext(),"Directory not created",Toast.LENGTH_LONG).show();
                        }
                    }
                    File file = new File(String.valueOf(new File(cacheFile, filename+"."+filetype)));

                    if (file.exists())
                        file.delete();

                    FileOutputStream output = new FileOutputStream(file);

                    byte buffer[] = new byte[1024];
                    int dataSize;
                    int loadedSize = 0;
                    while ((dataSize = input.read(buffer)) != -1) {
                        loadedSize += dataSize;
                        publishProgress(String.valueOf((int)((loadedSize*100)/lenghtOfFile)));
                        output.write(buffer, 0, dataSize);
                    }

                    output.flush();

                    output.close();

                    input.close();

                }else{
                    File cacheFile = null;
                    if(isExternalStorageWritable()==true && isExternalStorageReadable()==true) {
                        cacheFile = new File(Environment.getExternalStoragePublicDirectory(
                                Environment.DIRECTORY_DCIM), "Classcubby/");
                        if (!cacheFile.mkdirs()) {
                            //Toast.makeText(getActivity().getApplicationContext(),"Directory not created",Toast.LENGTH_LONG).show();
                        }
                    }

                    File file = new File(String.valueOf(new File(cacheFile, filename+"."+filetype)));

                    if (file.exists())
                        file.delete();

                    FileOutputStream output = new FileOutputStream(file);

                    byte buffer[] = new byte[1024];
                    int dataSize;
                    int loadedSize = 0;
                    while ((dataSize = input.read(buffer)) != -1) {
                        loadedSize += dataSize;
                        publishProgress(String.valueOf((int)((loadedSize*100)/lenghtOfFile)));
                        output.write(buffer, 0, dataSize);
                    }
                    output.close();

                }


            } catch (Exception e) {
                Toast.makeText(newcontext.getApplicationContext(),e.getMessage().toString(),Toast.LENGTH_LONG).show();
            }
            return null;

        }
        protected void onProgressUpdate(String... progress) {
            Log.d("ANDRO_ASYNC",progress[0]);
            if(holder1.filedownloadingprogress.getVisibility()==View.VISIBLE) {
                holder1.filedownloadingprogress.setProgressWithAnimation(Integer.parseInt(progress[0]), 100);
            }else if(holder1.imagedownloadingprogress.getVisibility()==View.VISIBLE){
                holder1.imagedownloadingprogress.setProgressWithAnimation(Integer.parseInt(progress[0]), 100);
            }
        }

        @Override
        protected void onPostExecute(String unused) {
            if(holder1.filedownloadingprogress.getVisibility()==View.VISIBLE) {
                holder1.downloadtext.setVisibility(View.INVISIBLE);
                holder1.filedownloadingprogress.setVisibility(View.GONE);
                holder1.downloadcanceltext.setVisibility(View.INVISIBLE);
            }else if(holder.imagedownloadingprogress.getVisibility()==View.VISIBLE){
                holder1.imagedownloadtext.setVisibility(View.INVISIBLE);
                holder1.imagedownloadingprogress.setVisibility(View.GONE);
                holder1.imagedownloadcanceltext.setVisibility(View.INVISIBLE);
                holder1.downloadbackgroundcolor.setVisibility(View.INVISIBLE);

                Glide.with(newcontext.getApplicationContext()).load(serverpathvalue).listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String s, Target<GlideDrawable> target, boolean b) {
                        Log.i("GLIDE", "onException :", e);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable glideDrawable, String s, Target<GlideDrawable> target, boolean b, boolean b1) {
                        Log.i("GLIDE", "onResourceReady");
                        holder1.iv_bitmap.setVisibility(View.VISIBLE);
                        return false;
                    }
                }).into(holder1.iv_bitmap);
            }
        }
    }

    class DownloadFilesizeAsync extends AsyncTask<String, String, String> {

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

                byte buffer[] = new byte[1024];
                int dataSize;
                int loadedSize = 0;
                while ((dataSize = input.read(buffer)) != -1) {
                    loadedSize += dataSize;
                    publishProgress(String.valueOf((int)((loadedSize*100)/lenghtOfFile)));
                    filesizenewvalue = loadedSize;
                }



            } catch (Exception e) {
                Toast.makeText(newcontext.getApplicationContext(),e.getMessage().toString(),Toast.LENGTH_LONG).show();
            }
            return null;

        }
        protected void onProgressUpdate(String... progress) {

        }

        @Override
        protected void onPostExecute(String unused) {
            if(attachmenttype.equals("Files")) {
                Long tempfilesize = Long.parseLong(String.valueOf(filesizenewvalue));
                String newfilesize = getfilesize(tempfilesize);
                holder.filesizevalue.setText(newfilesize);
            }else {
                Long tempfilesize = Long.parseLong(String.valueOf(filesizenewvalue));
                String newfilesize = getfilesize(tempfilesize);
                holder.imagesizevalue.setText(newfilesize);
            }
        }
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

    private static String getfilesize(long size) {
        if (size <= 0)
            return "0";
        final String[] units = new String[] { "B", "KB", "MB", "GB", "TB" };
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
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

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = { column };
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }
}
