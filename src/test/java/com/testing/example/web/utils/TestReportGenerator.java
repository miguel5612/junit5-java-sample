package com.testing.example.web.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Generador de reportes HTML para tests de automatización web.
 * Crea un reporte visual con los resultados de los tests ejecutados.
 */
public class TestReportGenerator {

    private final String reportTitle;
    private final List<TestResult> testResults;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public TestReportGenerator(String reportTitle) {
        this.reportTitle = reportTitle;
        this.testResults = new ArrayList<>();
    }

    /**
     * Inicia el reporte (registra hora de inicio).
     */
    public void startReport() {
        this.startTime = LocalDateTime.now();
    }

    /**
     * Finaliza el reporte (registra hora de fin).
     */
    public void endReport() {
        this.endTime = LocalDateTime.now();
    }

    /**
     * Agrega un resultado de test al reporte.
     */
    public void addTestResult(String testName, String status, String details) {
        testResults.add(new TestResult(testName, status, details));
    }

    /**
     * Genera y guarda el reporte HTML en el archivo especificado.
     */
    public void saveReport(String filePath) {
        try {
            File file = new File(filePath);
            file.getParentFile().mkdirs();

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(generateHtmlReport());
            }

            System.out.println("📊 Reporte generado exitosamente: " + file.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Error al guardar el reporte: " + e.getMessage());
        }
    }

    /**
     * Genera el contenido HTML del reporte.
     */
    private String generateHtmlReport() {
        StringBuilder html = new StringBuilder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        long passedTests = testResults.stream().filter(r -> "PASSED".equals(r.status)).count();
        long failedTests = testResults.stream().filter(r -> "FAILED".equals(r.status)).count();
        double successRate = testResults.isEmpty() ? 0 : (passedTests * 100.0 / testResults.size());

        html.append("<!DOCTYPE html>\n");
        html.append("<html lang='es'>\n");
        html.append("<head>\n");
        html.append("    <meta charset='UTF-8'>\n");
        html.append("    <meta name='viewport' content='width=device-width, initial-scale=1.0'>\n");
        html.append("    <title>").append(reportTitle).append("</title>\n");
        html.append("    <style>\n");
        html.append(getStyleSheet());
        html.append("    </style>\n");
        html.append("</head>\n");
        html.append("<body>\n");

        // Header
        html.append("    <div class='header'>\n");
        html.append("        <h1>").append(reportTitle).append("</h1>\n");
        html.append("        <p class='subtitle'>Reporte de Automatización Web con Selenium</p>\n");
        html.append("    </div>\n");

        // Summary
        html.append("    <div class='summary'>\n");
        html.append("        <h2>📊 Resumen de Ejecución</h2>\n");
        html.append("        <div class='summary-grid'>\n");
        html.append("            <div class='summary-item'>\n");
        html.append("                <span class='label'>Inicio:</span>\n");
        html.append("                <span class='value'>").append(startTime.format(formatter)).append("</span>\n");
        html.append("            </div>\n");
        html.append("            <div class='summary-item'>\n");
        html.append("                <span class='label'>Fin:</span>\n");
        html.append("                <span class='value'>").append(endTime.format(formatter)).append("</span>\n");
        html.append("            </div>\n");
        html.append("            <div class='summary-item'>\n");
        html.append("                <span class='label'>Total Tests:</span>\n");
        html.append("                <span class='value'>").append(testResults.size()).append("</span>\n");
        html.append("            </div>\n");
        html.append("            <div class='summary-item'>\n");
        html.append("                <span class='label'>✅ Exitosos:</span>\n");
        html.append("                <span class='value passed'>").append(passedTests).append("</span>\n");
        html.append("            </div>\n");
        html.append("            <div class='summary-item'>\n");
        html.append("                <span class='label'>❌ Fallidos:</span>\n");
        html.append("                <span class='value failed'>").append(failedTests).append("</span>\n");
        html.append("            </div>\n");
        html.append("            <div class='summary-item'>\n");
        html.append("                <span class='label'>Tasa de Éxito:</span>\n");
        html.append("                <span class='value'>").append(String.format("%.1f%%", successRate)).append("</span>\n");
        html.append("            </div>\n");
        html.append("        </div>\n");
        html.append("    </div>\n");

        // Test Results
        html.append("    <div class='results'>\n");
        html.append("        <h2>🧪 Resultados Detallados</h2>\n");
        html.append("        <table>\n");
        html.append("            <thead>\n");
        html.append("                <tr>\n");
        html.append("                    <th>#</th>\n");
        html.append("                    <th>Test</th>\n");
        html.append("                    <th>Estado</th>\n");
        html.append("                    <th>Detalles</th>\n");
        html.append("                </tr>\n");
        html.append("            </thead>\n");
        html.append("            <tbody>\n");

        for (int i = 0; i < testResults.size(); i++) {
            TestResult result = testResults.get(i);
            String statusClass = result.status.equals("PASSED") ? "passed" : "failed";
            String statusIcon = result.status.equals("PASSED") ? "✅" : "❌";

            html.append("                <tr>\n");
            html.append("                    <td>").append(i + 1).append("</td>\n");
            html.append("                    <td class='test-name'>").append(result.testName).append("</td>\n");
            html.append("                    <td class='status ").append(statusClass).append("'>");
            html.append(statusIcon).append(" ").append(result.status).append("</td>\n");
            html.append("                    <td class='details'>").append(result.details).append("</td>\n");
            html.append("                </tr>\n");
        }

        html.append("            </tbody>\n");
        html.append("        </table>\n");
        html.append("    </div>\n");

        // Footer
        html.append("    <div class='footer'>\n");
        html.append("        <p>Generado por JUnit 5 + Selenium WebDriver</p>\n");
        html.append("        <p>Proyecto Base de Testing - Principios SOLID</p>\n");
        html.append("    </div>\n");

        html.append("</body>\n");
        html.append("</html>");

        return html.toString();
    }

