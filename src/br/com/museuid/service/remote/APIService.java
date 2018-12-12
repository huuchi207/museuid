package br.com.museuid.service.remote;

import br.com.museuid.dto.DeviceInfo;
import br.com.museuid.dto.Product;
import br.com.museuid.dto.SessionDeviceInfo;
import br.com.museuid.dto.sample.Item;
import br.com.museuid.model.data.OrderDetail;
import br.com.museuid.service.remote.requestbody.AddDeviceRequest;
import br.com.museuid.service.remote.requestbody.PutQueueRequest;
import br.com.museuid.service.remote.requestbody.StringBody;
import br.com.museuid.service.remote.sample.SampleResponseDTO;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface APIService {
    //Sample
    @GET("data/2.5/box/city")
    Call<SampleResponseDTO<Item>> getSample(@Query("bbox") String bbox, @Query("appid") String appid);

    @PUT("clients/client")
    Call<ResponseDTO<DeviceInfo>> addDevice(@Body AddDeviceRequest addDeviceRequest);

    @GET("clients/auto-client")
    Call<ResponseDTO<SessionDeviceInfo>> checkDevice(@Query("macaddress") String macAddress);

    @GET("clients/clients")
    Call<ResponseDTO<String>> getClients();

    @GET("products/products")
    Call<ResponseDTO<List<Product>>> getProductList();

    @PUT("queues/queue")
    Call<ResponseDTO<Object>> putOrder(@Body PutQueueRequest putQueueRequest);

    @DELETE("queues/queue")
    Call<ResponseDTO<Object>> cancelOrder(@Query("queueid") String queue);

    @GET("queues/queues")
    Call<ResponseDTO<List<OrderDetail>>> getMyOrderList();

    @POST("queues/queue")
    Call<ResponseDTO<Object>> updateOrder(@Body OrderDetail orderDetail);

    @POST("machines/active")
    Call<ResponseDTO<Object>> activeServer(@Body StringBody key);
}