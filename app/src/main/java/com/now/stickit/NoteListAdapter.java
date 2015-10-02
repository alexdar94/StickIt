package com.now.stickit;

import android.app.Activity;
import android.app.AlertDialog;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RemoteViews;

import java.io.File;
import java.util.List;

/**
 * Created by User on 3/2/2015.
 */
public class NoteListAdapter extends BaseAdapter {
    private Context mContext;
    private List<String> filePaths=null;
    private int TAG=-1;


    public NoteListAdapter(Context c, List<String> filePaths,int TAG) {
        mContext = c;
        this.filePaths=filePaths;
        this.TAG=TAG;
    }

    public int getCount() {return filePaths.size();}

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(final int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(mContext);

            imageView.setLayoutParams(new GridView.LayoutParams(
                    (int)(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 85, mContext.getResources().getDisplayMetrics())),
                    (int)(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 85, mContext.getResources().getDisplayMetrics()))));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inSampleSize = 3;
        imageView.setImageBitmap(BitmapFactory.decodeFile(filePaths.get(position),opt));
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (TAG == 1) {
                                final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                                View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_memo_detail, null);
                                builder.setView(view);

                                ImageView img = (ImageView) view.findViewById(R.id.memo);
                                Button btn = (Button) view.findViewById(R.id.delete);

                                img.setImageBitmap(BitmapFactory.decodeFile(filePaths.get(position)));
                                final AlertDialog ad = builder.create();
                                ad.show();
                                btn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        File file = new File(filePaths.get(position));
                                        filePaths.remove(position);
                                        boolean deleted = file.delete();
                                        notifyDataSetChanged();
                                        ad.cancel();
                                    }
                                });
                            } else if (TAG == 2) {
                                Intent intent = new Intent(mContext, EditMemoActivity.class);
                                intent.putExtra("filePaths", filePaths.get(position));
                                mContext.startActivity(intent);
                            } else if (TAG == 3) {
                                Activity a = (Activity) mContext;
                                Intent intent = a.getIntent();
                                Bundle extras = intent.getExtras();
                                int mAppWidgetId=-1;
                                if (extras != null) {
                                    mAppWidgetId = extras.getInt(
                                            AppWidgetManager.EXTRA_APPWIDGET_ID,
                                            AppWidgetManager.INVALID_APPWIDGET_ID);
                                }

                                final RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.appwidget_layout);
                                remoteViews.setImageViewBitmap(R.id.img_widget, BitmapFactory.decodeFile(filePaths.get(position)));
                                remoteViews.setOnClickPendingIntent(R.id.widget, AppWidgetProvider.pendingIntent(a));
                                AppWidgetProvider.pushWidgetUpdate(a,mAppWidgetId, remoteViews);

                                Intent resultValue = new Intent();
                                resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
                                a.setResult(a.RESULT_OK, resultValue);
                                a.finish();
                            } else if(TAG == 4){
                                Intent resultValue = new Intent();
                                resultValue.putExtra("data",filePaths.get(position));
                                Activity a = (Activity) mContext;
                                a.setResult(a.RESULT_OK, resultValue);
                                a.finish();
                            }
                        }
                    });
        return imageView;
    }

}

