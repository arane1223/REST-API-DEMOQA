# Проект по автоматизации API тестирования на DEMOQA
<a href="https://demoqa.com/"><img width="30%" title="DEMOQA" src="images/icons/Toolsqa.jpg"></a>

## :point_up: Структура:

- <a href="#point_up_2-технологии-и-инструменты">Стек</a>
- <a href="#point_up_2-проведенные автотесты">Проведенные автотесты</a>
- <a href="#point_up_2-сборка-в-Jenkins">Сборка в Jenkins</a>
- <a href="#point_up_2-запуск-из-терминала">Запуск из терминала</a>
- <a href="#point_up_2-allure-отчет">Allure отчет</a>
- <a href="#point_up_2-интеграция-с-allure-testops">Интеграция с Allure TestOps</a>
- <a href="#point_up_2-интеграция-с-jira">Интеграция с Jira</a>
- <a href="#point_up_2-отчет-в-telegram">Отчет в Telegram</a>

## :point_up_2: Стек

<p align="center">
<img src="images/icons/Java.svg" width="50" />
<img src="images/icons/Intelij_IDEA.svg" width="50" />
<img src="images/icons/GitHub.svg" width="50" />
<img src="images/icons/JUnit5.svg" width="50" />
<img src="images/icons/Gradle.svg" width="50" />
<img src="images/icons/Jenkins.svg" width="50" />
<img src="images/icons/Allure_Report.svg" width="50" />
<img src="images/icons/AllureTestOps.svg" width="50" />
<img src="images/icons/Telegram.svg" width="50" />
<img src="images/icons/Jira.svg" width="50" />
<img src="images/icons/Rest-assured.png" width="50" />
<img src="images/icons/lombok.png" width="50" />
</p>

## :point_up_2: Проведенные автотесты
- Тесты с данными пользователей на DEMOQA
  - Успешная авторизация и получение токена с корректными данными
  - Неуспешная авторизация с некорректными данными
  - Неуспешная авторизация отсутствующего пользователя в базе
  - Неуспешная авторизация с отправкой пустых полей
  - Неуспешная повторная регистрация уже зарегистрированного пользователя
  - Успешное добавление и удаление нового пользователя

- Тесты с данными книг на DEMOQA
  - Проверка библиотеки книг по названиям
  - Проверка характеристик книги по ISBN
  - Проверка отсутствия книги по ISBN

## :point_up_2: Сборка в Jenkins
[**Сборка в Jenkins**](https://jenkins.autotests.cloud/job/demoqa-api-test/)
<p>
<img title="Jenkins Dashboard" src="images/screenshots/jenkins-project.png">
</p>

### Параметры сборки в Jenkins:
Сборка в Jenkins

- task (выбор групп тестов)
- baseUri (базовый URI, по умолчанию: https://demoqa.com/ )

## :point_up_2: Запуск из терминала
Локальный запуск:
```
gradle clean api
```
Удаленный запуск:
```
clean 
${TASK} 
-DbaseUri=${BASE_URI}
```

## :point_up_2: Allure отчет

[Allure отчет](https://jenkins.autotests.cloud/job/demoqa-api-test/allure/)

- ### Главный экран отчета
<p>
<img title="Allure report" src="images/screenshots/allure-report.png">
</p>

- ### Страница с проведенными тестами
<p>
<img title="Allure report suites" src="images/screenshots/allure-report-suites.png">
</p>

## :point_up_2: Интеграция с Allure TestOps

[Проект в Allure TestOps](https://allure.autotests.cloud/project/4938/dashboards)

- ### Экран с результатами запуска тестов
<p>
<img title="Allure TestOps" src="images/screenshots/allure-tes-tops.png">
</p>

- ### Страница с тестами в TestOps

<p>
<img title="Allure test cases" src="images/screenshots/allure-test-ops-tests.png">
</p>

## :point_up_2: Интеграция с Jira

[Задача в Jira](https://jira.autotests.cloud/browse/HOMEWORK-1505)

- ### Страница с задачей в Jira
<p>
<img title="Jira task description" src="images/screenshots/jira-task.png">
<img title="Jira test-cases" src="images/screenshots/jira-allure-test-case.png">
<img title="Jira test launches" src="images/screenshots/jira-allure-launches.png">
</p>

## :point_up_2: Отчет в Telegram
<p>
<img title="Telegram" src="images/screenshots/Telegram.png">
</p>
