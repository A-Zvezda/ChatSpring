package server;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AuthService {
    private static Connection connection;
    private static Statement stmt;

    public static void connection() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:mainDB.db");
            stmt = connection.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getMaxID() {
        String sql = String.format("SELECT MAX(id) FROM main");

        try {
            ResultSet rs = stmt.executeQuery(sql);

            if (rs.next()) {
                String str = rs.getString(1);
                return rs.getString(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getUserIDbyNick(String nick) {
        String sql = String.format("SELECT id FROM main where nickname = '%s'", nick);

        try {
            ResultSet rs = stmt.executeQuery(sql);

            if (rs.next()) {
                String str = rs.getString(1);
                return rs.getString(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static int setUserInBlackList(String userId, String userIdForBlock) {

        String sql = String.format("INSERT INTO blackList (userId, userIdForBlock) VALUES (%s, %s)", userId, userIdForBlock);
        int rs = 0;
        try {
            rs = stmt.executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println(sql);
            e.printStackTrace();
        }

        return rs;
    }

    public static int setNewUsers(String login, String nick, String pass) {
        int hash = pass.hashCode();
        int maxID =  Integer.parseInt(Objects.requireNonNull(getMaxID())) + 1;
        String sql = String.format("INSERT INTO main (id, login, password, nickname) VALUES (%s,'%s', %s ,'%s')", maxID, login, hash,nick);
        int rs = 0;
        try {
            rs = stmt.executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println(sql);
            e.printStackTrace();
        }

        return rs;
    }

    public static List<String> getBlackListByNickName(String nick) {
        List<String> blackList = new ArrayList<>();
        String sql = String.format("SELECT m.nickname\n" +
                "FROM main \n" +
                "LEFT JOIN blackList as bl\n" +
                "ON main.id = bl.userId\n" +
                "LEFT Join  main as m\t\n" +
                "on m.id = bl.userIdForBlock\n" +
                "WHERE main.nickname = '%s'", nick);
        try {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                blackList.add(rs.getString(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return blackList;
    }

    public static boolean changeNick (String userId, String newNick) {
        boolean res;
        String sql = String.format("UPDATE main SET nickname = '%s'  WHERE id = %s ", newNick, userId);
        int rs = 0;
        try {
           rs = stmt.executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println(sql);
            e.printStackTrace();
        }
        if (rs != 0 ) {
            res = true;
        } else {
            res  = false;
        }
        return res;
    }

    public static boolean nickIsBusy (String nick) {
        String sql = String.format("SELECT Count(nickname) as nickCount FROM main  WHERE nickname = '%s' ", nick);
        int res = 1;
        try {
            ResultSet rs = stmt.executeQuery(sql);

            if (rs.next()) {
                 res = rs.getInt(1);
                //return rs.getString(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res!=0;
    }

    public static String getNickByLoginAndPass(String login, String pass) {
        int hash = pass.hashCode();
        String sql = String.format("SELECT nickname FROM main where login = '%s' and password = '%s'", login, hash);

        try {
            ResultSet rs = stmt.executeQuery(sql);

            if (rs.next()) {
                String str = rs.getString(1);
                return rs.getString(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getLogin(String login) {
        String sql = String.format("SELECT nickname FROM main where login = '%s'", login);

        try {
            ResultSet rs = stmt.executeQuery(sql);

            if (rs.next()) {
                String str = rs.getString(1);
                return rs.getString(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void disconnect() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
