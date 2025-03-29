package com.pizzastudio.centerpoint.oauth2;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Base64;
import android.util.Log;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Authenticator;
import okhttp3.Credentials;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ServiceGenerator {

    public static final String API_BASE_URL = "https://api.pizzastudio.app/api-server/";
    public static final String OAUTH_BASE_URL = "https://api.pizzastudio.app/oauth2-server/";
//    public static final String API_BASE_URL = "http://10.0.2.2:8080/api-server/";
//    public static final String OAUTH_BASE_URL = "http://10.0.2.2:8080/oauth2-server/";
//    public static final String API_BASE_URL = "http://10.0.2.2:8081/";
//    public static final String OAUTH_BASE_URL = "http://10.0.2.2:8080/";

    public static final String API_OAUTH_CLIENTID = "my_client_id";
    public static final String API_OAUTH_CLIENTSECRET = "my_client_secret";

    private static Long READTIMEOUT = 60L;
    private static final String TAG = ServiceGenerator.class.getCanonicalName();
    private static OkHttpClient.Builder httpClient;
    private static Retrofit.Builder builder;
    private static Context mContext;
    private static AccessToken mToken;

    public static <S> S createService(Class<S> serviceClass) {
        httpClient = new OkHttpClient.Builder();
        builder = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());

        OkHttpClient client = httpClient.build();
        Retrofit retrofit = builder.client(client).build();
        return retrofit.create(serviceClass);
    }

    public static <S> S createAuthService(Class<S> serviceClass) {
        httpClient = new OkHttpClient.Builder();
        Log.d(TAG, "OAUTH_BASE_URL : " + OAUTH_BASE_URL);
        builder = new Retrofit.Builder()
                .baseUrl(OAUTH_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        httpClient.addInterceptor(new Interceptor(){

            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                HttpUrl url = original.url();
                if(mToken != null) Log.d(TAG, "mToken : " + mToken.toString());
                String authorizationValue = " Basic " + Base64.encodeToString((API_OAUTH_CLIENTID + ":" + API_OAUTH_CLIENTSECRET).getBytes(), Base64.NO_WRAP);

                Log.d(TAG, "base64Secret: " + authorizationValue);
//                if(mToken != null ){
                    url = url.newBuilder()
                            .username(API_OAUTH_CLIENTID)
                            .password(API_OAUTH_CLIENTSECRET)
                            .build();
                    Log.d(TAG, "before request url : " + url.toString());
                    Request newRequest = original.newBuilder()
                            .addHeader("Authorization", authorizationValue)
                            .url(url)
                            .build();

                    return chain.proceed(newRequest);
//                }// end of mToken
//                return chain.proceed(original);

            }
        });
        // curl -d "client_id=my_client_id&client_secret=my_client_secret&grant_type=client_credentials&scope=member.info.public" -X POST http://my_client_id:my_client_secret@localhost:8080/oauth/token

        OkHttpClient client = httpClient.addInterceptor(logging).build();


        Retrofit retrofit = builder.client(client).build();
        return retrofit.create(serviceClass);
    }

    public static <S> S createService(Class<S> serviceClass, AccessToken accessToken, Context c) {
        httpClient = new OkHttpClient.Builder()
                    .readTimeout(READTIMEOUT, TimeUnit.SECONDS);
        builder = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());
        Log.d(TAG, "createService : 1 access token : " + accessToken.toString());
        if(accessToken != null) {

            Log.d(TAG, "createService token is not null");
            mContext = c;
            mToken = accessToken;
            final AccessToken token = accessToken;
            httpClient.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();
                    Log.d(TAG, "createService httpClient intercepter " + token.toString());
                    Request.Builder requestBuilder = original.newBuilder()
                            .header("Accept", "application/json")
                            .header("Content-type", "application/json")
                            .header("Authorization",
                                    token.getTokenType() + " " + token.getAccessToken())
                            .method(original.method(), original.body());

                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                }
            });

            httpClient.authenticator(new Authenticator() {
                @Override
                public Request authenticate(Route route, Response response) throws IOException {
                    if(responseCount(response) >= 2) {
                        return null;
                    }
                    Log.d(TAG, "createService authenticator in  " + mToken.toString());
                    // We need a new client, since we don't want to make another call using our client with access token
                    APIClient tokenClient = createAuthService(APIClient.class);

                    Call<AccessToken> call = tokenClient.getNewAccessToken(
                            mToken.getClientID(), mToken.getClientSecret(), "client_credentials",
                            "member.info.public");
                    try {
                        retrofit2.Response<AccessToken> tokenResponse = call.execute();
                        Log.d(TAG, "createService tokenResponse : " + tokenResponse);
                        if(tokenResponse.code() == 200) {
                            AccessToken newToken = tokenResponse.body();
                            if(mToken != null){
                                newToken.setClientID(mToken.getClientID());
                                newToken.setClientSecret(mToken.getClientSecret());
                            }
                            Log.d(TAG, "createService : 2 retrofit get access token : " + newToken.toString());
                            mToken = newToken;
                            SharedPreferences prefs = mContext.getSharedPreferences("BuildConfig.APPLICATION_ID", Context.MODE_PRIVATE);
                            prefs.edit().putBoolean("oauth.loggedin", true).apply();
                            prefs.edit().putString("oauth.accesstoken", newToken.getAccessToken()).apply();
                            prefs.edit().putString("oauth.tokentype", newToken.getTokenType()).apply();
                            String credential = Credentials.basic(mToken.getClientID(), mToken.getClientSecret());

                            return response.request().newBuilder()

                                    .header("Authorization", newToken.getTokenType() + " " + newToken.getAccessToken())
                                    .build();
                        } else {
                            return null;
                        }
                    } catch(IOException e) {
                        return null;
                    }
                }
            });
        } // end of accessToken is null

        OkHttpClient client = httpClient.build();
        Retrofit retrofit = builder.client(client).build();
        return retrofit.create(serviceClass);
    }

    private static int responseCount(Response response) {
        int result = 1;
        while ((response = response.priorResponse()) != null) {
            result++;
        }
        return result;
    }
}
