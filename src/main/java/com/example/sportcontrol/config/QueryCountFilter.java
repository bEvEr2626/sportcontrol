package com.example.sportcontrol.config;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import net.ttddyy.dsproxy.QueryCount;
import net.ttddyy.dsproxy.QueryCountHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class QueryCountFilter implements Filter {

    private static final Logger LOG = LoggerFactory.getLogger(QueryCountFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        QueryCountHolder.clear();
        chain.doFilter(request, response);
        QueryCount queryCount = QueryCountHolder.getGrandTotal();
        String uri = ((HttpServletRequest) request).getRequestURI();
        LOG.info("[Query Count] {} => Total: {}",
                uri,
                queryCount.getTotal());
        QueryCountHolder.clear();
    }
}
