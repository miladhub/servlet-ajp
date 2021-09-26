import org.json.JSONObject;

import java.io.*;
import java.util.stream.Collectors;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet(urlPatterns = "/calc")
public class CalculatorServlet extends HttpServlet {
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("application/json; charset=utf-8");

        String data = request.getReader().lines().collect(Collectors.joining());

        JSONObject json = new JSONObject(data);

        int first = json.getInt("first");
        int second = json.getInt("second");
        String op = json.getString("op");

        int res = 0;
        switch (op) {
            case "+": res = first + second; break;
            case "-": res = first - second; break;
            case "*": res = first * second; break;
            case "/": res = first / second; break;
        }

        JSONObject resJson = new JSONObject()
                .put("result", res)
                .put("uid", request.getAttribute("uid"));

        out.println(resJson.toString());
    }
}
