package com.devsuperior.desafio_1.services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.desafio_1.DTOs.ClientDTO;
import com.devsuperior.desafio_1.entity.Client;
import com.devsuperior.desafio_1.repositories.ClientRepository;
import com.devsuperior.desafio_1.services.exception.DatabaseException;
import com.devsuperior.desafio_1.services.exception.ResourceNotFoundException;

@Service
public class ClientService {

	@Autowired
	private ClientRepository repository;

	// Busca paginada

	@Transactional(readOnly = true)
	public Page<ClientDTO> findAllPaged(PageRequest pageRequest) {
		Page<Client> list = repository.findAll(pageRequest);
		return list.map(x -> new ClientDTO(x));
	}

	// Busca por id do cliente. Não encontrado, tratamos a exceção.

	@Transactional(readOnly = true)
	public ClientDTO findById(Long id) {
		Optional<Client> obj = repository.findById(id);
		Client entity = obj.orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado!"));
		return new ClientDTO(entity);
	}

	@Transactional
	public ClientDTO insert(ClientDTO dto) {
		Client entity = new Client();
		copyDtoToEntity(dto, entity);
		entity = repository.save(entity);
		return new ClientDTO(entity);
	}

	public ClientDTO update(ClientDTO dto, Long id) {
		try {
			Client entity = repository.getOne(id);
			copyDtoToEntity(dto, entity);
			entity = repository.save(entity);
			return new ClientDTO(entity);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id não encontrado! " + id);
		}
	}

	public void delete(Long id) {
		try {
			repository.deleteById(id);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id não encontrado! " + id);
		} catch (DataIntegrityViolationException e) {
			throw new DatabaseException("Integrity violation");
		}
	}

	private void copyDtoToEntity(ClientDTO clientDTO, Client clientEntity) {
		clientEntity.setName(clientDTO.getName());
		clientEntity.setCpf(clientDTO.getCpf());
		clientEntity.setIncome(clientDTO.getIncome());
		clientEntity.setBirthDate(clientDTO.getBirthDate());
		clientEntity.setChildren(clientDTO.getChildren());

	}

}
