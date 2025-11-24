package com.duoc.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import jakarta.annotation.PostConstruct;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class OracleWalletConfig {
    
    @Value("${oracle.wallet.location:/app/wallet}")
    private String walletLocation;
    
    @PostConstruct
    public void init() {
        try {
            String walletPath = findWalletPath();
            
            if (walletPath == null) {
                throw new RuntimeException("Wallet directory not found in any location");
            }
            
            // Configurar propiedades del sistema ANTES de cualquier conexión
            System.setProperty("oracle.net.tns_admin", walletPath);
            System.setProperty("oracle.net.wallet_location", walletPath);
            
            // Verificar archivos requeridos
            verifyWalletFiles(walletPath);
            
            // Log de confirmación
            printConfiguration(walletPath);
            
        } catch (Exception e) {
            System.err.println("❌ Error configurando wallet: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to configure Oracle Wallet", e);
        }
    }
    
    private String findWalletPath() {
        // Prioridad 1: Ruta configurada
        File walletDir = new File(walletLocation);
        if (walletDir.exists() && walletDir.isDirectory()) {
            System.out.println("✅ Wallet encontrado en: " + walletLocation);
            return walletDir.getAbsolutePath();
        }
        
        System.err.println("⚠️  Wallet no encontrado en: " + walletLocation);
        
        // Prioridad 2: Desarrollo local (src/main/resources/wallet)
        walletDir = new File("src/main/resources/wallet");
        if (walletDir.exists() && walletDir.isDirectory()) {
            System.out.println("✅ Wallet encontrado en ruta de desarrollo: " + walletDir.getAbsolutePath());
            return walletDir.getAbsolutePath();
        }
        
        // Prioridad 3: Classpath resources (cuando está empaquetado en JAR)
        walletDir = new File("wallet");
        if (walletDir.exists() && walletDir.isDirectory()) {
            System.out.println("✅ Wallet encontrado en directorio raíz: " + walletDir.getAbsolutePath());
            return walletDir.getAbsolutePath();
        }
        
        // Prioridad 4: HOME del usuario
        String userHome = System.getProperty("user.home");
        walletDir = new File(userHome + "/wallet");
        if (walletDir.exists() && walletDir.isDirectory()) {
            System.out.println("✅ Wallet encontrado en home: " + walletDir.getAbsolutePath());
            return walletDir.getAbsolutePath();
        }
        
        System.err.println("❌ Wallet no encontrado en ninguna ubicación estándar");
        return null;
    }
    
    private void verifyWalletFiles(String walletPath) {
        System.out.println("\n📁 Verificando archivos del wallet...");
        
        String[] requiredFiles = {
            "tnsnames.ora",
            "sqlnet.ora",
            "cwallet.sso"
        };
        
        String[] optionalFiles = {
            "ewallet.p12",
            "keystore.jks",
            "truststore.jks",
            "ojdbc.properties"
        };
        
        boolean allRequired = true;
        
        for (String fileName : requiredFiles) {
            File file = new File(walletPath, fileName);
            if (file.exists() && file.canRead()) {
                System.out.println("   ✅ " + fileName + " (" + file.length() + " bytes)");
            } else {
                System.err.println("   ❌ " + fileName + " - NO ENCONTRADO O NO LEGIBLE");
                allRequired = false;
            }
        }
        
        for (String fileName : optionalFiles) {
            File file = new File(walletPath, fileName);
            if (file.exists() && file.canRead()) {
                System.out.println("   ✅ " + fileName + " (" + file.length() + " bytes)");
            } else {
                System.out.println("   ⚠️  " + fileName + " - opcional, no encontrado");
            }
        }
        
        if (!allRequired) {
            throw new RuntimeException("Faltan archivos requeridos en el wallet");
        }
        
        // Verificar contenido de tnsnames.ora
        verifyTnsNames(walletPath);
    }
    
    private void verifyTnsNames(String walletPath) {
        try {
            Path tnsPath = Paths.get(walletPath, "tnsnames.ora");
            String content = Files.readString(tnsPath);
            
            System.out.println("\n📝 Verificando tnsnames.ora...");
            
            String[] aliases = {"bdjvalencia_high", "bdjvalencia_medium", "bdjvalencia_low"};
            for (String alias : aliases) {
                if (content.contains(alias + " =")) {
                    System.out.println("   ✅ Alias encontrado: " + alias);
                } else {
                    System.out.println("   ⚠️  Alias no encontrado: " + alias);
                }
            }
            
            // Verificar que contiene el host de Oracle Cloud
            if (content.contains("adb.sa-santiago-1.oraclecloud.com")) {
                System.out.println("   ✅ Host Oracle Cloud configurado correctamente");
            } else {
                System.err.println("   ❌ Host Oracle Cloud no encontrado en tnsnames.ora");
            }
            
        } catch (Exception e) {
            System.err.println("⚠️  No se pudo verificar tnsnames.ora: " + e.getMessage());
        }
    }
    
    private void printConfiguration(String walletPath) {
        System.out.println("\n╔═══════════════════════════════════════════════╗");
        System.out.println("║        ORACLE WALLET CONFIGURED               ║");
        System.out.println("╚═══════════════════════════════════════════════╝");
        System.out.println(" Wallet Path: " + walletPath);
        System.out.println(" TNS Admin: " + System.getProperty("oracle.net.tns_admin"));
        System.out.println(" Wallet Location: " + System.getProperty("oracle.net.wallet_location"));
        System.out.println("═══════════════════════════════════════════════\n");
    }
}