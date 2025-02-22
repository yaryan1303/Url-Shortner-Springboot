package com.url.shortner.dtos;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class UrlMappingDTO {
  private Long id;

  private String originalUrl;
  private String shortUrl;

  private int clickCount;

  private LocalDateTime createDate;
  private String username;

}
