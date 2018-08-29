package org.iff.infra.util;

import org.junit.Assert;
import org.junit.Test;

import java.util.*;

public class PreRequiredHelperTest {

    @Test
    public void requireTrue() {
        Assert.assertTrue(PreRequiredHelper.requireTrue(true));
        try {
            PreRequiredHelper.requireTrue(false);
            Assert.fail();
        } catch (Exception e) {
        }
    }

    @Test
    public void requireFalse() {
        Assert.assertFalse(PreRequiredHelper.requireFalse(false));
        try {
            PreRequiredHelper.requireFalse(true);
            Assert.fail();
        } catch (Exception e) {
        }
    }

    @Test
    public void requireNull() {
        Assert.assertNull(PreRequiredHelper.requireNull(null));
        try {
            PreRequiredHelper.requireNull(new Object());
            Assert.fail();
        } catch (Exception e) {
        }
    }

    @Test
    public void requireNotNull() {
        Assert.assertNotNull(PreRequiredHelper.requireNotNull(new Object()));
        try {
            PreRequiredHelper.requireNotNull(null);
            Assert.fail();
        } catch (Exception e) {
        }
    }

    @Test
    public void requireEmpty() {
        Assert.assertNull(PreRequiredHelper.requireEmpty(null));
        Assert.assertEquals(PreRequiredHelper.requireEmpty(""), "");
        Assert.assertEquals(PreRequiredHelper.requireEmpty(new Object[0]).length, 0);
        Assert.assertEquals(PreRequiredHelper.requireEmpty(new ArrayList<>()).size(), 0);
        Assert.assertEquals(PreRequiredHelper.requireEmpty(new HashMap<>()).size(), 0);
        try {
            PreRequiredHelper.requireEmpty(new Object());
            Assert.fail();
        } catch (Exception e) {
        }
        try {
            PreRequiredHelper.requireEmpty(" ");
            Assert.fail();
        } catch (Exception e) {
        }
        try {
            PreRequiredHelper.requireEmpty(new Object[]{123});
            Assert.fail();
        } catch (Exception e) {
        }
        try {
            PreRequiredHelper.requireEmpty(Arrays.asList("123"));
            Assert.fail();
        } catch (Exception e) {
        }
        try {
            PreRequiredHelper.requireEmpty(MapHelper.toMap("key", 123));
            Assert.fail();
        } catch (Exception e) {
        }
    }

    @Test
    public void requireNotEmpty() {
        Assert.assertEquals(PreRequiredHelper.requireNotEmpty(" "), " ");
        Assert.assertNotNull(PreRequiredHelper.requireNotEmpty(new Object[]{123}));
        Assert.assertNotNull(PreRequiredHelper.requireNotEmpty(Arrays.asList("123")));
        Assert.assertNotNull(PreRequiredHelper.requireNotEmpty(MapHelper.toMap("key", 123)));
        try {
            PreRequiredHelper.requireNotEmpty(null);
            Assert.fail();
        } catch (Exception e) {
        }
        try {
            PreRequiredHelper.requireNotEmpty("");
            Assert.fail();
        } catch (Exception e) {
        }
        try {
            PreRequiredHelper.requireNotEmpty(new Object[0]);
            Assert.fail();
        } catch (Exception e) {
        }
        try {
            PreRequiredHelper.requireNotEmpty(new ArrayList<>());
            Assert.fail();
        } catch (Exception e) {
        }
        try {
            PreRequiredHelper.requireNotEmpty(new HashMap<>());
            Assert.fail();
        } catch (Exception e) {
        }
    }

    @Test
    public void requireBlank() {
        Assert.assertNull(PreRequiredHelper.requireBlank(null));
        Assert.assertEquals(PreRequiredHelper.requireBlank(""), "");
        Assert.assertEquals(PreRequiredHelper.requireBlank(" "), " ");
        try {
            PreRequiredHelper.requireBlank("a");
            Assert.fail();
        } catch (Exception e) {
        }
    }

