package habittracker.configservice.controller;

import habittracker.configservice.service.ConfigServiceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConfigServiceController {

    static ConfigServiceService configServiceService;

    private ConfigServiceController(ConfigServiceService configServiceService) {
        ConfigServiceController.configServiceService = configServiceService;
    }

    @GetMapping("/{application}/{profiles}/{label}")
    public ResponseEntity<String> findConfig(@PathVariable String application, @PathVariable String profiles, @PathVariable String label) {
        String documents = configServiceService.findConfig(application, profiles, label);
        return new ResponseEntity<>(documents, HttpStatus.OK);
    }

    @GetMapping("/{application}/{profiles}")
    public ResponseEntity<String> findConfig(@PathVariable String application, @PathVariable String profiles) {
        String documents = configServiceService.findConfig(application, profiles);
        return new ResponseEntity<>(documents, HttpStatus.OK);
    }
}