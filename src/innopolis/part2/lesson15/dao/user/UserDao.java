package innopolis.part2.lesson15.dao.user;

import innopolis.part2.lesson15.model.User;

import java.util.List;

public interface UserDao {
    Long addUser(User user);

    User getUserById(Long id);

    List<User> getUsers();

    boolean updateUserById(User user);

    boolean deleteUserById(Long id);
}