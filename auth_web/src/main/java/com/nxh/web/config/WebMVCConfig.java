package com.nxh.web.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.WebMvcProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ValueFilter;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.nxh.web.controller.CommonErrorController;

@Configuration
@PropertySource("spring-mvc.properties")
public class WebMVCConfig extends WebMvcConfigurerAdapter {
	private static final Log logger = LogFactory.getLog(WebMVCConfig.class);
	@Autowired
	private WebMvcProperties mvcProperties = new WebMvcProperties();
	
	@Autowired
	private ServerProperties properties;

	public void addViewControllers(ViewControllerRegistry registry) {
		logger.info("Adding welcome page: " + mvcProperties.getView().getPrefix() + "index"
				+ mvcProperties.getView().getSuffix());
		registry.addViewController("/").setViewName("index");
		registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
	}

	@Bean
	@ConditionalOnMissingBean({ FastJsonHttpMessageConverter.class })
	public FastJsonHttpMessageConverter fastJsonHttpMessageConverter() {
		FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
		FastJsonConfig fastJsonConfig = new FastJsonConfig();
		fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat, SerializerFeature.WriteClassName,
				SerializerFeature.WriteMapNullValue);
		ValueFilter valueFilter = new ValueFilter() {
			public Object process(Object oClass, String key, Object value) {
				if (null == value) {
					value = "";
				}
				return value;
			}
		};
		fastJsonConfig.setSerializeFilters(valueFilter);
		converter.setFastJsonConfig(fastJsonConfig);
		return converter;
	}

	@Bean
	public CommonErrorController basicErrorController(ErrorAttributes errorAttributes) {
		return new CommonErrorController(errorAttributes, this.properties.getError());
	}
}
