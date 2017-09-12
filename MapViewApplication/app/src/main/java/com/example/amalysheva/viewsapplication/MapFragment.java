package com.example.amalysheva.viewsapplication;

import android.Manifest;
import android.app.Fragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ru.yandex.yandexmapkit.MapController;
import ru.yandex.yandexmapkit.MapView;
import ru.yandex.yandexmapkit.OverlayManager;
import ru.yandex.yandexmapkit.map.GeoCode;
import ru.yandex.yandexmapkit.map.GeoCodeListener;
import ru.yandex.yandexmapkit.overlay.OnOverlayItemListener;
import ru.yandex.yandexmapkit.overlay.Overlay;
import ru.yandex.yandexmapkit.overlay.OverlayItem;
import ru.yandex.yandexmapkit.utils.ScreenPoint;

import static ru.yandex.yandexmapkit.map.MapEvent.MSG_LONG_PRESS;

public class MapFragment extends Fragment implements GeoCodeListener, OnOverlayItemListener {
    private OnMapFragmentInteractionListener mListener;
    MapController mMapController;
    OverlayManager mOverlayManager;
    MapView mapView;
    Overlay overlay;
    private static int myItemsCounter;
    private static final int PERMISSIONS_CODE = 109;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        mapView = (MapView) view.findViewById(R.id.map);

        mMapController = mapView.getMapController();

        mOverlayManager = mMapController.getOverlayManager();
        mMapController.addMapListener(event -> {
            if (event.getMsg() == MSG_LONG_PRESS) {
                mMapController.getDownloader().getGeoCode(MapFragment.this, mMapController.getGeoPoint(new ScreenPoint(event.getX(), event.getY())));
            }
        });

        mMapController.getOverlayManager().getMyLocation().setEnabled(true);
        checkPermission();
        overlay = new Overlay(mMapController);
        overlay.setVisible(true);
        mOverlayManager.addOverlay(overlay);
        return view;
    }


    @Override
    public void onClick(OverlayItem overlayItem) {
        overlayItem.getGeoPoint();
        mMapController.showBalloon(overlayItem.getBalloonItem());
        mMapController.notifyRepaint();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OnMapFragmentInteractionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " should implement OnMapFragmentInteractionListener");
        }
    }

    @Override
    public boolean onFinishGeoCode(GeoCode geoCode) {
        final OverlayItem tapOverlay = new OverlayItem(geoCode.getGeoPoint(), ContextCompat.getDrawable(mMapController.getContext(), R.drawable.shop));

        ImageBalloonItem balloon = new ImageBalloonItem(mMapController.getContext(), tapOverlay.getGeoPoint());
        balloon.setText(geoCode.getDisplayName());
        balloon.setId(myItemsCounter++);
        tapOverlay.setBalloonItem(balloon);
        tapOverlay.setOverlayItemListener(this);

        overlay.addOverlayItem(tapOverlay);
        mMapController.notifyRepaint();
        mListener.onFragmentInteraction(geoCode.getDisplayName(),balloon.getId());
        return true;
    }

    public void showBalloon(Integer position) {
        List<OverlayItem> oi = overlay.getOverlayItems();
        for(OverlayItem overlayItem: oi){
            ImageBalloonItem imageBalloonItem = ((ImageBalloonItem)overlayItem.getBalloonItem());
            if (imageBalloonItem.getId().equals(position)){
                mMapController.showBalloon(imageBalloonItem);
                break;
            }
        }
        mMapController.notifyRepaint();
    }

    public void changeBalloon(Integer position, String text) {
        List<OverlayItem> oi = overlay.getOverlayItems();
        for(OverlayItem overlayItem: oi){
            ImageBalloonItem imageBalloonItem = ((ImageBalloonItem)overlayItem.getBalloonItem());
            if (imageBalloonItem.getId().equals(position)){
                imageBalloonItem.setText(text);
                break;
            }
        }
        mMapController.notifyRepaint();
    }

    public void deleteBalloon(Integer position) {
        List<OverlayItem> oi = overlay.getOverlayItems();
        OverlayItem foundItem = null;
        for(OverlayItem overlayItem: oi){
            ImageBalloonItem imageBalloonItem = ((ImageBalloonItem)overlayItem.getBalloonItem());
            if (imageBalloonItem.getId().equals(position)){
                foundItem = overlayItem;
                break;
            }
        }
        if (foundItem != null) {
            overlay.removeOverlayItem(foundItem);
            mMapController.notifyRepaint();
        }
    }

    private void checkPermission() {
        int permACL = ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION);
        int permAFL = ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION);

        if (permACL != PackageManager.PERMISSION_GRANTED ||
                permAFL != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this.getActivity(),
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_CODE);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mMapController.getOverlayManager().getMyLocation().refreshPermission();

                }
            }
        }
    }

    public interface OnMapFragmentInteractionListener {

        void onFragmentInteraction(String link, Integer id);

    }
}
