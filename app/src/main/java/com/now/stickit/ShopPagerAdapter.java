package com.now.stickit;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by User on 3/16/2015.
 */
public class ShopPagerAdapter extends FragmentPagerAdapter{
    private String[] tabs={"Notes","Stickers","Fonts"};

    public ShopPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
      switch(i){
          case 0:
              return new NotesFragment();
          case 1:
              return new StickersFragment();
          case 2:
              return new FontsFragment();
          default:
              return null;
      }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabs[position];
    }

}
