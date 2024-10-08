package com.example.bookingserver.infrastructure.mapper;

import com.example.bookingserver.application.command.user.CreateUserCommand;
import com.example.bookingserver.application.command.user.DeleteUserCommand;
import com.example.bookingserver.application.command.user.UpdateInfoUserCommand;
import com.example.bookingserver.application.event.user.CreateUserEvent;
import com.example.bookingserver.application.event.user.DeleteUserEvent;
import com.example.bookingserver.application.event.user.UpdateAvatarUserEvent;
import com.example.bookingserver.application.event.user.UpdateInfoUserEvent;
import com.example.bookingserver.application.reponse.UserResponse;
import com.example.bookingserver.domain.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE= Mappers.getMapper(UserMapper.class);

    @Mapping(target = "dob", source = "dob", qualifiedByName = "convertStringToLocalDate")
    @Named("convertStringToLocalDate")
    default LocalDate toLocalDate(String date){
        return LocalDate.parse(date);
    }
    User toUserFromCreateCommand(CreateUserCommand command);

    void updateUser(@MappingTarget User user, UpdateInfoUserCommand command);

    UserResponse toResponse(User user);

    CreateUserEvent fromUserToCreateUserEvent(User user);
    UpdateAvatarUserEvent fromUserToUpdateAvatarEvent(User user);
    UpdateInfoUserEvent fromUserToUpdateUserEvent(User user);
    DeleteUserEvent fromCommandToEvent(DeleteUserCommand command);
}
