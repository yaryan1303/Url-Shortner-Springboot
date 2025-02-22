package com.url.shortner.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.url.shortner.Repository.CilckEventRepository;
import com.url.shortner.Repository.urlMappingRepository;
import com.url.shortner.dtos.ClickEventDTO;
import com.url.shortner.dtos.UrlMappingDTO;
import com.url.shortner.model.ClickEvent;
import com.url.shortner.model.UrlMapping;
import com.url.shortner.model.User;

@Service
public class UrlMappingService {

  @Autowired
  private urlMappingRepository UrlMappingRepository;

  @Autowired
  private CilckEventRepository clickEventRepository;

  public UrlMappingDTO createShortUrl(String originalUrl, User user) {
    String shortUrl = generateShortUrl();
    UrlMapping urlMapping = new UrlMapping();
    urlMapping.setOriginalUrl(originalUrl);
    urlMapping.setShortUrl(shortUrl);
    urlMapping.setUser(user);
    urlMapping.setCreatedDate(LocalDateTime.now());
    UrlMapping savedUrlMapping = UrlMappingRepository.save(urlMapping);

    return mapToDTO(savedUrlMapping);

  }

  private UrlMappingDTO mapToDTO(UrlMapping urlMapping) {
    UrlMappingDTO dto = new UrlMappingDTO();
    dto.setId(urlMapping.getId());
    dto.setOriginalUrl(urlMapping.getOriginalUrl());
    dto.setShortUrl(urlMapping.getShortUrl());
    dto.setCreateDate(urlMapping.getCreatedDate());
    dto.setUsername(urlMapping.getUser().getUsername());
    dto.setClickCount(urlMapping.getClickCount());
    return dto;
  }

  private String generateShortUrl() {
    Random random = new Random();
    String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    StringBuilder shortUrl = new StringBuilder();
    for (int i = 0; i < 8; i++) {
      int index = random.nextInt(characters.length());
      shortUrl.append(characters.charAt(index));
    }
    return shortUrl.toString();

  }

  public List<UrlMappingDTO> getUrlsByUser(User user) {
    return UrlMappingRepository.findByUser(user).stream().map(this::mapToDTO).toList();
  }

  public List<ClickEventDTO> getClickEventsByDate(String shortUrl, LocalDateTime start, LocalDateTime end) {
    UrlMapping urlMapping = UrlMappingRepository.findByShortUrl(shortUrl);
    if (urlMapping != null) {
      return clickEventRepository.findByUrlMappingAndClickDateBetween(urlMapping, start, end).stream()
          .collect(Collectors.groupingBy(click -> click.getClickDate().toLocalDate(), Collectors.counting()))
          .entrySet().stream()
          .map(entry -> {
            ClickEventDTO clickEventDTO = new ClickEventDTO();
            clickEventDTO.setClickDate(entry.getKey());
            clickEventDTO.setCount(entry.getValue());
            return clickEventDTO;
          })
          .collect(Collectors.toList());
    }
    return null;

  }

  public Map<LocalDate, Long> getTotalClicksByUserAndDate(User user, LocalDate start, LocalDate end) {
    List<UrlMapping> urlMappings = UrlMappingRepository.findByUser(user);
    List<ClickEvent> clickEvents = clickEventRepository.findByUrlMappingInAndClickDateBetween(urlMappings,
        start.atStartOfDay(), end.plusDays(1).atStartOfDay());
    return clickEvents.stream()
        .collect(Collectors.groupingBy(click -> click.getClickDate().toLocalDate(), Collectors.counting()));

  }

  public UrlMapping getOriginalUrl(String shortUrl) {
    UrlMapping urlMapping = UrlMappingRepository.findByShortUrl(shortUrl);
    if (urlMapping != null) {
      urlMapping.setClickCount(urlMapping.getClickCount() + 1);
      UrlMappingRepository.save(urlMapping);

      // Record Click Event
      ClickEvent clickEvent = new ClickEvent();
      clickEvent.setClickDate(LocalDateTime.now());
      clickEvent.setUrlMapping(urlMapping);
      clickEventRepository.save(clickEvent);
    }

    return urlMapping;
  }

}
