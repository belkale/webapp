package sample.app.web;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sample.app.service.HelloService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * User: belkale
 * Date: 8/18/13
 */
@Singleton
public class HelloServlet extends HttpServlet {
    private static final Logger LOG = LoggerFactory.getLogger(HelloServlet.class);

    private final HelloService helloService;
    public static final String NAME = "name";

    @Inject
    public HelloServlet(HelloService helloService){
        this.helloService = helloService;
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter(NAME);
        LOG.debug("Query Parameter " + NAME + ":" + name);
        String result = helloService.sayHello(name);
        PrintWriter out = resp.getWriter();
        out.println(result);
    }
}
