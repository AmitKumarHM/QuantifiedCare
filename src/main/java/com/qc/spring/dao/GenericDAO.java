package com.qc.spring.dao;

import java.io.Serializable;
import java.util.List;

import com.qc.spring.entities.States;

public interface GenericDAO {

	  public <T> T find(Class<T> type, Serializable id);

      public <T> T[] find(Class<T> type, Serializable... ids);

      public <T> T getReference(Class<T> type, Serializable id);

      public <T> T[] getReferences(Class<T> type, Serializable... ids);

      public Serializable save(Object entity);

      public Serializable[] save(Object... entities);

      public boolean remove(Object entity);

      public void remove(Object... entities);

      public boolean removeById(Class<?> type, Serializable id);

      public void removeByIds(Class<?> type, Serializable... ids);

      public <T> List<T> findAll(Class<T> type);
      
      public States findByStateName(String state);
	
}
