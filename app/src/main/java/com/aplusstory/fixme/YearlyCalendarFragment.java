package com.aplusstory.fixme;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.ik024.calendar_lib.custom.YearView;

import io.github.memfis19.cadar.view.MonthCalendar;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link YearlyCalendarFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link YearlyCalendarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class YearlyCalendarFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    YearView yearView;
    MonthCalendar monthCalendar;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public YearlyCalendarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment YearlyCalendarFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static YearlyCalendarFragment newInstance(String param1, String param2) {
        YearlyCalendarFragment fragment = new YearlyCalendarFragment();
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
        View returnView = inflater.inflate(R.layout.fragment_yearly_calendar, container, false);

        TextView janTextView = (TextView) returnView.findViewById(R.id.janText),
                febTextView = (TextView) returnView.findViewById(R.id.febText),
                marTextView = (TextView) returnView.findViewById(R.id.marText),
                aprTextView = (TextView) returnView.findViewById(R.id.aprText),
                mayTextView = (TextView) returnView.findViewById(R.id.mayText),
                junTextView = (TextView) returnView.findViewById(R.id.junText),
                julTextView = (TextView) returnView.findViewById(R.id.julText),
                augTextView = (TextView) returnView.findViewById(R.id.augText),
                sepTextView = (TextView) returnView.findViewById(R.id.sepText),
                octTextView = (TextView) returnView.findViewById(R.id.octText),
                novTextView = (TextView) returnView.findViewById(R.id.novText),
                decTextView = (TextView) returnView.findViewById(R.id.decText);

        //set background of textview
        janTextView.setBackgroundColor(getContext().getResources().getColor(R.color.graybg20));
        febTextView.setBackgroundColor(getContext().getResources().getColor(R.color.graybg20));
        marTextView.setBackgroundColor(getContext().getResources().getColor(R.color.graybg20));
        aprTextView.setBackgroundColor(getContext().getResources().getColor(R.color.graybg20));
        mayTextView.setBackgroundColor(getContext().getResources().getColor(R.color.graybg20));
        junTextView.setBackgroundColor(getContext().getResources().getColor(R.color.graybg20));
        julTextView.setBackgroundColor(getContext().getResources().getColor(R.color.graybg20));
        augTextView.setBackgroundColor(getContext().getResources().getColor(R.color.graybg20));
        sepTextView.setBackgroundColor(getContext().getResources().getColor(R.color.graybg20));
        octTextView.setBackgroundColor(getContext().getResources().getColor(R.color.graybg20));
        novTextView.setBackgroundColor(getContext().getResources().getColor(R.color.graybg20));
        decTextView.setBackgroundColor(getContext().getResources().getColor(R.color.graybg20));

        return returnView;
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
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
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
        void onFragmentInteraction(Uri uri);
    }
}
