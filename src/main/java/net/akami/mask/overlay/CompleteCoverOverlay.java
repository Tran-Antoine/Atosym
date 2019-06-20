package net.akami.mask.overlay;

/**
 * Marking interface describing an overlay which does not require brackets when displaying, regardless of
 * the encapsulated value. <br>
 * Math functions are a good example : {@code 4+x} does not need brackets when encapsulated
 * in {@code sin}, otherwise it would result in {@code sin((4+x))}. Fractions, however, are not complete cover overlays.
 * {@code (4+x)/2} obviously does not equal {@code 4+x/2}.
 * 
 */
public interface CompleteCoverOverlay extends ExpressionOverlay {
}
