package org.insang.unitTests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

import org.insang.domain.Member;
import org.insang.repository.MemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
public class RepositoryTests {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	MemberRepository repository;
	
	@Test
	public void shouldFindNoCustomersIfrepositoryIsEmpty() {
		Iterable<Member> members = repository.findAll();
 
		assertThat(members).isEmpty();
	}
	
	@Test
	public void shouldStoreAmember() {
		Member member = new Member();
		member.setEmail("insang@hansung.ac.kr");
		member.setName("insang");
		member.setScore(30);
		
		Member saved = repository.save(member);
 
		assertEquals(saved.getName(), member.getName());
		assertEquals(saved.getEmail(), member.getEmail());
		assertEquals(saved.getScore(), member.getScore());
	}
	
	@Test
	public void shouldFindAllMembers() {
		Member member1 = new Member();
		member1.setEmail("insang1@hansung.ac.kr");
		member1.setName("insang1");
		member1.setScore(30);
		entityManager.persist(member1);
		
		Member member2 = new Member();
		member2.setEmail("insang2@hansung.ac.kr");
		member2.setName("insang2");
		member2.setScore(40);
		entityManager.persist(member2);
		
		Member member3 = new Member();
		member3.setEmail("insang3@hansung.ac.kr");
		member3.setName("insang3");
		member3.setScore(45);
		entityManager.persist(member3);
		Iterable<Member> members = repository.findAll();
 
		assertThat(members).hasSize(3).contains(member1, member2, member3);
	}
 
	@Test
	public void shouldFindCustomerById() {
		Member member1 = new Member();
		member1.setEmail("insang1@hansung.ac.kr");
		member1.setName("insang1");
		member1.setScore(30);
		entityManager.persist(member1);
		
		Member member2 = new Member();
		member2.setEmail("insang2@hansung.ac.kr");
		member2.setName("insang2");
		member2.setScore(40);
		entityManager.persist(member2);
 
		Member found = repository.findOne(member2.getId());
 
		assertThat(found).isEqualTo(member2);
	}
	
	@Test
	public void should_deleteAllCustomer() {
		Member member1 = new Member();
		member1.setEmail("insang1@hansung.ac.kr");
		member1.setName("insang1");
		member1.setScore(30);
		entityManager.persist(member1);
		
		Member member2 = new Member();
		member2.setEmail("insang2@hansung.ac.kr");
		member2.setName("insang2");
		member2.setScore(40);
		entityManager.persist(member2);
 
		repository.deleteAll();;
 
		assertThat(repository.findAll()).isEmpty();
	}
 
}
