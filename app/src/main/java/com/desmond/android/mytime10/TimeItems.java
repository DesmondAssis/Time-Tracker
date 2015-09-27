package com.desmond.android.mytime10;

/**
 * Created by gk on 15/9/13.
 */

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.desmond.android.mytime10.util.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import static com.desmond.android.mytime10.util.FileUtils.sd_mt_items_FileName;

public class TimeItems extends AppCompatActivity {
    Button bt_ok_ti;
    List<String> data = new ArrayList();
    EditText et_new_li;
    File f = new File(sd_mt_items_FileName);
    ListView lv1;
    TextView tv_ti;
    String x = "";

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_timeitem);

        this.tv_ti = ((TextView) findViewById(R.id.tv_ti));
        this.tv_ti.setTextColor(Color.parseColor("#87ceeb"));

        this.lv1 = ((ListView) findViewById(R.id.lv_ti_all));
        String[] arrayOfString = null;
        if (this.f.exists()) {
            this.x = FileUtils.readFileSdcard(sd_mt_items_FileName);
            arrayOfString = this.x.split(";");
            if(arrayOfString != null && arrayOfString.length > 0) {
                for(String str : arrayOfString) {
                    this.data.add(str);
                }
            }
        }

        this.lv1.setAdapter(new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, this.data));
        this.lv1.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong) {
                Toast.makeText(TimeItems.this, "TIPS: 长按可以删除一项时间。", Toast.LENGTH_SHORT).show();
            }
        });
        this.lv1.setOnItemLongClickListener(new OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong) {
                TimeItems.this.tv_ti.setText("删除一项时间: " + (String) TimeItems.this.data.get(paramAnonymousInt));
                TimeItems.this.data.remove(paramAnonymousInt);
                FileUtils.writeFileSdcardNew(sd_mt_items_FileName, "");
                for (int i = 0; ; i++) {
                    if (i >= TimeItems.this.data.size()) {
                        updatelist();
                        if (TimeItems.this.data.size() == 0)
                            TimeItems.this.f.delete();
                        return false;
                    }
                    FileUtils.writeFileSdcard(sd_mt_items_FileName, ((String) TimeItems.this.data.get(i)).toString() + ";");
                }
            }
        });
        this.et_new_li = ((EditText) findViewById(R.id.et_new_ti));
        this.et_new_li.setOnClickListener(new OnClickListener() {
            public void onClick(View paramAnonymousView) {
                TimeItems.this.tv_ti.setText("增加一项新的时间");
            }
        });
        this.bt_ok_ti = ((Button) findViewById(R.id.button_ok_ti));
        this.bt_ok_ti.setOnClickListener(new OnClickListener() {
            public void onClick(View paramAnonymousView) {
                String str = TimeItems.this.et_new_li.getText().toString();
                if (!str.isEmpty()) {
                    FileUtils.writeFileSdcard(sd_mt_items_FileName, str + ";");
                    TimeItems.this.et_new_li.setText("");
                    TimeItems.this.data.add(str);
                    TimeItems.this.lv1.refreshDrawableState();
                    TimeItems.this.tv_ti.setText(" 全部的时间Record项");
                }
            }
        });
    }

    public void updatelist() {
        this.lv1.setAdapter(new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, this.data));
    }

}
