package com.example.sqlite_baitap.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
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

import com.example.sqlite_baitap.Class.SClass;
import com.example.sqlite_baitap.R;

import java.util.ArrayList;
import java.util.Objects;

public class ClassAdapter extends ArrayAdapter<SClass>
{
    Activity content;
    int idLayout;
    ArrayList<SClass> myList;
    private boolean isDisplay;
    public ClassAdapter(Activity content, int idLayout, ArrayList<SClass> myList) {
        super(content, idLayout, myList);
        this.content = content;
        this.idLayout = idLayout;
        this.myList = myList;
    }
    public boolean isDisplay() {
        return isDisplay;
    }
    public void setDisplay(boolean display) {
        isDisplay = display;
        for (int i=0;i<myList.size();i++){
            myList.get(i).setSelected(false);
        }
        notifyDataSetChanged();
    }
    @SuppressLint("ViewHolder")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater myInflater = content.getLayoutInflater();
        convertView=myInflater.inflate(idLayout,null);
        SClass currentClass= myList.get(position);
        TextView t1=convertView.findViewById(R.id.txt_id);
        TextView t2=convertView.findViewById(R.id.txt_name);
        TextView t3=convertView.findViewById(R.id.txt_depart);
        CheckBox check=convertView.findViewById(R.id.checkBox);
        t1.setText(currentClass.getId());
        t2.setText(currentClass.getName());
        t3.setText(currentClass.getDepart());
        check.setChecked(currentClass.isSelected());
        check.setVisibility(isDisplay ? CheckBox.VISIBLE : CheckBox.GONE);
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
