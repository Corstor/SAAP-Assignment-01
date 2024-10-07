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
            .layer("Persistance").definedBy("persistance")
            .layer("Business").definedBy("business")
            .layer("Presentation").definedBy("presentation")

            .whereLayer("Presentation").mayNotBeAccessedByAnyLayer()
            .whereLayer("Business").mayOnlyBeAccessedByLayers("Presentation")
            .whereLayer("Persistance").mayOnlyBeAccessedByLayers("Business");

        ruleDep1.check(importedClasses);
    }
}
