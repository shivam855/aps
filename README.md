# Selenium Automation Framework
### Java + Maven + TestNG + Apache POI + Selenium

---

## Project Structure

```
automation-framework/
├── pom.xml                          ← Maven dependencies
├── testng.xml                       ← TestNG suite runner
├── src/
│   ├── main/java/com/automation/
│   │   ├── base/
│   │   │   ├── BaseTest.java        ← @BeforeMethod / @AfterMethod
│   │   │   └── DriverManager.java   ← WebDriver init/quit
│   │   ├── config/
│   │   │   └── ConfigReader.java    ← Reads config.properties
│   │   ├── pages/
│   │   │   ├── BasePage.java        ← Common page actions
│   │   │   └── LoginPage.java       ← Example page object
│   │   └── utils/
│   │       ├── ExcelUtil.java       ← Apache POI Excel reader
│   │       └── ScreenshotUtil.java  ← Screenshot on failure
│   ├── main/resources/
│   │   ├── config.properties        ← Browser, URL, timeouts
│   │   └── log4j2.xml              ← Logging config
│   └── test/java/com/automation/
│       └── tests/
│           └── LoginTest.java       ← Sample test class
└── src/test/resources/
    └── TestData.xlsx                ← Excel test data (create manually)
```

---

## Eclipse IDE Setup (Step by Step)

### Step 1: Install Eclipse
Download **Eclipse IDE for Java Developers** from https://www.eclipse.org/downloads/

### Step 2: Install TestNG Plugin
1. `Help` → `Eclipse Marketplace`
2. Search **TestNG**
3. Install `TestNG for Eclipse`
4. Restart Eclipse

### Step 3: Import Project into Eclipse
1. `File` → `Import` → `Maven` → `Existing Maven Projects`
2. Browse to the `automation-framework` folder
3. Click **Finish**
4. Wait for Maven to download all dependencies (check Progress tab)

### Step 4: Create TestData.xlsx
Create an Excel file at `src/test/resources/TestData.xlsx`
with a sheet named **LoginData** and these columns:

| Username            | Password  | ExpectedResult |
|---------------------|-----------|----------------|
| admin@example.com   | Admin@123 | Pass           |
| wrong@example.com   | wrongpass | Fail           |

### Step 5: Update config.properties
Edit `src/main/resources/config.properties`:
- Set `base.url` to your application URL
- Set `browser` to `chrome`, `firefox`, or `edge`

### Step 6: Run Tests

**Option A — Run via Eclipse:**
- Right-click `testng.xml` → `Run As` → `TestNG Suite`

**Option B — Run via Maven:**
```bash
mvn test
```

**Option C — Run a single test class:**
- Right-click `LoginTest.java` → `Run As` → `TestNG Test`

---

## How to Add a New Page Object

```java
// 1. Create a new class extending BasePage
public class HomePage extends BasePage {

    @FindBy(id = "logout-btn")
    private WebElement logoutButton;

    public void clickLogout() {
        click(logoutButton);
    }
}
```

## How to Add a New Test

```java
// 2. Create a new test class extending BaseTest
public class HomeTest extends BaseTest {

    @Test
    public void testLogout() {
        new LoginPage().login("user", "pass");
        new HomePage().clickLogout();
        Assert.assertTrue(getDriver().getCurrentUrl().contains("login"));
    }
}
```

---

## Week 1 Deliverables Checklist
- [x] Maven project created with POM (pom.xml)
- [x] Selenium + TestNG + WebDriverManager configured
- [x] BaseTest with @BeforeMethod / @AfterMethod
- [x] DriverManager with ThreadLocal (parallel-safe)
- [x] ConfigReader for config.properties
- [x] BasePage with reusable Selenium actions
- [x] ExcelUtil with Apache POI for test data
- [x] Screenshot utility on test failure
- [x] Sample LoginPage and LoginTest
- [x] TestNG XML suite file
- [x] Log4j2 logging setup
