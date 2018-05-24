package com.wtwd.yusan.ease.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.wtwd.yusan.R;


public class EditActivity extends BaseActivity {
    private EditText editText;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.em_activity_edit);

        editText = (EditText) findViewById(R.id.edittext);
        String data = getIntent().getStringExtra("groupName");
        if (data != null)
            editText.setText(data);

    }

    public void save(View view) {
        setResult(RESULT_OK, new Intent().putExtra("data", editText.getText().toString()));
        finish();
    }

    public void back(View view) {
        finish();
    }
}

