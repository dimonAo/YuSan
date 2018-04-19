package com.wtwd.yusan.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wtwd.yusan.R;
import com.wtwd.yusan.base.CommonToolBarActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * time:2018/4/17
 * Created by w77996
 */

public class RechargeActivity extends CommonToolBarActivity{



    RecyclerView rcl_recharge;

    CheckBox ck_wechat_recharge;
    List<SelectBean> mNumList = new ArrayList<>();

    private String[] numData= {"5元","20","50","100"};

    RechargeNumAdapter mRechargeNumAdapter;
    @Override
    public void onCreateCommonView(Bundle saveInstanceState) {
        initData();
        initView();

    }

    /**
     * 初始化界面
     */
    private void initView() {
        text_tool_bar_title.setText("充值");
        rcl_recharge = (RecyclerView)findViewById(R.id.rcl_recharge);
        rcl_recharge.setLayoutManager(new GridLayoutManager(this,2));
        ck_wechat_recharge  = findViewById(R.id.ck_wechat_recharge);
        ck_wechat_recharge.setChecked(true);
        mRechargeNumAdapter = new RechargeNumAdapter(R.layout.item_recharge_num,mNumList);
        rcl_recharge.setAdapter(mRechargeNumAdapter);

        mRechargeNumAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                for(int i = 0; i < numData.length;i++){
                    mNumList.get(i).setSelect(false);
                }
                Log.e("dd",position+"");
                mNumList.get(position).setSelect(true);
                mRechargeNumAdapter.notifyDataSetChanged();
            }
        });
    }

    private void initData(){
        for(int i = 0; i<numData.length;i++){
            SelectBean selectBean = new SelectBean();
            selectBean.setNum(numData[i]);
            selectBean.setSelect(false);
            mNumList.add(selectBean);
        }
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_recharge;
    }

    @Override
    public View getSnackView() {
        return null;
    }

    private class RechargeNumAdapter extends BaseQuickAdapter<SelectBean,BaseViewHolder>{

        public RechargeNumAdapter(int layoutResId, @Nullable List<SelectBean> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, SelectBean item) {
            helper.setText(R.id.tv_item_recharge_num,item.getNum());
            if(item.isSelect() == true){
                helper.setBackgroundRes(R.id.lin_recharge,R.drawable.selector_recharge_num_select)
                        .setTextColor(R.id.tv_item_recharge_num, Color.WHITE);
            }else if(item.isSelect() == false){
                helper.setBackgroundRes(R.id.lin_recharge,R.drawable.selector_recharge_num)
                        .setTextColor(R.id.tv_item_recharge_num, Color.parseColor("#262626"));
            }
        }
    }

    private class SelectBean{
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
