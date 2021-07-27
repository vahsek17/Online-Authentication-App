package com.example.testproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.jetbrains.annotations.NotNull;

public class profileFragment extends Fragment
{
    TextView textView;
    //String  email="", password="", phone="";
    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState)
    {// = inflater.inflate(R.layout.fragment_home,container,false);

        Intent intent = new Intent(getActivity(),profileActivity.class);
        startActivity(intent);
        View view = null;
        return view;
    }
}
