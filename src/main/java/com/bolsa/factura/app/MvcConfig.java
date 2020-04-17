package com.bolsa.factura.app;

import java.util.Locale;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    // private final Logger log = LoggerFactory.getLogger(getClass());

    /*
     * @Override public void addResourceHandlers(ResourceHandlerRegistry registry) {
     * super.addResourceHandlers(registry);
     * 
     * String resourcePath =
     * Paths.get("uploads").toAbsolutePath().toUri().toString();
     * log.info("resourcePath: {}", resourcePath);
     * 
     * registry.addResourceHandler("/uploads/**")
     * .addResourceLocations(resourcePath); }
     */

    public void addViewControllers(ViewControllerRegistry registry) {
	registry.addViewController("/error_403").setViewName("error_403");

    }

    /**
     * Se configura el local resolver
     * @return
     */
    @Bean
    public LocaleResolver localeResolver() {
	SessionLocaleResolver localeResolver = new SessionLocaleResolver();

	localeResolver.setDefaultLocale(new Locale("es", "EC"));

	return localeResolver;

    }
    
    
    /**
     * Se configura el interceptor para cambiar el lenguage cada vez que cambie el parametro lang
     * @return
     */
    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
	LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
	localeChangeInterceptor.setParamName("lang");
	
	return localeChangeInterceptor;
	
    }

    /**
     * Se registra el interceptor
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
	registry.addInterceptor(localeChangeInterceptor());
    }
    

}
