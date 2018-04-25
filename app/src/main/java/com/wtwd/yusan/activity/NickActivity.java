package com.wtwd.yusan.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.wtwd.yusan.R;
import com.wtwd.yusan.base.CommonToolBarActivity;

public class NickActivity extends CommonToolBarActivity implements View.OnClickListener {

    private TextView text_left;
    private TextView text_right;
    private EditText edit_nick;

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_nick;
    }

    @Override
    public View getSnackView() {
        return null;
    }

    @Override
    public void onCreateCommonView(Bundle saveInstanceState) {

        text_left = (TextView) findViewById(R.id.text_left);
        text_right = (TextView) findViewById(R.id.text_right);
        edit_nick = (EditText) findViewById(R.id.edit_nick);
        text_tool_bar_title.setText(R.string.modify_user_nick);

        addListener();
    }

    private void addListener() {
        text_left.setOnClickListener(this);
        text_right.setOnClickListener(this);
    }

    private boolean checkNickNull() {
        String mNick = edit_nick.getText().toString();
        if (TextUtils.isEmpty(mNick)) {
            showToast(getString(R.string.nick_not_null));
            return false;
        }
        return true;
    }


    @Override
    public void onClick(View v) {
        if (R.id.text_left == v.getId()) {
            finish();
        } else if (R.id.text_right == v.getId()) {
            if (checkNickNull()) {
                Intent mIntent = new Intent();
                mIntent.putExtra("nick_name", edit_nick.getText().toString());
                setResult(100, mIntent);
                finish();
            }
        }
    }
}
