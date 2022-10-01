package com.finder.finder.helpers;

import com.finder.finder.model.Item;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface ItemsHandler {

    List<Item> getItems();
}
