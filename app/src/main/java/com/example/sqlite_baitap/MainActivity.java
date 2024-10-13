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
import android.widget.AdapterView;
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

import com.example.sqlite_baitap.Adapter.ClassAdapter;
import com.example.sqlite_baitap.Class.SClass;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView lv;
    Button buttonAdd;
    ArrayList<SClass> myList =new ArrayList<>();
    ClassAdapter myAdapter;
    SQLiteDatabase myDatabase;
    SQLiteDatabase studentDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        androidx.appcompat.widget.Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("DANH SACH LOP");
        lv=findViewById(R.id.lvClass);
        buttonAdd=findViewById(R.id.buttonAdd);
        myDatabase= openOrCreateDatabase("sqlclass.sqlite",MODE_PRIVATE,null);
        try
        {
            String sql = "CREATE TABLE tblclass(malop TEXT primary key, tenlop TEXT, makhoa TEXT)";
            myDatabase.execSQL(sql);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        queryList();
        myAdapter=new ClassAdapter(MainActivity.this,R.layout.class_layout,myList);
        lv.setAdapter(myAdapter);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intendAddClass = new Intent(MainActivity.this, AddClass.class );
                startActivityForResult(intendAddClass, 99);
            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(MainActivity.this, Student_List_Activity.class);
                String idclass=myList.get(position).getId();
                intent.putExtra("idclass",idclass);
                startActivity(intent);
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.options_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==R.id.item1)
        {
            boolean show=myAdapter.isDisplay();
            myAdapter.setDisplay(!show);
        }
        if (item.getItemId() == R.id.item2)
        {
            ArrayList<String> delList=new ArrayList<>();
            for (int i=0;i<myList.size();i++)
            {
                if (myList.get(i).isSelected())
                {
                    delList.add(myList.get(i).getId());
                }
            }
            String[] inputdb=delList.toArray(new String[0]);
            int size = inputdb.length;
            StringBuilder whereClause = new StringBuilder("malop IN (");
            for (int i = 0; i < size; i++) {
                whereClause.append("?");
                if (i < size - 1) {
                    whereClause.append(",");
                }
            }
            whereClause.append(")");
            int n=myDatabase.delete("tblclass",whereClause.toString(),inputdb);
            String msg="";
            if (n==0)
            {
                msg="No record to Delete";
            }
            else
            {
                msg=n+" record is Deleted";
                for (int i=0;i<myList.size();i++)
                {
                    if (myList.get(i).isSelected())
                    {
                        myList.remove(i);
                        i--;
                    }
                }
                studentDB=openOrCreateDatabase("sqlstudent.sqlite",MODE_PRIVATE,null);
                studentDB.delete("tblstudent",whereClause.toString(),inputdb);
                myAdapter.notifyDataSetChanged();
            }
            Toast.makeText(MainActivity.this,msg,Toast.LENGTH_SHORT).show();
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
                Toast.makeText(MainActivity.this,"Must selection only 1 record",Toast.LENGTH_SHORT).show();
                return true;
            }
            LinearLayout layout = new LinearLayout(this);
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.setPadding(30, 30, 30, 30);
            TextView textView1 = new TextView(this);
            textView1.setText("Ten Lop: ");
            layout.addView(textView1);
            final EditText input1=new EditText(MainActivity.this);
            input1.setInputType(InputType.TYPE_CLASS_TEXT);
            layout.addView(input1);
            TextView textView2 = new TextView(MainActivity.this);
            textView2.setText("Ma Khoa:");
            layout.addView(textView2);
            final EditText input2=new EditText(MainActivity.this);
            input2.setInputType(InputType.TYPE_CLASS_TEXT);
            layout.addView(input2);
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Change Information").setView(layout);
            int finalPos = pos;
            builder.setPositiveButton("Submit", null);
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    myAdapter.setDisplay(false);
                }
            });
            AlertDialog dialog=builder.create();
            dialog.show();
            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name=input1.getText().toString();
                    String makhoa=input2.getText().toString();
                    if (name.isEmpty() || makhoa.isEmpty())
                    {
                        Toast.makeText(MainActivity.this,"Please enter a String",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    ContentValues contentValues=new ContentValues();
                    contentValues.put("tenlop",name);
                    contentValues.put("makhoa",makhoa);
                    myDatabase.update("tblclass",contentValues,"malop=?",new String[]{myList.get(finalPos).getId()});
                    myList.get(finalPos).setName(name);
                    myList.get(finalPos).setDepart(makhoa);
                    myAdapter.notifyDataSetChanged();
                    dialog.dismiss();
                    myAdapter.setDisplay(false);
                }
            });
        }
        return super.onOptionsItemSelected(item);
    }

    public void queryList(){
        myList.clear();
        Cursor c=myDatabase.query("tblclass",null,null,null,null,null,null);
        if (c!=null) {
            c.moveToNext();
            SClass tempClass;
            while (!c.isAfterLast()) {
                tempClass = new SClass(c.getString(0), c.getString(1), c.getString(2));
                c.moveToNext();
                myList.add(tempClass);
            }
            c.close();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        myAdapter.setDisplay(false);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==99 && resultCode==100)
        {
            String id=data.getStringExtra("id");
            String name=data.getStringExtra("name");
            String depart=data.getStringExtra("depart");
            ContentValues contentValues= new ContentValues();
            contentValues.put("malop",id);
            contentValues.put("tenlop",name);
            contentValues.put("makhoa",depart);
            String msg="";
            if (myDatabase.insert("tblclass",null,contentValues) == -1){
                msg="FAIL TO INSERT RECORD";
            }
            else
            {
                msg="INSERT RECORD SUCCESSFULLY";
                myList.add(new SClass(id,name,depart));
                myAdapter.notifyDataSetChanged();
            }
            Toast.makeText(MainActivity.this,msg,Toast.LENGTH_SHORT).show();
        }
    }
}