package com.wtwd.yusan.util;


import android.os.Environment;

/**
 * 公有常量类
 */

public class Constans {

    /**
     * 请求接口成功返回status标识
     */
    public static final int REQUEST_SUCCESS = 1;
    /**
     * 请求接口失败返回status标识
     */
    public static final int REQUEST_FAIL = 0;


    /**
     * 请求http地址
     */
   // public static final String REQUEST_URL = "http://121.196.232.11:9883/Award/open";
    public static final String REQUEST_URL = "http://192.168.13.235:8080/open";
//    public static final String REQUEST_URL = "http://192.168.13.203:8080/CheeKat/open";
//    public static final String REQUEST_URL = "http://121.196.232.11:9883/Award/open";

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

    public static final String LOGIN_USER_EXIST = REQUEST_URL + "/userExist"; //登录判断用户是否存在/是否已注册
    public static final String REGISTER_USER = REQUEST_URL + "/registUser"; //注册，即第一次修改信息后请求服务器接口
    public static final String EDIT_USER = REQUEST_URL + "/editUser";
    public static final String UPLOAD_IMG = REQUEST_URL + "/uploadImg"; //上传主页图片
    public static final String RECEIVE_HOME_INFO = REQUEST_URL + "/getUserIndex";
    public static final String RECEIVE_HOME_IMG = REQUEST_URL + "/getUserIndexImg";
    public static final String UPLOAD_LOCATION = REQUEST_URL+"/uploadLocation";

    //    public static final String MY_WALLET = REQUEST_URL + "/myWallet";
    public static final String MY_WALLET = REQUEST_URL + "/getBalance";
    public static final String GET_NEAYBY_USER = REQUEST_URL + "/getNearBy";//获取附近的人
    public static final String GET_ALL_NEAYBY_USER = REQUEST_URL + "/getAllNearBy";//获取附近的人列表
    public static final String FEEDBACK = REQUEST_URL + "/feedback";//反馈
    public static final String GET_PACKETDETAIL = REQUEST_URL + "/getBalanceDetail";//钱包详情
    public static final String SET_INVISIBLE= REQUEST_URL + "/setInvisible";//设置用户状态
    public static final String GET_BALANCE = REQUEST_URL+"/getBalance";//获取月

    /******微信支付相关**********/
    public static final String WX_APP_ID = "";
    public static final String WX_API_KEY = "";
    public static final String WX_MCH_ID = "";

    public static final String WX_APP_SECRET = "";

    public static final String GET_WX_PERPERID = REQUEST_URL + "";//传值后台获取preperId

    public static final int ORDER_TYPE_TRADE = 1;//交易类型：充值
    public static final int ORDER_TYPE_WITHDRAWLS = 2;//交易类型：提现
    public static final int ORDER_TYPE_REDPACKET = 3;//交易类型：红包
    public static final int ORDER_TYPE_TASK = 4;//交易类型：任务

    public static final int LOG_RECHARGE = 1;//充值
    public static final int LOG_WITHDRAW= 2;//提现
    public static final int LOG_AWARD_REDPACKET = 3;//发红包
    public static final int LOG_FETCH_REDPACKET = 4;//接红包
    public static final int LOG_AWARD_TASK = 5;//发任务
    public static final int LOG_FETCH_TASK = 6;//接任务
    public static final int LOG_REFUND_READPACKET = 7;//红包退款
    public static final int LOG_REFUND_TASK = 8;//任务退款


    /**
     * 图片文件夹相关
     */
    public static final String EXTERNAL_ROOT_PATH = Environment.getExternalStorageDirectory().getPath() + "/CheeKat";

    /**
     * 图片下载存储位置
     */
    public static final String DOWNLOAD_PIC_PATH = EXTERNAL_ROOT_PATH + "/images";



}
