package ru.nsu.gulteam.prof_standards.backend.web.dto.mapping;

import org.mapstruct.Mapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Token;
import ru.nsu.gulteam.prof_standards.backend.domain.node.User;
import ru.nsu.gulteam.prof_standards.backend.web.dto.response.TokenDto;
import ru.nsu.gulteam.prof_standards.backend.web.dto.response.UserDto;

@Mapper(componentModel = "spring")
public interface TokenMapper {
    TokenDto toDto(Token token);
}
