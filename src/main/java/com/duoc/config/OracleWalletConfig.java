package com.duoc.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import jakarta.annotation.PostConstruct;
import java.io.File;

@Configuration
public class OracleWalletConfig {
    
    @Value("${oracle.wallet.location:/app/wallet}")
    private String walletLocation;
    
    @PostConstruct
    public void init() {
        try {
            // Usar la ruta configurada (funciona tanto en local como en Docker)
            File walletDir = new File(walletLocation);
            String walletPath = walletDir.getAbsolutePath();
            
            // Verificar que existe
            if (!walletDir.exists()) {
                System.err.println("ERROR: Wallet no encontrado en: " + walletPath);
                System.err.println("Intentando ruta alternativa...");
                
                // Fallback para desarrollo local
                walletDir = new File("src/main/resources/wallet");
                walletPath = walletDir.getAbsolutePath();
                
                if (!walletDir.exists()) {
                    System.err.println("ERROR: Wallet tampoco encontrado en: " + walletPath);
                    throw new RuntimeException("Wallet directory not found in any location");
                }
            }
            
            // Configurar propiedades del sistema ANTES de cualquier conexión
            System.setProperty("oracle.net.tns_admin", walletPath);
            System.setProperty("oracle.net.wallet_location", walletPath);
            
            // Log de confirmación
            System.out.println("\n╔═══════════════════════════════════════════════╗");
            System.out.println("║   ORACLE WALLET CONFIGURED                    ║");
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