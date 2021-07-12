package com.example.audtj.sumarto;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link twoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link twoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class twoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public twoFragment() {
        // Required empty public constructor
    }

    public static twoFragment newInstance(String param1, String param2) {
        twoFragment fragment = new twoFragment();
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

    private ListView orderListView;
    private StatisticsOrderListAdapter adapter;
    private List<Order> orderList;
    public static int totalPrice = 0;

    public static TextView price;

    private ArrayAdapter rankAdapter;
    private Spinner rankSpinner;

    private ListView rankListView;
    private RankListAdapter rankListAdapter;
    private List<Order> rankList;

    @Override
    public void onActivityCreated(Bundle b){
        super.onActivityCreated(b);
        orderListView = (ListView)getView().findViewById(R.id.orderListView);
        orderList = new ArrayList<Order>();
        adapter = new StatisticsOrderListAdapter(getContext().getApplicationContext(),orderList,this);
        orderListView.setAdapter(adapter);
        new BackgroundTask().execute();
        totalPrice = 0;
        price = (TextView) getView().findViewById(R.id.totalPrice);

        rankSpinner=(Spinner)getView().findViewById(R.id.rankSpinner);
        rankAdapter=ArrayAdapter.createFromResource(getActivity(),R.array.rank, R.layout.spinner_item);
        rankSpinner.setAdapter(rankAdapter);
        rankSpinner.setPopupBackgroundResource(R.color.colorPrimary);

        //21강
        rankListView = (ListView) getView().findViewById(R.id.rankListView);
        rankList = new ArrayList<Order>();
        rankListAdapter = new RankListAdapter(getContext().getApplicationContext(), rankList, this);
        rankListView.setAdapter(rankListAdapter);
        new ByyEntire().execute();

        rankSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(rankSpinner.getSelectedItem().equals("전체"))
                {
                    rankList.clear();
                    new ByyEntire().execute();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    class ByyEntire extends AsyncTask<Void, Void, String> {
        String target;
        @Override
        public void onPreExecute() {
            try {
                // super.onPreExecute();
                target = "https://audtjddbfl.cafe24.com/ByyEntire.php";
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
                String temp;//결과 값을 여기에 저장함
                StringBuilder stringBuilder = new StringBuilder();
                //버퍼생성후 한줄씩 가져옴
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
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                int count = 0;
                int menuID;
                String menuCategory;
                String menuName;
                int menuPrice;

                while (count < jsonArray.length()) {
                    JSONObject object = jsonArray.getJSONObject(count);
                    menuID = object.getInt("menuID");
                    menuCategory = object.getString("menuCategory");
                    menuName = object.getString("menuName");
                    menuPrice = object.getInt("menuPrice");

                    rankList.add(new Order(menuID, menuCategory, menuName, menuPrice));
                    count++;
                }
                rankListAdapter.notifyDataSetChanged();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class BackgroundTask extends AsyncTask<Void, Void, String> {
        String target;
        @Override
        public void onPreExecute() {
            try {
                target = "https://audtjddbfl.cafe24.com/StatisticsMenuList.php?userID=" + URLEncoder.encode(MainActivity.userID,"UTF-8");
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
                //버퍼생성후 한줄씩 가져옴
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
        public void onPostExecute(String result) { //가쿠리는 private로함
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                int count = 0;
                int menuID;
                String menuCategory;
                String menuName;
                while (count < jsonArray.length()) {
                    JSONObject object = jsonArray.getJSONObject(count);
                    menuID = object.getInt("menuID");
                    menuCategory = object.getString("menuCategory");
                    menuName = object.getString("menuName");
                    int menuPrice = object.getInt("menuPrice");
                    totalPrice += menuPrice;
                    orderList.add(new Order(menuID, menuCategory,menuName,menuPrice));
                    count++;
                }
                adapter.notifyDataSetChanged();
                price.setText(totalPrice +" 원");
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_two,container, false);
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
}
