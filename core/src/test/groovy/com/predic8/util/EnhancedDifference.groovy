package com.predic8.util

import com.predic8.soamodel.Difference

/**
 * Some utility methods for finding specific Differences in the compare result.
 *
 * Usage:
 * use(EnhancedDifference) {
 *  def difference = ...
 *  difference.find...
 * }
 *
 */
class EnhancedDifference {

    static boolean existsByDescription(Difference self, regex) {
        findByDescription(self, regex) as boolean
    }

    static List<Difference> findByDescription(Difference self, regex) {
        ((self?.description ==~ regex) ? [self] : []) +
                self?.diffs?.collect { it.findByDescription(regex) }?.flatten()
    }

    static Difference findUniqueByDescription(Difference self, regex) {
        def diffs = self?.findByDescription(regex) ?: []
        if (diffs.size() > 1) {
            throw new AssertionError("Expected max one Difference for $regex")
        }
        //noinspection GroovyAssignabilityCheck
        diffs ? diffs.first() : null
    }

    // Should be a static method on Difference instead of an instance method, but
    // not possible with Groovy Categories
    static boolean existsByDescription(Difference self, List<Difference> diffs, regex) {
        diffs.collect { it?.exisexistsByDescription(regex) }.find { it }
    }

    static List<Difference> findByDescription(Difference self, List<Difference> diffs, regex) {
        diffs.collect { it?.findByDescription(regex) }.flatten() as List<Difference>
    }

    // Should be a static method on Difference instead of an instance method, but
    // not possible with Groovy Categories
    static Difference findUniqueByDescription(Difference self, List<Difference> diffs, regex) {
        def found = diffs.collect { it?.findByDescription(regex) }.findAll { it }.flatten()
        if (found.size() > 1) {
            throw new AssertionError("Expected max one Difference for $regex")
        }
        //noinspection GroovyAssignabilityCheck
        found ? found.first() : null
    }

}
