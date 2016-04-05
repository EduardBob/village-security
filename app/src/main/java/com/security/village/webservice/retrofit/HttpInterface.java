package com.security.village.webservice.retrofit;

import java.util.HashMap;

import retrofit.Callback;
import retrofit.http.DELETE;
import retrofit.http.PATCH;
import retrofit.http.FieldMap;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.QueryMap;

/**
 * Created by s_inquisitor on 6/22/15.
 */
public interface HttpInterface {

    @GET("/{url}")
    void getAuth(@Path(value = "url", encode = false) String url,
                 @Header("Authorization") String header,
                 @QueryMap HashMap<String, String> query,
                 Callback<String> callback);

    @FormUrlEncoded
    @POST("/{url}")
    void post(@Path(value = "url", encode = false) String url,
              @Header("Authorization") String header,
              @FieldMap HashMap<String, String> map,
              Callback<String> callback);

    @FormUrlEncoded
    @PATCH("/{url}")
    void patch(@Path(value = "url", encode = false) String url,
               @Header("Authorization") String header,
               @FieldMap HashMap<String, String> map,
               Callback<String> callback);
}