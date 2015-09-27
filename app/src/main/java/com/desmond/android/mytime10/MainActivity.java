package com.desmond.android.mytime10;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.desmond.android.mytime10.util.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.desmond.android.mytime10.util.FileUtils.*;

/**
 * Created by gk on 15/9/25.
 */
public class MainActivity extends AppCompatActivity {
    private Button bt_record_items;
    private Button button_record_time;
    private Button button_del_items;

    private ListView listAllRecords;
    List<String> data = new ArrayList();
    File mytimeRecordItems = new File(sd_mt_record_tiems_FileName);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // init
        init();

        this.listAllRecords = ((ListView) findViewById(R.id.lv_ti_list));
       initList();

    }

    @Override
    protected void onResume() {
        super.onResume();

        // 刷新列表
        initList();
        this.listAllRecords.refreshDrawableState();
    }

    private void init() {
        // buttons
        initButtons();
    }

    private void initList() {
        this.data.clear();
        if (this.mytimeRecordItems.exists()) {
            String allRecordItems = FileUtils.readFileSdcard(sd_mt_record_tiems_FileName);
            String[] arrayOfString = allRecordItems.split(";");
            if(arrayOfString != null && arrayOfString.length > 0) {
                for(String str : arrayOfString) {
                    this.data.add(str);
                }
            }
        }

        this.listAllRecords.setAdapter(new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, this.data));
    }

    private void initButtons() {
        // 记录时间去哪儿
        this.bt_record_items = ((Button) findViewById(R.id.button_record_items));
        this.bt_record_items.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                Intent manageItems = new Intent();
                manageItems.setClass(MainActivity.this, TimeItems.class);
                startActivityForResult(manageItems, 0);
            }
        });

        // 新建事件
        this.button_record_time = ((Button) findViewById(R.id.button_record_time));
        this.button_record_time.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                Intent manageItems = new Intent();
                manageItems.setClass(MainActivity.this, Record.class);
                startActivityForResult(manageItems, 0);
            }
        });

        // 删除记录
        this.button_del_items = ((Button) findViewById(R.id.button_del_items));
        this.button_del_items.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                Intent manageItems = new Intent();
                manageItems.setClass(MainActivity.this, DeleteRecord.class);
                startActivityForResult(manageItems, 0);
            }
        });
    }
}
