package br.com.museuid.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import br.com.museuid.model.Employee;

public class EmployeeDTO {
  @SerializedName("employeeList")
  List<Employee> employeeList;

  public List<Employee> getEmployeeList() {
    return employeeList;
  }

  public void setEmployeeList(List<Employee> employeeList) {
    this.employeeList = employeeList;
  }
}
