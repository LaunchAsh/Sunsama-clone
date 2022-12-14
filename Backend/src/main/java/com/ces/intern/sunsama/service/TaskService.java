package com.ces.intern.sunsama.service;

import com.ces.intern.sunsama.dto.HashtagDTO;
import com.ces.intern.sunsama.dto.TaskDTO;
import com.ces.intern.sunsama.http.request.SubtaskRequest;
import com.ces.intern.sunsama.http.request.TaskRequest;
import org.springframework.lang.Nullable;
import org.springframework.scheduling.config.Task;
import org.springframework.stereotype.Service;

import java.util.List;

public interface TaskService {
    List<TaskDTO> getAllTask();
    TaskDTO getTaskById(long id);
    TaskDTO createTask(TaskRequest request);
    TaskDTO updateTask(long id, TaskRequest request);
    void deleteTask(long id);
    List<HashtagDTO> getHashtagByTaskId(long id);
    void addHashtagToTask(long taskId, long hashtagId);
    void removeHashtagFromTask(long taskId, long hashtagId);
    List<TaskDTO> getTasksByDateWithHashtag(String dateStr,@Nullable Long hashtagId);
    List<TaskDTO> getTasksByDueDate(String dateStr);
    List<TaskDTO> getSubtasksOfTask(long taskId);
    TaskDTO addSubtaskToTask(long taskId, SubtaskRequest request);
    void setTaskComplete(long taskId);
    void changeDateOfTask(long taskId, String date);
}
