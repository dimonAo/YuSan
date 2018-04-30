package com.wtwd.yusan.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPoolAdapter;
import com.bumptech.glide.util.Util;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.ui.ImagePreviewDelActivity;
import com.wtwd.yusan.R;
import com.wtwd.yusan.adapter.MeAddPicAdapter;
import com.wtwd.yusan.base.CommonToolBarActivity;
import com.wtwd.yusan.entity.ResultEntity;
import com.wtwd.yusan.entity.TaskEntity;
import com.wtwd.yusan.util.Constans;
import com.wtwd.yusan.util.GlideImageLoader;
import com.wtwd.yusan.util.GsonUtils;
import com.wtwd.yusan.util.Pref;
import com.wtwd.yusan.util.Utils;
import com.wtwd.yusan.widget.recycler.EasyRefreshLayout;
import com.wtwd.yusan.widget.recycler.LoadModel;
import com.wtwd.yusan.widget.view.SpaceItemDecoration;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

import static com.lzy.imagepicker.ImagePicker.REQUEST_CODE_PREVIEW;

/**
 * time:2018/4/16
 * Created by w77996
 */

public class UserIndexActivity extends CommonToolBarActivity {

    private static final int GRID_COUNT = 3;

    private RecyclerView recycler_pic;
    private MeAddPicAdapter mMeAddPicAdapter;
    long receiveUserId;
    ArrayList<ImageItem> images = new ArrayList<>();
    private EasyRefreshLayout refresh_img;
//    private String[] imgUrl;
//            = {"https://img-blog.csdn.net/20170428175617391?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQveWVjaGFvYQ==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/Center",
//            "https://img-blog.csdn.net/20170428175632797?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQveWVjaGFvYQ==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/Center",
//            "https://img-blog.csdn.net/20170428175637782?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQveWVjaGFvYQ==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/Center",
//            "https://img-blog.csdn.net/20170428175627934?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQveWVjaGFvYQ==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/Center"};

    @Override
    public void onCreateCommonView(Bundle saveInstanceState) {
        getUserId();

        initView();
    }

