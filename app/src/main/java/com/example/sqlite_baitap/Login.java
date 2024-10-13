package com.example.sqlite_baitap;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.sqlite_baitap.Class.SClass;

public class Login extends AppCompatActivity {
    EditText user,pass;
    Button b1,b2;
    SQLiteDatabase mydb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        mydb=openOrCreateDatabase("sqluser.sqlite",MODE_PRIVATE,null);
        try {
            String sql="CREATE TABLE user(username TEXT PRIMARY KEY,password TEXT)";
            mydb.execSQL(sql);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        user=findViewById(R.id.txt_username);
        pass=findViewById(R.id.txt_password);
        b1=findViewById(R.id.button3);
        b2=findViewById(R.id.buttonAddUser);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isTableEmpty("user"))
                {
                    Toast.makeText(Login.this,"Please create a user",Toast.LENGTH_SHORT).show();
                    return;
                }
                else
                {
                    String username=user.getText().toString();
                    String password=user.getText().toString();
                    String query = "SELECT * FROM user WHERE username = ? AND password = ?";
                    Cursor cursor = mydb.rawQuery(query, new String[]{username, password});
                    boolean exists = cursor.getCount() > 0;
                    cursor.close();
                    if (exists)
                    {
                        Intent r=new Intent(Login.this, MainActivity.class);
                        user.setText("");
                        pass.setText("");
                        startActivity(r);
                    }
                    else
                    {
                        Toast.makeText(Login.this,"INVALID",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout layout = new LinearLayout(Login.this);
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.setPadding(30, 30, 30, 30);
                TextView textView1 = new TextView(Login.this);
                textView1.setText("Ten dang nhap: ");
                layout.addView(textView1);
                EditText input1=new EditText(Login.this);
                input1.setInputType(InputType.TYPE_CLASS_TEXT);
                layout.addView(input1);
                TextView textView2 = new TextView(Login.this);
                textView2.setText("Mat khau:");
                layout.addView(textView2);
                EditText input2=new EditText(Login.this);
                input2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                layout.addView(input2);
                AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                builder.setTitle("Add User").setView(layout);
                builder.setPositiveButton("Submit", null);
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog=builder.create();
                dialog.show();
                dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String username=input1.getText().toString();
                        String password=input2.getText().toString();
                        if (username.isEmpty() || password.isEmpty())
                        {
                            Toast.makeText(Login.this,"Please enter a String",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        ContentValues contentValues=new ContentValues();
                        contentValues.put("username",username);
                        contentValues.put("password",password);
                        String msg="";
                        if (mydb.insert("user",null,contentValues) == -1){
                            msg="FAIL TO INSERT RECORD";
                        }
                        else
                        {
                            msg="INSERT RECORD SUCCESSFULLY";
                        }
                        Toast.makeText(Login.this,msg,Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
            }
        });
    }
    public boolean isTableEmpty(String tableName) {
        String countQuery = "SELECT COUNT(*) FROM " + tableName;
        Cursor cursor = mydb.rawQuery(countQuery, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        return count == 0;
    }
}