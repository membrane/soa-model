package com.predic8.schema.diff

import com.predic8.schema.SchemaParser
import com.predic8.soamodel.Difference
import com.predic8.util.EnhancedDifference
import com.predic8.xml.util.ClasspathResolver

class SchemaWithElementRefsDiffGeneratorTest extends GroovyTestCase {

    def refConstructV1
    def refConstructV2

    void setUp() {
        def parser = new SchemaParser(resourceResolver: new ClasspathResolver())
        refConstructV1 = parser.parse("/diff/ref-construct/RefConstructV1.xsd")
        refConstructV2 = parser.parse("/diff/ref-construct/RefConstructV2.xsd")
    }

    /**
     * Tests that when a ref changes to another element that is of the same complex type
     * as the original element the difference is still detected.
     */
    void testDiffForRefToDifferentElementOfSameType() {
        def differences = compare(refConstructV1, refConstructV2)

        use (EnhancedDifference) {
            Difference topLevelDiff = new Difference().findUniqueByDescription(differences, /.*PostpaidComplexType.*/)

            assertNotNull(topLevelDiff)
            assertNotNull(topLevelDiff.findUniqueByDescription(/.*AlternativeEnterpriseNumber.*added.*/))
            assertNotNull(topLevelDiff.findUniqueByDescription(/.*EnterpriseNumber.*removed.*/))
        }
    }

    void testDiffForRefToDifferentElementOfDifferentType() {
        def differences = compare(refConstructV1, refConstructV2)

        use (EnhancedDifference) {
            Difference topLevelDiff = new Difference().findUniqueByDescription(differences, /.*PrepaidComplexType.*/)

            assertNotNull(topLevelDiff)
            assertNotNull(topLevelDiff.findUniqueByDescription(/.*EnterpriseIdentification.*added.*/))
            assertNotNull(topLevelDiff.findUniqueByDescription(/.*EnterpriseNumber.*removed.*/))
        }
    }

    void testDiffForRefChangeInsideChoice() {
        def differences = compare(refConstructV1, refConstructV2)

        use (EnhancedDifference) {
            Difference topLevelDiff = new Difference().findUniqueByDescription(differences, /.*AnotherComplexType.*/)

            assertNotNull(topLevelDiff)
            assertNotNull(topLevelDiff.findUniqueByDescription(/.*AlternativeEnterpriseNumber.*added.*/))
            assertNotNull(topLevelDiff.findUniqueByDescription(/.*AlternativeEnterpriseIdentification.*added.*/))
            assertNotNull(topLevelDiff.findUniqueByDescription(/.*EnterpriseNumber.*removed.*/))
            assertNotNull(topLevelDiff.findUniqueByDescription(/.*EnterpriseIdentification.*removed.*/))
        }
    }

    void testDiffForChangesInSubtreeOfTypes() {
        def differences = compare(refConstructV1, refConstructV2)

        use (EnhancedDifference) {
            Difference topLevelDiff = new Difference().findUniqueByDescription(differences, /.*DeepComplex.*/)

            assertNotNull(topLevelDiff)
            assertNotNull(topLevelDiff.findUniqueByDescription(/.*\}EnterpriseNumber.*removed.*/))
            assertNotNull(topLevelDiff.findUniqueByDescription(/.*AlternativeEnterpriseNumber.*removed.*/))
            assertNotNull(topLevelDiff.findUniqueByDescription(/.*\}EnterpriseIdentification.*added.*/))
            assertNotNull(topLevelDiff.findUniqueByDescription(/.*AlternativeEnterpriseIdentification.*added.*/))
        }
    }

    private def compare(a, b) {
        new SchemaDiffGenerator(a: a, b: b).compare()
    }

}