    @Test
    public void requireNotBlank() {
        Assert.assertEquals(PreRequiredHelper.requireNotBlank("1"), "1");
        try {
            PreRequiredHelper.requireNotBlank(null);
            Assert.fail();
        } catch (Exception e) {
        }
        try {
            PreRequiredHelper.requireNotBlank("");
            Assert.fail();
        } catch (Exception e) {
        }
        try {
            PreRequiredHelper.requireNotBlank(" ");
            Assert.fail();
        } catch (Exception e) {
        }
    }

    @Test
    public void requireBeContained() {
        Assert.assertEquals(PreRequiredHelper.requireBeContained("test", "atesta"), "test");
        Assert.assertEquals(PreRequiredHelper.requireBeContained("test", new String[]{"a", "test"}), "test");
        Assert.assertEquals(PreRequiredHelper.requireBeContained("test", Arrays.asList("a", "test")), "test");
        Assert.assertEquals(PreRequiredHelper.requireBeContained("test", MapHelper.toMap("test", "a")), "test");
        try {
            PreRequiredHelper.requireBeContained(null, "");
            Assert.fail();
        } catch (Exception e) {
        }
        try {
            PreRequiredHelper.requireBeContained("test", "a");
            Assert.fail();
        } catch (Exception e) {
        }
        try {
            PreRequiredHelper.requireBeContained(null, new String[0]);
            Assert.fail();
        } catch (Exception e) {
        }
        try {
            PreRequiredHelper.requireBeContained("test", new String[]{"a"});
            Assert.fail();
        } catch (Exception e) {
        }
        try {
            PreRequiredHelper.requireBeContained(null, new ArrayList<>());
            Assert.fail();
        } catch (Exception e) {
        }
        try {
            PreRequiredHelper.requireBeContained("test", Arrays.asList("a"));
            Assert.fail();
        } catch (Exception e) {
        }
        try {
            PreRequiredHelper.requireBeContained(null, new HashMap<>());
            Assert.fail();
        } catch (Exception e) {
        }
        try {
            PreRequiredHelper.requireBeContained("test", MapHelper.toMap("a", "a"));
            Assert.fail();
        } catch (Exception e) {
        }
    }

    @Test
    public void requireContains() {
        Assert.assertEquals(PreRequiredHelper.requireContains("testa", "test"), "testa");
        Assert.assertEquals(PreRequiredHelper.requireContains(new String[]{"a", "test"}, "test").length, 2);
        Assert.assertEquals(PreRequiredHelper.requireContains(Arrays.asList("a", "test"), "test").size(), 2);
        Assert.assertEquals(PreRequiredHelper.requireContains(MapHelper.toMap("test", "a"), "test").size(), 1);
        try {
            PreRequiredHelper.requireContains("test", null);
            Assert.fail();
        } catch (Exception e) {
        }
        try {
            PreRequiredHelper.requireContains("test", "a");
            Assert.fail();
        } catch (Exception e) {
        }
        try {
            PreRequiredHelper.requireContains(new String[0], null);
            Assert.fail();
        } catch (Exception e) {
        }
        try {
            PreRequiredHelper.requireContains(new String[]{"test", "a"}, "b");
            Assert.fail();
        } catch (Exception e) {
        }
        try {
            PreRequiredHelper.requireContains(new ArrayList<>(), null);
            Assert.fail();
        } catch (Exception e) {
        }
        try {
            PreRequiredHelper.requireContains(Arrays.asList("a", "test"), "b");
            Assert.fail();
        } catch (Exception e) {
        }
        try {
            PreRequiredHelper.requireContains(new HashMap<>(), null);
            Assert.fail();
        } catch (Exception e) {
        }
        try {
            PreRequiredHelper.requireContains(MapHelper.toMap("test", "a"), "b");
            Assert.fail();
        } catch (Exception e) {
        }
    }

