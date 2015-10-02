package com.now.stickit;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by User on 5/8/2015.
 */
public class DialogStickersAdapter extends BaseAdapter {
    private Context mContext;
    private List<String> stickers=null;
    ImageLoader imageLoader;

    public DialogStickersAdapter(Context c,List<String> stickers) {
        mContext = c; this.stickers=stickers; imageLoader = new ImageLoader(c);
    }

    public int getCount() {return stickers.size();}

    public Object getItem(int position) {
        return stickers.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public class ViewHolder {ImageView imageView;}

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        final ImageView imageView;
        if (view == null) {
            holder = new ViewHolder();
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(GridView.LayoutParams.MATCH_PARENT, GridView.LayoutParams.MATCH_PARENT));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
            view=imageView;
            holder.imageView = imageView;
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
            imageView = (ImageView) view;
        }

        imageLoader.DisplayImage(stickers.get(position), holder.imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return imageView;
    }
}
