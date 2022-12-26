package com.levainshouse.mendolong.repository;

import com.levainshouse.mendolong.entity.Address;
import com.levainshouse.mendolong.entity.User;
import com.levainshouse.mendolong.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long> {

    List<Address> findByUser(User user);

    @Query("select a from Address a where a.user.role <> :userRole")
    List<Address> findByOppositeUserRole(@Param("userRole") UserRole userRole);
}
