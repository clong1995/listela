package com.cassiomolin.vanilla.task.controller;

import com.cassiomolin.vanilla.task.controller.mapper.TaskMapper;
import com.cassiomolin.vanilla.task.controller.model.CreateTaskDetails;
import com.cassiomolin.vanilla.task.controller.model.QueryTaskResult;
import com.cassiomolin.vanilla.task.domain.Task;
import com.cassiomolin.vanilla.task.domain.TaskFilter;
import com.cassiomolin.vanilla.task.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;

@RestController("tasks")
public class TaskController {

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private TaskService taskService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createTask(@Valid @NotNull @RequestBody CreateTaskDetails createTaskDetails) {

        Task task = taskMapper.toTask(createTaskDetails);
        task = taskService.createTask(task);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(task.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<QueryTaskResult>> findTasks(@RequestParam(name = "description", required = false) String description,
                                                           @RequestParam(name = "completed", required = false) Boolean completed) {

        TaskFilter filter = new TaskFilter();
        filter.setDescription(description);
        filter.setCompleted(completed);

        List<Task> tasks = taskService.findTasks(filter);
        List<QueryTaskResult> queryTaskResults = taskMapper.toQueryTaskResults(tasks);

        return ResponseEntity.ok(queryTaskResults);
    }
}