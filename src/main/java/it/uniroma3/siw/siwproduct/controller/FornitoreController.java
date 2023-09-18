package it.uniroma3.siw.siwproduct.controller;

import it.uniroma3.siw.siwproduct.model.Fornitore;
import it.uniroma3.siw.siwproduct.service.FornitoreService;
import it.uniroma3.siw.siwproduct.service.ProdottoService;
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


@Controller
public class FornitoreController {
	
	@Autowired
	private FornitoreService fornitoreService;
	
	@Autowired
	private FornitoreValidator fornitoreValidator;

	@Autowired
	private ProdottoService prodottoService;


	//metodoto che visualizza tutti i fornitori nella dashboard dell'admin
	@GetMapping(value="/admin/manageFornitori")
	public String manageFornitore(Model model) {
		model.addAttribute("fornitori", this.fornitoreService.getAllFornitori());
		return "/admin/manageFornitori";
	}


	@GetMapping(value="/admin/formNewFornitore")
	public String formNewFornitore(Model model) {
		model.addAttribute("fornitore", new Fornitore());
		return "admin/formNewFornitore";
	}

	@PostMapping("/admin/fornitore/add")
	public String postFornitoreAdd(@Valid @ModelAttribute("fornitore") Fornitore fornitore , BindingResult bindingResult , Model model){
		this.fornitoreValidator.validate(fornitore, bindingResult);
		if(!bindingResult.hasErrors()) {
			model.addAttribute("fornitore", this.fornitoreService.createFornitore(fornitore));
			return "redirect:/admin/manageFornitori";
		}else {
			model.addAttribute("messaggioErrore", "Questo fornitore esiste già");
			return "fornitoreError";

		}

	}

	//metodo per eliminare un prodotto
	@GetMapping(value="/admin/deleteFornitore/{fornitoreId}")
	public String RemoveFornitore(@PathVariable("fornitoreId") Long fornitoreId) {
		this.fornitoreService.deleteFornitore(fornitoreId);
		return "redirect:/admin/manageFornitori";
	}

	//metodo per modificare un prodotto
	@GetMapping(value="/admin/formUpdateFornitore/{fornitoreId}")
	public String formUpdateFornitore(@PathVariable("fornitoreId") Long fornitoreId, Model model) {
		Fornitore fornitore = this.fornitoreService.findFornitoreById(fornitoreId);
		if(fornitore!=null) {
			model.addAttribute("fornitore", fornitore);
			return "admin/formUpdateFornitore";

		}else {
			return "fornitoreError";
		}
	}

	//metodo che modifica un prodotto
	@PostMapping("/admin/manageFornitori/{fornitoreId}")
	public String updateFornitore(@PathVariable("fornitoreId") Long fornitoreId , @ModelAttribute("fornitore") Fornitore fornitore){
		//get prodotto from database by id
		Fornitore exisistinFornitore = new Fornitore();
		exisistinFornitore = this.fornitoreService.findFornitoreById(fornitoreId);
		exisistinFornitore.setNome(fornitore.getNome());
		exisistinFornitore.setAddress(fornitore.getAddress());
		exisistinFornitore.setEmail(fornitore.getEmail());
		//save updated product Object
		this.fornitoreService.saveFornitore(exisistinFornitore);
		return "redirect:/admin/manageFornitori";
	}

	@GetMapping(value="/admin/elencoProdottiFornitore/{fornitoreId}")
	public String formElencoFornitori(@PathVariable("fornitoreId") Long fornitoreId, Model model) {
		Fornitore fornitore = this.fornitoreService.findFornitoreById(fornitoreId);
		if(fornitore!=null) {
			model.addAttribute("fornitore", fornitore);
			model.addAttribute("prodotti",prodottoService.findAllProdotti());
			return "admin/elencoProdottiFornitore";

		}else {
			return "fornitoreError";
		}
	}


	//metodo per visualizzare i fornitori
	@GetMapping("/fornitori")
	public String getFornitori(Model model) {
		model.addAttribute("fornitori", this.fornitoreService.getAllFornitori());
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


	@GetMapping("/admin/addProdotto/{fornitoreId}/{prodottoId}")
	public String addProdottoToFornitore (@PathVariable("fornitoreId") Long fornitoreId, @PathVariable("prodottoId") Long prodottoId,Model model) {


		this.prodottoService.addFornitoreToProdotto(prodottoId, fornitoreId);

		model.addAttribute("fornitore", this.fornitoreService.addProdotto(fornitoreId,prodottoId));
		model.addAttribute("prodotti", this.prodottoService.findAllProdotti());
		return "redirect:/admin/elencoProdottiFornitore/{fornitoreId}";
	}

	@GetMapping("/admin/removeProdotto/{fornitoreId}/{prodottoId}")
	public String removeProdotto (@PathVariable("fornitoreId") Long fornitoreId, @PathVariable("prodottoId") Long prodottoId,Model model) {

		this.prodottoService.deleteFornitoreFromProdotto(prodottoId, fornitoreId);

		model.addAttribute("fornitore", this.fornitoreService.removeProdotto(fornitoreId,prodottoId));
		model.addAttribute("prodotti", this.prodottoService.findAllProdotti());

		return "redirect:/admin/elencoProdottiFornitore/{fornitoreId}";
	}


}
