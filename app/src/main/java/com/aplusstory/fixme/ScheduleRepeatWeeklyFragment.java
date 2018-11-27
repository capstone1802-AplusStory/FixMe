package com.aplusstory.fixme;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Toast;


public class ScheduleRepeatWeeklyFragment extends Fragment implements View.OnClickListener{
    public static final String ARG_PARAM_CHECK_DAY = "check_day";

    private Bundle arg;

    private boolean[] checkDay = {false, false, false, false, false, false, false}; // has information which day is checked

    private OnFragmentInteractionListener mListener = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        this.arg = savedInstanceState;
        this.arg = new Bundle();
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View returnView = inflater.inflate(R.layout.fragment_schedule_repeat_weekly, container, false);

//        final CheckBox checkBoxMon = (CheckBox) returnView.findViewById(R.id.checkMon);
//        final CheckBox checkBoxTue = (CheckBox) returnView.findViewById(R.id.checkTue);
//        final CheckBox checkBoxWed = (CheckBox) returnView.findViewById(R.id.checkWed);
//        final CheckBox checkBoxThu = (CheckBox) returnView.findViewById(R.id.checkThu);
//        final CheckBox checkBoxFri = (CheckBox) returnView.findViewById(R.id.checkFri);
//        final CheckBox checkBoxSat = (CheckBox) returnView.findViewById(R.id.checkSat);
//        final CheckBox checkBoxSun = (CheckBox) returnView.findViewById(R.id.checkSun);

        Button confirmButton = (Button) returnView.findViewById(R.id.confirmButton);
        confirmButton.setOnClickListener(this);
        return returnView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            Log.d(this.getClass().getName(),"Context does not implemented OnFragmentInteractionListener" );
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.confirmButton:
                CheckBox checkBoxMon = this.getView().findViewById(R.id.checkMon);
                CheckBox checkBoxTue = this.getView().findViewById(R.id.checkTue);
                CheckBox checkBoxWed = this.getView().findViewById(R.id.checkWed);
                CheckBox checkBoxThu = this.getView().findViewById(R.id.checkThu);
                CheckBox checkBoxFri = this.getView().findViewById(R.id.checkFri);
                CheckBox checkBoxSat = this.getView().findViewById(R.id.checkSat);
                CheckBox checkBoxSun = this.getView().findViewById(R.id.checkSun);

                this.checkDay[0] = checkBoxSun.isChecked();
                this.checkDay[1] = checkBoxMon.isChecked();
                this.checkDay[2] = checkBoxTue.isChecked();
                this.checkDay[3] = checkBoxWed.isChecked();
                this.checkDay[4] = checkBoxThu.isChecked();
                this.checkDay[5] = checkBoxFri.isChecked();
                this.checkDay[6] = checkBoxSat.isChecked();

                this.arg.putBooleanArray(ARG_PARAM_CHECK_DAY, this.checkDay);

                if (this.mListener != null) {
                    this.mListener.onFragmentInteraction(this.arg);
                }
                break;
            default:
                //something wrong
        }

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().hide(this).commit();
        fragmentManager.popBackStack();
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Bundle arg);
    }
}
