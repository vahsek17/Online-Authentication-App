package com.example.testproject;

import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.jetbrains.annotations.NotNull;

public class rulesAndInstructionsFragment extends Fragment
{
    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override

    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        //View view = inflater.inflate(R.layout.fragment_logout,container,false);
//        AlertDialog.Builder confirmLogout = new AlertDialog.Builder(getActivity()); //create alertBox
//        confirmLogout.setTitle("LogOut ?");
//        confirmLogout.setPositiveButton("yes", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("loginData", Context.MODE_PRIVATE);
//                        sharedPreferences.edit().clear().commit();//remove user data from local storaage
//                        Intent intent = new Intent(getActivity(),loginActivity.class);
//                        startActivity(intent);
//                        getActivity().finish(); }
//        }); //if Yes Button pressed, go to loginActivity
//        confirmLogout.setNegativeButton("No", new DialogInterface.OnClickListener() {
//            @Override public void onClick (DialogInterface dialog,int which)
//            { }
//        });
//        confirmLogout.create().show();  //show the alert button

        Intent intent = new Intent(getActivity(),rulesAndInstructions.class);
        startActivity(intent);
        View view = null;
        return view;
    }
}