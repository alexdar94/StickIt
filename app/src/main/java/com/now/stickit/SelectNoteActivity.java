package com.now.stickit;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class SelectNoteActivity extends Activity {
    private List<String> filePaths;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_note);

        final ActionBar mActionBar = getActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);

        GridView gridview = (GridView) findViewById(R.id.downloaded_notes);
        TextView textview=(TextView)findViewById(R.id.no_downloaded_note);
        File directory = getApplicationContext().getDir(getResources().getString(R.string.downloaded_note), Context.MODE_PRIVATE);
        if (directory.list().length <= 0) {
            textview.setVisibility(View.VISIBLE);
            gridview.setVisibility(View.INVISIBLE);
        } else {
            textview.setVisibility(View.INVISIBLE);
            gridview.setVisibility(View.VISIBLE);
            File[] files = directory.listFiles();
            filePaths=new ArrayList<String>();
            for (File file : files) {
                filePaths.add(file.getAbsolutePath());
            }
            gridview.setAdapter(new NoteListAdapter(this,filePaths,2));
            gridview.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_select_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                Intent upIntent = new Intent(this, NoteListActivity.class);
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
            case R.id.action_shop:
                Intent i=new Intent(this,ShopActivity.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
