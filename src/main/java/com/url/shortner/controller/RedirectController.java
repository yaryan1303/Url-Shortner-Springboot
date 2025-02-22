package com.url.shortner.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.url.shortner.model.UrlMapping;
import com.url.shortner.service.UrlMappingService;

@RestController
public class RedirectController {

  @Autowired
  private UrlMappingService urlMappingService;

  @RequestMapping("/{shortUrl}")
  public ResponseEntity<Void> redirect(@PathVariable String shortUrl) {
    UrlMapping urlMapping = urlMappingService.getOrignalUrl(shortUrl);
    if (urlMapping != null) {
      HttpHeaders headers = new HttpHeaders();
      headers.add("Location", urlMapping.getOriginalUrl());
      return ResponseEntity.status(302).headers(headers).build();
    }
    return ResponseEntity.notFound().build();



  }

}
