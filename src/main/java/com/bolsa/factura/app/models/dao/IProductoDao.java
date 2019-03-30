package com.bolsa.factura.app.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.bolsa.factura.app.models.entity.Producto;

public interface IProductoDao extends CrudRepository<Producto, Long> {

	@Query("select p from Producto p where p.nombre like %?1%")
	public List<Producto> findByName(String term);

	
	// Ver documentancion ya que solo se necesita de findBy<NOMBRE DE ATRIBUTO DE LA CLASE>LikeIgnoreCase 
	public List<Producto> findByNombreLikeIgnoreCase(String term);
}
