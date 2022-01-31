package com.ademkose.casesolving.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ademkose.casesolving.exception.BadRequestException;
import com.ademkose.casesolving.model.Presentation;
import com.ademkose.casesolving.service.PresentationService;

@Controller
public class PresentationController {
	private static final Logger LOG = LoggerFactory.getLogger(PresentationController.class);
	@Autowired
	PresentationService presentationService;

	@GetMapping("/presentation")
	public String presentationForm(Model model) {
		model.addAttribute("presentation", new Presentation());
		model.addAttribute("presentation_list", presentationService.getAllPresentation());
		LOG.info("[presentationForm]: Getting the presentation...");		
		return "presentation";
	}
	@GetMapping("/tracks")
	public String viewResult(Model model) {
		List<Presentation> new_Presentation_list = presentationService.startToMakeTracks();
		model.addAttribute("tracks_list",new_Presentation_list);
		LOG.info("[viewResult]: Listing tracks...");		
		return "tracks";
	}
	@GetMapping("/load_test_data")
	public String loadTestData(Model model, RedirectAttributes redirAttrs) {
		presentationService.loadTestData();			
		redirAttrs.addFlashAttribute("success", "The test presentations have been successfully added to database...");
		LOG.info("[loadTestData]: The test presentations have been successfully added to database... ");
		return "redirect:/presentation/";
	}
	@PostMapping("/presentation")
	public String presentationSubmit(@ModelAttribute Presentation presentation, Model model, RedirectAttributes redirAttrs) {
		
		LOG.info("[presentationSubmit]: Getting new presentation " + presentation.toString());
		try {
			presentationService.saveThePresentation(presentation);
			model.addAttribute("presentation_list", presentationService.getAllPresentation());
		} catch (BadRequestException e) {
			model.addAttribute("presentation_list", presentationService.getAllPresentation());
	        redirAttrs.addFlashAttribute("error", e.getMessage());
	        LOG.info("[presentationSubmit]: Error " + e.getMessage() + "-" + presentation.toString());			
	        return "redirect:/presentation/";
		}
		LOG.info("[presentationSubmit]: The presentation has been successfully added to database... " + presentation.toString());
		redirAttrs.addFlashAttribute("success", "The presentation has been successfully added to database...");
	    return "redirect:/presentation/";
	}	
}