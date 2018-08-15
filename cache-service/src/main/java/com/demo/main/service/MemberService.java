package com.demo.main.service;

import java.util.List;

import com.demo.main.domain.Member;

public interface MemberService {

	Member getMemberById(long id);
	List<Member> getAllMembers();
}
