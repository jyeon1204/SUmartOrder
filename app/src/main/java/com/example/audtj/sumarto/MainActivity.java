package com.example.audtj.sumarto;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView noticeListView;
    private NoticeListAdapter adapter;
    private List<Notice> noticeList;
    public static String userID;
    private AlertDialog dialog;
    ViewFlipper v_fllipper;
    private Button button1;
    private TextView txtResult;
    private TextView txtMent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        MobileAds.initialize(this, "ca-app-pub-2798096674083236~6389015506");

        ImageView introSR = (ImageView) findViewById(R.id.introSR);
        GlideDrawableImageViewTarget gifImage = new GlideDrawableImageViewTarget(introSR);
        Glide.with(this).load(R.drawable.introsr1).into(gifImage);

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        button1 = (Button)findViewById(R.id.button1);
        txtResult = (TextView)findViewById(R.id.txtResult);
        txtMent = (TextView)findViewById(R.id.txtMent);

        final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);





        int images[] = {
                R.drawable.slide1,
                R.drawable.slide2,
                R.drawable.slide3,
                R.drawable.slide4,
                R.drawable.slide5
        };

        v_fllipper = findViewById(R.id.image_slide);

        for(int image : images) {
            fllipperImages(image);
        }



        userID = getIntent().getStringExtra("userID");

        noticeListView = (ListView) findViewById(R.id.noticeListView);
        noticeList = new ArrayList<Notice>();

        adapter = new NoticeListAdapter(getApplicationContext(), noticeList);
        noticeListView.setAdapter(adapter);

        TextView idText = (TextView)findViewById(R.id.userID1);
        Intent intent = getIntent();
        idText.setText(userID);

        final Button orderButton = (Button)findViewById(R.id.orderButton);
        final Button twoButton = (Button)findViewById(R.id.twoButton);
        final Button threeButton = (Button)findViewById(R.id.threeButton);
        final LinearLayout notice = (LinearLayout)findViewById(R.id.notice);
        final FrameLayout frameLayout = (FrameLayout)findViewById(R.id.image_slide) ;
        final ImageView imageView = (ImageView)findViewById(R.id.introSR);
        final Button buyBT = (Button)findViewById(R.id.buyButton);
        final LinearLayout userInfo = (LinearLayout)findViewById(R.id.userInfo);
        final Button button1 = (Button)findViewById(R.id.button1) ;
        final AdView adView = (AdView)findViewById(R.id.adView);
        final TextView txtMent = (TextView)findViewById(R.id.txtMent);



        notice.setVisibility(View.GONE);
        buyBT.setVisibility(View.GONE);
        userInfo.setVisibility(View.GONE);

        notice.setVisibility(View.GONE);

        final TextView information = (TextView)findViewById(R.id.information);
        information.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,Pop.class));
            }
        });


        information.setVisibility(View.GONE);

        buyBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show();
            }

            Response.Listener<String> responseListener = new Response.Listener<String>(){

                @Override
                public void onResponse(String response) {
                    try{
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.getBoolean("success");
                        if(success){//사용할 수 있는 아이디라면
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            dialog = builder.setMessage("Register Your ID")
                                    .setPositiveButton("OK", null)
                                    .create();
                            dialog.show();
                            finish();//액티비티를 종료시킴(회원등록 창을 닫음)
                        }else{//사용할 수 없는 아이디라면
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            dialog = builder.setMessage("Register fail")
                                    .setNegativeButton("OK", null)
                                    .create();
                            dialog.show();
                        }

                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
                }
            };//Response.Listener 완료

            //Volley 라이브러리를 이용해서 실제 서버와 통신을 구현하는 부분
            BuyRequest buyRequest = new BuyRequest(userID, responseListener);




        });



        orderButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                notice.setVisibility(View.GONE);
                frameLayout.setVisibility(View.GONE);
                imageView.setVisibility(View.GONE);
                buyBT.setVisibility(View.GONE);
                userInfo.setVisibility(View.GONE);
                information.setVisibility(View.GONE);
                button1.setVisibility(View.GONE);
                txtMent.setVisibility(View.GONE);

                orderButton.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                twoButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                threeButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

                FragmentManager fragmentManager = getSupportFragmentManager();

                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment, new OrderFragment());
                fragmentTransaction.commit();
            }
        });

        twoButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                notice.setVisibility(View.GONE);
                imageView.setVisibility(View.GONE);
                frameLayout.setVisibility(View.GONE);
                userInfo.setVisibility(View.GONE);
                information.setVisibility(View.GONE);
                buyBT.setVisibility(View.VISIBLE);
                button1.setVisibility(View.GONE);
                adView.setVisibility(View.GONE);
                txtMent.setVisibility(View.GONE);


                orderButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                twoButton.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                threeButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment, new twoFragment());
                fragmentTransaction.commit();
            }
        });

        threeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                notice.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.GONE);
                frameLayout.setVisibility(View.GONE);
                buyBT.setVisibility(View.GONE);
                userInfo.setVisibility(View.VISIBLE);
                information.setVisibility(View.VISIBLE);
                button1.setVisibility(View.GONE);
                orderButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                twoButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                threeButton.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment,new threeFragment());
                fragmentTransaction.commit();
                txtMent.setVisibility(View.GONE);


            }


        });
                button1.setOnClickListener(new View.OnClickListener() {
                                               @Override
                                               public void onClick(View v) {
                                                   if ( Build.VERSION.SDK_INT >= 23 &&
                                                           ContextCompat.checkSelfPermission( getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
                                                       ActivityCompat.requestPermissions( MainActivity.this, new String[] {  android.Manifest.permission.ACCESS_FINE_LOCATION  },
                                                               0 );
                                                   }
                                                   else{
                                                       Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                                       String provider = location.getProvider();
                                                       double longitude = location.getLongitude();
                                                       double latitude = location.getLatitude();
                                                       double altitude = location.getAltitude();

                                                       lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                                                               1000,
                                                               1,
                                                               gpsLocationListener);
                                                       lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                                                               1000,
                                                               1,
                                                               gpsLocationListener);

                                                       if( !(latitude>=37.58 && latitude<=37.60 && longitude >=127.02 && longitude<=127.03))
                                                       {
                                                           orderButton.setVisibility(View.INVISIBLE);
                                                           twoButton.setVisibility(View.INVISIBLE);
                                                           txtMent.setText("학교 근처가 아니라면, 주문이 불가능해요");

                                                       }
                                                       else
                                                       {
                                                           txtMent.setText("학교 근처군요! 주문 가능해요");
                                                       }
                                                   }
                                               }
                                           }

                );


        new BackgroundTask().execute();
    }

    final LocationListener gpsLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {

            String provider = location.getProvider();
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();
            double altitude = location.getAltitude();

        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onProviderDisabled(String provider) {
        }
    };


    void show()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("주문 확인");
        builder.setMessage("주문 접수하시겠습니까?");
        builder.setPositiveButton("예",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(),"주문 접수되었습니다.",Toast.LENGTH_LONG).show();
                    }
                });
        builder.setNegativeButton("아니오",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(),"주문이 접수되지 않았습니다.",Toast.LENGTH_LONG).show();
                    }
                });
        builder.show();
    }

    public void fllipperImages(int image) {
        ImageView imageView = new ImageView(this);
        imageView.setBackgroundResource(image);

        v_fllipper.addView(imageView);      // 이미지 추가
        v_fllipper.setFlipInterval(3000);       // 자동 이미지 슬라이드 딜레이시간(1000 당 1초)
        v_fllipper.setAutoStart(true);          // 자동 시작 유무 설정

        // animation
        v_fllipper.setInAnimation(this,android.R.anim.slide_in_left);
        v_fllipper.setOutAnimation(this,android.R.anim.slide_out_right);
    }

    @Override
    public boolean onTouchEvent(MotionEvent touchevent){
        switch (v_fllipper.getDisplayedChild()){
            case 0:
                Intent intent = new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("http://www2.sungshin.ac.kr/main_kor/11108/subview.do?enc=Zm5jdDF8QEB8JTJGYmJzJTJGbWFpbl9rb3IlMkYzMTgyJTJGODEzMTAlMkZhcnRjbFZpZXcuZG8lM0ZwYWdlJTNEMSUyNnNyY2hDb2x1bW4lM0QlMjZzcmNoV3JkJTNEJTI2YmJzQ2xTZXElM0QlMjZiYnNPcGVuV3JkU2VxJTNEJTI2cmdzQmduZGVTdHIlM0QlMjZyZ3NFbmRkZVN0ciUzRCUyNmlzVmlld01pbmUlM0RmYWxzZSUyNnBhc3N3b3JkJTNEJTI2"));
                startActivity(intent);
                break;

            case 1:
                Intent intent1 = new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://www2.sungshin.ac.kr/main_kor/11107/subview.do?enc=Zm5jdDF8QEB8JTJGYmJzJTJGbWFpbl9rb3IlMkYzMTgxJTJGODEzMjIlMkZhcnRjbFZpZXcuZG8lM0ZwYWdlJTNEMSUyNnNyY2hDb2x1bW4lM0QlMjZzcmNoV3JkJTNEJTI2YmJzQ2xTZXElM0QlMjZiYnNPcGVuV3JkU2VxJTNEJTI2cmdzQmduZGVTdHIlM0QlMjZyZ3NFbmRkZVN0ciUzRCUyNmlzVmlld01pbmUlM0RmYWxzZSUyNnBhc3N3b3JkJTNEJTI2"));
                startActivity(intent1);
                break;

            case 2:
                Intent intent2 = new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("http://www2.sungshin.ac.kr/main_kor/11107/subview.do?enc=Zm5jdDF8QEB8JTJGYmJzJTJGbWFpbl9rb3IlMkYzMTgxJTJGODExNTglMkZhcnRjbFZpZXcuZG8lM0Y%3D"));
                startActivity(intent2);
                break;

            case 3:
                Intent intent3 = new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("http://www2.sungshin.ac.kr/main_kor/11107/subview.do?enc=Zm5jdDF8QEB8JTJGYmJzJTJGbWFpbl9rb3IlMkYzMTgxJTJGODEzMTMlMkZhcnRjbFZpZXcuZG8lM0ZwYWdlJTNEMSUyNnNyY2hDb2x1bW4lM0QlMjZzcmNoV3JkJTNEJTI2YmJzQ2xTZXElM0QlMjZiYnNPcGVuV3JkU2VxJTNEJTI2cmdzQmduZGVTdHIlM0QlMjZyZ3NFbmRkZVN0ciUzRCUyNmlzVmlld01pbmUlM0RmYWxzZSUyNnBhc3N3b3JkJTNEJTI2"));
                startActivity(intent3);
                break;

            case 4:
                Intent intent4 = new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("http://www2.sungshin.ac.kr/main_kor/11108/subview.do?enc=Zm5jdDF8QEB8JTJGYmJzJTJGbWFpbl9rb3IlMkYzMTgyJTJGODEzNTklMkZhcnRjbFZpZXcuZG8lM0ZwYWdlJTNEMSUyNnNyY2hDb2x1bW4lM0QlMjZzcmNoV3JkJTNEJTI2YmJzQ2xTZXElM0QlMjZiYnNPcGVuV3JkU2VxJTNEJTI2cmdzQmduZGVTdHIlM0QlMjZyZ3NFbmRkZVN0ciUzRCUyNmlzVmlld01pbmUlM0RmYWxzZSUyNnBhc3N3b3JkJTNEJTI2"));
                startActivity(intent4);
                break;

            default:break;
        }
        return true;
    }

    class BackgroundTask extends AsyncTask<Void, Void, String> {
        String target;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            target = "https://audtjddbfl.cafe24.com/NoticeList.php";
        }

        //실제 데이터를 가져오는 부분임
        @Override
        protected String doInBackground(Void... voids) {
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
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        //여기서는 가져온 데이터를 Notice객체에 넣은뒤 리스트뷰 출력을 위한 List객체에 넣어주는 부분
        @Override
        protected void onPostExecute(String result) { //가쿠리는 private로함
            super.onPostExecute(result);
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                int count = 0;
                String noticeContent, noticeName, noticeDate;

                //json타입의 값을 하나씩 빼서 Notice 객체에 저장후 리스트에 추가하는 부분
                while (count < jsonArray.length()) {
                    JSONObject object = jsonArray.getJSONObject(count);
                    noticeContent = object.getString("noticeContent");
                    noticeName = object.getString("noticeName");
                    noticeDate = object.getString("noticeDate");
                    Notice notice = new Notice(noticeContent, noticeName, noticeDate);
                    noticeList.add(notice);
                    adapter.notifyDataSetChanged();
                    count++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

private long pressedTime;

@Override
public void onBackPressed() {
    if(pressedTime == 0) {
        Toast.makeText(MainActivity.this,"한번 더 누르면 종료됩니다.",Toast.LENGTH_LONG).show();
        pressedTime = System.currentTimeMillis();
    }
    else {
        int seconds = (int) (System.currentTimeMillis() - pressedTime);

        if(seconds > 1500) {
            Toast.makeText(MainActivity.this,"한번 더 누르면 종료됩니다.",Toast.LENGTH_LONG).show();
            pressedTime = 0;
        }
        else {
            super.onBackPressed();
        }
    }
}


}