package soap.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jndi.JndiTemplate;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.server.EndpointInterceptor;
import org.springframework.ws.soap.server.endpoint.SoapFaultDefinition;
import org.springframework.ws.soap.server.endpoint.SoapFaultMappingExceptionResolver;
import org.springframework.ws.soap.server.endpoint.interceptor.PayloadValidatingInterceptor;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;
import soap.endpoint.PriceEndpoint;
import soap.exception.DetailSoapFaultDefinitionExceptionResolver;
import soap.exception.ServiceFaultException;
import soap.service.PriceService;

import javax.naming.NamingException;
import javax.sql.DataSource;
import java.util.List;
import java.util.Properties;

@Configuration
@EnableWs
@ComponentScan("soap")
public class AppConfig extends WsConfigurerAdapter {

	@Override
	public void addInterceptors(List<EndpointInterceptor> interceptors) {
		PayloadValidatingInterceptor validatingInterceptor = new PayloadValidatingInterceptor();
		validatingInterceptor.setValidateRequest(true);
		validatingInterceptor.setValidateResponse(true);
		validatingInterceptor.setXsdSchema(priceSchema());
		interceptors.add(validatingInterceptor);
	}

	@Bean
	public SoapFaultMappingExceptionResolver exceptionResolver(){
		SoapFaultMappingExceptionResolver exceptionResolver = new DetailSoapFaultDefinitionExceptionResolver();
		SoapFaultDefinition faultDefinition = new SoapFaultDefinition();
		faultDefinition.setFaultCode(SoapFaultDefinition.SERVER);
		exceptionResolver.setDefaultFault(faultDefinition);
		Properties errorMappings = new Properties();
		errorMappings.setProperty(Exception.class.getName(), SoapFaultDefinition.SERVER.toString());
		errorMappings.setProperty(ServiceFaultException.class.getName(), SoapFaultDefinition.SERVER.toString());
		exceptionResolver.setExceptionMappings(errorMappings);
		exceptionResolver.setOrder(1);
		return exceptionResolver;
	}

	@Bean(name = "Price")
	public DefaultWsdl11Definition defaultWsdl11Definition(XsdSchema priceSchema) {
		DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
		wsdl11Definition.setPortTypeName("PricePort");
		wsdl11Definition.setLocationUri("/soap");
		wsdl11Definition.setTargetNamespace(PriceEndpoint.NAMESPACE_URI);
		wsdl11Definition.setSchema(priceSchema);
		return wsdl11Definition;
	}
	@Bean
	public XsdSchema priceSchema() {
		return new SimpleXsdSchema(new ClassPathResource("Price.xsd"));
	}


	@Bean(name = "priceService")
	public PriceService userRolesJDBCTemplate(DataSource dataSource){
		PriceService priceService = new PriceService();
		priceService.setDataSource(dataSource);
		return priceService;
	}

	@Bean
	public DataSource dataSource(){
		DataSource dataSource = null;
		JndiTemplate jndiTemplate = new JndiTemplate();
		try {
			dataSource = jndiTemplate.lookup("jdbc/shoes",DataSource.class);
		}catch (NamingException e){}
		return dataSource;
	}

}
