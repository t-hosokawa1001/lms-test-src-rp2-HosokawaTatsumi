package jp.co.sss.lms.ct.f03_report;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

/**
 * 結合テスト レポート機能
 * ケース09
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース09 受講生 レポート登録 入力チェック")
public class Case09 {

	/** 前処理 */
	@BeforeAll
	static void before() {
		createDriver();
	}

	/** 後処理 */
	@AfterAll
	static void after() {
		closeDriver();
	}

	@Test
	@Order(1)
	@DisplayName("テスト01 トップページURLでアクセス")
	void test01() {

		goTo("http://localhost:8080/lms/");
		assertEquals("ログイン | LMS", webDriver.getTitle());
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(2)
	@DisplayName("テスト02 初回ログイン済みの受講生ユーザーでログイン")
	void test02() {

		// 初回ログイン済みのユーザー情報を入力してログインボタンを押下
		webDriver.findElement(By.id("loginId")).sendKeys("StudentAA01");
		webDriver.findElement(By.id("password")).sendKeys("StudentAA01");
		webDriver.findElement(By.cssSelector("input[type='submit']")).click();

		assertEquals("コース詳細 | LMS", webDriver.getTitle());
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(3)
	@DisplayName("テスト03 上部メニューの「ようこそ○○さん」リンクからユーザー詳細画面に遷移")
	void test03() {

		// ようこそ受講生ＡＡ１さんリンクを押下
		webDriver.findElement(By.xpath("//a[contains(., 'ようこそ受講生ＡＡ１さん')]")).click();

		pageLoadTimeout(60);
		assertEquals("ユーザー詳細", webDriver.getTitle());
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(4)
	@DisplayName("テスト04 該当レポートの「修正する」ボタンを押下しレポート登録画面に遷移")
	void test04() {

		// 日付が2022年10月2日(日)で週報【デモ】の行の修正するボタンを取得する
		String sectionDetailXpath = "//tr[contains(., '2022年10月2日(日)') "
				+ "and contains(., '週報【デモ】')]"
				+ "/td/form/input[@value='修正する']";
		WebElement sectionDetailButton = webDriver.findElement(By.xpath(sectionDetailXpath));

		// 存在するかを確認し詳細ボタンを押下
		assertNotNull(sectionDetailButton);
		scrollBy("400");
		sectionDetailButton.click();

		pageLoadTimeout(60);
		assertEquals("レポート登録 | LMS", webDriver.getTitle());
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 報告内容を修正して「提出する」ボタンを押下しエラー表示：学習項目が未入力")
	void test05() {

		// 値をクリアするため一度変数化する
		String inputLearningItemsXpath = "//div[./label[contains(text(), '学習項目')]]//input";
		WebElement inputLearningItems = webDriver
				.findElement(By.xpath(inputLearningItemsXpath));

		// 入力欄に値をクリアして入力し提出するボタンを押下
		inputLearningItems.clear();
		inputLearningItems.sendKeys("");
		scrollBy("400");
		webDriver.findElement(By.xpath("//button[contains(text(), '提出する')]")).click();

		pageLoadTimeout(60);
		assertEquals("レポート登録 | LMS", webDriver.getTitle());
		assertTrue(
				webDriver.findElement(By.xpath(inputLearningItemsXpath)).getAttribute("class").contains("errorInput"));
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(6)
	@DisplayName("テスト06 不適切な内容で修正して「提出する」ボタンを押下しエラー表示：理解度が未入力")
	void test06() {

		// 値をクリアするため一度変数化する
		String inputLearningItemsXpath = "//div[./label[contains(text(), '学習項目')]]//input";
		String inputComprehensionLevelXpath = "//div[./label[contains(text(), '理解度')]]//select";
		WebElement inputLearningItems = webDriver
				.findElement(By.xpath(inputLearningItemsXpath));

		// 入力欄に値をクリアして入力し提出するボタンを押下
		inputLearningItems.clear();
		inputLearningItems.sendKeys("ITリテラシー①");
		new Select(webDriver.findElement(By.xpath(inputComprehensionLevelXpath))).selectByIndex(0);
		scrollBy("400");
		webDriver.findElement(By.xpath("//button[contains(text(), '提出する')]")).click();

		pageLoadTimeout(60);
		assertEquals("レポート登録 | LMS", webDriver.getTitle());
		assertTrue(
				webDriver.findElement(By.xpath(inputComprehensionLevelXpath)).getAttribute("class")
						.contains("errorInput"));
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(7)
	@DisplayName("テスト07 不適切な内容で修正して「提出する」ボタンを押下しエラー表示：目標の達成度が数値以外")
	void test07() {

		// 値をクリアするため一度変数化する
		String inputComprehensionLevelXpath = "//div[./label[contains(text(), '理解度')]]//select";
		String inputAchievementLevelXpath = "//div[./label[contains(text(), '目標の達成度')]]//textarea";
		WebElement inputAchievementLevel = webDriver
				.findElement(By.xpath(inputAchievementLevelXpath));

		// 入力欄に値をクリアして入力し提出するボタンを押下
		new Select(webDriver.findElement(By.xpath(inputComprehensionLevelXpath))).selectByIndex(2);
		inputAchievementLevel.clear();
		inputAchievementLevel.sendKeys("あ");
		scrollBy("400");
		webDriver.findElement(By.xpath("//button[contains(text(), '提出する')]")).click();

		pageLoadTimeout(60);
		assertEquals("レポート登録 | LMS", webDriver.getTitle());
		assertTrue(
				webDriver.findElement(By.xpath(inputAchievementLevelXpath)).getAttribute("class")
						.contains("errorInput"));
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(8)
	@DisplayName("テスト08 不適切な内容で修正して「提出する」ボタンを押下しエラー表示：目標の達成度が範囲外")
	void test08() {

		// 値をクリアするため一度変数化する
		String inputAchievementLevelXpath = "//div[./label[contains(text(), '目標の達成度')]]//textarea";
		WebElement inputAchievementLevel = webDriver
				.findElement(By.xpath(inputAchievementLevelXpath));

		// 入力欄に値をクリアして入力し提出するボタンを押下
		inputAchievementLevel.clear();
		inputAchievementLevel.sendKeys("11");
		scrollBy("400");
		webDriver.findElement(By.xpath("//button[contains(text(), '提出する')]")).click();

		pageLoadTimeout(60);
		assertEquals("レポート登録 | LMS", webDriver.getTitle());
		assertTrue(
				webDriver.findElement(By.xpath(inputAchievementLevelXpath)).getAttribute("class")
						.contains("errorInput"));
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(9)
	@DisplayName("テスト09 不適切な内容で修正して「提出する」ボタンを押下しエラー表示：目標の達成度・所感が未入力")
	void test09() {

		// 値をクリアするため一度変数化する
		String inputAchievementLevelXpath = "//div[./label[contains(text(), '目標の達成度')]]//textarea";
		String inputImpressionsXpath = "//div[./label[contains(text(), '所感')]]//textarea";
		WebElement inputAchievementLevel = webDriver
				.findElement(By.xpath(inputAchievementLevelXpath));
		WebElement inputImpressions = webDriver
				.findElement(By.xpath(inputImpressionsXpath));

		// 入力欄に値をクリアして入力し提出するボタンを押下
		inputAchievementLevel.clear();
		inputAchievementLevel.sendKeys("");
		inputImpressions.clear();
		inputImpressions.sendKeys("");
		scrollBy("400");
		webDriver.findElement(By.xpath("//button[contains(text(), '提出する')]")).click();

		pageLoadTimeout(60);
		assertEquals("レポート登録 | LMS", webDriver.getTitle());
		assertTrue(
				webDriver.findElement(By.xpath(inputAchievementLevelXpath)).getAttribute("class")
						.contains("errorInput"));
		assertTrue(
				webDriver.findElement(By.xpath(inputImpressionsXpath)).getAttribute("class")
						.contains("errorInput"));
		scrollBy("250");
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(10)
	@DisplayName("テスト10 不適切な内容で修正して「提出する」ボタンを押下しエラー表示：所感・一週間の振り返りが2000文字超")
	void test10() {

		// 2000文字超の入力値を作成
		StringBuilder inputText = new StringBuilder();
		for (int i = 0; i < 50; i++) {
			inputText.append(
					"あいうえおかきくけこさしすせそたちつてと"
							+ "なにぬねのはひふへほまみむめもらりるれろ");
		}
		inputText.append("あ");

		// 値をクリアするため一度変数化する
		String inputAchievementLevelXpath = "//div[./label[contains(text(), '目標の達成度')]]//textarea";
		String inputImpressionsXpath = "//div[./label[contains(text(), '所感')]]//textarea";
		String inputALookBackAtTheWeekXpath = "//div[./label[contains(text(), '一週間の振り返り')]]//textarea";
		WebElement inputAchievementLevel = webDriver
				.findElement(By.xpath(inputAchievementLevelXpath));
		WebElement inputImpressions = webDriver
				.findElement(By.xpath(inputImpressionsXpath));
		WebElement inputALookBackAtTheWeek = webDriver
				.findElement(By.xpath(inputALookBackAtTheWeekXpath));

		// 入力欄に値をクリアして入力し提出するボタンを押下
		inputAchievementLevel.clear();
		inputAchievementLevel.sendKeys("10");
		inputImpressions.clear();
		inputImpressions.sendKeys(inputText);
		inputALookBackAtTheWeek.clear();
		inputALookBackAtTheWeek.sendKeys(inputText);
		scrollBy("400");
		webDriver.findElement(By.xpath("//button[contains(text(), '提出する')]")).click();

		pageLoadTimeout(60);
		assertEquals("レポート登録 | LMS", webDriver.getTitle());
		assertTrue(
				webDriver.findElement(By.xpath(inputImpressionsXpath)).getAttribute("class")
						.contains("errorInput"));
		assertTrue(
				webDriver.findElement(By.xpath(inputALookBackAtTheWeekXpath)).getAttribute("class")
						.contains("errorInput"));
		scrollBy("250");
		getEvidence(new Object() {
		});
	}

}
