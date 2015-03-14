package com.just.btconnection.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.just.btconnection.R;


public class AboutFragment extends Fragment {
    private View mRootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_about, container, false);
        return mRootView;
    }

    public View getRootView() {
        return mRootView;
    }
}
