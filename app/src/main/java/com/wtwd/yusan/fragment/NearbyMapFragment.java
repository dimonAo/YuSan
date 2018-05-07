package com.wtwd.yusan.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.wtwd.yusan.R;
import com.wtwd.yusan.activity.NearbyListActivity;
import com.wtwd.yusan.activity.TaskActivity;
import com.wtwd.yusan.activity.UserIndexActivity;
import com.wtwd.yusan.base.BaseFragment;
import com.wtwd.yusan.entity.LastVersionEntity;
import com.wtwd.yusan.entity.ResultEntity;
import com.wtwd.yusan.entity.UserEntity;
import com.wtwd.yusan.entity.operation.DaoUtils;
import com.wtwd.yusan.util.Constans;
import com.wtwd.yusan.util.GsonUtils;
import com.wtwd.yusan.util.Pref;
import com.wtwd.yusan.util.Utils;
import com.wtwd.yusan.util.ViewUtil;
import com.wtwd.yusan.widget.view.CircleImageView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

/**
 * 附近的人
 * time:2018/4/9
 * Created by w77996
 */
public class NearbyMapFragment extends BaseFragment implements AMapLocationListener, LocationSource, View.OnClickListener {

    private final static String TAG = "NearByMapFragment";

    /**
     * 地图界面view
     */
    private MapView mMapView;
    /**
     * 地图对象
     */
    private AMap mAMap;
    /**
     * 定位图标style
     */
    private MyLocationStyle mMyLocationStyle;
    /**
     * 声明AMapLocationClient类对象
     */
    private AMapLocationClient mAMapLocationClient;
    /**
     * 声明AMapLocationClienOptiont类对象
     */
    private AMapLocationClientOption mAMapLocationClientOption;
    /**
     * 声明定位改变监听器对象
     */
    private OnLocationChangedListener mOnLocationChangedListener;

    /**
     * 定位按钮
     */
    private ImageView img_location;
    /**
     * 奖赏按钮
     */
    private ImageView img_shang;
    /**
     * 附近的人列表
     */
    private ImageView img_nearbymap_list;
    /**
     * 用户状态
     */
    LinearLayout lin_nearbymap_status;

    /**
     * 我的位置
     */
    LatLng mMyLocation;

    private ImageView img_user_status;

    private TextView tv_user_status;

    float mScale;

    int mIsInVisible = 0;

    BitmapDescriptor bitmapDescriptor;

    private static NearbyMapFragment mNearbyMapFragment;

    /**
     * 实例化fragment
     *
     * @return
     */
    public static NearbyMapFragment newInstance() {
        if (null == mNearbyMapFragment) {
            mNearbyMapFragment = new NearbyMapFragment();
        }

        return mNearbyMapFragment;
    }

