package hn.uth.serverlet;

import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;

@WebServlet("/binario")
public class binario extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String tipo = request.getParameter("tipo");
        String numero = request.getParameter("numero");
        String cambiarModo = request.getParameter("cambiarModo");
        String resultado = "";
        String error = "";

        // Si se hizo clic en el botón de cambiar modo, solo alternar el tipo sin convertir
        if (cambiarModo != null && "true".equals(cambiarModo)) {
            if ("binarioADecimal".equals(tipo)) {
                tipo = "decimalABinario";
            } else {
                tipo = "binarioADecimal";
            }
            // Solo cambiar el modo, no hacer conversión
            mostrarVista(request, response, tipo, numero, "", "");
            return;
        }

        // Si no es cambiar modo, entonces es convertir
        try {
            if ("decimalABinario".equals(tipo)) {
                if (numero == null || numero.trim().isEmpty()) {
                    error = "Debe ingresar un número decimal.";
                } else {
                    double decimal = Double.parseDouble(numero);
                    resultado = convertirDecimalABinario(decimal);
                }
            } else if ("binarioADecimal".equals(tipo)) {
                if (numero == null || numero.trim().isEmpty()) {
                    error = "Debe ingresar un número binario.";
                } else if (!esBinarioValido(numero)) {
                    error = "Número binario inválido. Solo se permiten 0, 1 y el punto decimal.";
                } else {
                    double decimal = convertirBinarioADecimal(numero);
                    resultado = String.valueOf(decimal);
                    // Limpiar ceros innecesarios al final
                    if (resultado.contains(".")) {
                        resultado = resultado.replaceAll("0+$", "").replaceAll("\\.$", "");
                    }
                }
            } else {
                error = "Tipo de conversión no válido.";
            }
        } catch (NumberFormatException e) {
            error = "Número inválido: " + e.getMessage();
        } catch (Exception e) {
            error = "Error en la conversión: " + e.getMessage();
        }

        mostrarVista(request, response, tipo, numero, resultado, error);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        mostrarVista(request, response, "binarioADecimal", "", "", "");
    }

    private void mostrarVista(HttpServletRequest request, HttpServletResponse response,
                              String tipo, String numero, String resultado, String error) throws IOException {

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html lang='es'><head><meta charset='UTF-8'><meta name='viewport' content='width=device-width, initial-scale=1'>");
        out.println("<title>Conversión Binario/Decimal</title>");
        out.println("<style>");
        out.println("*,*::before,*::after{box-sizing:border-box}");
        out.println("body{margin:0;font-family:Inter,Segoe UI,Arial,sans-serif;min-height:100vh;display:flex;align-items:flex-start;justify-content:center;padding-top:40px;background:radial-gradient(1200px 600px at 10% 10%,#22d3ee22,transparent),radial-gradient(1200px 600px at 90% 90%,#a78bfa22,transparent),linear-gradient(135deg,#0ea5e9,#7c3aed) fixed}");
        out.println(".tarjeta{width:min(800px,95vw);background:#0b1020cc;backdrop-filter:blur(6px);border-radius:20px;box-shadow:0 25px 60px rgba(0,0,0,.35);overflow:hidden;color:#e5e7eb}");
        out.println(".cabecera{padding:28px 32px;background:linear-gradient(90deg,#22d3ee,#3b82f6,#8b5cf6);color:white}");
        out.println(".titulo{margin:0;font-weight:800;letter-spacing:.3px;font-size:clamp(20px,3.2vw,28px)}");
        out.println(".sub{margin:6px 0 0;font-weight:600;opacity:.95}");
        out.println(".contenido{padding:20px 32px}");
        out.println(".panel{background:#0f172a;border:1px solid #334155;border-radius:16px;padding:16px;margin-bottom:12px}");
        out.println(".panel h3{margin:0 0 12px;color:#93c5fd;font-weight:700}");
        out.println("input,select{width:100%;padding:10px 12px;border-radius:12px;border:1px solid #475569;background:#0b1220;color:#e5e7eb;outline:none;margin-bottom:10px}");
        out.println("input:focus,select:focus{border-color:#22d3ee;box-shadow:0 0 0 4px #22d3ee22}");
        out.println(".btn{appearance:none;border:0;border-radius:12px;padding:10px 16px;background:#3b82f6;color:#fff;font-weight:800;cursor:pointer;transition:.2s;text-decoration:none;display:inline-block;margin-top:8px}");
        out.println(".btn:hover{transform:translateY(-2px);box-shadow:0 10px 20px rgba(59,130,246,.35)}");
        out.println(".btn-convertir{background:#10b981}");
        out.println(".btn-convertir:hover{box-shadow:0 10px 20px rgba(16,185,129,.35)}");
        out.println(".btn-cambiar{background:#8b5cf6}");
        out.println(".btn-cambiar:hover{box-shadow:0 10px 20px rgba(139,92,246,.35)}");
        out.println(".error{background:#7f1d1d;border:1px solid #fecaca;color:#fee2e2;padding:8px 10px;border-radius:12px;margin-bottom:10px}");
        out.println("table{width:100%;border-collapse:collapse;margin-top:10px}");
        out.println("th,td{border:1px solid #334155;padding:10px;text-align:center}");
        out.println("th{background:#1e293b;color:#f8fafc;font-weight:700}");
        out.println("</style></head><body>");
        out.println("<div class='tarjeta'>");

        out.println("<div class='cabecera'>");
        out.println("<h1 class='titulo'>Conversión Binario/Decimal</h1>");
        out.println("<p class='sub'>Axel David Rubio Vega — Cuenta: 202210050019</p>");
        out.println("<p class='sub'>Edwin Rene Torres Hernandez  — Cuenta: 991051087</p>");
        out.println("</div>");

        out.println("<div class='contenido'>");
        if (!error.isEmpty()) out.println("<div class='error'>" + error + "</div>");

        out.println("<div class='panel'>");
        out.println("<h3>Selecciona el tipo de conversión e ingresa el número</h3>");
        out.println("<form method='post' action='" + request.getContextPath() + "/binario' id='conversionForm'>");

        out.println("<input type='hidden' name='tipo' id='tipo' value='" + (tipo != null ? tipo : "binarioADecimal") + "'>");

        out.println("<div style='margin-bottom: 15px;'>");
        out.println("<label>Tipo de conversión seleccionado:</label>");
        String textoBoton = "binarioADecimal".equals(tipo) ? "Binario a Decimal" : "Decimal a Binario";
        String descripcion = "binarioADecimal".equals(tipo) ?
                "Ingresa un número binario (ej: 101.101)" :
                "Ingresa un número decimal (ej: 5.625)";

        // Botón separado para cambiar modo - NO envía el formulario completo
        out.println("<button type='button' onclick='cambiarModo()' class='btn btn-cambiar' style='width: 100%;'>" + textoBoton + "</button>");
        out.println("</div>");

        out.println("<label>" + descripcion + ":</label>");
        out.println("<input type='text' name='numero' id='numeroInput' value='" + (numero != null ? numero : "") + "' required>");

        out.println("<div style='display:flex; gap:12px; margin-top:10px;'>");
        out.println("<button type='submit' class='btn btn-convertir'>Convertir</button>");
        //out.println("<a href='" + request.getContextPath() + "/MenuServlet' role='button' class='btn'>Volver al inicio</a>");
        out.println("<a class='btn' href='" + request.getContextPath() + "/menu'>Volver al menú</a>");
        out.println("</div>");

        out.println("</form>");
        out.println("</div>");

        if (!resultado.isEmpty()) {
            out.println("<div class='panel'>");
            out.println("<h3>Resultado</h3>");
            out.println("<table>");
            out.println("<tr><th>Entrada</th><th>Salida</th></tr>");
            String tipoConversion = "binarioADecimal".equals(tipo) ? "Decimal" : "Binario";
            out.println("<tr><td>" + numero + " (" + ("binarioADecimal".equals(tipo) ? "Binario" : "Decimal") + ")</td><td>" + resultado + " (" + tipoConversion + ")</td></tr>");
            out.println("</table>");
            out.println("</div>");
        }

        out.println("</div>");
        out.println("</div>");

        // JavaScript para manejar el cambio de modo
        out.println("<script>");
        out.println("function cambiarModo() {");
        out.println("  var form = document.getElementById('conversionForm');");
        out.println("  var tipoInput = document.getElementById('tipo');");
        out.println("  var numeroInput = document.getElementById('numeroInput');");
        out.println("  ");
        out.println("  // Crear un input hidden para indicar que solo queremos cambiar el modo");
        out.println("  var cambiarInput = document.createElement('input');");
        out.println("  cambiarInput.type = 'hidden';");
        out.println("  cambiarInput.name = 'cambiarModo';");
        out.println("  cambiarInput.value = 'true';");
        out.println("  form.appendChild(cambiarInput);");
        out.println("  ");
        out.println("  // Enviar el formulario");
        out.println("  form.submit();");
        out.println("}");
        out.println("</script>");

        out.println("</body></html>");
    }

    private boolean esBinarioValido(String binario) {
        if (binario == null || binario.isEmpty()) return false;

        int puntoCount = 0;
        for (int i = 0; i < binario.length(); i++) {
            char c = binario.charAt(i);
            if (c == '.') {
                puntoCount++;
                if (puntoCount > 1) return false;
            } else if (c != '0' && c != '1') {
                return false;
            }
        }
        return true;
    }

    private String convertirDecimalABinario(double numero) {
        boolean esNegativo = numero < 0;
        if (esNegativo) {
            numero = Math.abs(numero);
        }

        int parteEntera = (int) numero;
        double parteDecimal = numero - parteEntera;

        String binarioEntero;
        if (parteEntera == 0) {
            binarioEntero = "0";
        } else {
            binarioEntero = Integer.toBinaryString(parteEntera);
        }

        StringBuilder binarioDecimal = new StringBuilder();
        if (parteDecimal > 0) {
            binarioDecimal.append(".");
            for (int i = 0; i < 10 && parteDecimal > 0; i++) {
                parteDecimal *= 2;
                if (parteDecimal >= 1) {
                    binarioDecimal.append("1");
                    parteDecimal -= 1;
                } else {
                    binarioDecimal.append("0");
                }
            }
        }

        String resultado = binarioEntero + binarioDecimal.toString();
        return esNegativo ? "-" + resultado : resultado;
    }

    private double convertirBinarioADecimal(String binario) {
        boolean esNegativo = binario.startsWith("-");
        if (esNegativo) {
            binario = binario.substring(1);
        }

        String[] partes = binario.split("\\.");
        int parteEntera = Integer.parseInt(partes[0], 2);
        double parteDecimal = 0;

        if (partes.length > 1) {
            String fraccion = partes[1];
            for (int i = 0; i < fraccion.length(); i++) {
                if (fraccion.charAt(i) == '1') {
                    parteDecimal += 1 / Math.pow(2, i + 1);
                }
            }
        }

        double resultado = parteEntera + parteDecimal;
        return esNegativo ? -resultado : resultado;
    }
}