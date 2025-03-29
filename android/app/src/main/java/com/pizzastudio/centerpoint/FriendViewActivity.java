package com.pizzastudio.centerpoint;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.pizzastudio.centerpoint.db.DBHelper;
import com.pizzastudio.centerpoint.db.model.Participant;
import com.pizzastudio.centerpoint.db.model.Place;
import com.pizzastudio.centerpoint.model.SearchPlaceModel;
import com.pizzastudio.centerpoint.model.SearchPlaceServices;
import com.pizzastudio.centerpoint.oauth2.AccessToken;
import com.pizzastudio.centerpoint.oauth2.ServiceGenerator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ir.mirrajabi.searchdialog.core.BaseFilter;
import ir.mirrajabi.searchdialog.core.BaseSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.SearchResultListener;
import ir.mirrajabi.searchdialog.core.Searchable;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

public class FriendViewActivity extends AppCompatActivity {

    private static String TAG = FriendViewActivity.class.getCanonicalName();
    private Retrofit mRetrofit;
    private OkHttpClient mOkHttpClient;
    private Place selectedPlace;
    private DBHelper.PlaceDB placeDB;
    private DBHelper.ParticipantDB participantDB;
    private SearchPlaceServices mService;

    private TextView addressView;
    private EditText nameView;
    private Button locationSearchBtn;
    private Button confirmBtn;
    private Button deleteBtn;
    private Spinner transportType;

    private int meetID;
    private int participantID = -1;
    private Double pLatitude;
    private Double pLongitude;
    private Participant curUser;
//    private Place curPlace;
    private boolean editMode = false;
    private String selectedTransportType = "car";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Bundle b = getIntent().getExtras();
        meetID = b.getInt(MainActivity.EXTRA_ITEM_MEET_ID);
        try{
            participantID = b.getInt(MapViewActivity.EXTRA_ITEM_PARTICIPANT_ID, -1);
        }catch(Exception e){
            e.printStackTrace();
            participantID = -1;
        }
        Log.d(TAG, "participantID = " + participantID);
        if(participantID != -1){
            editMode = true;
        }
        try{
            pLatitude = b.getDouble(MapViewActivity.EXTRA_ITEM_CLICKED_LATITUDE, -1);
            pLongitude = b.getDouble(MapViewActivity.EXTRA_ITEM_CLICKED_LONGITUDE, -1);
        }catch(Exception e){
            e.printStackTrace();
        }
        getSupportActionBar().setTitle("친구추가");

        final SharedPreferences prefs = this.getSharedPreferences("BuildConfig.APPLICATION_ID", Context.MODE_PRIVATE);
        AccessToken token = new AccessToken();
        token.setAccessToken(prefs.getString("oauth.accesstoken", ""));
        token.setTokenType(prefs.getString("oauth.tokentype", ""));
        token.setClientID(ServiceGenerator.API_OAUTH_CLIENTID);
        token.setClientSecret(ServiceGenerator.API_OAUTH_CLIENTSECRET);
        Log.d(TAG, "accesstoken : " + token.toString());

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        mService = ServiceGenerator.createService(SearchPlaceServices.class, token, this);



        DBHelper db = DBHelper.getInstance(this);
        placeDB = db.new PlaceDB();
        participantDB = db.new ParticipantDB();


        addressView = findViewById(R.id.address);
        nameView = findViewById(R.id.name);
        locationSearchBtn = findViewById(R.id.location_search);
        confirmBtn = findViewById(R.id.confirm);
        deleteBtn = findViewById(R.id.deleteBtn);
        transportType = findViewById(R.id.transport_type);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.transport_type_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        transportType.setAdapter(adapter);
        transportType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectTransportType = (String)transportType.getItemAtPosition(position);
                if(selectTransportType.equals("지하철")){
                    selectedTransportType = "metro";
                }else{
                    selectedTransportType = "car";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        if(editMode){
            curUser = participantDB.get((long)participantID);
            Log.d(TAG, "participantID is " + participantID + " placeID is " + curUser.toString());
            selectedPlace = placeDB.get((long)curUser.getPlace_id());
            deleteBtn.setVisibility(View.VISIBLE);
            deleteBtn.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    int friend_id = curUser.getId();
                    deleteFriend();
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("result",friend_id);
                    resultIntent.putExtra("mode","delete");
                    setResult(RESULT_OK,resultIntent);
                    finish();
                }
            });

            setEditMode();
        }

        locationSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "ic_action_beer clicked ");
