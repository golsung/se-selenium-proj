package org.insang.unitTests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.List;

import org.insang.domain.Member;
import org.insang.repository.MemberRepository;
import org.insang.service.Memberservice;
import org.insang.web.DemoController;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.hamcrest.Matchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest
//@WebMvcTest(DemoController.class)
public class DemoControllerTests {
    private MockMvc mockMvc;
    
    @Autowired
    private WebApplicationContext webApplicationContext;
    
    @MockBean
    private Memberservice memberService;
    
    @Autowired 
    private ObjectMapper mapper;
    
    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }
    
    @Test
    public void shoudGoHomeWhenFirstVisited() throws Exception {
        
        mockMvc.perform(get("/"))
        		.andExpect(view().name("create"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("members"))
                .andDo(print());
    }
    
  @Test
  public void shoudDisplayHomePageWhenMultipleMembersAreCreated() throws Exception {
    List<Member> mList = new ArrayList<>();
    Member member1 = new Member();
    member1.setEmail("insang1@hansung.ac.kr");
    member1.setName("insang1");
    member1.setScore(30);
    member1.setId(12345L);
    mList.add(member1);
    
    Member member2 = new Member();
    member2.setEmail("insang2@hansung.ac.kr");
    member2.setName("insang2");
    member2.setScore(40);
    member2.setId(23456L);
    mList.add(member2);
    
    when(memberService.findAll()).thenReturn(mList);
	  
    mockMvc.perform(get("/"))
	    .andExpect(view().name("create"))
	    .andExpect(status().isOk())
	    .andExpect(model().attributeExists("members"))
	    .andExpect(model().attribute("members", hasSize(2)))
	    .andExpect(model().attribute("members", hasItem(
                allOf(
                        hasProperty("id", is(12345L)),
                        hasProperty("name", is("insang1")),
                        hasProperty("score", is(30)),
                        hasProperty("email", is("insang1@hansung.ac.kr"))
                )
        )))
        .andExpect(model().attribute("members", hasItem(
                allOf(
                        hasProperty("id", is(23456L)),
                        hasProperty("name", is("insang2")),
                        hasProperty("score", is(40)),
                        hasProperty("email", is("insang2@hansung.ac.kr"))
                )
        )))
	    .andDo(print());
  }
    
    @Test
    public void shoudRedirectToHomeWhenAMemberIsCreated() throws Exception {
    	Member member = new Member();
    	member.setEmail("insang@hansung.ac.kr");
    	member.setName("insang");
    	member.setScore(30);
    	member.setId(12345L);
    	when(memberService.create(member)).thenReturn(member);


    	mockMvc.perform(post("/")
    			.param("name", member.getName())
    			.param("email", member.getEmail())
    			.param("score", Integer.toString(member.getScore()))
    			)
    	.andExpect(view().name("redirect:/"))
    	.andExpect(redirectedUrl("/"))
    	.andDo(print());
    	
        ArgumentCaptor<Member> argument = ArgumentCaptor.forClass(Member.class);
        verify(memberService).create(argument.capture());
        assertEquals("insang", argument.getValue().getName());
        assertEquals("insang@hansung.ac.kr", argument.getValue().getEmail());
        assertEquals(30, argument.getValue().getScore());
   
    }
    
    @Test
    public void shoudGoToEditPageWhenAnExsitingMemberIsEdited() throws Exception {
    	Member member = new Member();
		member.setEmail("insang@hansung.ac.kr");
		member.setName("insang");
		member.setScore(30);
		member.setId(12345L);
        when(memberService.findById(12345L)).thenReturn(member);
        mockMvc.perform(get("/edit")
        		.param("id", Long.toString(12345L)))
        		.andExpect(view().name("edit"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("member"))
                .andExpect(model().attribute("member", hasProperty("id", is(12345L))))
                .andExpect(model().attribute("member", hasProperty("name", is("insang"))))
                .andExpect(model().attribute("member", hasProperty("email", is("insang@hansung.ac.kr"))))
                .andExpect(model().attribute("member", hasProperty("score", is(30))))
                .andDo(print());
    }
    
  @Test
  public void shoudRedirectToHomeWhenAMemberIsDeleted() throws Exception {
      Member member = new Member();
      member.setEmail("insang@hansung.ac.kr");
      member.setName("insang");
      member.setScore(30);
      member.setId(12345L);
      
      mockMvc.perform(post("/delete")
    		  .param("id", Long.toString(12345L)))
      .andExpect(view().name("redirect:/"))
      .andExpect(redirectedUrl("/"))
      .andDo(print());
      verify(memberService, never()).create(any());
 
  }
  
  @Test
  public void shoudRedirectToHomeWhenAMemberWithoutNameIEntered() throws Exception {
	  Member member = new Member();
	  member.setEmail("insang@hansung.ac.kr");
	  member.setName("");
	  member.setScore(30);
	  member.setId(12345L);

	  mockMvc.perform(post("/")
			  .param("name", member.getName())
			  .param("email", member.getEmail())
			  .param("score", Integer.toString(member.getScore()))
			  )
	  .andExpect(status().isOk())
	  .andExpect(view().name("create"))
	  .andExpect(model().attributeExists("members"))
	  .andDo(print());
	  verify(memberService, never()).create(any());

  }
}