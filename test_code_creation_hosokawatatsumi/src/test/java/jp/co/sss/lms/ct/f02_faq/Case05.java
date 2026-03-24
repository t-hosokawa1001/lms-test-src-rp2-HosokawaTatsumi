package jp.co.sss.lms.ct.f02_faq;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Set;

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
 * 結合テスト よくある質問機能
 * ケース05
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース05 キーワード検索 正常系")
public class Case05 {

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
	@DisplayName("テスト03 上部メニューの「ヘルプ」リンクからヘルプ画面に遷移")
	void test03() {
		// 機能メニューを開いてヘルプリンクを押下
		webDriver.findElement(By.linkText("機能")).click();
		webDriver.findElement(By.linkText("ヘルプ")).click();

		assertEquals("ヘルプ | LMS", webDriver.getTitle());
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(4)
	@DisplayName("テスト04 「よくある質問」リンクからよくある質問画面を別タブに開く")
	void test04() {
		// リンク押下前に開かれているタブ
		Set<String> oldWindowHandles = webDriver.getWindowHandles();
		// よくある質問リンクを押下
		webDriver.findElement(By.linkText("よくある質問")).click();

		// リンク押下後に開かれているタブ
		Set<String> newWindowHandles = webDriver.getWindowHandles();

		// タブが新しく開かれているかを確認し、新しく開かれたタブに遷移
		assertEquals(oldWindowHandles.size() + 1, newWindowHandles.size());
		newWindowHandles.removeAll(oldWindowHandles);
		for (String windowHandle : newWindowHandles) {
			webDriver.switchTo().window(windowHandle);
		}
		assertEquals("よくある質問 | LMS", webDriver.getTitle());
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 キーワード検索で該当キーワードを含む検索結果だけ表示")
	void test05() {
		// 検索用キーワード
		String searchWord = "申し込み";
		// 検索結果のキーワードを含まない要素を取得するxpath
		String searchCheckXpath = "//dl/dt/span["
				+ "not(contains(@class, 'text-primary')) and "
				+ "not(contains(text(), '" + searchWord + "'))]";

		// 入力欄にキーワードを入力し検索ボタンを押下
		webDriver.findElement(By.cssSelector("input[type='text']")).sendKeys(searchWord);
		webDriver.findElement(By.cssSelector("input[type='submit']")).click();

		// ロードに時間がかかる場合があるため完了するまで待機
		pageLoadTimeout(60);
		assertEquals("よくある質問 | LMS", webDriver.getTitle());

		// 検索結果にキーワードを含まない要素が無いことを確認
		List<WebElement> elements = webDriver.findElements(By.xpath(searchCheckXpath));
		assertTrue(elements.isEmpty());
		// エビデンス取得の為下部にスクロール
		scrollBy("200");
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(6)
	@DisplayName("テスト06 「クリア」ボタン押下で入力したキーワードを消去")
	void test06() {
		// クリアボタンを押下し入力欄が空文字になっていることを確認
		webDriver.findElement(By.cssSelector("input[type='button']")).click();
		assertEquals("", webDriver.findElement(By.cssSelector("input[type='text']")).getCssValue("value"));
		// エビデンス取得の為上部にスクロール
		scrollBy("-200");
		getEvidence(new Object() {
		});
	}

}
