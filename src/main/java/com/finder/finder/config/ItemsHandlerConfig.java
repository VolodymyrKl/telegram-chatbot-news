package com.finder.finder.config;

import com.finder.finder.helpers.ItemsHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ItemsHandlerConfig {

    private List<ItemsHandler> itemsHandler;

    @Bean
    public List<ItemsHandler> itemsHandlers() {
        return itemsHandler;
    }

    @Autowired
    public void setItemsHandler(List<ItemsHandler> itemsHandler) {
        this.itemsHandler = itemsHandler;
    }
}
