package jp.co.sss.lms.ct.f05_exam;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import java.text.SimpleDateFormat;
import java.util.Date;

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
 * 結合テスト 試験実施機能
 * ケース14
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース13 受講生 試験の実施 結果50点")
public class Case14 {

	/** テスト07およびテスト08 試験実施日時 */
	static Date date;

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
	@DisplayName("テスト03 「試験有」の研修日の「詳細」ボタンを押下しセクション詳細画面に遷移")
	void test03() {

		// 試験有の行の詳細ボタンを取得する
		String sectionDetailXpath = "//tr[contains(., '試験有')]"
				+ "/td/form/input[@value='詳細']";
		WebElement sectionDetailButton = webDriver.findElement(By.xpath(sectionDetailXpath));

		// 存在するかを確認し詳細ボタンを押下
		assertNotNull(sectionDetailButton);
		sectionDetailButton.click();

		visibilityTimeout(By.tagName("body"), 60);
		assertEquals("セクション詳細 | LMS", webDriver.getTitle());
		assertTrue(webDriver.findElement(By.xpath("//h3[contains(text(), '本日の試験')]")).isDisplayed());
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(4)
	@DisplayName("テスト04 「本日の試験」エリアの「詳細」ボタンを押下し試験開始画面に遷移")
	void test04() {

		// 本日の試験の詳細ボタンを押下
		String testDetailXpath = "//tbody[contains(., '試験')]"
				+ "/tr/td/form/input[@value='詳細']";
		webDriver.findElement(By.xpath(testDetailXpath)).click();

		visibilityTimeout(By.tagName("body"), 60);
		assertEquals("試験【ITリテラシー①】 | LMS", webDriver.getTitle());
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 「試験を開始する」ボタンを押下し試験問題画面に遷移")
	void test05() {

		// 試験を開始するボタンを押下
		webDriver.findElement(By.cssSelector("input[value='試験を開始する']")).click();

		visibilityTimeout(By.tagName("body"), 60);
		assertEquals("ITリテラシー① | LMS", webDriver.getTitle());
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(6)
	@DisplayName("テスト06 正答と誤答が半々で「確認画面へ進む」ボタンを押下し試験回答確認画面に遷移")
	void test06() {

		// 正答を6問誤答を6問作成
		webDriver.findElement(By.id("answer-0-2")).click();
		scrollBy("370");
		webDriver.findElement(By.id("answer-1-2")).click();
		scrollBy("370");
		webDriver.findElement(By.id("answer-2-0")).click();
		scrollBy("370");
		webDriver.findElement(By.id("answer-3-0")).click();
		scrollBy("370");
		webDriver.findElement(By.id("answer-4-1")).click();
		scrollBy("370");
		webDriver.findElement(By.id("answer-5-1")).click();
		scrollBy("370");
		webDriver.findElement(By.id("answer-6-1")).click();
		scrollBy("370");
		webDriver.findElement(By.id("answer-7-1")).click();
		scrollBy("370");
		webDriver.findElement(By.id("answer-8-3")).click();
		scrollBy("370");
		webDriver.findElement(By.id("answer-9-3")).click();
		scrollBy("370");
		webDriver.findElement(By.id("answer-10-2")).click();
		scrollBy("370");
		webDriver.findElement(By.id("answer-11-0")).click();
		// 確認画面へ進むボタンを押下
		scrollBy("370");
		webDriver.findElement(By.cssSelector("input[value='確認画面へ進む']")).click();

		visibilityTimeout(By.tagName("body"), 60);
		assertEquals("ITリテラシー① | LMS", webDriver.getTitle());
		assertEquals("回答数：12／12問 ", webDriver.findElement(By.xpath("//small[contains(text(), '回答数')]")).getText());
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(7)
	@DisplayName("テスト07 「回答を送信する」ボタンを押下し試験結果画面に遷移")
	void test07() throws InterruptedException {

		// データ保護のため5秒間待機
		Thread.sleep(5000);
		// 回答を送信するボタンを押下
		scrollBy("4500");
		webDriver.findElement(By.id("sendButton")).click();
		date = new Date();

		visibilityTimeout(By.tagName("body"), 60);
		assertEquals("ITリテラシー① | LMS", webDriver.getTitle());
		assertEquals("あなたのスコア：50.0点   正答数：6／12",
				webDriver.findElement(By.xpath("//small[contains(text(), 'あなたのスコア')]")).getText());
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(8)
	@DisplayName("テスト08 「戻る」ボタンを押下し試験開始画面に遷移後当該試験の結果が反映される")
	void test08() {

		// 試験実施日時
		String testDate = new SimpleDateFormat("yyyy年MM月dd日 HH時mm分ss秒").format(date);
		// 戻るボタンを押下
		scrollBy("5000");
		webDriver.findElement(By.cssSelector("input[value='戻る']")).click();

		visibilityTimeout(By.tagName("body"), 60);
		assertEquals("試験【ITリテラシー①】 | LMS", webDriver.getTitle());
		assertEquals("50.0点", webDriver.findElement(By.xpath("//tr[contains(., '" + testDate + "')]/td[2]")).getText());
		scrollBy("1000");
		getEvidence(new Object() {
		});
	}

}
