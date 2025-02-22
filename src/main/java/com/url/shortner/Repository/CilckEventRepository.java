package com.url.shortner.Repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.url.shortner.model.ClickEvent;
import com.url.shortner.model.UrlMapping;

public interface CilckEventRepository extends JpaRepository<ClickEvent, Long> {

  List<ClickEvent> findByUrlMappingAndClickDateBetween(UrlMapping mapping, LocalDateTime startDate,
      LocalDateTime endDate);

  List<ClickEvent> findByUrlMappingInAndClickDateBetween(List<UrlMapping> urlMappings, LocalDateTime startDate,
      LocalDateTime endDate);

}
