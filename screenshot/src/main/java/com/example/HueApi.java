package com.example;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.PUT;
import retrofit.http.Path;

public interface HueApi {

  String HUE_BRIDGE_IP = "http://192.168.1.6/";
  String HUE_USER = "GJBAeXMy6eobeZQ5wrZyoPnrR9kMxqB8zVT4YgdZ";
  String HUE_PATH = "/api/" + HUE_USER + "/";

  @PUT(HUE_PATH + "lights/{light}/state")
  Call<ResponseState> changeLight(@Path("light") int light, @Body State state);
}