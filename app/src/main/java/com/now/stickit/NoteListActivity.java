package com.now.stickit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class NoteListActivity extends Activity {
    private List<String> filePaths;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);

        GridView gridview = (GridView) findViewById(R.id.mynotes);
        TextView textview=(TextView)findViewById(R.id.no_note);
        File directory = getApplicationContext().getDir(getResources().getString(R.string.my_memo), Context.MODE_PRIVATE);
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
            gridview.setAdapter(new NoteListAdapter(this,filePaths,1));
            gridview.setVisibility(View.VISIBLE);
        }

            ImageButton editImageButton = (ImageButton) findViewById(R.id.eat_fab_button);
            editImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent ii = new Intent(getApplicationContext(), SelectNoteActivity.class);
            startActivity(ii);
            }});
    }

    @Override
    public void onBackPressed() {
        finish();
        moveTaskToBack(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_note_list, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
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
