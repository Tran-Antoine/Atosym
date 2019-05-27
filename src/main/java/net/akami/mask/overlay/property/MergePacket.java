package net.akami.mask.overlay.property;

/**
 * Empty marking interface, designed for classes containing data to transfer from the isApplicable method
 * to the merge method. In fact, the isApplicable must constructs results to check whether the property is applicable.
 * These results are most of the time very useful for merge behaviors, therefore they will be stored inside a MergePacket
 * so that they don't need to be reconstructed. <p>
 *
 * See {@link BaseEquivalenceMultProperty} for a concrete example.
 */
public interface MergePacket { }
