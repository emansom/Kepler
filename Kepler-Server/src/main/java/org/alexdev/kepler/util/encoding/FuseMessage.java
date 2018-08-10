package org.alexdev.kepler.util.encoding;

public class FuseMessage {

    public static String getArgument(int num, String data){
        String output = "";

        for(int i = 1; i <= num; i++){
            String tmpLen = data.substring(0,2);
            int len = Base64Encoding.decode(tmpLen.getBytes());
            output = data.substring(2, 2+len);
            data = data.substring(2+len);
        }

        return output;
    }

    public static int getArgumentEnd(int num, String data){
        int returnLen = 0;

        for(int i = 1; i <= num; i++){
            String tmpLen = data.substring(0,2);
            int len = Base64Encoding.decode(tmpLen.getBytes());
            data = data.substring(2+len);
            returnLen += 2+len;
        }

        return returnLen;
    }

    public static String getStructured(int num, String data){
        try{
            int itCount = 0;
            double maxIt = data.length() / 4;
            while (itCount <= maxIt){
                itCount++;
                String tmpStr = data.substring(0, 2);
                int id = Base64Encoding.decode(tmpStr.getBytes());

                tmpStr = data.substring(2, 2);
                int len = Base64Encoding.decode(tmpStr.getBytes());

                String output = data.substring(4, 4+len);
                data = data.substring(4 + len);

                if (num == id){
                    return output;
                }
            }
        }catch(Exception e){
            return "";
        }

        return "";
    }

}
