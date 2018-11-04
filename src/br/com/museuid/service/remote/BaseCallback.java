package br.com.museuid.service.remote;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import br.com.museuid.util.BundleUtils;
import javafx.application.Platform;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class BaseCallback<T> implements Callback<ResponseDTO<T>> {
  private static final Gson GSON = new Gson();

  public static final String SERVER_ERROR = "Error";
  private T mBody;

  @Override
  public void onResponse(Call<ResponseDTO<T>> call, Response<ResponseDTO<T>> response) {
    // Get body of request
      Platform.runLater(new Runnable() {
          @Override
          public void run() {
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

              mBody = response.body().getContent();
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
      });

  }

  public T getBody() {
    return mBody;
  }

  @Override
  public void onFailure(Call<ResponseDTO<T>> call, Throwable t) {
      Platform.runLater(new Runnable() {
          @Override
          public void run() {
              try {
                  onError(SERVER_ERROR, getServerMsg());
              } catch (IllegalStateException | NullPointerException ex) {
                  ex.printStackTrace();
              }
          }
      });
  }


  private String getServerMsg() {
    return BundleUtils.getResourceBundle().getString("server_error");
  }

  public abstract void onError(String errorCode, String errorMessage);

  public abstract void onSuccess(T data);

}