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
import org.openqa.selenium.support.ui.Select;

/**
 * 結合テスト 勤怠管理機能
 * ケース12
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース12 受講生 勤怠直接編集 入力チェック")
public class Case12 {

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
	@DisplayName("テスト05 不適切な内容で修正してエラー表示：出退勤の（時）と（分）のいずれかが空白")
	void test05() {

		// 出勤時に値を入力して更新ボタンを押下
		new Select(webDriver.findElement(By.id("startHour0"))).selectByIndex(10);
		scrollBy("400");
		webDriver.findElement(By.cssSelector("input[value='更新']")).click();

		pageLoadTimeout(60);
		assertEquals("勤怠情報変更｜LMS", webDriver.getTitle());
		assertEquals("* 出勤時間が正しく入力されていません。", webDriver.findElement(By.className("error")).getText());
		assertTrue(
				webDriver.findElement(By.id("startMinute0")).getAttribute("class").contains("errorInput"));
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(6)
	@DisplayName("テスト06 不適切な内容で修正してエラー表示：出勤が空白で退勤に入力あり")
	void test06() {

		// 出勤時と退勤時分に値を入力して更新ボタンを押下
		new Select(webDriver.findElement(By.id("startHour0"))).selectByIndex(0);
		new Select(webDriver.findElement(By.id("endHour0"))).selectByIndex(19);
		new Select(webDriver.findElement(By.id("endMinute0"))).selectByIndex(1);
		scrollBy("400");
		webDriver.findElement(By.cssSelector("input[value='更新']")).click();

		pageLoadTimeout(60);
		assertEquals("勤怠情報変更｜LMS", webDriver.getTitle());
		assertEquals("* 出勤情報がないため退勤情報を入力出来ません。", webDriver.findElement(By.className("error")).getText());
		assertTrue(
				webDriver.findElement(By.id("startHour0")).getAttribute("class").contains("errorInput"));
		assertTrue(
				webDriver.findElement(By.id("startMinute0")).getAttribute("class").contains("errorInput"));
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(7)
	@DisplayName("テスト07 不適切な内容で修正してエラー表示：出勤が退勤よりも遅い時間")
	void test07() {

		// 出勤時と退勤時分に値を入力して更新ボタンを押下
		new Select(webDriver.findElement(By.id("startHour0"))).selectByIndex(10);
		new Select(webDriver.findElement(By.id("startMinute0"))).selectByIndex(1);
		new Select(webDriver.findElement(By.id("endHour0"))).selectByIndex(9);
		new Select(webDriver.findElement(By.id("endMinute0"))).selectByIndex(1);
		scrollBy("400");
		webDriver.findElement(By.cssSelector("input[value='更新']")).click();

		pageLoadTimeout(60);
		assertEquals("勤怠情報変更｜LMS", webDriver.getTitle());
		assertEquals("* 退勤時刻[0]は出勤時刻[0]より後でなければいけません。", webDriver.findElement(By.className("error")).getText());
		assertTrue(
				webDriver.findElement(By.id("endHour0")).getAttribute("class").contains("errorInput"));
		assertTrue(
				webDriver.findElement(By.id("endMinute0")).getAttribute("class").contains("errorInput"));
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(8)
	@DisplayName("テスト08 不適切な内容で修正してエラー表示：出退勤時間を超える中抜け時間")
	void test08() {

		// 出勤時と退勤時分に値を入力して更新ボタンを押下
		new Select(webDriver.findElement(By.id("endHour0"))).selectByIndex(11);
		new Select(webDriver.findElement(By.id("endMinute0"))).selectByIndex(1);
		new Select(webDriver.findElement(
				By.name("attendanceList[0].blankTime"))).selectByIndex(8);
		scrollBy("400");
		webDriver.findElement(By.cssSelector("input[value='更新']")).click();

		pageLoadTimeout(60);
		assertEquals("勤怠情報変更｜LMS", webDriver.getTitle());
		assertEquals("* 中抜け時間が勤務時間を超えています。", webDriver.findElement(By.className("error")).getText());
		assertTrue(
				webDriver.findElement(By.name("attendanceList[0].blankTime"))
						.getAttribute("class").contains("errorInput"));
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(9)
	@DisplayName("テスト09 不適切な内容で修正してエラー表示：備考が100文字超")
	void test09() {

		// 100文字超の入力値を作成
		StringBuilder inputText = new StringBuilder();
		for (int i = 0; i < 5; i++) {
			inputText.append(
					"あいうえおかきくけこさしすせそたちつてと");
		}
		inputText.append("あ");
		// 出勤時と退勤時分に値を入力して更新ボタンを押下
		new Select(webDriver.findElement(By.id("endHour0"))).selectByIndex(19);
		new Select(webDriver.findElement(By.id("endMinute0"))).selectByIndex(1);
		new Select(webDriver.findElement(
				By.name("attendanceList[0].blankTime"))).selectByIndex(0);
		webDriver.findElement(By.name("attendanceList[0].note")).sendKeys(inputText);
		scrollBy("400");
		webDriver.findElement(By.cssSelector("input[value='更新']")).click();

		pageLoadTimeout(60);
		assertEquals("勤怠情報変更｜LMS", webDriver.getTitle());
		assertEquals("* 備考の長さが最大値(100)を超えています。", webDriver.findElement(By.className("error")).getText());
		assertTrue(
				webDriver.findElement(By.name("attendanceList[0].note"))
						.getAttribute("class").contains("errorInput"));
		getEvidence(new Object() {
		});
	}

}
