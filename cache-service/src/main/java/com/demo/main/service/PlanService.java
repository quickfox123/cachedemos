package com.demo.main.service;

import java.util.List;

import com.demo.main.domain.Plan;

public interface PlanService {

	Plan getPlanById(long id);
	List<Plan> getAllPlans();
}
