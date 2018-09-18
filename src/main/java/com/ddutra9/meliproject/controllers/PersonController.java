package com.ddutra9.meliproject.controllers;

import com.ddutra9.meliproject.model.Person;
import com.ddutra9.meliproject.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.AccessDeniedException;
import java.util.Map;

@RestController
public class PersonController {

    @Autowired
    PersonService personService;

    @PostMapping("/mutant/")
    public Person savePerson(@RequestBody Person person){
        return personService.saveAndValidIsMutant(person);
    }

    @GetMapping("/stats")
    public Map<String, Double> getPersonStats(){
        return personService.getStats();
    }

}
