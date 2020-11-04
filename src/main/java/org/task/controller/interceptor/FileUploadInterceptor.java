package org.task.controller.interceptor;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * Interceptor to limit file upload size
 */
//NOTE: since I wanted to use octet instead of multipart in controller I've implemented this
public class FileUploadInterceptor extends HandlerInterceptorAdapter {

    private final String LIMIT_REACHED_ERROR_MESSAGE = "Maximum upload size is %s kilobytes.";

    private final int maxFileUploadSize;

    public FileUploadInterceptor(int maxFileUploadSize) {
        this.maxFileUploadSize = maxFileUploadSize;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getMethod().equals("POST") && request.getContentType().equals(MediaType.APPLICATION_OCTET_STREAM_VALUE)
            || request.getContentType().equals(MediaType.MULTIPART_FORM_DATA_VALUE)) {
            if (request.getContentLength() > maxFileUploadSize * 1024) {
                String errorMessage = String.format(LIMIT_REACHED_ERROR_MESSAGE, maxFileUploadSize);
                response.sendError(HttpServletResponse.SC_REQUEST_ENTITY_TOO_LARGE, errorMessage);
                return false;

            }
        }
        return super.preHandle(request, response, handler);
    }

}