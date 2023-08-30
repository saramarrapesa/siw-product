package it.uniroma3.siw.siwproduct.service;

import it.uniroma3.siw.siwproduct.model.Image;
import it.uniroma3.siw.siwproduct.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class ImageService {

	@Autowired
	private ImageRepository imageRepository;

	public void salvaImmagine(Image immagine){
		this.imageRepository.save(immagine);
	}

	public Image findImmagineById (Long id){
		return this.imageRepository.findById(id).get();
	}
}
