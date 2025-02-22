package com.url.shortner.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.url.shortner.model.UrlMapping;
import com.url.shortner.model.User;

public interface urlMappingRepository extends JpaRepository<UrlMapping, Long> {
  UrlMapping findByShortUrl(String shortUrl);

  List<UrlMapping> findByUser(User user);

}
