package com.qc.spring.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qc.spring.dao.GoalDAO;
import com.qc.spring.service.GoalService;

@Service
@Transactional
public class GoalServiceImpl implements GoalService {

	@Autowired
	private GoalDAO goalDAO;

}
