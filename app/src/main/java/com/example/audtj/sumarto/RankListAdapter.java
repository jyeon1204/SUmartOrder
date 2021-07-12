package com.example.audtj.sumarto;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.List;

public class RankListAdapter extends BaseAdapter {
    private Context context;
    private List<Order> orderList;
    private Fragment parent;

    public RankListAdapter(Context context, List<Order> orderList, Fragment parent) {
        this.context = context;
        this.orderList = orderList;
        this.parent = parent;
    }

    @Override
    public int getCount() {
        return orderList.size();
    }

    @Override
    public Object getItem(int i) {
        return orderList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View convertView, final ViewGroup viewGroup) {
        View v = View.inflate(context, R.layout.rank, null);
        TextView rankTextView = (TextView)v.findViewById(R.id.rankTextView);

        TextView menuCategory = (TextView)v.findViewById(R.id.menuCategory);
        TextView menuName = (TextView)v.findViewById(R.id.menuName);
        TextView menuPrice = (TextView)v.findViewById(R.id.menuPrice);

        menuCategory.setText(orderList.get(i).menuCategory);
        menuName.setText(orderList.get(i).menuName);
        menuPrice.setText(orderList.get(i).menuPrice + " 원") ;

        rankTextView.setText((i+1) + "위");

       v.setTag(orderList.get(i).getMenuID());
        return v;

    }
}
