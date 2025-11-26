package com.hms.repository;

import com.hms.exception.DatabaseException;
import com.hms.domain.User;
import java.sql.*;


public class UserDAO {
	private DBHandler dbHandler;

	public UserDAO() {
		this.dbHandler = DBHandler.getInstance();
	}

    /**
     * Authenticate user by username and password
     * @param username Username
     * @param password Password
     * @return User object if authenticated, null otherwise
     * @throws DatabaseException if database operation fails
     */
    public User authenticateUser(String username, String password) throws DatabaseException {
        String sql = "SELECT * FROM User WHERE username = ? AND password = ?";
        
        try (Connection conn = dbHandler.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUser(rs);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error authenticating user: " + e.getMessage(), e);
        }
        return null;
    }

    /**
     * Get user by username
     * @param username Username
     * @return User object or null if not found
     * @throws DatabaseException if database operation fails
     */
    public User getUserByUsername(String username) throws DatabaseException {
        String sql = "SELECT * FROM User WHERE username = ?";
        
        try (Connection conn = dbHandler.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUser(rs);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error retrieving user: " + e.getMessage(), e);
        }
        return null;
    }

    /**
     * Map ResultSet to User object
     */
    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setUserId(rs.getInt("user_id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setFullName(rs.getString("full_name"));
        user.setRole(User.Role.valueOf(rs.getString("role")));
        user.setEmail(rs.getString("email"));
        user.setPhone(rs.getString("phone"));
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            user.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        return user;
    }

}

