package org.insang.unitTests;

import static org.junit.Assert.assertEquals;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.insang.domain.Member;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class MemberValidationTests {
	
	private static Validator validator;

    @BeforeClass
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }
    @Test
    public void shouldRaiseErrorWhenTheScoreGreaterThanMaxIsEntered() {
        Member member = new Member();
        member.setEmail("insang@hansung.ac.kr");
        member.setName("insang");
        member.setScore(60);
        Set<ConstraintViolation<Member>> violations = validator.validate(member);
        assertEquals(1, violations.size());
        String message = validator.validateProperty(member, "score").iterator().next().getMessage();
        assertEquals("should be less than 50", message);
    }
    
    @Test
    public void shouldRaiseErrorWhenTheScoreLessThanMinIsEntered() {
        Member member = new Member();
        member.setEmail("insang@hansung.ac.kr");
        member.setName("insang");
        member.setScore(-10);
        Set<ConstraintViolation<Member>> violations = validator.validate(member);
        assertEquals(1, violations.size());
        String message = validator.validateProperty(member, "score").iterator().next().getMessage();
        assertEquals("should be greater than 0", message);
    }
    
    
    @Test
    public void shouldBeWelcomeWhenTheValidScoreIsEntered() {
        Member member = new Member();
        member.setEmail("insang@hansung.ac.kr");
        member.setName("insang");
        member.setScore(20);
        Set<ConstraintViolation<Member>> violations = validator.validate(member);
        assertEquals(0, violations.size());
    }


}
