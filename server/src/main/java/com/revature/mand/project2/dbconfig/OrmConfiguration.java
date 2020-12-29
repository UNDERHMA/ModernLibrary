package com.revature.mand.project2.dbconfig;

import lombok.extern.log4j.Log4j2;
import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.io.InputStream;
import java.util.Properties;

/**
 * <h3>ORM Configuration</h3>
 * <p> Defines two beans that define the configuration required
 *     for a Hibernate SessionFactory and Transaction Manager</p>
 *
 * @author Mason Underhill
 */
@Log4j2
@Configuration
@EnableTransactionManagement
public class OrmConfiguration {

    /**
     * <h4>createEntityManager</h4>
     * <p>Bean which defines the properties of a Hibernate Session Factory which will
     *    be used by Spring ORM to manage the sessions and transactions of the database</p>
     *
     * @return LocalSessionFactoryBean The Spring Bean responsible for
     *                                 controlling the Hibernate Session Factory
     */
    @Bean
    public LocalSessionFactoryBean createEntityManager() {
        LocalSessionFactoryBean lSFB = new LocalSessionFactoryBean();
        // create and configure data source from environment variables
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        Properties connectionProperties = new Properties();
        try (InputStream propertiesFileStream = this.getClass().getClassLoader()
                .getResourceAsStream("db.properties")
        ) {
            connectionProperties.load(propertiesFileStream);
            dataSource.setUrl(connectionProperties.getProperty("dburl"));
            dataSource.setUser(connectionProperties.getProperty("dbuser"));
            dataSource.setPassword(connectionProperties.getProperty("dbpass"));
            dataSource.setDatabaseName(connectionProperties.getProperty("dbname"));
            dataSource.setPortNumbers(new int[] {Integer.valueOf(connectionProperties.getProperty("dbport"))});
            lSFB.setDataSource(dataSource);
        } catch (Exception e) {
            log.error("OrmConfiguration - createEntityManager - could not load properties", e);
        }
        // set hibernate properties
        Properties hibernateProperties = new Properties();
        hibernateProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQL9Dialect");
        hibernateProperties.setProperty("hibernate.show_sql", "true");
        lSFB.setHibernateProperties(hibernateProperties);
        lSFB.setPackagesToScan("com.revature.mand.project2");
        return lSFB;
    }

    /**
     * <h4>createTransactionManager</h4>
     * <p>Bean which creates a Hibernate Transaction Manager for the Session Factory.</p>
     *
     * @return HibernateTransactionManager Object which opens and closes transactions
     *                                     in a session as needed by Spring ORM
     */
    @Bean
    public HibernateTransactionManager createTransactionManager() {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory(createEntityManager().getObject());
        return transactionManager;
    }
}
