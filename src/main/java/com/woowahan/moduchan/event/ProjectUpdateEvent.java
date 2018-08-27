package com.woowahan.moduchan.event;

import com.woowahan.moduchan.domain.project.Project;
import org.springframework.context.ApplicationEvent;

public class ProjectUpdateEvent extends ApplicationEvent {
    private Project project;

    public ProjectUpdateEvent(Object source, Project project) {
        super(source);
        this.project = project;
    }

    public Project getProject(){
        return this.project;
    }
}
