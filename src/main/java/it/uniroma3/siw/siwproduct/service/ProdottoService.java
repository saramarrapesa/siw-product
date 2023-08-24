package it.uniroma3.siw.siwproduct.service;

import java.util.*;

import it.uniroma3.siw.siwproduct.model.Fornitore;
import it.uniroma3.siw.siwproduct.model.Prodotto;
import it.uniroma3.siw.siwproduct.model.Review;
import it.uniroma3.siw.siwproduct.repository.FornitoreRepository;
import it.uniroma3.siw.siwproduct.repository.ProdottoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;


import jakarta.transaction.Transactional;

@Service
public class ProdottoService {
	
	@Autowired
	private ProdottoRepository prodottoRepository;
	
	@Autowired
	private FornitoreRepository fornitoreRepository;
	
	
	//metodo per inserire un nuovo prodotto
	@Transactional
	public Prodotto createNewProdotto(Prodotto p) {
		return this.prodottoRepository.save(p);
	}
	
	//metodo per aggiornare un prodotto
	@Transactional
	public void updateProdotto(Prodotto prodotto) {
		this.prodottoRepository.save(prodotto);
	}
	//metodo per rimuovere un prodotto
	@Transactional
	public void deleteProdotto(Prodotto prodotto) {
		this.prodottoRepository.delete(prodotto);
	}
	
	//metodo per trovare uno specifico prodotto 
	@Transactional
	public Prodotto findProdottoById(Long prodottoId) {
		return this.prodottoRepository.findById(prodottoId).orElse(null);
	}
	
	public Prodotto saveProdotto(Prodotto prodotto) {
		return this.prodottoRepository.save(prodotto);
	}
	
	//metodo per trovare tutti i prodotti
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
			this.prodottoRepository.save(prodotto);
		}
		return prodotto;
		
	}
	
	//metodo per rimuovere un fornitore dalla lista dei fornitori di un prodotto
	public Prodotto deleteFornitoreFromProdotto(Long prodottoId, Long fornitoreId) {
		Prodotto prodotto = this.prodottoRepository.findById(prodottoId).orElse(null);
		Fornitore fornitore = this.fornitoreRepository.findById(fornitoreId).orElse(null);
		if(prodotto!=null && fornitore!=null) {
			List<Fornitore> fornitori = prodotto.getFornitori();
			fornitori.remove(fornitore);
			this.prodottoRepository.save(prodotto);
		}
		return prodotto;
	}
	
	public Prodotto findProdottoByNome(String nome) {
		return this.prodottoRepository.findByNome(nome).get(0);
	}
	
	//ogni utente può scrivere una recensione sul prodotto
	
	public String function (Model model , Prodotto prodotto , String username) {
		List<Fornitore> fornitoriProdotto = new ArrayList<>();
		if(prodotto.getFornitori()!=null)
			fornitoriProdotto.addAll(prodotto.getFornitori());
		fornitoriProdotto.remove(null);
		model.addAttribute("fonitoriProdotto", fornitoriProdotto);
		model.addAttribute("prodotto", prodotto);
		if(username!=null && this.alreadyRewied(prodotto.getReviews(), username))
			model.addAttribute("hasNotAlreadyCommented", false);
		else
			model.addAttribute("hasNotAlreadyCommented", true);
		model.addAttribute("reviw", new Review());
		model.addAttribute("reviws", prodotto.getReviews());
		model.addAttribute("hasRviws", !prodotto.getReviews().isEmpty());
		
		return "prodotto.html";
		}
	
	
	@Transactional
	public boolean alreadyRewied(Set<Review> reviews , String username){
		if(reviews != null) {
			for(Review rev : reviews) {
				if(rev.getUsername().equals(username))
					return true;
			}
		}
		return false;
	}
		
	

}
