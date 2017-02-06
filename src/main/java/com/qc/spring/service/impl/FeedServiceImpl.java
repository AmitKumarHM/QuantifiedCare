package com.qc.spring.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qc.spring.dao.FeedDAO;
import com.qc.spring.entities.Feeds;
import com.qc.spring.service.FeedService;

@Service
@Transactional
public class FeedServiceImpl implements FeedService {

	@Autowired
	private FeedDAO feedDAO;
	
	@Override
	public List<Feeds> findByUserId(Long id) {
		
		return feedDAO.byUserId(id);
	}

}
