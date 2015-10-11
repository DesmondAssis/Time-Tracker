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
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.desmond.android.mytime10.util.DateTimeUtils;
import com.desmond.android.mytime10.util.FileUtils;
import com.desmond.android.mytime10.util.ProrityEnum;
import com.desmond.android.mytime10.util.SymbolContants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static com.desmond.android.mytime10.util.FileUtils.sd_mt_record_tiems_FileName;
import static com.desmond.android.mytime10.util.FileUtils.sd_mt_items_FileName;

public class Record extends AppCompatActivity {
    private DatePicker datePicker;
    private TimePicker timeStartPicker;
    private TimePicker timeEndPicker;

    private void initDateTimePicker() {
        this.datePicker = ((DatePicker) findViewById(R.id.datePicker1));
        this.datePicker.setCalendarViewShown(false);

        timeStartPicker = (TimePicker) findViewById(R.id.timeStartPicker);
        timeEndPicker = (TimePicker) findViewById(R.id.timeEndPicker);
        Calendar calendar = Calendar.getInstance();

        datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                        // start:test
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, monthOfYear, dayOfMonth);
                        SimpleDateFormat format = new SimpleDateFormat(
                                "EEE yyyy年MM月dd日", Locale.CHINA);
                        Toast.makeText(Record.this,
                                format.format(calendar.getTime()), Toast.LENGTH_SHORT)
                                .show();

                        // end:test
                    }
                });

        timeStartPicker.setIs24HourView(true);
//        timeStartPicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
//            @Override
//            public void onTimeChanged(TimePicker view, int hourOfDay,
//                                      int minute) {
//
//                Toast.makeText(Record.this,
//                        hourOfDay + "分钟" + minute + "分钟",
//                        Toast.LENGTH_SHORT).show();
//            }
//        });

        timeEndPicker.setIs24HourView(true);

