package com.pizzastudio.centerpoint;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.ui.IconGenerator;
import com.mingle.entity.MenuEntity;
import com.mingle.sweetpick.RecyclerViewDelegate;
import com.mingle.sweetpick.SweetSheet;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.pizzastudio.centerpoint.db.DBHelper;
import com.pizzastudio.centerpoint.db.model.Meet;
import com.pizzastudio.centerpoint.db.model.Participant;
import com.pizzastudio.centerpoint.model.CalcCenterPointServices;
import com.pizzastudio.centerpoint.model.PointModel;
import com.pizzastudio.centerpoint.model.RecommendPlaceModel;
import com.pizzastudio.centerpoint.model.TraceModel;
import com.pizzastudio.centerpoint.model.TraceRouteModel;
import com.pizzastudio.centerpoint.oauth2.AccessToken;
import com.pizzastudio.centerpoint.oauth2.ServiceGenerator;
import com.pizzastudio.centerpoint.util.GpsTracker;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class MapViewActivity extends AppCompatActivity implements OnMapReadyCallback {

    private InterstitialAd mInterstitialAd;
    private static final String TAG = MapViewActivity.class.getCanonicalName() ;
    private View mControlsView;
    private FloatingActionButton actionButton;
    private int meetID;
    private String meetTitle;
    private DBHelper.ParticipantDB participantDB;
    private DBHelper.MeetDB meetDB;
    private List<Participant> participants = new ArrayList<>();
    private List<Marker> friendMarkerList = new ArrayList<>();
    private List<Marker> stationMarkerList = new ArrayList<>();
    private List<Marker> restaurantMarkerList = new ArrayList<>();
    public static final String EXTRA_ITEM_PARTICIPANT_ID = "com.pizzastudio.centerpoint.MapViewActivity.PARTICIPANT_ID";
    public static final String EXTRA_ITEM_CLICKED_LATITUDE = "com.pizzastudio.centerpoint.MapViewActivity.CLICKED_LATITUDE";
    public static final String EXTRA_ITEM_CLICKED_LONGITUDE = "com.pizzastudio.centerpoint.MapViewActivity.CLICKED_LONGITUDE";
    private GpsTracker gpsTracker;
    private Button calcBtn;
    // for sweetsheet
    private FrameLayout rl;
    private SweetSheet mSweetSheet;
    public static final String EXTRA_ITEM_DETAIL_URL = "com.pizzastudio.centerpoint.MapViewActivity.DETAIL_URL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-8638613247413657/8587689777");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        setContentView(R.layout.activity_map_view);
        rl = findViewById(R.id.rl);
        Bundle b = getIntent().getExtras();
        meetTitle = b.getString(MainActivity.EXTRA_ITEM_TITLE_MESSAGE);
        meetID = b.getInt(MainActivity.EXTRA_ITEM_MEET_ID);
        // initialize
        DBHelper db = DBHelper.getInstance(this);
        participantDB = db.new ParticipantDB();
        meetDB = db.new MeetDB();
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FragmentManager fragmentManager = getFragmentManager();
        MapFragment mapFragment = (MapFragment)fragmentManager
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Intent intent = getIntent();
        String title = intent.getStringExtra(MainActivity.EXTRA_ITEM_TITLE_MESSAGE);

        getSupportActionBar().setTitle(title);

        com.google.android.material.floatingactionbutton.FloatingActionButton fab = (com.google.android.material.floatingactionbutton.FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d(TAG, "sb3 button ic_action_place_light clicked ");
                Intent intent = new Intent(getApplicationContext(), FriendViewActivity.class);
                intent.putExtra(MainActivity.EXTRA_ITEM_MEET_ID, meetID);
                startActivityForResult(intent, 3000);
            }
        });

        mInterstitialAd.setAdListener(new AdListener() {
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
                // Code to be executed when the ad is displayed.
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
                // Code to be executed when the interstitial ad is closed.
            }
        });


        calcBtn = findViewById(R.id.calc_button);
        calcBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Log.d(TAG, "calcBtn clicked");
                // calc route and show route with progress dialog
                calcCenterPoint();
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }

            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.map_view_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private List<PointModel> resultPoints;

