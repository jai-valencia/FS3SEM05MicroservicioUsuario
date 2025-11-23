package com.duoc.config;


import org.springframework.context.annotation.Configuration;
import jakarta.annotation.PostConstruct;
import java.io.File;

@Configuration
public class OracleWalletConfig {
    
    @PostConstruct
    public void init() {
        try {
            // Obtener ruta absoluta al wallet
            File walletDir = new File("src/main/resources/wallet");
            String walletPath = walletDir.getAbsolutePath();
            
            // Verificar que existe
            if (!walletDir.exists()) {
                System.err.println("ERROR: Wallet no encontrado en: " + walletPath);
                throw new RuntimeException("Wallet directory not found");
            }
            
            // Configurar propiedades del sistema ANTES de cualquier conexión
            System.setProperty("oracle.net.tns_admin", walletPath);
            System.setProperty("oracle.net.wallet_location", walletPath);
            
            // Log de confirmación
            System.out.println("\n╔═══════════════════════════════════════════════╗");
            System.out.println("║   ORACLE WALLET CONFIGURED                ║");
            System.out.println("╚═══════════════════════════════════════════════╝");
            System.out.println(" Wallet Path: " + walletPath);
            System.out.println(" TNS Admin: " + System.getProperty("oracle.net.tns_admin"));
            System.out.println("═══════════════════════════════════════════════\n");
            
        } catch (Exception e) {
            System.err.println("Error configurando wallet: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to configure Oracle Wallet", e);
        }
    }
}
