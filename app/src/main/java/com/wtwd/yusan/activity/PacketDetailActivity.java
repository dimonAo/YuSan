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
import com.wtwd.yusan.util.Utils;
import com.wtwd.yusan.widget.recycler.EasyRefreshLayout;
import com.wtwd.yusan.widget.recycler.LoadModel;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
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

    List<PacketDetailEntity> list = new ArrayList<>();

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

    private void initView() {
        text_tool_bar_title.setText(R.string.packet_detail_title);
        recycler_packetdetail = findViewById(R.id.recycler_packetdetail);
        easylayout_packetdetail = findViewById(R.id.easylayout_packetdetail);
        recycler_packetdetail.setLayoutManager(new LinearLayoutManager(this));

        mPacketDetailAdapter = new PacketDetailAdapter(R.layout.item_packet_detail, list);
        recycler_packetdetail.setAdapter(mPacketDetailAdapter);

        initListener();

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
       // final List<LastVersionEntity> list = new ArrayList<>();
        HashMap<String,String> params = new HashMap<>();
        params.put("start",start+"");
        params.put("count",count+"");

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
                        ResultEntity mEn = Utils.getResultEntity(response);
                        if(1 == mEn.getStatus()){
                           // list.addAll(GsonUtils.getInstance().jsonToList(mEn.getObject(),LastVersionEntity.class));
                            if(1 == mLoadTYpe){
                                easylayout_packetdetail.loadMoreComplete();
                                easylayout_packetdetail.closeLoadView();
                                int position = mPacketDetailAdapter.getData().size();
                                mPacketDetailAdapter.getData().addAll(list);
                                mPacketDetailAdapter.notifyDataSetChanged();
                                recycler_packetdetail.scrollToPosition(position);
                            }else if(2 == mEn.getStatus()){
                                mPacketDetailAdapter.setNewData(list);
                                easylayout_packetdetail.refreshComplete();
                            }
                        }else{
                            String mError = Utils.getErrorString(mEn.getErrCode());
                            //Log.e(TAG,mError);
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

    private class PacketDetailAdapter extends BaseQuickAdapter<PacketDetailEntity,BaseViewHolder>{
        public PacketDetailAdapter(int layoutResId, @Nullable List<PacketDetailEntity> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, PacketDetailEntity item) {
            helper.setText(R.id.tv_packetdetail_money,item.getNum())
                    .setText(R.id.tv_packetdetail_type,item.getTime())
                    .setText(R.id.tv_packetdetail_time, item.getTime());
        }
    }



    private class PacketDetailEntity{
        String type;
        String time;
        String num;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }
    }
}
