package railfence.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import railfence.dto.RequestStructure;
import railfence.service.CipherService;



@RestController
public class ServiceController {
    private final CipherService service;

    public ServiceController(CipherService service) {
        this.service = service;
    }

    @PostMapping("/encode")
    public String encode(@RequestBody RequestStructure request) {
        return service.encode(request.text(), request.key());
    }

    @PostMapping("/decode")
    public String decode(@RequestBody RequestStructure request) {
        return service.decode(request.text(), request.key());
    }
}
