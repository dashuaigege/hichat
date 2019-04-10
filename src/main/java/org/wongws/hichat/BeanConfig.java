package org.wongws.hichat;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.wongws.hichat.util.SpringUtil;

@Configuration
@ComponentScan(basePackages = { "org.wongws.hichat.dao", "org.wongws.hichat.service" })
@EntityScan("org.wongws.hichat.entity")
@EnableJpaRepositories(basePackages = "org.wongws.hichat.repository", repositoryFactoryBeanClass = JpaRepositoryFactoryBean.class)
public class BeanConfig {
	
	@Bean
	public SpringUtil springUtil()
	{
		SpringUtil springUtil=new SpringUtil();
		return springUtil;
	}

}
