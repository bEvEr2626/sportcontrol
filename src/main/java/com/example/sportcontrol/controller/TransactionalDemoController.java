package com.example.sportcontrol.controller;

import com.example.sportcontrol.exception.SimulatedErrorException;
import com.example.sportcontrol.service.TransactionalDemoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/demo/transactional")
@RequiredArgsConstructor
public class TransactionalDemoController {

    private final TransactionalDemoService demoService;

    /**
     * Без @Transactional — Sport и Tournament сохранятся, несмотря на ошибку.
     * Данные останутся в БД (частичное сохранение).
     */
    @PostMapping("/without")
    public String withoutTransactional() {
        try {
            demoService.saveWithoutTransactional();
            return "OK";
        } catch (SimulatedErrorException ex) {
            return "ERROR (partial save): " + ex.getMessage();
        }
    }

    /**
     * С @Transactional — при ошибке всё откатывается.
     * Ни Sport, ни Tournament не сохранятся.
     */
    @PostMapping("/with")
    public String withTransactional() {
        try {
            demoService.saveWithTransactional();
            return "OK";
        } catch (SimulatedErrorException ex) {
            return "ERROR (rolled back): " + ex.getMessage();
        }
    }
}
