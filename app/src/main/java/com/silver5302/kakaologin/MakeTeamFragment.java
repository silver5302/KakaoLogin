package com.silver5302.kakaologin;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * Created by alfo06-19 on 2017-07-26.
 */

public class MakeTeamFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_make_team, container, false);
        Spinner spinner = (Spinner) view.findViewById(R.id.spinner);
        Spinner spinner2 = (Spinner)view.findViewById(R.id.spinner2);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(getContext(), R.array.datas_location, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner.setAdapter(adapter);

        ArrayAdapter adapter2=ArrayAdapter.createFromResource(getContext(),R.array.datas_number,android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner2.setAdapter(adapter2);

        spinner.setOnItemSelectedListener(listener);
        spinner2.setOnItemSelectedListener(listener);



        return view;
    }

    AdapterView.OnItemSelectedListener listener=new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            Toast.makeText(getContext(), parent.getId()+"", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };


}