    @Test
    public void requireNotBeContained() {
        Assert.assertEquals(PreRequiredHelper.requireNotBeContained("test", "a"), "test");
        Assert.assertEquals(PreRequiredHelper.requireNotBeContained("test", new String[]{"a", "b"}), "test");
        Assert.assertEquals(PreRequiredHelper.requireNotBeContained("test", Arrays.asList("a", "b")), "test");
        Assert.assertEquals(PreRequiredHelper.requireNotBeContained("test", MapHelper.toMap("a", "a")), "test");
        try {
            PreRequiredHelper.requireNotBeContained(null, "");
            Assert.fail();
        } catch (Exception e) {
        }
        try {
            PreRequiredHelper.requireNotBeContained("test", "testa");
            Assert.fail();
        } catch (Exception e) {
        }
        try {
            PreRequiredHelper.requireNotBeContained(null, new String[0]);
            Assert.fail();
        } catch (Exception e) {
        }
        try {
            PreRequiredHelper.requireNotBeContained("test", new String[]{"test"});
            Assert.fail();
        } catch (Exception e) {
        }
        try {
            PreRequiredHelper.requireNotBeContained(null, new ArrayList<>());
            Assert.fail();
        } catch (Exception e) {
        }
        try {
            PreRequiredHelper.requireNotBeContained("test", Arrays.asList("test"));
            Assert.fail();
        } catch (Exception e) {
        }
        try {
            PreRequiredHelper.requireNotBeContained(null, new HashMap<>());
            Assert.fail();
        } catch (Exception e) {
        }
        try {
            PreRequiredHelper.requireNotBeContained("test", MapHelper.toMap("test", "a"));
            Assert.fail();
        } catch (Exception e) {
        }
    }

    @Test
    public void requireNotContain() {
        Assert.assertEquals(PreRequiredHelper.requireNotContain("test", "a"), "test");
        Assert.assertEquals(PreRequiredHelper.requireNotContain(new String[]{"test"}, "a").length, 1);
        Assert.assertEquals(PreRequiredHelper.requireNotContain(Arrays.asList("test"), "a").size(), 1);
        Assert.assertEquals(PreRequiredHelper.requireNotContain(MapHelper.toMap("test", "a"), "a").size(), 1);
        try {
            PreRequiredHelper.requireNotContain("test", null);
            Assert.fail();
        } catch (Exception e) {
        }
        try {
            PreRequiredHelper.requireNotContain("test", "test");
            Assert.fail();
        } catch (Exception e) {
        }
        try {
            PreRequiredHelper.requireNotContain(new String[0], null);
            Assert.fail();
        } catch (Exception e) {
        }
        try {
            PreRequiredHelper.requireNotContain(new String[]{"test"}, "test");
            Assert.fail();
        } catch (Exception e) {
        }
        try {
            PreRequiredHelper.requireNotContain(new ArrayList<>(), null);
            Assert.fail();
        } catch (Exception e) {
        }
        try {
            PreRequiredHelper.requireNotContain(Arrays.asList("test"), "test");
            Assert.fail();
        } catch (Exception e) {
        }
        try {
            PreRequiredHelper.requireNotContain(new HashMap<>(), null);
            Assert.fail();
        } catch (Exception e) {
        }
        try {
            PreRequiredHelper.requireNotContain(MapHelper.toMap("test", "a"), "test");
            Assert.fail();
        } catch (Exception e) {
        }
    }

    @Test
    public void requireInstanceOf() {
        Assert.assertEquals(PreRequiredHelper.requireInstanceOf(Long.valueOf(1), Number.class), Long.valueOf(1));
        try {
            PreRequiredHelper.requireInstanceOf(null, null);
            Assert.fail();
        } catch (Exception e) {
        }
        try {
            PreRequiredHelper.requireInstanceOf(Long.valueOf(1), null);
            Assert.fail();
        } catch (Exception e) {
        }
        try {
            PreRequiredHelper.requireInstanceOf(Long.valueOf(1), String.class);
            Assert.fail();
        } catch (Exception e) {
        }
    }

    @Test
    public void requireSubType() {
        Assert.assertEquals(PreRequiredHelper.requireSubType(Long.class, Number.class), Long.class);
        Assert.assertTrue(Number.class.isAssignableFrom(Long.class));
        try {
            PreRequiredHelper.requireSubType(null, null);
            Assert.fail();
        } catch (Exception e) {
        }
        try {
            PreRequiredHelper.requireSubType(null, Number.class);
            Assert.fail();
        } catch (Exception e) {
        }
        try {
            PreRequiredHelper.requireSubType(String.class, Number.class);
            Assert.fail();
        } catch (Exception e) {
        }
    }

