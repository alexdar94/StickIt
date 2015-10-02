package com.now.stickit;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.GridView;
import android.widget.ListView;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 4/30/2015.
 */
public class RemoteDataTask extends AsyncTask<Void, Void, Void> {
    List<ParseObject> ob;
    private List<Note> notesList = null;
    private List<Font> fontsList = null;
    private List<Sticker> stickersList=null;
    GridView gridview;
    ListView listview;
    Context mContext;
    private int TAG=-1;

    public RemoteDataTask(Context context, GridView gridview, int TAG){
        mContext=context;
        this.gridview=gridview;
        this.TAG=TAG;
    }

    public RemoteDataTask(Context context, ListView listView, int TAG){
        mContext=context;
        this.listview=listView;
        this.TAG=TAG;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... params) {
        switch(TAG){
            case 1: try {
                notesList = new ArrayList<Note>();
                // Locate the class table named "SamsungPhones" in Parse.com
                ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
                        "SamsungPhones");
                // Locate the column named "position" in Parse.com and order list
                // by ascending
                query.orderByAscending("position");
                ob = query.find();
                for (ParseObject item : ob) {
                    ParseFile image = (ParseFile) item.get("phones");
                    Note note = new Note();
                    note.setNote(image.getUrl());
                    note.setName(item.get("name").toString());
                    note.setPrice(item.get("Price").toString());
                    notesList.add(note);
                }
            } catch (ParseException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            break;
            case 2: try {
                fontsList = new ArrayList<Font>();
                // Locate the class table named "Fonts" in Parse.com
                ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
                        "Fonts");
                // Locate the column named "position" in Parse.com and order list
                // by ascending
                query.orderByAscending("position");
                ob = query.find();
                for (ParseObject item : ob) {
                    ParseFile image = (ParseFile) item.get("font_icon");
                    ParseFile fontFile = (ParseFile) item.get("font");
                    Font font = new Font();
                    font.setIcon(image.getUrl());
                    font.setFont(fontFile.getUrl());
                    font.setName(item.get("name").toString());
                    font.setPrice(item.get("price").toString());
                    fontsList.add(font);
                }
            } catch (ParseException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            break;
            case 3:  try {
                stickersList = new ArrayList<Sticker>();
                List<String> stickers= new ArrayList<String>();
                // Locate the class table named "Stickers" in Parse.com
                ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
                        "Stickers");
                // Locate the column named "position" in Parse.com and order list
                // by ascending
                query.orderByAscending("position");
                ob = query.find();
                for (ParseObject item : ob) {
                    ParseFile sticker_icon = (ParseFile) item.get("sticker_icon");
                    //ParseFile sticker1 = (ParseFile) item.get("sticker1");
                    stickers.add(sticker_icon.getUrl());
                    for(int i=1;i<4;i++){stickers.add(((ParseFile) item.get("sticker"+i)).getUrl());}
                    Sticker sticker = new Sticker();
                    sticker.setStickers(stickers);
                    sticker.setName(item.get("name").toString());
                    sticker.setPrice(item.get("price").toString());
                    stickersList.add(sticker);
                }
            } catch (ParseException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
                break;
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        switch(TAG){
            case 1: gridview.setAdapter(new ShopItemsAdapter(mContext,notesList,TAG));
                break;
            case 2: gridview.setAdapter(new ShopItemsAdapter(mContext,fontsList,TAG,1));
                break;
            case 3: listview.setAdapter(new ListViewAdapter(mContext, stickersList));
                break;
        }
    }
}
