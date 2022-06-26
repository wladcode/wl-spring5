package com.bolsa.factura.app.controller;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bolsa.factura.app.models.entity.Cliente;
import com.bolsa.factura.app.models.entity.Factura;
import com.bolsa.factura.app.models.entity.ItemFactura;
import com.bolsa.factura.app.models.entity.Producto;
import com.bolsa.factura.app.models.service.IClienteService;
import com.bolsa.factura.app.util.Utils;

@Secured(Utils.ROLE_ADMIN)
@Controller
@RequestMapping("/factura")
@SessionAttributes("factura")
public class FacturaController {

	private final Logger LOG = LoggerFactory.getLogger(getClass());

	@Autowired
	private IClienteService clienteService;

	
	@GetMapping("/form/{clienteId}")
	public String crear(@PathVariable(value = "clienteId") long clienteId, Map<String, Object> model, RedirectAttributes flash) {

		Cliente cliente = clienteService.findById(clienteId);

		if (cliente == null) {
			flash.addAttribute("error", "El cliente no existe en la base de datos");
			return "redirect:/listarClientes";
		}

		Factura factura = new Factura();
		factura.setCliente(cliente);

		model.put("factura", factura);
		model.put("titulo", "Crear Factura");

		return "factura/form";
	}

	@GetMapping(value = "/cargarProductos/{term}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody List<Producto> cargarProductos(@PathVariable(value = "term") String term) {
		return clienteService.findByName(term);
	}

	@PostMapping("/crearFactura")
	public String crearFactura(@Valid Factura factura, BindingResult result, Model model, @RequestParam(name = "item_id[]", required = false) Long[] itemiD,
			@RequestParam(name = "cantidad[]", required = false) Integer[] cantidad, RedirectAttributes flash, SessionStatus status) {

		if (result.hasErrors()) {
			model.addAttribute("titulo", "Crear Factura");
			return "factura/form";
		}

		if (itemiD == null || itemiD.length == 0) {
			model.addAttribute("titulo", "Crear Factura");
			model.addAttribute("error", "La factura NO puede tener items");
			return "factura/form";
		}

		for (int i = 0; i < itemiD.length; i++) {
			Producto producto = clienteService.findProductoById(itemiD[i]);

			ItemFactura itemFactura = new ItemFactura();
			itemFactura.setCantidad(cantidad[i]);
			itemFactura.setProducto(producto);

			factura.addItemsFactura(itemFactura);

			LOG.info("ID: {} , CANTIDAD: {}", itemiD[i], cantidad[i]);

		}

		clienteService.saveFactura(factura);
		status.setComplete();
		flash.addFlashAttribute("success", "Factura creada correctamente");

		return "redirect:/ver/" + factura.getCliente().getId();

	}

	@GetMapping("/ver/{id}")
	public String verDetalle(@PathVariable(value = "id") Long id, Model model, RedirectAttributes flash) {

		Factura factura = clienteService.fetchFacturaByIdWithClienteWithItemFacturaWithProduco(id); //clienteService.findFacturaById(id);

		if (factura == null) {
			flash.addFlashAttribute("error", "La factura no existe en la base de datos");

			return "redirect:/listarClientes";
		}

		model.addAttribute("factura", factura);
		model.addAttribute("titulo", "Ver factura ".concat(factura.getDescripcion()));

		return "factura/ver";

	}

	@GetMapping("/eliminar/{id}")
	public String eliminarFactura(@PathVariable(value = "id") long id, RedirectAttributes flash) {

		Factura factura = clienteService.findFacturaById(id);

		if (factura != null) {
			clienteService.deleteFactura(id);
			flash.addFlashAttribute("success", "Factura elimina");
			return "redirect:/ver/" + factura.getCliente().getId();
		}

		flash.addFlashAttribute("error", "La factura no existe, no se pudo eliminar");

		return "redirect:/listarClientes";

	}

}
