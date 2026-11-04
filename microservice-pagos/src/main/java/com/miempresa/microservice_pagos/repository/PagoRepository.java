package com.miempresa.microservice_pagos.repository;

import com.miempresa.microservice_pagos.entity.Pago;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PagoRepository
        extends JpaRepository<Pago, String> {
}