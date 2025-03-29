package com.pizzastudio.centerpoint.oauth2;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface APIClient {
//    curl localhost:8080/oauth/token -d grant_type=client_credentials -d client_id=my_client_id -d client_secret=my_client_secret -d scope=member.info.public
    @POST("oauth/token")
    @FormUrlEncoded
    Call<AccessToken> getNewAccessToken(
            @Field("client_id") String clientId,
            @Field("client_secret") String clientSecret,
            @Field("grant_type") String grantType,
            @Field("scope") String scope);

//    @FormUrlEncoded
//    @POST("/oauth/token")
//    Call<AccessToken> getRefreshAccessToken(
//            @Field("refresh_token") String refreshToken,
//            @Field("client_id") String clientId,
//            @Field("client_secret") String clientSecret,
//            @Field("redirect_uri") String redirectUri,
//            @Field("grant_type") String grantType);

}
// curl http://my_client_id:my_client_secret@localhost:8080/oauth/token -d grant_type=client_credentials -d client_id=my_client_id -d client_secret=my_client_secret -d scope=member.info.public