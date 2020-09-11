/**
 * 
 */
package rs.neks.administration.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import rs.neks.administration.security.model.Operator;

/**
 * @author nemanja
 *
 */
@Controller
@RequestMapping("/")
public class HomeController {

	@RequestMapping(method = RequestMethod.GET)
	public String home(Model model) {
		return "home";
	}
	
	
	@RequestMapping(path = "/login", method = RequestMethod.GET)
	public String login(Model model) {
		model.addAttribute("operator", new Operator());
		return "login";
	}
	
}
