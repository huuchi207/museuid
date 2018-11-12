package br.com.museuid.service.remote;

import java.util.List;

import br.com.museuid.dto.DeviceInfo;
import br.com.museuid.dto.Product;
import br.com.museuid.dto.SessionDeviceInfo;
import br.com.museuid.dto.sample.Item;
import br.com.museuid.service.remote.requestbody.AddDeviceRequest;
import br.com.museuid.service.remote.sample.SampleResponseDTO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
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
}