/**
 * 
 */
package rs.neks.administration.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import rs.neks.administration.security.model.Operator;
import rs.neks.administration.security.service.OperatorService;

/**
 * @author nemanja
 *
 */
@Controller
@RequestMapping("/")
public class HomeController {
	
	@Autowired
	private OperatorService operatorService;

	@RequestMapping(method = RequestMethod.GET)
	public String home(Model model) {
		return "redirect:/billings";
	}
	
	
	@RequestMapping(path = "/login", method = RequestMethod.GET)
	public String login(Model model) {
		model.addAttribute("operator", new Operator());
		return "login";
	}
	
}