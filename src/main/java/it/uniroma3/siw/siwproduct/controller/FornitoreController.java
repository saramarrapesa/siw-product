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


	//metodo per visualizzare i fornitori
	@GetMapping("/fornitori")
	public String getFornitori(Model model) {
		model.addAttribute("fornitori", this.fornitoreService.findAllFornitori());
		return "fornitori";
	}

	//metodo per visualizzare un particolare fornitore in base a id
	@GetMapping("/fornitore/{fornitoreId}")
	public String getFornitore(@PathVariable("fornitoreId") Long fornitoreId, Model model) {
		Fornitore fornitore = this.fornitoreService.findFornitoreById(fornitoreId);
		if(fornitore!=null) {
			model.addAttribute("fornitore", fornitore);
			return "fornitore";
		}
		else {
			return "fornitoreError";
		}
	}

	//metodoto che visualizza tutti i fornitori nella dashboard dell'admin
	@GetMapping(value="/admin/manageFornitori")
	public String manageFornitore(Model model) {
		model.addAttribute("fornitori", this.fornitoreService.findAllFornitori());
		return "admin/manageFornitori";
	}

	@PostMapping("/admin/manageFornitori")
	public String newFornitore(@Valid @ModelAttribute("fornitore") Fornitore fornitore , BindingResult bindingResult , @RequestParam("fornitoreImage") MultipartFile multipartFile , Model model) throws IOException {
		this.fornitoreValidator.validate(fornitore, bindingResult);
		if(!bindingResult.hasErrors()) {
			model.addAttribute("fornitore", this.fornitoreService.createNewFornitore(fornitore , multipartFile));
			return "admin/manageFornitori";
		}else {
			model.addAttribute("messaggioErrore", "Questo fornitore esiste già");
			return "admin/formNewFornitore";

		}

	}
	@GetMapping(value="/admin/formNewFornitore")
	public String formNewFornitore(Model model) {
		model.addAttribute("fornitore", new Fornitore());
		return "admin/formNewFornitore";
	}


	//metodo per modificare un prodotto
	@GetMapping(value="/admin/formUpdateFornitore/{fornitoreId}")
	public String formUpdateFornitore(@PathVariable("fornitoreId") Long fornitoreId, Model model) {
		Fornitore fornitore = this.fornitoreService.findFornitoreById(fornitoreId);
		if(fornitore!=null) {
			model.addAttribute("fornitore", fornitoreService.findFornitoreById(fornitoreId));
			return "admin/formUpdateFornitore";

		}else {
			return "fornitoreError";
		}
	}

	//metodo che modifica un prodotto
	@PostMapping("/admin/manageFornitori/{fornitoreId}")
	public String updateFornitore(@PathVariable("fornitoreId") Long fornitoreId , @ModelAttribute("fornitore") Fornitore fornitore){
		//get prodotto from database by id
		Fornitore exisistinFornitore = this.fornitoreService.findFornitoreById(fornitoreId);
		exisistinFornitore.setId(fornitoreId);
		exisistinFornitore.setNome(fornitore.getNome());
		exisistinFornitore.setAddress(fornitore.getAddress());
		exisistinFornitore.setEmail(fornitore.getEmail());

		//save updated product Object
		this.fornitoreService.updateFornitore(fornitore);
		return "admin/manageProdotti";
	}

	//metodo per eliminare un prodotto
	@GetMapping(value="/admin/manageFornitori/{fornitoreId}")
	public String RemoveFornitore(@PathVariable("fornitoreId") Long fornitoreId) {
		this.fornitoreService.deleteFornitore(fornitoreId);
		return  "admin/manageFornitori";
	}

	

	


}
