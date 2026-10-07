package io.netbird.client.tool;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import io.netbird.client.tool.autoconnect.TrustedNetwork;

/**
 * Covers the match rule AutoConnectController.isCurrentWifiExempt() relies on:
 * a trusted SSID makes the current Wi-Fi exempt, so the trigger no longer
 * matches and a running tunnel is disconnected.
 */
public class TrustedNetworkUnitTest {
    @Test
    public void ssidEntryMatchesSameSsidOnAnyAccessPoint() {
        TrustedNetwork trusted = new TrustedNetwork("HomeWifi", null);
        assertTrue(trusted.matches("HomeWifi", "AA:BB:CC:DD:EE:01"));
        assertTrue(trusted.matches("HomeWifi", "AA:BB:CC:DD:EE:02"));
    }

    @Test
    public void ssidEntryDoesNotMatchOtherSsid() {
        TrustedNetwork trusted = new TrustedNetwork("HomeWifi", null);
        assertFalse(trusted.matches("CafeWifi", "AA:BB:CC:DD:EE:01"));
        assertFalse(trusted.matches("homewifi", null));
        assertFalse(trusted.matches(null, null));
    }

    @Test
    public void bssidEntryIsSoleMatchKeyAndCaseInsensitive() {
        TrustedNetwork trusted = new TrustedNetwork("HomeWifi", "aa:bb:cc:dd:ee:01");
        assertTrue(trusted.matches("Other", "AA:BB:CC:DD:EE:01"));
        assertFalse(trusted.matches("HomeWifi", "AA:BB:CC:DD:EE:02"));
        assertFalse(trusted.matches("HomeWifi", null));
    }

    @Test
    public void ssidIsTrimmedAndBlankBecomesNull() {
        assertEquals("HomeWifi", new TrustedNetwork("  HomeWifi ", null).ssid);
        assertNull(new TrustedNetwork("   ", null).ssid);
        assertFalse(new TrustedNetwork("   ", null).matches("   ", null));
    }
}
