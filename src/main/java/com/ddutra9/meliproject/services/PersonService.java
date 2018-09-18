package com.ddutra9.meliproject.services;

import com.ddutra9.meliproject.exceptions.BadRequestException;
import com.ddutra9.meliproject.exceptions.ForbiddenException;
import com.ddutra9.meliproject.model.Person;
import com.ddutra9.meliproject.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Service
public class PersonService {

    @Autowired
    PersonRepository personRepository;

    public Person saveAndValidIsMutant(Person person){
        person.setMutant(isMutant(person.getDna()));

        if(personRepository.findFirstByDna(person.getDna()).size() <= 0){
            personRepository.save(person);
        }

        if(!person.isMutant()){
            throw new ForbiddenException("Humano!");
        }

        return person;
    }

    public Map<String, Double> getStats() {
        Map<String, Double> map = new HashMap<>();

        List<Person> persons = personRepository.findAll();
        map.put("count_human_dna", (double) persons.stream().filter(p -> p.isMutant() == false).count());
        map.put("count_mutant_dna", (double) persons.stream().filter(p -> p.isMutant() == true).count());
        map.put("ratio", (map.get("count_mutant_dna") / map.get("count_human_dna")));
        return map;
    }

    private boolean isMutant(String[] dna) {
        int n = dna.length;
        char[][] matrix = new char[n][n];
        int horizontalMatch = 0, verticalMatch = 0, obliqueMatch = 0;

        for (int i = 0; i < n; i++) {
            if(!dna[i].matches("[ACTG]+")){
                throw new BadRequestException("Caracter invalido!");
            }

            if(n != dna[i].length()){
                throw new BadRequestException("Matriz deve ser NxN!");
            }

            for (int j = 0; j < n; j++) {
                matrix[i][j] = dna[i].charAt(j);
            }
        }

        for (int i = 0; i < n; i++) {
            StringBuilder sbHorizontal = new StringBuilder(), sbVertical = new StringBuilder(), sbObliqua = new StringBuilder();
            for (int j = 0; j < n; j++) {
                sbHorizontal.append(matrix[i][j]);
                sbVertical.append(matrix[j][i]);
            }

            sbObliqua.append(matrix[i][i]);

            horizontalMatch = horizontalMatch == 0? hasSequence(sbHorizontal.toString()) : horizontalMatch;
            verticalMatch = verticalMatch == 0? hasSequence(sbVertical.toString()) : verticalMatch;
            obliqueMatch = obliqueMatch == 0? hasSequence(sbObliqua.toString()) : obliqueMatch;
        }

        return horizontalMatch + verticalMatch + obliqueMatch > 1;
    }

    private int hasSequence(String dna){
        if(dna.contains("AAAA") || dna.contains("TTTT") || dna.contains("CCCC") || dna.contains("GGGG")){
            return 1;
        }

        return 0;
    }
}
