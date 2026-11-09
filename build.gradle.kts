plugins {
	java
	jacoco
}

repositories {
	mavenCentral()
}

sourceSets {
	main {
		java.setSrcDirs(listOf("src"))
		resources.setSrcDirs(emptyList<String>())
	}
	test {
		java.setSrcDirs(listOf("test"))
		resources.setSrcDirs(emptyList<String>())
	}
}

dependencies {
	testImplementation(platform(libs.junit.bom))
	testImplementation(libs.junit.jupiter)
	testRuntimeOnly(libs.junit.platform.launcher)
}

tasks.withType<JavaCompile>().configureEach {
	options.encoding = "UTF-8"
	if (name != "compileCompat17") options.release = 24
}

// Gradle の Test タスクは既定で -ea を付けて実行する
tasks.test {
	useJUnitPlatform {
		excludeTags("slow")
	}
	finalizedBy(tasks.jacocoTestReport)
}

jacoco {
	toolVersion = libs.versions.jacoco.get()
}

// 網羅率は src/ のクラスだけを集計する
tasks.jacocoTestReport {
	dependsOn(tasks.test)
	reports {
		html.required = true
		xml.required = true
	}
}

// 時間計測を歪めないよう、slow タグのテストは -ea なしで実行する
tasks.register<Test>("slowTest") {
	description = "最大制約の入力で実行時間を検査する"
	group = "verification"
	testClassesDirs = sourceSets.test.get().output.classesDirs
	classpath = sourceSets.test.get().runtimeClasspath
	enableAssertions = false
	useJUnitPlatform {
		includeTags("slow")
	}
}

val compileCompat17 = tasks.register<JavaCompile>("compileCompat17") {
	description = "lib.io.compat17 の Java 17 互換性を検査する"
	group = "verification"
	source = fileTree("src/lib/io/compat17")
	classpath = files()
	destinationDirectory = layout.buildDirectory.dir("compat17")
	options.release = 17
}

tasks.check {
	dependsOn(compileCompat17)
}
