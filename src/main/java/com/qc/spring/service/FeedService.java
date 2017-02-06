package com.qc.spring.service;

import java.util.List;

import com.qc.spring.entities.Feeds;

public interface FeedService {

	public List<Feeds> findByUserId(Long id);
}
