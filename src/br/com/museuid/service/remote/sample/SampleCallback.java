package br.com.museuid.service.remote.sample;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.util.List;

import br.com.museuid.util.BundleUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class SampleCallback<T> implements Callback<SampleResponseDTO<T>> {
  private static final Gson GSON = new Gson();

  public static final String SERVER_ERROR = "Error";
  private List<T> mBody;

  @Override
  public void onResponse(Call<SampleResponseDTO<T>> call, Response<SampleResponseDTO<T>> response) {
    // Get body of request
    mBody = null;
    String responseCode = SERVER_ERROR;
    String message = getServerMsg();
    JsonElement jsonResponse;

    if (!response.isSuccessful()) {
      onError(responseCode, message);
      return;
    }


    if (response.body() == null) {
      onError(responseCode, message);
      return;
    }

    mBody = response.body().getList();
    if (mBody == null) {
      try {
        onError(SERVER_ERROR, getServerMsg());
      } catch (IllegalStateException | NullPointerException ex) {
        ex.printStackTrace();
      }
      return;
    }


    // Request success
    onSuccess(mBody);

  }

  public List<T> getBody() {
    return mBody;
  }

  @Override
  public void onFailure(Call<SampleResponseDTO<T>> call, Throwable t) {
    try {
      onError(SERVER_ERROR, getServerMsg());
//      DialogUtils.dismissProgressDialog();
    } catch (IllegalStateException | NullPointerException ex) {
      ex.printStackTrace();
    }
  }


  private String getServerMsg() {
    return BundleUtils.getResourceBundle().getString("server.error");
  }

  public abstract void onError(String errorCode, String errorMessage);

  public abstract void onSuccess(List<T> data);

}