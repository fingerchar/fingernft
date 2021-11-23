package com.fingerchar;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages = {
        "com.fingerchar"
})
@MapperScan({"com.fingerchar.dao.**"})
@EnableTransactionManagement
public class BaseAdminApplication {

	public static void main(String[] args) {
		SpringApplication.run(BaseAdminApplication.class, args);
	}
}
