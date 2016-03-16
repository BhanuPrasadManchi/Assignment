/*
 *    Copyright (C) 2015 Haruki Hasegawa
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.healthifyme.bhanuprasad.healthifymetask;

import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.h6ah4i.android.widget.advrecyclerview.expandable.ExpandableItemConstants;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemViewHolder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

class MyExpandableItemAdapter
        extends AbstractExpandableItemAdapter<MyExpandableItemAdapter.MyGroupViewHolder, MyExpandableItemAdapter.MyChildViewHolder> {
    private static final String TAG = "MyExpandableItemAdapter";

    // NOTE: Make accessible with short name
    private interface Expandable extends ExpandableItemConstants {
    }

    private SlotsObject mProvider;

    public static abstract class MyBaseViewHolder extends AbstractExpandableItemViewHolder {

        public FrameLayout mContainer;

        public MyBaseViewHolder(View v) {
            super(v);
            mContainer = (FrameLayout) v.findViewById(R.id.container);
        }
    }

    public static class MyGroupViewHolder extends MyBaseViewHolder {
        public ExpandableItemIndicator mIndicator;
        public FrameLayout mContainer;
        public TextView mTextView;
        public TextView mSlotsAvailable;
        public FloatingActionButton fab;

        public MyGroupViewHolder(View v) {
            super(v);
            mIndicator = (ExpandableItemIndicator) v.findViewById(R.id.indicator);
            mContainer = (FrameLayout) v.findViewById(R.id.container);
            mTextView = (TextView) v.findViewById(R.id.text1);
            mSlotsAvailable = (TextView) v.findViewById(R.id.slot_available);
            fab = (FloatingActionButton)v.findViewById(R.id.fab);
        }
    }

    public static class MyChildViewHolder extends MyBaseViewHolder {
        public TextView mTextView;
        public MyChildViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.text1);
        }
    }

    public MyExpandableItemAdapter(SlotsObject dataProvider) {
        mProvider = dataProvider;

        // ExpandableItemAdapter requires stable ID, and also
        // have to implement the getGroupItemId()/getChildItemId() methods appropriately.
        setHasStableIds(true);
    }

    @Override
    public int getGroupCount() {
        return 3;
    }

    @Override
    public int getChildCount(int groupPosition) {
        if (groupPosition == 0){
            return mProvider.morning.size();
        }else if (groupPosition == 1){
            return mProvider.afternoon.size();
        }else if (groupPosition == 2){
            return mProvider.evening.size();
        }
//        switch (groupPosition) {
//            case 0:
//                return mProvider.morning.size();
//            case 1:
//                return mProvider.afternoon.size();
//            case 2:
//                return mProvider.evening.size();
//
//        }
        return 0;
    }

    @Override
    public long getGroupId(int groupPosition) {
        switch (groupPosition) {
            case 0:
                return 0;
            case 1:
                return 1;
            case 2:
                return 2;


        }
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        switch (groupPosition) {
            case 0:
                return Long.parseLong((mProvider.morning.get(childPosition).slot_id));
            case 1:
                return Long.parseLong((mProvider.afternoon.get(childPosition).slot_id));
            case 2:
                return Long.parseLong((mProvider.evening.get(childPosition).slot_id));
        }
        return 1;
    }

    @Override
    public int getGroupItemViewType(int groupPosition) {
        return 0;
    }

    @Override
    public int getChildItemViewType(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public MyGroupViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View v = inflater.inflate(R.layout.list_group_item, parent, false);
        return new MyGroupViewHolder(v);
    }

    @Override
    public MyChildViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View v = inflater.inflate(R.layout.list_item, parent, false);
        return new MyChildViewHolder(v);
    }

    @Override
    public void onBindGroupViewHolder(MyGroupViewHolder holder, int groupPosition, int viewType) {

        if (groupPosition == 0){
            holder.mTextView.setText("Morning");
            holder.mSlotsAvailable.setText(mProvider.morning_slots+" Slots available");

        }else if (groupPosition == 1){
            holder.mTextView.setText("Afternoon");
            holder.mSlotsAvailable.setText(mProvider.afternoon_slots+" Slots available");
        }else if (groupPosition == 2){
            holder.mTextView.setText("Evening");
            holder.mSlotsAvailable.setText(mProvider.evening_slots+" Slots available");
        }

        // mark as clickable
        holder.itemView.setClickable(true);

        // set background resource (target view ID: container)
        final int expandState = holder.getExpandStateFlags();

        if ((expandState & ExpandableItemConstants.STATE_FLAG_IS_UPDATED) != 0) {
            int bgResId;
            boolean isExpanded;
            boolean animateIndicator = ((expandState & Expandable.STATE_FLAG_HAS_EXPANDED_STATE_CHANGED) != 0);

            if ((expandState & Expandable.STATE_FLAG_IS_EXPANDED) != 0) {
                bgResId = R.drawable.bg_group_item_expanded_state;
                isExpanded = true;
            } else {
                bgResId = R.drawable.bg_group_item_normal_state;
                isExpanded = false;
            }

            holder.mContainer.setBackgroundResource(bgResId);
            holder.mIndicator.setExpandedState(isExpanded, animateIndicator);
        }
    }

    public String findTime(String dateString) throws ParseException {
        String[] dateformat = dateString.split("\\+");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = format.parse(dateformat[0]);
        String hour = (String) android.text.format.DateFormat.format("HH", date); //20
        String min = (String) android.text.format.DateFormat.format("mm", date); //20
        if (Integer.parseInt(hour)>12)
            return (Integer.parseInt(hour)-12)+":"+min +" PM";
        else
            return hour+":"+min +" AM";
    }

    @Override
    public void onBindChildViewHolder(MyChildViewHolder holder, int groupPosition, int childPosition, int viewType) {

        if (groupPosition == 0){
            if (mProvider.morning.size()>0){
                DayObject dayObject = mProvider.morning.get(childPosition);
                try {
                    holder.mTextView.setText(findTime(dayObject.start_time)+" - "+findTime(dayObject.end_time));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

        }else if (groupPosition == 1) {
            if (mProvider.afternoon.size() > 0) {
                DayObject dayObject = mProvider.afternoon.get(childPosition);
                try {
                    holder.mTextView.setText(findTime(dayObject.start_time) + " - " + findTime(dayObject.end_time));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }else if (groupPosition == 2){
                if (mProvider.evening.size()>0){
                    DayObject dayObject = mProvider.evening.get(childPosition);
                    try {
                        holder.mTextView.setText(findTime(dayObject.start_time)+" - "+findTime(dayObject.end_time));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
            }
        }

        // set background resource (target view ID: container)
        int bgResId;
        bgResId = R.drawable.bg_item_normal_state;
        holder.mContainer.setBackgroundResource(bgResId);
    }

    @Override
    public boolean onCheckCanExpandOrCollapseGroup(MyGroupViewHolder holder, int groupPosition, int x, int y, boolean expand) {
//        // check the item is *not* pinned
//        if (mProvider.getGroupItem(groupPosition).isPinned()) {
//            // return false to raise View.OnClickListener#onClick() event
//            return false;
//        }

        // check is enabled
        if (!(holder.itemView.isEnabled() && holder.itemView.isClickable())) {
            return false;
        }

        return true;
    }
}
