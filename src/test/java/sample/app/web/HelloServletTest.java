package sample.app.web;

import org.testng.Assert;
import org.testng.annotations.Test;
import sample.app.service.HelloService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.mockito.Mockito.*;

/**
 * User: belkale
 * Date: 8/18/13
 */
public class HelloServletTest {

    @Test
    public void test() throws IOException, ServletException {
        HelloService mockService = mock(HelloService.class);
        String name = "test";
        String message = "Hello test, Good Morning!";

        when(mockService.sayHello(name)).thenReturn(message);

        HttpServletRequest mockReq = mock(HttpServletRequest.class);
        when(mockReq.getParameter("name")).thenReturn(name);

        HttpServletResponse mockResp = mock(HttpServletResponse.class);
        StringWriter writer = new StringWriter();
        when(mockResp.getWriter()).thenReturn(new PrintWriter(writer, true));

        HelloServlet servlet = new HelloServlet(mockService);
        servlet.doGet(mockReq, mockResp);

        Assert.assertEquals(writer.toString(), message + "\n");
    }
}
