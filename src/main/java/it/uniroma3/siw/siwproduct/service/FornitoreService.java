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
	public Fornitore createNewFornitore(Fornitore fornitore) {
	
		return this.fornitoreRepository.save(fornitore);
	}
	
	@Transactional
	public Fornitore saveFornitore (Fornitore fornitore) {
		return this.fornitoreRepository.save(fornitore);
	}
	
	public Iterable<Fornitore> findAllForniori(){
		return this.fornitoreRepository.findAll();
	}
	
	@Transactional
	public List<Fornitore> findFornitoriInProdotto(Long prodottoId){
		List<Fornitore> fornitoriToAdd = new ArrayList<>();
		for(Fornitore f : this.fornitoreRepository.findFornitoriNotInProdotto(prodottoId)) {
			fornitoriToAdd.add(f);
		}
		return fornitoriToAdd;
	}
	
	public Fornitore findFornitoreById(Long id) {
		return this.fornitoreRepository.findById(id).orElse(null);
	}

}
