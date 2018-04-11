package com.wtwd.yusan.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.wtwd.yusan.R;
import com.wtwd.yusan.base.BaseActivity;

/**
 * time:2018/4/11
 * Created by w77996
 */

public class FeedBackActivity extends BaseActivity implements View.OnClickListener{
    /**
     * 反馈的内容
     */
    EditText ed_feedback_content;
    /**
     * 反馈内容的字数
     */
    TextView tv_feedback_num;
    /**
     * 提交反馈
     */
    Button btn_feedback_submit;
    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_feedback;
    }

    @Override
    public void onCreateView(Bundle saveInstanceState) {
        initView();

    }


    @Override
    public View getSnackView() {
        return null;
    }

    /**
     * 界面控件初始化
     */
    private void initView() {
        ed_feedback_content = (EditText)findViewById(R.id.ed_feedback_content);
        tv_feedback_num = (TextView)findViewById(R.id.tv_feedback_num);
        btn_feedback_submit = (Button)findViewById(R.id.btn_feedback_submit);

        ed_feedback_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // TODO: 2018/4/11 减少字数


                Editable editable = ed_feedback_content.getText();
                int len = editable.length();
                if (len > 200) {
                    //showToast("超出字数限制");
                    int selEndIndex = Selection.getSelectionEnd(editable);
                    String str = editable.toString();
                    //截取新字符串
                    String newStr = str.substring(0, 200);
                    ed_feedback_content.setText(newStr);
                    editable = ed_feedback_content.getText();

                    //新字符串的长度
                    int newLen = editable.length();
                    //旧光标位置超过字符串长度
                    if (selEndIndex > newLen) {
                        selEndIndex = editable.length();
                    }
                    //设置新光标所在的位置
                    Selection.setSelection(editable, selEndIndex);
                }else{
                    if(!TextUtils.isEmpty(charSequence.toString())){
                        tv_feedback_num.setText((200 - len)+"个字");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_feedback_submit:
                submitFeedback();
                break;
        }
    }

    /**
     * 提交反馈
     */
    private void submitFeedback() {
        // TODO: 2018/4/11 提交反馈
    }
}
