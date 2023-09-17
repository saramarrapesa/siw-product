package it.uniroma3.siw.siwproduct.service;

import it.uniroma3.siw.siwproduct.model.Fornitore;
import it.uniroma3.siw.siwproduct.model.Prodotto;
import it.uniroma3.siw.siwproduct.repository.FornitoreRepository;
import it.uniroma3.siw.siwproduct.repository.ProdottoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import jakarta.transaction.Transactional;

import java.util.*;

@Service
public class FornitoreService {
	
	@Autowired
	public FornitoreRepository fornitoreRepository;
	@Autowired
	public ProdottoRepository prodottoRepository;
	
	@Transactional
	public Fornitore createFornitore(Fornitore fornitore){
		this.fornitoreRepository.save(fornitore);
		return fornitore;
	}

	public Iterable<Fornitore> getAllFornitori(){
		return this.fornitoreRepository.findAll();
	}
	
	@Transactional
	public List<Fornitore> findFornitoriNotInProdotto(Long prodottoId){
		List<Fornitore> fornitoriToAdd = new ArrayList<>();
		for(Fornitore f : this.fornitoreRepository.findFornitoriNotInProdotto(prodottoId)) {
			fornitoriToAdd.add(f);
		}
		return fornitoriToAdd;
	}

	//metodo per aggiornare un prodotto
	@Transactional
	public void saveFornitore(Fornitore fornitore) {
		this.fornitoreRepository.save(fornitore);
	}
	//metodo per rimuovere un prodotto
	@Transactional
	public void deleteFornitore(Long fornitoreId) {
		this.fornitoreRepository.deleteById(fornitoreId);
	}
	
	public Fornitore findFornitoreById(Long id) {
		return this.fornitoreRepository.findById(id).orElse(null);
	}

	@Transactional
	public Fornitore addProdotto (Long fornitoreId, Long prodottoId){

		Fornitore fornitore = this.fornitoreRepository.findById(fornitoreId).get();
		Prodotto prodotto = this.prodottoRepository.findById(prodottoId).get();

		Set<Prodotto> prodotti = fornitore.getProdotti();
		prodotti.add(prodotto);
		fornitore.setProdotti(prodotti);
		return this.fornitoreRepository.save(fornitore);
	}

	@Transactional
	public Fornitore removeProdotto (Long fornitoreId, Long prodottoId){

		Fornitore fornitore = this.fornitoreRepository.findById(fornitoreId).get();
		Prodotto prodotto = this.prodottoRepository.findById(prodottoId).get();

		Set <Prodotto> prodotti = fornitore.getProdotti();
		prodotti.remove (prodotto);
		fornitore.setProdotti(prodotti);

		return this.fornitoreRepository.save(fornitore);
	}
	/*


	private Long rimuoviProdotti(Long id){
		List<Prodotto> prodotti = this.fornitoreRepository.findById(id).get().getProdotti();
		for (Prodotto prodotto : prodotti) {
			this.prodottoService.removeFornitore(prodotto.getId(), id);
			this.prodottoRepository.save(prodotto);
		}
		this.fornitoreRepository.findById(id);
		return id;
	}


*/
}
