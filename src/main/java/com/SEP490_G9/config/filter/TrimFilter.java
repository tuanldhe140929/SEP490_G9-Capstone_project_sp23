package com.SEP490_G9.config.filter;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;


public class TrimFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
    	TrimmedHttpServletRequest trimmedRequest = new TrimmedHttpServletRequest(request);
        filterChain.doFilter(trimmedRequest, response);
    }

    private static class TrimmedHttpServletRequest extends HttpServletRequestWrapper {

        TrimmedHttpServletRequest(HttpServletRequest request) {
            super(request);
        }

        @Override
        public String getParameter(String name) {
            String value = getRequest().getParameter(name);
            return value != null ? value.trim() : null;
        }

        @Override
        public String[] getParameterValues(String name) {
            String[] values = getRequest().getParameterValues(name);
            if (values == null) {
                return null;
            }
            for (int i = 0; i < values.length; i++) {
                values[i] = values[i].trim();
            }
            return values;
        }

        @Override
        public BufferedReader getReader() throws IOException {
            return new BufferedReader(new InputStreamReader(getRequest().getInputStream()));
        }

        public String getBody() throws IOException {
            String body = getReader().lines().collect(Collectors.joining());
            return body != null ? body.trim() : null;
        }
        
       
    }
}