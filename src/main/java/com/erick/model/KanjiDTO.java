package com.erick.model;

import java.util.List;
import lombok.Data;

@Data
public class KanjiDTO {
  public String kanji;
  public List<String> on;
  public List<String> kun;
  public List<String> meanings;
  public String grade;
  public List<WordDTO> exampleWords;

  @Override
  public String toString() {
    return kanji;
  }
}
