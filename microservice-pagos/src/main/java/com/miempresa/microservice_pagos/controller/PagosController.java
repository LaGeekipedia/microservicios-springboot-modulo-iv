package com.miempresa.microservice_pagos.controller;

import com.miempresa.microservice_pagos.client.ClientesClient;
import com.miempresa.microservice_pagos.dto.ClienteDTO;
import com.miempresa.microservice_pagos.entity.Pago;
import com.miempresa.microservice_pagos.events.PagoCreadoEvent;
import com.miempresa.microservice_pagos.messaging.PagosPublisher;
import com.miempresa.microservice_pagos.service.PagoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.Instant;

@RefreshScope
@RestController
@RequestMapping("/pagos")
public class PagosController {

    private final PagosPublisher pagosPublisher;
    private final PagoService pagoService;
    private final ClientesClient  clientesClient;

    @Value("${mensaje.bienvenida}")
    private String mensaje;

    @Value("${server.port}")
    private String puerto;

    @Autowired
    public PagosController(PagosPublisher pagosPublisher,
                           PagoService pagoService,
                           ClientesClient clientesClient) {

        this.pagosPublisher = pagosPublisher;
        this.pagoService = pagoService;
        this.clientesClient = clientesClient;
    }

    @GetMapping("/mensaje")
    public String obtenerMensaje() {
        return mensaje + " - Puerto: " + puerto;
    }

    // Nuevo endpoint simulando falla - Que se ha quedado colgada la solicitud
    @GetMapping("/lento")
    public String lento(@RequestParam(defaultValue = "0") long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return "OK request exitosa después de " + ms + " ms - Puerto: " + puerto;
    }

    // endpoint para consultar un cliente desde pagos
    @GetMapping("/clientes/{clienteId}")
    public ClienteDTO consultarCliente(
            @PathVariable String clienteId) {

        return clientesClient.buscarCliente(clienteId);
    }

    // endpoint para creación de pagos y publicación de eventos
    @PostMapping("/crear")
    public String crearPago(@RequestParam String pagoId,
                            @RequestParam String clienteId,
                            @RequestParam(defaultValue = "100.00") BigDecimal monto,
                            @RequestParam(defaultValue = "USD") String moneda) {

        Instant fechaCreacion = Instant.now();

        Pago pago = new Pago(pagoId, clienteId, monto, moneda, fechaCreacion);

        pagoService.guardarPago(pago);

        PagoCreadoEvent event = new PagoCreadoEvent(
                pagoId, clienteId, monto, moneda, java.time.Instant.now()
        );

        pagosPublisher.publicarPagoCreado(event);

        return "Pago creado y evento publicado: " + pagoId;
    }
}
