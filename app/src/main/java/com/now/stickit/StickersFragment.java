package com.now.stickit;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


public class StickersFragment extends Fragment {
    private final int TAG=3;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_stickers, container, false);

        ListView listView=(ListView)rootView.findViewById(R.id.listView__stickers);
        new RemoteDataTask(rootView.getContext(),listView,TAG).execute();
        return rootView;
    }
}
