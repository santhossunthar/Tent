package com.eYe3.Tent.tab;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import com.eYe3.Tent.R;

public class TentGroupFragment extends Fragment {
    public TentGroupFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_tg, container, false);
        return fragmentView;
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}
