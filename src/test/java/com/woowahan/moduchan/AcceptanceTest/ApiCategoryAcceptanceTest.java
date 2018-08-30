package com.woowahan.moduchan.AcceptanceTest;

import com.woowahan.moduchan.dto.category.CategoryDTO;
import com.woowahan.moduchan.dto.project.ProjectDTO;
import com.woowahan.moduchan.support.ApiAcceptanceTest;
import org.junit.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ApiCategoryAcceptanceTest extends ApiAcceptanceTest {

    @Test
    public void getCategories_성공() {
        ResponseEntity<List<CategoryDTO>> response = template().exchange("/api/categories",
                HttpMethod.GET,
                createHttpEntity(),
                new ParameterizedTypeReference<List<CategoryDTO>>() {
                });
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<CategoryDTO> categoryDTOs = response.getBody();
        assertThat(categoryDTOs.size()).isEqualTo(8);
    }

    @Test
    public void getCategoryPage_성공() {
        ResponseEntity<List<ProjectDTO>> response = template().exchange("/api/categories/2/last/0",
                HttpMethod.GET,
                createHttpEntity(),
                new ParameterizedTypeReference<List<ProjectDTO>>() {
                });
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().size()).isEqualTo(3);
        assertThat(response.getBody().stream().filter(categoryDTO -> categoryDTO.getTitle().equals("[남도애꽃] 샐러리양파 장아찌 200g")).count()).isEqualTo(1);
    }

    @Test
    public void getCategoryPage_실패_없는카테고리() {
        ResponseEntity<String> response = template().getForEntity("/api/categories/10/last/0", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().contains("category not found")).isTrue();
    }

    @Test
    public void getProjectsOfAllCategory_성공() {
        ResponseEntity<List<ProjectDTO>> response = template().exchange("/api/categories/all/last/0",
                HttpMethod.GET,
                createHttpEntity(),
                new ParameterizedTypeReference<List<ProjectDTO>>() {
                });
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().size()).isEqualTo(4);
        assertThat(response.getBody().stream().filter(categoryDTO -> categoryDTO.getTitle().equals("[남도애꽃] 샐러리양파 장아찌 200g")).count()).isEqualTo(1);
    }
}