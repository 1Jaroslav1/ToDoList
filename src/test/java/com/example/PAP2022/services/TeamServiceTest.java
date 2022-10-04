package com.example.PAP2022.services;

import com.example.PAP2022.enums.ApplicationUserRole;
import com.example.PAP2022.exceptions.TeamNotFoundException;
import com.example.PAP2022.models.ApplicationUser;
import com.example.PAP2022.models.Team;
import com.example.PAP2022.repository.TeamRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static org.assertj.core.api.Assertions.*;

public class TeamServiceTest {
    @Test
    @DisplayName("should get team optional by id")
    void loadTeamById_Test(){
        ApplicationUser user1 = new ApplicationUser("Bob", "Fire", "123@gmail.com", "pass", ApplicationUserRole.USER);
        ApplicationUser user2 = new ApplicationUser("Ann", "Water", "456@gmail.com", "pass", ApplicationUserRole.USER);
        ApplicationUser user3 = new ApplicationUser("John", "Earth", "789@gmail.com", "pass", ApplicationUserRole.USER);

        List<ApplicationUser> list = new ArrayList<>();

        list.add(user1);
        list.add(user2);
        list.add(user3);

        Team team = new Team("PAP", user1, list);

        var mockTeamRepo = mock(TeamRepository.class);
        when(mockTeamRepo.findById(anyLong())).thenReturn(Optional.of(team));

        mockTeamRepo.save(team);

        var toTest = new TeamService(mockTeamRepo, null);

        var result = toTest.loadTeamById((long) 0);

        assertThat(result.get().getName()).isEqualTo("PAP");
        assertThat(result.get().getTeamLeader()).isEqualTo(user1);
        assertThat(result.get().getTeamMembers()).isEqualTo(list);
    }

    @Test
    @DisplayName("should throw TeamNotFoundException")
    void getTeam_Test_With_TeamNotFoundException(){
        var mockTeamRepo = mock(TeamRepository.class);
        when(mockTeamRepo.findById(anyLong())).thenReturn(Optional.empty());

        var toTest = new TeamService(mockTeamRepo, null);

        var result = catchThrowable(() -> toTest.getTeam((long) 0));

        assertThat(result).isInstanceOf(TeamNotFoundException.class).hasMessageContaining("not find team with ID");
    }

    @Test
    @DisplayName("should get team by id")
    void getTeam_Test() throws TeamNotFoundException {
        ApplicationUser user1 = new ApplicationUser("Bob", "Fire", "123@gmail.com", "pass", ApplicationUserRole.USER);
        ApplicationUser user2 = new ApplicationUser("Ann", "Water", "456@gmail.com", "pass", ApplicationUserRole.USER);
        ApplicationUser user3 = new ApplicationUser("John", "Earth", "789@gmail.com", "pass", ApplicationUserRole.USER);

        List<ApplicationUser> list = new ArrayList<>();

        list.add(user1);
        list.add(user2);
        list.add(user3);

        Team team = new Team("PAP", user1, list);

        var mockTeamRepo = mock(TeamRepository.class);
        when(mockTeamRepo.findById(anyLong())).thenReturn(Optional.of(team));

        mockTeamRepo.save(team);

        var toTest = new TeamService(mockTeamRepo, null);

        var result = toTest.getTeam((long) 0);

        assertThat(result.getName()).isEqualTo("PAP");
        assertThat(result.getTeamLeader()).isEqualTo(user1);
        assertThat(result.getTeamMembers()).isEqualTo(list);
    }
}
