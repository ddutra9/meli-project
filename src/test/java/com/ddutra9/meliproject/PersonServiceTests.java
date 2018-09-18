package com.ddutra9.meliproject;

import com.ddutra9.meliproject.exceptions.BadRequestException;
import com.ddutra9.meliproject.exceptions.ForbiddenException;
import com.ddutra9.meliproject.model.Person;
import com.ddutra9.meliproject.repositories.PersonRepository;
import com.ddutra9.meliproject.services.PersonService;
import javafx.application.Application;
import org.junit.After;
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

	@Autowired
	PersonRepository personRepository;

	@After
	public void After(){
		personRepository.deleteAll();
	}

	@Test
	public void createMutantTest() {
		Person person = new Person();
		person.setDna(new String[]{"ATGCGA","CAGTGC","TTATGT","AGAAGG","CCCCTA","TCACTG"});

		try {
			personService.saveAndValidIsMutant(person);
		} catch (ForbiddenException ex){
			Assert.fail();
		}

		Map<String, Double> map = personService.getStats();
		Assert.assertEquals(map.get("count_mutant_dna"),  Double.valueOf(1));
		Assert.assertEquals(map.get("count_human_dna"), Double.valueOf(0));
	}

	@Test
	public void create2EqualMutantsTest() {
		createMutantTest();
		createMutantTest();
	}

	@Test
	public void createHumantTest() {
		Person person = new Person();
		person.setDna(new String[]{"ATGCGA","AAGTCC","ATATAT","CGAAGG","CCCCTA","TCACTG"});

		try {
			personService.saveAndValidIsMutant(person);
			Assert.fail();
		} catch (ForbiddenException ex){
			Assert.assertEquals("Humano!", ex.getMessage());
		}

		Map<String, Double> map = personService.getStats();
		Assert.assertEquals(map.get("count_human_dna"), Double.valueOf(1));
		Assert.assertEquals(map.get("count_mutant_dna"), Double.valueOf(0));
	}

	@Test
	public void createPersonWithUnknownCharTest() {
		Person person = new Person();
		person.setDna(new String[]{"ATGLGA","AAOTCC","ATAYAT","WGAAGG","CCCPTA","TCACBG"});

		try {
			personService.saveAndValidIsMutant(person);
			Assert.fail();
		} catch (BadRequestException ex){
			Assert.assertEquals("Caracter invalido!", ex.getMessage());
		}

		Map<String, Double> map = personService.getStats();
		Assert.assertEquals(map.get("count_human_dna"), Double.valueOf(0));
		Assert.assertEquals(map.get("count_mutant_dna"), Double.valueOf(0));
	}

	@Test
	public void createMatrizNxYTest() {
		Person person = new Person();
		person.setDna(new String[]{"ATGCGA","AAGTCC","ATATAT","CGAAGG","CCCCTA","TCACTG","TCACTG"});

		try {
			personService.saveAndValidIsMutant(person);
			Assert.fail();
		} catch (BadRequestException ex){
			Assert.assertEquals("Matriz deve ser NxN!", ex.getMessage());
		}

		Map<String, Double> map = personService.getStats();
		Assert.assertEquals(map.get("count_human_dna"), Double.valueOf(0));
		Assert.assertEquals(map.get("count_mutant_dna"), Double.valueOf(0));
	}

}
