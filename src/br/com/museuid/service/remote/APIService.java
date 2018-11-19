package br.com.museuid.service.remote;

import br.com.museuid.dto.*;
import br.com.museuid.dto.sample.Item;
import br.com.museuid.model.data.OrderDetail;
import br.com.museuid.service.remote.requestbody.*;
import br.com.museuid.service.remote.sample.SampleResponseDTO;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

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
    Call<ResponseDTO<Object>> deleteProduct(@Query("productid") String productId);

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
    Call<ResponseDTO<Object>> deleteUser(@Query("userid") String userId);

    @PUT("user")
    Call<ResponseDTO<UserDTO>> addUser(@Body UserDTO userDTO);

    @POST("reset-password")
    Call<ResponseDTO<UserDTO>> resetPasswordForCustomer(@Body UserDTO userDTO);

    @POST("client")
    Call<ResponseDTO<Object>> updateDeviceInfo(@Body DeviceInfo product);

    @DELETE("client")
    Call<ResponseDTO<Object>> deleteDevice(@Query("clientid") String clientId);

    @GET("clients")
    Call<ResponseDTO<List<DeviceInfo>>> getListDevice();

    @POST("queue")
    Call<ResponseDTO<Object>> updateQueue(@Body OrderDetail orderDetail);

    @POST("days-period")
    Call<ResponseDTO<ChartData>> getDayStatistic(@Body StatisticRequest statisticRequest);

    @POST("months-period")
    Call<ResponseDTO<ChartData>> getMonthStatistic(@Body StatisticRequest statisticRequest);

    @POST("years-period")
    Call<ResponseDTO<ChartData>> getYearStatistic(@Body StatisticRequest statisticRequest);

    @GET("queues")
    Call<ResponseDTO<List<OrderDetail>>> getOrderByStatus(@Query("filter") String statusFilter);
}