/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wtwd.yusan.ease.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.wtwd.yusan.R;
import com.wtwd.yusan.ease.net.response.GroupInfo;

import java.util.List;

public class NewGroupAdapter extends ArrayAdapter<GroupInfo> {

    private LayoutInflater inflater;

    public NewGroupAdapter(Context context, int res, List<GroupInfo> groups) {
        super(context, res, groups);
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.em_row_group, parent, false);
        }
        ((TextView) convertView.findViewById(R.id.name)).setText(getItem(position).getGroupIdStr());
        convertView.setVisibility(View.VISIBLE);

        return convertView;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }
}