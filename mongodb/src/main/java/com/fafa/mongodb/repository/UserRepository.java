package com.fafa.mongodb.repository;

import com.fafa.mongodb.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Sire
 * @version 1.0
 * @date 2022-02-09 21:10
 */
@Repository
public interface UserRepository extends MongoRepository<User, String> {

}