    /**
     * Obtiene la hoja de estilos CSS para el reporte.
     */
    private String getStyleSheet() {
        return """
            * {
                margin: 0;
                padding: 0;
                box-sizing: border-box;
            }

            body {
                font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                padding: 20px;
                min-height: 100vh;
            }

            .header {
                background: white;
                padding: 30px;
                border-radius: 10px;
                box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
                margin-bottom: 20px;
                text-align: center;
            }

            .header h1 {
                color: #2d3748;
                font-size: 2.5em;
                margin-bottom: 10px;
            }

            .subtitle {
                color: #718096;
                font-size: 1.2em;
            }

            .summary {
                background: white;
                padding: 30px;
                border-radius: 10px;
                box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
                margin-bottom: 20px;
            }

            .summary h2 {
                color: #2d3748;
                margin-bottom: 20px;
                font-size: 1.8em;
            }

            .summary-grid {
                display: grid;
                grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
                gap: 20px;
            }

            .summary-item {
                background: #f7fafc;
                padding: 20px;
                border-radius: 8px;
                border-left: 4px solid #667eea;
            }

            .summary-item .label {
                display: block;
                color: #718096;
                font-size: 0.9em;
                margin-bottom: 8px;
            }

            .summary-item .value {
                display: block;
                color: #2d3748;
                font-size: 1.8em;
                font-weight: bold;
            }

            .summary-item .value.passed {
                color: #38a169;
            }

            .summary-item .value.failed {
                color: #e53e3e;
            }

            .results {
                background: white;
                padding: 30px;
                border-radius: 10px;
                box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
                margin-bottom: 20px;
            }

            .results h2 {
                color: #2d3748;
                margin-bottom: 20px;
                font-size: 1.8em;
            }

            table {
                width: 100%;
                border-collapse: collapse;
            }

            thead {
                background: #667eea;
                color: white;
            }

            th, td {
                padding: 15px;
                text-align: left;
                border-bottom: 1px solid #e2e8f0;
            }

            th {
                font-weight: 600;
                text-transform: uppercase;
                font-size: 0.85em;
                letter-spacing: 0.5px;
            }

            tbody tr:hover {
                background: #f7fafc;
            }

            .test-name {
                font-weight: 500;
                color: #2d3748;
            }

            .status {
                font-weight: bold;
            }

            .status.passed {
                color: #38a169;
            }

            .status.failed {
                color: #e53e3e;
            }

            .details {
                color: #718096;
                font-size: 0.9em;
                max-width: 400px;
                overflow: hidden;
                text-overflow: ellipsis;
                white-space: nowrap;
            }

            .footer {
                background: white;
                padding: 20px;
                border-radius: 10px;
                box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
                text-align: center;
                color: #718096;
            }

            .footer p {
                margin: 5px 0;
            }

            @media (max-width: 768px) {
                .summary-grid {
                    grid-template-columns: 1fr;
                }

                table {
                    font-size: 0.9em;
                }

                th, td {
                    padding: 10px;
                }
            }
        """;
    }

    /**
     * Clase interna para representar un resultado de test.
     */
    private static class TestResult {
        final String testName;
        final String status;
        final String details;

        TestResult(String testName, String status, String details) {
            this.testName = testName;
            this.status = status;
            this.details = details;
        }
    }
}
