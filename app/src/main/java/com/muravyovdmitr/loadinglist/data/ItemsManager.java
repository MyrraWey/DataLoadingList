package com.muravyovdmitr.loadinglist.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Dima Muravyov on 23.06.2016.
 */
public class ItemsManager {
    public List<Item> mItems;

    public ItemsManager() {
        Random random = new Random();
        mItems = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            Item item = new Item();
            item.setTittle("Item #" + i);
            item.setLoad(false);
            item.setLoadingTime(1000 + random.nextInt(4000));

            mItems.add(item);
        }
    }

    public List<Item> getItems() {
        return mItems;
    }
}
