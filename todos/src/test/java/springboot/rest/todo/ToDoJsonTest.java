package springboot.rest.todo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class ToDoJsonTest {

    @Autowired
    private JacksonTester<ToDo> json;

    @Test
    public void canSerializeToDo() throws IOException {
        ToDo toDo = new ToDo(1L, "Buy groceries.");
        assertThat(json.write(toDo)).isStrictlyEqualToJson("test.json");
        assertThat(json.write(toDo)).hasJsonPathNumberValue("@.id");
        assertThat(json.write(toDo)).hasJsonPathStringValue("@.value");
        assertThat(json.write(toDo)).extractingJsonPathNumberValue("@.id").isEqualTo(1);
        assertThat(json.write(toDo)).extractingJsonPathStringValue("@.value").isEqualTo("Buy groceries.");
    }

    @Test
    public void canDeserializeToDo() throws IOException {
        String toDo = """
                {
                  "id": 1,
                  "value":"Buy groceries."
                }
                """;


        assertThat(json.parseObject(toDo).id).isEqualTo(1);
        assertThat(json.parseObject(toDo).text).isEqualTo("Buy groceries.");
    }
}
