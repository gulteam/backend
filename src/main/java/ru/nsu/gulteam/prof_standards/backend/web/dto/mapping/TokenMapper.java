package ru.nsu.gulteam.prof_standards.backend.web.dto.mapping;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Token;
import ru.nsu.gulteam.prof_standards.backend.domain.node.User;
import ru.nsu.gulteam.prof_standards.backend.web.dto.response.TokenDto;
import ru.nsu.gulteam.prof_standards.backend.web.dto.response.UserDto;

// Todo: check, why @Mapper not working
@Configuration
public class TokenMapper {

    @Bean
    public TokenMapper createTokenMapper() {
        return new TokenMapper();
    }

    public TokenDto toDto(Token token){
        TokenDto tokenDto = new TokenDto();

        tokenDto.setData(token.getData());

        return tokenDto;
    };
}
