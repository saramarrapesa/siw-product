package it.uniroma3.siw.siwproduct.controller;

import it.uniroma3.siw.siwproduct.model.Prodotto;
import it.uniroma3.siw.siwproduct.model.Review;
import it.uniroma3.siw.siwproduct.service.ProdottoService;
import it.uniroma3.siw.siwproduct.service.ReviewService;
import it.uniroma3.siw.siwproduct.validator.ReviewValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


import jakarta.validation.Valid;

@Controller
public class ReviewController {
	
	@Autowired
	private ReviewService reviewService;
	
	@Autowired
	private ReviewValidator reviewValidator;
	
	@Autowired
	private ProdottoService prodottoService;
	
	@Autowired
	private GlobalController globalController;
	
	@PostMapping("/user/uploadReview/{prodottoId}")
	public String newReview(@Valid @ModelAttribute("review") Review review , BindingResult bindingResult , Model model , @PathVariable("prodottoId") Long prodottoId) {
		this.reviewValidator.validate(review, bindingResult);
		if(!bindingResult.hasErrors()) {
			Prodotto prodotto = this.prodottoService.findProdottoById(prodottoId);
			if(this.globalController.getUser()!=null && !prodotto.getReviews().contains(review)) {
				review.setUsername(this.globalController.getUser().getUsername());
				this.reviewService.saveReview(review);
				prodotto.getReviews().add(review);
			}
			this.prodottoService.saveProdotto(prodotto);
			return this.prodottoService.function(model, prodotto, this.globalController.getUser().getUsername());
		}else {
		return "prodottoError";
		}
     }
	
	@GetMapping("/admin/deleteReview/{prodottoId}/{reviewId}")
	public String removeReview(Model model, @PathVariable("prodottoId") Long prodottoId,@PathVariable("reviewId") Long reviewId) {
		Prodotto prodotto = this.prodottoService.findProdottoById(prodottoId);
		Review review = this.reviewService.findById(reviewId);
		
		prodotto.getReviews().remove(review);
		this.reviewService.deleteReview(review);
		this.prodottoService.saveProdotto(prodotto);
		return this.prodottoService.function(model, prodotto, this.globalController.getUser().getUsername());
	}


 /*
@Autowired
private ReviewService commentoService;

	@Autowired
	private ProdottoService prodottoService;

	@Autowired
	private ReviewValidator commentoValidator;

	@Autowired
	private GlobalController globalController;

/*
	@PostMapping("/addCommento/{id}")
	public String addCommentoToProdotto(@Valid @ModelAttribute("commento") Review commento, BindingResult bindingResult, Model model,@PathVariable("id") Long id){

		//commento.setAutore(this.globalController.getUser().getUsername());
		Prodotto prodotto = this.prodottoService.findProdottoById(id);
		this.commentoValidator.validate(commento, bindingResult);
		this.prodottoService.addCommento(prodotto,this.commentoService.creaCommento(commento));

		model.addAttribute("prodotto", prodotto);
		model.addAttribute("commentato", this.prodottoService.commentato(id));
		model.addAttribute("user", this.globalController.getUser());
		model.addAttribute("commento", new Review());
		return "prodotto";
	}*/

	/*@GetMapping("/removeCommento/{prodottoId}/{commentoId}")
	public String removeCommentoToProdotto(@PathVariable("prodottoId") Long prodottoId, @PathVariable("commentoId") Long commentoId, Model model) {

		model.addAttribute("prodotto", this.prodottoService.removeCommento(prodottoId, commentoId));
		this.commentoService.deleteCommento(commentoId);
		model.addAttribute("commentato", this.prodottoService.commentato(prodottoId));
		model.addAttribute("user", this.globalController.getUser());
		model.addAttribute("commento", new Review());
		return "prodotto";
	}*/
}
