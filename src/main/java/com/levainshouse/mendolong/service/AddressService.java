package com.levainshouse.mendolong.service;

import com.levainshouse.mendolong.dto.address.AddressRegisterRequest;
import com.levainshouse.mendolong.entity.Address;
import com.levainshouse.mendolong.entity.User;
import com.levainshouse.mendolong.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AddressService {

    private static final String TAG = "ADDRESS_SERVICE=%s";
    private final AddressRepository addressRepository;

    @Transactional
    public List<Address> register(List<AddressRegisterRequest> registerRequests, User user) {
        log.debug(String.format(TAG, "Register"));
        List<Address> addresses = registerRequests.stream()
                .map(a -> Address.builder()
                        .user(user)
                        .latitude(a.getLatitude())
                        .longitude(a.getLongitude())
                        .build())
                .collect(Collectors.toList());

        return addressRepository.saveAll(addresses);
    }
}
