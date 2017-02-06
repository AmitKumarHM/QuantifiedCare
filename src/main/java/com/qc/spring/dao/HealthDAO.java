package com.qc.spring.dao;

import com.qc.spring.entities.Health;

public interface HealthDAO {

	public Health byID(Long id);

	public Health byUserId(Long id);
}
