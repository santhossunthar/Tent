package com.eYe3.Tent.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import com.eYe3.Tent.tab.MessageFragment;
import com.eYe3.Tent.tab.MyTentFragment;
import com.eYe3.Tent.tab.TentGroupFragment;

public class DownTabsAdapter extends FragmentPagerAdapter {
    int tabCounts;

    public DownTabsAdapter(FragmentManager fm, int tabCounts) {
        super(fm);
        this.tabCounts=tabCounts;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new TentGroupFragment();
            case 1:
                return new MyTentFragment();
            case 2:
                return new MessageFragment();
                default:
        }
        return null;
    }

    @Override
    public int getCount() {
        return tabCounts;
    }
}
