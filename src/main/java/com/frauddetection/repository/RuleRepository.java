package com.frauddetection.repository;

import com.frauddetection.entity.RuleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RuleRepository extends JpaRepository<RuleEntity, String> {
}
