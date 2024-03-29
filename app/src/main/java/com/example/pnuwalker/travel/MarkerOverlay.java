package com.example.pnuwalker.travel;

import com.example.pnuwalker.R;
import com.skt.Tmap.TMapMarkerItem2;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapView;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.slidingpanelayout.widget.SlidingPaneLayout;

public class MarkerOverlay extends TMapMarkerItem2 {

    private DisplayMetrics dm = null;

    private Context 	mContext = null;
    private MarkerInfoLayout markerInfoLayout;

    private PNUBuildingInfo pnuInfo;
    private String name;
    private String description;
    private String buildingNumber;

    @Override
    public Bitmap getIcon() {
        return super.getIcon();
    }

    @Override
    public void setIcon(Bitmap bitmap) {
        super.setIcon(bitmap);
    }

    @Override
    public void setTMapPoint(TMapPoint point) {
        super.setTMapPoint(point);
    }

    @Override
    public TMapPoint getTMapPoint() {
        return super.getTMapPoint();
    }

    @Override
    public void setPosition(float dx, float dy) {
        super.setPosition(dx, dy);
    }

    @Override
    public void setCalloutRect(Rect rect) {
        super.setCalloutRect(rect);
    }

    public void setPnuInfo(PNUBuildingInfo info) { pnuInfo = info; }

    public MarkerOverlay(Context context, String buildingNumber, String name , String description,  String id, MarkerInfoLayout layout) {
        this.mContext = context;
        this.buildingNumber = buildingNumber;
        this.name = name;
        this.description = description;

        dm = new DisplayMetrics();
        WindowManager wmgr = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        context.getDisplay().getMetrics(dm);
        markerInfoLayout = layout;
    }

    @Override
    public void draw(Canvas canvas, TMapView mapView, boolean showCallout) {
        int x = mapView.getRotatedMapXForPoint(getTMapPoint().getLatitude(), getTMapPoint().getLongitude());
        int y = mapView.getRotatedMapYForPoint(getTMapPoint().getLatitude(), getTMapPoint().getLongitude());

        canvas.save();
        canvas.rotate(-mapView.getRotate(), mapView.getCenterPointX(), mapView.getCenterPointY());

        float xPos = getPositionX();
        float yPos = getPositionY();

        int nPos_x, nPos_y;

        int nMarkerIconWidth = 0;
        int nMarkerIconHeight = 0;
        int marginX = 0;
        int marginY = 0;

        nMarkerIconWidth = getIcon().getWidth();
        nMarkerIconHeight = getIcon().getHeight();

        nPos_x = (int) (xPos * nMarkerIconWidth);
        nPos_y = (int) (yPos * nMarkerIconHeight);

        if(nPos_x == 0) {
            marginX = nMarkerIconWidth / 2;
        } else {
            marginX = nPos_x;
        }

        if(nPos_y == 0) {
            marginY = nMarkerIconHeight / 2;
        } else {
            marginY = nPos_y;
        }

        canvas.translate(x - marginX, y - marginY);
        canvas.drawBitmap(getIcon(), 0, 0, null);
        canvas.restore();
    }

    @Override
    public boolean onSingleTapUp(PointF point, TMapView mapView) {
        setMarkerInfoLayout();
        return true;
    }

    private void setMarkerInfoLayout() {
        markerInfoLayout.setName(name);
        markerInfoLayout.setBuildingNumber(buildingNumber);
        if ( markerInfoLayout.getVisibility() == View.INVISIBLE || markerInfoLayout.getVisibility() == View.GONE)
            markerInfoLayout.show();
        markerInfoLayout.setTMapPoint(getTMapPoint());
        markerInfoLayout.setBtnDetailActive(true);
        markerInfoLayout.setPNUInfo(pnuInfo);
        markerInfoLayout.setIsPNU(true);
    }
}