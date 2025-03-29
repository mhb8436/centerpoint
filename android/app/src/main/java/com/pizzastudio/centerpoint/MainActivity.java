package com.pizzastudio.centerpoint;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.pizzastudio.centerpoint.db.DBHelper;
import com.pizzastudio.centerpoint.db.model.Meet;
import com.pizzastudio.centerpoint.oauth2.APIClient;
import com.pizzastudio.centerpoint.oauth2.AccessToken;
import com.pizzastudio.centerpoint.oauth2.ServiceGenerator;
import com.pizzastudio.centerpoint.util.MyDividerItemDecoration;
import com.pizzastudio.centerpoint.util.RecyclerTouchListener;
import com.yarolegovich.lovelydialog.LovelyTextInputDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {



    private MeetAdapter mAdapter;
    private List<Meet> meetList = new ArrayList<>();
    private CoordinatorLayout coordinatorLayout;
    private RecyclerView recyclerView;
    private DBHelper.MeetDB meetDb;
    private DBHelper.ParticipantDB participantDB;
    private DBHelper.CenterPointDB centerPointDB;
    private DBHelper.RestaurantDB restaurantDB;
    private DBHelper.RouteDB routeDB;
    private DBHelper.PlaceDB placeDB;
    public static final String EXTRA_ITEM_TITLE_MESSAGE = "com.pizzastudio.centerpoint.MainActivity.ITEM_TITLE";
    public static final String EXTRA_ITEM_MEET_ID = "com.pizzastudio.centerpoint.MainActivity.MEET_ID";
    private AdView mAdView;

    static final String TAG = MainActivity.class.getCanonicalName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        coordinatorLayout = findViewById(R.id.coordinator_layout);
        recyclerView = findViewById(R.id.recycler_view);


        // DB Helper example
        DBHelper db = DBHelper.getInstance(this);
        meetDb = db.new MeetDB();
        participantDB = db.new ParticipantDB();
        centerPointDB = db.new CenterPointDB();
        restaurantDB = db.new RestaurantDB();
        routeDB = db.new RouteDB();
        placeDB = db.new PlaceDB();
//        makeTestData();
//        makeConfigDataSet();
        updateData();
        meetList.addAll(meetDb.listForView());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputDialogShow();

            }
        });


        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });

        mAdapter = new MeetAdapter(this, meetList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this,
                recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                Log.d(TAG, "onclick event ");
                Intent intent = new Intent(getApplicationContext(), MapViewActivity.class);
                intent.putExtra(EXTRA_ITEM_TITLE_MESSAGE, meetList.get(position).getName());
                intent.putExtra(EXTRA_ITEM_MEET_ID, meetList.get(position).getId());

                startActivityForResult(intent, 1000);
            }

            @Override
            public void onLongClick(View view, int position) {
                showActionsDialog(position);
            }
        }));

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "begin onActivityResult resultCode => [" + resultCode +"]" + " requestCode => [" + requestCode + "]");
        if(resultCode == RESULT_OK){
            switch (requestCode){
                case 1000:
                    Log.d(TAG, "requestCode : " + requestCode);
                    meetList.clear();
                    meetList.addAll(meetDb.listForView());
                    mAdapter.notifyDataSetChanged();
            }
        }
    }


    private void showActionsDialog(final int position) {
        CharSequence colors[] = new CharSequence[]{"Edit", "Delete"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose option");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
//                    showNoteDialog(true, notesList.get(position), position);
                } else {
                    deleteMeet(position);
                }
            }
        });
        builder.show();
    }

    private void deleteMeet(int position) {
        meetDb.delete(meetList.get(position));

        // removing the note from the list
        meetList.remove(position);
        mAdapter.notifyItemRemoved(position);
    }

    protected  void inputDialogShow(){
        new LovelyTextInputDialog(this, R.style.EditTextTintTheme)
                .setTopColorRes(R.color.darkDeepOrange)
                .setTitle("미팅 생성")
                .setMessage("미팅이름을 입력하세요!")
                .setIcon(R.drawable.ic_assignment_white_36dp)
                .setInputFilter(R.string.text_input_error_message, new LovelyTextInputDialog.TextFilter() {
                    @Override
                    public boolean check(String text) {
                        return text.matches("\\w+");
                    }
                })
                .setConfirmButton(android.R.string.ok, new LovelyTextInputDialog.OnTextInputConfirmListener() {
                    @Override
                    public void onTextInputConfirmed(String text) {
                        Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
                        Date date = new Date();
                        String modifiedDate= new SimpleDateFormat("yyyy-MM-dd").format(date);
                        long id = meetDb.insert(text, modifiedDate, "", String.valueOf(System.currentTimeMillis()/1000));
                        meetList.clear();
                        meetList.addAll(meetDb.listForView());
                        Log.d(TAG, "meetList size is " + meetList.size() + " new id is " + id);
                        mAdapter.notifyDataSetChanged();
                    }
                })
                .show();

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume begin");
        final SharedPreferences prefs = this.getSharedPreferences(
                "BuildConfig.APPLICATION_ID", Context.MODE_PRIVATE);

        APIClient client = ServiceGenerator.createAuthService(APIClient.class);
        Call<AccessToken> call = client.getNewAccessToken(ServiceGenerator.API_OAUTH_CLIENTID,
                ServiceGenerator.API_OAUTH_CLIENTSECRET, "client_credentials",
                "member.info.public");
        call.enqueue(new Callback<AccessToken>() {
            @Override
            public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                int statusCode = response.code();
                Log.d(TAG, "onResume AccessToken status : " + statusCode);
                if(statusCode == 200) {
                    AccessToken token = response.body();
                    Log.d(TAG, "onResume access token new : " + token.toString());
                    prefs.edit().putBoolean("oauth.loggedin", true).apply();
                    prefs.edit().putString("oauth.accesstoken", token.getAccessToken()).apply();
                    prefs.edit().putString("oauth.tokentype", token.getTokenType()).apply();

                    // TODO Show the user they are logged in
                } else {
                    // TODO Handle errors on a failed response
                }
            }

            @Override
            public void onFailure(Call<AccessToken> call, Throwable t) {
                // TODO Handle failure
            }
        });
    }


    public void updateData(){
        // update participant set place_id = place;

        participantDB.updateForDev();
    }
