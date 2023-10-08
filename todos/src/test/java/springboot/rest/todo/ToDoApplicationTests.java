package springboot.rest.todo;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import net.minidev.json.JSONArray;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
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
		ResponseEntity<String> response = restTemplate
				.getForEntity("/todos/100", String.class);

		DocumentContext documentContext = JsonPath.parse(response.getBody());
		Number id = documentContext.read("$.id");
		String value = documentContext.read("$.text");

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(id).isEqualTo(100);
		assertThat(value).isEqualTo("Buy groceries.");

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
	void shouldNotCrashWhenAskedForAnUnknownId(){
		ResponseEntity<String> response = restTemplate.getForEntity("/todos/-1", String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
		assertThat(response.getBody()).isBlank();
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
		String value = documentContext.read("$.text");

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(id).isNotNull();
		assertThat(value).isEqualTo("Complete 30 minutes of exercise.");
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
	void canUpdateAnExistingToDo() {
		ToDo toDo = new ToDo("Read 15 pages of the book.");
		HttpEntity<ToDo> request = new HttpEntity<>(toDo);

		ResponseEntity<Void> putResponse = restTemplate
				.exchange("/todos/100", HttpMethod.PUT, request, Void.class);

		ResponseEntity<String> getResponse = restTemplate
				.getForEntity("/todos/100", String.class);

		assertThat(putResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
		assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		DocumentContext documentContext = JsonPath.parse(getResponse.getBody());
		Number id = documentContext.read("$.id");
		String value = documentContext.read("$.text");

		assertThat(id).isEqualTo(100);
		assertThat(value).isEqualTo("Read 15 pages of the book.");
	}

	@Test
	void shouldNotUpdateAToDoThatDoesNotExist() {
		ToDo toDo = new ToDo(-1L, "Go for a walk.");
		HttpEntity<ToDo> request = new HttpEntity<>(toDo);
		ResponseEntity<Void> response = restTemplate
				.exchange("/todos/-1", HttpMethod.PUT, request, Void.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

	@Test
	@DirtiesContext
	void canDeleteAnExistingToDo() {
		ResponseEntity<Void> deleteResponse = restTemplate
				.exchange("/todos/100", HttpMethod.DELETE, null, Void.class);

		ResponseEntity<String> getResponse = restTemplate
				.getForEntity("/todos/100", String.class);

		assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
		assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

	@Test
	void shouldNotDeleteAToDoThatDoesNotExist() {
		ResponseEntity<Void> deleteResponse = restTemplate
				.exchange("/todos/-1", HttpMethod.DELETE, null, Void.class);
		assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}
}
