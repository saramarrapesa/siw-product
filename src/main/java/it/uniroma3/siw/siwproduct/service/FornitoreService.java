package it.uniroma3.siw.siwproduct.service;

import it.uniroma3.siw.siwproduct.model.Fornitore;
import it.uniroma3.siw.siwproduct.repository.FornitoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import jakarta.transaction.Transactional;

import java.util.*;

@Service
public class FornitoreService {
	
	@Autowired
	public FornitoreRepository fornitoreRepository;
	
	@Transactional
	public Fornitore createFornitore(Fornitore fornitore){
		this.fornitoreRepository.save(fornitore);
		return fornitore;
	}

	public Iterable<Fornitore> getAllFornitori(){
		return this.fornitoreRepository.findAll();
	}
	
/*	@Transactional
	public List<Fornitore> findFornitoriNotInProdotto(Long prodottoId){
		List<Fornitore> fornitoriToAdd = new ArrayList<>();
		for(Fornitore f : this.fornitoreRepository.findFornitoriNotInProdotto(prodottoId)) {
			fornitoriToAdd.add(f);
		}
		return fornitoriToAdd;
	}
*/
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

}
