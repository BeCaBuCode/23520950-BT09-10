package com.example.sqlite_baitap;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.sqlite_baitap.Adapter.StudentAdapter;
import com.example.sqlite_baitap.Class.Student;

import java.util.ArrayList;

public class Student_List_Activity extends AppCompatActivity {
    ListView lv;
    ArrayList<Student> myList;
    StudentAdapter studentAdapter;
    SQLiteDatabase mydb;
    Button b;
    String classId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_student_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Intent intent=getIntent();
        classId=intent.getStringExtra("idclass");
        Toolbar mytoolbar=findViewById(R.id.toolbar2);
        setSupportActionBar(mytoolbar);
        getSupportActionBar().setTitle("DANH SACH SINH VIEN");
        lv=findViewById(R.id.lv1);
        b=findViewById(R.id.buttonAdd2);
        myList=new ArrayList<>();
        mydb=openOrCreateDatabase("sqlstudent.sqlite",MODE_PRIVATE,null);
        try
        {
            String sql="CREATE TABLE tblstudent(mahs TEXT PRIMARY KEY,tenhs TEXT, ngaysinh TEXT,malop TEXT,FOREIGN KEY (malop) REFERENCES tblclass (malop))";
            mydb.execSQL(sql);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        queryList();
        studentAdapter=new StudentAdapter(Student_List_Activity.this, R.layout.student_layout,myList);
        lv.setAdapter(studentAdapter);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent r=new Intent(Student_List_Activity.this, AddStudent.class);
                r.putExtra("malop",classId);
                startActivityForResult(r,314);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.options_menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==R.id.item1)
        {
            boolean haveChecked=studentAdapter.isDisplay();
            studentAdapter.setDisplay(!haveChecked);
        }
        if (item.getItemId()==R.id.item2)
        {
            ArrayList<String> delList=new ArrayList<>();
            for (int i=0;i<myList.size();i++)
            {
                if (myList.get(i).isSelected())  delList.add(myList.get(i).getId());
            }
            String[] finalstring=delList.toArray(new String[0]);
            int size=finalstring.length;
            StringBuilder whereClause = new StringBuilder("mahs IN (");
            for (int i=0;i<size;i++)
            {
                whereClause.append("?");
                if (i < size - 1) {
                    whereClause.append(","); // Add comma between placeholders
                }
            }
            whereClause.append(")");
            int n=mydb.delete("tblstudent",whereClause.toString(),finalstring);
            String msg="";
            if (n==0){
                msg="No record to Delete";
            }
            else
            {
                msg=n+" record is Deleted";
                for (int i=0;i<myList.size();i++)
                {
                    myList.remove(i);
                    i--;
                }
                studentAdapter.notifyDataSetChanged();
            }
            Toast.makeText(Student_List_Activity.this,msg,Toast.LENGTH_SHORT).show();
        }
        if (item.getItemId() == R.id.item3)
        {
            int count=0;
            int pos =0;
            for (int i=0;i<myList.size();i++)
            {
                if (myList.get(i).isSelected())
                {
                    count++;
                    pos =i;
                }
            }
            if (count ==0) return true;
            if (count>1)
            {
                Toast.makeText(Student_List_Activity.this,"Must selection only 1 record",Toast.LENGTH_SHORT).show();
                return true;
            }
            LinearLayout layout = new LinearLayout(this);
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.setPadding(30, 30, 30, 30);
            TextView textView1 = new TextView(this);
            textView1.setText("Ho Ten: ");
            layout.addView(textView1);
            final EditText input1=new EditText(Student_List_Activity.this);
            input1.setInputType(InputType.TYPE_CLASS_TEXT);
            layout.addView(input1);
            TextView textView2 = new TextView(Student_List_Activity.this);
            textView2.setText("Ngay Sinh:");
            layout.addView(textView2);
            final EditText input2=new EditText(Student_List_Activity.this);
            input2.setInputType(InputType.TYPE_CLASS_TEXT);
            layout.addView(input2);
            AlertDialog.Builder builder = new AlertDialog.Builder(Student_List_Activity.this);
            builder.setTitle("Change Information").setView(layout);
            int finalPos = pos;
            builder.setPositiveButton("Submit", null);
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    studentAdapter.setDisplay(false);
                }
            });
            AlertDialog dialog=builder.create();
            dialog.show();
            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name=input1.getText().toString();
                    String birthday =input2.getText().toString();
                    if (name.isEmpty() || birthday.isEmpty())
                    {
                        Toast.makeText(Student_List_Activity.this,"Please enter a String",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    ContentValues contentValues=new ContentValues();
                    contentValues.put("tenhs",name);
                    contentValues.put("ngaysinh", birthday);
                    mydb.update("tblstudent",contentValues,"mahs=?",new String[]{myList.get(finalPos).getId()});
                    myList.get(finalPos).setName(name);
                    myList.get(finalPos).setBirthday(birthday);
                    studentAdapter.notifyDataSetChanged();
                    dialog.dismiss();
                    studentAdapter.setDisplay(false);
                }
            });
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==314 && resultCode==99)
        {
            String id=data.getStringExtra("id");
            String name=data.getStringExtra("name");
            String birthday=data.getStringExtra("birthday");
            String malop=data.getStringExtra("malop");
            ContentValues contentValues=new ContentValues();
            contentValues.put("mahs",id);
            contentValues.put("tenhs",name);
            contentValues.put("ngaysinh",birthday);
            contentValues.put("malop",malop);
            String msg="";
            if (mydb.insert("tblstudent",null,contentValues) == -1){
                msg="FAIL TO INSERT RECORD";
            }
            else
            {
                msg="INSERT RECORD SUCCESSFULLY";
                myList.add(new Student(id,name,birthday));
                studentAdapter.notifyDataSetChanged();
            }
            Toast.makeText(Student_List_Activity.this,msg,Toast.LENGTH_SHORT).show();
        }
    }

    public void queryList()
    {
        myList.clear();
        Cursor c=mydb.query("tblstudent",null,"malop=?",new String[]{classId},null,null,null);
        if (c!=null) {
            c.moveToNext();
            Student tempStudent;
            while (!c.isAfterLast()) {
                tempStudent = new Student(c.getString(0), c.getString(1), c.getString(2));
                c.moveToNext();
                myList.add(tempStudent);
            }
            c.close();
        }
    }
}