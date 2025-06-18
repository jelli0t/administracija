/**
 * 
 */
package rs.neks.administration.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import rs.neks.administration.security.model.Operator;
import rs.neks.administration.security.service.OperatorService;
import rs.neks.administration.util.Notification;

/**
 * @author jelles
 *
 */
@Controller
@RequestMapping("/operators")
public class OperatorController {

	@Autowired
	private OperatorService operatorService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	

	@RequestMapping(method = RequestMethod.GET)
	public String home(Model model) {
		List<Operator> operators = operatorService.findAll(true);
		model.addAttribute("operators", operators);
		return "operators";
	}
	
	
	@RequestMapping(path = {"/add", "/{id}/edit"}, method = RequestMethod.GET)
	public ModelAndView prepareOperator(@PathVariable Optional<Integer> id, ModelMap model) {
		Operator operator = id.map(x -> operatorService.findById(x)).orElse(new Operator());				
		if(!model.containsAttribute("operator")) {
			model.addAttribute("operator", operator);
		}
		return new ModelAndView("operators :: edit", model, (HttpStatus) model.getAttribute("httpStatus"));
	}
	
	
	@RequestMapping(path = "/save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView saveOperator(@Valid @RequestBody Operator operator, BindingResult bindingResult, ModelMap modelMap, 
			RedirectAttributes redirectAttributes) {		
		if (bindingResult.hasErrors()) {
			modelMap.addAttribute("operator", operator);
			modelMap.addAttribute("httpStatus", HttpStatus.BAD_REQUEST);
			modelMap.addAllAttributes(bindingResult.getModel());
			return prepareOperator(Optional.empty(), modelMap);
		}		
		encodeOperatorsPassword(operator);
		boolean result = operatorService.save(operator);
		Notification notification = new Notification(result, "Uspesno ste sacuvali podatke o kupcu", null);
		redirectAttributes.addFlashAttribute("notification", notification);
		redirectAttributes.addFlashAttribute("httpStatus", HttpStatus.OK);
		return new ModelAndView("redirect:/operators");
	}
	
	
	private void encodeOperatorsPassword(Operator operator) {
		final String encodedPasswd = passwordEncoder.encode(operator.getPassword());
		operator.setPassword(encodedPasswd);
	}

}