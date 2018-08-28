package com.woowahan.moduchan.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class ProjectUpdateEventListener implements ApplicationListener<ProjectUpdateEvent> {


    private final SimpMessagingTemplate template;

    @Autowired
    public ProjectUpdateEventListener(SimpMessagingTemplate template) {
        this.template = template;
    }

    @Override
    public void onApplicationEvent(ProjectUpdateEvent event) {
        template.convertAndSend("/subscribe/project", event.getProject().toDTO());
    }
}
