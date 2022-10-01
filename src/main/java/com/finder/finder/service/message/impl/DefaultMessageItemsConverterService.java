package com.finder.finder.service.message.impl;

import com.finder.finder.model.Item;
import com.finder.finder.service.message.MessageItemsConverterService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class DefaultMessageItemsConverterService implements MessageItemsConverterService {

    @Override
    public String mapItems(List<Item> items) {
        return items.stream()
                .map(a -> a.getTitle() + "\n" + getDescription(a) + a.getLink() + "\n")
                .collect(Collectors.joining(System.lineSeparator()));
    }

    private String getDescription(Item item) {
        String description = item.getDescription();
        return description != null ? description + "\n" : "";
    }
}
