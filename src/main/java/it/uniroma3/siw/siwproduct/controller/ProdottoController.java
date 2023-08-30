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

import java.io.IOException;
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



	//metodo per visualizzare tutti i prodotti

	@GetMapping("/prodotti")
	public  String getProdotti(Model model){
		model.addAttribute("prodotti", this.prodottoService.findAllProdotti());
		return "prodotti";
	}

	//metodo per visualizzare un particolare prodotto in base all'id

	@GetMapping("/prodotto/{prodottoId}")
	public String getProdotto(@PathVariable("prodottoId") Long prodottoId, Model model) {
		Prodotto prodotto = this.prodottoService.findProdottoById(prodottoId);
		if(prodotto!=null) {
			model.addAttribute("prodotto", prodotto);
			model.addAttribute("review", new Review());
			model.addAttribute("reviews", prodotto.getReviews());
			model.addAttribute("hasReviews", !prodotto.getReviews().isEmpty());

			if(this.globalController.getUser()!=null&& this.globalController.getUser().getUsername()!=null && this.prodottoService.alreadyReviewed(prodotto.getReviews(),this.globalController.getUser().getUsername()))
				model.addAttribute("hasNotAlreadyCommented", true);
			else
				model.addAttribute("hasNotAlreadyCommented", false);
			return "prodotto";
		}else {
			return "prodottoError";
		}
	}


	//COMPITI DELL'ADMIN


	//metodoto che visualizza tutti i prodotti nella dashboard dell'admin
	@GetMapping(value="/admin/manageProdotti")
	public String manageProdotto(Model model) {
		model.addAttribute("prodotti", this.prodottoService.findAllProdotti());
		model.addAttribute("fornitori", this.fornitoreService.getAllFornitori());
		return "admin/manageProdotti";
	}

	//metodo che crea un nuovo prodotto
	@PostMapping("/admin/manageProdotti")
	public String newProdotto(@Valid @ModelAttribute("prodotto")Prodotto prodotto , BindingResult bindingResult , @RequestParam("prodottoImage") MultipartFile multipartFile, Model model) throws IOException {
		this.prodottoValidator.validate(prodotto, bindingResult);
		if(!bindingResult.hasErrors()) {
			model.addAttribute("prodotto", this.prodottoService.createNewProdotto(prodotto, multipartFile));
			return "redirect:/admin/manageProdotti";
		}else {
			model.addAttribute("messaggioErrore", "Questo prodotto esiste già");
			return "admin/formNewProdotto";
		}

	}

	//metodo per creare un nuovo prodotto
	@GetMapping(value="/admin/formNewProdotto")
	public String formNewProdotto(Model model) {
		model.addAttribute("prodotto", new Prodotto());
		return "admin/formNewProdotto";
	}




	//metodo per modificare un prodotto
	@GetMapping(value="/admin/formUpdateProdotto/{prodottoId}")
	public String formUpdateProdotto(@PathVariable("prodottoId") Long prodottoId, Model model) {
		Prodotto prodotto = this.prodottoService.findProdottoById(prodottoId);
		if(prodotto!=null) {
			model.addAttribute("prodotto", prodotto);
			return "admin/formUpdateProdotto";
		
		}else {
			return "prodottoError";
		}
	}

	//metodo che modifica un prodotto
	@PostMapping("/admin/manageProdotti/{prodottoId}")
	public String updateProdotto(@PathVariable("prodottoId") Long prodottoId , @ModelAttribute("prodotto") Prodotto prodotto){
		//get prodotto from database by id
		Prodotto exisistinProdotto = new Prodotto();
		exisistinProdotto = this.prodottoService.findProdottoById(prodottoId);
		exisistinProdotto.setId(prodottoId);
		exisistinProdotto.setNome(prodotto.getNome());
		exisistinProdotto.setDescrizione(prodotto.getDescrizione());
		exisistinProdotto.setPrezzo(prodotto.getPrezzo());
		exisistinProdotto.setImage(prodotto.getImage());

		//save updated product Object
		this.prodottoService.saveProdotto(exisistinProdotto);
		return "redirect:/admin/manageProdotti";
	}

	//metodo per eliminare un prodotto
	@GetMapping(value="/admin/manageProdotti/{prodottoId}")
	public String RemoveProdotto(@PathVariable("prodottoId") Long prodottoId, Model model) {
		this.prodottoService.deleteProdotto(prodottoId);
		return  "admin/manageProdotti";
	}


	/*metodo per visualizzare tutti i fornitori di un prodotto
	@GetMapping(value = "/admin/manageProdotti/{prodottoId}")
	public String ElencoFornitoriProdotto (@PathVariable("prodottoId") Long prodottoId, Model model){
		model.addAttribute("elencoFornitori", this.prodottoService.elencoFornitoriProdottoById(prodottoId));
		return "/admin/elencoFornitori";

	}

	//metodo per aggiungere un fornitore all'elenco di fornitori di un prodotto
	@GetMapping(value="admin/formNewFornitore/{fornitoreId}/{prodottoId}")
	public String addFornitoreToProdotto(@PathVariable("fornitoreId")Long fornitoreId , @PathVariable("prodottoId") Long prodottoId, Model model) {
		Prodotto prodotto = this.prodottoService.addFornitoreToProdotto(fornitoreId, prodottoId);
		if(prodotto!=null) {
			model.addAttribute("prodotto", prodotto);
			return "admin/elencoFornitori";
		}
		else {
			return "prodottoError";
		}
	}*/
	//metodo per rimuovere un fornitore dall'elenco di fornitori di un prodotto

	/*@GetMapping(value = "/admin/elencoFornitori/{fornitoreId}/{prodottoId}")
	public String removeFornitoreToProdotto(@PathVariable("prodottoId") Long prodottoId , @PathVariable("fornitoreId") Long fornitoreId, Model model ){
		Prodotto prodotto = this.prodottoService.deleteFornitoreFromProdotto(prodottoId,fornitoreId);
		if(prodotto!=null){
			List<Fornitore> fornitoriToAdd= fornitoreService.findFornitoriNotInProdotto(prodottoId);
			model.addAttribute("prodotto",prodotto);
			model.addAttribute("fornitoriToAdd" , fornitoriToAdd);
			return "admin/elencoFornitori";
		}
		else{
			return "prodottoError";
		}
	}
	
	@GetMapping(value= "/admin/addFornitore/{fornitoreId}")
	public String addFornitore(@PathVariable("fornitoreId") Long fornitoreId, Model model) {
		model.addAttribute("fornitori", this.fornitoreService.findAllFornitori() );
		Prodotto prodotto = this.prodottoService.findProdottoById(fornitoreId);
		if(prodotto!=null) {
			model.addAttribute("prodotto", prodotto);
			return "admin/fornitoriToAdd";
		}else {
			return "prodottoError";
		}
	}

	@GetMapping("admin/updateFornitori/{prodottoId}")
	public String updateFornitori(@PathVariable("prodottoId") Long prodottoId, Model model){
		List<Fornitore> fornitoriToAdd= this.fornitoreService.findFornitoriNotInProdotto(prodottoId);
		model.addAttribute("fornitoriToAdd", fornitoriToAdd);
		Prodotto prodotto= this.prodottoService.findProdottoById(prodottoId);
		if(prodotto!=null){
			model.addAttribute("prodotto", prodotto);
			return "admin/fornitoriToAdd";
		}else{
			return "prodottoError";
		}
	}

*/
	
	
	

}
