package springboot.rest.todo;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import net.minidev.json.JSONArray;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.net.URI;

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

	@Test
	void canReturnAPageOfToDos() {
		ResponseEntity<String> response = restTemplate.getForEntity("/todos?page=0&size=1", String.class);

		DocumentContext documentContext = JsonPath.parse(response.getBody());
		JSONArray page = documentContext.read("$[*]");

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(page.size()).isEqualTo(1);
	}

	@Test
	@DirtiesContext
	void canCreateAToDo() {
		ToDo toDo = new ToDo( "Complete 30 minutes of exercise.");
		ResponseEntity<Void> response = restTemplate.postForEntity("/todos", toDo, Void.class);

		URI location = response.getHeaders().getLocation();
		ResponseEntity<String> getResponse = restTemplate.getForEntity(location, String.class);

		DocumentContext documentContext = JsonPath.parse(getResponse.getBody());
		Number id = documentContext.read("$.id");
		String value = documentContext.read("$.value");

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(id).isNotNull();
		assertThat(value).isEqualTo("Complete 30 minutes of exercise.");
	}

}
