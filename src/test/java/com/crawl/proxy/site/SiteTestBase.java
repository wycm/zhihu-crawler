package com.crawl.proxy.site;

import java.util.regex.Pattern;

public class SiteTestBase {
    protected String validIpAddressRegex = "^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$";
    protected Pattern pattern = Pattern.compile(validIpAddressRegex);
}
