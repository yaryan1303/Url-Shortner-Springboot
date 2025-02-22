package com.url.shortner.dtos;

import java.time.LocalDate;

import lombok.Data;

@Data
public class ClickEventDTO {

  private LocalDate clickDate;
  private Long count;

}
