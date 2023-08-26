package it.uniroma3.siw.siwproduct.controller;

import it.uniroma3.siw.siwproduct.model.Fornitore;
import it.uniroma3.siw.siwproduct.model.Prodotto;
import it.uniroma3.siw.siwproduct.model.Review;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
public class ProdottoController {
	
	@Autowired
	public ProdottoService prodottoService;
	
	@Autowired
	public FornitoreService fornitoreService;
	
	@Autowired
	public ProdottoValidator prodottoValidator;

	@Autowired
	public GlobalController globalController;
	
	@GetMapping(value="/admin/formNewProdotto")
	public String formNewProdotto(Model model) {
		model.addAttribute("prodotto", new Prodotto());
		return "admin/formNewProdotto";
	}
	
	@GetMapping(value="/admin/formUpdateProdotto")
	public String formUpdateProdotto(@PathVariable("prodottoId") Long prodottoId, Model model) {
		Prodotto prodotto = this.prodottoService.findProdottoById(prodottoId);
		if(prodotto!=null) {
			model.addAttribute("prodotto", prodottoService.findProdottoById(prodottoId));
			return "admin/formUpdateProdotto";
		
		}else {
			return "prodottoError";
		}
	}

	@GetMapping(value = "/admin/indexProdotto")
	public String indexProdotto(){
		return "admin/indexProdotto";
	}
	
	@GetMapping(value="/admin/manageProdotti")
	public String manageProdotto(Model model) {
		model.addAttribute("prodotti", this.prodottoService.findAllProdotti());
		return "admin/manageProdotti";
	}
	
	@GetMapping(value="admin/addFornitoreToProdotto/{fornitoreId}/{prodottoId}")
	public String addFornitoreToProdotto(@PathVariable("fornitoreId")Long fornitoreId , @PathVariable("prodottoId") Long prodottoId, Model model) {
		Prodotto prodotto = this.prodottoService.addFornitoreToProdotto(fornitoreId, prodottoId);
		if(prodotto!=null) {
			model.addAttribute("prodotto", prodotto);
			return "admin/formUpdateProdotto";
		}
		else {
			return "prodottoError";
		}
	}

	@GetMapping(value = "/admin/removeFornitoreToProdotto/{fornitoreId}/{prodottoId}")
	public String removeFornitoreToProdotto(@PathVariable("prodottoId") Long prodottoId , @PathVariable("fornitoreId") Long fornitoreId, Model model ){
		Prodotto prodotto = this.prodottoService.findProdottoById(prodottoId);
		if(prodotto!=null){
			List<Fornitore> fornitoriToAdd= fornitoreService.findFornitoriInProdotto(prodottoId);
			model.addAttribute("prodotto",prodotto);
			model.addAttribute("fornitoriToAdd" , fornitoriToAdd);
			return "admin/fornitoriToAdd";
		}
		else{
			return "prodottoError";
		}
	}
	
	@GetMapping(value= "/admin/addFornitore/{fornitoreId}")
	public String addFornitore(@PathVariable("fornitoreId") Long fornitoreId, Model model) {
		model.addAttribute("fornitori", this.fornitoreService.findAllForniori() );
		Prodotto prodotto = this.prodottoService.findProdottoById(fornitoreId);
		if(prodotto!=null) {
			model.addAttribute("prodotto", prodotto);
			return "admin/fornitoriToAdd";
		}else {
			return "prodottoError";
		}
	}
	
	@GetMapping("/formSearchProdotti")
	public String formSearchProdotti() {

		return "formSearchProdotti";
	}
	
	@PostMapping("/searchProdotti")
	public String searchProdotti(Model model , @RequestParam String nome) {
		model.addAttribute("prodotti", this.prodottoService.findProdottoByNome(nome));
		return "foundProdotti";
	}
	
	@PostMapping("/admin/prodotto")
	public String newProdotto(@Valid @ModelAttribute("prodotto")Prodotto prodotto , BindingResult bindingResult , @RequestParam("prodottoImage") MultipartFile[] multipartFile, Model model) {
		this.prodottoValidator.validate(prodotto, bindingResult);
		if(!bindingResult.hasErrors()) {
			model.addAttribute("prodotto", this.prodottoService.createNewProdotto(prodotto, multipartFile));
			return "prodotto";
		}else {
			return "admin/formNewProdotto";
		}
		
	}
	
	@GetMapping("/prodotto/{id}")
	public String getProdotto(@PathVariable("id") Long id, Model model) {
		Prodotto prodotto = this.prodottoService.findProdottoById(id);
		if(prodotto!=null) {
			model.addAttribute("prodotto", prodotto);
			model.addAttribute("review", new Review());
			model.addAttribute("reviews", prodotto.getReviews());
			model.addAttribute("hasReviews", !prodotto.getReviews().isEmpty());

			if(this.globalController.getUser()!=null&& this.globalController.getUser().getUsername()!=null && this.prodottoService.alreadyRewied(prodotto.getReviews(),this.globalController.getUser().getUsername()))
				model.addAttribute("hasNotAlreadyCommented", true);
			else
				model.addAttribute("hasNotAlreadyCommented", false);
			return "prodotto";
		}else {
			return "prodottoError";
		}
	}

	@GetMapping("/prodotti")
	public  String getProdotti(Model model){
		model.addAttribute("prodotti", this.prodottoService.findAllProdotti());
		return "prodotti";
	}

	@GetMapping("admin/updateFornitori/{id}")
	public String updateFornitori(@PathVariable("prodottoId") Long prodottoId, Model model){
		List<Fornitore> fornitoriToAdd= this.fornitoreService.findFornitoriInProdotto(prodottoId);
		model.addAttribute("fornitoriToAdd", fornitoriToAdd);
		Prodotto prodotto= this.prodottoService.findProdottoById(prodottoId);
		if(prodotto!=null){
			model.addAttribute("prodotto", prodotto);
			return "admin/fornitoriToAdd";
		}else{
			return "prodottoError";
		}
	}
	
	
	

}
