package br.com.museuid.service.remote;

import java.util.List;

import br.com.museuid.dto.ChartData;
import br.com.museuid.dto.DeviceInfo;
import br.com.museuid.dto.Product;
import br.com.museuid.dto.SecureQuestionDTO;
import br.com.museuid.dto.SessionDeviceInfo;
import br.com.museuid.dto.SessionUserInfo;
import br.com.museuid.dto.UploadImageDTO;
import br.com.museuid.dto.UserDTO;
import br.com.museuid.dto.sample.Item;
import br.com.museuid.model.data.OrderDetail;
import br.com.museuid.service.remote.requestbody.AddDeviceRequest;
import br.com.museuid.service.remote.requestbody.ChangePasswordRequest;
import br.com.museuid.service.remote.requestbody.LoginRequest;
import br.com.museuid.service.remote.requestbody.SecureQuestionRequest;
import br.com.museuid.service.remote.requestbody.StatisticRequest;
import br.com.museuid.service.remote.requestbody.StockImportingRequest;
import br.com.museuid.service.remote.sample.SampleResponseDTO;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface APIService {
  //Sample
  @GET("data/2.5/box/city")
  Call<SampleResponseDTO<Item>> getSample(@Query("bbox") String bbox, @Query("appid") String appid);

  @PUT("clients/client-admin")
  Call<ResponseDTO<DeviceInfo>> addDevice(@Body AddDeviceRequest addDeviceRequest);

  @GET("clients/auto-client")
  Call<ResponseDTO<SessionDeviceInfo>> checkDevice(@Query("macaddress") String macAddress);

  @GET("clients/clients")
  Call<ResponseDTO<String>> getClients();

  @GET("products/products")
  Call<ResponseDTO<List<Product>>> getProductList();

  @POST("users/login")
  Call<ResponseDTO<SessionUserInfo>> login(@Body LoginRequest loginRequest);

  @PUT("products/product")
  Call<ResponseDTO<Product>> addProduct(@Body Product product);

  @POST("products/product")
  Call<ResponseDTO<Product>> updateProduct(@Body Product product);

  @DELETE("products/product")
  Call<ResponseDTO<Object>> deleteProduct(@Query("productid") String productId);

  @PUT("queues/queue")
  Call<ResponseDTO<Object>> putOrder(@Body OrderDetail putQueueRequest);

  @GET("users/logout")
  Call<ResponseDTO<Object>> logOut();

  @GET("users/user")
  Call<ResponseDTO<UserDTO>> getCurrentUserInfo(@Query("userid") String userId);

  @POST("users/user")
  Call<ResponseDTO<UserDTO>> changeUserInfo(@Body UserDTO userDTO);

  @POST("users/change-password")
  Call<ResponseDTO<Object>> changePassword(@Body ChangePasswordRequest request);

  @GET("users/users")
  Call<ResponseDTO<List<UserDTO>>> getListUser();

  @DELETE("users/user")
  Call<ResponseDTO<Object>> deleteUser(@Query("userid") String userId);

  @PUT("users/user")
  Call<ResponseDTO<UserDTO>> addUser(@Body UserDTO userDTO);

  @POST("users/reset-password")
  Call<ResponseDTO<UserDTO>> resetPasswordForCustomer(@Body UserDTO userDTO);

  @POST("clients/client")
  Call<ResponseDTO<Object>> updateDeviceInfo(@Body DeviceInfo product);

  @DELETE("clients/client")
  Call<ResponseDTO<Object>> deleteDevice(@Query("clientid") String clientId);

  @GET("clients/clients")
  Call<ResponseDTO<List<DeviceInfo>>> getListDevice();

  @POST("queues/queue")
  Call<ResponseDTO<Object>> updateQueue(@Body OrderDetail orderDetail);

  @POST("statistics/days-period")
  Call<ResponseDTO<ChartData>> getDayStatisticPeriod(@Body StatisticRequest statisticRequest);

  @POST("statistics/months-period")
  Call<ResponseDTO<ChartData>> getMonthStatisticPeriod(@Body StatisticRequest statisticRequest);

  @POST("statistics/years-period")
  Call<ResponseDTO<ChartData>> getYearStatisticPeriod(@Body StatisticRequest statisticRequest);

  @GET("queues/queues")
  Call<ResponseDTO<List<OrderDetail>>> getOrderByStatus(@Query("filter") String statusFilter, @Query("handlerid") String handlerid);

  @POST("statistics/days")
  Call<ResponseDTO<ChartData>> getDayStatistic(@Body StatisticRequest statisticRequest);

  @POST("statistics/months")
  Call<ResponseDTO<ChartData>> getMonthStatistic(@Body StatisticRequest statisticRequest);

  @POST("statistics/years")
  Call<ResponseDTO<ChartData>> getYearStatistic(@Body StatisticRequest statisticRequest);

  @POST("statistics/line/revenue")
  Call<ResponseDTO<ChartData>> getProductRevenueStatistic(@Body StatisticRequest statisticRequest);

  @POST("statistics/line/sales")
  Call<ResponseDTO<ChartData>> getProductSalesStatistic(@Body StatisticRequest statisticRequest);

  @POST("pendingproducts/request")
  Call<ResponseDTO<Object>> updateImportingStockRequest(@Body StockImportingRequest stockImportingRequest);

  @PUT("pendingproducts/request")
  Call<ResponseDTO<Object>> createImportingStockRequest(@Body StockImportingRequest stockImportingRequest);

  @DELETE("pendingproducts/request")
  Call<ResponseDTO<Object>> deleteImportingStockRequest(@Query("pendingproductid") String pendingproductid);

  @GET("pendingproducts/requests")
  Call<ResponseDTO<List<StockImportingRequest>>> getImportingStockRequestList(@Query("filter") String filter, @Query("requesterid") String requesterid);

  @POST("pendingproducts/request/decision")
  Call<ResponseDTO<Object>> handleImportingRequest(@Body StockImportingRequest request);

  @GET("users/secure-question")
  Call<ResponseDTO<SecureQuestionDTO>> getListSecureQuestion();

  @POST("users/update-secure")
  Call<ResponseDTO<Object>> updateSecureQuestion(@Body SecureQuestionRequest secureQuestionRequest);


  @POST("users/forgot-password")
  Call<ResponseDTO<UserDTO>> forgotPass(@Body SecureQuestionRequest secureQuestionRequest);

  @Multipart
  @POST("images/image")
  Call<ResponseDTO<UploadImageDTO>> uploadImage(@Part MultipartBody.Part image);
}