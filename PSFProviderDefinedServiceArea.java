package clientConfig;

import com.aim.automation.helpers.PlatformContextUtils;
import dtos.client.config.ClientConfigJsonDTO;
import dtos.client.config.ClientConfigResponseDTO;
import factories.clientConfig.ClientConfigRequestDTOFactory;
import helpers.constants.BasePathConstants;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import steps.clientConfig.ClientConfigRequestSteps;

import java.util.Arrays;
import java.util.List;

import static helpers.constants.ConfigConstants.*;
import static helpers.constants.Constants.*;


public class PSFProviderDefinedServiceArea extends ClientConfigBaseTest {

    private static final String FILE_NAME = "client-configurations/psf_provider_defined_service_area_nodes.json";

    @BeforeClass
    public void init() {
        basePath = BasePathConstants.BASE_PATH_PROVIDER_CONFIG;
        body = new ClientConfigRequestDTOFactory().createdConfigRequestDTO(PROVIDER_DEFINED_SERVICE_AREA_CONFIG_ID);

    }

    /**
     * @return List of client Id's
     * @requestDTOInput currently has only column in data provider as this test has only one input.
     * if the test has more than one input, then the column size need to be altered accordingly
     * and each input should be extracted and add to dataProvider
     */
    @DataProvider(name = "clientConfigDataProvider")
    private Object[][] clientConfigDataProvider() {
        List<ClientConfigJsonDTO> clientConfigRequestDTO = readInputValuesFromNodesJson(FILE_NAME);

        String[][] requestDTOInput = new String[clientConfigRequestDTO.size()][1];
        for (int i = 0; i < clientConfigRequestDTO.size(); i++) {
            requestDTOInput[i][0] = clientConfigRequestDTO.get(i).getConfigurationNodeDTO().getPlatformContext().getMember().getClientId();
        }
        return requestDTOInput;
    }

    @Test(dataProvider = "clientConfigDataProvider")
    public void whenPostWithValidInputForProviderDefinedServiceAreaConfigTestThenReturnsSC200(String clientId) {

        PlatformContextUtils platformContextUtils = new PlatformContextUtils();
        platformContextHeader = platformContextUtils.changeClientId(platformContextHeader, Integer.valueOf(clientId));

        ClientConfigResponseDTO[] providerDefinedServiceAreaResponseDTO = new ClientConfigRequestSteps(platformContextHeader, headers).searchConfig(PROVIDER_DEFINED_SERVICE_AREA_CONFIG_ID);
        for (ClientConfigResponseDTO clientConfigResponseDTO : providerDefinedServiceAreaResponseDTO) {
            softly.then(clientConfigResponseDTO.getConfigurationId()).isEqualTo(PROVIDER_DEFINED_SERVICE_AREA_CONFIG_ID);
            softly.then(clientConfigResponseDTO.getConfigurationName()).isEqualTo(PROVIDER_DEFINED_SERVICE_AREA_CONFIG_NAME);

            }
        }
        softly.assertAll();
    }
}
