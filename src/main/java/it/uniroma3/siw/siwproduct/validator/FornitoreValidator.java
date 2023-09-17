package it.uniroma3.siw.siwproduct.validator;

import it.uniroma3.siw.siwproduct.model.Fornitore;
import it.uniroma3.siw.siwproduct.repository.FornitoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;



@Component
public class FornitoreValidator implements Validator {

	@Autowired
	private FornitoreRepository fornitoreRepository;
	
	@Override
	public boolean supports(Class<?> clazz) {
		return Fornitore.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {

		Fornitore fornitore = (Fornitore) target;
		if(fornitore.getNome()!=null && fornitore.getEmail()!=null && fornitoreRepository.existsByNomeAndEmail(fornitore.getNome(), fornitore.getEmail()))
			errors.reject("fornitore.duplicate");
		}

}