    @Test
    public void requireEmail() {
        Assert.assertEquals(PreRequiredHelper.requireEmail("test@test.com"), "test@test.com");
        try {
            PreRequiredHelper.requireEmail(null);
            Assert.fail();
        } catch (Exception e) {
        }
        try {
            PreRequiredHelper.requireEmail("");
            Assert.fail();
        } catch (Exception e) {
        }
        try {
            PreRequiredHelper.requireEmail("test.com@123213");
            Assert.fail();
        } catch (Exception e) {
        }
    }

    @Test
    public void requireUrl() {
        Assert.assertEquals(PreRequiredHelper.requireUrl("http://test.com"), "http://test.com");
        Assert.assertEquals(PreRequiredHelper.requireUrl("https://test.com"), "https://test.com");
        try {
            PreRequiredHelper.requireUrl(null);
            Assert.fail();
        } catch (Exception e) {
        }
        try {
            PreRequiredHelper.requireUrl("");
            Assert.fail();
        } catch (Exception e) {
        }
        try {
            PreRequiredHelper.requireUrl("http://test");
            Assert.fail();
        } catch (Exception e) {
        }
        try {
            PreRequiredHelper.requireUrl("http:test");
            Assert.fail();
        } catch (Exception e) {
        }
    }

    @Test
    public void requireMinLength() {
        Assert.assertEquals(PreRequiredHelper.requireMinLength("test", 4), "test");
        Assert.assertEquals(PreRequiredHelper.requireMinLength(new String[2], 2).length, 2);
        Assert.assertEquals(PreRequiredHelper.requireMinLength(Arrays.asList("a", "b"), 2).size(), 2);
        Assert.assertEquals(PreRequiredHelper.requireMinLength(MapHelper.toMap("a", "a", "b", "b"), 2).size(), 2);
        try {
            PreRequiredHelper.requireMinLength("test", 5);
            Assert.fail();
        } catch (Exception e) {
        }
        try {
            PreRequiredHelper.requireMinLength(new String[2], 3);
            Assert.fail();
        } catch (Exception e) {
        }
        try {
            PreRequiredHelper.requireMinLength(Arrays.asList("a", "b"), 3);
            Assert.fail();
        } catch (Exception e) {
        }
        try {
            PreRequiredHelper.requireMinLength(MapHelper.toMap("a", "a", "b", "b"), 3);
            Assert.fail();
        } catch (Exception e) {
        }
    }

    @Test
    public void requireMaxLength() {
        Assert.assertEquals(PreRequiredHelper.requireMaxLength("test", 4), "test");
        Assert.assertEquals(PreRequiredHelper.requireMaxLength(new String[2], 2).length, 2);
        Assert.assertEquals(PreRequiredHelper.requireMaxLength(Arrays.asList("a", "b"), 2).size(), 2);
        Assert.assertEquals(PreRequiredHelper.requireMaxLength(MapHelper.toMap("a", "a", "b", "b"), 2).size(), 2);
        try {
            PreRequiredHelper.requireMaxLength("test", 3);
            Assert.fail();
        } catch (Exception e) {
        }
        try {
            PreRequiredHelper.requireMaxLength(new String[2], 1);
            Assert.fail();
        } catch (Exception e) {
        }
        try {
            PreRequiredHelper.requireMaxLength(Arrays.asList("a", "b"), 1);
            Assert.fail();
        } catch (Exception e) {
        }
        try {
            PreRequiredHelper.requireMaxLength(MapHelper.toMap("a", "a", "b", "b"), 1);
            Assert.fail();
        } catch (Exception e) {
        }
    }