//                showPlaceSelectDialog();
                provideSimpleDialogWithApiCalls();
            }

        });
        confirmBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                int friend_id = saveFirend();
                Intent resultIntent = new Intent();
                Log.d(TAG, "saveFriend : " + friend_id);
                resultIntent.putExtra("result",friend_id);
                resultIntent.putExtra("mode", "edit");
                setResult(RESULT_OK,resultIntent);
                finish();
            }
        });

        if(pLatitude != -1 && pLongitude != -1 ){
            try {
                List<SearchPlaceModel> userList = mService.reverseGeoCoding(pLatitude, pLongitude).execute().body();
                SearchPlaceModel user = userList.get(0);
                Log.d(TAG, "mService.reverseGeoCoding SearchPlaceModel : " + user);
                Place p = new Place();
                p.setDisplay_name(user.getmName());
                p.setPlace_id(user.getmPlaceid());
                p.setSearch_string(pLatitude+","+pLongitude);
                p.setLatitude(user.getmLat());
                p.setLongitude(user.getmLon());
                p.setAddress(user.getmAddress().toString());
                Log.d(TAG, "reverse geocoded selected adress : " + p.getAddress());
                p.setTimestamp(Integer.toString((int) (System.currentTimeMillis() / 1000L)));
                saveSelectPlace(p);

            } catch (Exception e) {
                Log.d(TAG, "mService.reverseGeoCoding eror  " + e.getMessage());
                Place p = new Place();
                p.setDisplay_name(pLatitude+","+pLongitude);
                p.setPlace_id(pLatitude+","+pLongitude);
                p.setSearch_string(pLatitude+","+pLongitude);
                p.setLatitude(pLatitude);
                p.setLongitude(pLongitude);
                p.setAddress(pLatitude+","+pLongitude);
                Log.d(TAG, "reverse geocoded selected adress : " + p.getAddress());
                p.setTimestamp(Integer.toString((int) (System.currentTimeMillis() / 1000L)));
                saveSelectPlace(p);

            }
        }
    }

    private void setEditMode(){
        nameView.setText(curUser.getName());
        addressView.setText(curUser.getAddress());
        if(curUser.getType().equals("metro")){
            transportType.setSelection(1);
        }else{
            transportType.setSelection(0);
        }
        selectedPlace = placeDB.get(curUser.getPlace_id());
        Log.d(TAG, "selectedPlace => " + selectedPlace.toString());

    }


    public void saveSelectPlace(Place p){
        // save logic
        selectedPlace = p;
        Log.d(TAG, "saveSelectPlace : " + p.getDisplay_name());
        addressView.setText(p.getDisplay_name());
    }

    public int saveFirend(){
        int place_id = 0;
        place_id = placeDB.existPlace(selectedPlace.getPlace_id());
        Log.d(TAG, "saveFriend place_id : " + place_id);
        if(place_id == -1){
            place_id = (int)placeDB.insert(selectedPlace.getDisplay_name(), selectedPlace.getPlace_id(),
                    selectedPlace.getSearch_string(), selectedPlace.getLatitude(), selectedPlace.getLongitude(),
                    selectedPlace.getDisplay_name(), String.valueOf(System.currentTimeMillis()/1000L));

        }
        Log.d(TAG, "saveFriend place_id : " + place_id + ":" + selectedPlace.getPlace_id());

        long id = -1;
        if(!editMode){
            id = participantDB.insert(nameView.getText().toString(), selectedTransportType, selectedPlace.getDisplay_name(), place_id,
                    selectedPlace.getLatitude(), selectedPlace.getLongitude(), selectedPlace.getDisplay_name(), meetID);

        }else{
            // update logic
            Participant upitem = new Participant();
            upitem.setId(participantID);
            upitem.setName(nameView.getText().toString());
            upitem.setAddress(selectedPlace.getDisplay_name());
            upitem.setMeet_id(meetID);
            upitem.setLatitude(selectedPlace.getLatitude());
            upitem.setLongitude(selectedPlace.getLongitude());
            upitem.setPlace_id(place_id);
            upitem.setPlace(selectedPlace.getDisplay_name());
            upitem.setType(selectedTransportType);
            upitem.setTimestamp(String.valueOf(System.currentTimeMillis()/1000));
            participantDB.update(upitem);
            id = participantID;
            Log.d(TAG, "saveFriend update  : " + upitem.toString());
        }
        Log.d(TAG, "saveFriend participantDB["+editMode+"]id : " + id);

        return (int)id;
    }

    public void deleteFriend(){
        Log.d(TAG, "deleteFriend[" + curUser.getName() + "]");
        participantDB.delete(curUser);
    }

    private ArrayList<SearchPlaceModel> users;

    void provideSimpleDialogWithApiCalls() {
        final PlaceSearchDialogCompat<SearchPlaceModel> searchDialog =
                new PlaceSearchDialogCompat(FriendViewActivity.this, "장소검색",
                        "장소를 검색하세요..", null, new ArrayList(),
                        new SearchResultListener<Searchable>() {
                            @Override
                            public void onSelected(
                                    BaseSearchDialogCompat dialog,
                                    Searchable item, int position
                            ) {
                                Toast.makeText(FriendViewActivity.this, item.getTitle(),
                                        Toast.LENGTH_SHORT
                                ).show();
                                Log.d(TAG, users.toString());
                                Place p = new Place();
                                p.setDisplay_name(users.get(position).getmName());
                                p.setPlace_id(users.get(position).getmPlaceid());
                                p.setSearch_string(item.getTitle());
                                p.setLatitude(users.get(position).getmLat());
                                p.setLongitude(users.get(position).getmLon());
                                p.setAddress(users.get(position).getmAddress().toString());
                                Log.d(TAG, "selected adress : " + p.getAddress());
                                p.setTimestamp(Integer.toString((int) (System.currentTimeMillis() / 1000L)));
                                saveSelectPlace(p);
                                dialog.dismiss();
                            }
                        }
                );
        BaseFilter apiFilter = new BaseFilter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                if(charSequence.length() < 2){
                    return null;
                }
                doBeforeFiltering();
                FilterResults results = new FilterResults();
                results.values = new ArrayList<SearchPlaceModel>();
                results.count = 0;
                try {
                    // place db select first
                    users = mService
                            .getFakePlaceBasedOnASearchTag(charSequence.toString())
                            .execute()
                            .body();
                    Log.d(TAG, "users : " + users);
                    if (users != null) {
                        results.values = users;
                        results.count = users.size();
                    }
                    Log.d(TAG, "result count is " + String.valueOf(results.count));
                    return results;
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                if (filterResults != null) {
                    ArrayList<SearchPlaceModel> filtered = (ArrayList<SearchPlaceModel>) filterResults.values;
                    if (filtered != null) {
                        searchDialog.getFilterResultListener().onFilter(filtered);
                    }
                    doAfterFiltering();
                }
            }
        };
        searchDialog.setFilter(apiFilter);
        searchDialog.show();
    }
}
