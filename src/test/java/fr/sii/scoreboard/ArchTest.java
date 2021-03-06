package fr.sii.scoreboard;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {
        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("fr.sii.scoreboard");

        noClasses()
            .that()
            .resideInAnyPackage("fr.sii.scoreboard.service..")
            .or()
            .resideInAnyPackage("fr.sii.scoreboard.repository..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("..fr.sii.scoreboard.web..")
            .because("Services and repositories should not depend on web layer")
            .check(importedClasses);
    }
}
