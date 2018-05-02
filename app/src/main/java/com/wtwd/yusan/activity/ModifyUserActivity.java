package com.wtwd.yusan.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.util.Util;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.ui.ImagePreviewDelActivity;
import com.lzy.imagepicker.view.CropImageView;
import com.wtwd.yusan.R;
import com.wtwd.yusan.base.CommonToolBarActivity;
import com.wtwd.yusan.entity.UserEntity;
import com.wtwd.yusan.entity.operation.DaoManager;
import com.wtwd.yusan.entity.operation.DaoUtils;
import com.wtwd.yusan.util.Constans;
import com.wtwd.yusan.util.GlideImageLoader;
import com.wtwd.yusan.util.GsonUtils;
import com.wtwd.yusan.util.Pref;
import com.wtwd.yusan.util.Utils;
import com.wtwd.yusan.util.dialog.DialogUtil;
import com.wtwd.yusan.util.dialog.SelectDialog;
import com.wtwd.yusan.widget.view.CircleImageView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * time:2018/4/18
 * Created by w77996
 */

public class ModifyUserActivity extends CommonToolBarActivity implements View.OnClickListener {
    public static final int IMAGE_ITEM_ADD = -1;
    public static final int REQUEST_CODE_SELECT = 100;
    public static final int REQUEST_CODE_PREVIEW = 101;


    private RelativeLayout relative_head;
    private RelativeLayout relative_name;
    private RelativeLayout relative_sex;
    private RelativeLayout relative_height;
    private RelativeLayout relative_birthday;

    private TextView tv_modifyuser_sex;
    private TextView tv_modifyuser_height;
    private TextView text_user_nick;
    private CircleImageView circle_img_head;
    private TextView tv_modifyuser_birthday;
    private Button btn_withdrawals_submit;

    //    private String[] mModifySex;
    private List<String> mModifySexList = new ArrayList<>();
    private List<String> mModifyHeight = new ArrayList<>();
    private Dialog mSelectorDialog;
    private ImagePicker mImagePicker;

    /**
     * 账号
     */
    private String account;
    /**
     * isFirst 是否第一次登录
     * isPhone 是否为手机号
     */
    private boolean isFirst, isPhone;


    @Override
    public void onCreateCommonView(Bundle saveInstanceState) {
        initView();
    }

    private void initView() {
        mSelectorDialog = new Dialog(this, R.style.MyCommonDialog);

        String[] mModifySex = getResources().getStringArray(R.array.modify_info);
        mModifySexList.addAll(Arrays.asList(mModifySex));

        getHeightData();

        text_tool_bar_title.setText(R.string.modify_user_info_title);
        relative_head = (RelativeLayout) findViewById(R.id.relative_head);
        relative_name = (RelativeLayout) findViewById(R.id.relative_name);
        relative_sex = (RelativeLayout) findViewById(R.id.relative_sex);
        relative_height = (RelativeLayout) findViewById(R.id.relative_height);
        relative_birthday = (RelativeLayout) findViewById(R.id.relative_birthday);

        tv_modifyuser_sex = (TextView) findViewById(R.id.tv_modifyuser_sex);
        tv_modifyuser_height = (TextView) findViewById(R.id.tv_modifyuser_height);
        text_user_nick = (TextView) findViewById(R.id.text_user_nick);
        circle_img_head = (CircleImageView) findViewById(R.id.circle_img_head);
        tv_modifyuser_birthday = (TextView) findViewById(R.id.tv_modifyuser_birthday);
        btn_withdrawals_submit = (Button) findViewById(R.id.btn_withdrawals_submit);
        addListener();
        initImagePicker();
    }


    private void addListener() {
        relative_head.setOnClickListener(this);
        relative_name.setOnClickListener(this);
        relative_sex.setOnClickListener(this);
        relative_height.setOnClickListener(this);
        relative_birthday.setOnClickListener(this);
        btn_withdrawals_submit.setOnClickListener(this);

        obtainIsFirstLogin();

    }


