package com.dhh.hugeimageview;

import android.graphics.Matrix;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.dhh.hugeimageview.huge.HugeImageRegionLoader;
import com.dhh.hugeimageview.huge.TileDrawable;
import com.dhh.hugeimageview.hugeview.PinchImageView;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import ikidou.reflect.TypeBuilder;

public class MainActivity extends AppCompatActivity {

    private PinchImageView imageview;
    private PathViewB pathview;
    private TileDrawable mDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        imageview.post(new Runnable() {
            @Override
            public void run() {
                mDrawable = new TileDrawable();
                mDrawable.setInitCallback(new TileDrawable.InitCallback() {
                    @Override
                    public void onInit() {
                        imageview.setImageDrawable(mDrawable);
                        setPoints();
                        pathview.setMatrix(imageview.getCurrentImageMatrix(new Matrix()));
                    }
                });
                mDrawable.init(new HugeImageRegionLoader(MainActivity.this, Uri.parse("file:///android_asset/gs_7.png")), new Point(imageview.getWidth(), imageview.getHeight()));

            }
        });
        imageview.addOuterMatrixChangedListener(new PinchImageView.OuterMatrixChangedListener() {
            @Override
            public void onOuterMatrixChanged(PinchImageView pinchImageView) {
                pathview.setMatrix(pinchImageView.getCurrentImageMatrix(new Matrix()));
            }
        });
    }

    private void setPoints() {
        try {
            Gson gson = new Gson();
            InputStream inputStream = getAssets().open("patadata");
            com.google.gson.stream.JsonReader jsonReader = gson.newJsonReader(new InputStreamReader(inputStream));
            TypeAdapter<List<Point>> adapter = (TypeAdapter<List<Point>>) gson.getAdapter(TypeToken.get(TypeBuilder.newInstance(List.class).addTypeParam(Point.class).build()));
            List<Point> points = adapter.read(jsonReader);
            for (Point point : points) {
//                point.x /= 6;
//                point.y /= 6;
                point.y = mDrawable.getIntrinsicHeight() - point.y;
            }
            pathview.setPoints(points);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        imageview = (PinchImageView) findViewById(R.id.imageview);
        pathview = (PathViewB) findViewById(R.id.pathview);
    }
}
