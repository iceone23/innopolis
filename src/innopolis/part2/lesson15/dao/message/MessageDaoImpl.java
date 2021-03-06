package innopolis.part2.lesson15.dao.message;

import innopolis.part2.lesson15.connection.ConnectionManager;
import innopolis.part2.lesson15.connection.ConnectionManagerJdbcImpl;
import innopolis.part2.lesson15.model.Message;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * MessageDaoImpl
 *
 * @author Stanislav_Klevtsov
 */
public class MessageDaoImpl implements MessageDao {

    private static final ConnectionManager connectionManager =
            ConnectionManagerJdbcImpl.getInstance();

    @Override
    public Long addMessage(Message message) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "INSERT INTO messages values (DEFAULT, ?, ?, ?, ?)",
                     Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, message.getText());
            preparedStatement.setLong(2, message.getSenderId());
            preparedStatement.setLong(3, message.getRecipientId());
            preparedStatement.setLong(4, message.getAdId());
            preparedStatement.executeUpdate();

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getLong(1);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0L;
    }

    @Override
    public Message getMessageById(Long id) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT * FROM messages WHERE id = ?")) {
            preparedStatement.setLong(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return new Message(
                            resultSet.getLong(1),
                            resultSet.getString(2),
                            resultSet.getLong(3),
                            resultSet.getLong(4),
                            resultSet.getLong(5));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Message> getMessages() {
        List<Message> messages = new ArrayList<Message>();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT * FROM messages ORDER BY id")) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    messages.add(
                            new Message(
                                    resultSet.getLong(1),
                                    resultSet.getString(2),
                                    resultSet.getLong(3),
                                    resultSet.getLong(4),
                                    resultSet.getLong(5))
                    );
                }
                return messages;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean updateMessageById(Message message) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "UPDATE messages SET text=?, senderId=?, recipientId=?, adId=? " +
                             "WHERE id=?")) {
            preparedStatement.setString(1, message.getText());
            preparedStatement.setLong(2, message.getSenderId());
            preparedStatement.setLong(3, message.getRecipientId());
            preparedStatement.setLong(4, message.getAdId());
            preparedStatement.setLong(5, message.getId());
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteMessageById(Long id) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "DELETE FROM messages WHERE id=?")) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}