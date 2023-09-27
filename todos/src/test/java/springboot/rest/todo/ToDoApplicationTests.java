package springboot.rest.todo;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ToDoApplicationTests {

	@Autowired
	TestRestTemplate restTemplate;

	@Test
	void canReturnSavedToDo() {
		ResponseEntity<String> response = restTemplate.getForEntity("/todos/1", String.class);

		DocumentContext documentContext = JsonPath.parse(response.getBody());
		Number id = documentContext.read("$.id");
		String value = documentContext.read("$.value");

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(id).isEqualTo(1);
		assertThat(value).isEqualTo("Buy groceries.");

	}

	@Test
	void shouldNotCrashWhenAskedForAnUnknownId(){
		ResponseEntity<String> response = restTemplate.getForEntity("/todos/1000", String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
		assertThat(response.getBody()).isBlank();
	}

	@Test
	void canReturnAllToDos(){
		ResponseEntity<String> response = restTemplate.getForEntity("/todos", String.class);
		DocumentContext documentContext = JsonPath.parse(response.getBody());
		int length = documentContext.read("$.length()");

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(length).isGreaterThan(0);
	}

}
