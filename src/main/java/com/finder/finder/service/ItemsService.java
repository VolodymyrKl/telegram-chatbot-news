package com.finder.finder.service;

import com.finder.finder.model.Item;

import java.util.List;

public interface ItemsService {

    List<Item> getItems();

    String translateItem(String el, String uk, String title);
}
