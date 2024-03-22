package filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;

import java.io.IOException;

@WebFilter(urlPatterns = "/*")
public class ContentFilter implements Filter {
    private static final String ENCODING_UTF_8 = "UTF-8";
    private static final String CONTENT_TYPE_JSON = "application/json";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        servletRequest.setCharacterEncoding(ENCODING_UTF_8);

        servletResponse.setContentType(CONTENT_TYPE_JSON);
        servletResponse.setCharacterEncoding(ENCODING_UTF_8);

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
