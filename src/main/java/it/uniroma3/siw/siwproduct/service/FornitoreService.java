package it.uniroma3.siw.siwproduct.service;

import it.uniroma3.siw.siwproduct.model.Fornitore;
import it.uniroma3.siw.siwproduct.model.Image;
import it.uniroma3.siw.siwproduct.repository.FornitoreRepository;
import it.uniroma3.siw.siwproduct.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import jakarta.transaction.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
public class FornitoreService {
	
	@Autowired
	public FornitoreRepository fornitoreRepository;

	@Autowired
	public ImageRepository imageRepository;
	
	@Transactional
	public Fornitore createNewFornitore(Fornitore fornitore, MultipartFile multipartFile) throws IOException {

		try{
			fornitore.setPicture(imageRepository.save(new Image(multipartFile.getBytes())));
		}
		catch (IOException e){}
		this.fornitoreRepository.save(fornitore);
		return fornitore;
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
