package com.woowahan.moduchan.domain.user;

import com.woowahan.moduchan.domain.project.Project;
import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.List;

@Entity
@Getter
public class NormalUser extends User {
    private String phoneNo;
    private String address;
}
