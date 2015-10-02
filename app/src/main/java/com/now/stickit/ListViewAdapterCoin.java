package com.now.stickit;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 5/28/2015.
 */
public class ListViewAdapterCoin extends BaseAdapter{
    private Context mContext;
    private LayoutInflater inflater;
    private List<String> coinsList= new ArrayList<String>();
    private List<String> priceList= new ArrayList<String>();
    private TextView coinAmount;

    public ListViewAdapterCoin(Context c,TextView textView){
        mContext=c;
        inflater = LayoutInflater.from(c);
        coinsList.add("50");coinsList.add("100");coinsList.add("150");
        priceList.add("$0.99");priceList.add("$1.99");priceList.add("$2.99");
        coinAmount=textView;
    }

    @Override
    public int getCount(){
        return coinsList.size();
    }

    @Override
    public Object getItem(int position){
        return coinsList.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    public class ViewHolder{
        public TextView coin;
        public Button coin_btn;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        View v=convertView;
        ViewHolder holder;

        if(v==null){
            v = inflater.inflate(R.layout.listitem_coin, parent,false);

            holder=new ViewHolder();
            holder.coin=(TextView)v.findViewById(R.id.coin_value);
            holder.coin_btn=(Button)v.findViewById(R.id.coin_price);

            v.setTag(holder);
        }else{
            holder=(ViewHolder)v.getTag();
        }

        holder.coin.setText(coinsList.get(position));
        holder.coin_btn.setText(priceList.get(position));

        holder.coin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                SharedPreferences sharedPref = mContext.getSharedPreferences(
                        mContext.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                int coin = Integer.parseInt(coinsList.get(position));
                int savedCoin = sharedPref.getInt(mContext.getString(R.string.saved_coin),Context.MODE_PRIVATE);
                savedCoin+=coin;

                coinAmount.setText(savedCoin+"");
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt(mContext.getString(R.string.saved_coin), savedCoin);
                editor.commit();
            }
        });

        return v;
    }


}
