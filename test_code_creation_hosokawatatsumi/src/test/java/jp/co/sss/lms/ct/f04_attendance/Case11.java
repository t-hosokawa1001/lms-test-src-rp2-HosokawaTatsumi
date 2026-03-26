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
import org.openqa.selenium.WebElement;

/**
 * 結合テスト 勤怠管理機能
 * ケース11
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース11 受講生 勤怠直接編集 正常系")
public class Case11 {

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
	@DisplayName("テスト04 「勤怠情報を直接編集する」リンクから勤怠情報直接変更画面に遷移")
	void test04() {

		// 勤怠情報を直接編集するリンクを押下
		webDriver.findElement(By.linkText("勤怠情報を直接編集する")).click();

		pageLoadTimeout(60);
		assertEquals("勤怠情報変更｜LMS", webDriver.getTitle());
		assertNotNull(webDriver.findElement(By.xpath("//button[contains(text(), '定時')]")));
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 すべての研修日程の勤怠情報を正しく更新し勤怠管理画面に遷移")
	void test05() {

		// 全ての定時ボタンを押下して更新ボタンを押下
		for (WebElement button : webDriver.findElements(By.xpath("//button[contains(text(), '定時')]"))) {
			button.click();
		}
		scrollBy("250");
		webDriver.findElement(By.cssSelector("input[value='更新']")).click();

		pageLoadTimeout(60);
		assertEquals("勤怠情報変更｜LMS", webDriver.getTitle());
		for (WebElement startTime : webDriver.findElements(By.xpath("//tbody/tr/td[3]"))) {
			assertEquals("09:00", startTime.getText());
		}
		for (WebElement endTime : webDriver.findElements(By.xpath("//tbody/tr/td[4]"))) {
			assertEquals("18:00", endTime.getText());
		}
		getEvidence(new Object() {
		});
	}

}
