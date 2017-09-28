package com.dhh.hugeimageview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;


/**
 * Created by dhh on 2017/8/9.
 */

public class PathViewB extends View {

    private Bitmap baseBitmap;
    private Canvas baseCanvas;
    private List<Point> points;
    private Paint pathPaint;
    private Path path;
    private PathMeasure pm;
    private float[] pos;
    private Path circlePath;


    public PathViewB(Context context) {
        super(context);
        init();
    }

    public PathViewB(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PathViewB(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        pathPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        pathPaint.setColor(Color.BLUE);
        pathPaint.setStrokeWidth(10);
        pathPaint.setAntiAlias(true);
        pathPaint.setStyle(Paint.Style.STROKE);
        pathPaint.setStrokeJoin(Paint.Join.ROUND);
        pathPaint.setStrokeCap(Paint.Cap.ROUND);
//        pathPaint.setPathEffect(new CornerPathEffect(50));
//        pathPaint.setShader(new BitmapShader(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_round), Shader.TileMode.MIRROR, Shader.TileMode.MIRROR));
//        pathPaint.setShader(new LinearGradient(0, 0, 1000, 1000, Color.BLUE, Color.RED, Shader.TileMode.REPEAT));
        path = new Path();
        pm = new PathMeasure();
        pos = new float[2];
        circlePath = new Path();
        circlePath.addCircle(500, 500, 100, Path.Direction.CW);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);

    }

    @Override
    protected void onDraw(Canvas canvas) {
//        canvas.save();
//        canvas.setMatrix(mMatrix);
//        canvas.drawPath(path, pathPaint);
//        canvas.restore();
        float scale = getScale(mMatrix);
        pathPaint.setStrokeWidth(scale * 40);
//        pathPaint.setPathEffect(new CornerPathEffect(scale * 50));
        pathPaint.setColor(Color.GREEN);
        canvas.drawPath(dst, pathPaint);

//        dst.offset(20 * scale, 20 * scale, offsetPath);
//        pathPaint.setColor(Color.RED);
//        canvas.save();
//        canvas.scale(0.95F, 0.95F, getWidth() / 2, getHeight() / 2);
//
//        canvas.drawPath(dst, pathPaint);
//        canvas.restore();
    }

    Path offsetPath = new Path();

    public static float getScale(Matrix matrix) {
        return (float) Math.sqrt(getMatrixScale(matrix)[0] * getMatrixScale(matrix)[0] +
                getMatrixScale(matrix)[1] * getMatrixScale(matrix)[1]);
    }

    public static float[] getMatrixScale(Matrix matrix) {
        if (matrix != null) {
            float[] value = new float[9];
            matrix.getValues(value);
            return new float[]{value[0], value[3]};
        } else {
            return new float[2];
        }
    }

    Path dst = new Path();

    private Matrix mMatrix = new Matrix();


    public void setPoints(List<Point> points) {
        this.points = points;
        createPath();
    }

    private void createPath() {
        path.reset();
        path.moveTo(points.get(0).x, points.get(0).y);
        for (int i = 0; i < points.size() - 1; i++) {
            Point point = points.get(i);
            path.quadTo(point.x, point.y, point.x, point.y);
        }
        postInvalidate();
    }

    public void setMatrix(Matrix matrix) {
        mMatrix = matrix;
        path.transform(matrix, dst);
        postInvalidate();
    }
}
