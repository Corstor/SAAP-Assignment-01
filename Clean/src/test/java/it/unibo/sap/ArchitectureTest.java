package it.unibo.sap;

import org.junit.jupiter.api.Test;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;


public class ArchitectureTest {
    @Test
    public void cleanRule() {
        JavaClasses importedClasses = new ClassFileImporter().importPackages("clean");

        ArchRule ruleDep1 = noClasses().that().resideInAPackage("..domain..")
            .should().dependOnClassesThat().resideInAPackage("..application..")
            .orShould().dependOnClassesThat().resideInAPackage("..infrastructure..");


        ArchRule ruleDep2 = noClasses().that().resideInAPackage("..application..")
            .should().dependOnClassesThat().resideInAPackage("..infrastructue..");

        ruleDep1.check(importedClasses);

        ruleDep2.check(importedClasses);
    }
}
