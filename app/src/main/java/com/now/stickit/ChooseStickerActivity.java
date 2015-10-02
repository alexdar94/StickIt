package com.now.stickit;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class ChooseStickerActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_sticker);

        File directory = getApplicationContext().getDir(getResources().getString(R.string.downloaded_sticker), Context.MODE_PRIVATE);
        List<String> filePaths = new ArrayList<String>();
        for(File file:directory.listFiles()){
            filePaths.add(file.getAbsolutePath());
        }
        /*for (File file : directory.listFiles()) {
            for(File file1:file.listFiles()){
                filePaths.add(file1.getAbsolutePath());
            }
        }*/
        GridView gridView=(GridView)findViewById(R.id.sticker_selection);
        gridView.setAdapter(new NoteListAdapter(this,filePaths,4));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_choose_sticker, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
