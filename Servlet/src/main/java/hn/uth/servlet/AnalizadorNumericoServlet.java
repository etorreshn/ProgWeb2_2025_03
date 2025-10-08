package hn.uth.serverlet;

import java.io.*;
import java.util.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet("/numeros")
public class AnalizadorNumericoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, NumberFormatException {
        res.setContentType("text/html; charset=UTF-8");
        PrintWriter out = res.getWriter();

        // JM - Variables
        String s1 = req.getParameter("numero1");
        String s2 = req.getParameter("numero2");
        String s3 = req.getParameter("numero3");
        String lista = req.getParameter("lista"); // opción 3: N números para la moda

        // JM - Valor más repetido (moda)
        if (!esVacio(lista)) {
            try {
                List<Integer> numeros = aListaEnteros(lista); // lanza IllegalArgumentException si hay tokens inválidos
                if (numeros.isEmpty()) {
                    mostrarFormulario(out, req, "Escribe al menos un número en «Valor más repetido».", true);
                    return;
                }
                ResultadoModa r = calcularModa(numeros);

                inicioDocumento(out, "Analizador numérico — Moda");
                encabezado(out);
                out.println("<div class='contenido'><div class='grid'>");

                // JM - Respuesta repetidos
                out.println("<div class='panel'>");
                out.println("<h3>Resultado: Valor más repetido</h3>");
                out.println("<table>");
                out.println("<tr><th>Cantidad (N)</th><th>Entrada</th><th>Moda(s)</th><th>Frecuencia</th></tr>");
                out.println("<tr>");
                out.println("<td>" + numeros.size() + "</td>");
                out.println("<td>" + unir(numeros) + "</td>");
                out.println("<td>" + unir(r.modas) + "</td>");
                out.println("<td>" + r.vecesMax + "</td>");
                out.println("</tr>");
                out.println("</table>");

                // Chips (SIN rango, como pediste)
                out.println("<div class='chips'>");
                out.println("<span class='chip'>Valores únicos: " + r.frecuencias.size() + "</span>");
                out.println("</div>");

                out.println("<div class='acciones'>");
                out.println("<a class='btn fantasma' href='" + req.getContextPath() + "/numeros'>Nuevo cálculo</a>");
                out.println("<a class='btn' href='" + req.getContextPath() + "/menu'>Volver al menú</a>");
                out.println("</div>");
                out.println("</div>");

                // (Eliminada la tabla de frecuencias detallada)
                out.println("</div></div>");
                finDocumento(out);
                return;

            } catch (IllegalArgumentException e) {
                mostrarFormulario(out, req, e.getMessage(), true);
                return;
            }
        }

        // JM - Mayor y menor de 3 enteros
        if (!esVacio(s1) && !esVacio(s2) && !esVacio(s3)) {
            try {
                int n1 = Integer.parseInt(s1.trim());
                int n2 = Integer.parseInt(s2.trim());
                int n3 = Integer.parseInt(s3.trim());

                int mayor = Math.max(n1, Math.max(n2, n3));
                int menor = Math.min(n1, Math.min(n2, n3));

                inicioDocumento(out, "Analizador numérico — Mayor y menor");
                encabezado(out);
                out.println("<div class='contenido'><div class='grid'>");

                out.println("<div class='panel'>");
                out.println("<h3>Resultado</h3>");
                out.println("<table>");
                out.println("<tr><th>Entrada</th><th>Mayor</th><th>Menor</th></tr>");
                out.println("<tr><td>" + n1 + " , " + n2 + " , " + n3 + "</td><td>" + mayor + "</td><td>" + menor + "</td></tr>");
                out.println("</table>");
                out.println("<div class='chips'>");
                out.println("<span class='chip'>Orden: " + vistaOrdenada(n1, n2, n3) + "</span>");
                out.println("<span class='chip'>Rango: " + (mayor - menor) + "</span>");
                out.println("</div>");
                out.println("<div class='acciones'>");
                out.println("<a class='btn fantasma' href='" + req.getContextPath() + "/numeros'>Nuevo cálculo</a>");
                out.println("<a class='btn' href='" + req.getContextPath() + "/menu'>Volver al menú</a>");
                out.println("</div>");
                out.println("</div>");

                out.println("<div class='panel'>");
                out.println("<h3>Descripción</h3>");
                out.println("<p>Se reciben tres números enteros y se obtiene el <b>mayor</b> y el <b>menor</b>.</p>");
                out.println("</div>");

                out.println("</div></div>");
                finDocumento(out);
                return;

            } catch (NumberFormatException e) {
                mostrarFormulario(out, req, "Ingresa solo números enteros válidos en «Mayor y menor».", false);
                return;
            }
        }

