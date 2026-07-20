package com.hdplatform.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

class PlatformArchitectureTest {

    private static final JavaClasses PLATFORM_CLASSES = new ClassFileImporter()
            .importPackages("com.hdplatform");

    @Test
    void domain_must_remain_independent_from_frameworks_and_delivery_mechanisms() {
        ArchRule rule = noClasses()
                .that().resideInAPackage("..domain..")
                .should().dependOnClassesThat().resideInAnyPackage(
                        "org.springframework..",
                        "jakarta.persistence..",
                        "jakarta.servlet..",
                        "io.swagger..",
                        "lombok..");

        rule.check(PLATFORM_CLASSES);
    }

    @Test
    void application_layer_must_not_depend_on_inbound_or_outbound_adapters() {
        ArchRule rule = noClasses()
                .that().resideInAPackage("..application..")
                .should().dependOnClassesThat().resideInAPackage("..adapter..");

        rule.check(PLATFORM_CLASSES);
    }

    @Test
    void domain_layer_must_not_depend_on_application_or_adapters() {
        ArchRule rule = noClasses()
                .that().resideInAPackage("..domain..")
                .should().dependOnClassesThat().resideInAnyPackage(
                        "..application..",
                        "..adapter..");

        rule.check(PLATFORM_CLASSES);
    }

    @Test
    void platform_modules_must_not_form_dependency_cycles() {
        slices().matching("com.hdplatform.modules.(*)..")
                .should().beFreeOfCycles()
                .check(PLATFORM_CLASSES);
    }
}
