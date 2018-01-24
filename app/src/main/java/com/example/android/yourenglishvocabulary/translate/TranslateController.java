package com.example.android.yourenglishvocabulary.translate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by joseluis on 24/01/2018.
 */

public class TranslateController {

    private static final String TRANSLATE_YANDEX_API = "https://translate.yandex.net";

    private static final String API_KEY = "";

    public final TranslateService translateService;

    public TranslateController() {
        Gson gson = new GsonBuilder().setLenient().create();

        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.interceptors().add(chain -> {
            Request request = chain.request();
            HttpUrl url = request.url().newBuilder()
                    .addQueryParameter("key", API_KEY)
                    .build();
            Request newRequest = chain.request().newBuilder().url(url).build();
            return chain.proceed(newRequest);
        });

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(TRANSLATE_YANDEX_API)
                .client(client.build())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        translateService = retrofit.create(TranslateService.class);
    }
}
