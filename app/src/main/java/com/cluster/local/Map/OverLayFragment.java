package com.cluster.local.Map;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.cluster.local.MainActivity;
import com.cluster.local.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class OverLayFragment extends Fragment {


    public OverLayFragment() {
        // Required empty public constructor
    }

   private MainActivity mainActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_over_lay, container, false);

        mainActivity = (MainActivity) getActivity();
        mainActivity.showSpinner(false);


        root.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        Button button = (Button) root.findViewById(R.id.allowButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                        && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    //should enable app permission in the settings

                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", mainActivity.getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                    Toast.makeText(getContext(), "Please click on Permissions and activate the location permission", Toast.LENGTH_LONG).show();
                } else {
                    mainActivity.getMapFragment().getLocationService().update();
                }
            }
        });
        return root;
    }

    @Override
    public void onDestroy() {
        ((MainActivity) getActivity()).showSpinner(true);
        mainActivity = null;
        super.onDestroy();
    }
}
