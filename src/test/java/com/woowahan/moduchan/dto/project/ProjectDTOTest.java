package com.woowahan.moduchan.dto.project;

import com.woowahan.moduchan.domain.user.NormalUser;
import com.woowahan.moduchan.dto.product.ProductDTO;
import com.woowahan.moduchan.support.BaseTimeEntity;
import org.junit.Before;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class ProjectDTOTest {

    private static Validator validator;
    private final static Long ONE_DAY_DIFF = 86400000L;
    List<ProductDTO> productDTOs;
    private static Long todayTime;

    @Before
    public void setUp() throws Exception {
        productDTOs = new ArrayList<>();

        Set<NormalUser> normalUsers = new HashSet<>();
        normalUsers.add(new NormalUser());
        normalUsers.add(new NormalUser());
        normalUsers.add(new NormalUser());

        productDTOs.add(new ProductDTO(null, "테스트상품", 1000L, 100000L, "테스트입니다", normalUsers, null));
        todayTime = BaseTimeEntity.getTodayTime();
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void prjectValid_성공() {

        ProjectDTO projectDTO = new ProjectDTO(2L, null, "프로젝트", "프로젝트설명", "https://moduchan-img.s3.ap-northeast-2.amazonaws.com/media/53e15324-3732-444c-8528-468d41cf160a.png", null
                , todayTime + ONE_DAY_DIFF * 31, null, null, productDTOs, 1000000L, 1000000L);
        Set<ConstraintViolation<ProjectDTO>> constraintViolcations = validator.validate(projectDTO);
        assertThat(constraintViolcations.size()).isEqualTo(0);
    }

    @Test
    public void prjectValid_실패_목표금액_100만원_이하() {
        ProjectDTO projectDTO = new ProjectDTO(2L, null, "프로젝트", "프로젝트설명"
                , "https://moduchan-img.s3.ap-northeast-2.amazonaws.com/media/53e15324-3732-444c-8528-468d41cf160a.png", null
                , todayTime + ONE_DAY_DIFF * 31, null, null, productDTOs, 999999L, 9999999L);
        Set<ConstraintViolation<ProjectDTO>> constraintViolcations = validator.validate(projectDTO);
        assertThat(constraintViolcations.size()).isEqualTo(1);
    }

    @Test
    public void prjectValid_실패_유효하지않은_이미지url() {
        ProjectDTO projectDTO = new ProjectDTO(2L, null, "프로젝트", "프로젝트설명"
                , "https://moduchan-img.s3.ap-northeast-2.amazonaws.com/media/53e15324-3732-444c-8528-468d41cf160a.pag", null
                , todayTime + ONE_DAY_DIFF * 31, null, null, productDTOs, 10000000L, 10000000L);
        Set<ConstraintViolation<ProjectDTO>> constraintViolcations = validator.validate(projectDTO);
        assertThat(constraintViolcations.size()).isEqualTo(1);
    }

    @Test
    public void prjectValid_실패_목표날짜_30일이하() {
        ProjectDTO projectDTO = new ProjectDTO(2L, null, "프로젝트", "프로젝트설명"
                , "https://moduchan-img.s3.ap-northeast-2.amazonaws.com/media/53e15324-3732-444c-8528-468d41cf160a.png", null
                , todayTime + ONE_DAY_DIFF * 30, null, null, productDTOs, 10000000L, 10000000L);
        Set<ConstraintViolation<ProjectDTO>> constraintViolcations = validator.validate(projectDTO);
        assertThat(constraintViolcations.size()).isEqualTo(1);
    }

    @Test
    public void prjectValid_실패_목표금액미만() {
        List<ProductDTO> productDTOs = new ArrayList<>();
        productDTOs.add(new ProductDTO(null, "테스트상품", 999999L, 1L, "테스트입니다", null, null));

        ProjectDTO projectDTO = new ProjectDTO(2L, null, "프로젝트", "프로젝트설명", "https://moduchan-img.s3.ap-northeast-2.amazonaws.com/media/53e15324-3732-444c-8528-468d41cf160a.png", null
                , todayTime + ONE_DAY_DIFF * 31, null, null, productDTOs, 10000000L, 10000000L);
        Set<ConstraintViolation<ProjectDTO>> constraintViolcations = validator.validate(projectDTO);
        assertThat(constraintViolcations.size()).isEqualTo(1);
    }

    @Test
    public void prject_후원자수() {
        Set<NormalUser> normalUsers = new HashSet<>();
        normalUsers.add(null);

        normalUsers.forEach(normalUser -> {System.out.println(normalUser);});
        productDTOs.add(new ProductDTO(null, "테스트상품", 1000000L, 1L, "테스트입니다", normalUsers, null));

        ProjectDTO projectDTO = new ProjectDTO(2L, null, "프로젝트", "프로젝트설명", "https://moduchan-img.s3.ap-northeast-2.amazonaws.com/media/53e15324-3732-444c-8528-468d41cf160a.png", null
                , todayTime + ONE_DAY_DIFF * 31, null, null, productDTOs, 10000000L, 10000000L);

        assertThat(projectDTO.getSupporterCount()).isEqualTo(3L);
    }

    @Test
    public void prject_진행도_퍼센트계산() {
        ProjectDTO projectDTO = new ProjectDTO(2L, null, "프로젝트", "프로젝트설명", "https://moduchan-img.s3.ap-northeast-2.amazonaws.com/media/53e15324-3732-444c-8528-468d41cf160a.png", null
                , todayTime + ONE_DAY_DIFF * 31, null, null, productDTOs, 10000000L, 900000L);

        assertThat(projectDTO.getProgress()).isEqualTo(9);
    }

    @Test
    public void prject_남은날짜_계산() {
        ProjectDTO projectDTO = new ProjectDTO(2L, null, "프로젝트", "프로젝트설명", "https://moduchan-img.s3.ap-northeast-2.amazonaws.com/media/53e15324-3732-444c-8528-468d41cf160a.png", null
                , todayTime + ONE_DAY_DIFF * 31, null, null, productDTOs, 10000000L, 900000L);

        assertThat(projectDTO.getDayRemainingUntilDeadline()).isEqualTo(30L);
    }

}
