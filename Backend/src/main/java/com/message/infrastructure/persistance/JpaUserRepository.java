package com.message.infrastructure.persistance;

import com.message.domain.repositories.UserRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public class JpaUserRepository implements JpaRepository<UserEntity, Long>, UserRepository {

    

    
}
