package net.compsoc.ox.database.iface.core;

public interface KeyFactory<Key> {
    
    public Key fromString(String string) throws InvalidKeyException;
    public String toString(Key key);
    
    public static class IntegerKeyFactory implements KeyFactory<Integer> {

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
}
