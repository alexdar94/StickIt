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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 3/29/2015.
 */
public class ListViewAdapter extends BaseAdapter {
    private Context mContext;
    LayoutInflater inflater;
    ImageLoader imageLoader;
    private List<Sticker> stickersList=null;


    public ListViewAdapter(Context c,List<Sticker> stickersList){
        mContext=c;
        this.stickersList=stickersList;
        inflater = LayoutInflater.from(c);
        imageLoader = new ImageLoader(c);
    }

    @Override
    public int getCount(){
        return stickersList.size();
    }

    @Override
    public Object getItem(int position){
        return stickersList.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    public class ViewHolder{
        public ImageView img_sticker,img_sticker1,img_sticker2,img_sticker3;
        public TextView stickerName,stickerPrice;

        public ViewHolder(View view){
            stickerName=(TextView)view.findViewById(R.id.sticker_name);
            stickerPrice=(TextView)view.findViewById(R.id.sticker_price);
            img_sticker=(ImageView)view.findViewById(R.id.img_sticker_icon);
            img_sticker1=(ImageView)view.findViewById(R.id.img_sticker1);
            img_sticker2=(ImageView)view.findViewById(R.id.img_sticker2);
            img_sticker3=(ImageView)view.findViewById(R.id.img_sticker3);
        }
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        View v=convertView;
        ViewHolder holder;

        if(v==null){
            v = inflater.inflate(R.layout.listitem_stickers, parent,false);
            holder=new ViewHolder(v);
            v.setTag(holder);
        }else{
            holder=(ViewHolder)v.getTag();
        }

        holder.stickerName.setText(stickersList.get(position).getName());
        holder.stickerPrice.setText(stickersList.get(position).getPrice());
        imageLoader.DisplayImage(stickersList.get(position).getStickerIcon(), holder.img_sticker);
        imageLoader.DisplayImage(stickersList.get(position).getStickers(1), holder.img_sticker1);
        imageLoader.DisplayImage(stickersList.get(position).getStickers(2), holder.img_sticker2);
        imageLoader.DisplayImage(stickersList.get(position).getStickers(3),holder.img_sticker3);

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                View view = inflater.inflate(R.layout.dialog_sticker_detail, null);
                builder.setView(view);

                ImageView icon = (ImageView) view.findViewById(R.id.img_sticker_icon_temp);
                TextView name = (TextView) view.findViewById(R.id.sticker_name_temp);
                final TextView price = (TextView) view.findViewById(R.id.sticker_price_temp);
                Button btn = (Button) view.findViewById(R.id.btn_purchase_sticker);
                GridView gridView = (GridView) view.findViewById(R.id.gridview_stickers);

                imageLoader.DisplayImage(stickersList.get(position).getStickerIcon(), icon);
                name.setText(stickersList.get(position).getName());
                price.setText(stickersList.get(position).getPrice());

                final List<String> stickers = new ArrayList<String>();
                for (int i = 1; i < 4; i++) {
                    stickers.add(stickersList.get(position).getStickers(i));
                }

                gridView.setAdapter(new DialogStickersAdapter(mContext, stickers));

                builder.create();
                builder.show();

                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String filePath = mContext.getResources().getString(R.string.downloaded_sticker);
                        if (price.getText().equals("FREE")) {
                            for (int i = 0; i < 4; i++) {
                                new DownloadTask(mContext, filePath, stickersList.get(position).getStickers(i), stickersList.get(position).getName() + i).execute();
                            }
                        } else {
                            int x = Integer.parseInt(price.getText().toString());
                            SharedPreferences sharedPref = mContext.getSharedPreferences(mContext.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                            int savedCoin = sharedPref.getInt(mContext.getString(R.string.saved_coin), Context.MODE_PRIVATE);

                            if (savedCoin - x >= 0) {
                                for (int i = 0; i < 4; i++) {
                                    new DownloadTask(mContext, filePath, stickersList.get(position).getStickers(i), stickersList.get(position).getName() + i).execute();
                                }
                                savedCoin -= x;
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putInt(mContext.getString(R.string.saved_coin), savedCoin);
                                editor.commit();
                            } else {
                                Toast.makeText(mContext, "Not enough coin", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(mContext, CoinActivity.class);
                                mContext.startActivity(intent);
                            }
                        }
                        //String filePath = mContext.getResources().getString(R.string.downloaded_sticker) + File.separator + stickersList.get(position).getName();
                        /*for (int i=0;i<stickers.size();i++) {
                            new DownloadTask(mContext, filePath, stickers.get(i),"sticker"+i).execute();
                        }*/
                        notifyDataSetChanged();
                    }
                });


            }
        });
        return v;
    }


}
