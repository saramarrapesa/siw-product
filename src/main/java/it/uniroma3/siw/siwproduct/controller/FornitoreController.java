package it.uniroma3.siw.siwproduct.controller;

import it.uniroma3.siw.siwproduct.model.Fornitore;
import it.uniroma3.siw.siwproduct.service.FornitoreService;
import it.uniroma3.siw.siwproduct.validator.FornitoreValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@Controller
public class FornitoreController {
	
	@Autowired
	private FornitoreService fornitoreService;
	
	@Autowired
	private FornitoreValidator fornitoreValidator;
	
	@GetMapping(value="/admin/formNewFornitore")
	public String formNewFornitore(Model model) {
		model.addAttribute("fornitore", new Fornitore());
		return "admin/formNewFornitore";
	}
	
	@GetMapping(value="/admin/indexFornitore")
	public String indexFornitore() {
		return "admin/indexFornitore";
	}
	
	@PostMapping("/admin/fornitore")
		public String newFornitore(@Valid @ModelAttribute("fornitore") Fornitore fornitore , BindingResult bindingResult , @RequestParam("fornitoreImage") MultipartFile multipartFile , Model model) throws IOException {
		this.fornitoreValidator.validate(fornitore, bindingResult);
		if(!bindingResult.hasErrors()) {
			model.addAttribute("fornitore", this.fornitoreService.createNewFornitore(fornitore , multipartFile));
			return "fornitore";
		}else {
			model.addAttribute("messaggioErrore", "Questo fornitore esiste già");
			return "admin/formNewFornitore";

		}
		
	}
	
	@GetMapping("/fornitore/{id}")
	public String getFornitore(@PathVariable("id") Long id, Model model) {
		Fornitore fornitore = this.fornitoreService.findFornitoreById(id);
		if(fornitore!=null) {
			model.addAttribute("fornitore", fornitore);
			return "fornitore";
		}
		else {
			return "fornitoreError";
		}
	}
	
	@GetMapping("/fornitori")
	public String getFornitori(Model model) {
		model.addAttribute("fornitori", this.fornitoreService.findAllForniori());
		return "fornitori";
	}

}
