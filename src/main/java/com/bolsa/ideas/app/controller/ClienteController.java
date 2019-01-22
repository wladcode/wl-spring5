package com.bolsa.ideas.app.controller;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bolsa.ideas.app.models.entity.Cliente;
import com.bolsa.ideas.app.models.service.IClienteService;
import com.bolsa.ideas.app.util.paginator.PageRender;

@Controller
@SessionAttributes("cliente")
public class ClienteController {

	@Autowired
	private IClienteService clienteService;

	@GetMapping(value = "/listar")
	public String listar(Model model, @RequestParam(name = "page", defaultValue = "0") int page) {

		Pageable pageRequest = new PageRequest(page, 4);
		Page<Cliente> clientes = clienteService.findAll(pageRequest);
		
		PageRender<Cliente> pageRender = new PageRender<>("/listar", clientes);

		model.addAttribute("titulo", "Listado de clientes");
		model.addAttribute("clientes", clientes);
		model.addAttribute("page", pageRender);
		return "listar";
	}

	@GetMapping(value = "/create")
	public String create(Map<String, Object> model) {

		Cliente cliente = new Cliente();
		model.put("titulo", "Crear cliente");
		model.put("cliente", cliente);

		return "createUser";
	}

	@GetMapping(value = "/editar/{id}")
	public String editar(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {

		Cliente cliente = null;
		if (id != null && id > 0) {
			cliente = clienteService.findById(id);
			if (cliente == null) {
				flash.addFlashAttribute("error", "El id del cliente no existe en base de datos");
				return "redirect:/listar";
			}
		} else {
			flash.addFlashAttribute("error", "El id del cliente no puedo ser cero");
			return "redirect:/listar";
		}

		model.put("titulo", "Editar cliente");
		model.put("cliente", cliente);

		return "createUser";
	}

	@PostMapping(value = "/createUser")
	public String createUser(@Valid Cliente cliente, BindingResult result, Model model, RedirectAttributes flash, SessionStatus status) {
		if (result.hasErrors()) {
			model.addAttribute("titulo", "Crear cliente");
			return "createUser";
		}

		String messageFlash = (cliente.getId() != null) ? "Cliente editado" : "Cliente creado";

		clienteService.save(cliente);
		status.setComplete();
		flash.addFlashAttribute("success", messageFlash);
		return "redirect:listar";
	}

	@GetMapping(value = "/delete/{id}")
	public String eliminar(@PathVariable(value = "id") Long id, RedirectAttributes flash) {

		if (id != null && id > 0) {
			clienteService.delete(id);
		}

		flash.addFlashAttribute("success", "Cliente eliminado");
		return "redirect:/listar";
	}

}
