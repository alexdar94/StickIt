package com.now.stickit;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by User on 5/5/2015.
 */
public class DownloadTask extends AsyncTask<Void, Void, Void> {
    private Context mContext;
    private String filepath;
    private String fileName;
    private String mUrl="";

    public DownloadTask(Context c, String filepath, String url, String fileName){
        this.mContext=c;
        this.mUrl=url;
        this.filepath=filepath;
        this.fileName=fileName+".png";
        Log.e("asda",filepath);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //btn=(Button)((Activity)mContext).findViewById(R.id.btn_purchase);
       // btn.setText("Downloading...");
        Toast.makeText(mContext,"Downloading",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            File directory = mContext.getDir(filepath, Context.MODE_PRIVATE);
            File downloadFile=new File(directory,fileName);

            URL url = new URL(mUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            Bitmap bm = BitmapFactory.decodeStream(is);
            FileOutputStream fos = new FileOutputStream(downloadFile);
            ByteArrayOutputStream outstream = new ByteArrayOutputStream();

            bm.compress(Bitmap.CompressFormat.PNG, 100, outstream);
            byte[] byteArray = outstream.toByteArray();

            fos.write(byteArray);
            fos.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        //btn.setText("Downloaded");
        Toast.makeText(mContext,"Download completed.",Toast.LENGTH_SHORT).show();
    }
}