    @Test
    public void requireRangeLength() {
        Assert.assertEquals(PreRequiredHelper.requireRangeLength("test", 3, 5), "test");
        Assert.assertEquals(PreRequiredHelper.requireRangeLength(new String[2], 1, 3).length, 2);
        Assert.assertEquals(PreRequiredHelper.requireRangeLength(Arrays.asList("a", "b"), 1, 3).size(), 2);
        Assert.assertEquals(PreRequiredHelper.requireRangeLength(MapHelper.toMap("a", "a", "b", "b"), 1, 3).size(), 2);
        try {
            PreRequiredHelper.requireRangeLength("test", 5, 6);
            Assert.fail();
        } catch (Exception e) {
        }
        try {
            PreRequiredHelper.requireRangeLength(new String[2], 3, 4);
            Assert.fail();
        } catch (Exception e) {
        }
        try {
            PreRequiredHelper.requireRangeLength(Arrays.asList("a", "b"), 3, 4);
            Assert.fail();
        } catch (Exception e) {
        }
        try {
            PreRequiredHelper.requireRangeLength(MapHelper.toMap("a", "a", "b", "b"), 3, 4);
            Assert.fail();
        } catch (Exception e) {
        }
    }

    @Test
    public void requireGreaterEqualThan() {
        PreRequiredHelper.requireGreaterEqualThan("3", 3);
        PreRequiredHelper.requireGreaterEqualThan("3", 2);
        PreRequiredHelper.requireGreaterEqualThan(3, 3);
        PreRequiredHelper.requireGreaterEqualThan(3, 2);
        try {
            PreRequiredHelper.requireGreaterEqualThan((String) null, 4);
            Assert.fail();
        } catch (Exception e) {
        }
        try {
            PreRequiredHelper.requireGreaterEqualThan("3", 4);
            Assert.fail();
        } catch (Exception e) {
        }
        try {
            PreRequiredHelper.requireGreaterEqualThan(3, 4);
            Assert.fail();
        } catch (Exception e) {
        }
    }

    @Test
    public void requireLessEqualThan() {
        PreRequiredHelper.requireLessEqualThan("3", 3);
        PreRequiredHelper.requireLessEqualThan("3", 4);
        PreRequiredHelper.requireLessEqualThan(3, 3);
        PreRequiredHelper.requireLessEqualThan(3, 4);
        try {
            PreRequiredHelper.requireLessEqualThan((String) null, 4);
            Assert.fail();
        } catch (Exception e) {
        }
        try {
            PreRequiredHelper.requireLessEqualThan("3", 2);
            Assert.fail();
        } catch (Exception e) {
        }
        try {
            PreRequiredHelper.requireLessEqualThan(3, 2);
            Assert.fail();
        } catch (Exception e) {
        }
    }

    @Test
    public void requireRange() {
        PreRequiredHelper.requireRange("3", 2, 5);
        PreRequiredHelper.requireRange("2", 2, 5);
        PreRequiredHelper.requireRange("5", 2, 5);
        PreRequiredHelper.requireRange(3, 2, 5);
        PreRequiredHelper.requireRange(2, 2, 5);
        PreRequiredHelper.requireRange(5, 2, 5);
        try {
            PreRequiredHelper.requireRange((String) null, 2, 5);
            Assert.fail();
        } catch (Exception e) {
        }
        try {
            PreRequiredHelper.requireRange("6", 2, 5);
            Assert.fail();
        } catch (Exception e) {
        }
        try {
            PreRequiredHelper.requireRange("1", 2, 5);
            Assert.fail();
        } catch (Exception e) {
        }
        try {
            PreRequiredHelper.requireRange(6, 2, 5);
            Assert.fail();
        } catch (Exception e) {
        }
        try {
            PreRequiredHelper.requireRange(1, 2, 5);
            Assert.fail();
        } catch (Exception e) {
        }
    }

    @Test
    public void requireDate() {
        //yyyy-MM-dd HH:mm:ss, yyyy-MM-dd, yyyy/MM/dd HH:mm:ss, yyyy/MM/dd, yyyy-MM-dd'T'HH:mm:ss.SSS, yyyy-MM-dd'T'HH:mm:ss.SSSXXX, dd MMM yyyy
        PreRequiredHelper.requireDate("2018-01-01 01:01:01");
        PreRequiredHelper.requireDate("2018-01-01");
        PreRequiredHelper.requireDate("2018/01/01 01:01:01");
        PreRequiredHelper.requireDate("2018/01/01");
        PreRequiredHelper.requireDate("2018-01-01T01:01:01.001+08:00");
        PreRequiredHelper.requireDate("01 Jan 2018");
        try {
            PreRequiredHelper.requireDate(null);
            Assert.fail();
        } catch (Exception e) {
        }
        try {
            PreRequiredHelper.requireDate("");
            Assert.fail();
        } catch (Exception e) {
        }
    }

