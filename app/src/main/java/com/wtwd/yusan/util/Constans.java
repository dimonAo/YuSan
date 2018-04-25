package com.wtwd.yusan.util;


/**
 * 公有常量类
 */

public class Constans {

    /**
     * 请求http地址
     */
//    public static final String REQUEST_URL = "http://192.168.13.203:8080/CheeKat";
//    public static final String REQUEST_URL = "http://192.168.13.192:8080/CheeKat/open";
    public static final String REQUEST_URL = "http://192.168.13.203:8080/CheeKat/open";

    /**
     * 请求http端口
     */
    public static final int PORT = 0;
    /**
     * 请求超时时间
     */
    public static final int TIME_OUT = 30000;

    public static final String GET_ALL_MISSION = REQUEST_URL + "/getAllMission";

    public static final String GET_MY_MISSION = REQUEST_URL + "/getMyMission";
    public static final String ACCEPT_MISSION = REQUEST_URL + "/acceptMission";
    public static final String COMPLET_MISSION = REQUEST_URL + "/completeMission";
    public static final String PUBLISH_MISSION = REQUEST_URL + "/publishMission";
    public static final String CLOSE_MISSION = REQUEST_URL + "/closeMission";


    public static final String GET_NEAYBY_USER = REQUEST_URL + "/getNearBy";//获取附近的人
    public static final String FEEDBACK = REQUEST_URL+"/feedback";//反馈
    public static final String GET_PACKETDETAIL = REQUEST_URL+"/packetdetail";//钱包详情

    /******微信支付相关**********/
    public static final String  WX_APP_ID = "";
    public static final String WX_API_KEY="";
    public static final String WX_MCH_ID="";

    public static final String WX_APP_SECRET="";

    public static final String GET_WX_PERPERID = REQUEST_URL+"";//传值后台获取preperId

    public static final int ORDER_TYPE_TRADE = 1;//交易类型：充值
    public static final int ORDER_TYPE_WITHDRAWLS = 2;//交易类型：提现
    public static final int ORDER_TYPE_REDPACKET = 3;//交易类型：红包
    public static final int ORDER_TYPE_TASK = 4;//交易类型：任务
}
