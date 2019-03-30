package com.bolsa.factura.app.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bolsa.factura.app.models.dao.IClienteDao;
import com.bolsa.factura.app.models.dao.IFacturaDao;
import com.bolsa.factura.app.models.dao.IProductoDao;
import com.bolsa.factura.app.models.entity.Cliente;
import com.bolsa.factura.app.models.entity.Factura;
import com.bolsa.factura.app.models.entity.Producto;

@Service
public class ClienteServiceImpl implements IClienteService {
	@Autowired
	// Permite dar un nombre para seleccionar el bean concreto para una
	// implementacion
	// @Qualifier("clienteDaoJPA")
	private IClienteDao clienteDao;

	@Autowired
	private IProductoDao productoDao;

	@Autowired
	private IFacturaDao facturaDao;

	@Transactional(readOnly = true)
	@Override
	public List<Cliente> findAll() {
		return (List<Cliente>) clienteDao.findAll();
	}

	@Transactional // Sin readOnly porque es de escritura
	@Override
	public void save(Cliente cliente) {
		clienteDao.save(cliente);
	}

	@Transactional(readOnly = true)
	@Override
	public Cliente findById(Long id) {
		return clienteDao.findById(id).orElse(null);
	}

	@Transactional
	@Override
	public void delete(Long id) {
		clienteDao.deleteById(id);

	}

	@Override
	public Page<Cliente> findAll(Pageable pageable) {
		return clienteDao.findAll(pageable);
	}

	@Override
	public List<Producto> findByName(String term) {
		// return productoDao.findByName(term);

		return productoDao.findByNombreLikeIgnoreCase("%" + term + "%");
	}

	@Override
	@Transactional
	public void saveFactura(Factura factura) {
		facturaDao.save(factura);

	}

	@Override
	@Transactional(readOnly = true)
	public Producto findProductoById(long id) {

		return productoDao.findById(id).orElse(null);
	}

	@Override
	@Transactional(readOnly = true)
	public Factura findFacturaById(long id) {
		return facturaDao.findById(id).orElse(null);
	}

	@Override
	public void deleteFactura(long id) {
		facturaDao.deleteById(id);
	}

	@Override
	@Transactional(readOnly=true)
	public Factura fetchFacturaByIdWithClienteWithItemFacturaWithProduco(Long id) {
		return facturaDao.fetchByIdWithClienteWithItemFacturaWithProduco(id);
	}

	@Override
	@Transactional(readOnly=true)
	public Cliente fetchClienteByIdWithFactura(Long id) {
		return clienteDao.fetchByIdWithFactura(id);
	}

}
