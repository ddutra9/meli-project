package com.ddutra9.meliproject;

import com.ddutra9.meliproject.exceptions.ForbiddenException;
import com.ddutra9.meliproject.model.Person;
import com.ddutra9.meliproject.services.PersonService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PersonServiceTests {

	@Autowired
	PersonService personService;

	@Test
	public void createMutantAndHuman() {
		Person person = new Person();
		person.setDna(new String[]{"ATGCGA","CAGTGC","TTATGT","AGAAGG","CCCCTA","TCACTG"});

		try {
			personService.saveAndValidIsMutant(person);
		} catch (ForbiddenException ex){
			Assert.fail();
		}

		Map<String, Double> map = personService.getStats();
		Assert.assertTrue(map.get("count_mutant_dna") > 0);
		Assert.assertTrue(map.get("count_human_dna") == 0);

		person = new Person();
		person.setDna(new String[]{"ATGCGA","AAGTCC","ATATAT","CGAAGG","CCCCTA","TCACTG"});

		try {
			personService.saveAndValidIsMutant(person);
			Assert.fail();
		} catch (ForbiddenException ex){

		}

		map = personService.getStats();
		Assert.assertTrue(map.get("count_human_dna") > 0);
	}

}
