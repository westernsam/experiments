# Experiments

## json-serialization

A simple scala Map[String, [Map|Iterable|Primative]] -> json serializer using a state machine to avoid recursion. Also streams json to output stream to avoid unnecessary string allocation.

## Utf8 String

A Utf8String implementation that stores byte[] rather than char[] - hence can store (most) string in half the space. Length returns number of code points rather than size of array (as is the case with java.lan.String).
Stores offsets of multi-byte characters in smallest type possible

- first 64 bytes offsets are stored in byte array with 2 bits for length and 6 bits (64) for position
- next 16384 bytes in char array with 2 bits for length and 14 bits (16384) for position
- next 1073741824 bytes in int array with 2 bits for length and 30 bits (1073741824) for position 
 

