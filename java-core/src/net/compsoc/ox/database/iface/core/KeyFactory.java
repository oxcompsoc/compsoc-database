package net.compsoc.ox.database.iface.core;

public interface KeyFactory<Key> {
    
    public Key fromString(String string) throws InvalidKeyException;
    public String toString(Key key);
    
    public static class IntegerKeyFactory implements KeyFactory<Integer> {
        
        public static final IntegerKeyFactory SINGLETON = new IntegerKeyFactory();

        public Integer fromString(String string) throws InvalidKeyException {
            try {
                return Integer.valueOf(string);
            } catch(NumberFormatException e){
                throw new InvalidKeyException();
            }
        }

        @Override
        public String toString(Integer key) {
            return key.toString();
        }
        
    }
    
    public static class StringKeyFactory implements KeyFactory<String> {
        
        public static final StringKeyFactory SINGLETON = new StringKeyFactory();

        public String fromString(String string) throws InvalidKeyException {
           return string;
        }

        @Override
        public String toString(String key) {
            return key;
        }
        
    }
}
