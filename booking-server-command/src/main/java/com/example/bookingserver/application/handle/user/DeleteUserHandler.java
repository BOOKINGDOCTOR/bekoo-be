package com.example.bookingserver.application.handle.user;

import com.example.bookingserver.application.command.user.DeleteUserCommand;
import com.example.bookingserver.application.event.user.DeleteUserEvent;
import com.example.bookingserver.application.handle.Handler;
import com.example.bookingserver.domain.OutboxEvent;
import com.example.bookingserver.domain.repository.OutboxEventRepository;
import com.example.bookingserver.infrastructure.constant.ApplicationConstant;
import com.example.bookingserver.infrastructure.mapper.UserMapper;
import com.example.bookingserver.domain.repository.UserRepository;
import com.example.bookingserver.infrastructure.message.MessageProducer;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DeleteUserHandler implements Handler<DeleteUserCommand> {

    final UserRepository userRepository;
    final UserMapper userMapper;
    final MessageProducer messageProducer;
    final ObjectMapper objectMapper;
    final OutboxEventRepository outboxEventRepository;
    final String TOPIC="delete-user-event";

    @SneakyThrows
    @Override
    public void execute(DeleteUserCommand command) {
        List<String> ids= command.getIds();
        for(String id: ids){
            userRepository.delete(id);
        }

        DeleteUserEvent event= DeleteUserEvent.builder()
                .ids(ids)
                .build();
        String content= objectMapper.writeValueAsString(event);
        OutboxEvent outboxEvent= OutboxEvent.builder()
                .topic(TOPIC)
                .eventType("DELETE")
                .aggregateId("null")
                .aggregateType("User")
                .content(content)
                .status(ApplicationConstant.EventStatus.PENDING)
                .build();

        try {
            messageProducer.sendMessage(TOPIC, content);
            outboxEvent.setStatus(ApplicationConstant.EventStatus.SEND);
            log.info("SEND EVENT SUCCESS: {}", TOPIC);
        }catch (Exception e){
            log.error("SEND EVENT FAILED: {}", TOPIC );
        }
        outboxEventRepository.save(outboxEvent);
    }
}
