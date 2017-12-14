package com.example.android.yourenglishvocabulary.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.yourenglishvocabulary.R;

/**
 * Created by joseluis on 25/11/2017.
 */

public class TabExamplesFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("TAGWORD", "Initialization TabExamplesFragment");
        SaveNewWordActivity.ACTIVE_TAG = SaveNewWordActivity.EXAMPLES_TAG;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tab_examples, container, false);
        Log.d("TAGWORD", "Loading TabExamplesFragment");
        return rootView;
    }
}
