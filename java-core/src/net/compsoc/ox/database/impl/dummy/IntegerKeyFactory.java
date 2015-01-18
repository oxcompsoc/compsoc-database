package net.compsoc.ox.database.impl.dummy;

import net.compsoc.ox.database.iface.core.KeyFactory;

public class IntegerKeyFactory implements KeyFactory<Integer> {
    
    public static final IntegerKeyFactory SINGLETON = new IntegerKeyFactory();
    
    @Override
    public Integer fromString(String keyString) throws InvalidKeyException {
        try {
            return Integer.parseInt(keyString);
        } catch (NumberFormatException e){
            throw new InvalidKeyException();
        }
    }
    
    @Override
    public String toString(Integer key) {
        return key.toString();
    }
    
}
