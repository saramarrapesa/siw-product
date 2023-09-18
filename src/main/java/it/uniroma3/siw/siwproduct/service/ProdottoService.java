package it.uniroma3.siw.siwproduct.service;

import java.io.IOException;
import java.util.*;

import it.uniroma3.siw.siwproduct.model.Fornitore;
import it.uniroma3.siw.siwproduct.model.Image;
import it.uniroma3.siw.siwproduct.model.Prodotto;
import it.uniroma3.siw.siwproduct.model.Review;
import it.uniroma3.siw.siwproduct.repository.FornitoreRepository;
import it.uniroma3.siw.siwproduct.repository.ImageRepository;
import it.uniroma3.siw.siwproduct.repository.ProdottoRepository;
import it.uniroma3.siw.siwproduct.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;


import jakarta.transaction.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ProdottoService {
	
	@Autowired
	private ProdottoRepository prodottoRepository;
	
	@Autowired
	private FornitoreRepository fornitoreRepository;

	@Autowired
	private ImageRepository imageRepository;
	@Autowired
	private ReviewRepository reviewRepository;

	@Transactional
	public Prodotto createNewProdotto(Prodotto prodotto , MultipartFile multipartFile) throws IOException {
		Image imageProdotto = new Image(multipartFile.getBytes());
		this.imageRepository.save(imageProdotto);
		prodotto.setImage(imageProdotto);
		return this.prodottoRepository.save(prodotto);
	}
	@Transactional
	public void deleteProdotto(Long prodottoId) {
		this.prodottoRepository.deleteById(prodottoId);
	}

	@Transactional
	public Prodotto findProdottoById(Long prodottoId) {
		return this.prodottoRepository.findById(prodottoId).orElse(null);
	}
	
	public void saveProdotto(Prodotto prodotto) {
		this.prodottoRepository.save(prodotto);
	}

	//public Prodotto findProdottoByNome(String nome){return this.prodottoRepository.findByNomeProdotto(nome);}

	@Transactional
	public Iterable<Prodotto> findAllProdotti(){
		return this.prodottoRepository.findAll();
	}
	
	//metodo per aggiungere un fornitore alla lista dei fornitori di un prodotto 
	public Prodotto addFornitoreToProdotto(Long prodottoId, Long fornitoreId) {
		Fornitore fornitore= this.fornitoreRepository.findById(fornitoreId).orElse(null);
		Prodotto prodotto = this.prodottoRepository.findById(prodottoId).orElse(null);
		if(prodotto!=null&&fornitore!=null) {
			Set<Fornitore> fornitori= prodotto.getFornitori();
			fornitori.add(fornitore);
			prodotto.setFornitori(fornitori);
		}
		return this.prodottoRepository.save(prodotto);
		
	}

	
	//metodo per rimuovere un fornitore dalla lista dei fornitori di un prodotto
	public Prodotto deleteFornitoreFromProdotto(Long prodottoId, Long fornitoreId) {
		Prodotto prodotto = this.prodottoRepository.findById(prodottoId).orElse(null);
		Fornitore fornitore = this.fornitoreRepository.findById(fornitoreId).orElse(null);
		if(prodotto!=null && fornitore!=null) {
			Set<Fornitore> fornitori = prodotto.getFornitori();
			fornitori.remove(fornitore);
			prodotto.setFornitori(fornitori);
		}
		return this.prodottoRepository.save(prodotto);
	}


	//ogni utente può scrivere una recensione sul prodotto
	
	public void function (Model model , Prodotto prodotto , String username) {
		Set<Fornitore> fornitoriProdotto = new HashSet<>();
		if(prodotto.getFornitori()!=null)
			fornitoriProdotto.addAll(prodotto.getFornitori());
		fornitoriProdotto.remove(null);
		model.addAttribute("fonitoriProdotto", fornitoriProdotto);
		model.addAttribute("prodotto", prodotto);
		if(username!=null && this.alreadyReviewed(prodotto.getReviews(), username))
			model.addAttribute("hasNotAlreadyCommented", false);
		else
			model.addAttribute("hasNotAlreadyCommented", true);
		model.addAttribute("review", new Review());
		model.addAttribute("reviews", prodotto.getReviews());
		model.addAttribute("hasReviews", !prodotto.getReviews().isEmpty());

		}
	



	@Transactional
	public boolean alreadyReviewed(Set<Review> reviews , String username){
		if(reviews != null) {
			for(Review rev : reviews) {
				if(rev.getUsername().equals(username))
					return true;
			}
		}
		return false;
	}
}


	//ALESSANDRO
	/*

	@Transactional
	public void addCommento (Prodotto prodotto,Review commento){
		Set<Review> commenti = prodotto.getReviews();
		commenti.add(commento);
		prodotto.setReviews(commenti);
		this.prodottoRepository.save(prodotto);
	}

	public Prodotto addCommento(Long prodottoId,Long commentoId){
		Prodotto prodotto = this.prodottoRepository.findById(prodottoId).get();
		Review commento = this.commentiRepository.findById(commentoId).get();

		Set<Review> commenti = prodotto.getReviews();
		commenti.add(commento);
		prodotto.setReviews(commenti);
		return this.prodottoRepository.save(prodotto);
	}

	public Prodotto removeCommento(Long prodottoId,Long commentoId){

		Prodotto prodotto = this.prodottoRepository.findById(prodottoId).get();
		Review commento = this.commentiRepository.findById(commentoId).get();

		Set<Review> commenti = prodotto.getReviews();
		commenti.remove(commento);
		prodotto.setReviews(commenti);
		return this.prodottoRepository.save(prodotto);
	}

	@Transactional
	public boolean commentato (Long prodottoId){
		return this.verifica(this.findProdottoById(prodottoId));
	}

	/*private boolean verifica(Prodotto prodotto){
		Set<Review> commenti = prodotto.getReviews();
		if (this.globalController.getUser()==null) {
			return false;
		}
		String username = this.globalController.getUser().getUsername();
		for (Review c : commenti) {
			if (c.getUsername().equals(username)) {
				return false;
			}
		}
		return true;
	}*/


