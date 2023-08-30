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

	@Transactional
	public Prodotto createNewProdotto(Prodotto prodotto , MultipartFile multipartFile) throws IOException {
		Image imageProdotto = new Image(multipartFile.getBytes());
		this.imageRepository.save(imageProdotto);
		prodotto.setImage(imageProdotto);
		return this.prodottoRepository.save(prodotto);
	}
	@Transactional
	public void updateProdotto(Prodotto prodotto) {
		this.prodottoRepository.save(prodotto);
	}

	@Transactional
	public void deleteProdotto(Long prodottoId) {
		this.prodottoRepository.deleteById(prodottoId);
	}

	@Transactional
	public Prodotto findProdottoById(Long prodottoId) {
		return this.prodottoRepository.findById(prodottoId).orElse(null);
	}
	
	public Prodotto saveProdotto(Prodotto prodotto) {
		return this.prodottoRepository.save(prodotto);
	}
	

	@Transactional
	public Iterable<Prodotto> findAllProdotti(){
		return this.prodottoRepository.findAll();
	}
	
	//metodo per aggiungere un fornitore alla lista dei fornitori di un prodotto 
	public Prodotto addFornitoreToProdotto(Long prodottoId, Long fornitoreId) {
		Fornitore fornitore= this.fornitoreRepository.findById(fornitoreId).orElse(null);
		Prodotto prodotto = this.prodottoRepository.findById(prodottoId).orElse(null);
		if(prodotto!=null&&fornitore!=null) {
			List<Fornitore> fornitori= prodotto.getFornitori();
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
			List<Fornitore> fornitori = prodotto.getFornitori();
			fornitori.remove(fornitore);
			prodotto.setFornitori(fornitori);
		}
		return this.prodottoRepository.save(prodotto);
	}


	//ogni utente può scrivere una recensione sul prodotto
	
	public String function (Model model , Prodotto prodotto , String username) {
		List<Fornitore> fornitoriProdotto = new ArrayList<>();
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
		
		return "prodotto.html";
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
