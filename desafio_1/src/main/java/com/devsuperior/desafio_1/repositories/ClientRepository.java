package com.devsuperior.desafio_1.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.devsuperior.desafio_1.entity.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
	 Client findByName(String name);

}
