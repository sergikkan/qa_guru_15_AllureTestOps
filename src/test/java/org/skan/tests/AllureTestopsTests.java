package org.skan.tests;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.ElementsCollection;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Cookie;
import org.skan.api.AuthorizationApi;
import org.skan.api.TestCaseApi;
import org.skan.models.Step;
import org.skan.models.StepsCaseBody;

import java.util.List;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static io.qameta.allure.Allure.step;
import static org.skan.api.AuthorizationApi.ALLURE_TESTOPS_SESSION;
import static org.skan.api.TestCaseApi.createTestStep;
import static org.skan.specs.LoginSpecs.*;

public class AllureTestopsTests extends BaseTest {

    private void setCookies() {
        String authorizationCookie = new AuthorizationApi()
                .getAuthorizationCookie(TOKEN, USERNAME, PASSWORD);

        open("/favicon.ico");
        getWebDriver().manage().addCookie(new Cookie(ALLURE_TESTOPS_SESSION, authorizationCookie));
    }


    @Test
    @DisplayName("Создание тест-кейса с добавлением шагов")
    void createTestCaseAndAddSteps() {
        StepsCaseBody stepsCaseBody = new StepsCaseBody();
        Step stepOne = Step.generateRandomStep();
        Step stepTwo = Step.generateRandomStep();
        stepsCaseBody.setSteps(List.of(stepOne, stepTwo));

        int id = step("Создать тест-кейс",
                TestCaseApi::createTestCase);

        step("Создать шаги",
                () -> createTestStep(id, stepsCaseBody));

        step("Проверить что у шагов правильные keyword и name", () -> {
                    setCookies();
                    List<Step> list = stepsCaseBody.getSteps();
                    open("/project/1717/test-cases/" + id);
                    ElementsCollection collection = $$x(".//ul[@class='TreeElement']/li")
                            .shouldHave(CollectionCondition.size(list.size()));

                    for (int i = 0; i < collection.size(); i++) {
                        collection.get(i).$x(".//div[@class='StepKeyword']").shouldHave(text(list.get(i).getKeyword()));
                        collection.get(i).$x(".//pre[@class='Multiline']").shouldHave(text(list.get(i).getName()));
                    }
                }
        );
    }

}
