package com.finder.finder.service.impl;

import com.darkprograms.speech.translator.GoogleTranslate;
import com.finder.finder.helpers.ItemsHandler;
import com.finder.finder.model.Item;
import com.finder.finder.service.ItemsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DefaultItemService implements ItemsService {
    private List<ItemsHandler> itemsHandlerConfig;

    private final String[] TAGS = {"УПЦ", "Ukrainian", "Онуфрій", "Onufr", "UPC", "лавр", "lavr", "Ukraine", "Kyiv", "Kiew",
            "Епіфа", "Epiph", "ПЦУ", "PCU", "OCU", "church of Ukraine", "Павл", "Антон", "Синод", "Онуфрий",
            "Павел", "Києво-Печер", "Киево-Печер", "КДА", "братия"};

    @Override
    public List<Item> getItems() {
        List<Item> finalItems = new ArrayList<>();
        for (ItemsHandler itemsHandler : itemsHandlerConfig) {
            List<Item> items = itemsHandler.getItems();
            removeRelatedItemsByTags(items, finalItems);
        }
        List<Item> uniqueItems = removeDuplicates(finalItems);
        // set null to description because of limit telegram messages count.
        uniqueItems.forEach(a -> a.setDescription(null));
        return uniqueItems;
    }

    private List<Item> removeDuplicates(List<Item> finalItems) {
        return finalItems.stream()
                .distinct()
                .collect(Collectors.toList());
    }

    private void removeRelatedItemsByTags(List<Item> items, List<Item> finalItems) {
        for (Item item : items) {
            Arrays.stream(TAGS)
                    .filter(tag -> hasTag(item, tag))
                    .map(tag -> item)
                    .forEach(finalItems::add);
        }
    }

    private boolean hasTag(Item item, String tag) {
        return item.getTitle().contains(tag)
                || !StringUtils.isEmpty(item.getDescription()) && item.getDescription().contains(tag);
    }

    @Override
    public String translateItem(String source, String target, String text) {
        try {
            if (text.length() > 400) {
                return GoogleTranslate.translate(source, target, text.substring(0, 400));
            } else {
                return GoogleTranslate.translate(source, target, text);
            }

        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return "";
    }

    @Autowired
    public void setItemsHandlerConfig(List<ItemsHandler> itemsHandlerConfig) {
        this.itemsHandlerConfig = itemsHandlerConfig;
    }
}