    private void initView() {
        refresh_img = (EasyRefreshLayout) findViewById(R.id.refresh_img);
        recycler_pic = (RecyclerView) findViewById(R.id.recycler_pic);
        recycler_pic.setLayoutManager(new GridLayoutManager(this, GRID_COUNT));
        RecyclerView.ItemDecoration mDi = new SpaceItemDecoration(Utils.dip2px(this, 1), GRID_COUNT);
        recycler_pic.addItemDecoration(mDi);
        mMeAddPicAdapter = new MeAddPicAdapter(R.layout.item_add_img, null);

        /**
         * 如果进入的是别人的主页，最后不显示添加按钮
         */
        if (receiveUserId == mPref.getUserId()) {
            View view = LayoutInflater.from(this).inflate(R.layout.item_add_foot, null, false);
            ImageView img_item_recycler = view.findViewById(R.id.img_item_recycler);
            img_item_recycler.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    readyGoForResult(ImageGridActivity.class, 100);
                }
            });

            mMeAddPicAdapter.setFooterViewAsFlow(true);
            mMeAddPicAdapter.addFooterView(view);
        }

        recycler_pic.setAdapter(mMeAddPicAdapter);


        initImagePicker();
        addListener();
    }

    private int mLoadCount;

    private void addListener() {
        mMeAddPicAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intentPreview = new Intent(UserIndexActivity.this, ImagePreviewDelActivity.class);
                intentPreview.putExtra(ImagePicker.EXTRA_IMAGE_ITEMS, (ArrayList<ImageItem>) mMeAddPicAdapter.getData());
                intentPreview.putExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, position);
                intentPreview.putExtra(ImagePicker.EXTRA_FROM_ITEMS, true);
                startActivityForResult(intentPreview, REQUEST_CODE_PREVIEW);
            }
        });

        refresh_img.setLoadMoreModel(LoadModel.ADVANCE_MODEL, 0);
        refresh_img.addEasyEvent(new EasyRefreshLayout.EasyEvent() {
            @Override
            public void onLoadMore() {
                getImgData(20 * mLoadCount, 20);
                mLoadCount++;
            }

            @Override
            public void onRefreshing() {

            }
        });
    }


    /**
     * 进入这个界面需要传入userId，用来区别现在查看的是哪个用户的主页
     */
    private void getUserId() {
        if (null == getIntent()) {
            return;
        }

        Bundle bundle = getIntent().getExtras();
        receiveUserId = bundle.getLong("userId", 0L);
    }


    /**
     * 获取主页头部信息
     *
     * @param userId
     */
    private void receiveHomeInfo(String userId) {
        OkHttpUtils.post()
                .url(Constans.RECEIVE_HOME_INFO)
                .addParams("userId", userId)
                .build()
                .connTimeOut(Constans.TIME_OUT)
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {

                    }
                });
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (0L != receiveUserId) {

            receiveHomeInfo(receiveUserId + "");
        }
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_userindex;
    }

    @Override
    public View getSnackView() {
        return null;
    }

    ImagePicker imagePicker;

    private void initImagePicker() {
        imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());
        imagePicker.setCrop(false);
        imagePicker.setMultiMode(false);
        imagePicker.setCropCacheFolder(new File(Utils.getStorageDirectory(this)));

    }

    private void getImgData(int startIndex, int count) {
//        images = new ArrayList<>();
        Map<String, String> mImgMap = new HashMap<>();
        mImgMap.put("userId", receiveUserId + "");
        mImgMap.put("start", startIndex + "");
        mImgMap.put("count", count + "");

        OkHttpUtils.get()
                .params(mImgMap)
                .url(Constans.RECEIVE_HOME_IMG)
                .build()
                .connTimeOut(Constans.TIME_OUT)
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
//                        ResultEntity<TaskEntity> mEn = Utils.getResultEntity(response);
                        List<ImageItem> mLists = new ArrayList<>();
                        try {
                            JSONObject mImgJson = new JSONObject(response);
                            int resultCode = mImgJson.optInt("status");

                            if (Constans.REQUEST_SUCCESS == resultCode) {

                                String mImg = mImgJson.optString("object");

                                List<ImageInfo> mImgList = GsonUtils.GsonToList(mImg, ImageInfo.class);

                                if (mImgList.size() > 0) {
                                    for (int i = 0; i < mImgList.size(); i++) {
                                        ImageItem mItem = new ImageItem();
                                        mItem.path = mImgList.get(i).getImg();
                                        mLists.add(mItem);
                                    }
                                }

                                mMeAddPicAdapter.getData().addAll(mLists);
                                mMeAddPicAdapter.notifyDataSetChanged();
                                refresh_img.loadMoreComplete();
                                refresh_img.closeLoadView();

                                if (mImgList.size() < 20) {
                                    refresh_img.setLoadMoreModel(LoadModel.NONE);
                                }

                            } else {

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                });
//        for (int i = 0; i < imgUrl.length; i++) {
//            ImageItem item = new ImageItem();
//            item.path = imgUrl[i];
//            images.add(item);
//        }

        mMeAddPicAdapter.getData().addAll(images);
        mMeAddPicAdapter.notifyDataSetChanged();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if (data != null && requestCode == 100) {
                images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                Log.e(TAG, "" + images.get(0).width + " : ---> " + images.get(0).height);
                Log.e(TAG, "path : ---> " + images.get(0).path);
                mMeAddPicAdapter.getData().addAll(images);
                mMeAddPicAdapter.notifyDataSetChanged();

                Bitmap bitmap = BitmapFactory.decodeFile(images.get(0).path);//filePath

                uploadImgToService(bitmap);

            } else {
//                Toast.makeText(this, "没有数据", Toast.LENGTH_SHORT).show();
                showToast(getString(R.string.user_index_no_data));
            }
        } else if (resultCode == ImagePicker.RESULT_CODE_BACK) {
            //预览图片返回
            if (data != null && requestCode == REQUEST_CODE_PREVIEW) {
                images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_IMAGE_ITEMS);
                if (images != null) {
                    mMeAddPicAdapter.getData().clear();
                    mMeAddPicAdapter.getData().addAll(images);
                    mMeAddPicAdapter.notifyDataSetChanged();


                }
            }
        }
    }


    /**
     * 上传单张图片到服务器
     *
     * @param bitmap
     */
    private void uploadImgToService(Bitmap bitmap) {
        Map<String, String> mImgMap = new HashMap<>();
        mImgMap.put("userId", mPref.getUserId() + "");
        mImgMap.put("headImg", Utils.Bitmap2StrByBase64(bitmap));

        OkHttpUtils.post()
                .url(Constans.UPLOAD_IMG)
                .params(mImgMap)
                .build()
                .connTimeOut(Constans.TIME_OUT)
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {

                    }
                });

    }


    private class MeAddPicAdapter extends BaseQuickAdapter<ImageItem, BaseViewHolder> {

        public MeAddPicAdapter(int layoutResId, @Nullable List<ImageItem> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, ImageItem item) {
//            imagePicker.getImageLoader().displayImage(UserIndexActivity.this,
//                    item.path,
//                    (ImageView) helper.getView(R.id.img_item_recycler),
//                    item.width,
//                    item.height);
            Log.e(TAG, "item.path : ===> " + item.path);
            Uri uri;
            DiskCacheStrategy disk;
            if (item.path.contains("http")) {
                uri = Uri.parse(item.path);
                disk = DiskCacheStrategy.RESULT;
            } else {
                uri = Uri.fromFile(new File(item.path));
                disk = DiskCacheStrategy.NONE;
            }
//            if (item.path.contains("http")) {
            Glide.with(mContext)
                    .load(uri)
                    .asBitmap()
                    .diskCacheStrategy(disk)
                    .into((ImageView) helper.getView(R.id.img_item_recycler));
//            } else {
//                Glide.with(mContext)
//                        .load(Uri.fromFile(new File(item.path)))
//                        .asBitmap()
//                        .diskCacheStrategy(DiskCacheStrategy.NONE)
//                        .into((ImageView) helper.getView(R.id.img_item_recycler));
//            }
        }
    }


    private class ImageInfo {
        private Long user_img_id;                 //图片ID

        private String userIndexImgIdStr;

        private Long user_id;

        private String img;         //图片

        private String create_time;             //生成时间


        public Long getUser_img_id() {
            return user_img_id;
        }

        public void setUser_img_id(Long user_img_id) {
            this.user_img_id = user_img_id;
        }

        public String getUserIndexImgIdStr() {
            return userIndexImgIdStr;
        }

        public void setUserIndexImgIdStr(String userIndexImgIdStr) {
            this.userIndexImgIdStr = userIndexImgIdStr;
        }

        public Long getUser_id() {
            return user_id;
        }

        public void setUser_id(Long user_id) {
            this.user_id = user_id;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }
    }

}
