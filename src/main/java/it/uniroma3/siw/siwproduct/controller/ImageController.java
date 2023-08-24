package it.uniroma3.siw.siwproduct.controller;

import it.uniroma3.siw.siwproduct.model.Image;
import it.uniroma3.siw.siwproduct.model.Prodotto;
import it.uniroma3.siw.siwproduct.service.ImageService;
import it.uniroma3.siw.siwproduct.service.ProdottoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;


@Controller
public class ImageController {
	
	@Autowired
    private ImageService imageService;
	
	@Autowired
	private ProdottoService prodottoService;
	

    @GetMapping("/image/{id}")
    public ResponseEntity<byte[]> displayItemImage(@PathVariable("id") Long id) {
        Image img = this.imageService.findById(id);
        byte[] image = img.getBytes();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        return new ResponseEntity<>(image, headers, HttpStatus.OK);
    }	

	@PostMapping("/admin/updateProdottoImages/{prodottoId}")
	public String addImageToMovie(@PathVariable("prodottoId") Long prodottoId, @RequestParam("prodottoImage") MultipartFile[] multipartFile, Model model) {
		Prodotto prodotto = this.prodottoService.findProdottoById(prodottoId);
		Set<Image> immagini = new HashSet<>();
		try {
			
			for(MultipartFile file : multipartFile)
				immagini.add(imageService.saveImage(new Image(file.getBytes())));
			
        }
        catch (IOException e){}
		prodotto.addImages(immagini);
		this.prodottoService.saveProdotto(prodotto);
		model.addAttribute("prodotto", prodotto);
		return "/admin/formUpdateProdotto.html";

	}

	@GetMapping("/admin/deleteImage/{prodottoId}/{imageId}")
	public String removeImage(@PathVariable("prodottoId") Long prodottoId,@PathVariable("imageId") Long imageId, Model model){
		Prodotto prodotto = this.prodottoService.findProdottoById(prodottoId);
		Image image = this.imageService.findById(imageId);

		prodotto.getImages().remove(image);
		this.imageService.deleteImage(image);
		this.prodottoService.saveProdotto(prodotto);
		model.addAttribute("prodotto", prodotto);
		return "/admin/formUpdateProdotto.html";
	}

}
