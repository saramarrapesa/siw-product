package it.uniroma3.siw.siwproduct.repository;

import it.uniroma3.siw.siwproduct.model.Prodotto;
import org.springframework.data.repository.CrudRepository;

import java.util.List;



public interface ProdottoRepository extends CrudRepository<Prodotto, Long> {

	boolean existsByNome(String nome);

  //  Prodotto findByNomeProdotto(String cerca);
}
