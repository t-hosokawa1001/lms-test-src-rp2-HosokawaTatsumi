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

/**
 * 結合テスト レポート機能
 * ケース08
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース08 受講生 レポート修正(週報) 正常系")
public class Case08 {

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
	@DisplayName("テスト03 提出済の研修日の「詳細」ボタンを押下しセクション詳細画面に遷移")
	void test03() {

		// 日付が2022年10月2日(日)で提出済みの行の詳細ボタンを取得する
		String sectionDetailXpath = "//tr[contains(., '2022年10月2日(日)') "
				+ "and contains(., '提出済み')]"
				+ "/td/form/input[@value='詳細']";
		WebElement sectionDetailButton = webDriver.findElement(By.xpath(sectionDetailXpath));

		// 存在するかを確認し詳細ボタンを押下
		assertNotNull(sectionDetailButton);
		sectionDetailButton.click();

		pageLoadTimeout(60);
		assertEquals("セクション詳細 | LMS", webDriver.getTitle());
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(4)
	@DisplayName("テスト04 「確認する」ボタンを押下しレポート登録画面に遷移")
	void test04() {

		// 提出済み週報【デモ】を確認するボタンを押下
		scrollBy("200");
		webDriver.findElement(By.cssSelector("input[value='提出済み週報【デモ】を確認する']")).click();

		pageLoadTimeout(60);
		assertEquals("レポート登録 | LMS", webDriver.getTitle());
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 報告内容を修正して「提出する」ボタンを押下しセクション詳細画面に遷移")
	void test05() {

		// 値をクリアするため一度変数化する
		WebElement inputAchievementLevel = webDriver
				.findElement(By.xpath("//div[./label[contains(text(), '目標の達成度')]]//textarea"));
		WebElement inputImpressions = webDriver
				.findElement(By.xpath("//div[./label[contains(text(), '所感')]]//textarea"));

		// 入力欄に値をクリアして入力し提出するボタンを押下
		inputAchievementLevel.clear();
		inputAchievementLevel.sendKeys("10");
		inputImpressions.clear();
		inputImpressions.sendKeys("今週も頑張りました。");
		scrollBy("400");
		webDriver.findElement(By.xpath("//button[contains(text(), '提出する')]")).click();

		pageLoadTimeout(60);
		assertEquals("セクション詳細 | LMS", webDriver.getTitle());
		getEvidence(new Object() {
		});

	}

	@Test
	@Order(6)
	@DisplayName("テスト06 上部メニューの「ようこそ○○さん」リンクからユーザー詳細画面に遷移")
	void test06() {

		// ようこそ受講生ＡＡ１さんリンクを押下
		webDriver.findElement(By.xpath("//a[contains(., 'ようこそ受講生ＡＡ１さん')]")).click();

		pageLoadTimeout(60);
		assertEquals("ユーザー詳細", webDriver.getTitle());
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(7)
	@DisplayName("テスト07 該当レポートの「詳細」ボタンを押下しレポート詳細画面で修正内容が反映される")
	void test07() {

		// 日付が2022年10月2日(日)で週報【デモ】の行の詳細ボタンを取得する
		String sectionDetailXpath = "//tr[contains(., '2022年10月2日(日)') "
				+ "and contains(., '週報【デモ】')]"
				+ "/td/form/input[@value='詳細']";
		WebElement sectionDetailButton = webDriver.findElement(By.xpath(sectionDetailXpath));

		// 存在するかを確認し詳細ボタンを押下
		assertNotNull(sectionDetailButton);
		scrollBy("400");
		sectionDetailButton.click();

		pageLoadTimeout(60);
		assertEquals("レポート詳細 | LMS", webDriver.getTitle());
		// 変更箇所の要素取得用xpath
		String achievementLevelXpath = "//tr[contains(., '目標の達成度')]/td";
		String impressionsXpath = "//tr[contains(., '所感')]/td";
		assertEquals("10", webDriver.findElement(By.xpath(achievementLevelXpath)).getText());
		assertEquals("今週も頑張りました。", webDriver.findElement(By.xpath(impressionsXpath)).getText());
		getEvidence(new Object() {
		});
	}

}
