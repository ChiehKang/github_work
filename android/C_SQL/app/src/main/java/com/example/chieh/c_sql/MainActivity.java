package com.example.chieh.c_sql;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;

import static com.example.chieh.c_sql.myDBHelper.*;

public class MainActivity extends AppCompatActivity {
    private FileOutputStream outputStream;
    private FileInputStream inputStream;
    private Context context;

    private DrawerLayout mDrawer;
    private TableLayout tableLayout;

    private myDBHelper DBhelper;
    private Cursor cursor;

    //Dialog用
    private AlertDialog dialogTable;
    private AlertDialog dialogColumn;
    private AlertDialog dialogData;

    private Spinner spinner;
    private DatePickerDialog datePickerDialog;
    private EditText et_data;
    private EditText et_column;
    private  ListView listView;

    private String tableName;
    private ArrayList<String> tableList;
    private int cTypeNum = 1;

    private String _temp;
    private String _temp2;
    private int dataLength;

    @TargetApi(Build.VERSION_CODES.N)
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.context = context;
        DBhelper = new myDBHelper(this, "zhaitan.db", null, 1);
        mDrawer =(DrawerLayout)findViewById(R.id.drawer_layout);
        mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        tableLayout = (TableLayout)findViewById(R.id.table_layout);

        //Dialog
        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);

        //欄位
        dialogColumn = new AlertDialog.Builder(MainActivity.this).create();
        View v_column = inflater.inflate(R.layout.dialog_column, null);
        dialogColumn.setView(v_column);
        dialogColumn.getClass();
        et_column = (EditText)v_column.findViewById(R.id.editText);

        //資料
        dialogData = new AlertDialog.Builder(MainActivity.this).create();
        View v_data = inflater.inflate(R.layout.dialog_insert, null);
        dialogData.setView(v_data);
        dialogData.getClass();
        et_data = (EditText)v_data.findViewById(R.id.editText);

        //日期選擇 Dialog
        Calendar calendar = Calendar.getInstance();
        //datePickerDialog = new DatePickerDialog(this);
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String format = (month+1)+"月"+dayOfMonth+"日";
                try {
                    DBhelper.getWritableDatabase().execSQL(_temp + format + _temp2);
                }catch (Exception ex){
                    Toast.makeText(MainActivity.this, ex.toString(), Toast.LENGTH_SHORT).show();
                }
                print_table();
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));

        listView = (ListView)findViewById(R.id.list_view);

        //下拉式選單
        spinner = (Spinner)v_column.findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cTypeNum = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //取得tableList
        tableName="jay";
        tableList = new ArrayList();
        cTypeNum = 1;
        dataLength=512;

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(tableName);

        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mDrawer.openDrawer(GravityCompat.START);
            }
        });

        final NavigationView navigationView = (NavigationView)findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.action_add:
                        createTable(toolbar);
                        break;
                    //選擇資料表
                    case R.id.action_edit:
                        tableList.clear();
                        getTableList();
                        chooseTable(toolbar);

                        mDrawer.closeDrawer(Gravity.LEFT);
                        break;

                    case R.id.action_remove:
                        try {
                            String output = "";
                            DBhelper.getWritableDatabase().execSQL("DROP TABLE IF EXISTS " + tableName);
                            tableList.remove(tableName);

                            for(int i=0; i<tableList.size(); i++) {
                                output += tableList.get(i);
                                if(i<tableList.size()-1)
                                    output +=",";
                            }
                            outputStream = openFileOutput("table_list", Context.MODE_PRIVATE);
                            outputStream.write(output.getBytes());
                            outputStream.close();

                            chooseTable(toolbar);
                            Toast.makeText(MainActivity.this, "已刪除資料表: "+tableName, Toast.LENGTH_LONG).show();
                        }catch (Exception ex){};

                        mDrawer.closeDrawer(Gravity.LEFT);

                        break;
                }
                return false;
            }
        });
        //下方導覽欄
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    //新增資料
                    case R.id.action_add:
                        String output = "";
                        for(int i=0; i<tableList.size(); i++) {
                            if(tableList.get(i).length()>0)
                                output += tableList.get(i)+",";
                        }
                        Toast.makeText(MainActivity.this, "ouput:" + output + "\ncount:" + output.length(), Toast.LENGTH_LONG).show();
                        break;

                    //新增欄位
                    case R.id.action_edit:

                        try {
                            int length = -1;
                            String input = "";
                            byte[] buffer = new byte[dataLength];
                            inputStream = openFileInput("table_list");
                            while((length = inputStream.read(buffer)) != -1) {
                                input += new String(buffer, "UTF-8");
                            }
                            inputStream.close();
                        }catch (Exception ex) {
                            Toast.makeText(MainActivity.this, ex.toString(), Toast.LENGTH_LONG).show();
                        }

                        break;

                    case R.id.action_remove:

                        break;
                }
                return false;
            }
        });
        if(tableName != null) {
            print_table();
        }

        chooseTable(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            //增加欄位
            case R.id.action_add:
                try {
                    cursor = DBhelper.getReadableDatabase().query(tableName, null, null, null, null, null, null);
                    DBhelper.getWritableDatabase().execSQL("INSERT INTO " + tableName + "(id) VALUES (" + (cursor.getCount()) + ")");
                    cursor.close();
                    print_table();
                }catch (Exception ex) {
                    Toast.makeText(MainActivity.this, ex.toString(), Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.action_edit:
                try {
                    dialogColumn.setTitle("新增欄位");
                    dialogColumn.setButton(DialogInterface.BUTTON_NEGATIVE, "確定", new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String insert = et_column.getText().toString();
                            et_column.getText().clear();
                            try {
                                if (insert != "") {
                                    DBhelper.getWritableDatabase().execSQL("ALTER TABLE " + tableName + " ADD " + insert + " VCHAR(40)");
                                    DBhelper.getWritableDatabase().execSQL("UPDATE " + tableName + " SET " + insert + " = '" + cTypeNum + "' WHERE id = 0;");
                                    print_table();
                                    print_table();
                                }
                            }catch (Exception ex) {
                                Toast.makeText(MainActivity.this, ex.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    dialogColumn.show();
                }catch (Exception ex) {
                    Toast.makeText(MainActivity.this, ex.toString(), Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.action_remove:
                cursor = DBhelper.getReadableDatabase().query("temp_"+tableName, null, null, null, null, null, null);
                cursor.moveToFirst();
                Toast.makeText(MainActivity.this, cursor.getString(1), Toast.LENGTH_LONG).show();
                cursor.close();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    //輸出表格
    public boolean print_table() {
        tableLayout.removeAllViews();

        try {
            cursor = DBhelper.getReadableDatabase().query(tableName, null, null, null, null, null, null);
            int count = cursor.getColumnCount();
            if(count>0) {
                int[] dataType;
                dataType = new int[count];

                //Print 欄位名稱
                TableRow tr_cName = new TableRow(MainActivity.this);
                cursor.moveToFirst();
                for (int i = 1; i < count; i++) {
                    setTextView(tr_cName, cursor.getColumnName(i), i);
                    dataType[i] = cursor.getInt(i);
                }
                tableLayout.addView(tr_cName);

                //Print 內容
                while (cursor.moveToNext()) {
                    TableRow tr_date = new TableRow(MainActivity.this);
                    tr_date.setPadding(1, 20, 0, 20);
                    for (int i = 1; i < count; i++)
                        setTextView(tr_date, cursor.getString(i), cursor.getPosition(), cursor.getColumnName(i), dataType[i]);
                    tableLayout.addView(tr_date);
                }
            }
            cursor.close();
            return true;
        }catch(Exception ex){
            return false;
        }

    }
    //setTextView(TableRow, 欄位名稱, 欄位count)
    public void setTextView(TableRow tableRow, final String cName, final int count) {
        final TextView tv = new TextView(MainActivity.this);
        tv.setLayoutParams( new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));

        tv.setTextSize(20);
        tv.setMinWidth(100);
        tv.setMinHeight(40);
        tv.setPadding(20, 20, 20, 20);

        tv.setTextColor(Color.WHITE);
        tv.setBackgroundColor(Color.BLACK);

        tv.setText(cName);
        tv.setClickable(true);
        //OnClick 修改欄位名稱(function 複製&刪除)
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogData.setTitle(cName);
                et_data.requestFocus();
                dialogData.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                dialogData.setButton(DialogInterface.BUTTON_NEGATIVE, "確定", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String msg = et_data.getText().toString();
                        et_data.getText().clear();
                        if(msg != null) {
                            try {
                                //Toast.makeText(MainActivity.this, DBhelper.change(DBhelper.getWritableDatabase(), tableName, count, msg), Toast.LENGTH_LONG).show();
                                DBhelper.change(DBhelper.getWritableDatabase(), tableName, count, msg);

                                print_table();
                                print_table();
                            } catch (Exception ex) {
                                Toast.makeText(MainActivity.this, ex.toString(), Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
                dialogData.show();
            }
        });

        tv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                PopupMenu columnPopupMenu = new PopupMenu(MainActivity.this, tv);
                columnPopupMenu.getMenuInflater().inflate(R.menu.column_popup_menu, columnPopupMenu.getMenu());
                columnPopupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.addColumn:
                                addColumn();
                                break;

                            case R.id.deleteColumn:
                                DBhelper.alterTableDrop(DBhelper.getWritableDatabase(), tableName, cName);

                                print_table();
                                print_table();
                                break;

                            case R.id.clearColumn:
                                break;
                        }
                        return false;
                    }

                });
                columnPopupMenu.show();
                return true;
            }
        });
        tableRow.addView(tv);
    }
    //setTextView(TableRow, Data內容, id編號, 欄位名稱, 資料Type)
    public void setTextView(TableRow tableRow, String msg, final int id, final String cName, final int type) {
        final TextView tv = new TextView(MainActivity.this);
        tv.setLayoutParams( new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));

        tv.setTextSize(20);
        tv.setMinWidth(100);
        tv.setMinHeight(40);

        tv.setPadding(0, 10, 0, 10);
        tv.setTextColor(Color.WHITE);
        tv.setBackgroundColor(Color.BLACK);

        tv.setText(msg);
        tv.setClickable(true);
        //OnClick 修改資料內容
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogData.setTitle(cName);
                switch (type) {
                    //TEXT
                    case 0:
                        et_data.setInputType(InputType.TYPE_CLASS_TEXT);
                        columnEditer(cName, type, id);
                        break;
                    //INTEGER
                    case 1:
                        et_data.setInputType(InputType.TYPE_CLASS_NUMBER);
                        columnEditer(cName, type, id);
                        break;
                    //REAL
                    case 2:
                        et_data.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                        columnEditer(cName, type, id);
                        break;
                    //PHONE
                    case 4:
                        et_data.setInputType(InputType.TYPE_CLASS_PHONE);
                        columnEditer(cName, type, id);
                        break;
                    case 3:
                        _temp = "UPDATE " + tableName + " SET " + cName + " = '";
                        _temp2 = "' WHERE id = " + id + ";";
                        datePickerDialog.show();
                        break;
                }
            }
        });

        tv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                PopupMenu columnPopupMenu = new PopupMenu(MainActivity.this, tv);
                columnPopupMenu.getMenuInflater().inflate(R.menu.column_popup_menu, columnPopupMenu.getMenu());
                columnPopupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.addColumn:
                                addColumn();
                                break;

                            case R.id.deleteColumn:
                                DBhelper.alterTableDrop(DBhelper.getWritableDatabase(), tableName, cName);

                                print_table();
                                print_table();
                                break;

                            case R.id.clearColumn:
                                break;
                        }
                        return false;
                    }

                });
                columnPopupMenu.show();
                return true;
            }
        });
        tableRow.addView(tv);
    }
    //彈出Column TextEditer輸入
    public void columnEditer(final String cName, int type, final int id) {

        //自動彈出內容
        et_data.requestFocus();
        dialogData.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        dialogData.setButton(DialogInterface.BUTTON_NEGATIVE, "確定", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String msg = et_data.getText().toString();
                et_data.getText().clear();
                try {
                    DBhelper.getWritableDatabase().execSQL("UPDATE " + tableName + " SET " + cName + " = '" + msg + "' WHERE id = " + id + ";");
                    print_table();
                } catch (Exception ex) {
                    Toast.makeText(MainActivity.this, ex.toString( ), Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialogData.show();
    }
    //新增欄位
    public void addColumn(){
        try{
            dialogColumn.setTitle("新增欄位");
            et_data.setInputType(InputType.TYPE_CLASS_TEXT);
            et_data.requestFocus();
            dialogData.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            dialogColumn.setButton(DialogInterface.BUTTON_NEGATIVE, "確定", new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String insert = et_column.getText().toString();
                    et_column.getText().clear();
                    try {
                        if (insert != "") {
                            DBhelper.getWritableDatabase().execSQL("ALTER TABLE " + tableName + " ADD " + insert + " VCHAR(40)");
                            DBhelper.getWritableDatabase().execSQL("UPDATE " + tableName + " SET " + insert + " = '" + cTypeNum + "' WHERE id = 0;");

                            print_table();
                            print_table();
                        }
                    }catch (Exception ex) {
                        Toast.makeText(MainActivity.this, ex.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
            dialogColumn.show();
        }catch (Exception ex) {
            Toast.makeText(MainActivity.this, ex.toString(), Toast.LENGTH_SHORT).show();
        }
    }


    public void createTable(final Toolbar _toolbar){
        dialogData.setTitle("新增資料表");
        et_data.setInputType(InputType.TYPE_CLASS_TEXT);
        et_data.requestFocus();
        dialogData.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        dialogData.setButton(DialogInterface.BUTTON_NEGATIVE, "確定", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String msg = et_data.getText().toString();
                et_data.getText().clear();
                if(msg.matches("/_*/")) {
                    Toast.makeText(MainActivity.this, "禁止輸入非法字元:  _*/", Toast.LENGTH_LONG).show();
                }
                else if(msg != null) {
                    try {
                        tableName = msg;

                        try{
                            String sql = "CREATE TABLE IF NOT EXISTS " + tableName + " (id INTEGER PRIMARY KEY AUTOINCREMENT);";
                            DBhelper.getWritableDatabase().execSQL(sql);
                            DBhelper.getWritableDatabase().execSQL("INSERT INTO " + tableName + "(id) VALUES (0);");
                            tableList.add(tableName);
                        }
                        catch (SQLiteConstraintException sqlex) {
                            tableList.add(msg);
                            sqlex.printStackTrace();
                        }
                        String output = "";
                        for(int i=0; i<tableList.size(); i++) {
                            output += tableList.get(i);
                            if(i<tableList.size()-1)
                                output +=",";

                            Toast.makeText(MainActivity.this, "output:" +output+ "\ntableList:"+tableList.get(i), Toast.LENGTH_LONG).show();
                        }

                        outputStream = openFileOutput("table_list", Context.MODE_PRIVATE);
                        outputStream.write(output.getBytes());
                        outputStream.close();

                        mDrawer.closeDrawer(Gravity.LEFT);
                        _toolbar.setSubtitle(tableName);
                        print_table();
                        print_table();
                    }

                    catch (Exception ex) {

                        Toast.makeText(MainActivity.this, ex.toString(), Toast.LENGTH_LONG).show();
                    }
                }
                else
                    Toast.makeText(MainActivity.this, "請正確輸入資料表名稱。", Toast.LENGTH_LONG).show();
            }
        });
        dialogData.show();
    }
    public void chooseTable(final Toolbar _toolbar) {
        try {
            if (tableList.size() > 0) {
                String[] _list = new String[tableList.size()];
                for (int i = 0; i < tableList.size(); i++) {
                    _list[i] = tableList.get(i);
                }
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle("選擇資料表");
                dialog.setItems(_list, new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tableName = tableList.get(which);
                        _toolbar.setSubtitle(tableName);
                        if (!print_table()) {
                            Toast.makeText(MainActivity.this, "查無資料表: " + tableName, Toast.LENGTH_SHORT).show();
                            tableList.remove(tableName);
                        }
                    }
                });
                dialog.show();
            }
        }catch (Exception ex){ex.printStackTrace();}
    }

    //取得TableList
    public void getTableList(){
        try {
            int length = -1;
            String input = "";
            byte[] buffer = new byte[dataLength];
            inputStream = openFileInput("table_list");
            while((length = inputStream.read(buffer)) != -1) {
                String tempString = new String(buffer, 0, length, "UTF-8");
                input += tempString;
            }
            inputStream.close();
            String[] nameList = input.split(",");
            for(String tName : nameList) {
                if(tName.length()>0)
                    tableList.add(tName);
            }
        }catch (Exception ex) {
            Toast.makeText(MainActivity.this, ex.toString(), Toast.LENGTH_LONG).show();
        }
    }
}




