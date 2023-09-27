package springboot.rest.todo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/todos")
public class ToDoController {

    @Autowired
    private ToDoRepository toDoRepository;


    @GetMapping("/{id}")
    public ResponseEntity<ToDo> findById (@PathVariable Long id){
        Optional<ToDo> toDo = toDoRepository.findById(id);

        if(toDo.isPresent()) {
            return ResponseEntity.ok(toDo.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
