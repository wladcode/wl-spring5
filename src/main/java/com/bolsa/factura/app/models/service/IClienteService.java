package com.bolsa.factura.app.models.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.bolsa.factura.app.models.entity.Cliente;
import com.bolsa.factura.app.models.entity.Factura;
import com.bolsa.factura.app.models.entity.Producto;

public interface IClienteService {

	public List<Cliente> findAll();

	public Page<Cliente> findAll(Pageable pageable);

	public void save(Cliente cliente);

	public Cliente findById(Long id);
	
	public Cliente fetchClienteByIdWithFactura(Long id);


	public void delete(Long id);
	
	public List<Producto> findByName(String term);
	
	public void saveFactura(Factura factura);
	
	public Producto findProductoById(long id);
	
	public Factura findFacturaById(long id);
	
	public void deleteFactura(long id);
	
	
	public Factura fetchFacturaByIdWithClienteWithItemFacturaWithProduco(Long id);


}
