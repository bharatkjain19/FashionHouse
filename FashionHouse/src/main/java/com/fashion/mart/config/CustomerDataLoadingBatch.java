package com.fashion.mart.config;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.bind.BindException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import com.fashion.mart.constants.Constants;
import com.fashion.mart.model.Customer;

@Configuration
@EnableBatchProcessing
public class CustomerDataLoadingBatch {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Value("classPath:/input/customers.csv")
    private Resource inputResource;

    /**
     * JobBuilderFactory(JobRepository jobRepository)  Convenient factory for a JobBuilder which sets the JobRepository automatically
     */
    @Bean
    public Job readCSVFileJob() {
        return jobBuilderFactory
                .get("processJob")
                .incrementer(new RunIdIncrementer())
                .start(step())
                .build();
    }
    
    /**
     * StepBuilder which sets the JobRepository and PlatformTransactionManager automatically
     */

    @Bean
    public Step step() {
        return stepBuilderFactory
                .get("step")
                .<Customer, Customer>chunk(10)
                .reader(reader())
                .writer(writer())
                .build();
    }
    
    /**
     * FlatFileItemReader<T> Restartable ItemReader that reads lines from input setResource(Resource).
     * @return
     */
    
    @Bean
    public FlatFileItemReader<Customer> reader() {
        FlatFileItemReader<Customer> itemReader = new FlatFileItemReader<Customer>();

        DefaultLineMapper<Customer> lineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();

        tokenizer.setNames(Constants.USER_NAME_HEADER, Constants.AGE_HEADER, Constants.HEIGHT_HEADER, Constants.GENDER_HEADER, Constants.SALES_AMOUNT_HEADER, Constants.LAST_PURCHASE_DATE_HEADER);
        tokenizer.setDelimiter(Constants.DEFAULT_DELIMITER);

        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(new FieldSetMapper<Customer>() {
            @Override
            public Customer mapFieldSet(final FieldSet fieldSet) throws BindException {
                Customer input = new Customer();
                input.setUserName(fieldSet.readString(Constants.USER_NAME_HEADER));
                input.setAge(fieldSet.readInt(Constants.AGE_HEADER));
                input.setHeight(fieldSet.readInt(Constants.HEIGHT_HEADER));
                input.setGender(fieldSet.readString(Constants.GENDER_HEADER));
                input.setSalesAmount(fieldSet.readLong(Constants.SALES_AMOUNT_HEADER));
                input.setLastPurchaseDate(fieldSet.readDate(Constants.LAST_PURCHASE_DATE_HEADER,"yyyy-MM-dd'T'HH:mm:ss"));

                return input;
            }
        });

      itemReader.setLineMapper(lineMapper);
      itemReader.setLinesToSkip(1);
      itemReader.setResource(inputResource);
      return itemReader;
    }
    

    /**
     * The data source object is defined and created depending upon the type of database we use.
     * In this example we are using H2 databse so we 'schema-h2.sql'  for example if we are using mysql database
     * we will be using 'schema-mysql.sql'
     *
     *
     */

    @Bean
    public DataSource dataSource() {

        EmbeddedDatabaseBuilder embeddedDatabaseBuilder = new EmbeddedDatabaseBuilder();

        return embeddedDatabaseBuilder.addScript("classpath:org/springframework/batch/core/schema-drop-h2.sql")
                .addScript("classpath:org/springframework/batch/core/schema-h2.sql")
                .addScript("classpath:customer.sql")
                .setType(EmbeddedDatabaseType.H2)
                .build();
    }

    /**
     * The itemWriter object will set JDBC connection and sql statement is prepared for the batch action we want to perform in the database.
     * A convenient implementation for providing BeanPropertySqlParameterSource when the item has JavaBean properties that correspond to names used for parameters in the SQL statement.
     *
     */

    @Bean
    public JdbcBatchItemWriter<Customer> writer() {

        JdbcBatchItemWriter<Customer> itemWriter = new JdbcBatchItemWriter<Customer>();

        itemWriter.setDataSource(dataSource());
        itemWriter.setSql("INSERT INTO Customer ( USER_NAME,AGE,HEIGHT,GENDER,SALES_AMOUNT,LAST_PURCHASE_DATE) VALUES ( :userName, :age, :height, :gender, :salesAmount, :lastPurchaseDate )");
        itemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Customer>());
        return itemWriter;
    }

}
