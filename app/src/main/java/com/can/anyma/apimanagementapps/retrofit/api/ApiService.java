package com.can.anyma.apimanagementapps.retrofit.api;


import com.can.anyma.apimanagementapps.model.Sorun;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * Created by anyma on 16.05.2017.
 */

public interface ApiService {
    /*
    Retrofit get annotation with our URL
    And our method that will return us the List of ContactList
    */

    @POST("sorunlar")
    Call<Sorun> createSorun(@Body Sorun sorun);

    @GET("sorunlar")
    Call<List<Sorun>> getSorunlar();

    @GET("sorunlar/{id}")
    Call<Sorun> getSorun(@Path("id") int id);

    @DELETE("sorunlar/{id}")
    Call<Sorun> deleteSorun(@Path("id") int id);

    @Multipart
    @POST("upload")
    Call<ResponseBody> uploadPhoto(
            @Part MultipartBody.Part photo);

}