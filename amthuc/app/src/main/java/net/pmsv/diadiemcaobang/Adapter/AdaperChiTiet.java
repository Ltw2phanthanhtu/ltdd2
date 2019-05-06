package net.pmsv.diadiemcaobang.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import net.pmsv.diadiemcaobang.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by May1 on 5/31/2017.
 */

public class AdaperChiTiet extends BaseExpandableListAdapter {

    private Context _context;
    private ArrayList<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, ArrayList<String>> _listDataChild;

    public AdaperChiTiet(Context context, ArrayList<String> listDataHeader,
                                     HashMap<String, ArrayList<String>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return _listDataChild.get(_listDataHeader.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return _listDataHeader.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return _listDataChild.get(_listDataHeader.get(groupPosition)).get(childPosititon);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosititon) {
        return childPosititon;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View view, ViewGroup viewGroup) {
        String data = (String) getGroup(groupPosition);

        if (view == null) {
            LayoutInflater li = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = li.inflate(R.layout.lv_header_diadiem_detail, null);
        }

        ImageView imgHeader = (ImageView) view.findViewById(R.id.imgheaderOfElv);
        TextView tvHeader = (TextView) view.findViewById(R.id.tvHeaderDiaDiemDetail);
        tvHeader.setTypeface(null, Typeface.BOLD);
        tvHeader.setText(data);

        if (isExpanded == true) {
            tvHeader.setTextColor(Color.parseColor("#ff4081"));
        }
        else{
            tvHeader.setTextColor(Color.BLACK);
        }

        switch (groupPosition)
        {
            case 0:imgHeader.setImageResource(R.drawable.icon_thongtin);
                break;
            case 1: imgHeader.setImageResource(R.drawable.icon_clock);
                break;
            case 2: imgHeader.setImageResource(R.drawable.icon_map);
                break;
            case 3: imgHeader.setImageResource(R.drawable.icon_camera);
                break;
            case 4: imgHeader.setImageResource(R.drawable.icon_lightbub);
                break;
        }

        view.setPadding(0, 30, 0, 0);

        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean b, View view, ViewGroup viewGroup) {
        final String data = (String) getChild(groupPosition, childPosition);

        if (view == null) {
            LayoutInflater li = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = li.inflate(R.layout.lv_item_diadiem_detail, null);
        }
        TextView tvItem = (TextView) view.findViewById(R.id.tv_item_diadiem_detail);
        tvItem.setText(data);

        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

}
