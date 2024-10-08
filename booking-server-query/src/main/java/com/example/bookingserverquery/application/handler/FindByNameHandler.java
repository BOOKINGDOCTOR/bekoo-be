package com.example.bookingserverquery.application.handler;

import com.example.bookingserverquery.application.query.user.FindByNameQuery;
import com.example.bookingserverquery.application.reponse.user.FindByNameResponse;
import com.example.bookingserverquery.application.reponse.user.UserResponse;
import com.example.bookingserverquery.domain.User;
import com.example.bookingserverquery.domain.repository.UserRepository;
import com.example.bookingserverquery.infrastructure.mapper.UserMapper;
import com.example.bookingserverquery.infrastructure.repository.UserELRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class FindByNameHandler {

    final UserRepository userRepository;
    final UserMapper userMapper;

    public FindByNameResponse findByName(FindByNameQuery query){
        Pageable pageable= query.getPageable();

        Page<User> page= userRepository.findByName(query.getName(), pageable);

        List<UserResponse> userResponses= new ArrayList<>();
        for(User x: page.getContent()){
            UserResponse userResponse= userMapper.toResponse(x);
            userResponses.add(userResponse);
        }

        return FindByNameResponse.builder()
                .name(query.getName())
                .totalPage(page.getTotalPages())
                .pageSize(page.getSize())
                .pageIndex(page.getNumber())
                .orders(query.getOrders())
                .content(userResponses)
                .build();
    }
}
