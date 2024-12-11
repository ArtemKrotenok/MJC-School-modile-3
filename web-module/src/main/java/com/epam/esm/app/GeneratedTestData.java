package com.epam.esm.app;

import com.epam.esm.service.RandomDataService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class GeneratedTestData implements ApplicationListener<ContextRefreshedEvent> {

    @Value("${generated-test-data.dictionaries-path}")
    private String dictionariesPath;
    @Value("${generated-test-data.fill-new-data}")
    private Boolean fillNewData;
    private RandomDataService randomDataService;

    public GeneratedTestData(RandomDataService randomDataService) {
        this.randomDataService = randomDataService;
    }

    @SneakyThrows
    @Override
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        if (fillNewData) {
            randomDataService.generateDataInDb(dictionariesPath);
        }
    }
}