    private void obtainIsFirstLogin() {
        if (null == getIntent()) {
            return;
        }
        Bundle bundle = getIntent().getExtras();
        isFirst = bundle.getBoolean("isFirst");
        if (isFirst) {
            account = bundle.getString("account");
            isPhone = bundle.getBoolean("isPhone");
        }

        displayUserInfo();
    }

    private void displayUserInfo() {
        UserEntity mEn = DaoUtils.getUserManager().queryUserForUserId(mPref.getUserId());
        Log.e(TAG, "mEn : " + mEn.toString());

        if (null != mEn) {
            Glide.with(this)
                    .load(Uri.parse(mEn.getHead_img()))
                    .asBitmap()
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            circle_img_head.setImageBitmap(resource);
                        }
                    });

            text_user_nick.setText(mEn.getNick_name());
            tv_modifyuser_birthday.setText(mEn.getBirth());
            tv_modifyuser_sex.setText(mEn.getSex() + "");
            tv_modifyuser_height.setText(mEn.getHeight());

        }


    }


    /**
     * 身高数据（20cm-239cm）
     */
    private void getHeightData() {
        for (int i = 0; i < 220; i++) {
            mModifyHeight.add((20 + i) + "");
        }

    }

    /**
     * 初始化ImagePicker
     */
    private void initImagePicker() {
        mImagePicker = ImagePicker.getInstance();
        mImagePicker.setMultiMode(false);
        mImagePicker.setCrop(true);
        mImagePicker.setImageLoader(new GlideImageLoader());
        mImagePicker.setShowCamera(false);
        mImagePicker.setStyle(CropImageView.Style.RECTANGLE);
        mImagePicker.setSaveRectangle(true);

        mImagePicker.setFocusHeight((Utils.getDisplayHeight(this) * 2) / 5);
        mImagePicker.setFocusWidth(Utils.getDisplayWidth(this));
    }

    /**
     * 获取昵称，在提交时判断空条件提示
     *
     * @return
     */
    private String getUserName() {

        return text_user_nick.getText().toString();
    }


    /**
     * 获取生日日期
     *
     * @return
     */
    private String getBirthday() {
        return tv_modifyuser_birthday.getText().toString();
    }

    /**
     * 获取身高
     *
     * @return
     */
    private String getHeight() {
        return tv_modifyuser_height.getText().toString();
    }

    /**
     * 获取性别
     *
     * @return
     */
    private String getSex() {
        String sex = tv_modifyuser_sex.getText().toString();

        if (getString(R.string.common_man).equals(sex)) {
            return "1";
        } else {
            return "2";
        }

    }


    private void editUserInfo() {
        Map<String, String> mModifyMap = new HashMap<>();
        mModifyMap.put("userId", mPref.getUserId() + "");
        mModifyMap.put("headImg", Utils.Bitmap2StrByBase64(getBitmapForCircleImg()));
        mModifyMap.put("userName", getUserName());
        mModifyMap.put("birth", getBirthday());
        mModifyMap.put("height", getHeight());
        mModifyMap.put("sex", getSex());

        OkHttpUtils.post()
                .params(mModifyMap)
                .url(Constans.EDIT_USER)
                .build()
                .connTimeOut(Constans.TIME_OUT)
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e(TAG, "modify user info : " + response);
                        try {
                            JSONObject mUserJson = new JSONObject(response);

                            int mStatus = mUserJson.optInt("status");

                            if (Constans.REQUEST_SUCCESS == mStatus) {
//                                if (isFirst) {
//                                    //注册用户信息
//                                    String mObjectStr = mUserJson.optString("object");
////                                    JSONObject mObjectJson = new JSONObject(mObjectStr);
////                                    String mUser = mObjectJson.optString("user");
//                                    UserEntity mUserEn = GsonUtils.GsonToBean(mObjectStr, UserEntity.class);
////                                    Pref.getInstance(ModifyUserActivity.this).setUserId(mUserEn.getUser_id());
//
//                                    mPref.setUserId(mUserEn.getUser_id());
//                                    DaoUtils.getUserManager().insertObject(mUserEn);
//
//                                    readyGoForNewTask(MainActivity.class);
//
//
//                                } else {
                                    //修改用户信息
                                    UserEntity mUser = DaoUtils.getUserManager().queryUserForUserId(Pref.getInstance(ModifyUserActivity.this).getUserId());

                                    DaoUtils.getUserManager().updateObject(mUser);

                                    finish();
//                                }
                            } else {
                                int mError = mUserJson.optInt("errCode");
                                showToast(getErrorString(mError));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });


    }


    /**
     * 请求修改用户信息
     */
    private void modifyUserInfo() {
        String url, type, phone, openId;
        url = Constans.REGISTER_USER;

        if (isPhone) {
            type = "1"; //手机号登录
            phone = account;
            openId = "";
        } else {
            type = "2"; //微信登录
            phone = "";
            openId = account;
        }


        Map<String, String> mModifyMap = new HashMap<>();
        mModifyMap.put("userId", mPref.getUserId() + "");
        mModifyMap.put("headImg", Utils.Bitmap2StrByBase64(getBitmapForCircleImg()));
        mModifyMap.put("userName", getUserName());
        mModifyMap.put("birth", getBirthday());
        mModifyMap.put("height", getHeight());
        mModifyMap.put("sex", getSex());
        mModifyMap.put("phone", phone);
        mModifyMap.put("openId", openId);
        mModifyMap.put("type", type);

//        Log.e(TAG, "mModifyMap : " + mModifyMap.toString());

        OkHttpUtils.post()
                .url(url)
                .tag(this)
                .params(mModifyMap)
                .build()
                .connTimeOut(Constans.TIME_OUT)
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e(TAG, "modify e : " + e.toString());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            Log.e(TAG, "modify user info : " + response);
                            JSONObject mUserJson = new JSONObject(response);
                            int mStatus = mUserJson.optInt("status");

                            if (Constans.REQUEST_SUCCESS == mStatus) {
                                if (isFirst) {
                                    //注册用户信息
                                    String mObjectStr = mUserJson.optString("object");
//                                    JSONObject mObjectJson = new JSONObject(mObjectStr);
//                                    String mUser = mObjectJson.optString("user");
                                    UserEntity mUserEn = GsonUtils.GsonToBean(mObjectStr, UserEntity.class);
//                                    Pref.getInstance(ModifyUserActivity.this).setUserId(mUserEn.getUser_id());

                                    mPref.setUserId(mUserEn.getUser_id());
                                    DaoUtils.getUserManager().insertObject(mUserEn);

                                    readyGoForNewTask(MainActivity.class);


                                }
//                                else {
//                                    //修改用户信息
//                                    UserEntity mUser = DaoUtils.getUserManager().queryUserForUserId(Pref.getInstance(ModifyUserActivity.this).getUserId());
//
//                                    finish();
//                                }
                            } else {
                                int mError = mUserJson.optInt("errCode");
                                showToast(getErrorString(mError));
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                });
    }


    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_modifyuser;
    }

    @Override
    public View getSnackView() {
        return null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.relative_head:
                showHeadDialog();
                break;

            case R.id.relative_name:
                readyGoForResult(NickActivity.class, 101);
                break;

            case R.id.relative_height:
                Dialog mDialog = new Dialog(this, R.style.MyCommonDialog);
                DialogUtil.dialogShowPublishSex(ModifyUserActivity.this, mDialog, mModifyHeight, tv_modifyuser_height);

                break;

            case R.id.relative_sex:
                DialogUtil.dialogShowPublishSex(ModifyUserActivity.this, mSelectorDialog, mModifySexList, tv_modifyuser_sex);
                break;

            case R.id.relative_birthday:
                Dialog mBirDialog = new Dialog(this, R.style.MyCommonDialog);
                DialogUtil.dialogChooseBirthday(ModifyUserActivity.this, mBirDialog, tv_modifyuser_birthday);
                break;

            case R.id.btn_withdrawals_submit:
                if (isFirst) {
                    modifyUserInfo();
                } else {
                    editUserInfo();
                }

                break;
        }
    }


    private SelectDialog showDialog(SelectDialog.SelectDialogListener listener, List<String> names) {
        SelectDialog dialog = new SelectDialog(this, R.style
                .transparentFrameWindowStyle,
                listener, names);
        if (!this.isFinishing()) {
            dialog.show();
        }
        return dialog;
    }

    private void showHeadDialog() {
        List<String> names = new ArrayList<>();
        names.add(getString(R.string.modify_user_camera));
        names.add(getString(R.string.modify_user_photo_album));
        showDialog(new SelectDialog.SelectDialogListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0: // 直接调起相机
                        //打开选择,本次允许选择的数量
                        ImagePicker.getInstance().setSelectLimit(1);
                        Intent intent = new Intent(ModifyUserActivity.this, ImageGridActivity.class);
                        intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS, true); // 是否是直接打开相机
                        startActivityForResult(intent, REQUEST_CODE_SELECT);
                        break;
                    case 1:
                        //打开选择,本次允许选择的数量
                        ImagePicker.getInstance().setSelectLimit(1);
                        Intent intent1 = new Intent(ModifyUserActivity.this, ImageGridActivity.class);
                                /* 如果需要进入选择的时候显示已经选中的图片，
                                 * 详情请查看ImagePickerActivity
                                 * */
//                                intent1.putExtra(ImageGridActivity.EXTRAS_IMAGES,images);
                        startActivityForResult(intent1, REQUEST_CODE_SELECT);
                        break;
                    default:
                        break;
                }

            }
        }, names);
    }

    ArrayList<ImageItem> images = new ArrayList<>();

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            //添加图片返回
            if (data != null && requestCode == REQUEST_CODE_SELECT) {
                images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                if (images != null) {
                    Log.e(TAG, "images : ---> " + images.get(0).toString());
                    Log.e(TAG, "images size : ---> " + images.size());
//                    mHandler.sendEmptyMessage(DISPLAY_BG);

//                    circle_img_head.setImageBitmap(getBitmapForCircleImg());
                    Glide.with(ModifyUserActivity.this)
                            .load(images.get(0).path)
                            .asBitmap()

                            .into(new SimpleTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                    circle_img_head.setImageBitmap(resource);
                                }
                            });
                }
            }
        } else if (100 == resultCode) {
            String nick = data.getStringExtra("nick_name");
            text_user_nick.setText(nick);
        }


