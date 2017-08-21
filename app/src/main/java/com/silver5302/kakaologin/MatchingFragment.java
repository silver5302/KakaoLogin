package com.silver5302.kakaologin;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.Toast;

/**
 * Created by alfo06-19 on 2017-08-21.
 */

public class MatchingFragment extends Fragment {

    CalendarView calendarView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_matching,container,false);


        calendarView=(CalendarView) view.findViewById(R.id.calendar);


        return view;
    }
}
