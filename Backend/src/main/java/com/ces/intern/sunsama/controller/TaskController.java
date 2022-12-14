package com.ces.intern.sunsama.controller;

import com.ces.intern.sunsama.dto.HashtagDTO;
import com.ces.intern.sunsama.dto.TaskDTO;
import com.ces.intern.sunsama.entity.HashtagEntity;
import com.ces.intern.sunsama.http.exception.BadRequestException;
import com.ces.intern.sunsama.http.exception.NotFoundException;
import com.ces.intern.sunsama.http.request.SubtaskRequest;
import com.ces.intern.sunsama.http.request.TaskRequest;
import com.ces.intern.sunsama.http.response.TaskResponse;
import com.ces.intern.sunsama.service.HashtagService;
import com.ces.intern.sunsama.service.TaskService;
import com.ces.intern.sunsama.util.DateValidator;
import com.ces.intern.sunsama.util.ExceptionMessage;
import com.ces.intern.sunsama.util.ResponseMessage;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;
    private final ModelMapper modelMapper;

    @Autowired
    public TaskController(TaskService taskService, ModelMapper modelMapper) {
        this.taskService = taskService;
        this.modelMapper = modelMapper;
        TypeMap<TaskDTO, TaskResponse> propertyMapper = this.modelMapper.createTypeMap(TaskDTO.class, TaskResponse.class);
        propertyMapper.addMappings(mapper -> mapper.map(src -> src.getUser().getId(), TaskResponse::setUserId));
    }

    @GetMapping("/")
    public List<TaskResponse> getAllTasks(){
        List<TaskDTO> taskDTOS = taskService.getAllTask();
        return taskDTOS.stream()
                .map(task -> modelMapper.map(task, TaskResponse.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public TaskResponse getTask(@PathVariable long id){
        TaskDTO taskDTO = taskService.getTaskById(id);
        return modelMapper.map(taskDTO, TaskResponse.class);
    }

    @PostMapping("/")
    public TaskResponse createTask(@RequestBody TaskRequest request){
        System.out.println(request);
        if(request.getTitle() == "") throw new BadRequestException("Request missing task's title");
        TaskDTO createdTask = taskService.createTask(request);
        return modelMapper.map(createdTask, TaskResponse.class);
    }

    @PutMapping("/{id}")
    public TaskResponse updateTask(@PathVariable long id, @RequestBody TaskRequest request){
        TaskDTO taskDTO = taskService.updateTask(id, request);
        return modelMapper.map(taskDTO,TaskResponse.class);
    }
    @DeleteMapping("/{id}")
    public String deleteTask(@PathVariable long id){
        try{
            taskService.deleteTask(id);
        }
        catch (NotFoundException e){
            return ExceptionMessage.TASK_NOT_EXIST.getMessage();
        }
        return ResponseMessage.DELETE_SUCCESS;
    }

    @GetMapping("/{taskId}/hashtags")
    public List<HashtagDTO> getHashtagByTaskId(@PathVariable long taskId){
        return taskService.getHashtagByTaskId(taskId);
    }

    @PostMapping("/{taskId}/hashtags/{hashtagId}")
    public String addHashtagToTask(@PathVariable long taskId, @PathVariable long hashtagId){
        try{
            taskService.addHashtagToTask(taskId, hashtagId);
            return ResponseMessage.ADD_SUCCESS;
        }
        catch (RuntimeException e){
            return e.getMessage();
        }
    }

    @DeleteMapping("/{taskId}/hashtags/{hashtagId}")
    public String removeHashtagFromTask(@PathVariable long taskId, @PathVariable long hashtagId)
    {
        try{
            taskService.removeHashtagFromTask(taskId, hashtagId);
            return ResponseMessage.DELETE_SUCCESS;
        }
        catch(RuntimeException e){
            return e.getMessage();
        }
    }

    @GetMapping("/date/{dateStr}")
    public List<TaskResponse> getTasksByDate(@PathVariable String dateStr, @RequestParam(name = "hashtagId", required = false) Long hashtagId){
        if(!DateValidator.isValid(dateStr))
            throw new BadRequestException("Invalid date format. Expected yyyy-MM-dd");
        List<TaskDTO> taskDTOS = taskService.getTasksByDateWithHashtag(dateStr, hashtagId);
        return taskDTOS.stream().map(taskDTO -> modelMapper.map(taskDTO, TaskResponse.class)).toList();
    }

    @GetMapping("/dueDate/{dateStr}")
    public List<TaskResponse> getTasksByDueDate(@PathVariable String dateStr){
        if(!DateValidator.isValid(dateStr))
            throw new BadRequestException("Invalid date format. Expected yyyy-MM-dd");
        List<TaskDTO> taskDTOS = taskService.getTasksByDueDate(dateStr);
        return taskDTOS.stream().map(taskDTO -> modelMapper.map(taskDTO, TaskResponse.class)).toList();
    }

    @GetMapping("/{taskId}/subtasks")
    public List<TaskResponse> getSubtasksOfTask(@PathVariable long taskId){
        List<TaskDTO> taskDTOS = taskService.getSubtasksOfTask(taskId);
        return taskDTOS.stream().map(taskDTO -> modelMapper.map(taskDTO, TaskResponse.class)).toList();
    }

    @PostMapping("/{taskId}/subtasks")
    public TaskResponse addSubtaskToTask(@PathVariable long taskId,@RequestBody SubtaskRequest request){
        TaskDTO taskDTO = taskService.addSubtaskToTask(taskId, request);
        return modelMapper.map(taskDTO, TaskResponse.class);
    }

    @PostMapping("/{taskId}/complete")
    public String setTaskComplete(@PathVariable long taskId){
        try{
            taskService.setTaskComplete(taskId);
        }
        catch (RuntimeException e) {
            return e.getMessage();
        }
        return ResponseMessage.UPDATE_SUCCESS;
    }

    @PutMapping("/{taskId}/change_date")
    public String changeDateOfTask(@PathVariable long taskId, @RequestBody Map<String,String> data){
        if(!DateValidator.isValid(data.get("newDate")))
            throw new BadRequestException("Invalid date format. Expected yyyy-MM-dd");
        try{
            taskService.changeDateOfTask(taskId, data.get("newDate"));
        }
        catch (Exception e){
            return e.getMessage();
        }
        return ResponseMessage.UPDATE_SUCCESS;
    }
}
