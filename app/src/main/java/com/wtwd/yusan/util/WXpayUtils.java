package com.wtwd.yusan.util;


import android.widget.Toast;


import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.wtwd.yusan.MyApplication;
import com.wtwd.yusan.entity.OrederSendEntity;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Created by xmg on 2016/12/5.
 */

public class WXpayUtils {

    private static IWXAPI iwxapi;
    private static PayReq req;

    public static IWXAPI getWXAPI(){
        if (iwxapi == null){
            //通过WXAPIFactory创建IWAPI实例
            iwxapi = WXAPIFactory.createWXAPI(MyApplication.getContext(), null);
            req = new PayReq();
            //将应用的appid注册到微信
            iwxapi.registerApp(Constans.WX_APP_ID);
        }
        return iwxapi;
    }

    //生成随机字符串
    public static String genNonceStr() {
        Random random = new Random();
        return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
    }

    //获得时间戳
    private static long genTimeStamp() {
        return System.currentTimeMillis() / 1000;
    }

    //生成预支付随机签名
    public static  String genSign(OrederSendEntity info) {
        StringBuffer sb = new StringBuffer(info.toString());
        if (Constans.WX_API_KEY.equals("")){
            Toast.makeText(MyApplication.getContext(),"APP_ID为空",Toast.LENGTH_LONG).show();
        }
        //拼接密钥
        sb.append("key=");
        sb.append(Constans.WX_API_KEY);

        String appSign = MD5.getMessageDigest(sb.toString().getBytes());

        return appSign;
    }

    //生成支付随机签名
    private static String genAppSign(List<Param> params){
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).key);
            sb.append('=');
            sb.append(params.get(i).value);
            sb.append('&');
        }
        //拼接密钥
        sb.append("key=");
        sb.append(Constans.WX_API_KEY);

        String appSign = MD5.getMessageDigest(sb.toString().getBytes());
        return appSign.toUpperCase();
    }

    //生成支付参数
    private static void genPayReq(String prepayid) {
        req.appId = Constans.WX_APP_ID;
        req.partnerId = Constans.WX_MCH_ID;
        req.prepayId = prepayid;
        req.packageValue = "Sign=" + prepayid;
        req.nonceStr = genNonceStr();
        req.timeStamp = String.valueOf(genTimeStamp());

        List<Param> signParams = new LinkedList<Param>();
        signParams.add(new Param("appid", req.appId));
        signParams.add(new Param("noncestr", req.nonceStr));
        signParams.add(new Param("package", req.packageValue));
        signParams.add(new Param("partnerid", req.partnerId));
        signParams.add(new Param("prepayid", req.prepayId));
        signParams.add(new Param("timestamp", req.timeStamp));

        req.sign = genAppSign(signParams);
    }

    public static void Pay(String prepayid){
        if (judgeCanGo()){
            genPayReq(prepayid);
            iwxapi.registerApp(Constans.WX_APP_ID);
            iwxapi.sendReq(req);
        }
    }

    private static boolean judgeCanGo(){
        getWXAPI();
        if (!iwxapi.isWXAppInstalled()) {
            Toast.makeText(MyApplication.getContext(), "请先安装微信应用", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!iwxapi.isWXAppSupportAPI()) {
            Toast.makeText(MyApplication.getContext(), "请先更新微信应用", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public static class Param {

        String key;
        String value;

        public Param() {
        }

        public Param(String key, String value) {
            this.key = key;
            this.value = value;
        }

    }
}
