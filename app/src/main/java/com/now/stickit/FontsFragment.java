package com.now.stickit;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;


public class FontsFragment extends Fragment {
    private final int TAG = 2;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_fonts, container, false);
        GridView gridView=(GridView)rootView.findViewById(R.id.gridview_fonts);
        new RemoteDataTask(rootView.getContext(),gridView,TAG).execute();
        return rootView;
    }
}