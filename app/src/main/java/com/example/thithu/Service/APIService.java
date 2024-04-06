package com.example.thithu.Service;

import com.example.thithu.Model.Response;
import com.example.thithu.Model.XeMay;

import java.util.ArrayList;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIService {
    String ipv4 = "192.168.1.118";
    String DOMAIN = "http://"+ ipv4 +":3000/api/";

    @GET("get-list-xemay")
    Call<Response<ArrayList<XeMay>>> getXeMay();
    @DELETE("delete-xemay-by-id/{id}")
    Call<Response<Void>> deleteXeMay(@Path("id") String id);
    @Multipart
    @POST("add-xemay-with-file-image")
    Call<Response<XeMay>> addXeMayWithFileImage(@PartMap Map<String, RequestBody> requestBodyMap,
                                                @Part MultipartBody.Part image);
    @GET("search-xemay-by-name")
    Call<Response<ArrayList<XeMay>>> searchXeMayByName(@Query("name") String name);
}
