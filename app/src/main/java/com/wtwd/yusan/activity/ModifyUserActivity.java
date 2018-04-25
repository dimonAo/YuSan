package com.wtwd.yusan.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.util.Util;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.ui.ImagePreviewDelActivity;
import com.lzy.imagepicker.view.CropImageView;
import com.wtwd.yusan.R;
import com.wtwd.yusan.base.CommonToolBarActivity;
import com.wtwd.yusan.util.GlideImageLoader;
import com.wtwd.yusan.util.Utils;
import com.wtwd.yusan.util.dialog.DialogUtil;
import com.wtwd.yusan.util.dialog.SelectDialog;
import com.wtwd.yusan.widget.view.CircleImageView;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    private TextView tv_modifyuser_sex;
    private TextView tv_modifyuser_height;
    private TextView text_user_nick;
    private CircleImageView circle_img_head;

    //    private String[] mModifySex;
    private List<String> mModifySexList = new ArrayList<>();
    private List<String> mModifyHeight = new ArrayList<>();
    private Dialog mSelectorDialog;
    private ImagePicker mImagePicker;

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

        tv_modifyuser_sex = (TextView) findViewById(R.id.tv_modifyuser_sex);
        tv_modifyuser_height = (TextView) findViewById(R.id.tv_modifyuser_height);
        text_user_nick = (TextView) findViewById(R.id.text_user_nick);
        circle_img_head = (CircleImageView) findViewById(R.id.circle_img_head);

        addListener();
        initImagePicker();
    }


    private void addListener() {
        relative_head.setOnClickListener(this);
        relative_name.setOnClickListener(this);
        relative_sex.setOnClickListener(this);
        relative_height.setOnClickListener(this);

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
                        /**
                         * 0.4.7 目前直接调起相机不支持裁剪，如果开启裁剪后不会返回图片，请注意，后续版本会解决
                         *
                         * 但是当前直接依赖的版本已经解决，考虑到版本改动很少，所以这次没有上传到远程仓库
                         *
                         * 如果实在有所需要，请直接下载源码引用。
                         */
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

    ArrayList<ImageItem> images = null;

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
                    mHandler.sendEmptyMessage(DISPLAY_BG);
                }
            }
        }
//        else if (100 == resultCode) {
//            String nick = data.getStringExtra("nick_name");
//            text_user_nick.setText(nick);
//        }


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

    private static final int DISPLAY_BG = 0x01;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            if (DISPLAY_BG == msg.what) {
                ImageItem item = images.get(0);
//                Bitmap mBitmap = Bitmap.createBitmap(item.width, item.height, Bitmap.Config.ARGB_8888);
                Bitmap mBitmap = BitmapFactory.decodeFile(item.path);
//                mImagePicker.getImageLoader().displayImage(ModifyUserActivity.this, item.path, circle_img_head, item.width, item.height);
                circle_img_head.setImageBitmap(mBitmap);
            }

        }
    };

}