//        timeEndPicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
//            @Override
//            public void onTimeChanged(TimePicker view, int hourOfDay,
//                                      int minute) {
//
//                Toast.makeText(Record.this,
//                        hourOfDay + "分钟" + minute + "分钟",
//                        Toast.LENGTH_SHORT).show();
//            }
//        });
    }


    private ArrayAdapter<String> adapter;
    private ArrayAdapter<Double> adapter2;
    private ArrayAdapter<String> adapter3;

    File f1 = new File(sd_mt_items_FileName);
    private List<String> items = new ArrayList();
    double[] items2 = {0, 0.5, 1, 1.5, 2, 2.5, 3d, 3.5, 4d, 4.5};
    String[] items3 = {"重要紧急", "重要不紧急", "紧急不重要", "不紧急也不重要"};
    private List<String> allItemList = new ArrayList();
    private List<Double> customTimeList = new ArrayList();
    private List<String> priorityThingList = new ArrayList();

    private Spinner mySpinner;
    private Spinner mySpinner2;
    private Spinner mySpinner3;

    String date = "";
    double resultCustomTime = 0d;
    String resultCurrentItem = "";
    int priority = 0;
    Button bt_save;

    // text view
    TextView tv_new_summary;
    TextView tv_latest_event;


    private void initList() {
        this.mySpinner = ((Spinner) findViewById(R.id.spinner1));
        this.mySpinner2 = ((Spinner) findViewById(R.id.spinner2));
        this.mySpinner3 = ((Spinner) findViewById(R.id.spinner3));

        // 所有的事件
        if (this.f1.exists()) {
            String x1 = FileUtils.readFileSdcard(sd_mt_items_FileName);
            if (!x1.isEmpty()) {
                String[] arrayOfString = x1.split(";");
                if (arrayOfString != null && arrayOfString.length > 0) {
                    for (String str : arrayOfString) {
                        allItemList.add(str);
                    }
                }
            }
        }
//        allItemList.add("test");
//        allItemList.add("adb");
        this.adapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, this.allItemList);
        this.adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        this.mySpinner.setAdapter(this.adapter);
        this.mySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong) {
                paramAnonymousAdapterView.setVisibility(View.VISIBLE);
                Record.this.resultCurrentItem = ((String) Record.this.adapter.getItem(paramAnonymousInt));
                Record.this.tv_new_summary.setText("某年某月的某一天," + Record.this.resultCurrentItem + "," + Record.this.resultCustomTime + "分钟");
            }

            public void onNothingSelected(AdapterView<?> paramAnonymousAdapterView) {
                paramAnonymousAdapterView.setVisibility(View.VISIBLE);
            }
        });

        // 整时(0.5,1,1.5,2)
        for (double d : items2) {
            customTimeList.add(d * 60d);
        }
        this.adapter2 = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, this.customTimeList);
        this.adapter2.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        this.mySpinner2.setAdapter(this.adapter2);
        this.mySpinner2.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong) {
                paramAnonymousAdapterView.setVisibility(View.VISIBLE);
                Record.this.resultCustomTime = ((double) Record.this.adapter2.getItem(paramAnonymousInt));
                Record.this.tv_new_summary.setText("某年某月的某一天," + Record.this.resultCurrentItem + "," + Record.this.resultCustomTime + "分钟");
            }

            public void onNothingSelected(AdapterView<?> paramAnonymousAdapterView) {
                paramAnonymousAdapterView.setVisibility(View.VISIBLE);
            }
        });

        // 事情紧急重要程度
        for (String str : items3) {
            this.priorityThingList.add(str);
        }

        this.adapter3 = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, this.priorityThingList);
        this.adapter3.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        this.mySpinner3.setAdapter(this.adapter3);
        this.mySpinner3.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong) {
                paramAnonymousAdapterView.setVisibility(View.VISIBLE);
                Record.this.priority = paramAnonymousInt;
                Record.this.tv_new_summary.setText("某年某月的某一天," + Record.this.resultCurrentItem + "," + Record.this.resultCustomTime + "分钟");
            }

            public void onNothingSelected(AdapterView<?> paramAnonymousAdapterView) {
                paramAnonymousAdapterView.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_record);

        // date picker
        initDateTimePicker();

        // list
        initList();

        // alert label
        this.tv_new_summary = ((TextView) findViewById(R.id.textView_new_summary));
        this.tv_new_summary.setTextColor(Color.parseColor("#87ceeb"));

        // latest event label
        this.tv_latest_event = ((TextView) findViewById(R.id.textView_latest_event));
        this.tv_latest_event.setTextColor(Color.parseColor("#87ceeb"));
        this.tv_latest_event.setText(getLatestEvent());

        // button
        this.bt_save = ((Button) findViewById(R.id.button_save));
        this.bt_save.setBackgroundResource(R.drawable.main0);
        this.bt_save.setOnClickListener(new OnClickListener() {
            public void onClick(View paramAnonymousView) {
                double duration = Record.this.resultCustomTime != 0 ? Record.this.resultCustomTime : getDuration();
                if (duration != 0) {
                    Record.this.date = (Record.this.datePicker.getYear() + "-" + (1 + Record.this.datePicker.getMonth()) + "-" + Record.this.datePicker.getDayOfMonth());
                    FileUtils.writeFileSdcard(sd_mt_record_tiems_FileName, Record.this.date + SymbolContants.COLON + Record.this.resultCurrentItem + SymbolContants.COLON
                            + duration + SymbolContants.COLON + Record.this.priority
                            + SymbolContants.COLON + getLastRecordTime() + SymbolContants.SEM);
                    Record.this.finish();
                }
            }
        });
    }

    private String getLatestEvent() {
        String x1 = FileUtils.readFileSdcard(sd_mt_record_tiems_FileName);
        String latestEvent = "no latest event!";
        if (!x1.isEmpty()) {
            String[] arrayOfString = x1.split(";");
            if (arrayOfString != null && arrayOfString.length > 0) {
                String dateFilter = DateTimeUtils.getCurrentDateStr();
                for (String str : arrayOfString) {
                    if(str != null && str.startsWith(dateFilter)) {
                        String[] strArr = str.split(SymbolContants.COLON);
                        latestEvent  = (strArr.length ==  5 ? strArr[4] : "legacy") + ":\n"
                                + strArr[0] + ": " + strArr[1] + ": " + strArr[2] + " 分钟" + ": "
                                + ProrityEnum.getByPrority(strArr[3]).getName();
                    }
                }
            }
        }

        return latestEvent;
    }

    private String getLastRecordTime() {
        double duration = Record.this.resultCustomTime != 0 ? Record.this.resultCustomTime : getDuration();
        if(Record.this.resultCustomTime != 0) {
            return DateTimeUtils.getCurrentTime();
        } else {
            return this.timeEndPicker.getCurrentHour() + "~" + this.timeEndPicker.getCurrentMinute();
        }
    }

    private double getDuration() {

        int startHour = this.timeStartPicker.getCurrentHour();
        int startMins = this.timeStartPicker.getCurrentMinute();

        int endHour = this.timeEndPicker.getCurrentHour();
        int endMins = this.timeEndPicker.getCurrentMinute();

        return (endHour * 60 + endMins) - (startHour * 60 + startMins);
    }
}