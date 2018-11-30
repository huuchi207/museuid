package br.com.museuid.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SecureQuestionDTO {
  @SerializedName("questions")
  @Expose
  List<String> questions;

  public List<String> getQuestions() {
    return questions;
  }

  public void setQuestions(List<String> questions) {
    this.questions = questions;
  }
}
