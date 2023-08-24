package it.uniroma3.siw.siwproduct.validator;

import it.uniroma3.siw.siwproduct.model.Prodotto;
import it.uniroma3.siw.siwproduct.repository.ProdottoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;



@Component
public class ProdottoValidator implements Validator {

	@Autowired
	public ProdottoRepository prodottoRepository;
	
	@Override
	public boolean supports(Class<?> clazz) {
		return Prodotto.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
        Prodotto prodotto = (Prodotto)target;
        if(prodotto.getNome()!=null && prodottoRepository.existsByNome(prodotto.getNome()))
        		errors.reject("prodotto.duplicate");
        		
        	
	}

}