//        else if (resultCode == ImagePicker.RESULT_CODE_BACK) {
//            //预览图片返回
//            if (data != null && requestCode == REQUEST_CODE_PREVIEW) {
//                images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_IMAGE_ITEMS);
//                if (images != null) {
//                    selImageList.clear();
//                    selImageList.addAll(images);
//                    adapter.setImages(selImageList);
//                }
    }
//        }
//    }

    private Bitmap getBitmapForCircleImg() {
        Bitmap bitmap;
        if (images.size() <= 0) {
            BitmapDrawable mDrawable = (BitmapDrawable) circle_img_head.getDrawable();
            bitmap = mDrawable.getBitmap();
        } else {
            ImageItem item = images.get(0);
            bitmap = BitmapFactory.decodeFile(item.path);
        }
        return bitmap;
    }


    private static final int DISPLAY_BG = 0x01;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            if (DISPLAY_BG == msg.what) {
//                Bitmap mBitmap = Bitmap.createBitmap(item.width, item.height, Bitmap.Config.ARGB_8888);
//                mImagePicker.getImageLoader().displayImage(ModifyUserActivity.this, item.path, circle_img_head, item.width, item.height);
//                ImageItem item = images.get(0);
//                Bitmap mBitmap = BitmapFactory.decodeFile(item.path);

                circle_img_head.setImageBitmap(getBitmapForCircleImg());

            }

        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkHttpUtils.getInstance().cancelTag(this);
    }
}
