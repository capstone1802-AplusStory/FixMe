package com.aplusstory.tmap;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapPolyLine;
import com.skt.Tmap.TMapView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PathFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PathFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PathFragment extends Fragment {
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

    public PathFragment() {
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
    public static PathFragment newInstance(String param1, String param2) {
        PathFragment fragment = new PathFragment();
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
        View view = inflater.inflate(R.layout.fragment_path,null);
        mContext = this.getContext();

        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.pathview);
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

        tmapview.setSightVisible(true);
        m_pathPoint.add( new TMapPoint(37.570841, 126.985302) ); // SKT타워
        m_pathPoint.add( new TMapPoint(37.551135, 126.988205) ); // N서울타워
        m_pathPoint.add( new TMapPoint(37.579600, 126.976998) ); // 경복궁

        TMapPolyLine tMapPolyLine = new TMapPolyLine();
        tMapPolyLine.setLineColor(Color.BLUE);
        tMapPolyLine.setLineWidth(2);
        for( int i=0; i<m_pathPoint.size(); i++ ) {
            tMapPolyLine.addLinePoint( m_pathPoint.get(i) );
        }
        tmapview.addTMapPolyLine("Line1", tMapPolyLine);
        return view;
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
        void onFragmentInteraction(Uri urik);
    }
}