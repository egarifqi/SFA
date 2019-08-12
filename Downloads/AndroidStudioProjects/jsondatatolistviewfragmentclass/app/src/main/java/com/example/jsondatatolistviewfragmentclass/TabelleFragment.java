package com.example.jsondatatolistviewfragmentclass;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class TabelleFragment extends Fragment {
    JSONTask jsonTask;

    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tabelle_fragment, container, false);

        ArrayList<FootballModel> arrayList = new ArrayList<>();
        FootballAdapter adapter = new FootballAdapter(getActivity().getApplicationContext(), arrayList);
        ListView listView = (ListView) view.findViewById(R.id.listRanking);
        listView.setAdapter(adapter);


        Button buttonDL = (Button) view.findViewById(R.id.buttonDL);
        buttonDL.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view2) {
                        String result = "https://www.dein-weg-in-die-cloud.de/tomcat7/RestSoccer/fussball/tabelle";
                        startURLFetch(result);
                    }
                }
        );

        FootballModel fm = new FootballModel();

        arrayList.add(fm);

        return view;

    }

    protected void startURLFetch(String result) {
        jsonTask = new JSONTask(this);
        jsonTask.execute(result);
    }