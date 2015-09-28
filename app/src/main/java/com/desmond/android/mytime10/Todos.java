package com.desmond.android.mytime10;

/**
 * Created by gk on 15/9/13.
 */

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.desmond.android.mytime10.util.DateTimeUtils;
import com.desmond.android.mytime10.util.FileUtils;
import com.desmond.android.mytime10.util.SymbolContants;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import static com.desmond.android.mytime10.util.FileUtils.sd_mt_items_FileName;
import static com.desmond.android.mytime10.util.FileUtils.sd_mt_record_tiems_FileName;

public class Todos extends AppCompatActivity {

    // text view
    private TextView text_view;

    // date picker
    private DatePicker datePicker;

    // text input
    private EditText inputText;

    // select
    private Spinner typeSpinner;

    // button

    private Button bt_save;

    // data
    private ArrayAdapter<String> typeAdapater;
    String[] types = {"当天", "当前周", "当月"}; // daily,weekly,monthly
    private List<String> typeList = new ArrayList();
    
    // thing recording
    private int type = 0;

    private void initDateTimePicker() {
        this.datePicker = ((DatePicker) findViewById(R.id.datePicker1));
        this.datePicker.setCalendarViewShown(false);
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
                        Toast.makeText(Todos.this,
                                format.format(calendar.getTime()), Toast.LENGTH_SHORT)
                                .show();

                        // end:test
                    }
                });
    }

    @Override
    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_todos);

        // alert label
        this.text_view = ((TextView) findViewById(R.id.textView_new_summary));
        this.text_view.setText("请选择TODO task类型,默认当天");
        this.text_view.setTextColor(Color.parseColor("#87ceeb"));

        // date picker
        initDateTimePicker();

        // list
        initList();

        // input tasks
        this.inputText = ((EditText) findViewById(R.id.et_new_ti));

        // button
        this.bt_save = ((Button) findViewById(R.id.button_save));
        this.bt_save.setBackgroundResource(R.drawable.main0);
        this.bt_save.setOnClickListener(new OnClickListener() {
            public void onClick(View paramAnonymousView) {
                if(Todos.this.inputText.getText() != null && !"".equals(Todos.this.inputText.getText().toString())) {
                    String task = Todos.this.inputText.getText().toString();
                    writeToSD(task);

                    Todos.this.finish();
                }
            }
        });
    }

    private void initList() {
        this.typeSpinner = ((Spinner) findViewById(R.id.spinner2));

        for (String str : types) {
            this.typeList.add(str);
        }

        this.typeAdapater = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, this.typeList);
        this.typeAdapater.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        this.typeSpinner.setAdapter(this.typeAdapater);
        this.typeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong) {
                paramAnonymousAdapterView.setVisibility(View.VISIBLE);
                Todos.this.type = paramAnonymousInt;
                Todos.this.text_view.setText("任务类型: " + Todos.this.types[Todos.this.type]);
            }

            public void onNothingSelected(AdapterView<?> paramAnonymousAdapterView) {
                paramAnonymousAdapterView.setVisibility(View.VISIBLE);
            }
        });
    }

    private void writeToSD(String task) {
        int type = this.type;
        String fileName = FileUtils.sd_mt_todo_monthly_FileName;
        int year = datePicker.getYear();
        StringBuilder taskSb = new StringBuilder();
        taskSb.append(year).append("-");
        switch (type) {
            case 0 : {
                taskSb.append(1 + this.datePicker.getMonth()).append("-").append(this.datePicker.getDayOfMonth());
                fileName = FileUtils.sd_mt_todo_daily_FileName;
                break;
            }
            case 1: {
                taskSb.append(DateTimeUtils.getWeekOfYear(1));
                fileName = FileUtils.sd_mt_todo_weekly_FileName;
                break;
            }
            default: {
                taskSb.append(1 + this.datePicker.getMonth());
                break;
            }
        }
        taskSb.append(SymbolContants.COLON).append(task).append(SymbolContants.SEM);
        FileUtils.writeFileSdcard(fileName, taskSb.toString());
    }
}