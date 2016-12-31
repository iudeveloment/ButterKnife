package com.kitkat.android.butterknife;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {
    private Unbinder unbinder;
    private TextView textView;

    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        // Non - Activity Bind
        ButterKnife.bind(this, view);

        // View, Activity 또는 Dialog 에서 View 를 찾아야하는 코드를 단순화하는 findById() Method
        // 제네릭을 사용하여 return Type 을 추론하고 자동으로 캐스팅.
        textView = ButterKnife.findById(view, R.id.hello);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // Binding Reset
        unbinder.unbind();
    }
}
