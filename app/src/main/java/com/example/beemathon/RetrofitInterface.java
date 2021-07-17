package com.example.beemathon;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RetrofitInterface {

    //This interface will encode the parameters and the post methods we are going to use

    @POST("/login")
    Call<LoginResult> executeLogin(@Body HashMap<String, String> map);

    @POST("/signup")
    Call<Void> executeSignup (@Body HashMap<String, String> map);

    @POST("/verify")
    Call<Void> executeVerify (@Body HashMap<String, String> map);

    @POST("/payment")
    Call<Void> executePayment (@Body HashMap<String, String> map);


}
