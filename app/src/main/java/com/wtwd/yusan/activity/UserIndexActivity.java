package com.wtwd.yusan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.ui.ImagePreviewDelActivity;
import com.wtwd.yusan.R;
import com.wtwd.yusan.base.CommonToolBarActivity;
import com.wtwd.yusan.util.GlideImageLoader;
import com.wtwd.yusan.util.Utils;
import com.wtwd.yusan.widget.view.SpaceItemDecoration;

import java.util.ArrayList;
import java.util.List;

import static com.lzy.imagepicker.ImagePicker.REQUEST_CODE_PREVIEW;

/**
 * time:2018/4/16
 * Created by w77996
 */

public class UserIndexActivity extends CommonToolBarActivity {

    private static final int GRID_COUNT = 3;

    private RecyclerView recycler_pic;
    private MeAddPicAdapter mMeAddPicAdapter;

    @Override
    public void onCreateCommonView(Bundle saveInstanceState) {
        initView();
    }

    private void initView() {
        recycler_pic = (RecyclerView) findViewById(R.id.recycler_pic);
        recycler_pic.setLayoutManager(new GridLayoutManager(this, GRID_COUNT));
        RecyclerView.ItemDecoration mDi = new SpaceItemDecoration(Utils.dip2px(this, 1), GRID_COUNT);
        recycler_pic.addItemDecoration(mDi);


        mMeAddPicAdapter = new MeAddPicAdapter(R.layout.item_add_img, null);
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
        recycler_pic.setAdapter(mMeAddPicAdapter);

        initImagePicker();
        addListener();

    }

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

    }


    ArrayList<ImageItem> images = null;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if (data != null && requestCode == 100) {
                images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                mMeAddPicAdapter.getData().addAll(images);
                mMeAddPicAdapter.notifyDataSetChanged();
            } else {
//                Toast.makeText(this, "没有数据", Toast.LENGTH_SHORT).show();
                showToast(getString(R.string.user_index_no_data));
            }
        }
    }

    private class MeAddPicAdapter extends BaseQuickAdapter<ImageItem, BaseViewHolder> {

        public MeAddPicAdapter(int layoutResId, @Nullable List<ImageItem> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, ImageItem item) {
            imagePicker.getImageLoader().displayImage(UserIndexActivity.this, item.path, (ImageView) helper.getView(R.id.img_item_recycler), item.width, item.height);

        }
    }
}
