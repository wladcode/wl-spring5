package com.bolsa.factura.app.models.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.bolsa.factura.app.models.entity.Factura;

public interface IFacturaDao extends CrudRepository<Factura, Long>{
	
	@Query("select f from Factura f join fetch f.cliente c join fetch f.itemFacturas it join fetch it.producto where f.id=?1")
	public Factura fetchByIdWithClienteWithItemFacturaWithProduco(Long id);
	

}
