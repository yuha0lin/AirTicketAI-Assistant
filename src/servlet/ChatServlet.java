package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import java.util.logging.Logger;
import java.util.logging.Level;

public class ChatServlet extends HttpServlet {
    private static final String API_KEY = "sk-0ad1eaf5194249439f82698893607bef"; // 请替换为您的实际 API 密钥
    private static final String APP_ID = "1d2b00751a914efd84ea4b354fffd962"; // 请替换为您的实际应用 ID
    private static final String API_URL = "https://dashscope.aliyuncs.com/api/v1/apps/" + APP_ID + "/completion";
    private static final Logger LOGGER = Logger.getLogger(ChatServlet.class.getName());

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        String prompt = request.getParameter("prompt");
        
        try {
            HttpClient client = HttpClient.newHttpClient();
            JSONObject requestBody = new JSONObject();
            JSONObject input = new JSONObject();
            input.put("prompt", prompt);
            requestBody.put("input", input);
            requestBody.put("parameters", new JSONObject());

            HttpRequest apiRequest = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + API_KEY)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
                .build();

            HttpResponse<String> apiResponse = client.send(apiRequest, HttpResponse.BodyHandlers.ofString());

            if (apiResponse.statusCode() == 200) {
                PrintWriter out = response.getWriter();
                out.print(apiResponse.body());
                out.flush();
            } else {
                LOGGER.log(Level.SEVERE, "API request failed with status code: " + apiResponse.statusCode());
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("{\"error\":\"API请求失败\"}");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in ChatServlet", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"服务器错误: " + e.getMessage() + "\"}");
        }
    }
}