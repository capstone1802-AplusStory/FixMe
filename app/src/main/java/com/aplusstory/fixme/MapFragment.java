package com.aplusstory.fixme;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapInfo;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPOIItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapView;

import java.util.ArrayList;

import static android.support.constraint.Constraints.TAG;

import static com.skt.Tmap.MapUtils.mApiKey;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MapFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment
        implements TMapGpsManager.onLocationChangedCallback, View.OnClickListener {
    public static final String KEY_LATITUDE = "latitude";
    public static final String KEY_LONGITUDE = "longitude";
    public static final String KEY_ADDRESS = "address";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private Context mContext = null;
    private TMapData tmapdata = null;
    private TMapGpsManager tmapgps = null;
    private TMapView tmapview = null;
    private static String mApiKey = "426c8d5b-45a1-49af-8e3d-05f5f36d3b9e";
    private static int mMarkerID;

    private ArrayList<TMapPoint> m_tmapPoint = new ArrayList<TMapPoint>();
    private ArrayList<String> mArrayMarkerID = new ArrayList<String>();
    private ArrayList<MapPoint> m_mapPoint = new ArrayList<MapPoint>();

    private String address;
    private Double lat = null;
    private Double lon = null;

    private Button bt_find; //주소로 찾기 버튼
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    @Override
    public void onLocationChange(Location location) {
        if (m_bTrackingMode) {
            tmapview.setLocationPoint(location.getLongitude(), location.getLatitude());
        }
    }
    private boolean m_bTrackingMode = true;
    public MapFragment() {
        // Required empty public constructor
    }



    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MapFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MapFragment newInstance(String param1, String param2) {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map,null);
        mContext = this.getContext();


        //버튼 선언
        bt_find = (Button) view.findViewById(R.id.bt_findadd);

        //Tmap 각종 객체 선언
        tmapdata = new TMapData(); //POI검색, 경로검색 등의 지도데이터를 관리하는 클래스
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.mapview);
        tmapview = new TMapView(getActivity());

        linearLayout.addView(tmapview);
        tmapview.setSKTMapApiKey(mApiKey);


        /* 현재 보는 방향 */
        tmapview.setCompassMode(true);

        /* 현위치 아이콘표시 */
        tmapview.setIconVisibility(true);

        /* 줌레벨 */
        tmapview.setZoomLevel(15);
        tmapview.setUserScrollZoomEnable(false);

        /* 지도 타입 */
        tmapview.setMapType(TMapView.MAPTYPE_STANDARD);

        /* 언어 설정 */
        tmapview.setLanguage(TMapView.LANGUAGE_KOREAN);



        tmapgps = new TMapGpsManager(getActivity()); //단말의 위치탐색을 위한 클래스
        tmapgps.setMinTime(1000); //위치변경 인식 최소시간설정
        tmapgps.setMinDistance(5); //위치변경 인식 최소거리설정

        tmapgps.setProvider(tmapgps.NETWORK_PROVIDER); //네트워크 기반의 위치탐색
        tmapgps.OpenGps();
        //tmapgps.setProvider(tmapgps.GPS_PROVIDER); //위성기반의 위치탐색
        /*  화면중심을 단말의 현재위치로 이동 */
