package com.nxh.web.controller;

import java.util.Collections;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.web.AbstractErrorController;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller("basicErrorController")
@RequestMapping("${server.error.path:${error.path:/error}}")
public class CommonErrorController extends AbstractErrorController {
	private final ErrorProperties errorProperties;
	private final Logger logger = LoggerFactory.getLogger(CommonErrorController.class);

	public CommonErrorController(ErrorAttributes errorAttributes, ErrorProperties errorProperties) {
		super(errorAttributes);
		this.errorProperties = errorProperties;
	}

	@RequestMapping(produces = "text/html")
	public ModelAndView errorHtml(HttpServletRequest request, HttpServletResponse response) {
		HttpStatus status = getStatus(request);
        Map<String, Object> model = Collections.unmodifiableMap(getErrorAttributes(
                request, isIncludeStackTrace(request, MediaType.TEXT_HTML)));
        response.setStatus(status.value());
        logger.error("请求出现异常->"+request.getRequestURI()+"【"+ status.value()+"】"+model);
        if(status.is5xxServerError()){
        	return new ModelAndView("error_5xx", model);
        }
        return new ModelAndView("error", model);
	}
	@RequestMapping
    @ResponseBody
    public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
        Map<String, Object> body = getErrorAttributes(request,
                isIncludeStackTrace(request, MediaType.ALL));
        HttpStatus status = getStatus(request);
        logger.error("请求出现异常->"+request.getRequestURI()+"【"+ status.value()+"】"+body);
        return new ResponseEntity<Map<String, Object>>(body, status);
    }
	protected boolean isIncludeStackTrace(HttpServletRequest request, MediaType produces) {
		ErrorProperties.IncludeStacktrace include = getErrorProperties().getIncludeStacktrace();
		if (include == ErrorProperties.IncludeStacktrace.ALWAYS) {
			return true;
		}
		if (include == ErrorProperties.IncludeStacktrace.ON_TRACE_PARAM) {
			return getTraceParameter(request);
		}
		return false;
	}

	@Override
	public String getErrorPath() {
		return this.errorProperties.getPath();
	}

	public ErrorProperties getErrorProperties() {
		return errorProperties;
	}
	
	

}
