package com.duoc.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
public class DataSourceConfig {
    
    @Value("${spring.datasource.username}")
    private String username;
    
    @Value("${spring.datasource.password}")
    private String password;
    
    @Bean
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();
        
        // URL completa con referencia al wallet
        String jdbcUrl = "jdbc:oracle:thin:@(DESCRIPTION=" +
                "(RETRY_COUNT=20)(RETRY_DELAY=3)" +
                "(ADDRESS=(PROTOCOL=TCPS)(PORT=1522)(HOST=adb.sa-santiago-1.oraclecloud.com))" +
                "(CONNECT_DATA=(SERVICE_NAME=g8a31cd18839088_bdjvalencia_high.adb.oraclecloud.com))" +
                "(SECURITY=(SSL_SERVER_DN_MATCH=yes)(MY_WALLET_DIRECTORY=/app/wallet)))";
        
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(username);
        config.setPassword(password);
        config.setDriverClassName("oracle.jdbc.OracleDriver");
        
        // Propiedades adicionales de Oracle
        Properties props = new Properties();
        props.setProperty("oracle.net.tns_admin", "/app/wallet");
        props.setProperty("oracle.net.wallet_location", "/app/wallet");
        props.setProperty("oracle.jdbc.fanEnabled", "false");
        config.setDataSourceProperties(props);
        
        // HikariCP settings
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(5);
        config.setConnectionTimeout(60000);
        config.setIdleTimeout(600000);
        config.setMaxLifetime(1800000);
        config.setConnectionTestQuery("SELECT 1 FROM DUAL");
        
        System.out.println("\n╔═══════════════════════════════════════════════╗");
        System.out.println("║     DATASOURCE CONFIGURADO MANUALMENTE        ║");
        System.out.println("╚═══════════════════════════════════════════════╝");
        System.out.println(" JDBC URL: " + jdbcUrl.substring(0, 60) + "...");
        System.out.println(" Username: " + username);
        System.out.println("═══════════════════════════════════════════════\n");
        
        return new HikariDataSource(config);
    }
}