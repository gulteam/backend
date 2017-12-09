package ru.nsu.gulteam.prof_standards.backend.web.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Token;
import ru.nsu.gulteam.prof_standards.backend.domain.node.User;
import ru.nsu.gulteam.prof_standards.backend.domain.repository.TokenRepository;
import ru.nsu.gulteam.prof_standards.backend.domain.repository.UserRepository;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Collections;

@Configuration
public class TokenAuthenticationFilter extends GenericFilterBean {
    private TokenRepository tokenRepository;
    private UserRepository userRepository;

    @Autowired
    public TokenAuthenticationFilter(TokenRepository tokenRepository, UserRepository userRepository) {
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain)
            throws IOException, ServletException {

        final HttpServletRequest httpRequest = (HttpServletRequest) request;
        final String accessToken = httpRequest.getHeader("X-Auth-Token");
        if (null != accessToken) {
            Token token = tokenRepository.findByData(accessToken);

            if (null != token) {
                User owner = tokenRepository.getOwner(token);

                if (owner != null) {
                    final org.springframework.security.core.userdetails.User user =
                            new org.springframework.security.core.userdetails.User(
                                    owner.getLogin(),
                                    owner.getPasswordHash(),
                                    true,
                                    true,
                                    true,
                                    true,
                                    Collections.emptyList());

                    final UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }

        chain.doFilter(request, response);
    }
}