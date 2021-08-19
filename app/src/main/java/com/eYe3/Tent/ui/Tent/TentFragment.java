package com.eYe3.Tent.ui.Tent;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import com.eYe3.Tent.R;
import com.eYe3.Tent.adapters.DownTabsAdapter;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class TentFragment extends Fragment {
    ViewPager viewPager;
    TabLayout downTab;
    TabItem group, person, message;
    DownTabsAdapter mTabAdapter;

    public TentFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_tent, container, false);
        viewPager = fragmentView.findViewById(R.id.main_tabs);
        downTab = fragmentView.findViewById(R.id.down_main_tabs);
        group = fragmentView.findViewById(R.id.group);
        person = fragmentView.findViewById(R.id.person);
        message = fragmentView.findViewById(R.id.messages);
        return fragmentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mTabAdapter = new DownTabsAdapter(getFragmentManager(), downTab.getTabCount());
        viewPager.setAdapter(mTabAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(downTab));

        downTab.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}