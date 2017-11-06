package edu.weber.rhilton.cs3270.ldscsp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;


/**
 * A simple {@link Fragment} subclass.
 */
public class ConferenceSelectFragment extends Fragment {

    Spinner spiConfYear, spiConfMonth;
    View rootView;
    Button btnSave;
    String[] confMonths = {"04","10"};


    public ConferenceSelectFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_conference_select, container, false);
        spiConfYear = (Spinner) rootView.findViewById(R.id.spiConfYear);
        spiConfMonth = (Spinner) rootView.findViewById(R.id.spiConfMonth);
        btnSave = (Button) rootView.findViewById(R.id.btnSave);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity ma = (MainActivity) getActivity();
                ma.updateConference(spiConfYear.getSelectedItem().toString(),
                        spiConfMonth.getSelectedItem().toString());
            }
        });

        ArrayAdapter<CharSequence> monthsAdapter = ArrayAdapter.createFromResource(getActivity(),
               R.array.conference_months, android.R.layout.simple_spinner_item);
        monthsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spiConfMonth.setAdapter(monthsAdapter);

        ArrayAdapter<CharSequence> yearsAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.conference_years, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        yearsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spiConfYear.setAdapter(yearsAdapter);

        return rootView;
    }

}
