package com.denysiuk.dental;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {

        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("com.denysiuk.dental");

        noClasses()
            .that()
                .resideInAnyPackage("com.denysiuk.dental.service..")
            .or()
                .resideInAnyPackage("com.denysiuk.dental.repository..")
            .should().dependOnClassesThat()
                .resideInAnyPackage("..com.denysiuk.dental.web..")
        .because("Services and repositories should not depend on web layer")
        .check(importedClasses);
    }
}
