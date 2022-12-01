package com.togedocs.backend.api.controller;

import com.togedocs.backend.api.dto.SocketDto;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class SocketController {
    @MessageMapping("/{project_id}/refresh")
    @SendTo("/sub/{project_id}/refresh")
    public SocketDto RefreshRequest(@DestinationVariable("project_id") Long project_id, SocketDto socketDto) {
        Integer id = socketDto.getId();
        String content = socketDto.getContent();
        SocketDto result = new SocketDto(id, content);
        return result;
    }

    @MessageMapping("/{project_id}/focus")
    @SendTo("/sub/{project_id}/focus")
    public SocketDto FocusChange(@DestinationVariable("project_id") Long project_id, SocketDto socketDto) {
        Integer id = socketDto.getId();
        String content = socketDto.getContent();
        SocketDto result = new SocketDto(id, content);
        return result;
    }

}
