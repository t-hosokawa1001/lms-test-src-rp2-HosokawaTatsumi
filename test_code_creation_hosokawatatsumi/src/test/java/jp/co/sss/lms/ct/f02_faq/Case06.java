package jp.co.sss.lms.ct.f02_faq;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.By;

/**
 * 結合テスト よくある質問機能
 * ケース06
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース06 カテゴリ検索 正常系")
public class Case06 {

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
	@DisplayName("テスト05 カテゴリ検索で該当カテゴリの検索結果だけ表示")
	void test05() {
		// チェック項目の質問を含む要素を取得するxpath
		String categoryCheckXpath1 = "//span[contains(text(), 'キャンセル料・途中退校について')]";
		String categoryCheckXpath2 = "//span[contains(text(), '研修の申し込みはどのようにすれば良いですか？')]";

		// 【研修関係】リンクを押下
		webDriver.findElement(By.linkText("【研修関係】")).click();

		// ロードに時間がかかる場合があるため完了するまで待機
		pageLoadTimeout(60);
		assertEquals("よくある質問 | LMS", webDriver.getTitle());

		// チェック項目のテキストが全て存在することを確認
		assertNotNull(webDriver.findElement(By.xpath(categoryCheckXpath1)));
		assertNotNull(webDriver.findElement(By.xpath(categoryCheckXpath2)));
		// エビデンス取得の為下部にスクロール
		scrollBy("200");
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(6)
	@DisplayName("テスト06 検索結果の質問をクリックしその回答を表示")
	void test06() {
		// チェック項目の質問を含む要素を取得するxpath
		String categoryCheckXpath1 = "//span[contains(text(), 'キャンセル料・途中退校について')]";
		// チェック項目の質問に対応した回答を含む要素を取得するxpath
		String categoryCheckAnswerXpath = "//span[contains(text(), "
				+ "'受講者の退職や解雇等、やむを得ない事情による途中終了に関してなど、"
				+ "事情をお伺いした上で、協議という形を取らせて頂きます。\n"
				+ "弊社営業担当までご相談下さい。')]";

		// チェック項目の質問を押下
		webDriver.findElement(By.xpath(categoryCheckXpath1)).click();

		// チェック項目の質問に対応した回答が表示されたか確認
		assertTrue(webDriver.findElement(By.xpath(categoryCheckAnswerXpath)).isDisplayed());
		// エビデンス取得の為下部にスクロール
		scrollBy("200");
		getEvidence(new Object() {
		});
	}

}
