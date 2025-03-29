package com.pizzastudio.centerpoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.pizzastudio.centerpoint.services.PlaceCrawler;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

@Configuration
public class JsonPlaceholderConfig {
    @Autowired
    private Interceptor jsonPlaceholderInterceptor;

    @Bean("jsonPlaceholderOkHttpClient")
    public OkHttpClient jsonPlaceholderOkHttpClient() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        return new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(jsonPlaceholderInterceptor)
                .build();
    }

    @Bean("jsonPlaceholderObjectMapper")
    public ObjectMapper jsonPlaceholderObjectMapper() {

        return Jackson2ObjectMapperBuilder.json()

                .featuresToDisable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                .modules(new JavaTimeModule())
                .build();
    }

    @Bean("jsonPlaceholderRetrofit")
    public Retrofit jsonPlaceholderRetrofit(

            @Qualifier("jsonPlaceholderObjectMapper") ObjectMapper jsonPlaceholderObjectMapper,
            @Qualifier("jsonPlaceholderOkHttpClient") OkHttpClient jsonPlaceholderOkHttpClient
    ) {

        return new Retrofit.Builder()
                .baseUrl("https://nominatim.openstreetmap.org")
                .addConverterFactory(JacksonConverterFactory.create(jsonPlaceholderObjectMapper))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(jsonPlaceholderOkHttpClient)
                .build();
    }

    @Bean("placeCrawler")
    public PlaceCrawler jsonPlaceholderService(

            @Qualifier("jsonPlaceholderRetrofit") Retrofit jsonPlaceHolderRetrofit
    ) {
        return jsonPlaceHolderRetrofit.create(PlaceCrawler.class);
    }
}
