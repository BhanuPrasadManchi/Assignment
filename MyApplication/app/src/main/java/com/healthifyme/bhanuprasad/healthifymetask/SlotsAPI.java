package com.healthifyme.bhanuprasad.healthifymetask;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.ResponseBody;

import org.json.JSONObject;

import retrofit.Callback;
import retrofit.http.GET;

/**
 * Created by bhanuprasad on 3/14/2016.
 */
public interface SlotsAPI {
    String url = "/api/v1/booking/slots/all?username=alok%40x.coz&api_key=a4aeb4e27f27b5786828f6cdf00d8d2cb44fe6d7&vc=276&expert_username=neha%40healthifyme.com&format=json" ;

    /*Retrofit get annotation with our URL
      And our method that will return a Json Object
   */
    @GET(url)
    retrofit.Call<JSONObject> getSlots();
}
