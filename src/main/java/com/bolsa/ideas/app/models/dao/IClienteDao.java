package com.bolsa.ideas.app.models.dao;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.bolsa.ideas.app.models.entity.Cliente;

public interface IClienteDao extends PagingAndSortingRepository<Cliente, Long> {

}
