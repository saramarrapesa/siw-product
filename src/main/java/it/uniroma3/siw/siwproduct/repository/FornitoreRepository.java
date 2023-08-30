package it.uniroma3.siw.siwproduct.repository;

import it.uniroma3.siw.siwproduct.model.Fornitore;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;




public interface FornitoreRepository extends CrudRepository<Fornitore, Long> {
	
	@Query(value="select * "
			+ "from fornitore f "
			+ "where f.id not in "
			+ "(select fornitori_id "
			+ "from fornitori_prodotti "
			+ "where fornitori_prodotti.starred_prodotti_id = :prodottoId)", nativeQuery=true)

	 Iterable<Fornitore> findFornitoriNotInProdotto(@Param("prodottoId") Long prodottoId);
	 boolean existsByNomeAndEmail(String nome, String email);

}
