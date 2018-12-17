package br.com.museuid.util;

import br.com.museuid.app.App;
import br.com.museuid.dto.*;
import br.com.museuid.service.remote.ResponseDTO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class FakeDataUtils {
  public static List<EmployeeDTO> getFakeEmployeeList(){
    try{
      JsonReader reader = readFile("employee.json");
      if (reader != null){
        ResponseDTO<EmployeeListDTO> responseDTO = new Gson()
            .fromJson(reader, new TypeToken<ResponseDTO<EmployeeListDTO>>() {
            }.getType());
        if (responseDTO!= null){
          EmployeeListDTO employeeDTO =responseDTO.getResult();
          return employeeDTO.getEmployeeList();
        }
      }
    }catch (Exception e){
      Messenger.erro(e.getMessage());
    }

    return new ArrayList<>();
  }

    public static List<Product> getFakeProductList(){
        try{
            JsonReader reader = readFile("product.json");
            if (reader != null){
                ResponseDTO<List<Product>> responseDTO = new Gson()
                    .fromJson(reader, new TypeToken<ResponseDTO<List<Product>>>() {
                    }.getType());
                if (responseDTO!= null){
                    return responseDTO.getResult();
                }
            }
        }catch (Exception e){
            Messenger.erro(e.getMessage());
        }

        return new ArrayList<>();
    }

    public static ChartData getFakeGroupBarChart(){
        try{
            JsonReader reader = readFile("groupbarchart.json");
            if (reader != null){
                ResponseDTO<ChartData> responseDTO = new Gson()
                    .fromJson(reader, new TypeToken<ResponseDTO<ChartData>>() {
                    }.getType());
                if (responseDTO!= null){
                    ChartData chartData = responseDTO.getResult();
                    for (GroupChartPoint groupChartPoint : chartData.getGroupChartPoints()){
                        for (ChartPoint chartPoint : groupChartPoint.getChartPoints()){
                            chartPoint.setValue(1000000+ (Math.random() * ((100000 - 1000) + 1)));
                        }
                    }
                    return chartData;
                }
            }
        }catch (Exception e){
            Messenger.erro(e.getMessage());
        }
        return null;
    }
  public static JsonReader readFile(String fileDir){
    JsonReader reader = null;
    try {
      reader = new JsonReader(new InputStreamReader(
          App.class.getClass().getResourceAsStream("/br/com/museuid/json_fake/"+
              fileDir), "UTF-8"));
    } catch (Exception e) {
      e.printStackTrace();
    }
    return reader;
  }
}
