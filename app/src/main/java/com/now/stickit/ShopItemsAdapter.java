package com.now.stickit;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by User on 4/30/2015.
 */
public class ShopItemsAdapter extends BaseAdapter {
    private Context mContext;
    private List<Note> notesList=null;
    private List<Font> fontsList=null;
    LayoutInflater inflater;
    ImageLoader imageLoader;
    private int TAG;

    public ShopItemsAdapter(Context context, List<Note> notesList, int TAG) {
        mContext = context;
        this.notesList = notesList;
        inflater = LayoutInflater.from(context);
        imageLoader = new ImageLoader(context);
        this.TAG=TAG;
    }

    public ShopItemsAdapter(Context context, List<Font> fontsList, int TAG, int m) {
        mContext = context;
        this.fontsList = fontsList;
        inflater = LayoutInflater.from(context);
        imageLoader = new ImageLoader(context);
        this.TAG=TAG;
    }

    public class ViewHolder {ImageView imageView;}

    @Override
    public int getCount() {
        switch (TAG){
            case 1: return notesList.size();
            case 2: return fontsList.size();
            default: return -1;
        }
    }

    @Override
    public Object getItem(int position) {
        switch (TAG){
            case 1: return notesList.get(position);
            case 2: return fontsList.get(position);
            default: return -1;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

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

        switch (TAG){
            case 1: imageLoader.DisplayImage(notesList.get(position).getNote(),
                    holder.imageView);
                break;
            case 2: imageLoader.DisplayImage(fontsList.get(position).getIcon(),
                    holder.imageView);
                break;
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

                View view = inflater.inflate(R.layout.dialog_shop_item_detail,null);
                builder.setView(view);

                ImageView img=(ImageView)view.findViewById(R.id.item_view);
                TextView name=(TextView)view.findViewById(R.id.item_name);
                final TextView price=(TextView)view.findViewById(R.id.item_price);
                Button purchase=(Button)view.findViewById(R.id.btn_purchase);

                switch(TAG){
                    case 1:imageLoader.DisplayImage(notesList.get(position).getNote(),img);
                            name.setText(notesList.get(position).getName());
                            price.setText(notesList.get(position).getPrice());
                        break;
                    case 2:imageLoader.DisplayImage(fontsList.get(position).getIcon(),img);
                            name.setText(fontsList.get(position).getName());
                            price.setText(fontsList.get(position).getPrice());
                        break;
                }

                builder.create();
                builder.show();

                purchase.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (price.getText().equals("FREE")){
                            switch (TAG) {
                                case 1:
                                    new DownloadTask(mContext, mContext.getResources().getString(R.string.downloaded_note), notesList.get(position).getNote(), notesList.get(position).getName()).execute();
                                    notifyDataSetChanged();
                                    break;
                                case 2:
                                    new DownloadTask(mContext, mContext.getResources().getString(R.string.downloaded_font), fontsList.get(position).getFont(), fontsList.get(position).getName()).execute();
                                    notifyDataSetChanged();
                                    break;
                            }
                        }else{
                            int x=Integer.parseInt(price.getText().toString());
                            SharedPreferences sharedPref = mContext.getSharedPreferences(
                                    mContext.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                            int savedCoin = sharedPref.getInt(mContext.getString(R.string.saved_coin),Context.MODE_PRIVATE);

                            if(savedCoin-x>=0){
                                    switch (TAG) {
                                        case 1:
                                            new DownloadTask(mContext, mContext.getResources().getString(R.string.downloaded_note), notesList.get(position).getNote(), notesList.get(position).getName()).execute();
                                            notifyDataSetChanged();
                                            break;
                                        case 2:
                                            new DownloadTask(mContext, mContext.getResources().getString(R.string.downloaded_font), fontsList.get(position).getFont(), fontsList.get(position).getName()).execute();
                                            notifyDataSetChanged();
                                            break;
                                    }
                                savedCoin-=x;
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putInt(mContext.getString(R.string.saved_coin), savedCoin);
                                editor.commit();
                            }else{
                                Toast.makeText(mContext,"Not enough coin",Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(mContext,CoinActivity.class);
                                mContext.startActivity(intent);
                            }
                        }

                    }
                });
            }
        });
        return view;
    }

}
