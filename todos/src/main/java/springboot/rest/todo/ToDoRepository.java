package springboot.rest.todo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import springboot.rest.todo.ToDo;

@Repository
public interface ToDoRepository extends JpaRepository<ToDo, Long> {
}
