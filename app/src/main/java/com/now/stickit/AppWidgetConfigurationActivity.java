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


public class AppWidgetConfigurationActivity extends Activity {
    private List<String> filePaths;
    private int mAppWidgetId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setResult(RESULT_CANCELED);
        setContentView(R.layout.activity_app_widget_configuration);

        GridView gridview = (GridView) findViewById(R.id.config_mynotes);
        File directory = getApplicationContext().getDir(getResources().getString(R.string.my_memo), Context.MODE_PRIVATE);

        File[] files = directory.listFiles();
        filePaths=new ArrayList<String>();
        for (File file : files) {
            filePaths.add(file.getAbsolutePath());
        }
        gridview.setAdapter(new NoteListAdapter(this, filePaths, 3));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_app_widget_configuration, menu);
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
