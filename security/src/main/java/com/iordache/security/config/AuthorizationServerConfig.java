package com.iordache.security.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@AllArgsConstructor
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {


    private final AuthenticationManager authenticationManager;


    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.passwordEncoder(NoOpPasswordEncoder.getInstance());
        super.configure(security);
    }

  //  http://localhost:8080/oauth/authorize?response_type=code&client_id=client1&scope=read
  //  http://localhost:8080/oauth/token?grant_type=authorization_code&scope=read&code=.....codul pt a obtine token-ul
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("client1")
                .secret("secret1")
                .scopes("read")
                .authorizedGrantTypes("authorization_code", "refresh_token")
                .redirectUris("http://localhost:9090");
    }


    @Bean
    public TokenStore tokenStore(){
        var tokenStore = new JwtTokenStore(converter());
        return tokenStore;
    }

    @Bean
    public JwtAccessTokenConverter converter(){
        return new JwtAccessTokenConverter();
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager)
                .tokenStore(tokenStore())
                .accessTokenConverter(converter());
    }
}