package cn.edu.xmu.activity;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("cn.edu.xmu.activity.mapper")
@SpringBootApplication(scanBasePackages = {"cn.edu.xmu.ooad","cn.edu.xmu.activity"})
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
