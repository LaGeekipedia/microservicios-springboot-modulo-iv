package com.miempresa.microservice_pagos.service;

import com.miempresa.microservice_pagos.entity.Pago;
import com.miempresa.microservice_pagos.repository.PagoRepository;
import org.springframework.stereotype.Service;

@Service
public class PagoService {

    private final PagoRepository pagoRepository;

    public PagoService(PagoRepository pagoRepository) {
        this.pagoRepository = pagoRepository;
    }

    public Pago guardarPago(Pago pago) {
        return pagoRepository.save(pago);
    }
}