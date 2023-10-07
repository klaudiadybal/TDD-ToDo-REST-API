package springboot.rest.todo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
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

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody ToDo toDo, UriComponentsBuilder ucb){
        ToDo savedToDo = toDoRepository.save(toDo);

        URI location = ucb
                .path("/todos/{id}")
                .buildAndExpand(savedToDo.id)
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{requestedId}")
    public ResponseEntity<Void> put(@PathVariable Long requestedId, @RequestBody ToDo updatedToDo) {
        Optional<ToDo> todo = toDoRepository.findById(requestedId);

        if(todo.isPresent()) {
            ToDo updatedToDoToSave = new ToDo(todo.get().getId(), updatedToDo.getValue());
            toDoRepository.save(updatedToDoToSave);
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }
}
