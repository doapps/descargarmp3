package me.doapps.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.viewpagerindicator.IconPagerAdapter;

import me.doapps.fragments.Fragment_Download;
import me.doapps.fragments.Fragment_Search;

/**
 * Created by jnolascob on 10/09/2014.
 */
public class Fragment_Adapter extends FragmentPagerAdapter implements IconPagerAdapter {

    public Fragment_Adapter(FragmentManager fm){
        super(fm);
    }


    @Override
    public Fragment getItem(int i) {
        Fragment fragment;
        switch (i){
            case 0:
                fragment = new Fragment_Search();
                break;
            case 1:
                fragment = new Fragment_Download();
                break;
            default:
                fragment = new Fragment_Search();
        }
        return fragment;

    }

    @Override
    public int getIconResId(int i) {
        return 0;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position){
        String title = "";
        switch(position){
            case 0:
                title = "Buscar";
                break;
            case 1:
                title = "Descargar";
                break;
            case 2:
                title = "Reproducir";
                break;
        }
        return title;
    }

}
