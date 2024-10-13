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

public class AddClass extends AppCompatActivity {
    EditText t1,t2,t3;
    Button b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_class);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Intent getIntent=getIntent();
        t1=findViewById(R.id.editTextText);
        t2=findViewById(R.id.editTextText2);
        t3=findViewById(R.id.editTextText3);
        b=findViewById(R.id.button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s1,s2,s3;
                s1=t1.getText().toString();
                s2=t2.getText().toString();
                s3=t3.getText().toString();
                if (s1.isEmpty() || s2.isEmpty() || s3.isEmpty())
                {
                    Toast.makeText(AddClass.this, "INVALID" ,Toast.LENGTH_SHORT).show();
                    return;
                }
                getIntent.putExtra("id",s1);
                getIntent.putExtra("name",s2);
                getIntent.putExtra("depart",s3);
                setResult(100,getIntent);
                finish();
            }
        });
    }
}