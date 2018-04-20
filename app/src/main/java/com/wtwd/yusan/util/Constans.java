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


}