    @Test
    public void requireIpv4() {
        PreRequiredHelper.requireIpv4("1.1.1.1");
        PreRequiredHelper.requireIpv4("255.255.255.255");
        try {
            PreRequiredHelper.requireIpv4(null);
            Assert.fail();
        } catch (Exception e) {
        }
        try {
            PreRequiredHelper.requireIpv4("");
            Assert.fail();
        } catch (Exception e) {
        }
        try {
            PreRequiredHelper.requireIpv4("255.255.255.256");
            Assert.fail();
        } catch (Exception e) {
        }
    }

    @Test
    public void requireIpv6() {
        PreRequiredHelper.requireIpv6("::1");
        PreRequiredHelper.requireIpv6("1::1");
        PreRequiredHelper.requireIpv6("1:1:1:1:1:1:1:1");
        PreRequiredHelper.requireIpv6("FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF");
        try {
            PreRequiredHelper.requireIpv6(null);
            Assert.fail();
        } catch (Exception e) {
        }
        try {
            PreRequiredHelper.requireIpv6("");
            Assert.fail();
        } catch (Exception e) {
        }
        try {
            PreRequiredHelper.requireIpv6("255.255.255.256");
            Assert.fail();
        } catch (Exception e) {
        }
    }

    @Test
    public void requireMatch() {
        PreRequiredHelper.requireMatch("test", "te.t");
        try {
            PreRequiredHelper.requireMatch("test", null);
            Assert.fail();
        } catch (Exception e) {
        }
        try {
            PreRequiredHelper.requireMatch("test", "testa");
            Assert.fail();
        } catch (Exception e) {
        }
    }

    @Test
    public void requireChinese() {
        PreRequiredHelper.requireChinese("我们是中国人。");
        try {
            PreRequiredHelper.requireChinese(null);
            Assert.fail();
        } catch (Exception e) {
        }
        try {
            PreRequiredHelper.requireChinese("test");
            Assert.fail();
        } catch (Exception e) {
        }
        try {
            PreRequiredHelper.requireChinese("我们是中国人test");
            Assert.fail();
        } catch (Exception e) {
        }
    }

    @Test
    public void removeNull() {
        Assert.assertNull(PreRequiredHelper.removeNull((String) null));
        Assert.assertNull(PreRequiredHelper.removeNull((Long[]) null));
        Assert.assertNull(PreRequiredHelper.removeNull((List) null));
        Assert.assertNull(PreRequiredHelper.removeNull((Map) null));
        Assert.assertEquals(PreRequiredHelper.removeNull(" test "), " test ");
        Assert.assertEquals(PreRequiredHelper.removeNull(new Object[]{1, null, 2}).length, 2);
        Assert.assertEquals(PreRequiredHelper.removeNull(Arrays.asList(1, null, 2)).size(), 2);
        Assert.assertEquals(PreRequiredHelper.removeNull(MapHelper.toMap("a", "a", "b", null)).size(), 1);
    }

    @Test
    public void removeBlank() {
        Assert.assertNull(PreRequiredHelper.removeBlank((String) null));
        Assert.assertNull(PreRequiredHelper.removeBlank((Long[]) null));
        Assert.assertNull(PreRequiredHelper.removeBlank((List) null));
        Assert.assertNull(PreRequiredHelper.removeBlank((Map) null));
        Assert.assertEquals(PreRequiredHelper.removeBlank(" test "), " test ");
        Assert.assertEquals(PreRequiredHelper.removeBlank(new Object[]{1, null, 2, " "}).length, 2);
        Assert.assertEquals(PreRequiredHelper.removeBlank(Arrays.asList(1, null, 2, " ")).size(), 2);
        Assert.assertEquals(PreRequiredHelper.removeBlank(MapHelper.toMap("a", "a", "b", null, "c", " ")).size(), 1);
    }

