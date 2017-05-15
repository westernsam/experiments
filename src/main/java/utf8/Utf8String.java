package utf8;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public final class Utf8String {

    //TODO
    // - indexOf - char, string, utf8string
    // - codePointAt
    // - longer strings
    //     - char[] offsets
    //     - int[] offsets
    //     - long[] offsets
    // - concat
    // - substring
    // - compareTo
    // - equals
    // - hashcode
    // - toString
    // - codePoint IntStream
    // - charsAt(int i) char[]
    // - stringAt(int offset, int length) String

    private static final int _2_BYTE_CHR = 0xC0;
    private static final int _3_BYTE_CHR = 0xE0;
    private static final int _4_BYTE_CHR = 0xF0;

    private final byte[] bytes;
    private final byte[] first_64_offsets;

    public Utf8String(byte[] bytes) throws IOException {
        if (bytes.length > 64) throw new IllegalArgumentException("Only 64 byte strings are currently supported");
        this.bytes = bytes;

        ByteArrayOutputStream first64 = new ByteArrayOutputStream();
        for (int i = 0; i < Math.min(bytes.length, 63); i++) {
            byte b = bytes[i];

            if (isA(_4_BYTE_CHR, b)) {
                first64.write(shifted(i, 4));
            } else if (isA(_3_BYTE_CHR, b)) {
                first64.write(shifted(i, 3));
            } else if (isA(_2_BYTE_CHR, b)) {
                first64.write(shifted(i, 2));
            }
        }
        first_64_offsets = first64.toByteArray();
    }

    private byte[] shifted(int position, int numberOfUtf8Bytes) {
        int i1 = (numberOfUtf8Bytes << 5 | position);
        byte[] b1 = new byte[1];
        b1[0] = (byte) i1;
        return b1;
    }

    private boolean isA(int byteCharMask, byte b) {
        return (b & byteCharMask) == byteCharMask;
    }

    public int codePointAt(int index) {
        if ((index < 0) || (index >= bytes.length)) {
            throw new StringIndexOutOfBoundsException(index);
        }

//        for (int j = 0, count = 0; j < first_64_offsets.length && count < index; j++) {
//            byte first_64_offset = first_64_offsets[j];
//        }
        return 0;
    }

    public int length() {
        int numberOfMultiBytes = 0;
        for (byte b : first_64_offsets) {
            int upcastToInt = b & 0xFF;
            numberOfMultiBytes += (upcastToInt >> 5) - 1; //chunk away the position - not needed here
        }
        return bytes.length - numberOfMultiBytes;
    }

}
