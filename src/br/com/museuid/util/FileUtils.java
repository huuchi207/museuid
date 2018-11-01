package br.com.museuid.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import br.com.museuid.dto.EmployeeListDTO;
import br.com.museuid.dto.EmployeeDTO;
import br.com.museuid.dto.Product;
import br.com.museuid.dto.ProductListDTO;
import br.com.museuid.service.remote.ResponseDTO;

public class FileUtils {
  public static List<EmployeeDTO> getFakeEmployeeList(){
    try{
      JsonReader reader = readFile("src/br/com/museuid/json_fake/employee.json");
      if (reader != null){
        ResponseDTO<EmployeeListDTO> responseDTO = new Gson()
            .fromJson(reader, new TypeToken<ResponseDTO<EmployeeListDTO>>() {
            }.getType());
        if (responseDTO!= null){
          EmployeeListDTO employeeDTO =responseDTO.getContent();
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
            JsonReader reader = readFile("src/br/com/museuid/json_fake/product.json");
            if (reader != null){
                ResponseDTO<ProductListDTO> responseDTO = new Gson()
                    .fromJson(reader, new TypeToken<ResponseDTO<ProductListDTO>>() {
                    }.getType());
                if (responseDTO!= null){
                    ProductListDTO productListDTO =responseDTO.getContent();
                    return productListDTO.getProducts();
                }
            }
        }catch (Exception e){
            Messenger.erro(e.getMessage());
        }

        return new ArrayList<>();
    }

  public static JsonReader readFile(String fileDir){
    JsonReader reader = null;
    try {
      reader = new JsonReader(new FileReader(fileDir));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    return reader;
  }
}
