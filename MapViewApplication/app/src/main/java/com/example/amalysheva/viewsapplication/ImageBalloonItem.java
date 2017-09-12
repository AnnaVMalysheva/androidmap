package com.example.amalysheva.viewsapplication;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import ru.yandex.yandexmapkit.overlay.balloon.BalloonItem;
import ru.yandex.yandexmapkit.utils.GeoPoint;

public class ImageBalloonItem extends BalloonItem {

    protected ImageView imageView;
    protected TextView textView;
    private Integer id;
    Context mContext;

    public ImageBalloonItem(Context context, GeoPoint geoPoint) {
        super(context, geoPoint);
        mContext = context;
    }

    @Override
    public void inflateView(Context context){
        LayoutInflater inflater = LayoutInflater.from( context );
        model = (ViewGroup)inflater.inflate(R.layout.balloon_image_layout, null);
        imageView = (ImageView)model.findViewById( R.id.balloon_image_view1 );
        textView = (TextView)model.findViewById( R.id.balloon_text_view1 );
        setText(textView.getText());
    }

    @Override
    public void setText(CharSequence charSequence) {
        super.setText(charSequence);
        textView.setText(charSequence);
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}