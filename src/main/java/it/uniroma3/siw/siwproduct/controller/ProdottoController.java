package it.uniroma3.siw.siwproduct.controller;

import it.uniroma3.siw.siwproduct.model.Prodotto;
import it.uniroma3.siw.siwproduct.service.FornitoreService;
import it.uniroma3.siw.siwproduct.service.ProdottoService;
import it.uniroma3.siw.siwproduct.validator.ProdottoValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


import jakarta.validation.Valid;

@Controller
public class ProdottoController {
	
	@Autowired
	public ProdottoService prodottoService;
	
	@Autowired
	public FornitoreService fornitoreService;
	
	@Autowired
	public ProdottoValidator prodottoValidator;
	
	@GetMapping(value="/admin/formNewProdotto")
	public String formNewProdotto(Model model) {
		model.addAttribute("prodotto", new Prodotto());
		return "admin/foemNewProdotto.html";
	}
	
	@GetMapping(value="/admin/formUpdateProdotto")
	public String formUpdateProdotto(@PathVariable("prodottoId") Long prodottoId, Model model) {
		Prodotto prodotto = this.prodottoService.findProdottoById(prodottoId);
		if(prodotto!=null) {
			model.addAttribute("prodotto", prodottoService.findProdottoById(prodottoId));
			return "admin/formUpdateProdotto.html";
		
		}else {
			return "prodottoError.html";
		}
	}
	
	@GetMapping(value="/admin/manageProdotti")
	public String manageProdotto(Model model) {
		model.addAttribute("prodotti", this.prodottoService.findAllProdotti());
		return "admin/manageProdotti.html";
	}
	
	@GetMapping(value="admin/setFornitoreToProdotto/{fornitoreId}/{prodottoId}")
	public String setFornitoreToProdotto(@PathVariable("fornitoreId")Long fornitoreId , @PathVariable("prodottoId") Long prodottoId, Model model) {
		Prodotto prodotto = this.prodottoService.addFornitoreToProdotto(fornitoreId, prodottoId);
		if(prodotto!=null) {
			model.addAttribute("prodotto", prodotto);
			return "admin/formUpdateProdotto.html";
		}
		else {
			return "prodottoError.html";
		}
	}
	
	@GetMapping(value= "/admin/addFornitore/{fornitoreId}")
	public String addFornitore(@PathVariable("fornitoreId") Long fornitoreId, Model model) {
		model.addAttribute("fornitori", this.fornitoreService.findAllForniori() );
		Prodotto prodotto = this.prodottoService.findProdottoById(fornitoreId);
		if(prodotto!=null) {
			model.addAttribute("prodotto", prodotto);
			return "admin/fornitoreToAdd.html";
		}else {
			return "prodottoError.html";
		}
	}
	
	@GetMapping("/formSearchProdotti")
	public String formSearchProdotti() {
		return "formSearchProdotti.html";
	}
	
	@PostMapping("/searchProdotti")
	public String searchProdotti(Model model , @RequestParam String nome) {
		model.addAttribute("prodotti", this.prodottoService.findProdottoByNome(nome));
		return "foundProdotti.html";
	}
	
	@PostMapping("/prodotto")
	public String newProdotto(@Valid @ModelAttribute("prodotto")Prodotto prodotto , BindingResult bindingResult , Model model) {
		this.prodottoValidator.validate(prodotto, bindingResult);
		if(!bindingResult.hasErrors()) {
			this.prodottoService.saveProdotto(prodotto);
			model.addAttribute("prodotto", prodotto);
			return "prodotto.html";
		}else {
			return "formNewProdotto.html";
		}
		
	}
	
	@GetMapping("/prodotto/{id}")
	public String getProdotto(@PathVariable("id") Long id, Model model) {
		Prodotto prodotto = this.prodottoService.findProdottoById(id);
		if(prodotto!=null) {
			model.addAttribute("prodotto", prodotto);
			return "prodotto.html";
		}else {
			return "prodottoError";
		}
	}
	
	
	

}
