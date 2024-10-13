package com.example.sqlite_baitap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AddStudent extends AppCompatActivity {
    EditText t1,t2,t3,t4;
    Button submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_student);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Intent r=getIntent();
        t1=findViewById(R.id.editTextText6);
        t2=findViewById(R.id.editTextText4);
        t3=findViewById(R.id.editTextText5);
        t4=findViewById(R.id.editTextText7);
        submit=findViewById(R.id.button2);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id=t1.getText().toString();
                String name=t2.getText().toString();
                String birthday=t3.getText().toString();
                String malop=t4.getText().toString();
                if (!malop.equals(r.getStringExtra("malop")))
                {
                    Toast.makeText(AddStudent.this,"Ma Lop phai la: "+r.getStringExtra("malop"),Toast.LENGTH_SHORT).show();
                    return;
                }
                r.putExtra("id",id);
                r.putExtra("name",name);
                r.putExtra("birthday",birthday);
                r.putExtra("malop",malop);
                setResult(99,r);
                finish();
            }
        });
    }
}