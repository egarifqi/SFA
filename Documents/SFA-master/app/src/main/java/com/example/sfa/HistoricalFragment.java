package com.example.sfa;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class HistoricalFragment extends Fragment {


    public HistoricalFragment() {
        // Required empty public constructor
    }

    EditText date;
    DatePickerDialog datePickerDialog;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_historical, container, false);

        date = (EditText) view.findViewById(R.id.datepicker);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int mYear = calendar.get(Calendar.YEAR);
                int mMonth = calendar.get(Calendar.MONTH);
                int mDay = calendar.get(Calendar.DAY_OF_MONTH);
                return new DatePickerDialog(getActivity(),this, int mYear, );
            }
        });
        return view;
    }


}
