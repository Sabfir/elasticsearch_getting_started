package ua.ukrpost.config;

import org.flywaydb.core.Flyway;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@ComponentScan("ua.ukrpost")
@PropertySource(value = {
        "classpath:application.properties",
        "classpath:application-dev.properties"})
public class HibernateConfig {
    private static final String PACKAGE_TO_SCAN = "ua.ukrpost.entity";
    private Environment environment;
    @Value("classpath:db/migration/V2.0__populate_address.sql")
    private Resource countryPopulatorMemory;

    @Autowired
    public HibernateConfig(Environment environment) {
        this.environment = environment;
    }

    @Bean(name = "flyway", initMethod = "migrate")
    @Profile({"stage", "dev"})
    public Flyway flyway() {
        Flyway flyway = new Flyway();
        flyway.setBaselineOnMigrate(true);
        flyway.setLocations("db/migration");
        flyway.setDataSource(dataSource());
        // if the problem with checksum or failed migration run app with key -Dflyway.repair=true
        String repair = environment.getProperty("flyway.repair");
        if (repair != null && repair.equals("true")) {
            flyway.repair();
        }
        return flyway;
    }

    @Bean(name = "sessionFactory") @DependsOn("flyway")
    @Profile("stage")
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setPackagesToScan(PACKAGE_TO_SCAN);
        sessionFactory.setHibernateProperties(hibernatePropertiesProduction());
        return sessionFactory;
    }

    @Bean(name = "sessionFactory") @DependsOn("flyway")
    @Profile("dev")
    public LocalSessionFactoryBean sessionFactoryDevelopment() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSourceDevelopment());
        sessionFactory.setPackagesToScan(PACKAGE_TO_SCAN);
        sessionFactory.setHibernateProperties(hibernatePropertiesDevelopment());
        return sessionFactory;
    }

    @Bean(name = "sessionFactory")
    @Profile("memory")
    public LocalSessionFactoryBean sessionFactoryInMemory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSourceInMemory());
        sessionFactory.setPackagesToScan(PACKAGE_TO_SCAN);
        sessionFactory.setHibernateProperties(hibernatePropertiesInMemory());
        return sessionFactory;
    }

    @Bean(name = "dataSource")
    @Profile("stage")
    public DataSource dataSource() {
        System.out.println("-----------------------------------------");
        System.out.println("----------ACTIVE SPRING PROFILE----------");
        System.out.println("-------------------STAGE------------------");
        System.out.println("-----------------------------------------");
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(environment.getRequiredProperty("stage.jdbc.driverClassName"));
        dataSource.setUrl(environment.getRequiredProperty("stage.jdbc.url"));
        dataSource.setUsername(environment.getRequiredProperty("stage.jdbc.username"));
        dataSource.setPassword(environment.getRequiredProperty("stage.jdbc.password"));
        return dataSource;
    }

    @Bean(name = "dataSource")
    @Profile("dev")
    public DataSource dataSourceDevelopment() {
        System.out.println("-----------------------------------------");
        System.out.println("----------ACTIVE SPRING PROFILE----------");
        System.out.println("-------------------DEV-------------------");
        System.out.println("-----------------------------------------");
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(environment.getRequiredProperty("dev.jdbc.driverClassName"));
        dataSource.setUrl(environment.getRequiredProperty("dev.jdbc.url"));
        dataSource.setUsername(environment.getRequiredProperty("dev.jdbc.username"));
        dataSource.setPassword(environment.getRequiredProperty("dev.jdbc.password"));
        return dataSource;
    }

    @Bean(name = "dataSource")
    @Profile("memory")
    public DataSource dataSourceInMemory() {
        System.out.println("-----------------------------------------");
        System.out.println("----------ACTIVE SPRING PROFILE----------");
        System.out.println("------------------MEMORY-----------------");
        System.out.println("-----------------------------------------");
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(environment.getRequiredProperty("memory.jdbc.driverClassName"));
        dataSource.setUrl(environment.getRequiredProperty("memory.jdbc.url"));
        dataSource.setUsername(environment.getRequiredProperty("memory.jdbc.username"));
        dataSource.setPassword(environment.getRequiredProperty("memory.jdbc.password"));
        return dataSource;
    }

    private Properties hibernatePropertiesProduction() {
        Properties properties = new Properties();
        properties.put("hibernate.dialect", environment.getRequiredProperty("stage.hibernate.dialect"));
        properties.put("hibernate.format_sql", environment.getRequiredProperty("stage.hibernate.format_sql"));
        properties.put("hibernate.show_sql", environment.getRequiredProperty("stage.hibernate.show_sql"));
        properties.put("hibernate.hbm2ddl.auto", environment.getRequiredProperty("stage.hibernate.hbm2ddl.auto"));
        return properties;
    }

    private Properties hibernatePropertiesDevelopment() {
        Properties properties = new Properties();
        properties.put("hibernate.dialect", environment.getRequiredProperty("dev.hibernate.dialect"));
        properties.put("hibernate.format_sql", environment.getRequiredProperty("dev.hibernate.format_sql"));
        properties.put("hibernate.show_sql", environment.getRequiredProperty("dev.hibernate.show_sql"));
        properties.put("hibernate.hbm2ddl.auto", environment.getRequiredProperty("dev.hibernate.hbm2ddl.auto"));
        return properties;
    }

    private Properties hibernatePropertiesInMemory() {
        Properties properties = new Properties();
        properties.put("hibernate.dialect", environment.getRequiredProperty("memory.hibernate.dialect"));
        properties.put("hibernate.format_sql", environment.getRequiredProperty("memory.hibernate.format_sql"));
        properties.put("hibernate.show_sql", environment.getRequiredProperty("memory.hibernate.show_sql"));
        properties.put("hibernate.hbm2ddl.auto", environment.getRequiredProperty("memory.hibernate.hbm2ddl.auto"));
        return properties;
    }

    @Bean
    @Autowired
    public HibernateTransactionManager transactionManager(SessionFactory s) {
        HibernateTransactionManager txManager = new HibernateTransactionManager();
        txManager.setSessionFactory(s);
        return txManager;
    }

    // for in memory db, cuz trevis can't work with oracle, and we need CI
    @Bean
    @Autowired
    @Profile("memory")
    public DataSourceInitializer dataSourceInitializer(
            final DataSource dataSource, DatabasePopulator databasePopulator) {
        final DataSourceInitializer initializer = new DataSourceInitializer();
        initializer.setDataSource(dataSource);
        initializer.setDatabasePopulator(databasePopulator);
        return initializer;
    }

    @Bean(name = "databasePopulator")
    @Profile("memory")
    public DatabasePopulator databasePopulatorProduction() {
        System.out.println("DATABASE POPULATOR: memory");
        final ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(countryPopulatorMemory);
        return populator;
    }
}
