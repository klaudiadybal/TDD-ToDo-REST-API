package springboot.rest.todo;

import org.springframework.data.annotation.Id;

public class ToDo{

    @Id
    Long id;

    String text;

    public ToDo() {
    }

    public ToDo(String value) {
        this.text = value;
    }

    public ToDo(Long id, String value) {
        this.id = id;
        this.text = value;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}

