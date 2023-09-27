package springboot.rest.todo;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class ToDo{
    @Id
    Long id;

    String value;

    public ToDo() {
    }

    public ToDo(String value) {
        this.value = value;
    }

    public ToDo(Long id, String value) {
        this.id = id;
        this.value = value;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

