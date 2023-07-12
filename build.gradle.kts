import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jooq.meta.jaxb.Logging

plugins {
    kotlin("jvm") version "1.9.0"
    id("nu.studer.jooq") version "8.2"
    application
}

group = "dev.limelier"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://jitpack.io/")
}

dependencies {
    // discord
    implementation("net.dv8tion:JDA:5.0.0-beta.12")
    implementation("com.github.minndevelopment:jda-ktx:0.10.0-beta.1")

    // dependency injection
    implementation("org.kodein.di:kodein-di:7.20.2")

    // configuration
    implementation("com.sksamuel.hoplite:hoplite-core:2.8.0.RC1")
    implementation("com.sksamuel.hoplite:hoplite-toml:2.8.0.RC1")

    // database
    jooqGenerator("org.postgresql:postgresql:42.5.4")
    implementation("org.postgresql:postgresql:42.5.4")

    // logging
    implementation("org.slf4j:slf4j-api:2.0.7")
    implementation("org.slf4j:slf4j-simple:2.0.7")
    implementation("io.github.oshai:kotlin-logging-jvm:5.0.0-beta-02")

    // test
    testImplementation(kotlin("test"))
}

jooq {
    version.set("3.18.5")

    configurations {
        create("main") {
            jooqConfiguration.apply {
                logging = Logging.WARN
                jdbc.apply {
                    driver = "org.postgresql.Driver"
                    url = "jdbc:postgresql://localhost:5432/pal-machine"
                    user = "postgres"
                    password = "postgres"
                }
                generator.apply {
                    name = "org.jooq.codegen.KotlinGenerator"
                    generateSchemaSourceOnCompilation.set(false)
                    database.apply {
                        name = "org.jooq.meta.postgres.PostgresDatabase"
                        inputSchema = "public"
                    }
                    target.apply {
                        packageName = "dev.limelier"
                        directory = "src/generated/jooq"
                    }
                    strategy.name = "org.jooq.codegen.DefaultGeneratorStrategy"
                }
            }
        }
    }
}

tasks {
    test {
        useJUnitPlatform()
    }
    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "19"
    }
    withType<JavaCompile> {
        targetCompatibility = "19"
    }
}

application {
    mainClass.set("dev.limelier.palmachine.MainKt")
}
