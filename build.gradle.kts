plugins {
	java
	id("org.springframework.boot") version "4.1.0-SNAPSHOT"
	id("io.spring.dependency-management") version "1.1.7"
	id("org.hibernate.orm") version "7.4.0.Final"
	id("org.graalvm.buildtools.native") version "1.1.1"
	id("com.diffplug.spotless") version "8.6.0"
}

group = "com.ppmb"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
	maven { url = uri("https://repo.spring.io/snapshot") }
	maven { url = uri("https://repo.spring.io/milestone") }
}

extra["springModulithVersion"] = "2.1.0-RC1"

configurations {
	all {
		exclude(group = "org.springframework.boot", module = "spring-boot-starter-logging")
	}
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-log4j2")
	implementation("com.lmax:disruptor:4.0.0") // LMAX Disruptor for async Log4j2
	implementation("org.aspectj:aspectjweaver:1.9.22") // We use explicit version because spring-boot-starter-aop resolution is failing in this snapshot

	implementation("org.springframework.boot:spring-boot-h2console")
	implementation("org.springframework.boot:spring-boot-micrometer-tracing-brave")
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-amqp")
	implementation("org.springframework.boot:spring-boot-starter-data-elasticsearch")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-security-oauth2-client")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-webmvc")
	implementation("org.springframework.boot:spring-boot-starter-websocket")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:3.0.3")
	implementation("com.fasterxml.jackson.core:jackson-databind")
	implementation("org.springframework.boot:spring-boot-starter-data-redis")
	implementation("com.github.ben-manes.caffeine:caffeine")
	implementation("io.micrometer:micrometer-tracing-bridge-brave")
	implementation("org.springframework.amqp:spring-rabbit-stream")
	implementation("org.springframework.modulith:spring-modulith-events-api")
	implementation("org.springframework.modulith:spring-modulith-starter-core")
	implementation("org.springframework.modulith:spring-modulith-starter-jpa")
	implementation("org.springframework.security:spring-security-webauthn")
	implementation("net.ttddyy:datasource-proxy:1.11.0")
	implementation("com.auth0:java-jwt:4.4.0")
	implementation(platform("software.amazon.awssdk:bom:2.46.4"))
	implementation("software.amazon.awssdk:s3")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	developmentOnly("org.springframework.boot:spring-boot-docker-compose")
	runtimeOnly("com.h2database:h2")
	runtimeOnly("com.microsoft.sqlserver:mssql-jdbc")
	runtimeOnly("com.mysql:mysql-connector-j")
	runtimeOnly("com.oracle.database.jdbc:ojdbc11")
	runtimeOnly("io.micrometer:micrometer-registry-prometheus")
	runtimeOnly("org.postgresql:postgresql")
	runtimeOnly("org.springframework.modulith:spring-modulith-actuator")
	runtimeOnly("org.springframework.modulith:spring-modulith-events-amqp")
	runtimeOnly("org.springframework.modulith:spring-modulith-observability")
	annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
	testImplementation("org.springframework.boot:spring-boot-micrometer-tracing-test")
	testImplementation("org.springframework.boot:spring-boot-starter-actuator-test")
	testImplementation("org.springframework.boot:spring-boot-starter-data-elasticsearch-test")
	testImplementation("org.springframework.boot:spring-boot-starter-data-jpa-test")
	testImplementation("org.springframework.boot:spring-boot-starter-security-oauth2-client-test")
	testImplementation("org.springframework.boot:spring-boot-starter-security-test")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.boot:spring-boot-starter-validation-test")
	testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
	testImplementation("org.springframework.boot:spring-boot-starter-websocket-test")
	testImplementation("org.springframework.boot:spring-boot-testcontainers")
	testImplementation("org.springframework.modulith:spring-modulith-starter-test")
	testImplementation("org.testcontainers:testcontainers-elasticsearch")
	testImplementation("org.testcontainers:testcontainers-junit-jupiter")
	testImplementation("org.testcontainers:testcontainers-mssqlserver")
	testImplementation("org.testcontainers:testcontainers-mysql")
	testImplementation("org.testcontainers:testcontainers-oracle-free")
	testImplementation("org.testcontainers:testcontainers-postgresql")
	testImplementation("org.testcontainers:testcontainers-rabbitmq")
	testImplementation("com.redis:testcontainers-redis:2.2.4")
	testImplementation("org.testcontainers:testcontainers")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

spotless {
    java {
        // 自动切除不使用的 import、去除末尾空格、确保文件以换行符结尾
        removeUnusedImports()
        trimTrailingWhitespace()
        endWithNewline()

        // 强绑定 Google Java Style (使用 4 个空格缩进的 AOSP 变种)
        googleJavaFormat().aosp()
        
        // Kotlin DSL 传入多个路径的正确语法：使用 target() 函数
        target("src/main/java/**/*.java", "src/test/java/**/*.java")
    }
}

dependencyManagement {
	imports {
		mavenBom("org.springframework.modulith:spring-modulith-bom:${property("springModulithVersion")}")
	}
}

hibernate {
	enhancement {
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
