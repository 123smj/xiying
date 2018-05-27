///*
// * Decompiled with CFR 0_124.
// */
//package com.trade.util.ninepi;
//
//import java.io.IOException;
//import java.io.ObjectInputStream;
//import java.text.DateFormat;
//import java.text.DateFormatSymbols;
//import java.text.FieldPosition;
//import java.text.Format;
//import java.text.ParsePosition;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.GregorianCalendar;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Locale;
//import java.util.Map;
//import java.util.TimeZone;
//
//public class SimpleFastDateFormat
//extends Format {
//    private static final long serialVersionUID = 1L;
//    private static String cDefaultPattern;
//    private static final Map cInstanceCache;
//    private static final Map cDateInstanceCache;
//    private static final Map cTimeInstanceCache;
//    private static final Map cDateTimeInstanceCache;
//    private static final Map cTimeZoneDisplayCache;
//    private final String mPattern;
//    private final TimeZone mTimeZone;
//    private final boolean mTimeZoneForced;
//    private final Locale mLocale;
//    private final boolean mLocaleForced;
//    private transient Rule[] mRules;
//    private transient int mMaxLengthEstimate;
//
//    static {
//        cInstanceCache = new HashMap(7);
//        cDateInstanceCache = new HashMap(7);
//        cTimeInstanceCache = new HashMap(7);
//        cDateTimeInstanceCache = new HashMap(7);
//        cTimeZoneDisplayCache = new HashMap(7);
//    }
//
//    public static SimpleFastDateFormat getInstance() {
//        return SimpleFastDateFormat.getInstance(SimpleFastDateFormat.getDefaultPattern(), null, null);
//    }
//
//    public static SimpleFastDateFormat getInstance(String pattern) {
//        return SimpleFastDateFormat.getInstance(pattern, null, null);
//    }
//
//    public static SimpleFastDateFormat getInstance(String pattern, TimeZone timeZone) {
//        return SimpleFastDateFormat.getInstance(pattern, timeZone, null);
//    }
//
//    public static SimpleFastDateFormat getInstance(String pattern, Locale locale) {
//        return SimpleFastDateFormat.getInstance(pattern, null, locale);
//    }
//
//    public static synchronized SimpleFastDateFormat getInstance(String pattern, TimeZone timeZone, Locale locale) {
//        SimpleFastDateFormat emptyFormat = new SimpleFastDateFormat(pattern, timeZone, locale);
//        SimpleFastDateFormat format = (SimpleFastDateFormat)cInstanceCache.get(emptyFormat);
//        if (format == null) {
//            format = emptyFormat;
//            emptyFormat.init();
//            cInstanceCache.put(emptyFormat, emptyFormat);
//        }
//        return format;
//    }
//
//    public static SimpleFastDateFormat getDateInstance(int style) {
//        return SimpleFastDateFormat.getDateInstance(style, null, null);
//    }
//
//    public static SimpleFastDateFormat getDateInstance(int style, Locale locale) {
//        return SimpleFastDateFormat.getDateInstance(style, null, locale);
//    }
//
//    public static SimpleFastDateFormat getDateInstance(int style, TimeZone timeZone) {
//        return SimpleFastDateFormat.getDateInstance(style, timeZone, null);
//    }
//
//    public static synchronized SimpleFastDateFormat getDateInstance(int style, TimeZone timeZone, Locale locale) {
//        Pair key1;
//        SimpleFastDateFormat format;
//        Object key = new Integer(style);
//        if (timeZone != null) {
//            key = new Pair(key, timeZone);
//        }
//        if (locale == null) {
//            locale = Locale.getDefault();
//        }
//        if ((format = (SimpleFastDateFormat)cDateInstanceCache.get(key1 = new Pair(key, locale))) == null) {
//            try {
//                SimpleDateFormat ex = (SimpleDateFormat)DateFormat.getDateInstance(style, locale);
//                String pattern = ex.toPattern();
//                format = SimpleFastDateFormat.getInstance(pattern, timeZone, locale);
//                cDateInstanceCache.put(key1, format);
//            }
//            catch (ClassCastException var8) {
//                throw new IllegalArgumentException("No date pattern for locale: " + locale);
//            }
//        }
//        return format;
//    }
//
//    public static SimpleFastDateFormat getTimeInstance(int style) {
//        return SimpleFastDateFormat.getTimeInstance(style, null, null);
//    }
//
//    public static SimpleFastDateFormat getTimeInstance(int style, Locale locale) {
//        return SimpleFastDateFormat.getTimeInstance(style, null, locale);
//    }
//
//    public static SimpleFastDateFormat getTimeInstance(int style, TimeZone timeZone) {
//        return SimpleFastDateFormat.getTimeInstance(style, timeZone, null);
//    }
//
//    public static synchronized SimpleFastDateFormat getTimeInstance(int style, TimeZone timeZone, Locale locale) {
//        SimpleFastDateFormat format;
//        Object key = new Integer(style);
//        if (timeZone != null) {
//            key = new Pair(key, timeZone);
//        }
//        if (locale != null) {
//            key = new Pair(key, locale);
//        }
//        if ((format = (SimpleFastDateFormat)cTimeInstanceCache.get(key)) == null) {
//            if (locale == null) {
//                locale = Locale.getDefault();
//            }
//            try {
//                SimpleDateFormat ex = (SimpleDateFormat)DateFormat.getTimeInstance(style, locale);
//                String pattern = ex.toPattern();
//                format = SimpleFastDateFormat.getInstance(pattern, timeZone, locale);
//                cTimeInstanceCache.put(key, format);
//            }
//            catch (ClassCastException var7) {
//                throw new IllegalArgumentException("No date pattern for locale: " + locale);
//            }
//        }
//        return format;
//    }
//
//    public static SimpleFastDateFormat getDateTimeInstance(int dateStyle, int timeStyle) {
//        return SimpleFastDateFormat.getDateTimeInstance(dateStyle, timeStyle, null, null);
//    }
//
//    public static SimpleFastDateFormat getDateTimeInstance(int dateStyle, int timeStyle, Locale locale) {
//        return SimpleFastDateFormat.getDateTimeInstance(dateStyle, timeStyle, null, locale);
//    }
//
//    public static SimpleFastDateFormat getDateTimeInstance(int dateStyle, int timeStyle, TimeZone timeZone) {
//        return SimpleFastDateFormat.getDateTimeInstance(dateStyle, timeStyle, timeZone, null);
//    }
//
//    public static synchronized SimpleFastDateFormat getDateTimeInstance(int dateStyle, int timeStyle, TimeZone timeZone, Locale locale) {
//        SimpleFastDateFormat format;
//        Pair key = new Pair(new Integer(dateStyle), new Integer(timeStyle));
//        if (timeZone != null) {
//            key = new Pair(key, timeZone);
//        }
//        if (locale == null) {
//            locale = Locale.getDefault();
//        }
//        if ((format = (SimpleFastDateFormat)cDateTimeInstanceCache.get(key = new Pair(key, locale))) == null) {
//            try {
//                SimpleDateFormat ex = (SimpleDateFormat)DateFormat.getDateTimeInstance(dateStyle, timeStyle, locale);
//                String pattern = ex.toPattern();
//                format = SimpleFastDateFormat.getInstance(pattern, timeZone, locale);
//                cDateTimeInstanceCache.put(key, format);
//            }
//            catch (ClassCastException var8) {
//                throw new IllegalArgumentException("No date time pattern for locale: " + locale);
//            }
//        }
//        return format;
//    }
//
//    static synchronized String getTimeZoneDisplay(TimeZone tz, boolean daylight, int style, Locale locale) {
//        TimeZoneDisplayKey key = new TimeZoneDisplayKey(tz, daylight, style, locale);
//        String value = (String)cTimeZoneDisplayCache.get(key);
//        if (value == null) {
//            value = tz.getDisplayName(daylight, style, locale);
//            cTimeZoneDisplayCache.put(key, value);
//        }
//        return value;
//    }
//
//    private static synchronized String getDefaultPattern() {
//        if (cDefaultPattern == null) {
//            cDefaultPattern = new SimpleDateFormat().toPattern();
//        }
//        return cDefaultPattern;
//    }
//
//    protected SimpleFastDateFormat(String pattern, TimeZone timeZone, Locale locale) {
//        if (pattern == null) {
//            throw new IllegalArgumentException("The pattern must not be null");
//        }
//        this.mPattern = pattern;
//        boolean bl = this.mTimeZoneForced = timeZone != null;
//        if (timeZone == null) {
//            timeZone = TimeZone.getDefault();
//        }
//        this.mTimeZone = timeZone;
//        boolean bl2 = this.mLocaleForced = locale != null;
//        if (locale == null) {
//            locale = Locale.getDefault();
//        }
//        this.mLocale = locale;
//    }
//
//    protected void init() {
//        List rulesList = this.parsePattern();
//        this.mRules = rulesList.toArray(new Rule[rulesList.size()]);
//        int len = 0;
//        int i = this.mRules.length;
//        do {
//            if (--i < 0) {
//                this.mMaxLengthEstimate = len;
//                return;
//            }
//            len += this.mRules[i].estimateLength();
//        } while (true);
//    }
//
//    protected List parsePattern() {
//        DateFormatSymbols symbols = new DateFormatSymbols(this.mLocale);
//        ArrayList<NumberRule> rules = new ArrayList<NumberRule>();
//        String[] ERAs = symbols.getEras();
//        String[] months = symbols.getMonths();
//        String[] shortMonths = symbols.getShortMonths();
//        String[] weekdays = symbols.getWeekdays();
//        String[] shortWeekdays = symbols.getShortWeekdays();
//        String[] AmPmStrings = symbols.getAmPmStrings();
//        int length = this.mPattern.length();
//        int[] indexRef = new int[1];
//        int i = 0;
//        while (i < length) {
//            Rule rule2;
//            indexRef[0] = i;
//            String token = this.parseToken(this.mPattern, indexRef);
//            i = indexRef[0];
//            int tokenLen = token.length();
//            if (tokenLen == 0) break;
//            char c = token.charAt(0);
//            switch (c) {
//                Rule rule2;
//                case '\'': {
//                    String sub = token.substring(1);
//                    if (sub.length() == 1) {
//                        rule2 = new CharacterLiteral(sub.charAt(0));
//                        break;
//                    }
//                    rule2 = new StringLiteral(sub);
//                    break;
//                }
//                default: {
//                    throw new IllegalArgumentException("Illegal pattern component: " + token);
//                }
//                case 'D': {
//                    rule2 = this.selectNumberRule(6, tokenLen);
//                    break;
//                }
//                case 'E': {
//                    rule2 = new TextField(7, tokenLen < 4 ? shortWeekdays : weekdays);
//                    break;
//                }
//                case 'F': {
//                    rule2 = this.selectNumberRule(8, tokenLen);
//                    break;
//                }
//                case 'G': {
//                    rule2 = new TextField(0, ERAs);
//                    break;
//                }
//                case 'H': {
//                    rule2 = this.selectNumberRule(11, tokenLen);
//                    break;
//                }
//                case 'K': {
//                    rule2 = this.selectNumberRule(10, tokenLen);
//                    break;
//                }
//                case 'M': {
//                    if (tokenLen >= 4) {
//                        rule2 = new TextField(2, months);
//                        break;
//                    }
//                    if (tokenLen == 3) {
//                        rule2 = new TextField(2, shortMonths);
//                        break;
//                    }
//                    if (tokenLen == 2) {
//                        rule2 = TwoDigitMonthField.INSTANCE;
//                        break;
//                    }
//                    rule2 = UnpaddedMonthField.INSTANCE;
//                    break;
//                }
//                case 'S': {
//                    rule2 = this.selectNumberRule(14, tokenLen);
//                    break;
//                }
//                case 'W': {
//                    rule2 = this.selectNumberRule(4, tokenLen);
//                    break;
//                }
//                case 'Z': {
//                    if (tokenLen == 1) {
//                        rule2 = TimeZoneNumberRule.INSTANCE_NO_COLON;
//                        break;
//                    }
//                    rule2 = TimeZoneNumberRule.INSTANCE_COLON;
//                    break;
//                }
//                case 'a': {
//                    rule2 = new TextField(9, AmPmStrings);
//                    break;
//                }
//                case 'd': {
//                    rule2 = this.selectNumberRule(5, tokenLen);
//                    break;
//                }
//                case 'h': {
//                    rule2 = new TwelveHourField(this.selectNumberRule(10, tokenLen));
//                    break;
//                }
//                case 'k': {
//                    rule2 = new TwentyFourHourField(this.selectNumberRule(11, tokenLen));
//                    break;
//                }
//                case 'm': {
//                    rule2 = this.selectNumberRule(12, tokenLen);
//                    break;
//                }
//                case 's': {
//                    rule2 = this.selectNumberRule(13, tokenLen);
//                    break;
//                }
//                case 'w': {
//                    rule2 = this.selectNumberRule(3, tokenLen);
//                    break;
//                }
//                case 'y': {
//                    if (tokenLen >= 4) {
//                        rule2 = this.selectNumberRule(1, tokenLen);
//                        break;
//                    }
//                    rule2 = TwoDigitYearField.INSTANCE;
//                    break;
//                }
//                case 'z': {
//                    rule2 = tokenLen >= 4 ? new TimeZoneNameRule(this.mTimeZone, this.mTimeZoneForced, this.mLocale, 1) : new TimeZoneNameRule(this.mTimeZone, this.mTimeZoneForced, this.mLocale, 0);
//                }
//            }
//            rules.add((NumberRule)rule2);
//            ++i;
//        }
//        return rules;
//    }
//
//    protected String parseToken(String pattern, int[] indexRef) {
//        StringBuffer buf;
//        int i;
//        buf = new StringBuffer();
//        i = indexRef[0];
//        int length = pattern.length();
//        char c = pattern.charAt(i);
//        if (c >= 'A' && c <= 'Z' || c >= 'a' && c <= 'z') {
//            buf.append(c);
//            while (i + 1 < length) {
//                char var8 = pattern.charAt(i + 1);
//                if (var8 == c) {
//                    buf.append(c);
//                    ++i;
//                    continue;
//                }
//                break;
//            }
//        } else {
//            buf.append('\'');
//            boolean inLiteral = false;
//            while (i < length) {
//                c = pattern.charAt(i);
//                if (c == '\'') {
//                    if (i + 1 < length && pattern.charAt(i + 1) == '\'') {
//                        ++i;
//                        buf.append(c);
//                    } else {
//                        inLiteral = !inLiteral;
//                    }
//                } else {
//                    if (!inLiteral && (c >= 'A' && c <= 'Z' || c >= 'a' && c <= 'z')) {
//                        --i;
//                        break;
//                    }
//                    buf.append(c);
//                }
//                ++i;
//            }
//        }
//        indexRef[0] = i;
//        return buf.toString();
//    }
//
//    protected NumberRule selectNumberRule(int field, int padding) {
//        switch (padding) {
//            case 1: {
//                return new UnpaddedNumberField(field);
//            }
//            case 2: {
//                return new TwoDigitNumberField(field);
//            }
//        }
//        return new PaddedNumberField(field, padding);
//    }
//
//    @Override
//    public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
//        if (obj instanceof Date) {
//            return this.format((Date)obj, toAppendTo);
//        }
//        if (obj instanceof Calendar) {
//            return this.format((Calendar)obj, toAppendTo);
//        }
//        if (obj instanceof Long) {
//            return this.format((Long)obj, toAppendTo);
//        }
//        throw new IllegalArgumentException("Unknown class: " + (obj == null ? "<null>" : obj.getClass().getName()));
//    }
//
//    public String format(long millis) {
//        return this.format(new Date(millis));
//    }
//
//    public String format(Date date) {
//        GregorianCalendar c = new GregorianCalendar(this.mTimeZone);
//        c.setTime(date);
//        return this.applyRules(c, new StringBuffer(this.mMaxLengthEstimate)).toString();
//    }
//
//    public String format(Calendar calendar) {
//        return this.format(calendar, new StringBuffer(this.mMaxLengthEstimate)).toString();
//    }
//
//    public StringBuffer format(long millis, StringBuffer buf) {
//        return this.format(new Date(millis), buf);
//    }
//
//    public StringBuffer format(Date date, StringBuffer buf) {
//        GregorianCalendar c = new GregorianCalendar(this.mTimeZone);
//        c.setTime(date);
//        return this.applyRules(c, buf);
//    }
//
//    public StringBuffer format(Calendar calendar, StringBuffer buf) {
//        if (this.mTimeZoneForced) {
//            calendar.getTime();
//            calendar = (Calendar)calendar.clone();
//            calendar.setTimeZone(this.mTimeZone);
//        }
//        return this.applyRules(calendar, buf);
//    }
//
//    protected StringBuffer applyRules(Calendar calendar, StringBuffer buf) {
//        Rule[] rules = this.mRules;
//        int len = this.mRules.length;
//        int i = 0;
//        while (i < len) {
//            rules[i].appendTo(buf, calendar);
//            ++i;
//        }
//        return buf;
//    }
//
//    @Override
//    public Object parseObject(String source, ParsePosition pos) {
//        pos.setIndex(0);
//        pos.setErrorIndex(0);
//        return null;
//    }
//
//    public String getPattern() {
//        return this.mPattern;
//    }
//
//    public TimeZone getTimeZone() {
//        return this.mTimeZone;
//    }
//
//    public boolean getTimeZoneOverridesCalendar() {
//        return this.mTimeZoneForced;
//    }
//
//    public Locale getLocale() {
//        return this.mLocale;
//    }
//
//    public int getMaxLengthEstimate() {
//        return this.mMaxLengthEstimate;
//    }
//
//    public boolean equals(Object obj) {
//        if (!(obj instanceof SimpleFastDateFormat)) {
//            return false;
//        }
//        SimpleFastDateFormat other = (SimpleFastDateFormat)obj;
//        if (!(this.mPattern != other.mPattern && !this.mPattern.equals(other.mPattern) || this.mTimeZone != other.mTimeZone && !this.mTimeZone.equals(other.mTimeZone) || this.mLocale != other.mLocale && !this.mLocale.equals(other.mLocale) || this.mTimeZoneForced != other.mTimeZoneForced || this.mLocaleForced != other.mLocaleForced)) {
//            return true;
//        }
//        return false;
//    }
//
//    public int hashCode() {
//        int total = 0;
//        int total1 = total + this.mPattern.hashCode();
//        total1 += this.mTimeZone.hashCode();
//        total1 += this.mTimeZoneForced ? 1 : 0;
//        total1 += this.mLocale.hashCode();
//        return total1 += this.mLocaleForced ? 1 : 0;
//    }
//
//    public String toString() {
//        return "SimpleFastDateFormat[" + this.mPattern + "]";
//    }
//
//    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
//        in.defaultReadObject();
//        this.init();
//    }
//
//    private static class CharacterLiteral
//    implements Rule {
//        private final char mValue;
//
//        CharacterLiteral(char value) {
//            this.mValue = value;
//        }
//
//        @Override
//        public int estimateLength() {
//            return 1;
//        }
//
//        @Override
//        public void appendTo(StringBuffer buffer, Calendar calendar) {
//            buffer.append(this.mValue);
//        }
//    }
//
//    private static interface NumberRule
//    extends Rule {
//        public void appendTo(StringBuffer var1, int var2);
//    }
//
//    private static class PaddedNumberField
//    implements NumberRule {
//        private final int mField;
//        private final int mSize;
//
//        PaddedNumberField(int field, int size) {
//            if (size < 3) {
//                throw new IllegalArgumentException();
//            }
//            this.mField = field;
//            this.mSize = size;
//        }
//
//        @Override
//        public int estimateLength() {
//            return 4;
//        }
//
//        @Override
//        public void appendTo(StringBuffer buffer, Calendar calendar) {
//            this.appendTo(buffer, calendar.get(this.mField));
//        }
//
//        @Override
//        public final void appendTo(StringBuffer buffer, int value) {
//            block8 : {
//                int digits;
//                if (value < 100) {
//                    int digits2 = this.mSize;
//                    do {
//                        if (--digits2 < 2) {
//                            buffer.append((char)(value / 10 + 48));
//                            buffer.append((char)(value % 10 + 48));
//                            break block8;
//                        }
//                        buffer.append('0');
//                    } while (true);
//                }
//                if (value < 1000) {
//                    digits = 3;
//                } else {
//                    if (value <= -1) {
//                        throw new IllegalArgumentException("Negative values should not be possible" + value);
//                    }
//                    digits = Integer.toString(value).length();
//                }
//                int i = this.mSize;
//                do {
//                    if (--i < digits) {
//                        buffer.append(Integer.toString(value));
//                        break;
//                    }
//                    buffer.append('0');
//                } while (true);
//            }
//        }
//    }
//
//    private static class Pair {
//        private final Object mObj1;
//        private final Object mObj2;
//
//        public Pair(Object obj1, Object obj2) {
//            this.mObj1 = obj1;
//            this.mObj2 = obj2;
//        }
//
//        public boolean equals(Object obj) {
//            if (this == obj) {
//                return true;
//            }
//            if (!(obj instanceof Pair)) {
//                return false;
//            }
//            Pair key = (Pair)obj;
//            if (this.mObj1 == null ? key.mObj1 != null : !this.mObj1.equals(key.mObj1)) {
//                return false;
//            }
//            if (this.mObj2 == null ? key.mObj2 == null : this.mObj2.equals(key.mObj2)) {
//                return true;
//            }
//            return false;
//        }
//
//        public int hashCode() {
//            return (this.mObj1 == null ? 0 : this.mObj1.hashCode()) + (this.mObj2 == null ? 0 : this.mObj2.hashCode());
//        }
//
//        public String toString() {
//            return "[" + this.mObj1 + ':' + this.mObj2 + ']';
//        }
//    }
//
//    private static interface Rule {
//        public int estimateLength();
//
//        public void appendTo(StringBuffer var1, Calendar var2);
//    }
//
//    private static class StringLiteral
//    implements Rule {
//        private final String mValue;
//
//        StringLiteral(String value) {
//            this.mValue = value;
//        }
//
//        @Override
//        public int estimateLength() {
//            return this.mValue.length();
//        }
//
//        @Override
//        public void appendTo(StringBuffer buffer, Calendar calendar) {
//            buffer.append(this.mValue);
//        }
//    }
//
//    private static class TextField
//    implements Rule {
//        private final int mField;
//        private final String[] mValues;
//
//        TextField(int field, String[] values) {
//            this.mField = field;
//            this.mValues = values;
//        }
//
//        @Override
//        public int estimateLength() {
//            int max = 0;
//            int i = this.mValues.length;
//            while (--i >= 0) {
//                int len = this.mValues[i].length();
//                if (len <= max) continue;
//                max = len;
//            }
//            return max;
//        }
//
//        @Override
//        public void appendTo(StringBuffer buffer, Calendar calendar) {
//            buffer.append(this.mValues[calendar.get(this.mField)]);
//        }
//    }
//
//    private static class TimeZoneDisplayKey {
//        private final TimeZone mTimeZone;
//        private final int mStyle;
//        private final Locale mLocale;
//
//        TimeZoneDisplayKey(TimeZone timeZone, boolean daylight, int style, Locale locale) {
//            this.mTimeZone = timeZone;
//            if (daylight) {
//                style |= Integer.MIN_VALUE;
//            }
//            this.mStyle = style;
//            this.mLocale = locale;
//        }
//
//        public int hashCode() {
//            return this.mStyle * 31 + this.mLocale.hashCode();
//        }
//
//        public boolean equals(Object obj) {
//            if (this == obj) {
//                return true;
//            }
//            if (!(obj instanceof TimeZoneDisplayKey)) {
//                return false;
//            }
//            TimeZoneDisplayKey other = (TimeZoneDisplayKey)obj;
//            if (this.mTimeZone.equals(other.mTimeZone) && this.mStyle == other.mStyle && this.mLocale.equals(other.mLocale)) {
//                return true;
//            }
//            return false;
//        }
//    }
//
//    private static class TimeZoneNameRule
//    implements Rule {
//        private final TimeZone mTimeZone;
//        private final boolean mTimeZoneForced;
//        private final Locale mLocale;
//        private final int mStyle;
//        private final String mStandard;
//        private final String mDaylight;
//
//        TimeZoneNameRule(TimeZone timeZone, boolean timeZoneForced, Locale locale, int style) {
//            this.mTimeZone = timeZone;
//            this.mTimeZoneForced = timeZoneForced;
//            this.mLocale = locale;
//            this.mStyle = style;
//            if (timeZoneForced) {
//                this.mStandard = SimpleFastDateFormat.getTimeZoneDisplay(timeZone, false, style, locale);
//                this.mDaylight = SimpleFastDateFormat.getTimeZoneDisplay(timeZone, true, style, locale);
//            } else {
//                this.mStandard = null;
//                this.mDaylight = null;
//            }
//        }
//
//        @Override
//        public int estimateLength() {
//            return this.mTimeZoneForced ? Math.max(this.mStandard.length(), this.mDaylight.length()) : (this.mStyle == 0 ? 4 : 40);
//        }
//
//        @Override
//        public void appendTo(StringBuffer buffer, Calendar calendar) {
//            if (this.mTimeZoneForced) {
//                if (this.mTimeZone.useDaylightTime() && calendar.get(16) != 0) {
//                    buffer.append(this.mDaylight);
//                } else {
//                    buffer.append(this.mStandard);
//                }
//            } else {
//                TimeZone timeZone = calendar.getTimeZone();
//                if (timeZone.useDaylightTime() && calendar.get(16) != 0) {
//                    buffer.append(SimpleFastDateFormat.getTimeZoneDisplay(timeZone, true, this.mStyle, this.mLocale));
//                } else {
//                    buffer.append(SimpleFastDateFormat.getTimeZoneDisplay(timeZone, false, this.mStyle, this.mLocale));
//                }
//            }
//        }
//    }
//
//    private static class TimeZoneNumberRule
//    implements Rule {
//        static final TimeZoneNumberRule INSTANCE_COLON = new TimeZoneNumberRule(true);
//        static final TimeZoneNumberRule INSTANCE_NO_COLON = new TimeZoneNumberRule(false);
//        final boolean mColon;
//
//        TimeZoneNumberRule(boolean colon) {
//            this.mColon = colon;
//        }
//
//        @Override
//        public int estimateLength() {
//            return 5;
//        }
//
//        @Override
//        public void appendTo(StringBuffer buffer, Calendar calendar) {
//            int offset = calendar.get(15) + calendar.get(16);
//            if (offset < 0) {
//                buffer.append('-');
//                offset = - offset;
//            } else {
//                buffer.append('+');
//            }
//            int hours = offset / 3600000;
//            buffer.append((char)(hours / 10 + 48));
//            buffer.append((char)(hours % 10 + 48));
//            if (this.mColon) {
//                buffer.append(':');
//            }
//            int minutes = offset / 60000 - 60 * hours;
//            buffer.append((char)(minutes / 10 + 48));
//            buffer.append((char)(minutes % 10 + 48));
//        }
//    }
//
//    private static class TwelveHourField
//    implements NumberRule {
//        private final NumberRule mRule;
//
//        TwelveHourField(NumberRule rule) {
//            this.mRule = rule;
//        }
//
//        @Override
//        public int estimateLength() {
//            return this.mRule.estimateLength();
//        }
//
//        @Override
//        public void appendTo(StringBuffer buffer, Calendar calendar) {
//            int value = calendar.get(10);
//            if (value == 0) {
//                value = calendar.getLeastMaximum(10) + 1;
//            }
//            this.mRule.appendTo(buffer, value);
//        }
//
//        @Override
//        public void appendTo(StringBuffer buffer, int value) {
//            this.mRule.appendTo(buffer, value);
//        }
//    }
//
//    private static class TwentyFourHourField
//    implements NumberRule {
//        private final NumberRule mRule;
//
//        TwentyFourHourField(NumberRule rule) {
//            this.mRule = rule;
//        }
//
//        @Override
//        public int estimateLength() {
//            return this.mRule.estimateLength();
//        }
//
//        @Override
//        public void appendTo(StringBuffer buffer, Calendar calendar) {
//            int value = calendar.get(11);
//            if (value == 0) {
//                value = calendar.getMaximum(11) + 1;
//            }
//            this.mRule.appendTo(buffer, value);
//        }
//
//        @Override
//        public void appendTo(StringBuffer buffer, int value) {
//            this.mRule.appendTo(buffer, value);
//        }
//    }
//
//    private static class TwoDigitMonthField
//    implements NumberRule {
//        static final TwoDigitMonthField INSTANCE = new TwoDigitMonthField();
//
//        TwoDigitMonthField() {
//        }
//
//        @Override
//        public int estimateLength() {
//            return 2;
//        }
//
//        @Override
//        public void appendTo(StringBuffer buffer, Calendar calendar) {
//            this.appendTo(buffer, calendar.get(2) + 1);
//        }
//
//        @Override
//        public final void appendTo(StringBuffer buffer, int value) {
//            buffer.append((char)(value / 10 + 48));
//            buffer.append((char)(value % 10 + 48));
//        }
//    }
//
//    private static class TwoDigitNumberField
//    implements NumberRule {
//        private final int mField;
//
//        TwoDigitNumberField(int field) {
//            this.mField = field;
//        }
//
//        @Override
//        public int estimateLength() {
//            return 2;
//        }
//
//        @Override
//        public void appendTo(StringBuffer buffer, Calendar calendar) {
//            this.appendTo(buffer, calendar.get(this.mField));
//        }
//
//        @Override
//        public final void appendTo(StringBuffer buffer, int value) {
//            if (value < 100) {
//                buffer.append((char)(value / 10 + 48));
//                buffer.append((char)(value % 10 + 48));
//            } else {
//                buffer.append(Integer.toString(value));
//            }
//        }
//    }
//
//    private static class TwoDigitYearField
//    implements NumberRule {
//        static final TwoDigitYearField INSTANCE = new TwoDigitYearField();
//
//        TwoDigitYearField() {
//        }
//
//        @Override
//        public int estimateLength() {
//            return 2;
//        }
//
//        @Override
//        public void appendTo(StringBuffer buffer, Calendar calendar) {
//            this.appendTo(buffer, calendar.get(1) % 100);
//        }
//
//        @Override
//        public final void appendTo(StringBuffer buffer, int value) {
//            buffer.append((char)(value / 10 + 48));
//            buffer.append((char)(value % 10 + 48));
//        }
//    }
//
//    private static class UnpaddedMonthField
//    implements NumberRule {
//        static final UnpaddedMonthField INSTANCE = new UnpaddedMonthField();
//
//        UnpaddedMonthField() {
//        }
//
//        @Override
//        public int estimateLength() {
//            return 2;
//        }
//
//        @Override
//        public void appendTo(StringBuffer buffer, Calendar calendar) {
//            this.appendTo(buffer, calendar.get(2) + 1);
//        }
//
//        @Override
//        public final void appendTo(StringBuffer buffer, int value) {
//            if (value < 10) {
//                buffer.append((char)(value + 48));
//            } else {
//                buffer.append((char)(value / 10 + 48));
//                buffer.append((char)(value % 10 + 48));
//            }
//        }
//    }
//
//    private static class UnpaddedNumberField
//    implements NumberRule {
//        private final int mField;
//
//        UnpaddedNumberField(int field) {
//            this.mField = field;
//        }
//
//        @Override
//        public int estimateLength() {
//            return 4;
//        }
//
//        @Override
//        public void appendTo(StringBuffer buffer, Calendar calendar) {
//            this.appendTo(buffer, calendar.get(this.mField));
//        }
//
//        @Override
//        public final void appendTo(StringBuffer buffer, int value) {
//            if (value < 10) {
//                buffer.append((char)(value + 48));
//            } else if (value < 100) {
//                buffer.append((char)(value / 10 + 48));
//                buffer.append((char)(value % 10 + 48));
//            } else {
//                buffer.append(Integer.toString(value));
//            }
//        }
//    }
//
//}
