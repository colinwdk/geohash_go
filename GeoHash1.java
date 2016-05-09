import java.io.File;  
import java.io.FileInputStream;  
import java.util.BitSet;  
import java.util.HashMap;  
  
  
public class GeoHash1 {  
  
    private static int numbits = 6 * 5;  
    final static char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8',  
            '9', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j', 'k', 'm', 'n', 'p',  
            'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };  
      
    final static HashMap<Character, Integer> lookup = new HashMap<Character, Integer>();  
    static {  
        int i = 0;  
        for (char c : digits)  
            lookup.put(c, i++);  
    }  
  
    public static void main(String[] args)  throws Exception{  
  
    	GeoHash1 gh= new GeoHash1();
//        System.out.println(new GeoHash1().encode(39.92324, 116.3906));  
        System.out.println(gh.fromLongToString(gh.encodeHashToLong(39.92324, 116.3906, 7)));
    }  

//    public double[] decode(String geohash) {  
//        StringBuilder buffer = new StringBuilder();  
//        for (char c : geohash.toCharArray()) {  
//  
//            int i = lookup.get(c) + 32;  
//            buffer.append( Integer.toString(i, 2).substring(1) );  
//        }  
//          
//        BitSet lonset = new BitSet();  
//        BitSet latset = new BitSet();  
//          
//        //even bits  
//        int j =0;  
//        for (int i=0; i< numbits*2;i+=2) {  
//            boolean isSet = false;  
//            if ( i < buffer.length() )  
//              isSet = buffer.charAt(i) == '1';  
//            lonset.set(j++, isSet);  
//        }  
//          
//        //odd bits  
//        j=0;  
//        for (int i=1; i< numbits*2;i+=2) {  
//            boolean isSet = false;  
//            if ( i < buffer.length() )  
//              isSet = buffer.charAt(i) == '1';  
//            latset.set(j++, isSet);  
//        }  
//          
//        double lon = decode(lonset, -180, 180);  
//        double lat = decode(latset, -90, 90);  
//          
//        return new double[] {lat, lon};       
//    }  
//      
//    private double decode(BitSet bs, double floor, double ceiling) {  
//        double mid = 0;  
//        for (int i=0; i<bs.length(); i++) {  
//            mid = (floor + ceiling) / 2;  
//            if (bs.get(i))  
//                floor = mid;  
//            else  
//                ceiling = mid;  
//        }  
//        return mid;  
//    }  
//      
//      
//    public String encode(double lat, double lon) {  
//        BitSet latbits = getBits(lat, -90, 90);  
//        BitSet lonbits = getBits(lon, -180, 180);  
//        StringBuilder buffer = new StringBuilder();  
//        for (int i = 0; i < numbits; i++) {  
//            buffer.append( (lonbits.get(i))?'1':'0');  
//            buffer.append( (latbits.get(i))?'1':'0');  
//        }  
//        return base32(Long.parseLong(buffer.toString(), 2));  
//    }  
//  
//    private BitSet getBits(double lat, double floor, double ceiling) {  
//        BitSet buffer = new BitSet(numbits);  
//        for (int i = 0; i < numbits; i++) {  
//            double mid = (floor + ceiling) / 2;  
//            if (lat >= mid) {  
//                buffer.set(i);  
//                floor = mid;  
//            } else {  
//                ceiling = mid;  
//            }  
//        }  
//        System.out.println("floor="+floor+"ceiling="+ceiling);
//        return buffer;  
//    }  
//  
//    public static String base32(long i) {  
//        char[] buf = new char[65];  
//        int charPos = 64;  
//        boolean negative = (i < 0);  
//        if (!negative)  
//            i = -i;  
//        while (i <= -32) {  
//            buf[charPos--] = digits[(int) (-(i % 32))];  
//            i /= 32;  
//        }  
//        buf[charPos] = digits[(int) (-i)];  
//  
//        if (negative)  
//            buf[--charPos] = '-';  
//        return new String(buf, charPos, (65 - charPos));  
//    }  
//    
    
    long encodeHashToLong(double latitude, double longitude, int length) {
        boolean isEven = true;
        double minLat = -90.0, maxLat = 90;
        double minLon = -180.0, maxLon = 180.0;
        long bit = 0x8000000000000000L;
        long g = 0;

        long target = 0x8000000000000000L >>> (5 * length);
        while (bit != target) {
            if (isEven) {
                double mid = (minLon + maxLon) / 2;
                if (longitude >= mid) {
                    g |= bit;
                    minLon = mid;
                } else
                    maxLon = mid;
            } else {
                double mid = (minLat + maxLat) / 2;
                if (latitude >= mid) {
                    g |= bit;
                    minLat = mid;
                } else
                    maxLat = mid;
            }

            isEven = !isEven;
            bit >>>= 1;
        }
        System.out.println("aa\"aaminLon="+minLon+"maxLon="+maxLon);
        System.out.println("minLat="+minLat+"maxLat="+maxLat);
        return g |= length;
    }
    
    private static final String BASE32 = "0123456789bcdefghjkmnpqrstuvwxyz";
    String fromLongToString(long hash) {
        int length = (int) (hash & 0xf);
        if (length > 12 || length < 1)
            throw new IllegalArgumentException("invalid long geohash " + hash);
        char[] geohash = new char[length];
        for (int pos = 0; pos < length; pos++) {
            geohash[pos] = BASE32.charAt(((int) (hash >>> 59)));
            hash <<= 5;
        }
        return new String(geohash);
    }
  
}