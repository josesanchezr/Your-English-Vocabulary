package com.example.android.yourenglishvocabulary.translate;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by joseluis on 24/01/2018.
 */

public interface TranslateService {
    @GET("/api/v1.5/tr.json/translate?lang=en-es")
    Call<TranslateData> translateWord(@Query("text") String text);
}
