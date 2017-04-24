/*
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/
 */
package org.phenotips.pingback.internal.client.data;

import org.phenotips.pingback.internal.client.PingDataProvider;

import org.xwiki.test.mockito.MockitoComponentMockingRule;

import java.util.Map;

import org.json.JSONObject;
import org.junit.Rule;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import junit.framework.Assert;

/**
 * Unit tests for {@link OsPingDataProvider}.
 *
 * @version: $Id$
 */
public class OsPingDataProviderTest
{
    @Rule
    public MockitoComponentMockingRule<PingDataProvider> mocker =
        new MockitoComponentMockingRule<PingDataProvider>(OsPingDataProvider.class);

    @Test
    public void testProvideMapping() throws Exception
    {
        JSONAssert.assertEquals("{\"osArch\":{\"type\":\"string\",\"index\":\"not_analyzed\"},"
            + "\"osName\":{\"type\":\"string\",\"index\":\"not_analyzed\"},"
            + "\"osVersion\":{\"type\":\"string\",\"index\":\"not_analyzed\"}}",
            new JSONObject(this.mocker.getComponentUnderTest().provideMapping()), false);
    }

    @Test
    public void provideDataReportsMemoryStats() throws Exception
    {
        Map<String, Object> result = this.mocker.getComponentUnderTest().provideData();
        Assert.assertNotNull(result);
        Assert.assertFalse(result.isEmpty());
        Assert.assertEquals(System.getProperty("os.arch"), result.get("osArch"));
        Assert.assertEquals(System.getProperty("os.name"), result.get("osName"));
        Assert.assertEquals(System.getProperty("os.version"), result.get("osVersion"));
    }
}
