package jp.co.sss.lms.ct.f04_attendance;

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

/**
 * 結合テスト 勤怠管理機能
 * ケース10
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース10 受講生 勤怠登録 正常系")
public class Case10 {

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
	@DisplayName("テスト03 上部メニューの「勤怠」リンクから勤怠管理画面に遷移")
	void test03() {

		// 勤怠リンクを押下
		webDriver.findElement(By.xpath("//a[contains(., '勤怠')]")).click();

		pageLoadTimeout(60);
		assertEquals("勤怠情報変更｜LMS", webDriver.getTitle());
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(4)
	@DisplayName("テスト04 「出勤」ボタンを押下し出勤時間を登録")
	void test04() {

		// 出勤ボタンを押下
		webDriver.findElement(By.cssSelector("input[value='出勤']")).click();

		pageLoadTimeout(60);
		assertEquals("勤怠情報変更｜LMS", webDriver.getTitle());

		// 比較用の要素を取得
		String now = webDriver.findElement(By.id("now")).getText();
		String startTime = webDriver.findElement(By.xpath("//tr[contains(., '2026年3月25日(水)')]/td[3]")).getText();
		assertTrue(now.contains(startTime));
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 「退勤」ボタンを押下し退勤時間を登録")
	void test05() {

		// 退勤ボタンを押下
		webDriver.findElement(By.cssSelector("input[value='退勤']")).click();

		pageLoadTimeout(60);
		assertEquals("勤怠情報変更｜LMS", webDriver.getTitle());

		// 比較用の要素を取得
		String now = webDriver.findElement(By.id("now")).getText();
		String endTime = webDriver.findElement(By.xpath("//tr[contains(., '2026年3月25日(水)')]/td[4]")).getText();
		assertTrue(now.contains(endTime));
		getEvidence(new Object() {
		});
	}

}
