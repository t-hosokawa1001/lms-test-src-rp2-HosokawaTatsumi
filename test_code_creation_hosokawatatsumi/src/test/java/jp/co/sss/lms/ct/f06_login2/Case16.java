package jp.co.sss.lms.ct.f06_login2;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

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
 * 結合テスト ログイン機能②
 * ケース16
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース16 受講生 初回ログイン 変更パスワード未入力")
public class Case16 {

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
	@DisplayName("テスト02 DBに初期登録された未ログインの受講生ユーザーでログイン")
	void test02() {

		// 未ログインユーザー情報を入力してログインボタンを押下
		webDriver.findElement(By.id("loginId")).sendKeys("StudentAA02");
		webDriver.findElement(By.id("password")).sendKeys("StudentAA02");
		webDriver.findElement(By.cssSelector("input[type='submit']")).click();

		assertEquals("セキュリティ規約 | LMS", webDriver.getTitle());
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(3)
	@DisplayName("テスト03 「同意します」チェックボックスにチェックを入れ「次へ」ボタン押下")
	void test03() {

		// 同意しますにチェックを入れ次へボタンを押下
		webDriver.findElement(By.cssSelector("input[type='checkbox']")).click();
		webDriver.findElement(By.xpath("//button[contains(text(), '次へ')]")).click();

		visibilityTimeout(By.tagName("body"), 60);
		assertEquals("パスワード変更 | LMS", webDriver.getTitle());
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(4)
	@DisplayName("テスト04 パスワードを未入力で「変更」ボタン押下")
	void test04() {

		// 変更ボタンを押下
		webDriver.findElement(By.xpath("//button[contains(text(), '変更')]")).click();
		visibilityTimeout(By.id("upd-btn"), 60);
		webDriver.findElement(By.id("upd-btn")).click();

		visibilityTimeout(By.tagName("body"), 60);
		// エラークラスを持つ要素をすべて取得
		List<WebElement> errors = webDriver.findElements(By.className("error"));
		assertEquals("現在のパスワードは必須です。", errors.get(1).getText());
		// 同じspanに二つのエラー文が入るため個別に検証
		assertTrue(errors.get(2).getText().contains("パスワードは必須です。"));
		assertTrue(errors.get(2).getText().contains(
				"「パスワード」には半角英数字のみ使用可能です。"
						+ "また、半角英大文字、半角英小文字、"
						+ "数字を含めた8～20文字を入力してください。"));
		assertEquals("確認パスワードは必須です。", errors.get(3).getText());
		assertTrue(
				webDriver.findElement(By.id("currentPassword")).getAttribute("class").contains("errorInput"));
		assertTrue(
				webDriver.findElement(By.id("password")).getAttribute("class").contains("errorInput"));
		assertTrue(
				webDriver.findElement(By.id("passwordConfirm")).getAttribute("class").contains("errorInput"));
		scrollBy("30");
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 20文字以上の変更パスワードを入力し「変更」ボタン押下")
	void test05() {

		// 20文字超の入力値
		String inputText = "ABCDEabcde0123456789A";
		// 値を入力し変更ボタンを押下
		webDriver.findElement(By.id("currentPassword")).sendKeys("StudentAA02");
		webDriver.findElement(By.id("password")).sendKeys(inputText);
		webDriver.findElement(By.id("passwordConfirm")).sendKeys(inputText);
		scrollBy("200");
		webDriver.findElement(By.xpath("//button[contains(text(), '変更')]")).click();
		visibilityTimeout(By.id("upd-btn"), 60);
		webDriver.findElement(By.id("upd-btn")).click();

		visibilityTimeout(By.tagName("body"), 60);
		assertEquals("パスワードの長さが最大値(20)を超えています。",
				webDriver.findElement(By.xpath("(//span[contains(@class, 'error')])[2]")).getText());
		assertTrue(
				webDriver.findElement(By.id("password")).getAttribute("class").contains("errorInput"));
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(6)
	@DisplayName("テスト06 ポリシーに合わない変更パスワードを入力し「変更」ボタン押下")
	void test06() {

		// ポリシーに合わない入力値
		String inputText = "studentaa";
		// 値を入力し変更ボタンを押下
		webDriver.findElement(By.id("currentPassword")).sendKeys("StudentAA02");
		webDriver.findElement(By.id("password")).sendKeys(inputText);
		webDriver.findElement(By.id("passwordConfirm")).sendKeys(inputText);
		scrollBy("200");
		webDriver.findElement(By.xpath("//button[contains(text(), '変更')]")).click();
		visibilityTimeout(By.id("upd-btn"), 60);
		webDriver.findElement(By.id("upd-btn")).click();

		visibilityTimeout(By.tagName("body"), 60);
		assertEquals(
				"「パスワード」には半角英数字のみ使用可能です。"
						+ "また、半角英大文字、半角英小文字、"
						+ "数字を含めた8～20文字を入力してください。",
				webDriver.findElement(By.xpath("(//span[contains(@class, 'error')])[2]")).getText());
		assertTrue(
				webDriver.findElement(By.id("password")).getAttribute("class").contains("errorInput"));
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(7)
	@DisplayName("テスト07 一致しない確認パスワードを入力し「変更」ボタン押下")
	void test07() {

		// 値を入力し変更ボタンを押下
		webDriver.findElement(By.id("currentPassword")).sendKeys("StudentAA02");
		webDriver.findElement(By.id("password")).sendKeys("StudentAA002");
		webDriver.findElement(By.id("passwordConfirm")).sendKeys("StudentAA00");
		scrollBy("200");
		webDriver.findElement(By.xpath("//button[contains(text(), '変更')]")).click();
		visibilityTimeout(By.id("upd-btn"), 60);
		webDriver.findElement(By.id("upd-btn")).click();

		visibilityTimeout(By.tagName("body"), 60);
		assertEquals("パスワードと確認パスワードが一致しません。",
				webDriver.findElement(By.xpath("(//span[contains(@class, 'error')])[2]")).getText());
		assertTrue(
				webDriver.findElement(By.id("password")).getAttribute("class").contains("errorInput"));
		getEvidence(new Object() {
		});
	}

}