//
    public void makeTestData(){
        meetDb.deleteAll();

        meetDb.insert("테스트 모임 1", "2019-01-01", "한식당","2019-01-01");
        meetDb.insert("테스트 모임 2", "2019-01-01", "중식당","2019-01-02");
        meetDb.insert("테스트 모임 3", "2019-01-01", "마부엌","2019-01-03");
        meetDb.insert("테스트 모임 4", "2019-01-01", "당구장","2019-01-04");
        Meet m = meetDb.get(1);
        Log.d(TAG, "meet name is " + m.getName());
//        participantDB.deleteAll();
//        centerPointDB.deleteAll();

        for(int i=1;i<10;i++){
            // String name, String type, String place, int place_id, double latitude, double longitude, String address, int meet_id
            participantDB.insert("이지훈","metro","목성유치원", 1,37.5125228, 126.860630796949, "목성유치원, 목동동로2길, 신정7동, 신정동, 양천구, 서울특별시, 08089, 대한민국", i);
            participantDB.insert("박성래","metro", "삼성전자인력개발원", 2,37.2978904, 127.1924941, "삼성인력개발원(창조관), 에버랜드 글로벌페어~아메리칸 어드벤처, 가실리, 처인구, 용인시, 경기도, 17040, 대한민국", i);
            participantDB.insert("오승일","metro","행신역 ", 3,37.6126061, 126.8347539, "행신역, 소원로, 행신동, 덕양구, 고양시, 경기도, 10522, 대한민국", i);
            centerPointDB.insert("양재역",37.4826373, 127.035864, i, "2018-11-11 13:00:00");
        }
    }




}
