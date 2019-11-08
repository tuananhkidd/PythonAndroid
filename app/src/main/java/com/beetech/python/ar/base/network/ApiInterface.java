package com.beetech.python.ar.base.network;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("https://reqres.in/api/users?page=2")
    Single<BaseResponse> getData();

}
