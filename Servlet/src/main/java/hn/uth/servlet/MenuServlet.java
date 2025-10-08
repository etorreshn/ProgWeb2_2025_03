package hn.uth.serverlet;
import java.io.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet("/menu")
public class MenuServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String theme = request.getParameter("theme");
        boolean isDark = "dark".equals(theme);

        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Herramientas Matemáticas</title>");
        out.println("<style>");
        out.println(":root {");
        out.println("  --bg: " + (isDark ? "#0f172a" : "#f8fafc") + ";");
        out.println("  --text: " + (isDark ? "#f1f5f9" : "#1e293b") + ";");
        out.println("  --card-bg: " + (isDark ? "#1e293b" : "#ffffff") + ";");
        out.println("  --accent: " + (isDark ? "#3b82f6" : "#2563eb") + ";");
        out.println("  --accent-hover: " + (isDark ? "#60a5fa" : "#1d4ed8") + ";");
        out.println("  --shadow: " + (isDark ? "rgba(0,0,0,0.3)" : "rgba(0,0,0,0.1)") + ";");
        out.println("}");
        out.println("body { margin: 0; padding: 0; font-family: 'Segoe UI', system-ui, sans-serif; background: var(--bg); color: var(--text); display: flex; flex-direction: column; align-items: center; min-height: 100vh; position: relative; }");
        out.println(".theme-toggle { position: fixed; top: 20px; right: 20px; z-index: 1000; }");
        out.println(".toggle-btn { width: 60px; height: 30px; background: var(--card-bg); border: 2px solid var(--accent); border-radius: 25px; cursor: pointer; position: relative; transition: all 0.3s; display: flex; align-items: center; padding: 0 5px; }");
        out.println(".toggle-knob { width: 20px; height: 20px; background: var(--accent); border-radius: 50%; transition: all 0.3s; position: absolute; left: 5px; }");
        out.println(".dark .toggle-knob { transform: translateX(30px); }");
        out.println(".toggle-icon { font-size: 12px; margin: 0 2px; }");
        out.println(".container { text-align: center; padding: 100px 20px 60px; }");
        out.println("h1 { margin-bottom: 80px; font-size: 3em; font-weight: 300; background: linear-gradient(135deg, var(--accent), " + (isDark ? "#a78bfa" : "#7c3aed") + "); -webkit-background-clip: text; -webkit-text-fill-color: transparent; }");
        out.println(".circles-grid { display: flex; gap: 70px; justify-content: center; flex-wrap: wrap; }");
        out.println(".circle { width: 210px; height: 210px; border-radius: 50%; background: var(--card-bg); display: flex; flex-direction: column; align-items: center; justify-content: center; text-decoration: none; color: var(--text); transition: all 0.3s ease; box-shadow: 0 12px 35px var(--shadow); border: 3px solid transparent; position: relative; overflow: hidden; }");
        out.println(".circle::before { content: ''; position: absolute; top: 0; left: 0; right: 0; bottom: 0; background: linear-gradient(135deg, var(--accent), transparent); opacity: 0; transition: opacity 0.3s; }");
        out.println(".circle:hover { transform: translateY(-15px) scale(1.08); border-color: var(--accent); box-shadow: 0 25px 50px var(--shadow); }");
        out.println(".circle:hover::before { opacity: 0.1; }");
        out.println(".circle-icon { font-size: 3.8em; margin-bottom: 20px; z-index: 2; filter: drop-shadow(0 4px 8px rgba(0,0,0,0.3)); }");
        out.println(".circle-text { font-size: 1.1em; font-weight: 600; z-index: 2; text-align: center; padding: 0 15px; line-height: 1.3; }");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");

        out.println("<div class='theme-toggle'>");
        out.println("<a href='?theme=" + (isDark ? "light" : "dark") + "' class='toggle-btn " + (isDark ? "dark" : "") + "'>");
        out.println("<span class='toggle-icon'>☀️</span>");
        out.println("<div class='toggle-knob'></div>");
        out.println("<span class='toggle-icon'>🌙</span>");
        out.println("</a>");
        out.println("</div>");

        out.println("<div class='container'>");
        out.println("<h1>Herramientas Matemáticas</h1>");

        out.println("<div class='circles-grid'>");

        out.println("<a href='binario' class='circle'>");
        out.println("<div class='circle-icon'>🔣</div>");
        out.println("<div class='circle-text'>Conversor Binario</div>");
        out.println("</a>");

        out.println("<a href='numeros' class='circle'>");
        out.println("<div class='circle-icon'>📈</div>");
        out.println("<div class='circle-text'>Analizador Numérico</div>");
        out.println("</a>");

        out.println("<a href='hipotenusa' class='circle'>");
        out.println("<div class='circle-icon'>△</div>");
        out.println("<div class='circle-text'>Calculadora Geométrica</div>");
        out.println("</a>");

        out.println("</div>");
        out.println("</div>");
        out.println("</body>");
        out.println("</html>");
    }
}