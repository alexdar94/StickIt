package com.now.stickit;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EditMemoActivity extends Activity {
    private DrawingView drawingView;
    private float smallBrush, mediumBrush, largeBrush;
    private ImageButton currentPaint;
    private RelativeLayout editMemoViewParent;
    private EditText mEditText;
    private ArrayList<ImageView> imageViews=new ArrayList<ImageView>();
    private boolean editTextOnTouch,dialogShown;
    private boolean editTextCreated=false;
    private PopupWindow pw;
    private static final int PICK_IMAGE = 1;
    private static final int PICK_STICKER = 2;
    private static final int CAMERA_PIC_REQUEST = 1337;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_memo);

        String filePath=getIntent().getExtras().getString("filePaths");
        Bitmap immutableBmp= BitmapFactory.decodeFile(filePath);
        Bitmap myBitmap=immutableBmp.copy(Bitmap.Config.ARGB_8888, true);
        Drawable d = new BitmapDrawable(getResources(), myBitmap);
        final ActionBar mActionBar = getActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);

        drawingView=(DrawingView)findViewById(R.id.drawing);
        editMemoViewParent=(RelativeLayout)findViewById(R.id.edit_memo_view_parent);
        if (android.os.Build.VERSION.SDK_INT >= 16){
            drawingView.setBackground(d);
        }
        else{
            drawingView.setBackgroundDrawable(d);
        }
    }

    public void addText(View view){
        drawingView.drawMode(false);
        editTextOnTouch=true;
        Toast.makeText(getApplicationContext(), "Tap to add text box", Toast.LENGTH_SHORT).show();
        if(editTextCreated==true){
            mEditText.setFocusableInTouchMode(true);
            mEditText.setOnTouchListener(viewTouchListener);
            mEditText.setOnDragListener(viewDragListener);
        }
        editMemoViewParent.setOnTouchListener(addETListener);
        drawingView.setOnTouchListener(addETListener);
    }

    public void draw(View view){
        smallBrush = getResources().getInteger(R.integer.small_size);
        mediumBrush = getResources().getInteger(R.integer.medium_size);
        largeBrush = getResources().getInteger(R.integer.large_size);

        ImageButton btn=(ImageButton)findViewById(R.id.draw);
        int[] location = new int[2];
        btn.getLocationOnScreen(location);
        LayoutInflater lf = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View ll=(View)lf.inflate(R.layout.popup_pen,null);
        if(pw==null){
            pw= new PopupWindow(this);
            pw.setFocusable(true);
            pw.setOutsideTouchable(true);
            pw.setWidth((int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, getResources().getDisplayMetrics()));
            pw.setHeight((int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 90, getResources().getDisplayMetrics()));
            //pw.setContentView(lf.inflate(R.layout.popup_pen,null));
            pw.setContentView(ll);
            pw.setBackgroundDrawable(new BitmapDrawable());
            LinearLayout colorPalette=(LinearLayout)ll.findViewById(R.id.color_palette);
            currentPaint=(ImageButton)colorPalette.getChildAt(0);
            currentPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));
            pw.showAtLocation(view, Gravity.NO_GRAVITY, location[0], location[1]-150);
        }

        if (pw.isShowing()) {
            pw.dismiss();
            Log.e("is showing","dismiss");
        } else {
            pw.showAtLocation(view, Gravity.NO_GRAVITY, location[0], location[1]-150);
            Log.e("show","");
        }

        drawingView.drawMode(true,false);
        if(editTextCreated == true){
            mEditText.setFocusable(false);
            mEditText.setOnTouchListener(null);
            mEditText.setOnClickListener(null);
            mEditText.setOnDragListener(null);
        }

    }

    public void erase(View view){
        drawingView.drawMode(true,true);
        if(editTextCreated==true){
            mEditText.setFocusable(false);
            mEditText.setOnTouchListener(null);
            mEditText.setOnClickListener(null);
            mEditText.setOnDragListener(null);
        }
    }

    public void addImage(View view){
        drawingView.drawMode(false);
        editTextOnTouch=true;
        if(editTextCreated==true){
            mEditText.setFocusable(false);
            mEditText.setOnTouchListener(null);
            mEditText.setOnClickListener(null);
            mEditText.setOnDragListener(null);
        }

        ImageButton addImageButton=(ImageButton)findViewById(R.id.addImage);
        PopupMenu popupMenu = new PopupMenu(getApplicationContext(),addImageButton);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu,popupMenu.getMenu());
        Object menuHelper;
        Class[] argTypes;

        try {
            Field fMenuHelper = PopupMenu.class.getDeclaredField("mPopup");
            fMenuHelper.setAccessible(true);
            menuHelper = fMenuHelper.get(popupMenu);
            argTypes = new Class[] { boolean.class };
            menuHelper.getClass().getDeclaredMethod("setForceShowIcon", argTypes).invoke(menuHelper, true);
        } catch (Exception e) {
            popupMenu.show();
        }

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.add_gallery:
                        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                        galleryIntent.setType("image/*");
                        startActivityForResult(galleryIntent, PICK_IMAGE);
                        break;
                    case R.id.add_sticker:
                        File directory = getApplicationContext().getDir(getResources().getString(R.string.downloaded_sticker), Context.MODE_PRIVATE);
                        if (directory.list().length <= 0) {
                            Intent intent=new Intent(getApplicationContext(),ShopActivity.class);
                            startActivity(intent);
                        } else {
                            Intent i=new Intent(getApplicationContext(),ChooseStickerActivity.class);
                            startActivityForResult(i,PICK_STICKER);
                        }
                        break;
                    case R.id.capture:
                        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
                        break;
                }
                return true;
            }
        });
        popupMenu.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            switch(requestCode){
                case PICK_IMAGE:
                    Uri pickedImage = data.getData();
                    try {
                        addImage(getBitmapFromUri(pickedImage));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;

                case CAMERA_PIC_REQUEST:
                    addImage((Bitmap) data.getExtras().get("data"));
                    break;

                case PICK_STICKER:
                    addImage(BitmapFactory.decodeFile(data.getExtras().get("data").toString()));
                    break;
            }

        }
    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }

    public void undo(View view){
        drawingView.undo();
    }

    protected View.OnTouchListener addETListener =new View.OnTouchListener(){
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            float x=event.getX();
            float y=event.getY();
            if(editTextCreated==false) {
                if(event.getAction()==MotionEvent.ACTION_DOWN){
                    addEditText(x,y);
                }
            }else return false;
            return true;
        }
    };


    private void addEditText(float x,float y){
        RelativeLayout.LayoutParams editTextParams= new RelativeLayout.LayoutParams((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 150, getResources().getDisplayMetrics())
                ,(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 150, getResources().getDisplayMetrics()));
        //RelativeLayout.LayoutParams editTextParams= new RelativeLayout.LayoutParams(300,300);
        editTextParams.leftMargin=(int)x-(editTextParams.width/2);
        editTextParams.topMargin=(int)y-(editTextParams.height/2);

        mEditText=new EditText(this);
        mEditText.setHint("Enter note");
        mEditText.setInputType(InputType.TYPE_CLASS_TEXT);
        mEditText.setBackgroundResource(R.drawable.edittext_shape);
        mEditText.setGravity(Gravity.NO_GRAVITY);
        editMemoViewParent.addView(mEditText, editTextParams);

        InputMethodManager inputMethodManager=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInputFromWindow(editMemoViewParent.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);

        mEditText.setFocusableInTouchMode(true);
        mEditText.setOnTouchListener(viewTouchListener);
        editMemoViewParent.setOnDragListener(viewDragListener);
        editTextCreated=true;
    }

    protected View.OnTouchListener viewTouchListener=new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent event) {
            if(editTextOnTouch==true){
                int x = (int) event.getX();
                int y = (int) event.getY();
                int width = view.getLayoutParams().width;
                int height = view.getLayoutParams().height;

                if (((x - width <= 40 && x - width > 0) || (width - x <= 40 && width - x > 0))&&(((y - height <= 40 && y - height > 0) || (height - y <= 40 && height - y > 0))) ){
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            break;
                        case MotionEvent.ACTION_MOVE:
                            Log.e(">>", "width:" + width + " height:" + height + " x:" + x + " y:" + y);
                            view.getLayoutParams().width = x;
                            view.getLayoutParams().height = y;
                            view.requestLayout();
                            break;
                        case MotionEvent.ACTION_UP:
                            break;
                    }
                }else if(x<=40 && y<=40){
                    if(dialogShown){

                    }else{
                        dialogShown=true;
                        final AlertDialog.Builder builder = new AlertDialog.Builder(EditMemoActivity.this);
                        View builderView = LayoutInflater.from(EditMemoActivity.this).inflate(R.layout.dialog_edit_text, null);
                        builder.setView(builderView);
                        final TextView myView = new TextView(getApplicationContext());
                        myView.setText("Text properties");
                        myView.setTextSize(22);
                        final Spinner spinner_font=(Spinner)builderView.findViewById(R.id.spinner_font);
                        final Spinner spinner_size=(Spinner)builderView.findViewById(R.id.spinner_size);
                        addItemsOnSpinner(spinner_font);
                        builder.setCustomTitle(myView)
                                .setPositiveButton("Cancel", new DialogInterface.OnClickListener()
                                {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialogShown=false;
                                    }
                                })
                                .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialogShown = false;
                                        mEditText.setTextSize(TypedValue.COMPLEX_UNIT_SP, Integer.parseInt(spinner_size.getSelectedItem().toString()));
                                        String font = spinner_font.getSelectedItem().toString();
                                        if (font.equals("Default")) {
                                            mEditText.setTypeface(Typeface.SANS_SERIF);
                                        } else if (font.equals("More fonts..")) {

                                        } else {
                                            Typeface externalFont = Typeface.createFromAsset(getAssets(), "fonts/" + font + ".ttf");
                                            mEditText.setTypeface(externalFont);
                                        }
                                    }
                                }).show();
                    }
                }else if(Math.abs(x-width)<=40 && y<=40){
                    editMemoViewParent.removeView(mEditText);
                    editTextCreated=false;
                    Toast.makeText(getApplicationContext(),"Tap to add text box", Toast.LENGTH_SHORT).show();
                    editMemoViewParent.setOnTouchListener(addETListener);
                }else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    ClipData data = ClipData.newPlainText("", "");
                    View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                    view.startDrag(data, shadowBuilder, view, 0);
                    view.setVisibility(View.INVISIBLE);
                    ViewGroup.MarginLayoutParams vlp=(ViewGroup.MarginLayoutParams)view.getLayoutParams();
                    float m=view.getX();
                    float n=view.getLeft();
                    float o=vlp.leftMargin;
                    Log.e("", "getX: "+m + "getLeft: " + n+"leftMargin: "+o+" ");
                } else return false;
            }else{
                return false;
            }
            return false;
        }
    };

    protected View.OnDragListener viewDragListener=new View.OnDragListener() {
        @Override
        public boolean onDrag(View v, DragEvent event) {
            float x = event.getX();
            float y = event.getY();
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    //no action necessary
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    //no action necessary
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    //no action necessary
                    break;
                case DragEvent.ACTION_DROP:
                    View view = (View) event.getLocalState();
                    view.setX(x - (view.getWidth() / 2));
                    view.setY(y - (view.getHeight() / 2));
                    view.setVisibility(View.VISIBLE);
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    //no action necessary
                    break;
                default:
                    break;
            }
            return true;
        }
    };

    public void addItemsOnSpinner(Spinner x){
        Spinner spinner=x;
        final List<String> list=new ArrayList<String>();
        list.add("Default");
        list.add("Chantelli Antiqua");
        list.add("TFIOS");
        list.add("More fonts..");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item,list){

            public View getView(final int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTextColor(Color.BLACK);
                String font= list.get(position);
                if(font.equals("Default")){

                }else if(font.equals("More fonts..")){

                }else{
                    Typeface externalFont = Typeface.createFromAsset(getAssets(), "fonts/"+font+".ttf");
                    ((TextView) v).setTextColor(Color.BLACK);
                    ((TextView) v).setTypeface(externalFont);}
                return v;
            }
        };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void addImage(Bitmap bitmap){
        RelativeLayout.LayoutParams imageParams= new RelativeLayout.LayoutParams((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, getResources().getDisplayMetrics())
                ,(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, getResources().getDisplayMetrics()));
        ImageView mImageView=new ImageView(this);
        mImageView.setImageBitmap(bitmap);
        editMemoViewParent.addView(mImageView, imageParams);
        imageViews.add(mImageView);
        mImageView.setOnTouchListener(viewTouchListener);
        editMemoViewParent.setOnDragListener(viewDragListener);
    }

    public void paintClicked(View view){
        if(view!=currentPaint){
            ImageButton imgView = (ImageButton)view;
            String color = view.getTag().toString();
            drawingView.setColor(color);

            imgView.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));
            currentPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint));
            currentPaint=(ImageButton)view;
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Save")
                .setMessage("Memo is not saved yet...")
                .setPositiveButton("Discard", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent= new Intent(getApplicationContext(),NoteListActivity.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Save", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        saveMemo();
                    }
                })
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_memo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                new AlertDialog.Builder(this)
                        .setTitle("Save")
                        .setMessage("Memo is not saved yet...")
                        .setPositiveButton("Discard", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent upIntent = new Intent(getApplicationContext(), NoteListActivity.class);
                                startActivity(upIntent);
                            }
                        })
                        .setNegativeButton("Save", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                saveMemo();
                            }
                        })
                        .show();
            case R.id.action_settings:
                return true;
            case R.id.action_shop:
                Intent i = new Intent(this, ShopActivity.class);
                startActivity(i);
                return true;
            case R.id.action_save:
                Toast.makeText(getApplicationContext(), "Saving memo...", Toast.LENGTH_SHORT).show();
                saveMemo();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void saveMemo(){
        if(editTextCreated==true){
            mEditText.setFocusableInTouchMode(false);
            mEditText.setOnTouchListener(null);
            mEditText.setOnClickListener(null);
            mEditText.setOnDragListener(null);
        }
        editMemoViewParent.buildDrawingCache();
        final Bitmap bitmap=editMemoViewParent.getDrawingCache();

        Runnable r = new Runnable()
        {
            @Override
            public void run()
            {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
                Date now = new Date();
                String fileName = formatter.format(now).toString() + ".png";

                File directory = getApplicationContext().getDir(getResources().getString(R.string.my_memo), Context.MODE_PRIVATE);
                File savedFile= new File(directory,fileName);

                try {
                    FileOutputStream out = new FileOutputStream(savedFile);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                    Log.e("saving",directory+fileName);
                    out.flush();
                    out.close();
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        Thread t = new Thread(r);
        t.start();
        Intent intent= new Intent(this, NoteListActivity.class);
        startActivity(intent);
    }
}
