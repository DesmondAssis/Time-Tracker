package com.desmond.android.mytime10;

/**
 * Created by gk on 15/9/13.
 */

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import static com.desmond.android.mytime10.util.FileUtils.sd_mt_record_tiems_FileName;

public class DeleteRecord extends Activity {
    Button bt_delete;
    File f = new File(sd_mt_record_tiems_FileName);
    int i = 1;
    TextView tv_delete_summary;

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_deleterecord);
        this.bt_delete = ((Button) findViewById(R.id.bt_delete_records));
        this.bt_delete.setBackgroundResource(R.drawable.main0);
        this.tv_delete_summary = ((TextView) findViewById(R.id.tv_delete_records_title));
        this.tv_delete_summary.setTextColor(Color.parseColor("#87ceeb"));

        this.bt_delete.setOnClickListener(new OnClickListener() {
            public void onClick(View paramAnonymousView) {
                if (DeleteRecord.this.i > 0) {
                    DeleteRecord.this.tv_delete_summary.setText("真的要删除吗？");
                    DeleteRecord localDeleteRecord = DeleteRecord.this;
                    localDeleteRecord.i = (-1 + localDeleteRecord.i);
                } else {
                    if (DeleteRecord.this.f.exists()) {
                        DeleteRecord.this.f.delete();
                        DeleteRecord.this.tv_delete_summary.setText("你的时间记录已经删除.\n且行且珍惜...");
                    } else {
                        DeleteRecord.this.tv_delete_summary.setText("时间记录不存在...");
                    }
                }
            }
        });
    }
}