package com.uasmobprog.API;

import com.uasmobprog.Model.Data;
import com.uasmobprog.Model.Employee;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface Api {

    @GET("?nim=2301914176&nama=Farhan%20Novyan%20Hafizh")
    Call<Data> all();

    @GET("{id}/?nim=2301914176&nama=Farhan%20Novyan%20Hafizh")
    Call<Data> select(@Path("id") int id);
}
