package com.proathome.utils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.proathome.fragments.ButtonFragment;

public class CommonUtils {

    public static void setFragment(AppCompatActivity activity, String nameFragment, int contentRes){

        Fragment fragment = getFragmentById(nameFragment);
        activity.getSupportFragmentManager().beginTransaction().add(contentRes, fragment).commit();

    }

    private static Fragment getFragmentById(String nameFragment) {

        Fragment fragment = null;

        switch (nameFragment) {

            case ButtonFragment.TAG:
                fragment = new ButtonFragment();
                break;

        }

        return fragment;

    }

}