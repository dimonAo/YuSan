package com.wtwd.yusan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.util.Util;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wtwd.yusan.R;
import com.wtwd.yusan.base.CommonToolBarActivity;
import com.wtwd.yusan.ease.Constant;
import com.wtwd.yusan.ease.net.ApiInterface;
import com.wtwd.yusan.ease.util.JsonUtil;
import com.wtwd.yusan.entity.ResultEntity;
import com.wtwd.yusan.util.Constans;
import com.wtwd.yusan.util.Pref;
import com.wtwd.yusan.util.Utils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import okhttp3.Call;

/**
 * Created by Administrator on 2018/4/14 0014.
 */

public class RedPacketActivity extends CommonToolBarActivity {
    public static final int RED_PACKET_RESULT_CODE = 0x02;

    private RecyclerView recycler_red_packet;

    private Button btn_red_packet_commit;
    private TextView text_task_cost;

    private static final int REQUEST_CODE_PAY_PACKET = 11;

    private int[] mDrawables = {R.mipmap.redpacket_wechat
//            , R.mipmap.redpacket_zhifubao
            , R.mipmap.redpacket_yue};

    //    private String[] mRedPacketName = {"钱包余额(%1.2f)", "支付宝", "微信"};
    private String[] mRedPacketName;

    private List<RedPacketRecyclerEntity> mList = new ArrayList<>();

    private RedPacketAdapter mRedPacketAdapter;

    private int pos;

    private int trade_type;
    private String toChatUsername;
    private String money;
    private String to;

    @Override
    public void onCreateCommonView(Bundle saveInstanceState) {
        mRedPacketName = getResources().getStringArray(R.array.payment);
        initView();
//        getDate();
    }

    private void getDate(String balance) {
        for (int i = 0; i < mRedPacketName.length; i++) {
            RedPacketRecyclerEntity redPacketRecyclerEntity = new RedPacketRecyclerEntity();
            if (1 == i) {
//                redPacketRecyclerEntity.setmRedPacketName(String.format(mRedPacketName[0], new Random().nextInt(10) + new Random().nextDouble()));
                Log.e(TAG, "double balance : " + balance);
                Log.e(TAG, "double balance : " + Double.parseDouble(balance));
                redPacketRecyclerEntity.setmRedPacketName(String.format(mRedPacketName[i], Float.parseFloat(balance)));
//                redPacketRecyclerEntity.setmRedPacketName("钱包余额(" + balance + ")");
            } else {
                redPacketRecyclerEntity.setmRedPacketName(mRedPacketName[i]);
            }
            redPacketRecyclerEntity.setmDrawableId(mDrawables[i]);
            mList.add(redPacketRecyclerEntity);
        }
//        recycler_red_packet.removeItemDecorationAt(mList.size()-1);
        mList.get(0).setCheck(true);
        mRedPacketAdapter.notifyDataSetChanged();
    }

