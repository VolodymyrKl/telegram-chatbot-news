package com.finder.finder.helpers.impl.offchurch;

import com.finder.finder.helpers.AbstractRequestSenderService;
import com.finder.finder.helpers.ItemsHandler;
import com.finder.finder.model.Item;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Component
public class OCUChurchHelper extends AbstractRequestSenderService implements ItemsHandler {

    @Override
    public List<Item> getItems() {

        HttpResponse<String> standardHttpResponse = null;
        try {
            standardHttpResponse = super.getStandardHttpResponse("https://www.pomisna.info/uk/category/vsi-novyny/");
        } catch (
                IOException exception) {
            exception.printStackTrace();
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        }
        if (standardHttpResponse != null) {
            Document document = Jsoup.parse(standardHttpResponse.body());
            Elements elements = document.getElementsByTag("h3");
            List<Item> items = new ArrayList<>();
            for (int i = 0; i < elements.size(); i++) {
                Element element = elements.get(i);

                Elements dateElements = document.getElementsByClass("date-item");
                if (isNewPublications(dateElements.get(i))) {
                    Item item = new Item();

                    populateNewsItem(element, item);

                    items.add(item);
                }
            }
            return items;
        }
        return new ArrayList<>();
    }

    private void populateNewsItem(Element element, Item item) {
        item.setTitle(element.text());
        item.setLink(element.child(0).attr("href"));
    }

    private boolean isNewPublications(Element element) {
        String datetime = element.text();
        SimpleDateFormat formatter = new SimpleDateFormat("dd E yyyy");
//        try {
//            Date parsedDate = formatter.parse(datetime);
//            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//            String formattedDate = simpleDateFormat.format(parsedDate);
//
//            LocalDate dateOfPublication = LocalDate.parse(formattedDate);
//            LocalDate currentDate = LocalDate.now();

//            return dateOfPublication.isBefore(currentDate);
        return true;
//        } catch (java.text.ParseException e) {
//            e.printStackTrace();
//        }
//        return false;
    }
}