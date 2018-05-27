package com.trade.config;

import com.trade.service.ws.ips.ScanService;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JAXAutoConfig {

    @Bean(name = "hxScanService")
    public ScanService getJaxWsProxyFactoryBean(){
        String url = "https://thumbpay.e-years.com/psfp-webscan/services/scan";
        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        factory.setAddress(url);
        factory.setServiceClass(ScanService.class);
        return (ScanService) factory.create();
    }

}
