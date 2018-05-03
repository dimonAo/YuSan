package com.wtwd.yusan.ease.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wtwd.yusan.R;
import com.wtwd.yusan.ease.net.response.ContactListInfo;
import com.wtwd.yusan.ease.util.CharacterParser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by pc-20170420 on 2018/4/24.
 */

public class ContactListSortAdapter extends BaseAdapter implements SectionIndexer {

    private List<ContactListInfo> list = null;
    private Context mContext;
    public boolean[] isCheckedArray;
    private List<String> existMembers;
    private boolean mIsCheck;

    public ContactListSortAdapter(Context mContext, List<ContactListInfo> list,boolean isCheck) {
        this.mContext = mContext;
        this.mIsCheck=isCheck;
        this.list = filledData(list);
        isCheckedArray = new boolean[list.size()];
        if (existMembers == null)
            existMembers = new ArrayList<String>();
    }

    /**
     * 当ListView数据发生变化时,调用此方法来更新ListView
     *
     * @param list
     */
    public void updateListView(List<ContactListInfo> list) {
        list = filledData(list);
        this.list = list;
        notifyDataSetChanged();
    }

    public int getCount() {
        return this.list.size();
    }

    public Object getItem(int position) {
        return list.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        ViewHolder viewHolder = null;
        final ContactListInfo contact = list.get(position);
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.item_contact_list, null);
            viewHolder.cbCantact = view.findViewById(R.id.cb_cantact);
            if(mIsCheck){
                viewHolder.cbCantact.setVisibility(View.VISIBLE);
            }else{
                viewHolder.cbCantact.setVisibility(View.GONE);
            }
            viewHolder.tvContactName = view.findViewById(R.id.contactName);
            viewHolder.tvIndexName = view.findViewById(R.id.indexName);
            viewHolder.ivAvatar = view.findViewById(R.id.ivAvatar);
            viewHolder.llIndex = view.findViewById(R.id.llIndex);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        //根据position获取分类的首字母的char ascii值
        int section = getSectionForPosition(position);

        //如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
        if (position == getPositionForSection(section)) {
            viewHolder.llIndex.setVisibility(View.VISIBLE);
            viewHolder.tvIndexName.setVisibility(View.VISIBLE);
            viewHolder.tvIndexName.setText(contact.getSortLetters());
        } else {
            viewHolder.llIndex.setVisibility(View.GONE);
            viewHolder.tvIndexName.setVisibility(View.GONE);
        }

        Glide.with(mContext).load(contact.getHead_img()).asBitmap().placeholder(R.drawable.ease_default_avatar).error(R.drawable.ease_default_avatar).into(viewHolder.ivAvatar);
        final String userName = contact.getUser_name();
        viewHolder.tvContactName.setText(userName);
        if (viewHolder.cbCantact != null) {
            if (existMembers != null && existMembers.contains(userName)) {
                viewHolder.cbCantact.setButtonDrawable(R.drawable.em_checkbox_bg_gray_selector);
            } else {
                viewHolder.cbCantact.setButtonDrawable(R.drawable.em_checkbox_bg_selector);
            }
        }
        viewHolder.cbCantact.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // check the exist members
                if (existMembers.contains(userName)) {
                    isChecked = true;
                    buttonView.setChecked(true);
                }
                isCheckedArray[position] = isChecked;
            }
        });
        // keep exist members checked
        if (existMembers.contains(userName)) {
            viewHolder.cbCantact.setChecked(true);
            isCheckedArray[position] = true;
        } else {
            viewHolder.cbCantact.setChecked(isCheckedArray[position]);
        }
        return view;

    }


    final static class ViewHolder {
        LinearLayout llIndex;
        TextView tvIndexName;
        TextView tvContactName;
        CircleImageView ivAvatar;
        CheckBox cbCantact;
    }


    /**
     * 根据ListView的当前位置获取分类的首字母的char ascii值
     */
    public int getSectionForPosition(int position) {
        return list.get(position).getSortLetters().charAt(0);
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = list.get(i).getSortLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }

        return -1;
    }


    @Override
    public Object[] getSections() {
        return null;
    }

    public class PinyinComparator implements Comparator<ContactListInfo> {

        public int compare(ContactListInfo o1, ContactListInfo o2) {
            //这里主要是用来对ListView里面的数据根据ABCDEFG...来排序
            if (o2.getSortLetters().equals("#")) {
                return -1;
            } else if (o1.getSortLetters().equals("#")) {
                return 1;
            } else {
                return o1.getSortLetters().compareTo(o2.getSortLetters());
            }
        }
    }

    /**
     * 为ListView填充数据
     *
     * @param data
     * @return
     */
    private List<ContactListInfo> filledData(List<ContactListInfo> data) {
        List<ContactListInfo> mSortList = new ArrayList<ContactListInfo>();
        CharacterParser characterParser = new CharacterParser();
        for (int i = 0; i < data.size(); i++) {
            ContactListInfo sortModel = data.get(i);
            //汉字转换成拼音
            String pinyin = characterParser.getSelling(sortModel.getUser_name());
            String sortString = pinyin.substring(0, 1).toUpperCase();

            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")) {
                sortModel.setSortLetters(sortString.toUpperCase());
            } else {
                sortModel.setSortLetters("#");
            }
            mSortList.add(sortModel);
        }
        Collections.sort(mSortList,new PinyinComparator());

        return mSortList;

    }


}
