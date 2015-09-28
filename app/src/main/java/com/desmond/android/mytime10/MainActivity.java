package com.desmond.android.mytime10;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.desmond.android.mytime10.util.DateTimeUtils;
import com.desmond.android.mytime10.util.FileUtils;
import com.desmond.android.mytime10.util.ProrityEnum;
import com.desmond.android.mytime10.util.SymbolContants;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import static com.desmond.android.mytime10.util.FileUtils.*;

/**
 * Created by gk on 15/9/25.
 */
public class MainActivity extends AppCompatActivity {
    // prority alert text
    private TextView textView_p0;
    private TextView textView_p1;
    private TextView textView_p2;
    private TextView textView_p3;


    // buttons
    private Button bt_record_items;
    private Button button_record_time;
    private Button button_del_items;
    private Button button_todos;

    private TextView dateAlert;
    private TextView textTop3;

    private ListView listAllRecords;
    List<String> data = new ArrayList();

    // daily tasks
    private ListView listDailyTasks;
    List<String> dailyData = new ArrayList();

    // weekly tasks
    private ListView listWeeklyTasks;
    List<String> weeeklyData = new ArrayList();

    // monthly tasks
    private ListView listMonthlyTasks;
    List<String> monthlyData = new ArrayList();

    private int textFlag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // init
        init();

        // alert text
        this.textView_p0 = (TextView)findViewById(R.id.textView_p0);
        this.textView_p1 = (TextView)findViewById(R.id.textView_p1);
        this.textView_p2 = (TextView)findViewById(R.id.textView_p2);
        this.textView_p3 = (TextView)findViewById(R.id.textView_p3);


        // list
        this.listAllRecords = ((ListView) findViewById(R.id.lv_ti_list));
        this.listDailyTasks = ((ListView) findViewById(R.id.lv_daily_task_list));
        this.listWeeklyTasks = ((ListView) findViewById(R.id.lv_weekly_task_list));
        this.listMonthlyTasks = ((ListView) findViewById(R.id.lv_monthly_task_list));
        this.listDailyTasks.setBackgroundColor(Color.rgb(179, 255, 100));
        this.listWeeklyTasks.setBackgroundColor(Color.rgb(179, 205, 125));
        this.listMonthlyTasks.setBackgroundColor(Color.rgb(179, 255, 0));

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
        // alert label
        this.dateAlert = (TextView)findViewById(R.id.textView_new_summary);
        this.dateAlert.setText(DateTimeUtils.getCurrentDateStr());
        this.dateAlert.setTextColor(Color.parseColor("#87ceeb"));
        this.textTop3 = (TextView)findViewById(R.id.textView_top3);
        this.textTop3.setTextColor(Color.parseColor("#87ceeb"));
        final View.OnClickListener textListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String alertText = DateTimeUtils.getCurrentDateStr();
                String top3 = "今天时间使用TOP 3";
                if(textFlag == 1) { // 月
                    alertText = DateTimeUtils.getCurrentMonthStr();
                    top3 = "本月时间使用TOP 3";
                } else if(textFlag == 2) { // 年
                    alertText = DateTimeUtils.getCurrentYearStr();
                    top3 = "今年时间使用TOP 3";
                }

                MainActivity.this.dateAlert.setText(alertText);
                MainActivity.this.textTop3.setText(top3);

                MainActivity.this.data = getTop3();
                updatePrority();
                updateTop3List();

