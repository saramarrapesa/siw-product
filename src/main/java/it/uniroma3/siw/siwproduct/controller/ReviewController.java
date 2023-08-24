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
		return "prodottoError.html";
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
}
