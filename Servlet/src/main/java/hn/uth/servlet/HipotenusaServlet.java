package hn.uth.serverlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/hipotenusa")
public class HipotenusaServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        mostrarFormulario(request, response, "", "", "", "");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String catetoA = request.getParameter("catetoA");
        String catetoB = request.getParameter("catetoB");
        String resultado = "";
        String error = "";

        try {
            double a = Double.parseDouble(catetoA);
            double b = Double.parseDouble(catetoB);

            if (a <= 0 || b <= 0) throw new NumberFormatException("Los catetos deben ser mayores que cero");
            
            double c = Math.sqrt(a*a + b*b);
            resultado = String.format("%.2f", c);

        } catch (NumberFormatException e) {
            error = "Error: " + e.getMessage();
        }

        mostrarFormulario(request, response, catetoA, catetoB, resultado, error);
    }

    private void mostrarFormulario(HttpServletRequest request, HttpServletResponse response,
                                   String catetoA, String catetoB, String resultado, String error) throws IOException {

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html lang='es'><head><meta charset='UTF-8'><meta name='viewport' content='width=device-width, initial-scale=1'>");
        out.println("<title>Calculadora de Hipotenusa</title>");
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
        out.println("input{width:100%;padding:10px 12px;border-radius:12px;border:1px solid #475569;background:#0b1220;color:#e5e7eb;outline:none;margin-bottom:10px}");
        out.println("input:focus{border-color:#22d3ee;box-shadow:0 0 0 4px #22d3ee22}");
        out.println(".btn{appearance:none;border:0;border-radius:12px;padding:10px 16px;background:#3b82f6;color:#fff;font-weight:800;cursor:pointer;transition:.2s;text-decoration:none;display:inline-block;margin-top:8px}");
        out.println(".btn:hover{transform:translateY(-2px);box-shadow:0 10px 20px rgba(59,130,246,.35)}");
        out.println(".error{background:#7f1d1d;border:1px solid #fecaca;color:#fee2e2;padding:8px 10px;border-radius:12px;margin-bottom:10px}");
        out.println("table{width:100%;border-collapse:collapse;margin-top:10px}");
        out.println("th,td{border:1px solid #334155;padding:10px;text-align:center}");
        out.println("th{background:#1e293b;color:#f8fafc;font-weight:700}");

        // Triángulo
        out.println(".triangulo-container{position:relative;width:250px;height:250px;margin:10px auto}");
        out.println(".label{position:absolute;color:#fff;font-weight:700}");
        out.println("</style></head><body>");
        out.println("<div class='tarjeta'>");

        out.println("<div class='cabecera'>");
        out.println("<h1 class='titulo'>Calculadora de Hipotenusa</h1>");
        out.println("<p class='sub'>Ledin Noe Mendez Reyes — Cuenta: 202310060827</p>");
        out.println("<p class='sub'>Bruno Alexander Cruz Perdomo — Cuenta: 202330010342</p>");

        out.println("</div>");

        // Contenido
        out.println("<div class='contenido'>");
        if (!error.isEmpty()) out.println("<div class='error'>" + error + "</div>");

        // Formulario
        out.println("<div class='panel'>");
        out.println("<h3>Introduce los catetos</h3>");
        out.println("<form method='post' action='" + request.getContextPath() + "/hipotenusa'>");
        out.println("<label>Cateto A:</label>");
        out.println("<input type='number' step='any' name='catetoA' value='" + catetoA + "' required min='0.01'>");
        out.println("<label>Cateto B:</label>");
        out.println("<input type='number' step='any' name='catetoB' value='" + catetoB + "' required min='0.01'>");

        out.println("<div style='display:flex; gap:12px; margin-top:10px;'>");
        out.println("<button type='submit' class='btn' " +
                "style='font-family:Inter,Segoe UI,Arial,sans-serif; font-weight:800; font-size:16px; line-height:1; height:44px; display:inline-flex; align-items:center; justify-content:center;'>Calcular</button>");
        out.println("<a href='" + request.getContextPath() + "/menu' role='button' class='btn' " +
                "style='font-family:Inter,Segoe UI,Arial,sans-serif; font-weight:800; font-size:16px; line-height:1; height:44px; display:inline-flex; align-items:center; justify-content:center; text-decoration:none;'>Volver al menú</a>");

        out.println("</div>");
        out.println("</form>");
        out.println("</div>");

        // Resultado y triángulo
        if (!resultado.isEmpty()) {
            double a = Double.parseDouble(catetoA);
            double b = Double.parseDouble(catetoB);
            double scale = 18; // Factor de escala más compacto
            int aPx = (int)(a * scale); // vertical
            int bPx = (int)(b * scale); // horizontal

            out.println("<div class='panel'>");
            out.println("<h3>Resultado</h3>");
            out.println("<table>");
            out.println("<tr><th>Operación</th><th>Catetos</th><th>Hipotenusa</th></tr>");
            out.println("<tr><td>Cálculo de Hipotenusa</td><td>A: " + catetoA + ", B: " + catetoB + "</td><td>" + resultado + "</td></tr>");
            out.println("</table>");

            // Triángulo visual, base horizontal
            out.println("<div class='triangulo-container' style='position:relative;width:" + (bPx + 50) + "px;height:" + (aPx + 50) + "px;margin:10px auto;'>");

            // Triángulo rectángulo: base horizontal (B), altura vertical (A)
            out.println("<div style='position:absolute;bottom:0;left:0;width:0;height:0;");
            out.println("border-bottom:" + aPx + "px solid #3b82f6;");  // cateto vertical
            out.println("border-right:" + bPx + "px solid transparent;"); // cateto horizontal
            out.println("'></div>");

            // Etiquetas de catetos e hipotenusa
            out.println("<div class='label' style='position:absolute;bottom:" + (aPx/2) + "px;left:-20px;color:#fff;font-weight:700;'>A: " + catetoA + "</div>");
            out.println("<div class='label' style='position:absolute;bottom:-20px;left:" + (bPx/2) + "px;color:#fff;font-weight:700;'>B: " + catetoB + "</div>");
            out.println("<div class='label' style='position:absolute;bottom:" + (aPx/2) + "px;left:" + (bPx/2 + 10) + "px;color:#fff;font-weight:700;'>C: " + resultado + "</div>");

            out.println("</div>");
            out.println("</div>");
        }

        out.println("</div>");
        out.println("</div>");
        out.println("</body></html>");
    }
}