package it.uniroma3.siw.siwproduct.repository;

import it.uniroma3.siw.siwproduct.model.Prodotto;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;



public interface ProdottoRepository extends CrudRepository<Prodotto, Long> {

	boolean existsByNome(String nome);

    List<Prodotto> findByNome(String nome);
}
