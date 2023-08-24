package it.uniroma3.siw.siwproduct.repository;

import it.uniroma3.siw.siwproduct.model.User;
import org.springframework.data.repository.CrudRepository;


public interface UserRepository extends CrudRepository<User, Long> {

}
