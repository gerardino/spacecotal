package com.example.spacecoatl.app;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.example.spacecoatl.app.components.NotificationRowGenerator;
import com.example.spacecoatl.app.components.UtilFunctions;

import java.text.SimpleDateFormat;
import java.util.Date;
import android.app.Activity;
import android.app.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class Dashboard extends Fragment {
    //private OnItemSelectedListener listener;
    private final SimpleDateFormat _sdfWatchTime = new SimpleDateFormat("HH:mm");
    BroadcastReceiver _broadcastReceiver;
    private TextView _tvTime;

    NotificationRowGenerator nrg;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_dashboard,
                container, false);

        nrg = new NotificationRowGenerator(view.getContext(), _sdfWatchTime);

        _tvTime = (TextView)view.findViewById(R.id._tvTime);
        _tvTime.setText(_sdfWatchTime.format(new Date()));

        TableLayout tl = (TableLayout)(view.findViewById(R.id.notificationTable));
        tl.addView(nrg.getNotificationRow("Current location", "90º 14º", null, R.drawable.glyphicons_340_globe));

//        notificationIconHolder

        LinearLayout iconHolder = (LinearLayout)view.findViewById(R.id.notificationIconHolder);

        final float scale = view.getContext().getResources().getDisplayMetrics().density;
        int pixels = (int) (25 * scale + 0.5f);

        int[] iconsToLoad = new int[] { R.drawable.glyphicons_073_wifi,
                R.drawable.glyphicons_012_heart,
                R.drawable.glyphicons_205_electricity,
                R.drawable.glyphicons_252_oxygen_bottle};


        for(int i = 0; i < iconsToLoad.length; i++){
            Drawable d = view.getResources().getDrawable(iconsToLoad[i]);

            ImageView img = new ImageView(getActivity());
            img.setImageDrawable(UtilFunctions.InvertDrawable(d));
//            img.setPadding(pixels/5, pixels/5, pixels/5, pixels/5);
            img.setMinimumWidth(pixels);
            img.setMinimumHeight(pixels);

            iconHolder.addView(img);
        }
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        _broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context ctx, Intent intent) {
                if (intent.getAction().compareTo(Intent.ACTION_TIME_TICK) == 0) {
                    _tvTime.setText(_sdfWatchTime.format(new Date()));
                }
            }
        };

        getActivity().registerReceiver(_broadcastReceiver, new IntentFilter(Intent.ACTION_TIME_TICK));

    }

    @Override
    public void onStop() {
        super.onStop();
        if (_broadcastReceiver != null)
            getActivity().unregisterReceiver(_broadcastReceiver);

    }
}
