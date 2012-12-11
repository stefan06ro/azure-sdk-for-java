/**
 * Copyright 2012 Microsoft Corporation
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.microsoft.windowsazure.services.media;

import static org.junit.Assert.*;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.junit.Test;

import com.microsoft.windowsazure.services.core.ServiceException;
import com.microsoft.windowsazure.services.media.models.Asset;
import com.microsoft.windowsazure.services.media.models.AssetInfo;
import com.microsoft.windowsazure.services.media.models.AssetState;
import com.microsoft.windowsazure.services.media.models.ContentKey;
import com.microsoft.windowsazure.services.media.models.ContentKeyInfo;
import com.microsoft.windowsazure.services.media.models.ContentKeyType;
import com.microsoft.windowsazure.services.media.models.EncryptionOption;
import com.sun.jersey.core.util.MultivaluedMapImpl;

public class AssetIntegrationTest extends IntegrationTestBase {

    private void verifyInfosEqual(String message, AssetInfo expected, AssetInfo actual) {
        verifyAssetProperties(message, expected.getName(), expected.getAlternateId(), expected.getOptions(),
                expected.getState(), actual);
    }

    private void verifyAssetProperties(String message, String testName, String altId,
            EncryptionOption encryptionOption, AssetState assetState, AssetInfo actualAsset) {
        verifyAssetProperties(message, testName, altId, encryptionOption, assetState, null, null, null, actualAsset);
    }

    private void verifyAssetProperties(String message, String testName, String altId,
            EncryptionOption encryptionOption, AssetState assetState, String id, Date created, Date lastModified,
            AssetInfo actualAsset) {
        assertNotNull(message, actualAsset);
        assertEquals(message + " Name", testName, actualAsset.getName());
        assertEquals(message + " AlternateId", altId, actualAsset.getAlternateId());
        assertEquals(message + " Options", encryptionOption, actualAsset.getOptions());
        assertEquals(message + " State", assetState, actualAsset.getState());
        if (id != null) {
            assertEquals(message + " Id", id, actualAsset.getId());
        }
        if (created != null) {
            assertEquals(message + " Created", created, actualAsset.getCreated());
        }
        if (lastModified != null) {
            assertEquals(message + " LastModified", lastModified, actualAsset.getLastModified());
        }
    }

    @Test
    public void createAssetOptionsSuccess() throws Exception {
        // Arrange
        String testName = testAssetPrefix + "createAssetOptionsSuccess";
        String altId = "altId";
        EncryptionOption encryptionOption = EncryptionOption.StorageEncrypted;
        AssetState assetState = AssetState.Published;

        // Act
        AssetInfo actualAsset = service.create(Asset.create().setAlternateId(altId).setOptions(encryptionOption)
                .setState(assetState).setName(testName));

        // Assert
        verifyAssetProperties("actualAsset", testName, altId, encryptionOption, assetState, actualAsset);
    }

    @Test
    public void createAssetMeanString() throws Exception {
        // Arrange
        String meanString = "'\"(?++\\+&==/&?''$@://   +ne <some><XML></stuff>"
                + "{\"jsonLike\":\"Created\":\"\\/Date(1336368841597)\\/\",\"Name\":null,cksum value\"}}"
                + "Some unicode: \uB2E4\uB974\uB2E4\uB294\u0625 \u064A\u062F\u064A\u0648\u0009\r\n";

        String testName = testAssetPrefix + "createAssetMeanString" + meanString;

        // Act
        AssetInfo actualAsset = service.create(Asset.create().setName(testName));

        // Assert
        assertEquals("actualAsset Name", testName, actualAsset.getName());
    }

    @Test
    public void createAssetNullNameSuccess() throws Exception {
        // Arrange

        // Act
        AssetInfo actualAsset = null;
        try {
            actualAsset = service.create(Asset.create());
            // Assert
            verifyAssetProperties("actualAsset", "", "", EncryptionOption.None, AssetState.Initialized, actualAsset);
        }
        finally {
            // Clean up the anonymous asset now while we have the id, because we
            // do not want to delete all anonymous assets in the bulk-cleanup code.
            try {
                if (actualAsset != null) {
                    service.delete(Asset.delete(actualAsset.getId()));
                }
            }
            catch (ServiceException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Test
    public void getAssetSuccess() throws Exception {
        // Arrange
        String testName = testAssetPrefix + "GetAssetSuccess";
        String altId = "altId";
        EncryptionOption encryptionOption = EncryptionOption.StorageEncrypted;
        AssetState assetState = AssetState.Published;

        AssetInfo assetInfo = service.create(Asset.create().setName(testName).setAlternateId(altId)
                .setOptions(encryptionOption).setState(assetState));

        // Act
        AssetInfo actualAsset = service.get(Asset.get(assetInfo.getId()));

        // Assert
        verifyInfosEqual("actualAsset", assetInfo, actualAsset);
    }

    @Test
    public void getAssetInvalidId() throws ServiceException {
        expectedException.expect(ServiceException.class);
        expectedException.expect(new ServiceExceptionMatcher(400));
        service.get(Asset.get(invalidId));
    }

    @Test
    public void getAssetNonexistId() throws ServiceException {
        expectedException.expect(ServiceException.class);
        expectedException.expect(new ServiceExceptionMatcher(404));
        service.get(Asset.get(validButNonexistAssetId));
    }

    @Test
    public void listAssetSuccess() throws ServiceException {
        // Arrange
        String altId = "altId";
        EncryptionOption encryptionOption = EncryptionOption.StorageEncrypted;
        AssetState assetState = AssetState.Published;

        String[] assetNames = new String[] { testAssetPrefix + "assetA", testAssetPrefix + "assetB" };
        List<AssetInfo> expectedAssets = new ArrayList<AssetInfo>();
        for (int i = 0; i < assetNames.length; i++) {
            String name = assetNames[i];
            expectedAssets.add(service.create(Asset.create().setName(name).setAlternateId(altId)
                    .setOptions(encryptionOption).setState(assetState)));
        }

        // Act
        Collection<AssetInfo> listAssetResult = service.list(Asset.list());

        // Assert

        verifyListResultContains("listAssets", expectedAssets, listAssetResult, new ComponentDelegate() {
            @Override
            public void verifyEquals(String message, Object expected, Object actual) {
                verifyInfosEqual(message, (AssetInfo) expected, (AssetInfo) actual);
            }
        });
    }

    @Test
    public void canListAssetsWithOptions() throws ServiceException {
        String[] assetNames = new String[] { testAssetPrefix + "assetListOptionsA",
                testAssetPrefix + "assetListOptionsB", testAssetPrefix + "assetListOptionsC",
                testAssetPrefix + "assetListOptionsD" };
        List<AssetInfo> expectedAssets = new ArrayList<AssetInfo>();
        for (int i = 0; i < assetNames.length; i++) {
            String name = assetNames[i];
            expectedAssets.add(service.create(Asset.create().setName(name)));
        }

        MultivaluedMapImpl options = new MultivaluedMapImpl();
        options.add("$top", "2");
        Collection<AssetInfo> listAssetResult = service.list(Asset.list(options));

        // Assert

        assertEquals(2, listAssetResult.size());
    }

    @Test
    public void updateAssetSuccess() throws Exception {
        // Arrange
        String originalTestName = testAssetPrefix + "updateAssetSuccessOriginal";
        EncryptionOption originalEncryptionOption = EncryptionOption.StorageEncrypted;
        AssetState originalAssetState = AssetState.Initialized;
        AssetInfo originalAsset = service.create(Asset.create().setName(originalTestName).setAlternateId("altId")
                .setOptions(originalEncryptionOption));

        String updatedTestName = testAssetPrefix + "updateAssetSuccessUpdated";
        String altId = "otherAltId";

        // Act
        service.update(Asset.update(originalAsset.getId()).setName(updatedTestName).setAlternateId(altId));
        AssetInfo updatedAsset = service.get(Asset.get(originalAsset.getId()));

        // Assert
        verifyAssetProperties("updatedAsset", updatedTestName, altId, originalEncryptionOption, originalAssetState,
                updatedAsset);
    }

    @Test
    public void updateAssetNoChangesSuccess() throws Exception {
        // Arrange
        String originalTestName = testAssetPrefix + "updateAssetNoChangesSuccess";
        String altId = "altId";
        AssetInfo originalAsset = service.create(Asset.create().setName(originalTestName).setAlternateId(altId));

        // Act
        service.update(Asset.update(originalAsset.getId()));
        AssetInfo updatedAsset = service.get(Asset.get(originalAsset.getId()));

        // Assert
        verifyInfosEqual("updatedAsset", originalAsset, updatedAsset);
    }

    @Test
    public void updateAssetFailedWithInvalidId() throws ServiceException {
        expectedException.expect(ServiceException.class);
        expectedException.expect(new ServiceExceptionMatcher(404));
        service.update(Asset.update(validButNonexistAssetId));
    }

    @Test
    public void deleteAssetSuccess() throws Exception {
        // Arrange
        String assetName = testAssetPrefix + "deleteAssetSuccess";
        AssetInfo assetInfo = service.create(Asset.create().setName(assetName));
        List<AssetInfo> listAssetsResult = service.list(Asset.list());
        int assetCountBaseline = listAssetsResult.size();

        // Act
        service.delete(Asset.delete(assetInfo.getId()));

        // Assert
        listAssetsResult = service.list(Asset.list());
        assertEquals("listAssetsResult.size", assetCountBaseline - 1, listAssetsResult.size());

        expectedException.expect(ServiceException.class);
        expectedException.expect(new ServiceExceptionMatcher(404));
        service.get(Asset.get(assetInfo.getId()));
    }

    @Test
    public void deleteAssetFailedWithInvalidId() throws ServiceException {
        expectedException.expect(ServiceException.class);
        expectedException.expect(new ServiceExceptionMatcher(404));
        service.delete(Asset.delete(validButNonexistAssetId));
    }

    // @Ignore("due to issue 507")
    @Test
    public void linkAssetContentKeySuccess() throws ServiceException, URISyntaxException {
        // Arrange
        String originalTestName = testAssetPrefix + "linkAssetContentKeyInvalidIdFailed";
        AssetInfo assetInfo = service.create(Asset.create().setName(originalTestName)
                .setOptions(EncryptionOption.StorageEncrypted));
        String contentKeyId = String.format("nb:kid:UUID:%s", UUID.randomUUID());
        String encryptedContentKey = "dummyEncryptedContentKey";
        ContentKeyInfo contentKeyInfo = service.create(ContentKey.create(contentKeyId,
                ContentKeyType.StorageEncryption, encryptedContentKey));
        URI serviceUri = service.getRestServiceUri();
        String escapedContentKeyId;
        try {
            escapedContentKeyId = URLEncoder.encode(contentKeyId, "UTF-8");
        }
        catch (UnsupportedEncodingException e) {
            throw new InvalidParameterException(contentKeyId);
        }
        URI contentKeyUri = new URI(String.format("%sContentKeys('%s')", serviceUri, escapedContentKeyId));

        // Act
        service.action(Asset.linkContentKey(assetInfo.getId(), contentKeyUri));

        // Assert

        // List<ContentKeyInfo> contentKeyInfos = service.list(ContentKey.list(assetInfo.getId()));
        // ContentKeyInfo contentKeyInfo = contentKeyInfos.get(0)
        // assertEquals(contentKeyId, contentKeyInfo.getId());

    }

    @Test
    public void linkAssetContentKeyInvalidIdFailed() throws ServiceException, URISyntaxException {
        // Arrange
        String originalTestName = testAssetPrefix + "linkAssetContentKeyInvalidIdFailed";
        URI invalidContentKeyUri = new URI("https://server/api/ContentKeys('nb:kid:UUID:invalidContentKeyId')");

        // Act
        expectedException.expect(ServiceException.class);
        expectedException.expect(new ServiceExceptionMatcher(400));
        service.action(Asset.linkContentKey(validButNonexistAssetId, invalidContentKeyUri));

        // Assert

    }
}
