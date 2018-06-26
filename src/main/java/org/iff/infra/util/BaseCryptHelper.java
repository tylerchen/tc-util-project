/*******************************************************************************
 * Copyright (c) 2014-9-28 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;

import java.io.ByteArrayOutputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * base64, base62 encrypt dencrypt, URLDecoder.decode, URLDecoder.encode helper.
 *
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since 2014-9-28
 */
public class BaseCryptHelper {
    private static char[] encodes = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".toCharArray();
    //===============================Base64
    private static byte[] decodes = new byte[256];

    static {
        for (int i = 0; i < encodes.length; i++) {
            decodes[encodes[i]] = (byte) i;
        }
    }

    /**
     * encode bytes to base64 StringBuffer.
     *
     * @param data
     * @return
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     * @since Jul 19, 2016
     */
    public static StringBuffer encodeBase64(byte[] data) {
        return new StringBuffer().append(Base64.encodeBase64String(data));
    }

    /**
     * encode string to base64 string.
     *
     * @param toEncode
     * @return
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     * @since Jul 19, 2016
     */
    public static String encodeBase64(String toEncode) {
        return Base64.encodeBase64String(StringUtils.getBytesUtf8(toEncode));
    }

    /**
     * decode base64 chars to bytes.
     *
     * @param data
     * @return
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     * @since Jul 19, 2016
     */
    public static byte[] decodeBase64(char[] data) {
        return Base64.decodeBase64(new String(data).getBytes());
    }

    /**
     * decode base64 string to byte.
     *
     * @param data
     * @return
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     * @since Jul 19, 2016
     */
    public static byte[] decodeBase64(String data) {
        return Base64.decodeBase64(data);
    }

    /*===============================Base64 END*/


    /*===============================Base64 Url safe start*/

    /**
     * encode bytes to base64 StringBuffer.
     *
     * @param data
     * @return
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     * @since Jul 19, 2016
     */
    public static StringBuffer encodeBase64UrlSafe(byte[] data) {
        return new StringBuffer().append(new Base64(true).encodeToString(data));
    }

    /**
     * encode string to base64 string.
     *
     * @param toEncode
     * @return
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     * @since Jul 19, 2016
     */
    public static String encodeBase64UrlSafe(String toEncode) {
        return new Base64(true).encodeToString(StringUtils.getBytesUtf8(toEncode));
    }

    /**
     * decode base64 chars to bytes.
     *
     * @param data
     * @return
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     * @since Jul 19, 2016
     */
    public static byte[] decodeBase64UrlSafe(char[] data) {
        return new Base64(true).decode(new String(data));
    }

    /**
     * decode base64 string to byte.
     *
     * @param data
     * @return
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     * @since Jul 19, 2016
     */
    public static byte[] decodeBase64UrlSafe(String data) {
        return new Base64(true).decode(data);
    }
    /*===============================Base64 Url END*/

    /*===============================Base62*/

    /**
     * encode data to base62 StringBuffer.
     *
     * @param data
     * @return
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     * @since Jul 19, 2016
     */
    public static StringBuffer encodeBase62(byte[] data) {
        StringBuffer sb = new StringBuffer(data.length * 2);
        int pos = 0, val = 0;
        for (int i = 0; i < data.length; i++) {
            val = (val << 8) | (data[i] & 0xFF);
            pos += 8;
            while (pos > 5) {
                char c = encodes[val >> (pos -= 6)];
                sb.append(/**/c == 'i' ? "ia"
                        :
                        /**/c == '+' ? "ib"
                        :
                        /**/c == '/' ? "ic" : c);
                val &= ((1 << pos) - 1);
            }
        }
        if (pos > 0) {
            char c = encodes[val << (6 - pos)];
            sb.append(/**/c == 'i' ? "ia"
                    :
                    /**/c == '+' ? "ib"
                    :
                    /**/c == '/' ? "ic" : c);
        }
        return sb;
    }

    /**
     * encode string to base62 string.
     *
     * @param string
     * @return
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     * @since Jul 19, 2016
     */
    public static String encodeBase62(String string) {
        return encodeBase62(string.getBytes()).toString();
    }

    /**
     * decode base62 chars to bytes.
     *
     * @param data
     * @return
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     * @since Jul 19, 2016
     */
    public static byte[] decodeBase62(char[] data) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(data.length);
        int pos = 0, val = 0;
        for (int i = 0; i < data.length; i++) {
            char c = data[i];
            if (c == 'i') {
                c = data[++i];
                c =
                        /**/c == 'a' ? 'i'
                        :
                        /**/c == 'b' ? '+'
                        :
                        /**/c == 'c' ? '/' : data[--i];
            }
            val = (val << 6) | decodes[c];
            pos += 6;
            while (pos > 7) {
                baos.write(val >> (pos -= 8));
                val &= ((1 << pos) - 1);
            }
        }
        return baos.toByteArray();
    }

    /**
     * decode base62 string to bytes.
     *
     * @param string
     * @return
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     * @since Jul 19, 2016
     */
    public static byte[] decodeBase62(String string) {
        return decodeBase62(string.toCharArray());
    }

    //===============================Base62 END

    /**
     * encode url.
     *
     * @param url
     * @return
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     * @since Nov 21, 2016
     */
    public static String urlEncode(String url) {
        if (url != null && url.length() > 0) {
            try {
                return URLEncoder.encode(url, "UTF-8");
            } catch (Exception e) {
            }
        }
        return "";
    }

    /**
     * decode url.
     *
     * @param url
     * @return
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     * @since Nov 21, 2016
     */
    public static String urlDecode(String url) {
        if (url != null && url.length() > 0) {
            try {
                return URLDecoder.decode(url, "UTF-8");
            } catch (Exception e) {
            }
        }
        return "";
    }
}
