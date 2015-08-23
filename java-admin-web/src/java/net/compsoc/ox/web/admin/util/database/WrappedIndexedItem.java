package net.compsoc.ox.web.admin.util.database;

import java.util.LinkedList;
import java.util.List;

import net.compsoc.ox.database.iface.core.IndexedItem;
import net.compsoc.ox.database.iface.core.KeyFactory;

public class WrappedIndexedItem<Key> {
    
    private final KeyFactory<Key> keyFactory;
    public final IndexedItem<Key> item;
    
    public WrappedIndexedItem(KeyFactory<Key> keyFactory, IndexedItem<Key> item) {
        this.keyFactory = keyFactory;
        this.item = item;
    }
    
    public String keyString() {
        return keyFactory.toString(item.key());
    }
    
    public static <Key> List<WrappedIndexedItem<Key>> wrappedIndexedItemList(
        KeyFactory<Key> keyFactory, Iterable<? extends IndexedItem<Key>> items) {
        List<WrappedIndexedItem<Key>> wrappedItems = new LinkedList<>();
        for (IndexedItem<Key> item : items)
            wrappedItems.add(new WrappedIndexedItem<Key>(keyFactory, item));
        return wrappedItems;
    }
}