                textFlag++;
                if(textFlag == 3) {
                    textFlag = 0;
                }
            }
        };
        this.dateAlert.setOnClickListener(textListener);

        // buttons
        initButtons();
    }

    private Map<Integer, String> getProrityMap() {
        List<String> sortList = filterRecordItems();
        Map<Integer, String> allRecords = new HashMap<Integer, String>(3);
        if(sortList != null) {
            double p0 = 0d, p1 = 0d, p2 = 0d, p3= 0d;
            for(String str : sortList) {
                String[] itm = str.split(SymbolContants.COLON);
                int prority = Integer.parseInt(itm[3]);
                double spending = Double.parseDouble(itm[2]);
                switch (prority) {
                    case 0 : p0 += spending; break;
                    case 1 : p1 += spending; break;
                    case 2 : p2 += spending; break;
                    case 3 : p3 += spending; break;
                }
            }

            double total = p0 + p1 + p2 + p3;
            allRecords.put(0, percent(p0, total) + " -- " +  p0 + " m");
            allRecords.put(1, percent(p1, total) + " -- " +  p1 + " m");
            allRecords.put(2, percent(p2, total) + " -- " +  p2 + " m");
            allRecords.put(3, percent(p3, total) + " -- " +  p3 + " m");
        }

        return allRecords;
    }

    private int percent(double p, double total) {
        double per = 0;
        if (total != 0) {
            per = p / total;
        }

        return (int) (per * 100);
    }

    private List<String> getTop3() {
        List<String> sortList = filterRecordItems();
        List<String> allRecords = new ArrayList<String>();
        if(sortList != null) {
            for(String str : sortList) {
                String[] itm = str.split(SymbolContants.COLON);
                allRecords.add(itm[1] + ": " + itm[2] + " 分钟" + ": " + ProrityEnum.getByPrority(itm[3]).getName());
            }
        }

        return allRecords;
    }

    private List<String> filterRecordItems() {
        List<String> filteredList = new ArrayList<String>();
        List<String> dataList = readSingltonToList(sd_mt_record_tiems_FileName);
        String filterDateStr = this.dateAlert.getText().toString();
        if(dataList != null) {
            for(String str : dataList) {
                if(str.startsWith(filterDateStr)) {
                    filteredList.add(str);
                }
            }
        }

        List<String> sortedList = new ArrayList<String>(filteredList);

        Collections.sort(sortedList, new Comparator<String>() {
            @Override
            public int compare(String lhs, String rhs) {
                String[] ldata = lhs.split(SymbolContants.COLON);
                String[] rdata = rhs.split(SymbolContants.COLON);

                return Double.parseDouble(ldata[2]) - Double.parseDouble(rdata[2]) <= 0 ? 1 : -1;
            }
        });

        return sortedList;
    }

    private void updateTop3List() {
        this.listAllRecords.setAdapter(new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, this.data));
        this.listAllRecords.refreshDrawableState();
    }

    private void updatePrority() {
        Map<Integer, String> prorityMap =  getProrityMap();
        textView_p0.setText(prorityMap.get(0));
        textView_p1.setText(prorityMap.get(1));
        textView_p2.setText(prorityMap.get(2));
        textView_p3.setText(prorityMap.get(3));
    }


    private void initList() {
        // prority text
        updatePrority();

        // top 3
        this.data.clear();
        this.data.addAll(getTop3());
        this.listAllRecords.setAdapter(new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, this.data));

        // daily
        this.dailyData.clear();
        readSingltonToList(SymbolContants.DAILY);
        this.listDailyTasks.setAdapter(new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, this.dailyData));
        this.listDailyTasks.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong) {
                Toast.makeText(MainActivity.this, "TIPS: 长按可以删除一项时间。", Toast.LENGTH_SHORT).show();
            }
        });

        this.listDailyTasks.setOnItemLongClickListener(new OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong) {
                String currentDateStr = DateTimeUtils.getCurrentDateStr();
                List<String> list = readSingltonToList(sd_mt_todo_daily_FileName);
                if (list != null && list.size() > 0) {
                    for (String str : list) {
                        if (str.equals(currentDateStr + SymbolContants.COLON + MainActivity.this.dailyData.get(paramAnonymousInt))) {
                            list.remove(str);
                        }
                    }
                }

                MainActivity.this.dailyData.remove(paramAnonymousInt);
                updatelist(SymbolContants.DAILY);

                StringBuilder sb = new StringBuilder();
                for (String str : list) {
                    sb.append(str).append(SymbolContants.SEM);
                }

//                FileUtils.delete(sd_mt_todo_daily_FileName);
                FileUtils.writeFileSdcardNew(sd_mt_todo_daily_FileName, sb.toString());

                return true;
            }
        });

        // weekly
        this.weeeklyData.clear();
        readSingltonToList(SymbolContants.WEEKLY);
        this.listWeeklyTasks.setAdapter(new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, this.weeeklyData));
        this.listWeeklyTasks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong) {
                Toast.makeText(MainActivity.this, "TIPS: 长按可以删除一项时间。", Toast.LENGTH_SHORT).show();
            }
        });
        this.listWeeklyTasks.setOnItemLongClickListener(new OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong) {

                String currentWeekStr = DateTimeUtils.getWeekOfYear();
                List<String> list = readSingltonToList(sd_mt_todo_weekly_FileName);
                if (list != null && list.size() > 0) {
                    for (String str : list) {
                        if (str.equals(currentWeekStr + SymbolContants.COLON + MainActivity.this.weeeklyData.get(paramAnonymousInt))) {
                            list.remove(str);
                        }
                    }
                }

                MainActivity.this.weeeklyData.remove(paramAnonymousInt);
                updatelist(SymbolContants.WEEKLY);

                StringBuilder sb = new StringBuilder();
                for (String str : list) {
                    sb.append(str).append(SymbolContants.SEM);
                }

//                FileUtils.delete(sd_mt_todo_weekly_FileName);
                FileUtils.writeFileSdcardNew(sd_mt_todo_weekly_FileName, sb.toString());

                return false;
            }
        });

        // monthly
        this.monthlyData.clear();
        readSingltonToList(SymbolContants.MONTHLY);
        this.listMonthlyTasks.setAdapter(new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, this.monthlyData));
        this.listMonthlyTasks.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong) {
                Toast.makeText(MainActivity.this, "TIPS: 长按可以删除一项时间。", Toast.LENGTH_SHORT).show();
            }
        });
        this.listMonthlyTasks.setOnItemLongClickListener(new OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong) {

                String currentMonthStr = DateTimeUtils.getCurrentMonthStr();
                List<String> list = readSingltonToList(sd_mt_todo_monthly_FileName);
                if (list != null && list.size() > 0) {
                    for (String str : list) {
                        if (str.equals(currentMonthStr + SymbolContants.COLON + MainActivity.this.monthlyData.get(paramAnonymousInt))) {
                            list.remove(str);
                        }
                    }
                }

                MainActivity.this.monthlyData.remove(paramAnonymousInt);
                updatelist(SymbolContants.MONTHLY);

                StringBuilder sb = new StringBuilder();
                for (String str : list) {
                    sb.append(str).append(SymbolContants.SEM);
                }

//                FileUtils.delete(sd_mt_todo_monthly_FileName);
                FileUtils.writeFileSdcardNew(sd_mt_todo_monthly_FileName, sb.toString());

                return false;
            }
        });

    }

    private void readSingltonToList(int type) {

        switch (type) {
            case SymbolContants.DAILY : {
                String currentDateStr = DateTimeUtils.getCurrentDateStr();
                List<String> list = readSingltonToList(sd_mt_todo_daily_FileName);
                if(list != null && list.size() > 0) {
                    for(String str : list) {
                        if(str.startsWith(currentDateStr)) {
                            String data[] = str.split(SymbolContants.COLON);
                            this.dailyData.add(data[1]);
                        }
                    }
                }
                break;
            }
            case SymbolContants.WEEKLY: {
                String currentWeek = DateTimeUtils.getWeekOfYear();
                List<String> list = readSingltonToList(sd_mt_todo_weekly_FileName);
                if(list != null && list.size() > 0) {
                    for(String str : list) {
                        if(str.startsWith(currentWeek)) {
                            String data[] = str.split(SymbolContants.COLON);
                            this.weeeklyData.add(data[1]);
                        }
                    }
                }
                break;
            }
            default: {
                String currentMonthStr = DateTimeUtils.getCurrentMonthStr();
                List<String> list = readSingltonToList(sd_mt_todo_monthly_FileName);
                if(list != null && list.size() > 0) {
                    for(String str : list) {
                        if(str.startsWith(currentMonthStr)) {
                            String data[] = str.split(SymbolContants.COLON);
                            this.monthlyData.add(data[1]);
                        }
                    }
                }
                break;
            }
        }
    }

    private List<String> readSingltonToList(String path) {
        File file = new File(path);
        List<String> recordList = new ArrayList<String>();

        if(file.exists()) {
            String str = FileUtils.readFileSdcard(path);
            String[] arrayOfString = str.split(SymbolContants.SEM);
            if (arrayOfString != null && arrayOfString.length > 0) {
                for (String r : arrayOfString) {
                    recordList.add(r);
                }
            }
        }

        return recordList;
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

        // TODOs
        this.button_todos = ((Button) findViewById(R.id.button_todos));
        this.button_todos.setBackgroundColor(Color.rgb(119, 255, 220));
        this.button_todos.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                Intent manageItems = new Intent();
                manageItems.setClass(MainActivity.this, Todos.class);
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

    public void updatelist(int type) {
        switch (type) {
            case SymbolContants.DAILY : {
                this.listDailyTasks.setAdapter(new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, this.dailyData));
                break;
            }
            case SymbolContants.WEEKLY: {
                this.listWeeklyTasks.setAdapter(new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, this.weeeklyData));
                break;
            }
            case SymbolContants.MONTHLY : {
                this.listMonthlyTasks.setAdapter(new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, this.monthlyData));
                break;
            }
        }
    }
}
