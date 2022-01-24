package com.example.deliveryAppServer.controller;

import com.example.deliveryAppServer.model.user.CustomerEntity;
import com.example.deliveryAppServer.model.user.ProviderEntity;
import com.example.deliveryAppServer.service.ProviderService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.apache.bcel.classfile.Module;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/provider")
@Slf4j
public class ProviderController {
    /*
    @Autowired
    private OrderService orderService;
    */

    @Autowired
    private ProviderService providerService;

    @PostMapping("/postProvider")
    @ResponseStatus(code = HttpStatus.CREATED)
    public void createNewProvider(@Valid @RequestBody ProviderEntity provider){
        log.info("[REST Controller] Post new provider");
        providerService.createNewProvider(provider);
    }

    @PutMapping("/putProvider")
    @ResponseStatus(code = HttpStatus.OK)
    public void putProvider(@Valid @RequestBody ProviderEntity provider){
        log.info("[REST Controller] Put provider");
        providerService.updateProvider(provider);
    }
    @GetMapping("/getAvalaibleProviders")
    @ResponseStatus(code = HttpStatus.OK)
    public List<ProviderEntity> getAvailaibleProvider(){
        log.info("[REST Controller] Get Available provider");
        return providerService.getAvailableProviders();
    }



    @PostMapping("/login")
    @ResponseStatus(code = HttpStatus.OK)
    public Long login(@RequestBody Map<String, String> params){
        log.info("[REST Controller] Login provider");
        String username = params.get("username");
        String password = params.get("password");
        return providerService.loginProvider(username, password);
    }
}
