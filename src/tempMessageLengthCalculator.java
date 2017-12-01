public class tempMessageLengthCalculator {
    public static void main(String args[])
    {
        byte[] a = new byte[4];
        int b = 327690001;
        int c = 0;
        a = changeIntToMessageLengthBytes(b);
        c = changeMessageLengthBytesToInt(a);
        System.out.println(a + "\n" + b + "\n" + c);
    }

    public static byte[] changeIntToMessageLengthBytes(int length)
    {
        byte[] msgLength = new byte[4];
        int tempInt = length;
        int i = 0;
        int sizeByteCanHold = 256;
        while(tempInt%sizeByteCanHold != 0)
        {
            msgLength[i] = (byte) (tempInt%sizeByteCanHold);
            tempInt = tempInt/sizeByteCanHold;
            i++;
        }

        return msgLength;
    }

    public static int changeMessageLengthBytesToInt(byte[] msgLength)
    {
        int length = 0;
        int sizeByteCanHold = 256;
        for(int i=0; i<msgLength.length; i++)
        {
            length += (msgLength[i] & 0xff) * (int) Math.pow(sizeByteCanHold, i);
        }

        return length;
    }
}
