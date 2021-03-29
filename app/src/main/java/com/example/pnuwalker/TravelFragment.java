package com.example.pnuwalker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;

import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapView;

public class TravelFragment extends Fragment {

    TMapView tMapView;
    TMapData tMapData;
    RelativeLayout mapLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.travel_fragment, container, false);

        mapLayout = view.findViewById(R.id.map_layout);
        tMapView = new TMapView(getActivity());

        mapLayout.addView(tMapView);

        initMap();
        return view;
    }

    private void initMap() {
        tMapView.setSKTMapApiKey("l7xxdd90d676a9874f619cc8913aefc2bfe0"); //API 키 인증

        tMapView.setLanguage(TMapView.LANGUAGE_KOREAN);
        tMapView.setZoomLevel(17); //지도 초기 확대수준 설정
        tMapView.setSightVisible(true); //현재 보고있는 방향을 표시
        tMapView.setIconVisibility(true); //현재 위치를 표시하는 파랑색 아이콘을 표기
        tMapView.setLocationPoint(129.08102472195395, 35.23380020523731 ); //현재 위치 설정
        tMapView.setCenterPoint(129.08102472195395, 35.23380020523731); // 지도 중심좌표 설정
    }
}
