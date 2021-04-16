package clientConfig;


import com.aim.automation.tests.base.BaseTest;
import dtos.client.config.ClientConfigJsonDTO;
import dtos.client.config.ClientConfigTestCaseDTO;
import helpers.JsonFileLoader;
import helpers.clientConfig.ClientConfigHelper;
import steps.clientConfig.ClientConfigRequestSteps;

import java.util.ArrayList;
import java.util.List;

import static helpers.constants.ConfigConstants.END_DATE_9999;

public class ClientConfigBaseTest extends BaseTest {

    protected ClientConfigHelper clientConfigTestsHelper;
    private final ClientConfigRequestSteps clientConfigRequestSteps;
    private final JsonFileLoader jsonFileLoader;


    public ClientConfigBaseTest() {
        clientConfigTestsHelper = new ClientConfigHelper();
        clientConfigRequestSteps = new ClientConfigRequestSteps();
        jsonFileLoader = new JsonFileLoader();
    }


    /**
     * Utility method used in a {@linkDataProvider} for loading test
     * cases from pre-defined json with test cases for a given client.
     *
     * @param fileName json file name containing test cases (e.g. client183.json)
     * @return two-dimensional array of {@link ClientConfigTestCaseDTO} containing the test cases for a given client.
     */
    protected ClientConfigTestCaseDTO[][] loadTestCaseDTOsFromJson(String fileName) {
        ClientConfigTestCaseDTO[] testCases = jsonFileLoader.readDtoFromFile(fileName, ClientConfigTestCaseDTO[].class);
        ClientConfigTestCaseDTO[][] dtos = new ClientConfigTestCaseDTO[testCases.length][1];

        for (int i = 0; i < testCases.length; i++) {
            dtos[i][0] = testCases[i];
        }

        return dtos;
    }

    /**
     * Utility method used in a DataProvider to generate dynamic data provider with inputs, basically for easy identification of test names
     *
     * @param fileName json file name, basically from DEV mongo scripts
     * @return list of list of active nodes/inputs available in JSON/CSV
     */
    public List<ClientConfigJsonDTO> readInputValuesFromNodesJson(String fileName) {

        ClientConfigJsonDTO[] clientConfigRequestDTO = jsonFileLoader.readDtoFromFile(fileName, ClientConfigJsonDTO[].class);
        List<ClientConfigJsonDTO> listClientConfigRequestDTO = new ArrayList<>();
        for (ClientConfigJsonDTO clientConfigJsonDTO : clientConfigRequestDTO) {
            if (clientConfigJsonDTO.getEndDate().equals(END_DATE_9999)) {
                listClientConfigRequestDTO.add(clientConfigJsonDTO);
            }
        }
        return listClientConfigRequestDTO;
    }

}
