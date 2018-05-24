package com.wtwd.yusan.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.textservice.TextInfo;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wtwd.yusan.R;
import com.wtwd.yusan.base.CommonToolBarActivity;
import com.wtwd.yusan.entity.LastVersionEntity;
import com.wtwd.yusan.entity.ResultEntity;
import com.wtwd.yusan.util.Constans;
import com.wtwd.yusan.util.GsonUtils;
import com.wtwd.yusan.util.Pref;
import com.wtwd.yusan.util.Utils;
import com.wtwd.yusan.widget.recycler.EasyRefreshLayout;
import com.wtwd.yusan.widget.recycler.LoadModel;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

/**
 * time:2018/4/17
 * Created by w77996
 */

public class PacketDetailActivity extends CommonToolBarActivity {

    EasyRefreshLayout easylayout_packetdetail;

    RecyclerView recycler_packetdetail;

    PacketDetailAdapter mPacketDetailAdapter;



    /**
     * 加载类型
     * 1：上拉加载
     * 2：下拉刷新
     */
    private int mLoadTYpe;

    /**
     * 上拉加载次数
     */
    private int mLoadCount;

    @Override
    public void onCreateCommonView(Bundle saveInstanceState) {
        initView();
    }


    @Override
    protected void onResume() {
        super.onResume();

        initListener();


        mLoadTYpe = 0;
        mLoadCount = 0;
        getPacketDetail(0, 20);
    }

    private void initView() {
        text_tool_bar_title.setText(R.string.packet_detail_title);
        recycler_packetdetail = findViewById(R.id.recycler_packetdetail);
        easylayout_packetdetail = findViewById(R.id.easylayout_packetdetail);
        recycler_packetdetail.setLayoutManager(new LinearLayoutManager(this));

        mPacketDetailAdapter = new PacketDetailAdapter(R.layout.item_packet_detail, null);
        recycler_packetdetail.setAdapter(mPacketDetailAdapter);

        //initListener();
        //getPacketDetail(mLoadCount * 20, 20);

    }

    private void initListener() {
        easylayout_packetdetail.setLoadMoreModel(LoadModel.ADVANCE_MODEL, 5);

        easylayout_packetdetail.addEasyEvent(new EasyRefreshLayout.EasyEvent() {
            @Override
            public void onLoadMore() {
                mLoadTYpe = 1;
                getPacketDetail(mLoadCount * 20, 20);

                mLoadCount++;
            }

            @Override
            public void onRefreshing() {
                mLoadTYpe = 2;
                mLoadCount = 0;
                getPacketDetail(0, 20);

            }
        });
    }

