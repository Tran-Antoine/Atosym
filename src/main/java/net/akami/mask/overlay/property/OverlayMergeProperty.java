package net.akami.mask.overlay.property;

// MergeBehavior is a pattern applied to all elements, whereas overlaymergeproperty can be applied or not.
// Maybe use a list of mergebehavior in the merge class ?
public interface OverlayMergeProperty {

    boolean requiresStartingOver();

}
