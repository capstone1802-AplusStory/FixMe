package com.aplusstory.fixme;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PointF;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
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
import com.skt.Tmap.TMapPolyLine;
import com.skt.Tmap.TMapView;

import java.io.Serializable;
import java.util.ArrayList;

import static android.support.constraint.Constraints.TAG;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FootprintFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FootprintFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Context mContext = null;
    private TMapView tmapview = null;
    private static String mApiKey = "426c8d5b-45a1-49af-8e3d-05f5f36d3b9e";

    private ArrayList<TMapPoint> m_pathPoint = new ArrayList<TMapPoint>();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public FootprintFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PathFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FootprintFragment newInstance(String param1, String param2) {
        FootprintFragment fragment = new FootprintFragment();
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
        View view = inflater.inflate(R.layout.fragment_footprint, null);
        mContext = this.getContext();

        Bundle moveBundle = new Bundle();



        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.FootprintFragment);
        tmapview = new TMapView(getActivity());

        linearLayout.addView(tmapview);
        tmapview.setSKTMapApiKey(mApiKey);

        /* 현재 보는 방향 */
        tmapview.setCompassMode(true);

        /* 현위치 아이콘표시 */
        tmapview.setIconVisibility(true);

        /* 줌레벨 */
        tmapview.setZoomLevel(15);

        /* 지도 타입 */
        tmapview.setMapType(TMapView.MAPTYPE_STANDARD);

        /* 언어 설정 */
        tmapview.setLanguage(TMapView.LANGUAGE_KOREAN);

        tmapview.setSightVisible(true);


        if(getArguments()!=null){
            moveBundle = getArguments();

            LocationDataManager.PathData moveData = (LocationDataManager.PathData) moveBundle.getSerializable("MovementData");
            int locNum = moveData.locaArr.length;
            for(int i = 0 ;i < locNum; i++){
                addPathPoint(moveData.locaArr[i].latitude,moveData.locaArr[i].longitude);
            }

        }

//
//        addPathPoint(37.5050881, 126.957101); // SKT타워
//        addPathPoint(37.505536, 126.9567); // N서울타워
//        addPathPoint(37.5044113, 126.950567); // N서울타워
//        addPathPoint(37.4888071, 126.9635373); // N서울타워
//        addPathPoint(37.4863095, 126.98870369); // N서울타워
//        addPathPoint(37.500318, 127.003479); // 1N서울타워
//        addPathPoint(37.5154864, 127.03752039); // N서울타워
//        addPathPoint(37.521885, 127.062315); // N서울타워
//        addPathPoint(37.5389565, 127.07112789); // 경복궁

        TMapPolyLine tMapPolyLine = new TMapPolyLine();
        tMapPolyLine.setLineColor(Color.BLUE);
        tMapPolyLine.setLineWidth(2);
        for (int i = 0; i < m_pathPoint.size(); i++) {
            tMapPolyLine.addLinePoint(m_pathPoint.get(i));
        }
        /*임의 지점에 점을 추가해보기 위한 코드*/
//        tmapview.setOnLongClickListenerCallback(new TMapView.OnLongClickListenerCallback(){
//
//            @Override
//            public void onLongPressEvent(ArrayList<TMapMarkerItem> arrayList, ArrayList<TMapPOIItem> arrayList1, TMapPoint tMapPoint) {
//                final double lat = tMapPoint.getLatitude();
//                final double lon = tMapPoint.getLongitude();
//                tmapview.addTMapPolyLine("Line2",tMapPolyLine);
//                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//                builder.setTitle("이곳으로 지정하시겠습니까?");
//
//                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        addPathPoint(lat,lon);
//                        //이곳에 데이터 인텐트에 담아서 넘겨주고, 확정 짓는 코드 필요
//                    }
//                });
//                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();
//                    }
//                });
//
//                builder.show();
//                //1. 위도, 경도로 주소 검색하기
//
//            }
//        });
        /*zoom level 과 center point 최적화*/
        tmapview.addTMapPolyLine("Line1", tMapPolyLine);
        setBoundary();
        return view;
    }

    public void addPathPoint(Double latitude, Double longitude) {
        m_pathPoint.add(new TMapPoint(latitude, longitude));
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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

    public void setBoundary() {

        TMapData tmapdata = new TMapData();

        tmapdata.findAllPOI("asdf", new TMapData.FindAllPOIListenerCallback() {
            @Override
            public void onFindAllPOI(ArrayList<TMapPOIItem> poiItem) {
                TMapInfo tmapInfo = tmapview.getDisplayTMapInfo(m_pathPoint);
                tmapview.setCenterPoint(tmapInfo.getTMapPoint().getLongitude(), tmapInfo.getTMapPoint().getLatitude());
                tmapview.setZoomLevel(tmapInfo.getTMapZoomLevel());
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
