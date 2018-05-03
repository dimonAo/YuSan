/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
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

import com.hyphenate.chat.EMChatRoom;
import com.wtwd.yusan.R;

import java.util.List;

public class ChatRoomAdapter extends ArrayAdapter<EMChatRoom> {

	private LayoutInflater inflater;
	private String addChatRoomString;

	public ChatRoomAdapter(Context context, int res, List<EMChatRoom> groups) {
		super(context, res, groups);
		this.inflater = LayoutInflater.from(context);
		addChatRoomString = context.getResources().getString(R.string.add_public_chat_room);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.em_row_group, parent, false);
			}
			((TextView) convertView.findViewById(R.id.name)).setText(getItem(position).getName());

		return convertView;
	}

	@Override
	public int getCount() {
		return super.getCount();
	}

}