    public NearbyMapFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.fragment_nearbymap;
    }

    @Override
    public void initFragmentView(Bundle savedInstanceState, View mView) {
        initView(savedInstanceState, mView);
        initData();
        addListener();
    }

    /**
     * 拿到用户状态
     */
    private void initData() {
        UserEntity user = DaoUtils.getUserManager().queryUserForUserId(Pref.getInstance(getActivity()).getUserId());
        mIsInVisible = user.getInvisible();
        setUserInvisible(mIsInVisible);
    }

    /**
     * 初始化地图界面及定位
     *
     * @param savedInstanceState
     * @param view
     */
    private void initView(Bundle savedInstanceState, View view) {
        Log.e(TAG, "initView-------------------- 初始化界面");
        // LatLng centerBJPoint= new LatLng(22.381754,114.055235);//地图默认中心店
        // AMapOptions mapOptions = new AMapOptions(); // 定义了一个配置 AMap 对象的参数类
        //  mapOptions.camera(new CameraPosition(centerBJPoint, 15f, 0, 0));
        text_tool_bar_title.setText(R.string.main_nearby);
        img_location = (ImageView) view.findViewById(R.id.img_location);
        img_shang = (ImageView) view.findViewById(R.id.img_shang);
        img_nearbymap_list = (ImageView) view.findViewById(R.id.img_nearbymap_list);
        lin_nearbymap_status = (LinearLayout) view.findViewById(R.id.lin_nearbymap_status);
        img_user_status = view.findViewById(R.id.img_user_status);
        tv_user_status = view.findViewById(R.id.tv_user_status);
        mMapView = view.findViewById(R.id.map);
        // mMapView = new MapView(getActivity(),mapOptions);
        mMapView.onCreate(savedInstanceState);
        if (mAMap == null) {
            mAMap = mMapView.getMap();
        }
        mMyLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);
        mMyLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.mipmap.neaby_map_location_point));//设置定位图标
        // mMyLocationStyle.myLocationIcon(null);
        mMyLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);//定位一次，且将视角移动到地图中心点。
        mMyLocationStyle.anchor(0.5f, 1f);//偏移
        mMyLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));// 设置圆形的边框颜色
        mMyLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));// 设置圆形的填充颜色
        mAMap.setMinZoomLevel(13);
        mAMap.setMaxZoomLevel(18);
        mAMap.setMyLocationStyle(mMyLocationStyle);
        mAMap.getUiSettings().setMyLocationButtonEnabled(false);//设置默认定位按钮是否显示，非必需设置。
        mAMap.getUiSettings().setRotateGesturesEnabled(false);//禁止地图旋转手势
        mAMap.getUiSettings().setTiltGesturesEnabled(false);//禁止倾斜手势
        mAMap.getUiSettings().setZoomControlsEnabled(false);
        mAMap.getUiSettings().setLogoBottomMargin(-50);
        mAMap.setLocationSource(this);
        mAMap.setMyLocationEnabled(true);
        mAMap.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (marker.getObject().getClass().equals(LastVersionEntity.class)) {
                    LastVersionEntity lastVersionEntity = (LastVersionEntity) marker.getObject();
                    Bundle bundle = new Bundle();
                    bundle.putLong("userId",lastVersionEntity.getUser_id());
                    readyGo(UserIndexActivity.class,bundle);
                    //Toast.makeText(getActivity(), "这是自定义marker，代号" + lastVersionEntity.getUser_id(), Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
        mAMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                Log.e(TAG, "onCameraChange-----");
            }

            @Override
            public void onCameraChangeFinish(CameraPosition cameraPosition) {
                Log.e(TAG, "onCameraChangeFinish-----" + cameraPosition.target.longitude + " " + cameraPosition.target.latitude + " " + cameraPosition.zoom+" "+mAMap.getScalePerPixel());
                // TODO: 2018/4/12 向后台请求数据并显示
                float range = cameraPosition.zoom * mAMap.getScalePerPixel();

                mScale = mAMap.getScalePerPixel();
                Log.e(TAG,"range "+range);
                int pixel = Math.round( range/ mAMap.getScalePerPixel());
                if(0 == mIsInVisible ){
                    //在线
                    getNearbyUser(cameraPosition.target.latitude,cameraPosition.target.longitude,mAMap.getScalePerPixel());
                }else if(1 == mIsInVisible){

                }


            }
        });
        mAMap.setOnMapLoadedListener(new AMap.OnMapLoadedListener() {
            @Override
            public void onMapLoaded() {
                Log.e(TAG, "onMapLoaded--------------");
            }
        });
    }

    /**
     * 添加监听器
     */
    private void addListener() {
        img_location.setOnClickListener(this);
        img_shang.setOnClickListener(this);
        img_nearbymap_list.setOnClickListener(this);
        lin_nearbymap_status.setOnClickListener(this);
    }

    /******** AMapLocationListener 定位回调监听器*************/
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        Log.e(TAG, "onLocationChanged ----------------------- :");
        if (null != mOnLocationChangedListener && null != aMapLocation) {
            if (null != aMapLocation && aMapLocation.getErrorCode() == 0) {
                Log.e(TAG, "onLocationChanged 定位成功 ----------------------- :" + "aMapLocation 信息 " + aMapLocation.getLongitude() + " " + aMapLocation.getLatitude());
                mOnLocationChangedListener.onLocationChanged(aMapLocation);//显示定位
                mMyLocation = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());//构造位置
                uploadLocation(aMapLocation.getLatitude()+"", aMapLocation.getLongitude()+"");
                Pref.getInstance(getActivity()).setCity(aMapLocation.getCity());
               /* MarkerOptions markerOption = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.neaby_map_location_point))
                        .position(latLng)
                        .draggable(false);
                mAMap.addMarker(markerOption);*/
                mAMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mMyLocation, 15));
                moveMapToPosition(mMyLocation);

                //addCustomMarkersToMap(latLng);
            } else {
                Log.e(TAG, "onLocationChanged 定位失败----------------------- :" + "aMapLocation 信息 " + aMapLocation.getErrorCode() + " " + aMapLocation.getErrorInfo());
            }
        }

    }
    /******* LocationSource 回调*************/

    /**
     * 激活定位
     *
     * @param onLocationChangedListener
     */
    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        Log.e(TAG, "activate--------------- 激活定位");
        mOnLocationChangedListener = onLocationChangedListener;
        location();
    }

    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
        Log.e(TAG, "deactivate--------------- 停止定位");
        mOnLocationChangedListener = null;
        if (null != mAMapLocationClient) {
            mAMapLocationClient = null;
            mAMapLocationClient.onDestroy();
        }
        mAMapLocationClient = null;
    }

    /**
     * 必须重写以下方法
     */
    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        /*if (null != mAMapLocationClient) {
            mAMapLocationClient.onDestroy();
        }else{
           Log.e(TAG,"mAMapLocationClient != null");
        }*/
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_location:
                location();
                break;
            case R.id.img_shang:
                Intent taskIntent = new Intent(getActivity(), TaskActivity.class);
                startActivity(taskIntent);
                break;
            case R.id.img_nearbymap_list:
                if(1 == mIsInVisible){
                   showToast("隐身状态");
                    return;
                }
                Intent nearbyListIntent = new Intent(getActivity(), NearbyListActivity.class);
                nearbyListIntent.putExtra("lat",mMyLocation.latitude+"");
                nearbyListIntent.putExtra("lng",mMyLocation.longitude+"");
                nearbyListIntent.putExtra("scale",mScale);
                startActivity(nearbyListIntent);
                break;
            case R.id.lin_nearbymap_status:
                changeUserStatus();
                break;
        }
    }


    /**
     * 定位
     */
    private void location() {
        Log.e(TAG, "location--------------- 开始定位");
        if (mAMapLocationClient == null) {
            mAMapLocationClient = new AMapLocationClient(getActivity());//初始化定位
            mAMapLocationClientOption = new AMapLocationClientOption();//初始化AMapLocationClientOption对象
            mAMapLocationClient.setLocationListener(this);
            mAMapLocationClientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//高精度定位
            mAMapLocationClientOption.setOnceLocation(true);
            mAMapLocationClientOption.setOnceLocationLatest(true);//设置单次精确定位
            mAMapLocationClient.setLocationOption(mAMapLocationClientOption);
            mAMapLocationClient.startLocation();//开始定位
        } else {
            mAMapLocationClient.startLocation();//开始定位
        }
    }

    /**
     * 移动地图视角到某个精确位置
     *
     * @param latLng 坐标
     */
    private void moveMapToPosition(LatLng latLng) {
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(
                new CameraPosition(
                        latLng,//新的中心点坐标
                        15,    //新的缩放级别
                        0,     //俯仰角0°~45°（垂直与地图时为0）
                        0      //偏航角 0~360° (正北方为0)
                ));
        mAMap.animateCamera(cameraUpdate, 300, new AMap.CancelableCallback() {
            @Override
            public void onFinish() {


            }

            @Override
            public void onCancel() {

            }
        });
    }

    /**
     * 上传定位数据
     * @param lat
     * @param lng
     */
    private void uploadLocation(String lat,String lng){

        OkHttpUtils.get()
                .tag(this)
                .url(Constans.UPLOAD_LOCATION)
                .addParams("lat",lat)
                .addParams("lng",lng)
                .addParams("userId",Pref.getInstance(getActivity()).getUserId()+"")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                     //   Log.e("uploadLocation",e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("uploadLocation",response.toString());
                        ResultEntity mEn = Utils.getResultEntity(response);
                        if(1 == mEn.getStatus()){

                        }
                    }
                });
    }
    /**
     * 设置用户状态
     */
    private void setUserInvisible(int  isInVisible){
        if(1 == isInVisible){
            tv_user_status.setText(R.string.nearby_map_stealth);
            img_user_status.setImageDrawable(ContextCompat.getDrawable(getActivity(),R.mipmap.nearby_map_invisible));
        }else if(0 == isInVisible){
            tv_user_status.setText(R.string.nearby_map_online);
            img_user_status.setImageDrawable(ContextCompat.getDrawable(getActivity(),R.mipmap.nearby_map_visible));
        }
    }
    /**
     * 改变用户状态
     */
    private void changeUserStatus() {

        int inVisibleParam = 0;
        if(0 == mIsInVisible){
            inVisibleParam = 1;
        }else{
            inVisibleParam = 0;
        }

        OkHttpUtils.get()
                .url(Constans.SET_INVISIBLE)
                .addParams("invisible",inVisibleParam+"")
                .addParams("userId",Pref.getInstance(getActivity()).getUserId()+"")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                        showToast(getString(R.string.nearby_map_setting_error));
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("setInvisible",response.toString());
                        ResultEntity mEn = Utils.getResultEntity(response);
                        if(1 == mEn.getStatus()){
                            UserEntity user =  DaoUtils.getUserManager().queryUserForUserId(Pref.getInstance(getActivity()).getUserId());
                            if(0 == mIsInVisible){
                                mIsInVisible = 1;
                                clearMarkers();
                            }else{
                                mIsInVisible = 0;
                            }
                            user.setInvisible(mIsInVisible);
                            DaoUtils.getUserManager().updateObject(user);
                            setUserInvisible(mIsInVisible);
                           // changeUserStatus();
                            showToast(getString(R.string.nearby_map_setting_success));
                        }else {
                            showToast(getString(R.string.nearby_map_setting_error));
                        }
                    }
                });
    }

    /**
     * 获取附近的人数据
     */
    private synchronized  void getNearbyUser(Double lat,Double lng,float zoom) {
        clearMarkers();
      /*  HashMap<String,Double> latMap = new HashMap<>();
        latMap.put("lat",mMyLocation.latitude);
        HashMap<String,Double> lngMap = new HashMap<>();
        lngMap.put("lngMap",mMyLocation.longitude);
        HashMap<String,Float> distanceMap = new HashMap<>();
        distanceMap.put("distanceMap",mScale);*/
        HashMap<String,String> params = new HashMap<>();
        params.put("userId","1");
        params.put("lat",lat+"");
        params.put("lng",lng+"");
        params.put("distance",zoom+"");
      Log.e(TAG,"进入getNearbyUser");
        OkHttpUtils.get()
                .url(Constans.GET_NEAYBY_USER)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                     //   Log.e(TAG,e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            JSONObject json = new JSONObject(response);
                            int status = json.getInt("status");
                           // int errCode = json.getInt("errCode");
                            String result = json.getString("object");
                            List<LastVersionEntity> list = GsonUtils.getInstance().jsonToList(result, LastVersionEntity.class);
                            if (!list.isEmpty()) {
                                Log.e(TAG,"list size"+list.size());
                                for (int i = 0; i < list.size(); i++) {
                                    addMarkerToMap(list.get(i));
                                }
                            }else{
                                Log.e(TAG,"list is null");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                           Log.e(TAG,response);
                            /*ResultEntity mEn = Utils.getResultEntity(response);
                            if (1 == mEn.getStatus()) {
                               // String result = json.getString("object");
                                List<LastVersionEntity> list = GsonUtils.getInstance().jsonToList(mEn.getObject(), LastVersionEntity.class);
                                if (!list.isEmpty()) {
                                    for (int i = 0; i < list.size(); i++) {
                                        addMarkerToMap(list.get(i));
                                    }
                                }
                            }*/
                    }
                });
    }

    /**
     * 添加marker到地图上
     */
    private synchronized void addMarkerToMap(final LastVersionEntity lastVersionEntity) {
        Log.e(TAG,lastVersionEntity.getLat()+"  "+lastVersionEntity.getLng());
        final MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.setFlat(true);
        markerOptions.anchor(0.5f, 0.5f);
        markerOptions.position(new LatLng(lastVersionEntity.getLat(), lastVersionEntity.getLng()));
        customizeMarkerIcon(lastVersionEntity, new OnMarkerIconLoadListener() {
            @Override
            public void markerIconLoadingFinished(View view) {
                //bitmapDescriptor = BitmapDescriptorFactory.fromView(view);
                Log.e(TAG,lastVersionEntity.toString());
                if(lastVersionEntity.getUser_id()!=Pref.getInstance(getActivity()).getUserId()){
                    Marker marker;
                    markerOptions.icon(bitmapDescriptor);
                    marker = mAMap.addMarker(markerOptions);
                    marker.setObject(lastVersionEntity);
                }

            }
        });

    }

    /**
     * 添加附近的人标记
     */
   /* private void addMarker(final LatLng latLng, final MarkerBean markerBean) {
        String url = "http://ucardstorevideo.b0.upaiyun.com/test/e8c8472c-d16d-4f0a-8a7b-46416a79f4c6.png";
        final MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.setFlat(true);
        markerOptions.anchor(0.5f, 0.5f);
        markerOptions.position(new LatLng(latLng.latitude, latLng.longitude));
        customizeMarkerIcon(url, markerBean, new OnMarkerIconLoadListener() {
            @Override
            public void markerIconLoadingFinished(View view) {
                //bitmapDescriptor = BitmapDescriptorFactory.fromView(view);
                Marker marker;
                markerOptions.icon(bitmapDescriptor);
                marker = mAMap.addMarker(markerOptions);
                marker.setObject(markerBean);
            }
        });

    }*/

    /*private void addCustomMarkersToMap(LatLng latLng) {

        List<LatLng> locations = new ArrayList<>();
        locations = addSimulatedData(latLng, 20, 0.02);
        for (int i = 0; i < locations.size(); i++) {
            addMarker(locations.get(i), new MarkerBean(i));
        }
    }*/


    /**
     * 从网络上下载imgUrl的图标
     *
     * @return
     */
    private void customizeMarkerIcon(LastVersionEntity lastVersionEntity, final OnMarkerIconLoadListener listener) {
        final View markerView;
        //String url = "http://ucardstorevideo.b0.upaiyun.com/test/e8c8472c-d16d-4f0a-8a7b-46416a79f4c6.png";
        Log.e(TAG,lastVersionEntity.toString());
        if(lastVersionEntity.getSex() == 1){
            markerView = LayoutInflater.from(getActivity()).inflate(R.layout.marker_man_bg, null);
        }else{
            markerView = LayoutInflater.from(getActivity()).inflate(R.layout.marker_female_bg, null);
        }

       // RelativeLayout rl = markerView.findViewById(R.id.rl_map_bg);
      //  rl.setBackground(ContextCompat.getDrawable(getActivity(),R.mipmap.nearby_map_man_bg));
       /* if (lastVersionEntity.getUser().getSex() == 0) {
           rl.setBackground(ContextCompat.getDrawable(getActivity(),R.mipmap.nearby_map_man_bg));
        } else {
           // rl.setBackground(ContextCompat.getDrawable(getActivity(),R.mipmap.nearby_map_);
        }*/
        final CircleImageView icon = (CircleImageView) markerView.findViewById(R.id.marker_item_icon);
        Glide.with(this)
                .load(lastVersionEntity.getHead_img())
                .asBitmap()
                .thumbnail(0.2f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {
                        //待图片加载完毕后再设置bitmapDes
                        icon.setImageBitmap(bitmap);
                        bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(ViewUtil.convertViewToBitmap(markerView));
                        listener.markerIconLoadingFinished(markerView);
                    }
                });
    }

    /**
     * 自定义监听接口,用来marker的icon加载完毕后回调添加marker属性
     */
    public interface OnMarkerIconLoadListener {
        void markerIconLoadingFinished(View view);
    }

   /* public class MarkerBean {
        private int markerId;

        public MarkerBean(int markerId) {
            this.markerId = markerId;
        }

        public int getMarkerId() {
            return markerId;
        }

        public void setMarkerId(int markerId) {
            this.markerId = markerId;
        }
    }*/

    /**
     * 模拟获取网络上的marker数据
     *
     * @param centerPoint
     * @param num
     * @param offset
     * @return
     */
    private List<LatLng> addSimulatedData(LatLng centerPoint, int num, double offset) {
        List<LatLng> data = new ArrayList<>();
        if (num > 0) {
            for (int i = 0; i < num; i++) {
                double lat = centerPoint.latitude + (Math.random() - 0.5) * offset;
                double lon = centerPoint.longitude + (Math.random() - 0.5) * offset;
                LatLng latlng = new LatLng(lat, lon);
                data.add(latlng);
            }
        }
        return data;
    }

    /**
     * 删除marker
     */
    //删除指定Marker
    private void clearMarkers() {
        //获取地图上所有Marker
        List<Marker> mapScreenMarkers = mAMap.getMapScreenMarkers();
        for (int i = 0; i < mapScreenMarkers.size(); i++) {
            Marker marker = mapScreenMarkers.get(i);
            if (marker.getObject() instanceof LastVersionEntity) {
                marker.remove();//移除当前Marker
            }
        }
        mAMap.reloadMap();//刷新地图
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        OkHttpUtils.getInstance().cancelTag(this);
    }
}
