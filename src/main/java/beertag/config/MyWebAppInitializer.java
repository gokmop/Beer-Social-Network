//package beertag.config;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;

//public class MyWebAppInitializer implements WebApplicationInitializer {
//
//    @Override
//    public void onStartup(ServletContext container) {
//        AnnotationConfigWebApplicationContext context
//                = new AnnotationConfigWebApplicationContext();
//        context.setConfigLocation("beertag");
//
//        container.addListener(new ContextLoaderListener(context));
//
//        DispatcherServlet servlet = new DispatcherServlet(context);
//        servlet.setThrowExceptionIfNoHandlerFound(true);
//        ServletRegistration.Dynamic dispatcher = container
//                .addServlet("dispatcher", servlet);
//
//        dispatcher.setLoadOnStartup(1);
//        dispatcher.addMapping("/");
//    }
//}