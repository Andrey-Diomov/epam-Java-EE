package by.diomov.newsportal.filter;

import java.io.IOException;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

public class EncodingFilter implements Filter {
	private String code;

	public void init(FilterConfig fConfig) throws ServletException {
		code = fConfig.getInitParameter("encoding");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		String codeRequest = request.getCharacterEncoding();

		if (code != null && !code.equalsIgnoreCase(codeRequest)) {
			request.setCharacterEncoding(code);
			response.setCharacterEncoding(code);
		}
		chain.doFilter(request, response);
	}

	public void destroy() {
		code = null;
	}
}
