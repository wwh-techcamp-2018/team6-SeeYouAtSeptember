package com.woowahan.moduchan.AcceptanceTest;

import com.woowahan.moduchan.dto.project.ProjectDTO;
import util.ApiAcceptanceTest;
import org.junit.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ApiProjectAcceptanceTest extends ApiAcceptanceTest {

    @Test
    public void getProjects_성공() {
        ResponseEntity<List<ProjectDTO>> response = template().exchange("/api/projects",
                HttpMethod.GET,
                createHttpEntity(),
                new ParameterizedTypeReference<List<ProjectDTO>>() {
                });
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().size()).isEqualTo(4);
        assertThat(response.getBody().stream().filter(categoryDTO -> categoryDTO.getTitle().equals("[남도애꽃] 샐러리양파 장아찌 200g")).count()).isEqualTo(1);
    }

    @Test
    public void getProject_성공() {
        ResponseEntity<ProjectDTO> response = template().exchange("/api/projects/1",
                HttpMethod.GET,
                createHttpEntity(),
                new ParameterizedTypeReference<ProjectDTO>() {
                });
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getTitle()).isEqualTo("[남도애꽃] 샐러리양파 장아찌 200g");
    }

    @Test
    public void createProject_성공(){
        ResponseEntity<Void> response = templateWithNormalUser()
                .postForEntity("/api/projects", getDefaultProjectDTO(),Void.class);;
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    public void createProject_실패_로그인_하지않은_사용자(){
        ResponseEntity<Void> response = template()
                .postForEntity("/api/projects", getDefaultProjectDTO(),Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void createProject_실패_유효성검사_실패(){
        ProjectDTO projectDTO = getDefaultProjectDTO();
        projectDTO.setThumbnailUrl("test");
        ResponseEntity<Void> response = templateWithNormalUser()
                .postForEntity("/api/projects", projectDTO,Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}