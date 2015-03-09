package com.buu.se.duanrestaurant;

import android.app.Activity;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class MainActivity extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        Log.d("TEST", position+"");
//        View decorView = getWindow().getDecorView();
//        int uiOptions;
        switch (position) {
            case 0:
//                uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                        | View.SYSTEM_UI_FLAG_FULLSCREEN;
//                decorView.setSystemUiVisibility(uiOptions);
                fragmentManager.beginTransaction()
                        .replace(R.id.container, ProfileFragment.newInstance(position))
                        .commit();
                break;
            case 1:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, TableFragment.newInstance(position))
                        .commit();
                break;
            case 2:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, OrderFragment.newInstance(position))
                        .commit();
                break;
            case 3:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, TipFragment.newInstance(position))
                        .commit();
                break;
            case 6:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, TableOrder.newInstance(position))
                        .commit();
                break;
            case 7:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, ReserveTable.newInstance(position))
                        .commit();
                break;
            default:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, PlaceholderFragment.newInstance(position))
                        .commit();
                break;
        }

    }

//    public class RoundedImageView extends ImageView {
//
//        public RoundedImageView(Context context) {
//            super(context);
//        }
//
//        public RoundedImageView(Context context, AttributeSet attrs) {
//            super(context, attrs);
//        }
//
//        public RoundedImageView(Context context, AttributeSet attrs, int defStyle) {
//            super(context, attrs, defStyle);
//        }
//
//        @Override
//        protected void onDraw(Canvas canvas) {
//
//            Drawable drawable = getDrawable();
//
//            if (drawable == null) {
//                return;
//            }
//
//            if (getWidth() == 0 || getHeight() == 0) {
//                return;
//            }
//            Bitmap b =  ((BitmapDrawable)drawable).getBitmap() ;
//            Bitmap bitmap = b.copy(Bitmap.Config.ARGB_8888, true);
//
//            int w = getWidth(), h = getHeight();
//
//
//            Bitmap roundBitmap =  getCroppedBitmap(bitmap, w);
//            canvas.drawBitmap(roundBitmap, 0,0, null);
//
//        }
//        public Bitmap getCroppedBitmap(Bitmap bmp, int radius) {
//            Bitmap sbmp;
//            if(bmp.getWidth() != radius || bmp.getHeight() != radius)
//                sbmp = Bitmap.createScaledBitmap(bmp, radius, radius, false);
//            else
//                sbmp = bmp;
//            Bitmap output = Bitmap.createBitmap(sbmp.getWidth(),
//                    sbmp.getHeight(), Bitmap.Config.ARGB_8888);
//            Canvas canvas = new Canvas(output);
//
//            final int color = 0xffa19774;
//            final Paint paint = new Paint();
//            final Rect rect = new Rect(0, 0, sbmp.getWidth(), sbmp.getHeight());
//
//            paint.setAntiAlias(true);
//            paint.setFilterBitmap(true);
//            paint.setDither(true);
//            canvas.drawARGB(0, 0, 0, 0);
//            paint.setColor(Color.parseColor("#BAB399"));
//            canvas.drawCircle(sbmp.getWidth() / 2+0.7f, sbmp.getHeight() / 2+0.7f,
//                    sbmp.getWidth() / 2+0.1f, paint);
//            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
//            canvas.drawBitmap(sbmp, rect, rect, paint);
//
//            return output;
//        }
//
//    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 0:
                mTitle = getString(R.string.app_name);
                break;
            case 1:
                mTitle = getString(R.string.title_table);
                break;
            case 2:
                mTitle = getString(R.string.title_order);
                break;
            case 3:
                mTitle = getString(R.string.title_tip);
                break;
            case 4:
                mTitle = getString(R.string.title_setting);
                break;
            case 5:
                mTitle = getString(R.string.title_logout);
                break;
            case 6:
                mTitle = "รายการอาหาร";
                break;
            case 7:
                mTitle = "จองโต๊ะอาหาร";
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    public void onClickConfirm(View v){
        AlertDialog.Builder alertDlg = new AlertDialog.Builder(this);

        alertDlg.setMessage("คุณต้องการชำระเงินโต๊ะ... ใช่หรือไม่ ?")
                .setTitle("ชำระเงิน")
                .setCancelable(false)
                .setPositiveButton("ยืนยันการชำระเงิน",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
        alertDlg.setNegativeButton("ยกเลิก",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDlg.create();
        alert.show();

    }
    public void onClickBookTable(View v){
        AlertDialog.Builder alertDlg = new AlertDialog.Builder(this);

        alertDlg.setMessage("คุณต้องการทำการจองโต๊ะ... ใช่หรือไม่ ?")
                .setTitle("จองโต๊ะ")
                .setCancelable(false)
                .setPositiveButton("ยืนยันการจอง",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
        alertDlg.setNegativeButton("ยกเลิก",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDlg.create();
        alert.show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
////        if (id == R.id.action_settings) {
////            return true;
////        }
//
//        return super.onOptionsItemSelected(item);
//    }



}
