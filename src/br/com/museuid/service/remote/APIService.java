package br.com.museuid.service.remote;

import java.util.List;

import br.com.museuid.dto.DeviceInfo;
import br.com.museuid.dto.Product;
import br.com.museuid.dto.SessionDeviceInfo;
import br.com.museuid.dto.SessionUserInfo;
import br.com.museuid.dto.UserDTO;
import br.com.museuid.dto.sample.Item;
import br.com.museuid.service.remote.requestbody.AddDeviceRequest;
import br.com.museuid.service.remote.requestbody.ChangePasswordRequest;
import br.com.museuid.service.remote.requestbody.LoginRequest;
import br.com.museuid.service.remote.requestbody.PutQueueRequest;
import br.com.museuid.service.remote.sample.SampleResponseDTO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface APIService {
    //Sample
    @GET("data/2.5/box/city")
    Call<SampleResponseDTO<Item>> getSample(@Query("bbox") String bbox, @Query("appid") String appid);

    @PUT("client")
    Call<ResponseDTO<DeviceInfo>> addDevice(@Body AddDeviceRequest addDeviceRequest);

    @GET("auto-client")
    Call<ResponseDTO<SessionDeviceInfo>> checkDevice(@Query("macaddress") String macAddress);

    @GET("clients")
    Call<ResponseDTO<String>> getClients();

    @GET("products")
    Call<ResponseDTO<List<Product>>> getProductList();

    @POST("login")
    Call<ResponseDTO<SessionUserInfo>> login(@Body LoginRequest loginRequest);

    @PUT("product")
    Call<ResponseDTO<Product>> addProduct(@Body Product product);

    @POST("product")
    Call<ResponseDTO<Product>> updateProduct(@Body Product product);

    @DELETE("product")
    Call<ResponseDTO<Object>> deleteProduct(@Body String productId);

    @PUT("queue")
    Call<ResponseDTO<Object>> putOrder(@Body PutQueueRequest putQueueRequest);

    @GET("logout")
    Call<ResponseDTO<Object>> logOut();

    @GET("user")
    Call<ResponseDTO<UserDTO>> getCurrentUserInfo(@Query("userid") String userId);

    @POST("user")
    Call<ResponseDTO<UserDTO>> changeUserInfo(@Body UserDTO userDTO);

    @POST("change-password")
    Call<ResponseDTO<Object>> changePassword(@Body ChangePasswordRequest request);

    @GET("users")
    Call<ResponseDTO<List<UserDTO>>> getListUser();

    @DELETE("user")
    Call<ResponseDTO<Object>> deleteUser(@Body String userId);

    @PUT("user")
    Call<ResponseDTO<UserDTO>> addUser(@Body UserDTO userDTO);

    @POST("reset-password")
    Call<ResponseDTO<Object>> resetPasswordForCustomer(@Body String userid);
}