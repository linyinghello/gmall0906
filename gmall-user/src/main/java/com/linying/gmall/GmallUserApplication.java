package com.linying.gmall;

import tk.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.linying.gmall.user.mapper")
public class GmallUserApplication {

	public static void main(String[] args) {
		SpringApplication.run(GmallUserApplication.class, args);
	}

}

