package org.insang.web;

import java.util.List;

import org.insang.domain.Member;
import org.insang.service.Memberservice;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class DemoController {
	
	@Autowired
	private Memberservice memberService;
	
	@RequestMapping(value="/", method=RequestMethod.GET)
	public ModelAndView list(@ModelAttribute Member member, ModelAndView mav) {
		mav.setViewName("create");
		Iterable<Member> members = memberService.findAll();
		mav.addObject("members", members);
		return mav;
	}
	
	@RequestMapping(value="/deleteall", method=RequestMethod.POST)
	public ModelAndView deleteAll(@ModelAttribute Member member, ModelAndView mav) {
		memberService.deleteAll();
		ModelAndView r= new ModelAndView("redirect:/");
		return r;
	}

	@RequestMapping(value="/edit", method=RequestMethod.GET)
	public ModelAndView edit(@RequestParam Long id, ModelAndView mav) {
		Member member = memberService.findById(id);	
		mav.setViewName("edit");
		mav.addObject("member", member);
		return mav;
	}
	
	@RequestMapping(value={"/", "/edit"}, method=RequestMethod.POST)
	public ModelAndView update(@ModelAttribute @Validated Member member, BindingResult result, 
			ModelAndView mav) {
		
		ModelAndView r=null;
		if (!result.hasErrors()) {
			memberService.create(member);
			r= new ModelAndView("redirect:/");
		} else {
			mav.setViewName("create");
			Iterable<Member> members = memberService.findAll();
			mav.addObject("members", members);
			r = mav;
		}
		return r;
	}
	
	@RequestMapping(value="/delete", method=RequestMethod.POST)
	public ModelAndView delete(@RequestParam Long id, ModelAndView mav) {
		memberService.delete(id);
		return new ModelAndView("redirect:/");
	}
}