package com.melnikov.auth_service.controller;

import com.melnikov.auth_service.config.SecurityConfig;
import com.melnikov.auth_service.jwt.JwtTokenProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TestController.class)
@Import({SecurityConfig.class, JwtTokenProvider.class})
public class TestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(roles = "USER")
    void shouldAccessSecuredEndpoint() throws Exception {
        mockMvc.perform(get("/api/test/secured"))
                .andExpect(status().isOk())
                .andExpect(content().string("This is a secured endpoint"));
    }

    @Test
    void shouldDenyAccessToSecuredEndpointForUnauthenticatedUser() throws Exception {
        mockMvc.perform(get("/api/test/secured"))
                .andExpect(status().isForbidden());
    }
}
