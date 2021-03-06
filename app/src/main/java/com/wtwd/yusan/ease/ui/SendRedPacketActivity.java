package com.wtwd.yusan.ease.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wtwd.yusan.R;
import com.wtwd.yusan.activity.RedPacketActivity;
import com.wtwd.yusan.ease.util.EditInputFilter;


/**
 * Created by ChangLe on 2018/4/25.
 */

public class SendRedPacketActivity extends BaseActivity implements View.OnClickListener {
    private EditText mEditText;
    private RelativeLayout mRlEdit;
    private Button mBtSend;
    private TextView mTvMoney;
    private EditInputFilter mEditInputFilter;
    private String toCharUsername;
    private String to;
    private static final int REQUEST_CODE_PAY_PACKET = 11;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.em_activity_envelopes);

        mEditText = findViewById(R.id.edittext);
        mRlEdit = findViewById(R.id.rl_edit);
        mBtSend = findViewById(R.id.btn_send);
        mTvMoney = findViewById(R.id.tv_money);
        mEditInputFilter = new EditInputFilter(this);
        InputFilter[] filters = {mEditInputFilter};
        mRlEdit.setOnClickListener(this);
        mBtSend.setOnClickListener(this);
        mEditText.addTextChangedListener(editWatcher);
        mEditText.setFilters(filters);
        mEditText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        to = getIntent().getExtras().getString("to");
        toCharUsername = getIntent().getExtras().getString("toid");
    }

    private TextWatcher editWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            String money = editable.toString().trim();
            if (!TextUtils.isEmpty(money) && !money.equals("0") && !money.equals("0.")) {
                if (money.toString().indexOf(".") == 0) {
                    String str = "0" + money;
                    mEditText.setText(str);
                    mEditText.setSelection(str.length());
                    mTvMoney.setText(str);
                } else {
                    mTvMoney.setText(money);
                }
            } else {
                mTvMoney.setText("0.00");
            }
        }
    };

    public void back(View view) {
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_send:
                String money = mTvMoney.getText().toString().trim();
                if (!TextUtils.isEmpty(money) && !money.equals("0.00")) {
                    //// TODO: 2018/5/4 跳转至支付界面
                    Bundle bundle = new Bundle();
                    bundle.putString("money",money);
                    bundle.putInt("trade_type",1);
                    bundle.putString("to",to);
                    bundle.putString("toid",toCharUsername);
                    Intent intent = new Intent(this,RedPacketActivity.class);
                    intent.putExtras(bundle);
                    startActivityForResult(intent,REQUEST_CODE_PAY_PACKET);
                  //  setResult(RESULT_OK, new Intent().putExtra("money", money));
                }
               // finish();
                break;
            case R.id.rl_edit:
                mEditText.setFocusable(true);
                mEditText.setFocusableInTouchMode(true);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_PAY_PACKET&&resultCode == 200){
            setResult( Activity.RESULT_OK,data);
            finish();
        }
    }
}