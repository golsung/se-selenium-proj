package org.insang.service;

import java.util.List;

import javax.annotation.PostConstruct;

import org.insang.domain.Member;
import org.insang.repository.MemberRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Memberservice {

	@Autowired
	private MemberRepository memberRepository;

	public Member create(Member member) {
		Member created = memberRepository.saveAndFlush(member);
		return created;
	}

	public List<Member> findAll() {
		// TODO Auto-generated method stub
		return memberRepository.findAll();
	}

	public Member findById(Long id) {
		Member member = memberRepository.findById(id);
		return member;
	}
	
	public void delete(Long id) {
		memberRepository.delete(id);
	}

	public void deleteAll() {
		memberRepository.deleteAll();
		
	}
}
