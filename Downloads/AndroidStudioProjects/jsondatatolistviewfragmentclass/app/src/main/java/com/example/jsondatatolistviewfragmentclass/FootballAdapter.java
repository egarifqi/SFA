package com.example.jsondatatolistviewfragmentclass;

import android.widget.ArrayAdapter;

public class FootballAdapter extends ArrayAdapter<FootballModel> {


    public FootballAdapter(Context context, ArrayList<FootballModel> arrayList) {
        super(context, R.layout.football_adapter_item, arrayList);
    }


    @Override
    public View getView(int position, View myView, ViewGroup parent) {
        LayoutInflater vi = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        myView = vi.inflate(R.layout.football_adapter_item, null);


        FootballModel fm = new FootballModel();
        fm = getItem(position);


        //TODO TextViews



        return myView;

    }