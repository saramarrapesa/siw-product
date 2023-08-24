package it.uniroma3.siw.siwproduct.repository;

import java.util.Optional;

import it.uniroma3.siw.siwproduct.model.Credentials;
import org.springframework.data.repository.CrudRepository;


public interface CredentialsRepository extends CrudRepository<Credentials, Long> {

	public Optional<Credentials> findByUsername(String username);
}
