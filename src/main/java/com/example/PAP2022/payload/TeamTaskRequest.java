package com.example.PAP2022.payload;

import com.example.PAP2022.enums.TaskPriority;
import com.example.PAP2022.models.ApplicationUser;
import com.example.PAP2022.models.Team;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TeamTaskRequest {
    public String title;
    public String description;
    public LocalDateTime deadline;
    public TaskPriority priority;
    public Long giverId;
    public List<ApplicationUser> receivers;
    public Long teamId;
}
