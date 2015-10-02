package com.now.stickit;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;


public class CoinActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin);

        final ActionBar mActionBar = getActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);

        ListView listView=(ListView)findViewById(R.id.listView_coin);
        TextView coinAmount=(TextView)findViewById(R.id.coin_amount);
        listView.setAdapter(new ListViewAdapterCoin(this,coinAmount));


        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        int savedCoin = sharedPref.getInt(getString(R.string.saved_coin), Context.MODE_PRIVATE);
        coinAmount.setText(savedCoin+"");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_coin, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent upIntent = new Intent(this, ShopActivity.class);
                //startActivity(upIntent);
                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                    TaskStackBuilder.from(this)
                            .addNextIntent(upIntent)
                            .startActivities();
                    finish();
                } else {
                    NavUtils.navigateUpTo(this, upIntent);
                }
            case R.id.action_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
