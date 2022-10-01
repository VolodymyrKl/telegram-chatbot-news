package com.finder.finder.service.message;

import com.finder.finder.model.Item;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface MessageItemsConverterService {
    String mapItems(List<Item> items);
}
