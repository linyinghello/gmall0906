package com.linying.gmall.mamage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan("com.linying.gmall.mamage.mapper")
public class GmallMamageApplication {

	public static void main(String[] args) {
		SpringApplication.run(GmallMamageApplication.class, args);
	}

}

