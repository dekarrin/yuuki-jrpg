package yuuki.ui;

/**
 * Thrown when a method is frozen and a method is called which requires it to
 * not be frozen.
 */
@SuppressWarnings("serial")
public class FrozenException extends RuntimeException {}
