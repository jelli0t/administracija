/**
 * 
 */
package rs.neks.administration.controller;

import java.time.LocalDate;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 * @author jelles
 *
 */
@Controller
@RequestMapping("/year")
@SessionAttributes({"year"})
public class YearAware {
	
	
	@ModelAttribute("year")
	public int defaultYear(Model model) {
		return LocalDate.now().getYear();
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> switchYear(@RequestParam("year") Integer year, Model model) {
		if(year == null || year < 2010 || year > 2099) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		model.addAttribute("year", year);
		return new ResponseEntity<>(HttpStatus.OK);
	}

}