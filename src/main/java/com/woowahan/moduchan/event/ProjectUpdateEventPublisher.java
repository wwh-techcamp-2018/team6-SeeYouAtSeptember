package com.woowahan.moduchan.event;

import com.woowahan.moduchan.domain.project.Project;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ProjectUpdateEventPublisher {
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public void publishEvent(Project project) {
        log.debug("publishing project update event");
        ProjectUpdateEvent projectUpdateEvent = new ProjectUpdateEvent(this, project);
        applicationEventPublisher.publishEvent(projectUpdateEvent);
    }
}
