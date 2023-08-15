package com.stage.elearning.repository;

import com.stage.elearning.model.user.UserEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Repository
@Transactional(readOnly = true)
public interface UserEntityRepository extends JpaRepository<UserEntity, UUID> {


    @Query(value = "SELECT U FROM UserEntity  U WHERE U.id = :id")
    Optional<UserEntity> fetchUserWithId(@Param("id") UUID id);
    @Query(value = "SELECT U FROM UserEntity U WHERE  U.email = :email ")
    Optional<UserEntity> fetchUserWithEmail(@Param("email") String email);

    @Query(value = "SELECT U FROM UserEntity  U WHERE U.fullName = :fullName")
    Optional<UserEntity> fetchUserWithFullName(@Param("fullName") String fullName);
    @Query(value = "SELECT U FROM UserEntity U " +
            "WHERE COALESCE(:fullName, U.fullName) = U.fullName " +
            "AND COALESCE(:address, U.address) = U.address " +
            "AND COALESCE(:email, U.email) = U.email " +
            "AND COALESCE(:phoneNumber, U.phoneNumber) = U.phoneNumber " +
            "AND U.role.name != 'ADMIN' ")
    List<UserEntity> searchUsers(
            @Param("fullName") String fullName,
            @Param("address") String address,
            @Param("email") String email,
            @Param("phoneNumber") String phoneNumber
    );


    @Query(value = "SELECT U FROM UserEntity U where U.role.name != 'ADMIN' order by U.id ")
    List<UserEntity> fetchAllUsers(Pageable pageable);

    @Query("SELECT U FROM UserEntity U WHERE U.fullName LIKE :prefix%")
    List<UserEntity> fetchUsersWithFullNamePrefix(@Param("prefix")String prefix);
    @Query("SELECT U FROM UserEntity U WHERE U.email LIKE :prefix%")
    List<UserEntity> fetchUsersWithEmailPrefix(@Param("prefix")String prefix);
    @Query("SELECT U FROM UserEntity U WHERE U.phoneNumber LIKE :prefix%")
    List<UserEntity> fetchUsersWithPhoneNumberPrefix(@Param("prefix")String prefix);

    @Query(value = "SELECT EXISTS(SELECT U FROM UserEntity U WHERE  U.fullName = :fullName) AS RESULT")
    Boolean isFullNameRegistered(@Param("fullName")String fullName);
    @Query(value = "SELECT EXISTS(SELECT U FROM UserEntity U WHERE  U.email = :email) AS RESULT")
    Boolean isEmailRegistered(@Param("email")String email);
    @Query(value = "SELECT EXISTS(SELECT U FROM UserEntity U WHERE  U.phoneNumber = :phoneNumber) AS RESULT")
    Boolean isPhoneNumberRegistered(@Param("phoneNumber")String phoneNumber);


    @Query(value = "SELECT COUNT(U) FROM UserEntity U")
    long getTotalUserEntityCount();

}
