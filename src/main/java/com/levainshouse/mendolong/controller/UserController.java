package com.levainshouse.mendolong.controller;

import com.levainshouse.mendolong.constant.WebProperties;
import com.levainshouse.mendolong.dto.SuccessResponse;
import com.levainshouse.mendolong.dto.address.AddressRegisterRequest;
import com.levainshouse.mendolong.dto.address.AddressRegisterResponse;
import com.levainshouse.mendolong.dto.user.UserSignInRequest;
import com.levainshouse.mendolong.dto.user.UserSignInResponse;
import com.levainshouse.mendolong.entity.Address;
import com.levainshouse.mendolong.entity.User;
import com.levainshouse.mendolong.service.AddressService;
import com.levainshouse.mendolong.service.TokenService;
import com.levainshouse.mendolong.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/api/users")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AddressService addressService;
    private final TokenService tokenService;

    @PostMapping(value = "/sign-in",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessResponse> signIn(
            @Valid @RequestBody UserSignInRequest signInRequest){
        User findUser = userService.validateNewUser(signInRequest);
        if(findUser == null){
            User savedUser = userService.signIn(signInRequest);
            String accessToken = tokenService.issue(savedUser);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .header("authorization", accessToken)
                    .body(new SuccessResponse(
                            HttpStatus.CREATED.name(),
                            WebProperties.SUCCESS_RESPONSE_MESSAGE,
                            new UserSignInResponse(savedUser)
                    ));
        } else {
            String accessToken = tokenService.issue(findUser);
            return ResponseEntity.ok()
                    .header("authorization", accessToken)
                    .body(new SuccessResponse(
                            HttpStatus.OK.name(),
                            WebProperties.SUCCESS_RESPONSE_MESSAGE,
                            new UserSignInResponse(findUser)
                    ));
        }
    }

    @PostMapping(value = "/sign-in/address",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessResponse> signInAddress(
            @RequestHeader("authorization") String accessToken,
            @Valid @RequestBody List<AddressRegisterRequest> registerRequests){
        User user = userService.getUser(tokenService.verify(accessToken));
        List<Address> addresses = addressService.register(registerRequests, user);

        List<AddressRegisterResponse> registerResponses = addresses.stream()
                .map(AddressRegisterResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok()
                .body(new SuccessResponse(
                        HttpStatus.OK.name(),
                        WebProperties.SUCCESS_RESPONSE_MESSAGE,
                        registerResponses
                ));
    }
}
