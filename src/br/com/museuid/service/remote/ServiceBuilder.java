package br.com.museuid.service.remote;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import br.com.museuid.Constants;
import br.com.museuid.config.ConstantConfig;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceBuilder {
  private static Retrofit retrofit = null;
  private static final String BASEURL= "http://api.openweathermap.org/";
  private static APIService sApiService;
  public static synchronized APIService getApiService() {
    sApiService = getRetrofit(BASEURL).create(APIService.class);

    return sApiService;
  }

  public static Retrofit getRetrofit(String baseUrl) {
    if (retrofit==null) {
      HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
      if (ConstantConfig.DEBUG) {
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
      }
      OkHttpClient client = new OkHttpClient.Builder()
          .addInterceptor(interceptor)
          .addInterceptor(new Interceptor() {
            // User agent default
            @Override
            public Response intercept(Chain chain) throws IOException {
              // Set original User agent
              Request original = chain.request();
              Request.Builder builder = original.newBuilder();
              for (Map.Entry<String, String> header: getHeader().entrySet()){
                builder.header(header.getKey(), header.getValue() == null ? "" : header.getValue());
              }
              Request request = builder
                  .method(original.method(), original.body())
                  .build();

              return chain.proceed(request);
            }
          }).build();
      retrofit = new Retrofit.Builder()
          .baseUrl(baseUrl)
          .client(client)
          .addConverterFactory(GsonConverterFactory.create())
          .build();
    }
    return retrofit;
  }
  public static HashMap<String, String> getHeader(){
    HashMap<String, String> headers = new HashMap<>();
//    String userToken = PrefWrapper.getUserTokenOfPosition(App.getInstance());
//    if (userToken == null) {
//      userToken = "";
//    }
//    if(isLogin){
//      userToken ="";
//    }
//    headers.put(Constants.USER_AGENT, System.getProperty(Constants.HTTP_AGENT));
//    headers.put(Constants.USER_TOKEN, !Configs.FAKE ? userToken : "CgQ9LDpb0swcnFoeRE/+SBvrPfyTkfZeMPMIKiq45uNeG06TDmhSFX3vCGFjZB/n");
    headers.put(Constants.CONTENT_TYPE, Constants.APPLICATION_JSON);

//    if(isLogin){
//      UserDTO userDTO = PrefWrapper.getUser(App.getInstance());
//      headers.put(Constants.LOGIN_AREA_CODE, userDTO == null ? "" : userDTO.getAreaCode());
//      headers.put(Constants.LOGIN_DEPT_LEVEL, userDTO ==  null ? "" : userDTO.getDeptLevel());
//    }
    return headers;
  }
}
