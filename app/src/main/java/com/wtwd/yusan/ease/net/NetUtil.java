package com.wtwd.yusan.ease.net;

/**
 * Created by XJM on 2018/4/15.
 */

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.wtwd.yusan.MyApplication;
import com.wtwd.yusan.ease.net.callback.BaseResponseCallback;
import com.wtwd.yusan.ease.net.callback.StringCallback;
import com.wtwd.yusan.ease.net.response.BaseResponse;
import com.wtwd.yusan.ease.util.StringUtils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import java.util.concurrent.Executors;

/**
 * Java原生的API可用于发送HTTP请求，即java.net.URL、java.net.URLConnection，这些API很好用、很常用，
 * 但不够简便；
 * <p>
 * 1.通过统一资源定位器（java.net.URL）获取连接器（java.net.URLConnection） 2.设置请求的参数 3.发送请求
 * 4.以输入流的形式获取返回内容 5.关闭输入流
 *
 * @author H__D
 */
public class NetUtil {

    private static final String baseUrl = "http://121.196.232.11:9883/Award/open/";

    private static Handler handler = new Handler(Looper.getMainLooper());

    /**
     * 上传文件
     *
     * @param filePaths
     * @param callback
     */
    public static void upLoadFile(final String[] filePaths, final StringCallback callback) {
        upLoadFile(baseUrl, filePaths, callback);
    }

    /**
     * 上传文件
     *
     * @param url
     * @param filePaths
     * @param callback
     */
    public static void upLoadFile(final String url, final String[] filePaths, final StringCallback callback) {
        Executors.newCachedThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                final String response = uploadFile(url, filePaths);
                runUiThread(response, callback);

            }
        });
    }

    /**
     * POST请求表单提交
     *
     * @param params
     * @param callback
     */
    public static void post(final String api,final Map<String, String> params, final BaseResponseCallback callback) {
        Executors.newCachedThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    String response = post(baseUrl+api, params);
                    runUiThread(response, callback);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * POST请求表单提交
     *
     * @param api
     * @param maps
     * @param callback
     */
    public static void post(final String api, final Map<String, String> maps, final StringCallback callback) {

        Executors.newCachedThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    String response = post(baseUrl+api, maps);
                    runUiThread(response, callback);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 多文件上传的方法
     *
     * @param actionUrl：上传的路径
     * @param uploadFilePaths：需要上传的文件路径，数组
     * @return
     */
    @SuppressWarnings("finally")
    private static String uploadFile(String actionUrl, String[] uploadFilePaths) {
        String end = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        DataOutputStream ds = null;
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;
        StringBuffer resultBuffer = new StringBuffer();
        String tempLine = null;
        try {
            // 统一资源
            URL url = new URL(actionUrl);
            // 连接类的父类，抽象类
            URLConnection urlConnection = url.openConnection();
            // http的连接类
            HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;
            // 设置是否从httpUrlConnection读入，默认情况下是true;
            httpURLConnection.setDoInput(true);
            // 设置是否向httpUrlConnection输出
            httpURLConnection.setDoOutput(true);
            // Post 请求不能使用缓存
            httpURLConnection.setUseCaches(false);
            // 设定请求的方法，默认是GET
            httpURLConnection.setRequestMethod("POST");
            // 设置字符编码连接参数
            httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
            // 设置字符编码
            httpURLConnection.setRequestProperty("Charset", "UTF-8");
            // 设置请求内容类型
            httpURLConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            // 设置DataOutputStream
            ds = new DataOutputStream(httpURLConnection.getOutputStream());
            for (int i = 0; i < uploadFilePaths.length; i++) {
                String uploadFile = uploadFilePaths[i];
                String filename = uploadFile.substring(uploadFile.lastIndexOf("//") + 1);
                ds.writeBytes(twoHyphens + boundary + end);
                ds.writeBytes("Content-Disposition: form-data; " + "name=\"file" + i + "\";filename=\"" + filename
                        + "\"" + end);
                ds.writeBytes(end);
                FileInputStream fStream = new FileInputStream(uploadFile);
                int bufferSize = 1024;
                byte[] buffer = new byte[bufferSize];
                int length = -1;
                while ((length = fStream.read(buffer)) != -1) {
                    ds.write(buffer, 0, length);
                }
                ds.writeBytes(end);
                /* close streams */
                fStream.close();
            }
            ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
            /* close streams */
            ds.flush();
            if (httpURLConnection.getResponseCode() >= 300) {
                throw new Exception(
                        "HTTP Request is not success, Response code is " + httpURLConnection.getResponseCode());
            }

            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = httpURLConnection.getInputStream();
                inputStreamReader = new InputStreamReader(inputStream);
                reader = new BufferedReader(inputStreamReader);
                tempLine = null;
                resultBuffer = new StringBuffer();
                while ((tempLine = reader.readLine()) != null) {
                    resultBuffer.append(tempLine);
                    resultBuffer.append("\n");
                }
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (ds != null) {
                try {
                    ds.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (inputStreamReader != null) {
                try {
                    inputStreamReader.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            return resultBuffer.toString();
        }
    }

    //post表单请求
    private static String post(String url, Map<String, String> form) {

        if(form!=null){
            if(!form.containsKey("userId"))
                form.put("userId", MyApplication.getUserId());
        }

        HttpURLConnection conn = null;
        PrintWriter pw = null;
        BufferedReader rd = null;
        StringBuilder out = new StringBuilder();
        StringBuilder sb = new StringBuilder();
        String line = null;
        String response = null;
        for (String key : form.keySet()) {
            if (out.length() != 0) {
                out.append("&");
            }
            out.append(key).append("=").append(form.get(key));
        }
        try {
            conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("POST");
            //设置请求属性
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Connection", "Keep-Alive");// 维持长连接
            conn.setRequestProperty("Charset", "UTF-8");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setReadTimeout(20000);
            conn.setConnectTimeout(20000);
            conn.setUseCaches(false);
            conn.connect();
            pw = new PrintWriter(conn.getOutputStream());
            pw.write(out.toString());
            pw.flush();
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            response = sb.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (pw != null) {
                    pw.close();
                }
                if (rd != null) {
                    rd.close();
                }
                if (conn != null) {
                    conn.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return response;
    }

    private static void runUiThread(final String response, final BaseResponseCallback callback) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (StringUtils.isEmpty(response)) {
                    callback.onError("数据为空！");
                } else {

                    if(!response.startsWith("{")||!response.endsWith("}")) {
                        Log.e("response", response);
                        return;
                    }

                    BaseResponse infos = JSON.parseObject(response, BaseResponse.class);
                    if(infos.getStatus() == 1)
                        callback.onSuccess(infos);
                    else
                        callback.onError(infos.getMsg());
                }
            }
        });
    }

    private static void runUiThread(final String response, final StringCallback callback) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (StringUtils.isEmpty(response)) {
                    callback.onError("数据为空！");
                } else {
                    callback.onSuccess(response);
                }
            }
        });
    }


}
