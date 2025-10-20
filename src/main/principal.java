package main;

import java.io.*;
import java.util.*;

public class Principal {

    public static void main(String[] args) throws Exception {
        // Archivo de entrada
        File input = new File("src/recursos/puntuaciones.txt");
        if (!input.exists()) {
            System.out.println("No se encontró el archivo de puntuaciones.");
            return;
        }

        // Niveles del juego
        List<String> niveles = Arrays.asList("nivel1", "nivel2", "nivel3", "nivel4", "nivel5");
        List<Process> procesos = new ArrayList<>();

        // Carpeta de salida
        File carpetaSalida = new File("salida");
        if (!carpetaSalida.exists())
            carpetaSalida.mkdirs();

        // Lanzar un proceso por cada nivel
        for (String nivel : niveles) {
            // 🔧 Classpath corregido: -cp src
            ProcessBuilder pb = new ProcessBuilder(
    "java", "-cp", "bin", "main.ProcesarNivel", nivel
);


            pb.redirectOutput(new File("salida/ranking_" + nivel + ".txt"));
            pb.redirectErrorStream(true);
            pb.directory(new File(".")); // Usa la carpeta actual del proyecto

            Process proceso = pb.start();
            procesos.add(proceso);
        }

        // Esperar a que todos los subprocesos terminen
        for (Process p : procesos) {
            p.waitFor();
        }

        // Mostrar ranking global
        mostrarRankingGlobal();
    }

    // Combina los rankings individuales en un Top Global
    private static void mostrarRankingGlobal() throws IOException {
        System.out.println("\n=== TOP GLOBAL DE JUGADORES ===");
        File carpeta = new File("salida");
        List<String> todasLineas = new ArrayList<>();

        for (File f : Objects.requireNonNull(carpeta.listFiles())) {
            todasLineas.addAll(java.nio.file.Files.readAllLines(f.toPath()));
        }

        Map<String, Integer> puntuaciones = new HashMap<>();
        for (String linea : todasLineas) {
            if (linea.contains(",")) {
                String[] partes = linea.split(",");
                String nombre = partes[0].trim();
                try {
                    int puntos = Integer.parseInt(partes[1].trim());
                    puntuaciones.put(nombre, puntos);
                } catch (NumberFormatException ignored) {
                }
            }
        }

        puntuaciones.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(5)
                .forEach(e -> System.out.println(e.getKey() + " - " + e.getValue() + " pts"));
    }
}
