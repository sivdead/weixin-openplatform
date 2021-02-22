package com.github.sivdead.wx.open.dao.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BaseJPA<T,ID> extends JpaRepository<T, ID>, QuerydslPredicateExecutor<T>, JpaSpecificationExecutor<T> {
}
