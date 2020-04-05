package server;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;


import javax.sql.DataSource;
import java.sql.DriverManager;
import java.sql.SQLException;

@Configuration
public class SpringConfig {


    @Bean
    public AuthService authService(DataSource dataSource) throws SQLException {
        return new AuthService(dataSource);
    }

    @Bean
    public Server server(AuthService authService) {
        return new Server(authService);
    }
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setUrl("jdbc:sqlite:mainDB.db");
        ds.setUsername("root");
        ds.setPassword("root");
        ds.setDriverClassName("org.sqlite.JDBC");
        return ds;
    }


}