//    public static String API_OAUTH_CLIENTID  = "my_client_id";
//    public static String API_OAUTH_CLIENTSECRET  = "my_client_secret";
    private void calcCenterPoint(){
        Log.d(TAG, "calcCenterPoint begin");


        final SharedPreferences prefs = this.getSharedPreferences("BuildConfig.APPLICATION_ID", Context.MODE_PRIVATE);
        AccessToken token = new AccessToken();
        token.setAccessToken(prefs.getString("oauth.accesstoken", ""));
        token.setTokenType(prefs.getString("oauth.tokentype", ""));
        token.setClientID(ServiceGenerator.API_OAUTH_CLIENTID);
        token.setClientSecret(ServiceGenerator.API_OAUTH_CLIENTSECRET);

        CalcCenterPointServices service = ServiceGenerator.createService(CalcCenterPointServices.class, token, this);

        // call server with
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://10.0.2.2:8080/")
//                .addConverterFactory(ScalarsConverterFactory.create())
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();

//        CalcCenterPointServices service = retrofit.create(CalcCenterPointServices.class);
        List<PointModel> postData =  new ArrayList<>();
        for(Participant p: participants){
            // public PointModel(String name, String type, Double lat, Double lng)
            PointModel pm = new PointModel(p.getName(), p.getType(), p.getLatitude(), p.getLongitude());
            postData.add(pm);
        }
        Call<List<PointModel>> call=service.calcuation(postData);
        Log.d(TAG, "calcCenterPoint call " + call.toString());
        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(MapViewActivity.this);
//        progressDoalog.setMax(100);
        progressDoalog.setMessage("Loading....");
//        progressDoalog.setTitle("Loading....");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        // show it
        progressDoalog.show();

        call.enqueue(new Callback<List<PointModel>>() {
            @Override
            public void onResponse(Call<List<PointModel>> call, Response<List<PointModel>> response) {
                Log.d(TAG, "calcCenterPoint onResponse " + response);
                //response.body() have your LoginResult fields and methods  (example you have to access error then try like this response.body().getError() )
                List<PointModel> responseJson = response.body();
                Log.d(TAG, responseJson.toString());
                progressDoalog.dismiss();
                // draw result how to??
                resultPoints = responseJson;
                createRecommendCenterPoint(responseJson);
            }

            @Override
            public void onFailure(Call<List<PointModel>> call, Throwable t) {
                //for getting error in network put here Toast, so get the error on network
                Log.d(TAG, "onFailure called");
                progressDoalog.dismiss();
                Log.d(TAG, t.getMessage());
            }
        });

    }

    private void createRecommendCenterPoint(List<PointModel> result){

        Log.d(TAG, "createRecommendCenterPoint begin");
        refreshFriendMarker();
        IconGenerator iconFactory = new IconGenerator(getApplicationContext());
        for(PointModel p : result){
            List<TraceModel> trList = p.getTr();
            for(TraceModel tr: trList){
                // create recommend station marker
//                MarkerOptions markerOptions = new MarkerOptions()
//                        .position(new LatLng(tr.getLat(), tr.getLng()))
//                        .title(tr.getName())
//                        .snippet(tr.getName())
//                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

                iconFactory.setRotation(0);
                iconFactory.setContentRotation(0);
                iconFactory.setStyle(IconGenerator.STYLE_GREEN);
                Marker marker = addIcon(iconFactory, tr.getName(), new LatLng(tr.getLat(), tr.getLng()));

//                Marker marker = this.map.addMarker(markerOptions);
                String tag = "station_"+tr.getId();
                marker.setTag(tag);

                stationMarkerList.add(marker);

            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "begin onActivityResult resultCode => [" + resultCode +"]" + " requestCode => [" + requestCode + "]");
        if(resultCode == RESULT_OK){
            switch (requestCode){
                case 3000:
                    // add Marker
                    refreshFriendMarker();
                    break;
                case 4000:
                    String mode = (String)data.getStringExtra("mode");
                    Log.d(TAG, "4000 mode is [" + mode + "]");
//                    participants = participantDB.findByMeetID(meetID);
                    if(mode.equals("edit")){
                        // redraw marker
                        refreshFriendMarker();
                    }else if(mode.equals("delete")){
                        // delete marker
                        refreshFriendMarker();
                    }
            }
        }
    }

    private void refreshFriendMarker(){
        // clear all marker
        Log.d(TAG, "begin refreshFriendMarker");
        if(this.map == null)
            return;
        if(this.map != null)
            this.map.clear();
        participants = participantDB.findByMeetID(meetID);
        IconGenerator iconFactory = new IconGenerator(getApplicationContext());

        for(Participant p: participants){
            Log.d(TAG, p.getName());
            iconFactory.setRotation(0);
            iconFactory.setContentRotation(0);
            iconFactory.setStyle(IconGenerator.STYLE_BLUE);
            Marker marker = addIcon(iconFactory, p.getName(), new LatLng(p.getLatitude(), p.getLongitude()));

            String tag = "friend_" + p.getId();
            marker.setTag(tag);

            friendMarkerList.add(marker);
        }
        moveCamera();
    }

    protected void addFriendMarkerWithRouteTime(IconGenerator iconFactory, int id, String title, Double latitude, Double longitude){
        iconFactory.setRotation(0);
        iconFactory.setContentRotation(0);
        iconFactory.setStyle(IconGenerator.STYLE_BLUE);
        Marker marker = addIcon(iconFactory, title, new LatLng(latitude, longitude));

        String tag = "friend_" + id;
        marker.setTag(tag);
        friendMarkerList.add(marker);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected");
        final Intent resultIntent = new Intent();
        switch (item.getItemId()) {
            case android.R.id.home:

                resultIntent.putExtra("result",0);
                setResult(RESULT_OK,resultIntent);

                this.finish();
                return true;
            case R.id.deleteBtn:
                Log.d(TAG, "delete This Meeting");

                new AlertDialog.Builder(MapViewActivity.this)
                        .setTitle("미팅 삭제")
                        .setMessage("이 미팅을 정말 삭제하시겠습니까?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                participantDB.deleteAll(meetID);
                                Meet meet = new Meet();
                                meet.setId(meetID);
                                meetDB.delete(meet);

                                resultIntent.putExtra("result",0);
                                setResult(RESULT_OK,resultIntent);
                                finish();

                            }})
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                resultIntent.putExtra("result",0);
                                setResult(RESULT_OK,resultIntent);
                                finish();
                            }}).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    GoogleMap map;
    List<Polyline> routeLineList = new ArrayList<>();
    List<RecommendPlaceModel> menuLinkList = new ArrayList<>();

    @Override
    public void onMapReady(final GoogleMap map) {
        this.map = map;
        double latitude = -1;
        double longitude = -1;
            gpsTracker = new GpsTracker(MapViewActivity.this);
            if(gpsTracker.canGetLocation()){
                latitude = gpsTracker.getLatitude();
                longitude = gpsTracker.getLongitude();
                Log.d(TAG, "current location in GPSTracker => " + latitude + ", " + longitude);
            }
        Log.d(TAG, "current location => " + latitude + ", " + longitude);
        LatLng mapPoint = new LatLng(latitude, longitude); // 자신의 gps 좌표 위치, 없으면 목동 으로 ㅋㅋ
        map.moveCamera(CameraUpdateFactory.newLatLng(mapPoint));
        map.animateCamera(CameraUpdateFactory.zoomTo(10));
        this.map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                // add Friend marker with reverse geo coding
                final LatLng newLatLng = latLng;
                new AlertDialog.Builder(MapViewActivity.this)
                        .setTitle("친구추가")
                        .setMessage("이 지점을 친구의 위치로 추가하시겠습니까?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                Intent intent = new Intent(getApplicationContext(), FriendViewActivity.class);
                                intent.putExtra(MainActivity.EXTRA_ITEM_MEET_ID, meetID);
                                intent.putExtra(EXTRA_ITEM_CLICKED_LATITUDE, newLatLng.latitude);
                                intent.putExtra(EXTRA_ITEM_CLICKED_LONGITUDE, newLatLng.longitude);
                                startActivityForResult(intent, 4000);
                            }})
                        .setNegativeButton(android.R.string.no, null).show();

            }
        });
        this.map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                String tag = (String)marker.getTag();
                Log.d(TAG, tag + " marker clicked ");
                if(tag.contains("station_")){
                    if(resultPoints == null){
                        return false;
                    }
                    // clear line
                    for(Polyline pp : routeLineList){
                        pp.remove();
                    }
                    routeLineList.clear();
//                    for(Marker mm : friendMarkerList){
//                        mm.remove();
//                    }
//                    friendMarkerList.clear();
                    IconGenerator iconFactory = new IconGenerator(getApplicationContext());
                    for(PointModel pm : resultPoints){
                        List<TraceModel> trList = pm.getTr();

                        for(TraceModel tr: trList){

                            if(tr.getId() == Integer.parseInt(tag.split("_")[1])){
                                // create recommend route (car or subway)
                                int lineColor = Color.BLUE;
                                if(pm.getType().equals("metro")){
                                    lineColor = Color.GREEN;
                                }
                                PolylineOptions pl = new PolylineOptions()
                                        .width(15)
                                        .color(lineColor);
                                List<TraceRouteModel> trmList = tr.getTrace_list();
                                List<LatLng> pointList = new ArrayList<>();
                                long totMin = 0;
                                for(TraceRouteModel trm:trmList){
                                    if(trm.getLat() > 30 || trm.getLng() > 120){
                                        pointList.add(new LatLng(trm.getLat(),trm.getLng()));
                                        totMin += trm.getMinute();
                                    }
                                }
                                pl.addAll(pointList);
                                Log.d(TAG, pm.getName() + ":" + " of trmList -> " + trmList.toString());
                                Polyline line = map.addPolyline(pl);
                                routeLineList.add(line);

                                //change friend marker
                                int pId = -1;
                                for(Participant p : participants){
                                    if(pm.getName().equals(p.getName())){
                                        pId = p.getId();
                                    }
                                }
                                addFriendMarkerWithRouteTime(iconFactory, pId, pm.getName() + "("+(int)totMin+"분)", pm.getLat(), pm.getLng());
                                Log.d(TAG, "trace name " + pm.getName());
                                // create recommend restraunt
                                List<RecommendPlaceModel> rpmList = tr.getRecommend_place();
                                // add below menu
                                if(mSweetSheet != null) mSweetSheet.dismiss();
                                mSweetSheet = new SweetSheet(rl);
                                mSweetSheet.setDelegate(new RecyclerViewDelegate(true));
                                mSweetSheet.setOnMenuItemClickListener(new SweetSheet.OnMenuItemClickListener() {
                                    @Override
                                    public boolean onItemClick(int position, MenuEntity menuEntity1) {

                                        for(Marker mm: restaurantMarkerList){
                                            mm.remove();
                                        }
                                        restaurantMarkerList.clear();

                                        IconGenerator iconFactory = new IconGenerator(getApplicationContext());
                                        iconFactory.setRotation(0);
                                        iconFactory.setContentRotation(0);
                                        iconFactory.setStyle(IconGenerator.STYLE_ORANGE);
                                        RecommendPlaceModel rpm = menuLinkList.get(position);
                                        Marker marker1 = addIcon(iconFactory, rpm.getTitle(), new LatLng(rpm.getLat(), rpm.getLng()));
                                        String tag1 = "restaurant_" + rpm.getId();
                                        marker1.setTag(tag1);
                                        restaurantMarkerList.add(marker1);
                                        moveCamera(rpm.getLat(), rpm.getLng());
                                        return false;
                                    }
                                });


                                final ArrayList<MenuEntity>   detailList = new ArrayList<>();
                                menuLinkList.clear();
                                for(RecommendPlaceModel rpm: rpmList){
                                    MenuEntity ma = addRestaurantItem(rpm.getTitle(), rpm.getThumb_img(), rpm.getThumb_link(), rpm.getDetail() + " / " + rpm.getLocation(), rpm.getScore());
                                    if(ma.title != null){
                                        detailList.add(ma);
                                        Log.d(TAG, ma.title + " is added [" + detailList.size() +"]");
                                        menuLinkList.add(rpm);
                                    }
                                }
                                Log.d(TAG, "[" + tag + "] detailList size is " + detailList.size());
                                mSweetSheet.setMenuList(detailList);
                                mSweetSheet.show();
                            }
                        }
                    }

                } // end of if
                else if(tag.contains("friend_")){
                    for(Participant p: participants){
                        if(tag.equals("friend_"+p.getId())){
                            // edit mode call
                            editFriend(p.getId());
                        }

                    }
                } // end of if friend_
                else  if(tag.contains("restaurant_")){
                    //sweetsheet clear and create for describe restaurant information
                    for(RecommendPlaceModel rpm : menuLinkList){
                        if(tag.equals("restaurant_" + rpm.getId())){
                            showRestaurantDetail(rpm.getTitle(), rpm.getThumb_link());
                        }
                    }



                }
                return false;
            }
        });
        refreshFriendMarker();
    }

    private void showRestaurantDetail(String title, String url){
        Log.d(TAG, "showRestaurantDetail " + title + " " + url);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(title);

        WebView wv = new WebView(this);
        Log.d(TAG, String.format("restaurant url  %s will loaded", url));
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                wv.loadUrl(url);
            }
        });

        wv.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        alert.setView(wv);
        alert.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        alert.show();


    }

    private void moveCamera(){

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Marker marker : friendMarkerList) {
            builder.include(marker.getPosition());
        }
        try{
            LatLngBounds bounds = builder.build();
            int padding = 0; // offset from edges of the map in pixels
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
            Log.d(TAG, "current location in moveCamera() => " + bounds.getCenter().toString());
            this.map.moveCamera(cu);
            this.map.animateCamera(cu);
        }catch (IllegalStateException e){
            gpsTracker = new GpsTracker(MapViewActivity.this);
            if(gpsTracker.canGetLocation()){
                double latitude = gpsTracker.getLatitude();
                double  longitude = gpsTracker.getLongitude();
                Log.d(TAG, "current location in moveCamera() GPSTracker => " + latitude + ", " + longitude);
                LatLng mapPoint = new LatLng(latitude, longitude); // 자신의 gps 좌표 위치, 없으면 목동 으로 ㅋㅋ
                this.map.animateCamera(CameraUpdateFactory.newLatLngZoom(mapPoint,10));
            }
        }

    }


    private void moveCamera(double latitude, double longitude){
        gpsTracker = new GpsTracker(MapViewActivity.this);
        if(gpsTracker.canGetLocation()){
            LatLng mapPoint = new LatLng(latitude, longitude); // 자신의 gps 좌표 위치, 없으면 목동 으로 ㅋㅋ
            this.map.animateCamera(CameraUpdateFactory.newLatLngZoom(mapPoint,15));
        }
    }

    // edit friend view
    public void editFriend(int id){
        Log.d(TAG, "editFriend marker clicked ");
//                showPlaceSelectDialog();
        Intent intent = new Intent(getApplicationContext(), FriendViewActivity.class);
        intent.putExtra(MainActivity.EXTRA_ITEM_MEET_ID, meetID);
        intent.putExtra(MapViewActivity.EXTRA_ITEM_PARTICIPANT_ID, id);
        startActivityForResult(intent, 4000);
    }



    private MenuEntity addRestaurantItem(final String title, String img, String link, String address, Double score){
        final MenuEntity menuEntity = new MenuEntity();
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.ic_launcher);
        requestOptions.circleCropTransform();
        requestOptions.transforms( new RoundedCorners(300));

