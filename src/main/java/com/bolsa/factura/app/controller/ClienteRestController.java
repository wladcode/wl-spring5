package com.bolsa.factura.app.controller;

import com.bolsa.factura.app.models.entity.Cliente;
import com.bolsa.factura.app.models.service.IClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class ClienteRestController {

    @Autowired
    private IClienteService clienteService;

    @GetMapping("/listar")    public List<Cliente> listarRest() {
        return clienteService.findAll();
    }
}
