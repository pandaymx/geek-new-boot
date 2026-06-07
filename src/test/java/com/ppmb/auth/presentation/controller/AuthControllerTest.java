package com.ppmb.auth.presentation.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ppmb.application.service.IdentityTokenProvider;
import com.ppmb.auth.application.service.RefreshTokenService;
import com.ppmb.auth.presentation.dto.RefreshTokenRequest;
import com.ppmb.auth.presentation.dto.TokenPairResponse;
import com.ppmb.security.config.SecurityConfiguration;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

@WebMvcTest(AuthController.class)
@Import(SecurityConfiguration.class)
@WithMockUser
@DisabledInAotMode
class AuthControllerTest {

    @Autowired private MockMvc mockMvc;

    @Autowired private ObjectMapper objectMapper;

    @MockitoBean private RefreshTokenService refreshTokenService;

    @MockitoBean private IdentityTokenProvider identityTokenProvider;

    @Test
    void refresh_ShouldReturnNewTokens_WhenRequestIsValid() throws Exception {
        String oldUuid = UUID.randomUUID().toString();
        String newUuid = UUID.randomUUID().toString();
        RefreshTokenRequest request = new RefreshTokenRequest(oldUuid);
        TokenPairResponse response = new TokenPairResponse("new-access-token", newUuid);

        when(refreshTokenService.rotateRefreshToken(oldUuid)).thenReturn(response);

        mockMvc.perform(
                        post("/auth/refresh")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.accessToken").value("new-access-token"))
                .andExpect(jsonPath("$.data.refreshToken").value(newUuid));
    }

    @Test
    void refresh_ShouldReturnBadRequest_WhenUuidIsInvalid() throws Exception {
        RefreshTokenRequest request = new RefreshTokenRequest("invalid-uuid-format");

        mockMvc.perform(
                        post("/auth/refresh")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void refresh_ShouldReturnBadRequest_WhenUuidIsBlank() throws Exception {
        RefreshTokenRequest request = new RefreshTokenRequest("");

        mockMvc.perform(
                        post("/auth/refresh")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