    private void initView() {
        text_tool_bar_title.setText(R.string.red_packet_title);
        text_task_cost = (TextView) findViewById(R.id.text_task_cost);


        btn_red_packet_commit = (Button) findViewById(R.id.btn_red_packet_commit);
        recycler_red_packet = (RecyclerView) findViewById(R.id.recycler_red_packet);
        recycler_red_packet.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRedPacketAdapter = new RedPacketAdapter(R.layout.item_red_packet_type, mList);
        recycler_red_packet.setAdapter(mRedPacketAdapter);
        DividerItemDecoration mDi = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        mDi.setDrawable(ContextCompat.getDrawable(this, R.drawable.shape_line));
        recycler_red_packet.addItemDecoration(mDi);

        mRedPacketAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Log.e("dd", "ddd" + position);
                pos = position;
                for (int i = 0; i < mList.size(); i++) {
                    mList.get(i).setCheck(false);
                }
                mList.get(position).setCheck(true);
//                recycler_red_packet.removeItemDecorationAt(mList.size()-1);
                mRedPacketAdapter.notifyDataSetChanged();
            }
        });
        money = getMissionParameterIntent().getString("money");
        text_task_cost.setText(money);
        trade_type = getMissionParameterIntent().getInt("trade_type");
        to = getMissionParameterIntent().getString("to");
        toChatUsername = getMissionParameterIntent().getString("toid");
        getWalletBalance();
        addListener();
    }

    private Bundle getMissionParameterIntent() {

        return getIntent().getExtras();
    }

    private void addListener() {
        btn_red_packet_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Intent intent = new Intent();
//                Bundle bundle = new Bundle();
//                bundle.putString("money", text_task_cost.getText().toString());
//                intent.putExtras(bundle);
//                setResult(RED_PACKET_RESULT_CODE, intent);
//                finish();

//塞钱进红包，支付
                if(1 == trade_type){
                    //红包
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString("userName",Constant.CONSTANT_USER_NAME);
                    bundle.putString("payType","1");
                    bundle.putString("money",money);
                    bundle.putString("type","3");
                    bundle.putString("to",to);
                    bundle.putString("to",toChatUsername);
                    intent.putExtras(bundle);
                    setResult(200,intent);
                    finish();
                    //publishRedPacket();
                }else if(2 == trade_type){
                    //任务
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString("payType","1");
                    bundle.putString("money",money);
                    intent.putExtras(bundle);
                    setResult(200,intent);
                    finish();
                  //  publishMission();
                }


            }
        });
    }

    private void publishRedPacket() {
        Map<String, String> params = new HashMap<String, String>();
//        params.put("userId", userId);
        params.put("userName", Constant.CONSTANT_USER_NAME);
        params.put("payType", "1");
        params.put("money", money);
        params.put("type", "3");
        params.put("to", to);
        params.put("to_id", toChatUsername);
        ApiInterface.payRedPacket(params, new com.wtwd.yusan.ease.net.callback.StringCallback() {

            @Override
            public void onSuccess(String response) {
                Log.i("IMDemo", "sendRedPacket response:" + response);
                if("1".equals(to)) {
                  /*  EMMessage msg = EMMessage.createSendMessage(EMMessage.Type.TXT);
                    msg.setDirection(EMMessage.Direct.SEND);
                    msg.setTo(toChatUsername);
                    EMTextMessageBody body = new EMTextMessageBody(WTWD_REDPACKET_TXT+":NULL"+":0");
                    msg.addBody(body);
                    sendLocalMessage(msg);*/
                }
                //Toast.makeText(getActivity(), JsonUtil.getJsonUtil().getStatusStr(response), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error) {
                Log.i("IMDemo", "sendRedPacket error:" + error);
               // Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void publishMission() {

        //request service for publish mission
//                bundle.putString("userId", userId + "");
//                bundle.putString("content", getTaskDetailContent()); //任务描述
//                bundle.putString("type", getTaskType()); //任务类型
//                bundle.putString("sex", getSexType()); //接受者性别限制
//                bundle.putString("money", getTaskMoney());//任务金额
//                bundle.putString("address", getTaskAddress());//任务地址
//                bundle.putString("startTime", getTaskStartTime());//任务开始时间 yyyy-MM-dd HH:mm格式
//                bundle.putString("to", toId); //发送给谁，0所有人，指定人传用户userid
//                bundle.putString("anonymous", getTaskAnonymous()); //是否匿名，1匿名 ； 0不匿名


        Bundle bundle = getMissionParameterIntent();

        Map<String, String> mPublishMap = new HashMap<>();
        mPublishMap.put("userId", bundle.getString("userId"));
        mPublishMap.put("content", bundle.getString("content"));
        mPublishMap.put("type", bundle.getString("type"));
        mPublishMap.put("sex", bundle.getString("sex"));
        mPublishMap.put("money", bundle.getString("money"));
        mPublishMap.put("address", bundle.getString("address"));
        mPublishMap.put("startTime", bundle.getString("startTime"));
        mPublishMap.put("to", bundle.getString("to"));
        mPublishMap.put("toId", "0");
        mPublishMap.put("anonymous", bundle.getString("anonymous"));
        mPublishMap.put("payType", pos + "");

        Log.e(TAG, "publish map : " + mPublishMap.toString());
        OkHttpUtils.get()
                .url(Constans.PUBLISH_MISSION)
                .params(mPublishMap)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e(TAG, "publish mission : " + response);
//                        ResultEntity mEn = Utils.getResultEntity(response);
                        try {
                            JSONObject mMissionJson = new JSONObject(response);
                            int status = mMissionJson.optInt("status");
                            if (Constans.REQUEST_SUCCESS == status) {
                                //request success
                                finish();
                            } else {
                                showToast("发布任务失败");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }


    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_red_packet;
    }

    @Override
    public View getSnackView() {
        return null;
    }

    /**
     * 获取账户余额
     */
    private void getWalletBalance() {
        OkHttpUtils.get()
                .addParams("userId", Pref.getInstance(this).getUserId() + "")
                .url(Constans.MY_WALLET)
                .build()
                .connTimeOut(Constans.TIME_OUT)
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e(TAG, "balance : " + response);
                        try {
                            JSONObject mWalletJson = new JSONObject(response);
                            int mStatus = mWalletJson.optInt("status");

                            if (Constans.REQUEST_SUCCESS == mStatus) {
                                JSONObject mObjectJson = new JSONObject(mWalletJson.optString("object"));

                                double balance = mObjectJson.optDouble("balance");
                                Log.e(TAG, "request balance : " + balance);
                                getDate(balance + "");
                            } else {
                                showToast(getErrorString(mStatus));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                });
    }


    private class RedPacketRecyclerEntity {
        private int mDrawableId;
        private String mRedPacketName;
        private boolean IsCheck;

        public int getmDrawableId() {
            return mDrawableId;
        }

        public void setmDrawableId(int mDrawableId) {
            this.mDrawableId = mDrawableId;
        }

        public String getmRedPacketName() {
            return mRedPacketName;
        }

        public void setmRedPacketName(String mRedPacketName) {
            this.mRedPacketName = mRedPacketName;
        }

        public boolean isCheck() {
            return IsCheck;
        }

        public void setCheck(boolean check) {
            IsCheck = check;
        }
    }

    private class RedPacketAdapter extends BaseQuickAdapter<RedPacketRecyclerEntity, BaseViewHolder> {


        public RedPacketAdapter(int layoutResId, @Nullable List<RedPacketRecyclerEntity> data) {
            super(layoutResId, data);

        }

        @Override
        protected void convert(BaseViewHolder helper, RedPacketRecyclerEntity item) {
            helper.setImageDrawable(R.id.img_redpacket_head, ContextCompat.getDrawable(RedPacketActivity.this, item.getmDrawableId()))
                    .setText(R.id.tv_redpacket_text, item.getmRedPacketName())
                    .setChecked(R.id.ck_redpacket, item.isCheck());
        }
    }
}
