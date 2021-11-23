package com.fingerchar.api.utils;


import java.util.Arrays;
import java.util.List;

import org.springframework.util.StringUtils;

public class Str2ArrayUtils {

    public static List<String> sliceString2StringArray(String content) {
        return sliceString2StringArray(content, ",");
    }

    public static List<String> sliceString2StringArray(String content, String sepatrator) {
        if (StringUtils.isEmpty(content)) {
            return null;
        }
        if(StringUtils.isEmpty(sepatrator)) {
        	return null;
        }
        content = StringUtils.trimAllWhitespace(content);
        String[] arr = content.split(sepatrator);
        if (arr != null && arr.length > 0) {
            return Arrays.asList(arr);
        }
        return null;
    }

}
