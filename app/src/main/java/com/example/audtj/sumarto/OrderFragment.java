package com.example.audtj.sumarto;

import android.app.AlertDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.JsonReader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class OrderFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public OrderFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters

    public static OrderFragment newInstance(String param1, String param2) {
        OrderFragment fragment = new OrderFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private ArrayAdapter categoryAdapter;
    private Spinner categorySpinner;

    private String sungshinUniversity = "";


    private ListView orderListView;
    private OrderListAdapter adapter;
    private List<Order> orderList;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final RadioGroup sungshinUniversityGroup = (RadioGroup)getView().findViewById(R.id.sungshinUniversityGroup);

        categorySpinner = (Spinner) getView().findViewById(R.id.categorySpinner);

        sungshinUniversityGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup RadioGroup, int i) {
                RadioButton sungshinButton = (RadioButton) getView().findViewById(i);
                sungshinUniversity = sungshinButton.getText().toString();

                if (sungshinUniversity.equals("수정"))
                {
                    categoryAdapter = ArrayAdapter.createFromResource(getActivity(),R.array.sumenucategory, android.R.layout.simple_spinner_dropdown_item);
                    categorySpinner.setAdapter(categoryAdapter);
                }
                else if (sungshinUniversity.equals("운정"))
                {
                    categoryAdapter = ArrayAdapter.createFromResource(getActivity(),R.array.unmenucategory, android.R.layout.simple_spinner_dropdown_item);
                    categorySpinner.setAdapter(categoryAdapter);
                }
            }
        });

        orderListView = (ListView) getView().findViewById(R.id.orderListView);
        orderList = new ArrayList<Order>();
        adapter = new OrderListAdapter(getContext().getApplicationContext(), orderList, this);
        orderListView.setAdapter(adapter);

        Button searchButton = (Button) getView().findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new BackgroundTask().execute();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    class BackgroundTask extends AsyncTask<Void, Void, String> {
        String target;

        @Override
        public void onPreExecute() {
            try {
               // super.onPreExecute();
                target = "https://audtjddbfl.cafe24.com/MenuList.php?sungshinUniversity=" + URLEncoder.encode(sungshinUniversity, "UTF-8") + "&menuCategory=" + URLEncoder.encode(categorySpinner.getSelectedItem().toString(), "UTF-8");
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        @Override
        public String doInBackground(Void... voids) {
            try {
                URL url = new URL(target);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String temp;
                StringBuilder stringBuilder = new StringBuilder();
                while ((temp = bufferedReader.readLine()) != null) {
                    stringBuilder.append(temp + "\n");
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                return stringBuilder.toString().trim();//결과값이 여기에 리턴되면 이 값이 onPostExcute의 파라미터로 넘어감

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                orderList.clear();
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                int count = 0;
                int menuID;
                String sungshinUniversity;
                String menuCategory;
                String menuName;
                int menuPrice;
                while (count < jsonArray.length()) {
                    JSONObject object = jsonArray.getJSONObject(count);
                    menuID = object.getInt("menuID");
                    sungshinUniversity = object.getString("sungshinUniversity");
                    menuCategory = object.getString("menuCategory");
                    menuName = object.getString("menuName");
                    menuPrice = object.getInt("menuPrice");
                    Order order = new Order(menuID,sungshinUniversity,menuCategory,menuName,menuPrice);
                    orderList.add(order);
                    count++;
                }
                if(count==0) {
                    AlertDialog dialog;
                    AlertDialog.Builder builder = new AlertDialog.Builder(OrderFragment.this.getActivity());
                    dialog = builder.setMessage("조회된 메뉴가 없습니다. \n날짜를 확인하세요")
                            .setPositiveButton("확인",null)
                            .create();
                    dialog.show();
                }
                adapter.notifyDataSetChanged();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
