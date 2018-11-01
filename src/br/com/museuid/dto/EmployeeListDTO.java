package br.com.museuid.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EmployeeListDTO {
  @SerializedName("employeeList")
  List<EmployeeDTO> employeeList;

  public List<EmployeeDTO> getEmployeeList() {
    return employeeList;
  }

  public void setEmployeeList(List<EmployeeDTO> employeeList) {
    this.employeeList = employeeList;
  }
}
