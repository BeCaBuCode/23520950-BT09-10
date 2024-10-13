package com.example.sqlite_baitap.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.sqlite_baitap.Class.Student;
import com.example.sqlite_baitap.R;

import java.util.ArrayList;
import java.util.Objects;

public class StudentAdapter extends ArrayAdapter<Student> {
    Activity content;
    int idLayout;
    ArrayList<Student> myList;
    private boolean isDisplay;
    public StudentAdapter(Activity content, int idLayout, ArrayList<Student> myList) {
        super(content,idLayout,myList);
        this.content = content;
        this.idLayout = idLayout;
        this.myList = myList;
        this.isDisplay=false;
    }

    public boolean isDisplay() {
        return isDisplay;
    }

    public void setDisplay(boolean display) {
        isDisplay = display;
        for (int i=0;i<myList.size();i++)
        {
            myList.get(i).setSelected(false);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater myInflater = content.getLayoutInflater();
        convertView = myInflater.inflate(idLayout,null);
        TextView t1=convertView.findViewById(R.id.txt_Sid);
        TextView t2=convertView.findViewById(R.id.txt_Sname);
        TextView t3=convertView.findViewById(R.id.txt_birthday);
        Student curr=myList.get(position);
        t1.setText(curr.getId());
        t2.setText(curr.getName());
        t3.setText(curr.getBirthday());
        CheckBox check=convertView.findViewById(R.id.checkBox2);
        check.setVisibility(isDisplay ? CheckBox.VISIBLE : CheckBox.GONE);
        check.setChecked(curr.isSelected());
        check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Objects.requireNonNull(getItem(position)).setSelected(isChecked);
                notifyDataSetChanged();
            }
        });
        return convertView;
    }
}
