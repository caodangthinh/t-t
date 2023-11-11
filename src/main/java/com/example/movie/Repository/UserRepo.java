package com.example.movie.Repository;

import com.example.movie.Entity.ShowTime;
import com.example.movie.Entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    @Query(value ="SELECT * FROM User WHERE user_id= ?1",  nativeQuery = true)
    User getUserByUserId(Long user_id);

    @Query("SELECT u FROM User u WHERE u.username = ?1")
    User findByUsername(String username);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO user_role (user_id, role_id) " +
            "VALUES (?1, ?2)",
            nativeQuery = true)
    void addRoleToUser(Long userId, Long roleId);

    //viết hàm lấy id của username
    @Query("SELECT u.userId FROM User u WHERE u.username = ?1")
    Long getUserIdByUsername(String username);

    //tên của các role của 1 user: các quyền của userId
    @Query(value = "SELECT r.Name " +
            "FROM roles r " +
            "INNER JOIN user_role ur ON r.role_id = ur.role_id " +
            "WHERE ur.user_id = ?1",
            nativeQuery = true)
    String[] getRolesOfUser(Long userId);

    //lay het role
    @Query(value =" SELECT DISTINCT  c.name FROM user a, user_role b, roles c WHERE a.user_id = b.user_id and b.role_id = c.role_id",  nativeQuery = true)
    String[] getAllRole();

    @Query(value ="SELECT role_id from user_role ORDER BY user_id",  nativeQuery = true)
    String[] getAllUserRole();

    //Lưu user role
    @Modifying
    @Transactional
    @Query(value ="UPDATE user_role SET user_id = ?1, role_id = ?2 WHERE user_id = ?1",  nativeQuery = true)
    void saveUserRole(Long user_id, Long role_id);

    default List<User> getAllUser(Integer pageNo,
                                  Integer pageSize,
                                  String sortBy){
        return findAll(PageRequest.of(pageNo,
                pageSize,
                Sort.by(sortBy)))
                .getContent();
    }

    @Query("""
            SELECT u
            FROM User u
            WHERE u.username LIKE %?1%
                OR u.fullName LIKE %?1%
                OR u.Email LIKE %?1%
            """)
    List<User> searchUser(String keyword);

}

