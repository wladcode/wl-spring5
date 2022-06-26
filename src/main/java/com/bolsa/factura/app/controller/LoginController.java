package com.bolsa.factura.app.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {

	@GetMapping("/login")
	public String login(Model model, Principal principal, RedirectAttributes flash, @RequestParam(value = "error", required = false) String error,
			@RequestParam(value = "logout", required = false) String logout) {

		if (principal != null) {
			flash.addFlashAttribute("info", "Ya tiene iniciada una session");
			return "redirect:/";
		}

		if (error != null) {
			model.addAttribute("error", "Usuario o clave incorrecto");
		}
		
		if (logout != null) {
			model.addAttribute("success", "Sesión cerrada correctamente.");
		}
		return "login/login";
	}

}