    @Test
    public void trimAndRemoveBlank() {
        Assert.assertNull(PreRequiredHelper.trimAndRemoveBlank((String) null));
        Assert.assertNull(PreRequiredHelper.trimAndRemoveBlank((Long[]) null));
        Assert.assertNull(PreRequiredHelper.trimAndRemoveBlank((List) null));
        Assert.assertNull(PreRequiredHelper.trimAndRemoveBlank((Map) null));
        Assert.assertEquals(PreRequiredHelper.trimAndRemoveBlank(" test "), "test");
        Assert.assertEquals(PreRequiredHelper.trimAndRemoveBlank(new Object[]{1, null, 2, " a ", " "}).length, 3);
        Assert.assertEquals(PreRequiredHelper.trimAndRemoveBlank(Arrays.asList(1, null, 2, " a ", " ")).size(), 3);
        Assert.assertEquals(PreRequiredHelper.trimAndRemoveBlank(MapHelper.toMap("a", "a", "b", null, "c", " ", "d", " a ")).size(), 2);
    }

    @Test
    public void nullToEmpty() {

        Assert.assertEquals(PreRequiredHelper.nullToEmpty((String) null), "");
        Assert.assertEquals(PreRequiredHelper.nullToEmpty((Collection<?>) null).size(), 0);
        Assert.assertEquals(PreRequiredHelper.nullToEmpty((Map<?, ?>) null).size(), 0);
        Assert.assertEquals(PreRequiredHelper.nullToEmpty((Object[]) null).length, 0);

        Assert.assertEquals(PreRequiredHelper.nullToEmpty((boolean[]) null).length, 0);
        Assert.assertEquals(PreRequiredHelper.nullToEmpty((short[]) null).length, 0);
        Assert.assertEquals(PreRequiredHelper.nullToEmpty((int[]) null).length, 0);
        Assert.assertEquals(PreRequiredHelper.nullToEmpty((long[]) null).length, 0);
        Assert.assertEquals(PreRequiredHelper.nullToEmpty((float[]) null).length, 0);
        Assert.assertEquals(PreRequiredHelper.nullToEmpty((double[]) null).length, 0);
        Assert.assertEquals(PreRequiredHelper.nullToEmpty((Boolean[]) null).length, 0);
        Assert.assertEquals(PreRequiredHelper.nullToEmpty((Short[]) null).length, 0);
        Assert.assertEquals(PreRequiredHelper.nullToEmpty((Integer[]) null).length, 0);
        Assert.assertEquals(PreRequiredHelper.nullToEmpty((Long[]) null).length, 0);
        Assert.assertEquals(PreRequiredHelper.nullToEmpty((Float[]) null).length, 0);
        Assert.assertEquals(PreRequiredHelper.nullToEmpty((Double[]) null).length, 0);
    }

    @Test
    public void isEmpty() {
        Assert.assertEquals(PreRequiredHelper.isEmpty(null), true);
        Assert.assertEquals(PreRequiredHelper.isEmpty(new ArrayList<>()), true);
        Assert.assertEquals(PreRequiredHelper.isEmpty(new HashMap<>()), true);
        Assert.assertEquals(PreRequiredHelper.isEmpty(new Object[0]), true);
    }

    @Test
    public void isNotEmpty() {
        Assert.assertEquals(PreRequiredHelper.isNotEmpty(null), false);
        Assert.assertEquals(PreRequiredHelper.isNotEmpty(new ArrayList<>()), false);
        Assert.assertEquals(PreRequiredHelper.isNotEmpty(new HashMap<>()), false);
        Assert.assertEquals(PreRequiredHelper.isNotEmpty(new Object[0]), false);
    }

    @Test
    public void equalsToNull() {
        Assert.assertEquals(PreRequiredHelper.equalsToNull("a", null), "a");
        Assert.assertNull(PreRequiredHelper.equalsToNull("a", "a"));
        Assert.assertNull(PreRequiredHelper.equalsToNull(1, 1));
    }
}