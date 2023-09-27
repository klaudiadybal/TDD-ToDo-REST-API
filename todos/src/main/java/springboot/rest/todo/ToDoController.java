package springboot.rest.todo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/todos")
public class ToDoController {

    @Autowired
    private ToDoRepository toDoRepository;


    @GetMapping("/{id}")
    public ResponseEntity<ToDo> findById(@PathVariable Long id){
        Optional<ToDo> toDo = toDoRepository.findById(id);

        if(toDo.isPresent()) {
            return ResponseEntity.ok(toDo.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<ToDo>> findAll(Pageable pageable) {
        Page<ToDo> page = toDoRepository.findAll(
                PageRequest.of(
                        pageable.getPageNumber(),
                        pageable.getPageSize()
                )
        );

        return ResponseEntity.ok(page.getContent());
    }
}
