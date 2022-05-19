package com.app.roktoDorkar.view.RequestFrag.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.app.roktoDorkar.view.RequestFrag.MyRequestFrag;
import com.app.roktoDorkar.view.RequestFrag.ReceivedFrag;

public class MyFragmentAdapter extends FragmentStateAdapter {
    public MyFragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0)
        {
            return new ReceivedFrag();
        }else if (position==1)
        {
            return new MyRequestFrag();
        }
        return new ReceivedFrag();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