//        tmapview.setTrackingMode(true);
        tmapview.setSightVisible(true);


        //풍선 클릭시
        tmapview.setOnCalloutRightButtonClickListener(new TMapView.OnCalloutRightButtonClickCallback() {
            @Override
            public void onCalloutRightButton(TMapMarkerItem markerItem) {

                lat = markerItem.latitude;
                lon = markerItem.longitude;
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("이곳으로 지정하시겠습니까?");

                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //이곳에 데이터 인텐트에 담아서 넘겨주고, 확정 짓는 코드 필요
                        Bundle bd = new Bundle();
                        bd.putDouble(KEY_LATITUDE, lat);
                        bd.putDouble(KEY_LONGITUDE, lon);
                        bd.putString(KEY_ADDRESS, address);
                        MapFragment.this.mListener.onFragmentInteraction(bd);
                    }
                });
                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
                //1. 위도, 경도로 주소 검색하기
                tmapdata.convertGpsToAddress(lat, lon, new TMapData.ConvertGPSToAddressListenerCallback() {
                    @Override
                    public void onConvertToGPSToAddress(String strAddress) {
                        address = strAddress;
                    }
                });
            }

        });

        //버튼 리스너 등록
        bt_find.setOnClickListener(this);
        tmapview.setOnLongClickListenerCallback(new TMapView.OnLongClickListenerCallback(){

            @Override
            public void onLongPressEvent(ArrayList<TMapMarkerItem> arrayList, ArrayList<TMapPOIItem> arrayList1, TMapPoint tMapPoint) {
                lat = tMapPoint.getLatitude();
                lon = tMapPoint.getLongitude();
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                tmapdata.convertGpsToAddress(lat, lon, new TMapData.ConvertGPSToAddressListenerCallback() {
                    @Override
                    public void onConvertToGPSToAddress(String strAddress) {
                        address = strAddress;
                    }
                });
                builder.setTitle("이곳으로 지정하시겠습니까?\n"+address);

                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Bundle bd = new Bundle();
                        bd.putDouble(KEY_LATITUDE, lat);
                        bd.putDouble(KEY_LONGITUDE, lon);
                        bd.putString(KEY_ADDRESS, address);
                        MapFragment.this.mListener.onFragmentInteraction(bd);
                    }
                });
                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
                //1. 위도, 경도로 주소 검색하기

            }
        });

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Bundle bd);
    }

    //핀 찍을 data
    public void addPoint(String name, double latitude, double longitude, String address) {
        // 강남 //
        m_mapPoint.add(new MapPoint(name, latitude, longitude, address));
        m_tmapPoint.add(new TMapPoint(latitude, longitude));
    }


    // 마커(핀) 찍는함수
    public void showMarkerPoint() {
        tmapview.removeAllMarkerItem();
        for (int i = 0; i < m_mapPoint.size(); i++) {
            TMapPoint point = new TMapPoint(m_mapPoint.get(i).getLatitude(),
                    m_mapPoint.get(i).getLongitude());
            TMapMarkerItem item1 = new TMapMarkerItem();
            Bitmap bitmap = null;
            /* 핀 이미지 */
            bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.poi_dot);

            item1.setTMapPoint(point);
            item1.setName(m_mapPoint.get(i).getName());
            item1.setVisible(item1.VISIBLE);

            item1.setIcon(bitmap);

            item1.setCalloutTitle(m_mapPoint.get(i).getName());
            item1.setCalloutSubTitle(m_mapPoint.get(i).getAddress());
            item1.setCanShowCallout(true);
            item1.setAutoCalloutVisible(true);

            /* 풍선 안 우측버튼 */
            Bitmap bitmap_i = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.marker_select);

            item1.setCalloutRightButtonImage(bitmap_i);

            String strID = String.format("pmarker%d", mMarkerID++);

            tmapview.addMarkerItem(strID, item1);
            mArrayMarkerID.add(strID);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_findadd:
                convertToAddress();
                break;
        }
    }

    //3. 주소검색으로 위도, 경도 검색하기
    /* 명칭 검색을 통한 주소 변환 */
    public void convertToAddress() {
        //다이얼로그 띄워서, 검색창에 입력받음
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setTitle("통합 검색");
        builder.setMessage("구체적으로 해주십시오\n(예 : 중앙대학교 310관)");

        final EditText input = new EditText(this.getContext());
        builder.setView(input);

        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String strData = input.getText().toString();
                m_mapPoint.clear();
                m_tmapPoint.clear();
                TMapData tmapdata = new TMapData();

                tmapdata.findAllPOI(strData, new TMapData.FindAllPOIListenerCallback() {
                    @Override
                    public void onFindAllPOI(ArrayList<TMapPOIItem> poiItem) {
                        for (int i = 0; i < poiItem.size(); i++) {
                            TMapPOIItem item = poiItem.get(i);

                            Log.d("주소로찾기", "POI Name: " + item.getPOIName().toString() + ", " +
                                    "Address: " + item.getPOIAddress().replace("null", "") + ", " +
                                    "Point: " + item.getPOIPoint().toString());
                            addPoint(item.getPOIName(),item.getPOIPoint().getLatitude(),item.getPOIPoint().getLongitude(),item.getPOIAddress());
                        }

                        /*map data 고정값 테스트*/
//                        addPoint("ㅁㄴㅇㄹ",37.570841, 126.985302,"ㅁㄴㅇㄹ"); // SKT타워
//                        addPoint("ㅁㄴㅇㄹ",37.551135, 126.988205,"ㅁㄴㅇㄹ"); // N서울타워
//                        addPoint("ㅁㄴㅇㄹ",37.579567, 126.976998,"ㅁㄴㅇㄹ"); // 경복궁
                        TMapInfo tmapInfo = tmapview.getDisplayTMapInfo(m_tmapPoint);
                        tmapview.setCenterPoint(tmapInfo.getTMapPoint().getLongitude(),tmapInfo.getTMapPoint().getLatitude());
                        tmapview.setZoomLevel(tmapInfo.getTMapZoomLevel());
                        showMarkerPoint();
                    }
                });
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
}