//        menuEntity.iconId = R.drawable.ic_action_restraunt;
//        menuEntity.title = title;

        final ImageView menuEntity1ImageView = new ImageView(getApplicationContext());
        Glide.with(getApplicationContext())
                .asBitmap()
                .apply(requestOptions)
                .load(img)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        try {
                            menuEntity1ImageView.setImageBitmap(resource);
                            menuEntity.icon = menuEntity1ImageView.getDrawable();
                        }catch(Exception e){
                            menuEntity.iconId = R.drawable.ic_action_restraunt;

                        }
                    }
                })
        ;
        menuEntity.title = title;
        menuEntity.titleColor = Color.BLACK;
        menuEntity.addressColor = Color.DKGRAY;
        menuEntity.scoreColor = Color.RED;
        menuEntity.address = address;
        menuEntity.score = score + "";
//        Log.d(TAG, title +" => "+ menuEntity.toString());
        return menuEntity;
    }


    // add Icon
    private Marker addIcon(IconGenerator iconFactory, CharSequence text, LatLng position) {
        MarkerOptions markerOptions = new MarkerOptions().
                icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(text))).
                position(position).
                anchor(iconFactory.getAnchorU(), iconFactory.getAnchorV());

        Marker marker = this.map.addMarker(markerOptions);
        return marker;
    }

}
