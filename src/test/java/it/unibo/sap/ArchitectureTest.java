package it.unibo.sap;

import org.junit.jupiter.api.Test;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;


public class ArchitectureTest {
    @Test
    public void layeredRule() {
        JavaClasses importedClasses = new ClassFileImporter().importPackages("layered");

        ArchRule ruleDep1 = layeredArchitecture().consideringAllDependencies()
            .layer("Database").definedBy("..database..")
            .layer("Persistence").definedBy("..persistence..")
            .layer("Business").definedBy("..business..")
            .layer("Presentation").definedBy("..presentation..")

            .whereLayer("Presentation").mayNotBeAccessedByAnyLayer()
            .whereLayer("Business").mayOnlyBeAccessedByLayers("Presentation")
            .whereLayer("Persistence").mayOnlyBeAccessedByLayers("Business")
            .whereLayer("Database").mayOnlyBeAccessedByLayers("Persistence");

        ruleDep1.check(importedClasses);
    }
}
