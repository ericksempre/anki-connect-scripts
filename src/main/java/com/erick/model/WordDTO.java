package com.erick.model;

import java.util.LinkedHashSet;
import java.util.Set;
import lombok.Data;

@Data
public class WordDTO {
  public String word;
  public String reading;
  public int frequency;
  public LinkedHashSet<String> meanings = new LinkedHashSet<>();

  @Override
  public String toString() {
    return word;
  }
}
