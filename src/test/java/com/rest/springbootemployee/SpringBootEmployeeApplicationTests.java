package com.rest.springbootemployee;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;

@SpringBootTest
@AutoConfigureMockMvc
class SpringBootEmployeeApplicationTests {

	@Autowired
	MockMvc client;

	@Autowired
	EmployeeRepository employeeRepository;

	@BeforeEach
	void cleanRepository() {
			employeeRepository.clearAll();
	}

	@Test
	void should_get_all_employees_when_perform_get_given_employees() throws Exception {
	    //given
	    employeeRepository.create(new Employee(1, "Susan", 20, "Female", 10000));
		employeeRepository.create(new Employee(2, "Bob", 21, "Male", 5000));
	    //when
	    client.perform(MockMvcRequestBuilders.get("/employees"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].id").isNumber())
				.andExpect(MockMvcResultMatchers.jsonPath("$[1].id").isNumber())
				.andExpect(MockMvcResultMatchers.jsonPath("$[*].age", containsInAnyOrder(20,21)))
				.andExpect(MockMvcResultMatchers.jsonPath("$[*].gender", containsInAnyOrder("Female", "Male")))
				.andExpect(MockMvcResultMatchers.jsonPath("$[*].salary", containsInAnyOrder(10000, 5000)))
				.andExpect(MockMvcResultMatchers.jsonPath("$[*].name", containsInAnyOrder("Susan", "Bob")));

	    //then
	}

	@Test
	void should_get_employee_when_perform_get_by_id_given_employees() throws Exception {
		//given
		Employee employee = employeeRepository.create(new Employee(1, "Susan", 20, "Female", 10000));
		//when
		client.perform(MockMvcRequestBuilders.get("/employees/{id}", employee.getId()))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Susan"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.age").value(20))
				.andExpect(MockMvcResultMatchers.jsonPath("$.gender").value("Female"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.salary").value(10000));

		//then
	}

	@Test
	void should_get_employee_with_specific_gender_when_perform_get_by_gender_given_employees() throws Exception {
		//given
		Employee susan = employeeRepository.create(new Employee(1, "Susan", 20, "Female", 10000));
		Employee bob = employeeRepository.create(new Employee(2, "Bob", 20, "Male", 10000));
		Employee peter = employeeRepository.create(new Employee(3, "Peter", 20, "Male", 10000));
		//when
		client.perform(MockMvcRequestBuilders.get("/employees?gender={gender}", "Male"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$[*].name", containsInAnyOrder("Bob", "Peter")))
				.andExpect(MockMvcResultMatchers.jsonPath("$[*].age", containsInAnyOrder(20, 20)))
				.andExpect(MockMvcResultMatchers.jsonPath("$[*].gender", containsInAnyOrder("Male", "Male")))
				.andExpect(MockMvcResultMatchers.jsonPath("$[*].salary", containsInAnyOrder(10000, 10000)));

		//then
	}

	@Test
	void should_get_2_employees_when_perform_get_by_page_given_employees() throws Exception {
		//given
		Employee susan = employeeRepository.create(new Employee(1, "Susan", 20, "Female", 10000));
		Employee bob = employeeRepository.create(new Employee(2, "Bob", 20, "Male", 10000));
		Employee peter = employeeRepository.create(new Employee(3, "Peter", 22, "Male", 44));
		Employee sam = employeeRepository.create(new Employee(2, "Sam", 23, "Male", 45));
		Employee ted = employeeRepository.create(new Employee(3, "Ted", 20, "Male", 10000));
		//when
		client.perform(MockMvcRequestBuilders.get("/employees?page={page}&pageSize={pageSize}", "2", "2"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].id").isNumber())
				.andExpect(MockMvcResultMatchers.jsonPath("$[1].id").isNumber())
				.andExpect(MockMvcResultMatchers.jsonPath("$[*].age", containsInAnyOrder(22,23)))
				.andExpect(MockMvcResultMatchers.jsonPath("$[*].gender", containsInAnyOrder("Male", "Male")))
				.andExpect(MockMvcResultMatchers.jsonPath("$[*].salary", containsInAnyOrder(44, 45)))
				.andExpect(MockMvcResultMatchers.jsonPath("$[*].name", containsInAnyOrder("Peter", "Sam")));

	}

}
