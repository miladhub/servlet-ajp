import org.json.JSONObject;

import java.io.*;
import java.util.stream.Collectors;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet(urlPatterns = "/calc")
public class CalculatorServlet extends HttpServlet {
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        PrintWriter writer = response.getWriter();
        String html = "<html><h2>uid = " + request.getAttribute("uid") + "</h2></html>";
        writer.println(html);
    }
}
