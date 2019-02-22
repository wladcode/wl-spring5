package com.bolsa.ideas.app.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bolsa.ideas.app.models.entity.Cliente;
import com.bolsa.ideas.app.models.service.IClienteService;
import com.bolsa.ideas.app.models.service.IUploadFileService;
import com.bolsa.ideas.app.util.paginator.PageRender;

@Controller
@SessionAttributes("cliente")
public class ClienteController {

	@Autowired
	private IClienteService clienteService;

	@Autowired
	private IUploadFileService uploadFileService;

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
	public String createUser(@Valid Cliente cliente, BindingResult result, Model model, @RequestParam("file") MultipartFile foto, RedirectAttributes flash, SessionStatus status) {
		if (result.hasErrors()) {
			model.addAttribute("titulo", "Crear cliente");
			return "createUser";
		}

		if (!foto.isEmpty()) {

			if (cliente.getId() != null && cliente.getId() > 0 && cliente.getFoto() != null && cliente.getFoto().length() > 0) {

				uploadFileService.delete(cliente.getFoto());

			}
			try {
				String uniqueFileName = uploadFileService.copy(foto);
				flash.addFlashAttribute("info", "Foto subida correctamente.");
				cliente.setFoto(uniqueFileName);

			} catch (IOException e) {
				e.printStackTrace();
			}
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
			Cliente cliente = clienteService.findById(id);
			clienteService.delete(id);
			flash.addFlashAttribute("success", "Cliente eliminado");
			if (uploadFileService.delete(cliente.getFoto())) {
				flash.addFlashAttribute("info", "Foto " + cliente.getFoto() + " eliminada con exito");
			}

		}

		return "redirect:/listar";
	}

	@GetMapping(value = "/ver/{id}")
	public String ver(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {

		Cliente cliente = clienteService.findById(id);
		if (cliente == null) {
			flash.addFlashAttribute("erro", "El cliente no existe");
			return "redirect:/listar";
		}

		model.put("cliente", cliente);
		model.put("titulo", "Detalle del cliente: " + cliente.getNombre());

		return "ver";
	}

	// Para no tructar la extension del archivo
	@GetMapping(value = "/uploads/{filename:.+}")
	public ResponseEntity<Resource> verFoto(@PathVariable String filename) {
		Resource recurso = null;
		try {
			recurso = uploadFileService.load(filename);
			return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + recurso.getFilename() + "\"").body(recurso);

		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		return null;
	}

}