    private void getPacketDetail(int start, int count) {

        final List<WalletLogEntity> mList = new ArrayList<>();

        HashMap<String, String> params = new HashMap<>();
        params.put("start", start + "");
        params.put("count", count + "");
        params.put("userId", Pref.getInstance(this).getUserId() + "");

        OkHttpUtils.get()
                .url(Constans.GET_PACKETDETAIL)
                .params(params)
                .build()
                .connTimeOut(Constans.TIME_OUT)
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        if (1 == mLoadTYpe) {
                            easylayout_packetdetail.loadMoreComplete();
                            easylayout_packetdetail.closeLoadView();

                        } else if (2 == mLoadTYpe) {
                            easylayout_packetdetail.refreshComplete();
                        }
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e(TAG, response.toString());

                        JSONObject mLogJson = null;
                        try {
                            mLogJson = new JSONObject(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        int status = mLogJson.optInt("status");
                        String mLogJsonArray = mLogJson.optString("object");
                        List<WalletLogEntity> list = GsonUtils.jsonToList(mLogJsonArray, WalletLogEntity.class);
                        Log.e(TAG, list.size() + "");


                        if (1 == status) {
                            mList.addAll(list);
                            for (WalletLogEntity entity : list) {
                                Log.e(TAG, entity.toString());
                            }
                            if (mList.size() < 20) {
                                easylayout_packetdetail.setLoadMoreModel(LoadModel.NONE); //取消加载更多
                            } else {
                                easylayout_packetdetail.setLoadMoreModel(LoadModel.COMMON_MODEL);
                            }
                            Log.e(TAG,mLoadTYpe+"");
                            if (1 == mLoadTYpe) {
                                easylayout_packetdetail.loadMoreComplete();
                                easylayout_packetdetail.closeLoadView();
                                int position = mPacketDetailAdapter.getData().size();
                                mPacketDetailAdapter.getData().addAll(mList);
                                mPacketDetailAdapter.notifyDataSetChanged();
                                recycler_packetdetail.scrollToPosition(position);
                            } else if (2 == mLoadTYpe) {
                                mPacketDetailAdapter.setNewData(mList);
                                easylayout_packetdetail.refreshComplete();
                            }else {
                                mPacketDetailAdapter.setNewData(mList);
                            }
                        } else {
                            if (1 == mLoadTYpe) {
                                easylayout_packetdetail.loadMoreComplete();
                                easylayout_packetdetail.closeLoadView();
                            } else if (2 == mLoadTYpe) {
                                easylayout_packetdetail.refreshComplete();
                            }
                        }
                    }
                });
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_packet_detail;
    }

    @Override
    public View getSnackView() {
        return null;
    }

    private class PacketDetailAdapter extends BaseQuickAdapter<WalletLogEntity, BaseViewHolder> {
        public PacketDetailAdapter(int layoutResId, @Nullable List<WalletLogEntity> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, WalletLogEntity item) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(Long.parseLong(item.getCreate_time()));
            helper.setText(R.id.tv_packetdetail_time, calendar.get(Calendar.YEAR)+"年"+calendar.get(Calendar.MONTH+1)
                            + "月"+calendar.get(Calendar.DATE)+"日"+calendar.get(Calendar.HOUR_OF_DAY)+"时"+calendar.get(Calendar.MINUTE)+"分");
            String money = "0";
            if(item.getChange_money() < 0){
                money = ""+item.getChange_money();
            }else{
                money = "+"+item.getChange_money();
            }
            helper.setText(R.id.tv_packetdetail_money, money);
            String type = "";
            if(Constans.LOG_RECHARGE == item.getType()){
               type = "充值";
            }else if(Constans.LOG_WITHDRAW == item.getType()){
                type="提现";
            }else if(Constans.LOG_AWARD_REDPACKET == item.getType()||Constans.LOG_FETCH_REDPACKET == item.getType()){
                type = "红包";

            }else if(Constans.LOG_AWARD_TASK == item.getType()||Constans.LOG_FETCH_TASK == item.getType()){
                type = "任务";
            }else if(Constans.LOG_REFUND_READPACKET == item.getType() || Constans.LOG_REFUND_TASK == item.getType()){
                type = "退款";
            }
            helper.setText(R.id.tv_packetdetail_type, type);

        }
    }


    class WalletLogEntity {


        private Long log_id;//ID

        private String longIdStr;

        private String record_sn;//订单号

        private Integer type;//交易类型

        private Long user_id;//用户ID

        private Double change_money;//变动金额

        private Double money;//变动后的金额

        private String remark;//备注

        private String create_time;//创建时间

        public Long getLog_id() {
            return log_id;
        }

        public void setLog_id(Long log_id) {
            this.log_id = log_id;
        }

        public String getLongIdStr() {
            return longIdStr;
        }

        public void setLongIdStr(String longIdStr) {
            this.longIdStr = longIdStr;
        }

        public String getRecord_sn() {
            return record_sn;
        }

        public void setRecord_sn(String record_sn) {
            this.record_sn = record_sn;
        }

        public Integer getType() {
            return type;
        }

        public void setType(Integer type) {
            this.type = type;
        }

        public Long getUser_id() {
            return user_id;
        }

        public void setUser_id(Long user_id) {
            this.user_id = user_id;
        }

        public Double getChange_money() {
            return change_money;
        }

        public void setChange_money(Double change_money) {
            this.change_money = change_money;
        }

        public Double getMoney() {
            return money;
        }

        public void setMoney(Double money) {
            this.money = money;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        @Override
        public String toString() {
            return "WalletLog{" +
                    "log_id=" + log_id +
                    ", longIdStr='" + longIdStr + '\'' +
                    ", record_sn='" + record_sn + '\'' +
                    ", type=" + type +
                    ", user_id=" + user_id +
                    ", change_money=" + change_money +
                    ", money=" + money +
                    ", remark='" + remark + '\'' +
                    ", create_time='" + create_time + '\'' +
                    '}';
        }
    }
}