        // JM - Sin parámetros: mostrar formulario
        mostrarFormulario(out, req, null, false);
    }

    // =================== VISTAS ===================

    private void inicioDocumento(PrintWriter out, String titulo) {
        out.println("<!DOCTYPE html><html lang='es'><head><meta charset='UTF-8'/>"
                + "<meta name='viewport' content='width=device-width, initial-scale=1'/>"
                + "<title>"+titulo+"</title>"
                + "<style>"
                + "*,*::before,*::after{box-sizing:border-box}"
                + "body{margin:0;font-family:Inter,Segoe UI,Arial,sans-serif;min-height:100vh;"
                + "display:flex;align-items:center;justify-content:center;"
                + "transition:background .3s,color .3s}"
                + "body.modo-dia{background:#e5e7eb;color:#111827;}"
                + "body.modo-noche{background:#121212;color:#f5f5f5;}"
                + ".tarjeta{width:min(980px,95vw);border-radius:16px;"
                + "box-shadow:0 10px 25px rgba(0,0,0,.2);overflow:hidden;"
                + "transition:background .3s,color .3s}"
                + "body.modo-dia .tarjeta{background:#f8fafc;color:#1e293b;}"
                + "body.modo-noche .tarjeta{background:#1e1e1e;color:#f5f5f5;}"
                + ".cabecera{padding:24px 28px;display:flex;justify-content:space-between;align-items:center}"
                + "body.modo-dia .cabecera{background:#3b82f6;color:#fff;}"
                + "body.modo-noche .cabecera{background:#2a2a2a;color:#fff;}"
                + ".titulo{margin:0;font-weight:800;letter-spacing:.3px;font-size:clamp(20px,3.2vw,28px)}"
                + ".sub{margin:6px 0 0;font-weight:600;opacity:.9}"
                + ".contenido{padding:26px 32px}"
                + ".grid{display:grid;grid-template-columns:1.1fr .9fr;gap:22px}"
                + ".panel{border-radius:14px;padding:22px;transition:background .3s,border .3s}"
                + "body.modo-dia .panel{background:#ffffff;border:1px solid #d1d5db}"
                + "body.modo-noche .panel{background:#2a2a2a;border:1px solid #3a3a3a}"
                + ".panel h3{margin:0 0 12px;font-weight:700}"
                + "body.modo-dia .panel h3{color:#2563eb}"
                + "body.modo-noche .panel h3{color:#93c5fd}"
                + "table{width:100%;border-collapse:collapse;border-radius:8px;overflow:hidden;margin-top:8px}"
                + "th,td{padding:12px 14px;text-align:center}"
                + "body.modo-dia th{background:#f1f5f9;color:#111827}"
                + "body.modo-noche th{background:#333;color:#f9f9f9}"
                + "body.modo-dia td{border-bottom:1px solid #e5e7eb}"
                + "body.modo-noche td{border-bottom:1px solid #2e2e2e}"
                + ".chips{display:flex;gap:10px;flex-wrap:wrap;margin-top:12px}"
                + "body.modo-dia .chip{background:#f3f4f6;border:1px solid #d1d5db;color:#374151}"
                + "body.modo-noche .chip{background:#2f2f2f;border:1px solid #444;color:#d1d1d1}"
                + ".acciones{display:flex;gap:12px;margin-top:18px;flex-wrap:wrap}"
                + ".btn{appearance:none;border:0;border-radius:10px;padding:12px 18px;cursor:pointer;"
                + "transition:.2s;text-decoration:none;display:inline-block;font-weight:700}"
                + "body.modo-dia .btn{background:#2563eb;color:#fff}"
                + "body.modo-dia .btn:hover{background:#1d4ed8}"
                + "body.modo-noche .btn{background:#444;color:#fff}"
                + "body.modo-noche .btn:hover{background:#666}"
                + ".btn.fantasma{background:transparent;border:1px solid #9ca3af}"
                + "label{display:block;margin:12px 0 6px;font-weight:600}"
                + "body.modo-dia label{color:#374151}"
                + "body.modo-noche label{color:#d1d1d1}"
                + "input,textarea{width:100%;padding:12px 14px;border-radius:10px;outline:none;transition:.3s}"
                + "body.modo-dia input,body.modo-dia textarea{border:1px solid #9ca3af;background:#f9fafb;color:#111827}"
                + "body.modo-noche input,body.modo-noche textarea{border:1px solid #555;background:#111;color:#f5f5f5}"
                + "input:focus,textarea:focus{border-color:#2563eb;box-shadow:0 0 0 3px #2563eb44}"
                + ".error{padding:10px 12px;border-radius:10px;margin-bottom:10px;font-weight:600}"
                + "body.modo-dia .error{background:#fee2e2;border:1px solid #fca5a5;color:#991b1b}"
                + "body.modo-noche .error{background:#3f1d1d;border:1px solid #f87171;color:#fecaca}"
                + ".pista{font-size:12px;margin-top:6px}"
                + "body.modo-dia .pista{color:#6b7280}"
                + "body.modo-noche .pista{color:#9ca3af}"
                + ".link{display:inline-block;padding:10px 14px;border-radius:8px;text-decoration:none;font-weight:600}"
                + "body.modo-dia .link{border:1px solid #9ca3af;color:#1e3a8a;background:#f3f4f6}"
                + "body.modo-dia .link:hover{background:#e5e7eb}"
                + "body.modo-noche .link{border:1px solid #555;color:#f5f5f5;background:#2a2a2a}"
                + "body.modo-noche .link:hover{background:#3a3a3a}"

                // === Switch Día/Noche estilo toggle ===
                + ".modo-switch{position:absolute; top:16px; right:20px; z-index:1000}"
                + ".switch{position:relative; width:66px; height:34px; border-radius:999px;"
                + "  background:#f8fafc; border:2px solid #2563eb; display:flex; align-items:center;"
                + "  padding:0 8px; cursor:pointer; box-shadow:0 2px 6px rgba(0,0,0,.12)}"
                + "body.modo-noche .switch{background:#1f1f1f; border-color:#5f84ff}"
                + ".switch input{display:none}"
                + ".switch .icon{font-size:16px; line-height:1; pointer-events:none;}"
                + ".switch .icon.sun{color:#f59e0b; margin-left:auto}"
                + ".switch .icon.moon{color:#c7d2fe; margin-right:auto}"
                + ".switch .knob{position:absolute; top:5px; left:5px; width:22px; height:22px;"
                + "  background:#2563eb; border-radius:50%; transition:transform .22s ease;}"
                + "body.modo-noche .switch .knob{background:#5f84ff}"
                + ".switch input:checked + .track .knob{transform:translateX(32px)}"
                + ".switch .track{position:relative; width:100%; height:100%; display:flex; align-items:center;}"
                + "</style>"

                + "<script>"
                // Validaciones existentes
                + "function msjRequerido(e,msj){e.target.setCustomValidity('');if(!e.target.validity.valid){e.target.setCustomValidity(msj);} }"
                + "function limpiarMensaje(e){e.target.setCustomValidity('');}"
                + "function validarListaModa(){var t=document.getElementById('lista');if(!t){return true;}var raw=(t.value||'').trim();"
                + "if(raw===''){t.setCustomValidity('Escribe uno o más números enteros.');t.reportValidity();return false;}"
                + "var tokens=raw.split(/[,\\s]+/).filter(Boolean);var malos=[];"
                + "for(var i=0;i<tokens.length;i++){if(!/^[-]?\\d+$/.test(tokens[i])) malos.push(tokens[i]);}"
                + "if(malos.length>0){t.setCustomValidity('No válido: '+malos.join(', '));t.reportValidity();return false;}"
                + "t.setCustomValidity('');return true;}"

                // Switch Día/Noche con localStorage
                + "(function(){"
                + "  var chk;"
                + "  function aplicarModo(m){"
                + "    document.body.classList.remove('modo-dia','modo-noche');"
                + "    document.body.classList.add(m==='noche'?'modo-noche':'modo-dia');"
                + "    if(chk){ chk.checked=(m==='noche'); }"
                + "  }"
                + "  function alternar(){"
                + "    var m=chk && chk.checked ? 'noche' : 'dia';"
                + "    localStorage.setItem('modo', m);"
                + "    aplicarModo(m);"
                + "  }"
                + "  document.addEventListener('DOMContentLoaded',function(){"
                + "    chk=document.getElementById('chk-modo');"
                + "    var m=localStorage.getItem('modo')||'dia';"
                + "    aplicarModo(m);"
                + "    if(chk){ chk.addEventListener('change', alternar); }"
                + "  });"
                + "})();"
                + "</script>"
                + "</head><body><div class='tarjeta'>");
    }

    private void encabezado(PrintWriter out) {
        out.println("<div class='cabecera' style='position:relative;'>");
        out.println("<div>");
        out.println("<h1 class='titulo'>Analizador numérico <span style='font-size:14px;font-weight:700;opacity:.95'>&mdash; Mayor/Menor y Moda</span></h1>");
        out.println("<p class='sub'>Jeferson Mauricio Martinez Ramos — Cuenta: 202310010648</p>");
        out.println("<p class='sub'>Kensy Rosio Rodriguez — Cuenta: 202120120079</p>");
        out.println("</div>");

        // Switch Día/Noche
        out.println("<div class='modo-switch'>"
                + "  <label class='switch'>"
                + "    <input id='chk-modo' type='checkbox' aria-label='Cambiar modo'>"
                + "    <div class='track'>"
                + "      <span class='icon moon'>🌙</span>"
                + "      <span class='icon sun'>☀️</span>"
                + "      <div class='knob'></div>"
                + "    </div>"
                + "  </label>"
                + "</div>");

        out.println("</div>");
    }

    private void finDocumento(PrintWriter out) {
        out.println("</div></body></html>");
    }

    // =================== FORMULARIO ===================

    private void mostrarFormulario(PrintWriter out, HttpServletRequest req, String mensajeError, boolean enfocarModa) {
        inicioDocumento(out, "Analizador numérico — Formulario");
        encabezado(out);
        out.println("<div class='contenido'><div class='grid'>");

        // JM - Mayor/Menor
        out.println("<div class='panel'>");
        out.println("<h3>Mayor y menor de 3 enteros</h3>");
        if (mensajeError != null && !enfocarModa) out.println("<div class='error'>" + mensajeError + "</div>");
        out.println("<form method='get' action='" + req.getContextPath() + "/numeros'>");
        out.println("<label for='numero1'>Primer número</label>");
        out.println("<input id='numero1' name='numero1' type='number' required placeholder='Ej. 1' "
                + "oninvalid=\"msjRequerido(event,'Este campo es obligatorio.')\" oninput='limpiarMensaje(event)'>");
        out.println("<label for='numero2'>Segundo número</label>");
        out.println("<input id='numero2' name='numero2' type='number' required placeholder='Ej. 2' "
                + "oninvalid=\"msjRequerido(event,'Este campo es obligatorio.')\" oninput='limpiarMensaje(event)'>");
        out.println("<label for='numero3'>Tercer número</label>");
        out.println("<input id='numero3' name='numero3' type='number' required placeholder='Ej. 3' "
                + "oninvalid=\"msjRequerido(event,'Este campo es obligatorio.')\" oninput='limpiarMensaje(event)'>");
        out.println("<button class='btn' type='submit'>Calcular mayor y menor</button>");
        out.println("</form>");
        out.println("</div>");

        // JM - Moda
        out.println("<div class='panel'>");
        out.println("<h3>Valor más repetido (N enteros)</h3>");
        if (mensajeError != null && enfocarModa) out.println("<div class='error'>" + mensajeError + "</div>");
        out.println("<form method='get' action='" + req.getContextPath() + "/numeros' onsubmit='return validarListaModa()'>");
        out.println("<label for='lista'>Escribe N enteros separados por comas, espacios o saltos de línea</label>");
        out.println("<textarea id='lista' name='lista' rows='6' required "
                + "placeholder='Ej.: 1, 2, 3, 4, 5, 6, 7' "
                + "oninvalid=\"msjRequerido(event,'Escribe uno o más números enteros.')\" "
                + "oninput='limpiarMensaje(event)'></textarea>");
        out.println("<div class='pista'>Ejemplos válidos: <code>1, 2, 3, 4, 5, 6, 7</code> o <code>10 10 1 2 2 2 3</code></div>");
        out.println("<button class='btn' type='submit'>Calcular valor más repetido</button>");
        out.println("</form>");
        out.println("<div class='links'><a class='link' href='" + req.getContextPath() + "/menu'>Volver al menú</a></div>");
        out.println("</div>");

        out.println("</div></div>");
        finDocumento(out);
    }

    // =================== LÓGICA ===================

    private static boolean esVacio(String s) { return s == null || s.trim().isEmpty(); }

    private String vistaOrdenada(int a, int b, int c) {
        int[] v = {a,b,c};
        if (v[0] > v[1]) { int t=v[0]; v[0]=v[1]; v[1]=t; }
        if (v[1] > v[2]) { int t=v[1]; v[1]=v[2]; v[2]=t; }
        if (v[0] > v[1]) { int t=v[0]; v[0]=v[1]; v[1]=t; }
        return v[0] + " ≤ " + v[1] + " ≤ " + v[2];
    }

    private List<Integer> aListaEnteros(String texto) throws NumberFormatException {
        String[] partes = texto.trim().split("[,\\s]+"); // comas, espacios o saltos de línea
        List<Integer> lista = new ArrayList<>();
        List<String> invalidos = new ArrayList<>();

        for (String p : partes) {
            if (p.isEmpty()) continue;
            if (p.matches("-?\\d+")) { // permite negativos
                lista.add(Integer.parseInt(p));
            } else {
                invalidos.add(p);
            }
        }

        if (!invalidos.isEmpty()) {
            throw new IllegalArgumentException("Los siguientes valores no son enteros válidos: " + String.join(", ", invalidos));
        }
        return lista;
    }

    private ResultadoModa calcularModa(List<Integer> numeros) {
        LinkedHashMap<Integer,Integer> conteo = new LinkedHashMap<>();
        int max = 0;
        for (int n : numeros) {
            int c = conteo.getOrDefault(n, 0) + 1;
            conteo.put(n, c);
            if (c > max) max = c;
        }
        List<Integer> modas = new ArrayList<>();
        for (Map.Entry<Integer,Integer> e : conteo.entrySet()) {
            if (e.getValue() == max) modas.add(e.getKey());
        }
        return new ResultadoModa(max, modas, conteo);
    }

    private String unir(Collection<Integer> valores) {
        StringBuilder sb = new StringBuilder();
        Iterator<Integer> it = valores.iterator();
        while (it.hasNext()) {
            sb.append(it.next());
            if (it.hasNext()) sb.append(", ");
        }
        return sb.toString();
    }

    // DTO simple para moda
    private static class ResultadoModa {
        final int vecesMax;
        final List<Integer> modas;
        final LinkedHashMap<Integer,Integer> frecuencias;
        ResultadoModa(int vecesMax, List<Integer> modas, LinkedHashMap<Integer,Integer> frecuencias) {
            this.vecesMax = vecesMax;
            this.modas = modas;
            this.frecuencias = frecuencias;
        }
    }
}
