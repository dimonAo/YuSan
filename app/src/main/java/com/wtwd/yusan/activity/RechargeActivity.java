package com.wtwd.yusan.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wtwd.yusan.R;
import com.wtwd.yusan.base.CommonToolBarActivity;
import com.wtwd.yusan.entity.PrepayIdEntity;
import com.wtwd.yusan.entity.ResultEntity;
import com.wtwd.yusan.entity.TaskEntity;
import com.wtwd.yusan.util.Constans;
import com.wtwd.yusan.util.GsonUtils;
import com.wtwd.yusan.util.Pref;
import com.wtwd.yusan.util.Utils;
import com.wtwd.yusan.util.WXpayUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;

/**
 * time:2018/4/17
 * Created by w77996
 */

public class RechargeActivity extends CommonToolBarActivity implements View.OnClickListener {


    RecyclerView rcl_recharge;

    Button btn_recharg;

    EditText ed_recharge;

    CheckBox ck_wechat_recharge;
    List<SelectBean> mNumList = new ArrayList<>();

    private String[] numData = {"5元", "20元", "50元", "100元"};

    RechargeNumAdapter mRechargeNumAdapter;
    private int selectionStart;
    private int selectionEnd;

    private int selectedNumItem = 5;


    @Override
    public void onCreateCommonView(Bundle saveInstanceState) {
        initData();
        initView();
        addListener();

    }

    private void addListener() {
        btn_recharg.setOnClickListener(this);
        ed_recharge.setOnClickListener(this);
    }

    /**
     * 初始化界面
     */
    private void initView() {
        text_tool_bar_title.setText(R.string.packet_recharge);
        rcl_recharge = (RecyclerView) findViewById(R.id.rcl_recharge);
        rcl_recharge.setLayoutManager(new GridLayoutManager(this, 2));
        ck_wechat_recharge = findViewById(R.id.ck_wechat_recharge);
        ck_wechat_recharge.setChecked(true);
        mRechargeNumAdapter = new RechargeNumAdapter(R.layout.item_recharge_num, mNumList);
        rcl_recharge.setAdapter(mRechargeNumAdapter);
        btn_recharg = findViewById(R.id.btn_recharg);
        ed_recharge = findViewById(R.id.ed_recharge);

        mRechargeNumAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                for (int i = 0; i < numData.length; i++) {
                    mNumList.get(i).setSelect(false);
                }
                Log.e("dd", position + "");
                selectedNumItem = 5+5*position;
                mNumList.get(position).setSelect(true);
                mRechargeNumAdapter.notifyDataSetChanged();
            }
        });

        ed_recharge.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                selectionStart = ed_recharge.getSelectionStart();
                selectionEnd = ed_recharge.getSelectionEnd();
                if (!isOnlyPointNumber(ed_recharge.getText().toString()) && !TextUtils.isEmpty(s.toString())) {
                    // PromptManager.showToast(context,"您输入的数字保留在小数点后两位");
                    //删除多余输入的字（不会显示出来）
                    s.delete(selectionStart - 1, selectionEnd);
                    ed_recharge.setText(s);
                    ed_recharge.setSelection(s.length());
                }

            }
        });
    }

    private void initData() {
        for (int i = 0; i < numData.length; i++) {
            SelectBean selectBean = new SelectBean();
            selectBean.setNum(numData[i]);
            selectBean.setSelect(false);
            if (i == 0) {
                selectBean.setSelect(true);
            }

            mNumList.add(selectBean);
        }
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_recharge;
    }

    @Override
    public View getSnackView() {
        return rcl_recharge;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_recharg:
                float rechageData = 0l;
                String rechageMoney = ed_recharge.getText().toString();
                if(null == rechageMoney||"".equals(rechageMoney)|| 0 == Float.parseFloat(rechageMoney)){
                    rechageData = Float.parseFloat(selectedNumItem+"");
                    rechargMoney(rechageData);
                }else{
                    Log.e(TAG,rechageMoney);
                   /* float rechageData = Float.parseFloat(ed_recharge.getText().toString());
                    Log.e(TAG, rechageData + "");*/
                   rechageData = Float.parseFloat(rechageMoney);
                    rechargMoney(rechageData);
                }

              /*  int total_fee = Integer.parseInt((rechageData * 100)+"");
                Log.e(TAG,total_fee+"");
                if(rechageData <=0){
                    showToast("输入错误");
                    return;
                }*/
                // rechargMoney(rechageData);
                break;
        }
    }

    /**
     * 充值,请求服务器返回preperId
     */
    private void rechargMoney(float rechargData) {

//        double total_fee = Double.parseDouble((rechargData * 100) + "");
//        Log.e(TAG,((int)total_fee)+"");
        OkHttpUtils.get()
                .addParams("userId", Pref.getInstance(this).getUserId()+"")
                .addParams("body", getString(R.string.recharge_for_user))//商品描述
                .addParams("money", rechargData+"")
                .addParams("spbill_create_ip", Utils.getIPAddress(this))
                .addParams("trade_type", "APP")
                .addParams("payType",0+"")
                .addParams("type",1+"")
                .url(Constans.GET_WX_PERPERID)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e(TAG, response.toString());
                        JSONObject result = null;
                        try {
                            result = new JSONObject(response);
                            int status = result.optInt("status");
                            if(1 == status){
                                String mPerpayIdJson= result.optString("object");
                                Log.e(TAG, "mTaskJsonArray --> " + mPerpayIdJson);
                                JSONObject prepay = new JSONObject(mPerpayIdJson);
                                String prepayid = prepay.getString("prepayid");
                                PrepayIdEntity m= GsonUtils.GsonToBean(mPerpayIdJson,PrepayIdEntity.class);
                                Log.e(TAG,m.toString());
                                WXpayUtils.Pay(prepayid);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

    }

    private class RechargeNumAdapter extends BaseQuickAdapter<SelectBean, BaseViewHolder> {

        public RechargeNumAdapter(int layoutResId, @Nullable List<SelectBean> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, SelectBean item) {
            helper.setText(R.id.tv_item_recharge_num, item.getNum());
            if (item.isSelect() == true) {
                helper.setBackgroundRes(R.id.lin_recharge, R.drawable.selector_recharge_num_select)
                        .setTextColor(R.id.tv_item_recharge_num, Color.WHITE);
            } else if (item.isSelect() == false) {
                helper.setBackgroundRes(R.id.lin_recharge, R.drawable.selector_recharge_num)
                        .setTextColor(R.id.tv_item_recharge_num, Color.parseColor("#262626"));
            }
        }
    }

    public static boolean isOnlyPointNumber(String number) {//保留两位小数正则
        Pattern pattern = Pattern.compile("^\\d+\\.?\\d{0,2}$");
        Matcher matcher = pattern.matcher(number);
        return matcher.matches();
    }

    public static boolean isMoney(String str) {
        Pattern pattern = Pattern.compile("^(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){0,2})?$"); // 判断小数点后2位的数字的正则表达式
        Matcher match = pattern.matcher(str);
        if (match.matches() == false) {
            return false;
        } else {
            return true;
        }
    }

    private class SelectBean {
        String num;
        boolean isSelect;

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }

        public boolean isSelect() {
            return isSelect;
        }

        public void setSelect(boolean select) {
            isSelect = select;
        }
    }
}
