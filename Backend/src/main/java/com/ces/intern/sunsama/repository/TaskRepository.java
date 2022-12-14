package com.ces.intern.sunsama.repository;

import com.ces.intern.sunsama.entity.HashtagEntity;
import com.ces.intern.sunsama.entity.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.scheduling.config.Task;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long> {

    @Query(value = "SELECT * FROM task JOIN task_hashtag ON task.id = task_hashtag.task_id WHERE id=:task_id AND hashtag_id = :hashtag_id", nativeQuery = true)
    TaskEntity getTaskHasHashtag(@Param("task_id") long taskId, @Param("hashtag_id") long hashtagId);
    @Query(value = "SELECT * FROM task WHERE CAST(task.date AS varchar) LIKE :date% AND task.parent_id=0", nativeQuery = true)
    List<TaskEntity> getTasksByDate(@Param("date") String date);
    @Query(value = "SELECT * FROM task WHERE CAST(task.dueDate AS varchar) LIKE :date% AND task.parent_id=0", nativeQuery = true)
    List<TaskEntity> getTasksByDueDate(@Param("date") String date);
    @Query(value = "SELECT * FROM task WHERE task.parent_id=:taskId", nativeQuery = true)
    List<TaskEntity> getSubtaskOfTask(@Param("taskId") long taskId);
    @Query(value = "DELETE FROM task WHERE task.parent_id=:taskId", nativeQuery = true)
    @Modifying
    void deleteAllSubtasksOfTask(@Param("taskId") long taskId);
    @Query(value = "SELECT * FROM task WHERE task.parent_id=0", nativeQuery = true)
    List<TaskEntity> getAllParentTask();
}
