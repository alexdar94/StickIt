package com.now.stickit;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 3/29/2015.
 */
public class Sticker {
    private String name,price, sticker_icon;
    private List<String> stickers= new ArrayList<String>();
    //setter
    public void setName(String name){
        this.name=name;
    }

    public void setPrice(String price){this.price=price;}

    public void setStickers(List<String> stickers){
        this.stickers=stickers;
    }

    //getter
    public String getName(){return this.name;}

    public String getPrice() {return this.price;}

    public String getStickerIcon(){return this.stickers.get(0);}

    public String getStickers(int position){return stickers.get(position);}
}
