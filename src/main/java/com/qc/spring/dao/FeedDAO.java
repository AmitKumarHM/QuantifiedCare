package com.qc.spring.dao;

import java.util.List;

import com.qc.spring.entities.Feeds;

public interface FeedDAO {

	public List<Feeds> byUserId(Long id);
}
