package com.example.PAP2022.services;

import com.example.PAP2022.exceptions.*;
import com.example.PAP2022.models.ApplicationUser;
import com.example.PAP2022.models.Task;
import com.example.PAP2022.models.Team;
import com.example.PAP2022.payload.TeamRequest;
import com.example.PAP2022.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TeamService {

    private final TeamRepository teamRepository;
    private final ApplicationUserDetailsService applicationUserService;

    @Autowired
    public TeamService(TeamRepository teamRepository,
                       ApplicationUserDetailsService applicationUserService) {
        this.teamRepository = teamRepository;
        this.applicationUserService = applicationUserService;
    }

    public Optional<Team> loadTeamById(Long teamId) {
        return teamRepository.findById(teamId);
    }

    public List<Team> getAllTeams(){
        return teamRepository.findAll().stream()
                .sorted(Comparator.comparing(Team::getName))
                .collect(Collectors.toList());
    }

    public Team getTeam(Long teamId) throws TeamNotFoundException {
        if (loadTeamById(teamId).isPresent()){
            return loadTeamById(teamId).get();
        } else {
            throw new TeamNotFoundException("Could not find team with ID " + teamId);
        }
    }

    public List<Task> getTeamTasks(Long teamId) throws TeamNotFoundException {
        return getTeam(teamId).getTeamTasks().stream()
                .sorted(Comparator.comparing(Task::getDeadline).reversed())
                .collect(Collectors.toList());
    }

    public List<ApplicationUser> getTeamMembers(Long teamId) throws TeamNotFoundException {
        return getTeam(teamId).getTeamMembers().stream()
                .sorted(Comparator.comparing(ApplicationUser::getLastName))
                .collect(Collectors.toList());
    }

    public ApplicationUser getTeamLeader(Long teamId) throws TeamNotFoundException {
        return getTeam(teamId).getTeamLeader();
    }

    public String getTeamName(Long teamId) throws TeamNotFoundException {
        return getTeam(teamId).getName();
    }

    public Long deleteTeam(Long teamId) throws TeamNotFoundException {
        if (loadTeamById(teamId).isPresent()) {
            teamRepository.deleteById(teamId);
        } else {
            throw new TeamNotFoundException("Could not find team with ID " + teamId);
        }
        return teamId;
    }

    public Team saveTeam(TeamRequest request) throws UserNotFoundException {
        ApplicationUser leader = applicationUserService.getApplicationUser(request.getTeamLeaderId());
        List<ApplicationUser> teamMembers = applicationUserService.getUsersByIds(request.getMembersIds());
        if (!teamMembers.contains(leader)) {
            teamMembers.add(leader);
        }

        Team team = new Team(
                request.getName(),
                leader,
                teamMembers
        );

        return teamRepository.save(team);
    }

    public Team addMember(Long teamId, Long memberId) throws
            TeamNotFoundException,
            UserNotFoundException,
            UserExistsException,
            UserNotEnabledException {

        Team team = getTeam(teamId);
        ApplicationUser applicationUser = applicationUserService.getApplicationUser(memberId);

        if (team.getTeamMembers().contains(applicationUser)) {
            throw new UserExistsException("User with ID " + memberId + " is already a member");
        }

        if (!applicationUser.getEnabled()) {
            throw new UserNotEnabledException("User with ID " + memberId + " is not enabled");
        }

        List<ApplicationUser> users = team.getTeamMembers();
        users.add(applicationUser);
        team.setTeamMembers(users);

        return teamRepository.save(team);
    }

    public Team editTeam(Long teamId, TeamRequest teamRequest) throws UserNotFoundException, TeamNotFoundException {
        Team team = getTeam(teamId);
        ApplicationUser leader = applicationUserService.getApplicationUser(teamRequest.getTeamLeaderId());
        List<ApplicationUser> teamMembers = applicationUserService.getUsersByIds(teamRequest.getMembersIds());

        team.setName(teamRequest.getName());
        team.setTeamLeader(applicationUserService.getApplicationUser(teamRequest.getTeamLeaderId()));

        if (!teamMembers.contains(leader)) {
            teamMembers.add(leader);
        }

        team.setTeamMembers(teamMembers);

        return teamRepository.save(team);
    }

    public Team deleteMember(Long teamId, Long memberId) throws TeamNotFoundException, UserNotFoundException, TeamLeaderDeletionException {
        Team team = getTeam(teamId);
        ApplicationUser applicationUser = applicationUserService.getApplicationUser(memberId);

        if (!team.getTeamMembers().contains(applicationUser)) {
            throw new UserNotFoundException("User with ID " + memberId + " is not a member");
        }

        if (team.getTeamLeader().getId() == applicationUser.getId()) {
            throw new TeamLeaderDeletionException("The leader cannot be removed from the team");
        }

        List<ApplicationUser> filteredMembers = team.getTeamMembers().stream()
                .filter(user -> user.getId() != memberId)
                .collect(Collectors.toList());
        team.setTeamMembers(filteredMembers);

        return teamRepository.save(team);
    }
}
