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

public class StatisticsOrderListAdapter extends BaseAdapter {
    private Context context;
    private List<Order> orderList;
    private Fragment parent;
    private String userID = MainActivity.userID;


    public StatisticsOrderListAdapter(Context context, List<Order> orderList, Fragment parent) {
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
        View v = View.inflate(context, R.layout.statistics, null);

        TextView menuCategory = (TextView) v.findViewById(R.id.menuCategory);
        TextView menuName = (TextView) v.findViewById(R.id.menuName);
        TextView menuPrice = (TextView) v.findViewById(R.id.menuPrice);

        menuCategory.setText(orderList.get(i).menuCategory + "");
        menuName.setText(orderList.get(i).menuName + "");
        menuPrice.setText(orderList.get(i).menuPrice + "원");

        v.setTag(orderList.get(i).getMenuID());

       Button deleteButton = (Button)v.findViewById(R.id.deleteButton);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userID = MainActivity.userID;
                Response.Listener<String> responseListener = new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try
                        {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if (success) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(parent.getActivity());
                                AlertDialog dialog = builder.setMessage("메뉴가 삭제 되었습니다.")
                                        .setPositiveButton("확인", null)
                                        .create();
                                dialog.show();
                                twoFragment.totalPrice -= orderList.get(i).getMenuPrice();
                                twoFragment.price.setText(twoFragment.totalPrice+" 원");
                                orderList.remove(i);
                                notifyDataSetChanged();
                            }
                            else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(parent.getActivity());
                                AlertDialog dialog = builder.setMessage("메뉴 삭제에 실패했습니다.")
                                        .setNegativeButton("다시시도", null)
                                        .create();
                                dialog.show();
                            }
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                };
                DeleteRequest deleteRequest = new DeleteRequest(userID, orderList.get(i).getMenuID() + "",responseListener);
                RequestQueue queue = Volley.newRequestQueue(parent.getActivity());
                queue.add(deleteRequest);
            }
        });

        return v;

    }
    }
