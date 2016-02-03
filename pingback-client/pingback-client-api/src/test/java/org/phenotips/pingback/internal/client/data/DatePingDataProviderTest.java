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

import org.phenotips.pingback.internal.JestClientManager;

import org.xwiki.instance.InstanceId;
import org.xwiki.instance.InstanceIdManager;
import org.xwiki.test.mockito.MockitoComponentMockingRule;

import java.util.UUID;

import org.junit.Rule;
import org.junit.Test;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import io.searchbox.client.JestClient;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import net.sf.json.JSONObject;
import net.sf.json.test.JSONAssert;

/**
 * Unit tests for {@link DatePingDataProvider}.
 *
 * @version $Id: 6c2ebdc183c5120f58766cad8a44e85539ff91c5 $
 * @since 6.1M1
 */
public class DatePingDataProviderTest
{
    @Rule
    public MockitoComponentMockingRule<DatePingDataProvider> mocker =
            new MockitoComponentMockingRule<>(DatePingDataProvider.class);

    @Test
    public void provideMapping() throws Exception {
        JSONAssert.assertEquals("{\"sinceDays\":{\"type\":\"long\"},\"firstPingDate\":{\"type\":\"date\"}}",
                JSONObject.fromObject(this.mocker.getComponentUnderTest().provideMapping()));
    }

    @Test
    public void provideData() throws Exception {
        InstanceId id = new InstanceId(UUID.randomUUID().toString());
        InstanceIdManager idManager = this.mocker.getInstance(InstanceIdManager.class);
        when(idManager.getInstanceId()).thenReturn(id);

        JestClient client = mock(JestClient.class);
        SearchResult searchResult = new SearchResult(new Gson());
        String resultString = "{\n" +
                "   \"took\": 4,\n" +
                "   \"timed_out\": false,\n" +
                "   \"_shards\": {\n" +
                "      \"total\": 5,\n" +
                "      \"successful\": 5,\n" +
                "      \"failed\": 0\n" +
                "   },\n" +
                "   \"hits\": {\n" +
                "      \"total\": 2,\n" +
                "      \"max_score\": 0,\n" +
                "      \"hits\": []\n" +
                "   },\n" +
                "   \"aggregations\": {\n" +
                "      \"firstPingDate\": {\n" +
                "         \"value\": 1392854400000\n" +
                "      },\n" +
                "      \"serverTime\": {\n" +
                "         \"value\": 1393200000000\n" +
                "      }\n" +
                "   }\n" +
                "}";
        searchResult.setJsonString(resultString);
        searchResult.setJsonObject(new JsonParser().parse(resultString).getAsJsonObject());
        searchResult.setSucceeded(true);
        when(client.execute(any(Search.class))).thenReturn(searchResult);

        JestClientManager jestManager = this.mocker.getInstance(JestClientManager.class);
        when(jestManager.getClient()).thenReturn(client);

        JSONAssert.assertEquals("{\"sinceDays\":4,\"firstPingDate\":1392854400000}",
                JSONObject.fromObject(this.mocker.getComponentUnderTest().provideData()));
    }
}
