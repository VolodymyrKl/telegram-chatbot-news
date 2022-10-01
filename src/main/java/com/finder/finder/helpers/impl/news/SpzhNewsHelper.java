package com.finder.finder.helpers.impl.news;

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
import java.util.ArrayList;
import java.util.List;

@Component
public class SpzhNewsHelper extends AbstractRequestSenderService implements ItemsHandler {

    private static final String URL_START = "https://spzh.news";
    public static final int NEWS_COUNT = 16;

    @Override
    public List<Item> getItems() {
        HttpResponse<String> standardHttpResponse = null;
        try {
            standardHttpResponse = super.getStandardHttpResponse("https://spzh.news/ua/news");
        } catch (IOException exception) {
            exception.printStackTrace();
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        }
        if (standardHttpResponse != null) {
            Document document = Jsoup.parse(standardHttpResponse.body());

            Elements elements = document.getElementsByClass("entry-title");

            List<Item> items = new ArrayList<>();
            for (int i = 0; i < NEWS_COUNT; i++) {
                Element element = elements.get(i);
                if (isNewPublications(element)) {
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
        item.setLink(URL_START + element.child(0).attr("href"));
    }

    private boolean isNewPublications(Element element) {
//        Elements pubdate = element.select("pubdate");
//        SimpleDateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z");
//        try {
//            Date parsedDate = formatter.parse(pubdate.get(0).text());
//            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//            String formattedDate = simpleDateFormat.format(parsedDate);
//
//            LocalDate dateOfPublication = LocalDate.parse(formattedDate);
//            LocalDate currentDate = LocalDate.now();
//
////            return dateOfPublication.isBefore(currentDate);
//            return true;
//        } catch (java.text.ParseException e) {
//            e.printStackTrace();
//        }
        return true;
    }
}
