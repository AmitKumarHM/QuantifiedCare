package com.qc.spring.service;

import com.qc.spring.entities.Health;

public interface HealthService {

	public Health findByID(Long id);

	public Health findByUserId(Long id);
}
